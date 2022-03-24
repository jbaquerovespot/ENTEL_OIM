package entel.oim.plugins.scheduler;

import java.io.FileInputStream;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import entel.oim.connectors.utilities.SuccessFactorsConnection;
import oracle.iam.certification.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.entitymgr.vo.SearchCriteria.Operator;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;
import oracle.iam.reconciliation.utils.Sys;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.RequestEntity;
import oracle.iam.request.vo.RequestStage;
import oracle.iam.scheduler.vo.TaskSupport;


/**
 * Execute the delete trusted job for SuccessFactors Connector
 * @author Oracle
 *
 */
public class TrustedUserDeleteReconciliationTask extends TaskSupport {
	
	private final static String className = TrustedUserDeleteReconciliationTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	private final ReconOperationsService rosrv = Platform.getService(ReconOperationsService.class);
    private final RequestService rsrv = Platform.getService(RequestService.class);
    private final UserManager umgr = Platform.getService(UserManager.class);
    private final NotificationService nsrv = Platform.getService(NotificationService.class);
    private final RoleManager rmgr = Platform.getService(RoleManager.class);
    private final tcITResourceInstanceOperationsIntf itResOps = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    private final ArrayList<String> managers = new ArrayList<>();
    private final Properties props = new Properties();
    private final Date auditJobTime = Calendar.getInstance().getTime();
	
	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);

		logger.finer("Loading properties file");
		props.load(new FileInputStream(Sys.getProperty("Entel.PropertiesFileLoc")));
		
		String filter = (String) hm.get("Filter");
		String itResource = (String) hm.get("IT Resource Name");
		String resObject = (String) hm.get("Resource Object Name");
		String obType = (String) hm.get("Object Type");
		String origin = (String) hm.get("Origin");
		String usersFilter = (String) hm.get("Users Filter");
		String canditateFilter = (String) hm.get("Candidate Filter");
		String usersFilterCheck = (String) hm.get("Users Filter Check");
		String canditateFilterCheck = (String) hm.get("Candidate Filter Check");
		String maxUsers = (String) hm.get("Max Users");
		String allowDeleteMaxUsers = (String) hm.get("Allow Delete Max Users");
		boolean isDeleteUsersFlagOn = Boolean.parseBoolean((String) hm.get("Delete Users"));
		boolean isAuditFlagOn = Boolean.parseBoolean((String) hm.get("Audit Users"));
		
		logger.log(Level.FINER, "Getting details of IT Resource [{0}]", itResource);
		Map<String, String> itResourceDetails = getITResourceDetails(itResource);

		logger.finer("Finding differential of deleted users");
		Set<String[]> toDelete = findDifferential(itResourceDetails, filter, itResource, resObject, origin, usersFilter, canditateFilter, isAuditFlagOn);
		ArrayList<String[]> notifyUser = new ArrayList<>();
		ArrayList<String[]> notifyUserFailed = new ArrayList<>();

		if (isStop()) {
		    logger.log(Level.FINE, "Interrupted JOB");
		    return;
		}
		
		logger.finer("Performing new second check for selected delete users");
		Set<String[]> toDeleteFinally = performDoubleCheckUsers(itResourceDetails, filter, itResource, resObject, origin, usersFilterCheck, canditateFilterCheck, isAuditFlagOn,toDelete);
		
		
		logger.finer("Checking is users to delete is more than allowed ");
		if (toDeleteFinally != null 
				&& toDeleteFinally.size() > Integer.parseInt(maxUsers)) {
		
			logger.finer("Sending notification with massive users deletions");
			notifyMassiveDeletion(toDeleteFinally);
			
			if (!Boolean.parseBoolean(allowDeleteMaxUsers)) {
				logger.severe("Massive users deletion is not allowed");
				return;
			}
					
		}

		logger.finer("CHecking if delete flag is TRUE: " + isDeleteUsersFlagOn);
		if (isDeleteUsersFlagOn) {
			
			logger.finer("It is true the delete, looping over users to delete");
			for (String[] user : toDeleteFinally) {
			    String usrKey = user[0];
			    String usrLogin = user[1];
			    String usrRUT = user[2];
			    String usrPersonId = user[3];
	
			    if (isStop()) {
					logger.log(Level.FINE, "Interrupted JOB");
					return;
			    }
	
				// Remove the manager from the deleted user
				User uo = new User(usrKey);
				uo.setAttribute(UserManagerConstants.AttributeName.MANAGER_KEY.getId(), null);
	
				umgr.modify(uo);
				logger.finer("Removed manager of user [" + usrLogin + "]");
	
			    logger.finer("Making the request to delete user: " + usrLogin);
			    String reqId = requestUserDeletion(usrKey, usrLogin);
	
			    if (reqId != null) {
			    	//List of deleted users.
			    	notifyUser.add(user);
			    	
			    	Map<String, Object> reconData = new HashMap<>();
					reconData.put("Person Id", usrPersonId);
					reconData.put("Username", usrLogin);
					reconData.put("RUT", usrRUT);
					
					logger.finer("Creating Event for Audit");            
					EventAttributes ea = new EventAttributes(true, rosrv.getDefaultDateFormat(), ChangeType.DELETE, null);
					long rEvKey = rosrv.createReconciliationEvent(resObject, reconData, ea);
		
					//No se procesa el evento ya que se usa la solicitud anterior
					//Si se usa el evento no pasa por el periodo de gracia en la eliminación
					//rosrv.processReconciliationEvent(rEvKey);
					
					logger.finer("Closing Event");
					rosrv.closeReconciliationEvent(rEvKey);
					
					logger.log(Level.FINER, "Request ID [{1}] for deletion of user [{0}]. Reconcilliation event Key [{2}]",
					        new Object[] { usrLogin, reqId, rEvKey });
			    } else {
			    	//List of not deleted users. 
			    	notifyUserFailed.add(user);
			    }
			}
			
			// Send mail of users that tried to be deleted
			logger.fine("Calling the procedure to notify the users that tried to be deleted");
			notifyUsers(notifyUser,notifyUserFailed);

			// Disable all managers that tried to be deleted
			disableManagers();

		} else {
			// Delete flag if False. Send notifiction with all the users to delete
			logger.fine("Calling the procedure to notify the users that will be deleted");
			notifyUser.addAll(toDeleteFinally);
			notifyUsers(notifyUser,notifyUserFailed);

		}
		

	}
	
	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
	
	
	/**
	 * Return the user id list of employee of Success Factors
	 * @param xmlUsers XML response from service User of Success Factors
	 * @param itResourceDetails The it resource details
	 * @param accessToken Token to call the service
	 * @param isAuditFlagOn Flag to indicate save audit
	 * @return The user id list (rut) of employee of Success Factors
	 * @throws Exception 
	 */
	public List<String> getUsersIdList (String xmlUsers, Map<String, String> itResourceDetails, String accessToken, boolean isAuditFlagOn) throws Exception {
		
		logger.entering(className, "getUsersIdList");
		List<String> usersIdList = new ArrayList<String>();
		
		try {
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUsers));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			if (list.size() == 0) {
				logger.severe("Not a valid response from SuccessFactors: " + xmlUsers);
				throw new Exception("Not a valid response from SuccessFactors: " + xmlUsers);
			}
			
			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the userId of the current user");
					Element entry = (Element )list.get(i);
					
					logger.finer("Looping over all link attributes");
			        List<Element> linkElements= (List<Element>) entry.elements("link"); 
			        for (Element link : linkElements) {
			        	logger.finer("Checking if inline found");
			        	Element inline = link.element("inline");
			        	if ( inline != null) {
			        		String title= link.attributeValue("title");
			        		logger.finest("Inline attribute found: " + title);
				        	
			        		logger.finer("Getting person id of the user");
			        		Element detPerson = inline.element("entry");
			        		Element content = (Element) detPerson.element("content");
					        Element properties = (Element) content.element("properties");
					        Element personId = (Element) properties.element("personId");
					        Element personIdExternal = (Element) properties.element("personIdExternal");

			        		logger.finer("Checking if audit is on to save the total xml output");
							if (isAuditFlagOn) {
								insertAuditUserDelete(personIdExternal.getStringValue(), personId.getStringValue(),entry.asXML());
							}
					        
							logger.finest("Adding the personId: \"" + personId.getStringValue()+ "\" to the list");
							usersIdList.add(personId.getStringValue());
					        break;
				        	
				        }
				        
			        }
			        
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsersIdList - Unexpected error" ,e);
			throw e;
			
		}
		
		
		logger.exiting(className, "getUsersIdList");
		return usersIdList;
		
	}
	
	
	
	
	/**
	 * Return the person id of employee of Success Factors
	 * @param xmlPerson XML response from service User of Success Factors
	 * @return The person id list (PersonId) of employee of Success Factors
	 * @throws Exception 
	 */
	public static String getPersonId (String xmlPerson) throws Exception {
		
		logger.entering(className, "getPersonId");
		String personId = null;
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlPerson));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the userId of the current user");
					Element entry = (Element )list.get(i);
			        Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element personIdElement = (Element) properties.element("personId");
			        
			        personId = personIdElement.getStringValue();
			        logger.finest("Person Id found: " + personId);
			        
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getPersonId - Unexpected error" ,e);
			throw e;
		}
		
		
		logger.exiting(className, "getPersonId");
		return personId;
		
	}
	
	
	
	/**
     * Perform a second check for the users to be deleted
     * 
     * @param itResourceDetails
     *            the it resource details
     * @param filter
     *            the filter of the user to delete
     * @param itResource
     *            the name of the it resource
     * @param roName
     *            the resource object name
     * @param origin
     *            the origin of the reconciliation
     * @param usersFilter 
     * 			  Filter to find users
     * @param canditateFilter 
     * 			  Filter to find candidates
     * @param isAuditFlagOn 
     * 			  Flag to indicate save audit           
     * @param toDeleteUsers
     * 			  Set of users original marked to delete
     * @return 	  
     * 			  A set of user data to delete
     * @throws Exception
     *             if something goes wrong.
     */
    private Set<String[]> performDoubleCheckUsers(Map<String, String> itResourceDetails, String filter, String itResource,
            String roName, String origin, String usersFilter, String candidateFilter, boolean isAuditFlagOn, Set<String[]> toDeleteUsers) throws Exception {

    	logger.entering(className, "performDoubleCheckUsers");
    	Set<String[]> toDeleteFinally = new HashSet<>();

		try {
		    logger.fine("Looping over the users original to delete");
			for (String[] ssffUser : toDeleteUsers) {
	
		    	String usrRUT = ssffUser[2];
			    String usrPersonId = ssffUser[3];
			    logger.finest("Looping over usrRUT:" + usrRUT + " | usrPersonId:" +usrPersonId);
				
			    logger.fine("Getting XML of the user");
			    String xmlUserResult = getXmlResult(itResourceDetails, itResource, usersFilter, isAuditFlagOn, usrRUT, usrPersonId);   
			    String xmlCandidateResult = getXmlResult(itResourceDetails, itResource, candidateFilter, isAuditFlagOn, usrRUT, usrPersonId);   
				
			    logger.fine("Getting the status of the user");
			    String usrStatus = getUserStatus(xmlUserResult);
			    logger.finest("Status: " + usrStatus);
			    
			    logger.fine("Getting the start date of the candidate");
			    String candidateStartDate = getCandidateStartDate(xmlCandidateResult);
			    logger.finest("candidateStartDate: " + candidateStartDate);
			    
			    logger.finer("Chekcing if the user is disable and is not a candidate employee");
			    if ( candidateStartDate == null && 
			           ( (usrStatus == null) || 
			             (usrStatus != null && usrStatus.equalsIgnoreCase("f"))
			           )) {
			    	logger.finer("Adding to the final list");
				    toDeleteFinally.add(ssffUser);
			    }
			    
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "unexpected Error - performDoubleCheckUsers",e);
			throw e;
		
		}
	
		logger.log(Level.INFO, "[{0}] {1} to delete from {2}", new Object[] { toDeleteFinally.size(), roName, itResource });
	
		if (logger.isLoggable(Level.FINER)) {
		    logger.log(Level.FINER, "[{0}] {1} to delete: {2}",
		            new Object[] { toDeleteFinally.size(), roName, Arrays.deepToString(toDeleteFinally.toArray()) });
		}

		logger.exiting(className, "performDoubleCheckUsers",toDeleteFinally);
		return toDeleteFinally;
    }

    
    /**
     * Finds the accounts to be deleted from OIM.
     * 
     * @param itResourceDetails
     *            the it resource details
     * @param filter
     *            the filter of the user to delete
     * @param itResource
     *            the name of the it resource
     * @param roName
     *            the resource object name
     * @param origin
     *            the origin of the reconciliation
     * @param usersFilter 
     * 			  Filter to find users
     * @param canditateFilter 
     * 			  Filter to find candidates
     * @param isAuditFlagOn 
     * 			  Flag to indicate save audit           
     * @return 
     * 			  A set of user data to delete
     * @throws Exception
     *             if something goes wrong.
     */
    private Set<String[]> findDifferential(Map<String, String> itResourceDetails, String filter, String itResource,
            String roName, String origin, String usersFilter, String canditateFilter, boolean isAuditFlagOn) throws Exception {

    	logger.entering(className, "findDifferential");
    	Connection oimdb = null;
    	Set<String[]> toDelete = new HashSet<>();

		try {
		    // Find all the target accounts
		    Set<String> target = findTargetAccounts(itResourceDetails, itResource, roName, usersFilter, canditateFilter, isAuditFlagOn);
	
		    oimdb = Platform.getOperationalDS().getConnection();
		    Set<String[]> oimUsers = findOimUsers(oimdb, origin, filter);
	
		    for (String[] oimUser : oimUsers) {
	
				String usrKey = oimUser[0];
				String usrLogin = oimUser[1];
				String usrRut = oimUser[2];
				String usrPersonId = oimUser[3];
		
				boolean found = false;
				for (String targetUser : target) {
				    if (targetUser.equalsIgnoreCase(usrPersonId)) {
					found = true;
					break;
				    }
				}
		
				if (!found) {
				    long count = requestsPending(oimdb, usrKey);
				    if (count == 0) {
		
					logger.log(Level.FINER, "Adding user [{0}] for deletion request", usrLogin);
		
					String[] user = new String[] { usrKey, usrLogin, usrRut, usrPersonId };
					toDelete.add(user);
				    } else {
					logger.log(Level.FINEST, "Ignoring user [{0}] due to [{1}] pending deletion request(s)",
					        new Object[] { usrLogin, count });
				    }
				}
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "unexpected Error - findDifferential",e);
			if (null != oimdb) {
		    	logger.finer("Closing database connection!");
			    oimdb.close();
			
		    }
			throw e;
		
		} finally {
		    if (null != oimdb) {
		    	logger.finer("Closing database connection!");
			    oimdb.close();
			
		    }
		}
	
		logger.log(Level.INFO, "[{0}] {1} to delete from {2}", new Object[] { toDelete.size(), roName, itResource });
	
		if (logger.isLoggable(Level.FINER)) {
		    logger.log(Level.FINER, "[{0}] {1} to delete: {2}",
		            new Object[] { toDelete.size(), roName, Arrays.deepToString(toDelete.toArray()) });
		}

		logger.exiting(className, "findDifferential");
		return toDelete;
    }
    
    
    /**
     * Finds all the target users of the IT Resource.
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param itResource
     *            the name of the IT Resource.
     * @param roName
     *            the name of the Resource Object
     * @param usersFilter 
     * 			  Filter to find users
     * @param canditateFilter 
     * 			  Filter to find candidates
     * @param isAuditFlagOn 
     * 			  Flag to indicate save audit 
     * @return 
     * 			The found target accounts.
     * @throws Exception 
     */
    private Set<String> findTargetAccounts(Map<String, String> itResourceDetails, String itResource, String roName, String usersFilter, String canditateFilter, boolean isAuditFlagOn) throws Exception {

    	logger.entering(className, "findTargetAccounts");
    	Set<String> target = new HashSet<>();
    	
    	try {
    		
    		logger.finer("Getting Active Users");
	    	Set<String> targetActiveUsers = findActiveUsers(itResourceDetails, itResource, usersFilter, isAuditFlagOn);
	    	target.addAll(targetActiveUsers);
	    	logger.finest("Active users found: " + targetActiveUsers.size());
	    	
	    	logger.finer("Getting Candidates Users");
	    	Set<String> targetCandidatesusers = findActiveUsers(itResourceDetails, itResource, canditateFilter, isAuditFlagOn);
	    	target.addAll(targetCandidatesusers);
	    	logger.finest("Candidates users found: " + targetCandidatesusers.size());
	    	
	    	logger.log(Level.FINE, "Found [{0}] users for [{1}]", new Object[] { target.size(), itResource });
			
    	} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "findTargetAccounts - Unexpected error", e);
			throw e;
		}
    	
		logger.exiting(className, "findTargetAccounts", target.size());
		return target;
    }
    
    
    
    /**
     * Finds all the active users of the IT Resource.
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param itResource
     *            the name of the IT Resource.
     * @param reconUrl 
     * 			  Filter to execute reconciliation
     * @param isAuditFlagOn 
     * 			  Flag to indicate save audit 
     * @return 
     * 			The found target accounts.
     * @throws Exception 
     */
    private Set<String> findActiveUsers(Map<String, String> itResourceDetails, String itResource, String reconUrl, boolean isAuditFlagOn) throws Exception {

    	logger.entering(className, "findActiveUsers");
    	Set<String> target = new HashSet<>();
    	int invocation = 1;
    	try {
    	
	    	logger.fine("Call to establish connection with SuccessFactors");
			String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
			logger.finest("Access token: " + accessToken);
			
			logger.finer("Constructing the filter for active users");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'"); 
			Calendar cal = Calendar.getInstance();
	    	cal.add(Calendar.DATE, 1);
	    	
	    	logger.finer("getting default filter for users");
	    	reconUrl = reconUrl.replace("(Date)", sdf.format(cal.getTime()));
	    	
			logger.finer("getting default filter for users");
			String serviceRecon = reconUrl.split("\\?")[0];
			
			logger.fine("Calling the User service from Success Factors");
	    	String usersXML = SuccessFactorsConnection.getServiceResponse( itResourceDetails,reconUrl,accessToken);
			
			logger.finer("Checking if audit is on to save the total xml output");
			if (isAuditFlagOn) {
				insertAuditUsersXML(reconUrl,invocation,usersXML);
			}
			
			logger.fine("Constructing user id list");
			List<String> usersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken, isAuditFlagOn);
	    	target.addAll(usersIdList);
	    	
	    	logger.fine("Checking if exists more results");
			String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(usersXML);
			while (moreCodeValues != null) {
				
				logger.finer("Increase invocation number");
				invocation++;
				
				logger.finest("Extracting the filter for next values");
				String nextResultFilter = moreCodeValues.split(serviceRecon)[1];
				logger.finest("Filter for next values: "+ nextResultFilter);
				
				logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
				usersXML = SuccessFactorsConnection.getServiceResponse(itResourceDetails,serviceRecon+nextResultFilter,accessToken);
				logger.finest("Response from Success Factors: " + usersXML);
				
				logger.finer("Checking if audit is on to save the total xml output");
				if (isAuditFlagOn) {
					insertAuditUsersXML(serviceRecon+nextResultFilter,invocation,usersXML);
				}
				
				logger.fine("Constructing the new user id list");
				List<String> newUsersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken, isAuditFlagOn);
				
				logger.fine("Adding the new values to the original Map");
				target.addAll(newUsersIdList);
				
				logger.fine("Checking if exists more results");
				moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(usersXML);
				
			}
			
			logger.log(Level.FINE, "Found [{0}] users for [{1}]", new Object[] { target.size(), itResource });
			
    	} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "findActiveUsers - Unexpected error", e);
			throw e;
		}
    	
		logger.exiting(className, "findActiveUsers", target.size());
		return target;
    }
    
    
   

    /**
     * Finds the active users in OIM.
     * 
     * @param oimdb
     *            the open OIM DB Connection.
     * @param origin
     *            the usr_udf_origin column value.
     * @param filter
     *            a SQL filter to reduce the number of users returned.
     * @return the found OIM users.
     * @throws SQLException
     */
    private Set<String[]> findOimUsers(Connection oimdb, String origin, String filter) throws SQLException {
	String sql = "select usr_key, usr_login, usr_udf_rut , usr_udf_personid from usr where upper(usr_udf_origin) = ? "
	        + "and usr_status != 'Deleted' and ((usr.usr_data_level!=1 and usr.usr_data_level!=2) "
	        + "or usr.usr_data_level is null) and usr_automatically_delete_on is null";

		logger.entering(className, "findOimUsers");
	
		if (filter != null && !filter.isEmpty()) {
		    sql += " and " + filter;
		}
	
		Set<String[]> users = new HashSet<>();
		PreparedStatement stmt = null;
		try {
		    stmt = oimdb.prepareStatement(sql);
		    stmt.setString(1, origin.toUpperCase());
		    ResultSet rs = stmt.executeQuery();
	
		    if (!rs.isBeforeFirst()) {
			return users;
		    }
	
		    while (rs.next()) {
			if (isStop()) {
			    logger.log(Level.FINE, "Interrupted JOB");
	
			    if (stmt != null) {
				stmt.close();
			    }
	
			    rs.close();
			    return users;
			}
	
			String[] user = new String[4];
			String usrKey = rs.getString("usr_key");
			String usrLogin = rs.getString("usr_login");
			String usrRut = rs.getString("usr_udf_rut");
			String usrPersonId = rs.getString("usr_udf_personid");
	
			user[0] = usrKey;
			user[1] = usrLogin;
			user[2] = usrRut;
			user[3] = usrPersonId;
	
			users.add(user);
		    }
	
		    rs.close();
		} finally {
		    if (stmt != null) {
			stmt.close();
		    }
		}
	
		logger.exiting(className, "findOimUsers");
		return users;
    }
    

    /**
     * Finds the count of deletion requests pending for a specific user.
     * 
     * @param oimdb
     *            the OIM DB open connection
     * @param usrKey
     *            the usr_key of the user to check
     * @return the number of pending deletion requests.
     * @throws Exception
     */
    private long requestsPending(Connection oimdb, String usrKey) throws Exception {
		
    	logger.entering(className, "requestsPending");
    	PreparedStatement rStmt = null;
		long count = 0;
	
		StringBuilder rSql = new StringBuilder("select count(1) from request r , request_entities re, usr ")
		        .append(" where r.request_key = re.request_key and usr.usr_key = re.entity_key and r.request_status in ('")
		        .append(RequestStage.STAGE_AWAITING_APPROVAL).append("','")
		        .append(RequestStage.STAGE_AWAITING_CHILD_REQUEST_COMPLETION).append("','")
		        .append(RequestStage.STAGE_AWAITING_EXECUTION).append("','")
		        .append(RequestStage.STAGE_AWAITING_DEPENDENT_REQUEST_COMPLETION).append("')")
		        .append(" and re.request_entity_type = '" + RequestEntity.USER_ENTITY_TYPE + "' ")
		        .append(" and re.request_entitity_operation = '" + RequestConstants.MODEL_DELETE_OPERATION
		                + "' and usr.usr_key = ?");
	
		try {
	
		    rStmt = oimdb.prepareStatement(rSql.toString());
		    rStmt.setString(1, usrKey);
		    ResultSet cr = rStmt.executeQuery();
	
		    // Should not fail as a select count always returns.
		    cr.next();
		    count = cr.getLong(1);
	
		    cr.close();
		} finally {
		    if (null != rStmt) {
			rStmt.close();
		    }
		}

		logger.exiting(className, "requestsPending");
		return count;
    }

    
    /**
     * Creates the user delete request for a user.
     * 
     * @param usrKey
     *            the user to be deleted
     * @param usrLogin
     *            the login of the user to be deleted
     * @return The request id of the deletion or null if it couldn't be deleted.
     */
    private String requestUserDeletion(String usrKey, String usrLogin) {
	
    	logger.entering(className, "requestUserDeletion");
    	
    	try {
		    List<RequestEntity> entities = new ArrayList<>();
		    RequestEntity entity = new RequestEntity();
		    entity.setEntityKey(usrKey);
		    entity.setRequestEntityType(OIMType.User);
		    entity.setOperation(RequestConstants.MODEL_DELETE_OPERATION);
	
		    entities.add(entity);
	
		    RequestData request = new RequestData();
		    request.setTargetEntities(entities);
		    request.setJustification("Eliminacion de cuenta por reconciliacion desde Success Factors.");
	
		    logger.exiting(className, "requestUserDeletion");
		    return rsrv.submitRequest(request);
		    
		} catch (Exception e) {
		    // Do not stop the task if an exception occurs.
		    logger.log(Level.SEVERE, "requestUserDeletion - Unexpected error", e);
	
		    // Notify if a user to be deleted has subordinates.
		    addToNotificationIfManager(usrKey);
		    
		    logger.exiting(className, "requestUserDeletion");
		    return null;
		}
    }
    
    
    
    /**
     * Disables the managers that tried to be deleted.
     */
    private void disableManagers() {
	try {
	    if (!managers.isEmpty()) {
		ArrayList<String> realDisable = new ArrayList<>();
		for (String mgrKey : managers) {
		    Set<String> mgrDets = new HashSet<>();
		    mgrDets.add(UserManagerConstants.AttributeName.STATUS.getId());
		    User mgr = umgr.getDetails(UserManagerConstants.AttributeName.USER_KEY.getId(), mgrKey, mgrDets);

		    if (!mgr.getStatus().equals(UserManagerConstants.AttributeValues.USER_STATUS_DISABLED.getId())
		            || !mgr.getStatus()
		                    .equals(UserManagerConstants.AttributeValues.USER_STATUS_DISABLED_UNTIL_START_DATE
		                            .getId())) {

			realDisable.add(mgrKey);
		    }
		}
		if (!realDisable.isEmpty()) {
		    logger.fine("Disabling [" + realDisable.size() + "] managers");

		    UserManagerResult res = umgr.disable(realDisable, false);
		    List success = res.getSucceededResults();
		    HashMap<String, String> fail = res.getFailedResults();

		    logger.info("Disabled " + success.size() + " managers successfully: " + success + ". " + fail.size()
		            + " disables failed: " + fail);
		} else {
		    logger.finer("No managers to disable");
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE, "disableManagers - Unexpected error", e);
	}
    }
    
    
    
    /**
     * Adds a user to the notification queue if it is a manager
     * 
     * @param usrKey
     *            the user key to check
     */
    private void addToNotificationIfManager(String usrKey) {
	try {
	    if (hasSubordinates(usrKey)) {
		managers.add(usrKey);
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE, "addToNotificationIfManager - Unexpected error", e);
	}
    }

    /**
     * Indicates whether a user has subordinates or not.
     * 
     * @param usrKey
     *            the usr_key of the user to check
     * @return {@code true} if the user has subordinates. {@code false}
     *         otherwise.
     * @throws Exception
     */
    private boolean hasSubordinates(String usrKey) throws Exception {
	SearchCriteria mgrSc = new SearchCriteria(UserManagerConstants.AttributeName.MANAGER_KEY.getId(), usrKey,
	        Operator.EQUAL);
	SearchCriteria delSc = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
	        UserManagerConstants.AttributeValues.USER_STATUS_DELETED, Operator.NOT_EQUAL);

	SearchCriteria join = new SearchCriteria(mgrSc, delSc, Operator.AND);
	Set<String> usrAttrs = new HashSet<>();
	usrAttrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());

	HashMap<String, Object> sParam = new HashMap<>();
	sParam.put("STARTROW", 0);
	sParam.put("ENDROW", 1);

	List<User> u = umgr.search(join, usrAttrs, sParam);
	return u != null && !u.isEmpty();
    }

 
    
    /**
	 * Sends a notification of the users who tried to be deleted to security
	 * area, operations and access control.
	 * 
	 * @param toDelete Users deleted
	 * @param failedOnes Users NOT deleted
	 */
	private void notifyUsers(ArrayList<String[]> toDelete, ArrayList<String[]> failedOnes) {

		logger.entering(className, "notifyUsers");

		// Construction of table with users to delete
		String html = toConstructHtmlTable(toDelete);
		
		String htmlFailed = toConstructHtmlTable(failedOnes);

		// Construction of template parameters
		HashMap<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("info_html", html.toString());
		templateParams.put("info_htmlF", htmlFailed.toString());

		logger.fine("Begins building list of adressees.");
		String[] adresseeOperaciones = usersLogins("Operaciones");
		String[] adresseeSeguridad = usersLogins("Seguridad");
		String[] adresseeCA = usersLogins("Control Acceso");

		int opLength = adresseeOperaciones.length;
		int seLength = adresseeSeguridad.length;
		int caLength = adresseeCA.length;

		String[] adressee = new String[opLength + seLength + caLength];

		for (int i = 0; i < opLength; i++) {
			adressee[i] = adresseeOperaciones[i];
		}

		for (int i = 0; i < seLength; i++) {
			adressee[opLength + i] = adresseeSeguridad[i];
		}

		for (int i = 0; i < caLength; i++) {
			adressee[opLength + seLength + i] = adresseeCA[i];
		}

		logger.fine("List of adressees build.");

		sendEmailNotification("TrustedUserDeleteReconciliation Alert", templateParams, adressee);

		logger.exiting(className, "notifyUsers");

	}

	/**
	 * Construction of HTML with table of users
	 * 
	 * @param title
	 *            Table's title
	 * @param users
	 *            List of users
	 * @return
	 */
	private String toConstructHtmlTable(ArrayList<String[]> users) {

		logger.entering(className, "toConstructHtmlTable");

		logger.fine("Begins building content for notification.");

		// Construction of table with users without gestor
		StringBuilder html = new StringBuilder("<table border><tr><td><center><b>Login</b></center>"
				+ "</td><td><center><b>RUT</b></center></td></tr>");

		for (String[] user : users) {

			html.append("<tr><td><center>").append(user[1]).append("</center></td>");
			html.append("<td><center>").append(user[2]).append("</center></td></tr>");

		}

		html.append("</table>");

		logger.exiting(className, "toConstructHtmlTable");

		return html.toString();
	}

	/**
	 * Sending email with information to group's addressees
	 * 
	 * @param templateName
	 *            The template name that will be used
	 * @param templateParams
	 *            Parameters to be replaced in the template
	 * @param addressee
	 *            List of addressees
	 */
	private void sendEmailNotification(String templateName, HashMap<String, Object> templateParams,
			String[] addressee) {

		logger.entering(className, "sendEmailNotification", templateName);

		NotificationEvent ev = new NotificationEvent();

		try {

			// Setting template through the property
			ev.setTemplateName(templateName);
			// Loading users
			ev.setUserIds(addressee);
			// Setting params to replace in the template
			ev.setParams(templateParams);

			logger.fine("Sending subordinated manager notification [CheckInfoExtern Alert] to reviewers "
					+ ev.getUserIds().toString());

			nsrv.notify(ev);

		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}
	}

	/**
	 * List of users logins that belong to a group
	 * 
	 * @param groupName
	 *            Group name to retrieve users
	 * @return
	 */
	private String[] usersLogins(String groupName) {

		logger.entering(className, "usersLogins", groupName);

		List<String> ids = new ArrayList<>();
		try {
			Set<String> attrNames = new HashSet<>();
			attrNames.add(RoleManagerConstants.ROLE_KEY);

			Role role = rmgr.getDetails(RoleManagerConstants.ROLE_NAME, groupName, attrNames);
			List<User> users = rmgr.getRoleMembers((String) role.getAttribute(RoleManagerConstants.ROLE_KEY), true);

			for (User user : users) {
				ids.add(user.getLogin());
			}

		} catch (SearchKeyNotUniqueException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (NoSuchRoleException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - NoSuchRoleException error", e);
		} catch (RoleLookupException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - RoleLookupException error", e);
		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (RoleMemberException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}

		return ids.toArray(new String[ids.size()]);

	}
	
	
	
	/**
     * Finds the specified IT Resource details
     * 
     * @param itResourceName
     *            the name of the IT Resource to find. Case sensitive
     * @return a map with the found IT Resource details
     * @throws tcAPIException
     * @throws tcColumnNotFoundException
     * @throws tcITResourceNotFoundException
     */
    public Map<String, String> getITResourceDetails(String itResourceName)
            throws tcAPIException, tcColumnNotFoundException, tcITResourceNotFoundException {

    	logger.entering(className, "getITResourceDetails");
    	
		HashMap<String, String> searchcriteria = new HashMap<>();
		searchcriteria.put("IT Resources.Name", itResourceName);
	
		tcResultSet trs = itResOps.findITResourceInstances(searchcriteria);
		int rowCount = trs.getRowCount();
		long itResourceKey = -1;
	
		if (rowCount > 0) {
		    trs.goToRow(0);
		    itResourceKey = trs.getLongValue("IT Resource.Key");
		}

		Map<String, String> itAttrib = new HashMap<>();
		if (itResourceKey > 0) {
		    tcResultSet paramsRs = itResOps.getITResourceInstanceParameters(itResourceKey);
		    for (int i = 0; i < paramsRs.getRowCount(); i++) {
			paramsRs.goToRow(i);
	
			String name = paramsRs.getStringValue("IT Resources Type Parameter.Name");
			String value = paramsRs.getStringValue("IT Resources Type Parameter Value.Value");
	
			itAttrib.put(name, value);
		    }
		}
		
		logger.exiting(className, "getITResourceDetails");
		return itAttrib;
    }
    
    
    /**
     * Sends a notification of the users trying to be deleted massive
     * @param usersToDelete Users that ar trying to be deleted.
     */
    private void notifyMassiveDeletion(Set<String[]> usersToDelete) {
    	logger.entering(className, "notifyMassiveDeletion");
		try {
			logger.fine("Constructing the HTM table");
		    StringBuilder html = new StringBuilder(
		            "<table><tr><th>Login</th><th>RUT</th></tr>");
	
		    logger.finer("Looping over users");
		    for (String[] oimUser : usersToDelete) {
	
				String usrLogin = oimUser[1];
				String usrRut = oimUser[2];
				logger.finest("User "+usrLogin+" with RUT "+usrRut);
				html.append("<tr><td>").append(usrLogin).append("</td><td>").append(usrRut).append("</td></tr>");;
	
		    }
		    html.append("</table>");
	
		    logger.fine("Preparing notification event");
		    NotificationEvent ev = new NotificationEvent();
		    ev.setTemplateName("TrustedUserMassiveDeleteReconciliation Alert");
		    
		    logger.fine("Begins building list of adressees.");
			String[] adresseeOperaciones = usersLogins("Operaciones");
			String[] adresseeSeguridad = usersLogins("Seguridad");
			String[] adresseeCA = usersLogins("Control Acceso");
			int opLength = adresseeOperaciones.length;
			int seLength = adresseeSeguridad.length;
			int caLength = adresseeCA.length;
			String[] adressee = new String[opLength + seLength + caLength];
			for (int i = 0; i < opLength; i++) {
				adressee[i] = adresseeOperaciones[i];
			}
			for (int i = 0; i < seLength; i++) {
				adressee[opLength + i] = adresseeSeguridad[i];
			}
			for (int i = 0; i < caLength; i++) {
				adressee[opLength + seLength + i] = adresseeCA[i];
			}
			logger.fine("List of adressees build.");
			ev.setUserIds(adressee);
	
		    logger.fine("Setting parameters");
		    HashMap<String, Object> templateParams = new HashMap<String, Object>();
		    templateParams.put("Usrs_Html", html.toString());
		    ev.setParams(templateParams);
		    
		    logger.info("Sending massive user deletion notification ["
		            + "TrustedUserMassiveDeleteReconciliation Alert" + "] to reviewers "
		            + Arrays.toString(adressee));
		    nsrv.notify(ev);
		} catch (Exception e) {
		    logger.log(Level.SEVERE, "Unexpected error - notifyMassiveDeletion", e);
		}
		logger.exiting(className, "notifyMassiveDeletion");
    }
    
    
    
    /**
     * Insert audit for the user get from  SuccessFactors
     * @param userId ID of the user
     * @param personId Person ID of the user
     * @param xml XML output
     */
    private void insertAuditUserDelete(String userId, String personId, String xml) {
    	logger.exiting(className, "insertAuditUserDelete" , new Object[]{userId,personId});
    	PreparedStatement stmt = null;
    	Connection oimdb = null;
    	int count=0;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.finer("Construct insert to execute");
	    	String sql = "insert into ENTEL_AUDIT_SSFF_USER_DELETE(jobdate,userid,personid,xml) values(?,?,?,?)";
	    	stmt = oimdb.prepareStatement(sql);
	    	Clob clob = oimdb.createClob();
	    	clob.setString(1, xml);
	    	stmt.setDate(1, new java.sql.Date(auditJobTime.getTime()));
	    	stmt.setString(2, userId);
	    	stmt.setString(3, personId);
	    	stmt.setClob(4, clob);
	    	
	    	logger.finer("Performing INSERT");
	    	count = stmt.executeUpdate();
            
	    } catch (Exception e) {
    		e.printStackTrace();
    		logger.log(Level.SEVERE, "Unexpected error - insertAuditUserDelete", e);
    	} finally {
    		try {
	    		if (stmt != null) {
	    			logger.finest("Closing Prepared Statement...");
	    			stmt.close();
	    			
			    }
	    		if (oimdb != null ) {
					logger.finer("Closing database connection!");
				    oimdb.close();
				}
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.log(Level.SEVERE, "Unexpected error - insertAuditUserDelete", e);
    		}
    	}
				    	
    	logger.exiting(className, "insertAuditUserDelete",count);
    	
    }


    
    /**
     * Insert audit for the user get from  SuccessFactors
     * @param userId ID of the user
     * @param personId Person ID of the user
     * @param xml XML output
     */
    private void insertAuditUserCheck(String userId, String personId, String service, String xml) {
    	logger.exiting(className, "insertAuditUserCheck" , new Object[]{userId,personId});
    	PreparedStatement stmt = null;
    	Connection oimdb = null;
    	int count=0;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.finer("Construct insert to execute");
	    	String sql = "insert into ENTEL_AUDIT_SSFF_USER_CHECK(jobdate,service,userid,personid,xml) values(?,?,?,?,?)";
	    	stmt = oimdb.prepareStatement(sql);
	    	Clob clob = oimdb.createClob();
	    	clob.setString(1, xml);
	    	stmt.setDate(1, new java.sql.Date(auditJobTime.getTime()));
	    	stmt.setString(2, service);
	    	stmt.setString(3, userId);
	    	stmt.setString(4, personId);
	    	stmt.setClob(5, clob);
	    	
	    	logger.finer("Performing INSERT");
	    	count = stmt.executeUpdate();
            
	    } catch (Exception e) {
    		e.printStackTrace();
    		logger.log(Level.SEVERE, "Unexpected error - insertAuditUserCheck", e);
    	} finally {
    		try {
	    		if (stmt != null) {
	    			logger.finest("Closing Prepared Statement...");
	    			stmt.close();
	    			
			    }
	    		if (oimdb != null ) {
					logger.finer("Closing database connection!");
				    oimdb.close();
				}
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.log(Level.SEVERE, "Unexpected error - insertAuditUserCheck", e);
    		}
    	}
				    	
    	logger.exiting(className, "insertAuditUserCheck",count);
    	
    }

    
    /**
     * Insert audit for the users XML get from  SuccessFactors
     * @param service Service call
     * @param invocation Number of the invocation
     * @param xml XML output
     */
    private void insertAuditUsersXML(String service, int invocation, String xml) {
    	logger.exiting(className, "insertAuditUsersXML" , new Object[]{service,invocation});
    	PreparedStatement stmt = null;
    	Connection oimdb = null;
    	int count=0;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.finer("Construct insert to execute");
	    	String sql = "insert into ENTEL_AUDIT_SSFF_USERS_XML(jobdate,service,invocation,xml) values(?,?,?,?)";
	    	stmt = oimdb.prepareStatement(sql);
	    	Clob clob = oimdb.createClob();
	    	clob.setString(1, xml);
	    	stmt.setDate(1, new java.sql.Date(auditJobTime.getTime()));
	    	stmt.setString(2, service);
	    	stmt.setInt(3, invocation);
	    	stmt.setClob(4,clob);
	    	
	    	
	    	logger.finer("Performing INSERT");
	    	count = stmt.executeUpdate();
            
	    } catch (Exception e) {
    		e.printStackTrace();
    		logger.log(Level.SEVERE, "Unexpected error - insertAuditUserDelete", e);
    	} finally {
    		try {
	    		if (stmt != null) {
	    			logger.finest("Closing Prepared Statement...");
	    			stmt.close();
	    			
			    }
	    		if (oimdb != null ) {
					logger.finer("Closing database connection!");
				    oimdb.close();
				}
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.log(Level.SEVERE, "Unexpected error - insertAuditUsersXML", e);
    		}
    	}
				    	
    	logger.exiting(className, "insertAuditUsersXML",count);
    	
    }
    
    
    /**
     * Check if an user must be deleted
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param itResource
     *            the name of the IT Resource.
     * @param reconUrl 
     * 			  Filter to execute reconciliation
     * @param isAuditFlagOn 
     * 			  Flag to indicate save audit 
     * @return 
     * 			Flag to delete
     * @throws Exception 
     */
    private String getXmlResult(Map<String, String> itResourceDetails, String itResource, String reconUrl, boolean isAuditFlagOn, String userId, String personId) throws Exception {

    	logger.entering(className, "getXmlResult");
    	String xmlResult = null;
    	try {
    	
	    	logger.fine("Call to establish connection with SuccessFactors");
			String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
			logger.finest("Access token: " + accessToken);
			
			logger.finer("Constructing the filter for active users");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'"); 
			Calendar cal = Calendar.getInstance();
	    	cal.add(Calendar.DATE, 1);
	    	
	    	logger.finer("getting default filter for users");
	    	reconUrl = reconUrl.replace("(Date)", sdf.format(cal.getTime()));
	    	
	    	logger.finer("getting default filter for users");
	    	reconUrl = reconUrl.replace("(UserID)", userId);
			
			logger.fine("Calling the User service from Success Factors");
	    	xmlResult = SuccessFactorsConnection.getServiceResponse( itResourceDetails,reconUrl,accessToken);
			logger.finest("Response from Success Factors: " + xmlResult);
			
			logger.finer("Checking if audit is on to save the total xml output");
			if (isAuditFlagOn) {
				insertAuditUserCheck(userId, personId,reconUrl,xmlResult);
				
			}
			
    	} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getXmlResult - Unexpected error", e);
			throw e;
		}
    	
		logger.exiting(className, "getXmlResult");
		return xmlResult;
    }
    
    
    
	/**
	 * Return the user status list of employee of Success Factors
	 * @param xmlUser XML response from service User of Success Factors
	 * @return The user status of Success Factors
	 * @throws Exception 
	 */
	public String getUserStatus (String xmlUser) throws Exception {
		
		logger.entering(className, "getUserStatus");
		String usrStatus = null;
		
		try {
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUser));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			if (list.size() == 0) {
				logger.severe("Not a valid response from SuccessFactors: " + xmlUser);
				throw new Exception("Not a valid response from SuccessFactors: " + xmlUser);
			}
			
			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the userId of the current user");
					Element entry = (Element )list.get(i);
					Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element status = (Element) properties.element("status");

			        logger.finest("Status found: " +status.getStringValue());
			        usrStatus = status.getStringValue();
			        break;
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUserStatus - Unexpected error" ,e);
			throw e;
			
		}
		
		
		logger.exiting(className, "getUserStatus");
		return usrStatus;
		
	}
	
	
	/**
	 * Return the candidate start date list of employee of Success Factors
	 * @param xmlUser XML response from service User of Success Factors
	 * @return The candidate start date of Success Factors
	 * @throws Exception 
	 */
	public String getCandidateStartDate (String xmlUser) throws Exception {
		
		logger.entering(className, "getCandidateStartDate");
		String usrStartDate = null;
		
		try {
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUser));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			if (list.size() == 0) {
				logger.severe("Not a valid response from SuccessFactors: " + xmlUser);
				throw new Exception("Not a valid response from SuccessFactors: " + xmlUser);
			}
			
			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the StartDate of the current user");
					Element entry = (Element )list.get(i);
					Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element startDate = (Element) properties.element("startDate");

			        logger.finest("StartDate found: " +startDate.getStringValue());
			        usrStartDate = startDate.getStringValue();
			        break;
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getCandidateStartDate - Unexpected error" ,e);
			throw e;
		}
		
		
		logger.exiting(className, "getCandidateStartDate");
		return usrStartDate;
		
	}
		
	
}
