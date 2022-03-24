package entel.oim.plugins.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.entitymgr.vo.SearchCriteria.Operator;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.scheduler.vo.TaskSupport;


/**
 * Execute the absense control job for OIM users
 * @author Oracle
 *
 */
public class AbsenceControlTask extends TaskSupport {
	
	private final static String className = AbsenceControlTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	// Services
    private final UserManager umgr = Platform.getService(UserManager.class);
    private final ProvisioningService psrv = Platform.getService(ProvisioningService.class);
    private final RoleManager rmgr = Platform.getService(RoleManager.class);
    private final NotificationService nsrv = Platform.getService(NotificationService.class);
    private final tcITResourceInstanceOperationsIntf itResOps = Platform.getService(tcITResourceInstanceOperationsIntf.class);


    // Atributtes
    private final static String ATTR_USER_EXTPER_ID = "ExtendedPermission";
    private final static String SUCCESSFACTORS_ENTEL = "SSFF-TRUSTED";
 	
    
	
	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
		
		String itResource = (String) hm.get("IT Resource Name");
		String service = (String) hm.get("Service");
		String accounts = (String) hm.get("Application Instance Names");
		String filterRut = (String) hm.get("Filter RUT");
		String absenceTypes = (String) hm.get("Types of Absences");
		
		logger.log(Level.FINER, "Getting details of IT Resource [{0}]", itResource);
		Map<String, String> itResourceDetails = getITResourceDetails(itResource);

		logger.log(Level.FINE, "Processing the users with absences...");
		Map<String,String> usersWithAbsenceMap =  getUsersWithAbsence(itResourceDetails, service, absenceTypes);

		logger.fine("Calling the procedure to disable the accounts of users with absences");
		disableUserAccounts(usersWithAbsenceMap,accounts,filterRut);
		
		logger.fine("Calling the procedure to reenable the accounts of users without absence");
		List<String> usersToEnable = enableUserAccounts(usersWithAbsenceMap, accounts, filterRut);

		// Send mail of users with absence
		logger.fine("Calling the procedure to notify the accounts of users with absence");
		notifyUsersWithAbsence(usersWithAbsenceMap);

		// Send mail of users enabled
		logger.fine("Calling the procedure to notify the accounts of users enabled");
		notifyUsersEnabled(usersToEnable);
		logger.exiting(className, "execute");

	}
	
	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
    
    
    /**
     * Disable accounts of users with absences
     * @param usersWithAbsenceMap Map of users with absences
     * @param accounts Types of accounts to disable
     * @param filterRut Rut to filter the execution
     */
    private void disableUserAccounts(Map<String,String> usersWithAbsenceMap,String accounts,String filterRut) {
    	
    	logger.entering(className, "disableUserAccounts");
    	
    	try {
    		
    		logger.finer("Checking if filtered execution of disable user");
    		if (filterRut != null && !filterRut.isEmpty()) {
    			
    			if (usersWithAbsenceMap.keySet().contains(filterRut)) {
    				logger.fine("Filtered execution of disable user");
    				
    				logger.finer("Finding usrKey for RUT [" + filterRut + "]");
					String usrKey= getUserKey(filterRut);
					
					if (usrKey != null && !usrKey.isEmpty()) {
						logger.finest("User found: " +usrKey);
						User uo = new User(usrKey);
						uo.setAttribute(ATTR_USER_EXTPER_ID, usersWithAbsenceMap.get(filterRut));
		
						logger.finer("Activating Extended Permission to user [" + usrKey + "]");
						umgr.modify(uo);
						
						logger.finer("Finding OIM accounts to disable");
						Set<Long> toDisable = findOIMAccouts(usrKey,accounts,true);
						for (long oiuKey : toDisable) {
						    
						    logger.finest("Disabling account: " + oiuKey);
						    psrv.disable(oiuKey);
		
						}
					}
				
    			}
    			
    		} else {
    			logger.fine("Full execution of disable user");
    			
				for (String rut : usersWithAbsenceMap.keySet()) {
				
					logger.finer("Finding usrKey for RUT [" + rut + "]");
					String usrKey=  getUserKey(rut);
					
					if (usrKey != null && !usrKey.isEmpty()) {
						logger.finest("User found: " +usrKey);
						User uo = new User(usrKey);
						uo.setAttribute(ATTR_USER_EXTPER_ID, usersWithAbsenceMap.get(rut));
		
						logger.finer("Activating Extended Permission to user [" + usrKey + "]");
						umgr.modify(uo);
						
						logger.finer("Finding OIM accounts to disable");
						Set<Long> toDisable = findOIMAccouts(usrKey,accounts,true);
						for (long oiuKey : toDisable) {
						    
						    logger.finest("Disabling account: " + oiuKey);
						    psrv.disable(oiuKey);
		
						}
					}
					
				
				}

			}
    		
    	} catch (Exception e) {
			e.printStackTrace();
    		logger.log(Level.SEVERE, "disableUserAccounts - Unexpected error", e);
		}
	
		logger.exiting(className, "disableUserAccounts");
    	
    };
    
    
    
    /**
     * Enable accounts of users with absences
     * @param usersWithAbsenceMap Map of users with absences
     * @param accounts Types of accounts to enable
     * @param filterRut Rut to filter the execution
     * @return 
     */
    private List<String> enableUserAccounts(Map<String,String> usersWithAbsenceMap, String accounts, String filterRut) {
    	
    	logger.entering(className, "enableUserAccounts");
    	List<String> usersToEnable = new ArrayList<String>();
    	
    	try {
    		
    		logger.fine("Getting the users with absence in OIM");
    		Set<String[]> usersSet = findOimUsersWithAbsence(filterRut);
    		
    		logger.finer("Looping over all the users with absences in OIM");
    		for (String[] user : usersSet) {
			
				String usrKey = user[0];
				String usrLogin = user[1];
				String usrRut = user[2];
				
				logger.finest("Chekcing if user [" +usrLogin+"] has absence in Success Factors" );
				if (!usersWithAbsenceMap.keySet().contains(usrRut)) {
					
					logger.finer("Deactivating Extended Permission to user [" + usrLogin + "]");
					User uo = new User(usrKey);
					uo.setAttribute(ATTR_USER_EXTPER_ID, null);
					umgr.modify(uo);
					usersToEnable.add(usrLogin);
					
					logger.finer("Finding OIM accounts to enable");
					Set<Long> toEnable = findOIMAccouts(usrKey,accounts,false);
					for (long oiuKey : toEnable) {
					    
					    logger.finest("Enabling account: " + oiuKey);
					    psrv.enable(oiuKey);
	
					}
				
				}
				
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.log(Level.SEVERE, "enableUserAccounts - Unexpected error", e);
		}
	
		logger.exiting(className, "enableUserAccounts");
    	return usersToEnable;
    };
    
    
    
    /**
     * Set of Accounts Id of the user
     * @param usrkey User Key of the accounts
     * @param accounts Types of accounts to find
     * @param enabled Status of the account to find
     * @return Set of Accounts Id of the user 
     * @throws SQLException
     */
    private Set<Long> findOIMAccouts(String usrkey, String accounts, boolean enabled) throws SQLException {

    	logger.entering(className, "findOIMAccouts");
    	Set<Long> targetAccounts = new HashSet<>();
    	PreparedStatement stmt = null;
    	Connection oimdb = null;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.fine("Checking if accounts must be enabled/disabled");
	    	if (accounts != null && !accounts.isEmpty()) {
	    	
		    	String[] appInstancesNames = accounts.split(",");
		    	logger.finest("Number of types of accounts to check: "+ appInstancesNames.length);
	    	
		    	for (int i = 0 ;i < appInstancesNames.length; i++ ) {
		    		
		    		String sql = null;
					if (enabled) {
				    	sql = "select oiu.oiu_key from oiu join app_instance on app_instance.app_instance_key= oiu.app_instance_key "
			    		        + " join ost on ost.ost_key=oiu.ost_key where app_instance.app_instance_name='"+appInstancesNames[i]+"' and ost.ost_status in ('Provisioned','Enabled') and oiu.usr_key ='"+usrkey+"'";
				    } else {
				    	sql = "select oiu.oiu_key from oiu join app_instance on app_instance.app_instance_key= oiu.app_instance_key "
			    		        + " join ost on ost.ost_key=oiu.ost_key where app_instance.app_instance_name='"+appInstancesNames[i]+"' and ost.ost_status = 'Disabled' and oiu.usr_key ='"+usrkey+"'";
				    }
			    	logger.finest("SQL to execute: " + sql);
					
			    	logger.finer("Executing query");
			    	stmt = oimdb.prepareStatement(sql);
			    	ResultSet rs = stmt.executeQuery();
			
				    if (!rs.isBeforeFirst()) {
				    	logger.finer("No accounts found!");
				    } else {
				    	
					    while (rs.next()) {
					    	long oiuKey = rs.getLong("oiu_key");
					    	logger.finest("Adding account: " + oiuKey);
					    	targetAccounts.add(oiuKey);
					    }
					    
				    }
				    
				    logger.finest("Closing result set...");
				    rs.close();
				    
		    	}
	    	
	    	}
	    	
	    } finally {
    		if (stmt != null) {
    			logger.finest("Closing Prepared Statement...");
    			stmt.close();
    			
		    }
    		if (oimdb != null ) {
				logger.finer("Closing database connection!");
			    oimdb.close();
			}
    	}
				    	
		logger.exiting(className, "findOIMAccouts", targetAccounts.size());
		return targetAccounts;
    }
    
    
    /**
     * Find all user with absence in OIM
     * @param filterRut Rut for filtered lookup
     * @return Set of users with absence in OIM
     * @throws SQLException
     */
    private Set<String[]> findOimUsersWithAbsence(String filterRut) throws SQLException {
	
    	logger.entering(className, "findOimUsersWithAbsence", filterRut);
    	Set<String[]> users = new HashSet<>();
    	PreparedStatement stmt = null;
    	Connection oimdb = null;
    	try {
    		
    		logger.finer("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
	    	
	    	logger.finer("Constructing sql to execute");
	    	String sql = "select usr_key, usr_login, usr_udf_rut from usr where upper(usr_udf_origin) = ? "
		        + "and usr_status = 'Active' and usr_udf_extendedpermission is not null";
	    	if (filterRut != null && !filterRut.isEmpty()) {
			    sql += " and usr_udf_rut = '" + filterRut + "'";
			}
	    	logger.finest("Query to execute: " +sql);

	    	
	    	logger.finer("Executing query");
			stmt = oimdb.prepareStatement(sql);
		    stmt.setString(1, SUCCESSFACTORS_ENTEL);
		    ResultSet rs = stmt.executeQuery();
	
		    if (!rs.isBeforeFirst()) {
		    	logger.fine("No user found");
		    } else {
		    	
		    	logger.finer("Looping over users result");
		    	while (rs.next()) {
					
					String[] user = new String[3];
					user[0] = rs.getString("usr_key");
					user[1] = rs.getString("usr_login");
					user[2] = rs.getString("usr_udf_rut");
					logger.finest("Adding user: " + rs.getString("usr_login"));
					users.add(user);
			    }
		    }

		    logger.finer("Closing result set");
		    rs.close();
		    
		} finally {
		    if (stmt != null) {
		    	logger.finer("Closing Prepared Statement");
		    	stmt.close();
		    }
		    if (oimdb != null ) {
				logger.finer("Closing database connection!");
			    oimdb.close();
			}
		}
	
		logger.exiting(className, "findOimUsersWithAbsence", users.size());
		return users;
    }
    
    
    /**
     * Return the user key of an user in OIM
     * @param rut Rut of the user to find
     * @return User Key of the User
     * @throws AccessDeniedException 
     * @throws UserSearchException 
     * @throws Exception
     */
    private String getUserKey(String rut) throws Exception {
    	
    	logger.entering(className, "getUserKey", rut);
    	String usrKey = null;
    	
    	try {
    		
	    	logger.fine("Constructing search criteria...");
	    	SearchCriteria rutSc = new SearchCriteria("RUT", rut, Operator.EQUAL);
	    	SearchCriteria actSc = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
	    	        UserManagerConstants.AttributeValues.USER_STATUS_ACTIVE, Operator.EQUAL);
	    	SearchCriteria join = new SearchCriteria(rutSc, actSc, Operator.AND);
	    	Set<String> usrAttrs = new HashSet<>();
	    	usrAttrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());
	
	    	HashMap<String, Object> sParam = new HashMap<>();
	    	sParam.put("STARTROW", 0);
	    	sParam.put("ENDROW", 1);
	
	    	logger.fine("Calling the User Manager Service to find user");
	    	List<User> usrList = umgr.search(join, usrAttrs, sParam);
			
	    	if (usrList != null && usrList.size() > 0) { 
	    		logger.finest("User found!");
	    		usrKey = usrList.get(0).getEntityId();
	    	} 

    	} catch (UserSearchException | AccessDeniedException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUserKey - Unexpected error", e);
			throw e;
		}
    	
    	logger.exiting(className, "getUserKey", usrKey);
    	return usrKey;
	
    }
	
   
    
    /**
     * Finds all users with absences in Success Factors
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            {@linkplain #getITResourceDetails(String)} method.
     * @param itResource
     *            the name of the IT Resource.
     * @param absenceTypes
     * 			  types of absences that are taking in consideration     
     * @return A map with all the users with absences      
     * @throws Exception
     */
    private Map<String,String> getUsersWithAbsence(Map<String, String> itResourceDetails, String service, String absenceTypes)
            throws Exception {

    	logger.entering(className, "getUsersWithAbsence", service);
    	Map<String,String> userWithAbsenceMap;
    	
    	logger.fine("Call to establish connection with SuccessFactors");
		String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
		
		logger.fine("Find users with absence in Success Factors");
		userWithAbsenceMap = findUsersWithAbsence(itResourceDetails,accessToken,service,absenceTypes);
		
		logger.exiting(className, "getUsersWithAbsence", service);
		return userWithAbsenceMap;
    }
    
    
    /**
     * Get the user list with active absences
     * @param hostURL
     * 			Host of SuccessFactors
     * @param accessToken
     * 			Token to call SuccessFactos
     * @param service
     * 			Service to call to get absences
     * @param absenceTypes
     * 			Types of absences to considerate
     * @return
     * 			The Map of user with active absences in SuccessFactors
     */
    private Map<String,String> findUsersWithAbsence (Map<String, String> itResourceDetails, String accessToken, String service, String absenceTypes) throws Exception {
    	
    	logger.entering(className, "findUsersWithAbsence");
    	Map<String,String> userMap;
    	
    	logger.finest("Getting the actual date");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date today = Calendar.getInstance().getTime();        
					
		logger.fine("Establishing the service and filter to call");
		String filter = "?$filter=%28endDate%20ge%20'"+df.format(today)+"'%20or%20undeterminedEndDate%20eq%20'true'%29%20and%20startDate%20le%20'"+df.format(today)+"'%20and%20approvalStatus%20eq%20'APPROVED'%20and%20mdfSystemStatus%20eq%20'A'";
		
    	logger.fine("Calling the EmployeeTime service from Success Factors");
		String xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service+filter,accessToken);
		
		logger.finest("Response from Success Factors: " + xmlService);
		
		logger.fine("Constructing the Map");
		userMap = getUsersMap(xmlService,absenceTypes);
		
		logger.fine("Checking if exists more results");
		String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
		while (moreCodeValues != null) {
			
			logger.finest("Extracting the filter for next values");
			String nextResultFilter = moreCodeValues.split(service)[1];
			logger.finest("Filter for next values: "+ nextResultFilter);
			
			logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
			xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service+nextResultFilter,accessToken);
			logger.finest("Response from Success Factors: " + xmlService);
			
			logger.fine("Constructing the List for new values");
			Map<String,String> nextMap = getUsersMap(xmlService,absenceTypes);
			
			logger.fine("Adding the new values to the original Map");
			userMap.putAll(nextMap);
			
			logger.fine("Checking if exists more results");
			moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
			
		}
		
		logger.exiting(className, "findUsersWithAbsence");
		return userMap;
	    
    }
    
    
    
    
	/**
	 * Call to service in SuccessFactors
	 * @param hostURL Host URl of the SuccessFactors
	 * @param service Service to call
	 * @param accessToken Access Token of the session of SuccessFactors
	 * @return XML result
	 */
	public static String getUsersWithAbsence(String hostURL, String service, String accessToken) { 
		
		logger.entering(className, "getServiceResponse");
		String result = null;
		
		try {
			
			logger.fine("Establishing the headers for the request");
			URL url = new URL(hostURL+"odata/v2/"+service);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			logger.fine("Trying connection...");
			conn.connect();

			logger.fine("Verifying success on the connection");
			if (conn.getResponseCode() != 200) {
				logger.severe("Error establishing connection with the Service. Error code: " +conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			} else {
				logger.fine("Connection success to the Service");
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
		
				String output;
				logger.finest("Saving the Output from the Service");
				while ((output = br.readLine()) != null) {
					logger.finest(output);
					result = output;
				}
		
				logger.fine("Disconnection from server.");
				conn.disconnect();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getServiceResponse - Unexpected error",e);

		} 
		
		logger.exiting(className, "getServiceResponse",result);
		return result;

	}

    
    
	/**
	 * Return the list of the users of Success Factors
	 * @param xml XML response from service of Success Factors
	 * @param absenceTypes Types of absences to considerate
	 * @return A Map with values of Success Factors
	 */
	public static Map<String,String> getUsersMap(String xml,String absenceTypes) {
		
		logger.entering(className, "getUsersMap");
		Map<String,String> usersMap = new HashMap<String,String>();
		
		try {
			
			logger.finer("Checking if absences types is not null");
			if (absenceTypes != null && !absenceTypes.isEmpty()) {
				
				logger.fine("Split the list of absences");
				String[] absenceTypesArray = absenceTypes.split(",");
				
				logger.fine("Parsing the XML to Document");
				SAXReader saxBuilder = new SAXReader();
				Document document = saxBuilder.read(new StringReader(xml));
				
				logger.fine("Get all elements returned by the service");
				List<Node> list =  document.selectNodes("//feed/*");
	
				logger.fine("Loop over the users in the XML");
				for (int i = 0; i < list.size(); i++) {
					if ( "entry".equals(list.get(i).getName())) {
						
						logger.fine("Get the userId of the current element");
						Element entry = (Element )list.get(i);
				        Element content = (Element) entry.element("content");
				        Element properties = (Element) content.element("properties");
				        Element userId = (Element) properties.element("userId");
				        Element timeType = (Element) properties.element("timeType");
				        
				        if (Arrays.asList(absenceTypesArray).contains(timeType.getStringValue())) {
					        logger.finest("Adding to the list the user: " + userId.getStringValue());
					        usersMap.put(userId.getStringValue(), timeType.getStringValue());
				        }
					}
					
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsersMap - Unexpected error: ",e);
			
		}
		
		logger.exiting(className, "getUsersMap");
		return usersMap;
		
	}
	
	/**
	 * Method to send notifications with the disabled accounts due to absence
	 * 
	 * @param usersWithAbsenceMap
	 *            List of users with absence
	 */
	private void notifyUsersWithAbsence(Map<String, String> usersWithAbsenceMap) {

		logger.entering(className, "notifyUsersWithAbsence");

		List<String> usersWithAbsenceList = new ArrayList<String>();

		for (String userId : usersWithAbsenceMap.keySet()) {
			usersWithAbsenceList.add(userId);
		}

		// Construction of table with users with absence
		String html = toConstructHtmlTable("USUARIOS CON AUSENCIA", usersWithAbsenceList);

		// Construction of template parameters
		HashMap<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("info_html", html.toString());

		logger.fine("Begins building list of adressees.");
		String[] adresseeOperaciones = usersLogins("Operaciones");
		String[] adresseeSeguridad = usersLogins("Seguridad");

		String[] adressee = new String[adresseeOperaciones.length + adresseeSeguridad.length];

		for (int i = 0; i < adresseeOperaciones.length; i++) {
			adressee[i] = adresseeOperaciones[i];
		}

		for (int i = 0; i < adresseeSeguridad.length; i++) {
			adressee[adresseeOperaciones.length + i] = adresseeSeguridad[i];
		}

		logger.fine("List of adressees build.");

		sendEmailNotification("AbsenceControlDisable Alert", templateParams, adressee);

		logger.exiting(className, "notifyUsersEnabled");

	}

	/**
	 * Method to send notifications of users enabled.
	 * 
	 * @param usersToEnable
	 *            List of users enabled.
	 */
	private void notifyUsersEnabled(List<String> usersToEnable) {

		logger.entering(className, "notifyUsersEnabled");

		// Construction of table with users to enable
		String html = toConstructHtmlTable("USUARIOS HABILITADOS", usersToEnable);

		// Construction of template parameters
		HashMap<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("info_html", html.toString());

		logger.fine("Begins building list of adressees.");
		String[] adresseeOperaciones = usersLogins("Operaciones");
		String[] adresseeSeguridad = usersLogins("Seguridad");

		String[] adressee = new String[adresseeOperaciones.length + adresseeSeguridad.length];

		for (int i = 0; i < adresseeOperaciones.length; i++) {
			adressee[i] = adresseeOperaciones[i];
		}

		for (int i = 0; i < adresseeSeguridad.length; i++) {
			adressee[adresseeOperaciones.length + i] = adresseeSeguridad[i];
		}

		logger.fine("List of adressees build.");

		sendEmailNotification("AbsenceControlEnable Alert", templateParams, adressee);

		logger.exiting(className, "notifyUsersEnabled");

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
	private String toConstructHtmlTable(String title, List<String> users) {

		logger.entering(className, "toConstructHtmlTable");

		logger.fine("Begins building content for notification [" + title + "].");

		// Construction of table with users without gestor
		StringBuilder html = new StringBuilder("<table border><tr><td><center><b>" + title + "</b></center></td></tr>");

		for (String user : users) {

			html.append("<tr><td><center>").append(user).append("</center></td></tr>");

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
	
        
}
