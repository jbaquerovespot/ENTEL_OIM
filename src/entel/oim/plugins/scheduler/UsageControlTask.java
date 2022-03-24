package entel.oim.plugins.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
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
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.scheduler.vo.TaskSupport;


/**
 * Execute the usage control job for OIM users
 * @author Oracle
 *
 */
public class UsageControlTask extends TaskSupport {
	
	private final static String className = UsageControlTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

    private final UserManager umgr = Platform.getService(UserManager.class);
    private final NotificationService nsrv = Platform.getService(NotificationService.class);
    private final ProvisioningService psrv = Platform.getService(ProvisioningService.class);
    private final RoleManager rmgr = Platform.getService(RoleManager.class);
    
	
	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
		
		String inactiveDays = (String) hm.get("Inactive Days");
		String accounts = (String) hm.get("Accounts");
		String disableDays = (String) hm.get("Disable Days");
		String filterRUT = (String) hm.get("Filter RUT");
		logger.log(Level.FINER, "Getting details of Acounts ["+accounts+"] with inactive days ["+inactiveDays+"]");

		logger.finer("Finding the accounts to be disabled");
		Set<Long> toDisable = findOIMAccouts(inactiveDays, accounts, filterRUT);

		if (isStop()) {
		    logger.log(Level.FINE, "Interrupted JOB");
		    return;
		}

		logger.finer("Disabling the founds accounts");
		for (long oiuKey : toDisable) {
		    
		    if (isStop()) {
				logger.log(Level.FINE, "Interrupted JOB");
				return;
		    }
		    
		    logger.finest("Disabling account: " + oiuKey);
		    psrv.disable(oiuKey);

		}

		// Send mail of accounts disabled
		notifyDisabledAccounts(toDisable);
		
		logger.finer("Finding the entities to be disable");
		Set<String> toDisableEntities = findOIMEntities(disableDays, filterRUT);

		if (isStop()) {
		    logger.log(Level.FINE, "Interrupted JOB");
		    return;
		}

		if (toDisableEntities.contains(""))
		logger.finer("Disabling the founds entities");
		for (String usrLogin : toDisableEntities) {
		    
		    if (isStop()) {
				logger.log(Level.FINE, "Interrupted JOB");
				return;
		    }
		    
		    logger.finest("Disabling entity: " + usrLogin);
		    umgr.disable(usrLogin,true);
		    
		}

		// Send mail of accounts disabled
		notifyDisabledEntities(toDisableEntities);	

	}

	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
	
	
    /**
     * Finds all the target users of the IT Resource.
     * 
     * @param inactiveDays
     *            Number of days that an account can not by inactive.
     * @param accounts
     *            Accounts to be checked.
     * @param filterRUT
     * 			  Filter RUT
     * @return the found target accounts.
     * @throws SQLException
     */
    private Set<Long> findOIMAccouts(String inactiveDays, String accounts, String filterRUT)
            throws SQLException {

    	logger.entering(className, "findOIMAccouts");
    	Set<Long> targetAccounts = new HashSet<>();
    	
    	logger.fine("Getting OIM Connection");
    	Connection oimdb = Platform.getOperationalDS().getConnection();
		
    	String[] formsAccounts = accounts.split(";");
    	logger.finest("Number of types of accounts to check: "+ formsAccounts.length);
    	
    	logger.finest("Looping ");
    	for (int i = 0 ;i < formsAccounts.length; i++ ) {
    		
    		String[] formsAccountsDetail = formsAccounts[i].split("|");
    		String account = formsAccountsDetail[0].toUpperCase().trim();
    		String attribute = formsAccountsDetail[1].toUpperCase().trim();
    		String dateFormat = formsAccountsDetail[2].trim();

    		String sql = "select oiu.oiu_key from "+account+" join oiu on oiu.orc_key="+account+".orc_key "
		    		        + "join ost on ost.ost_key=oiu.ost_key join usr on usr.usr_key = oiu.usr_key where ost.ost_status in ('Provisioned','Enabled') "
		    		        + "and trunc(sysdate) - to_date("+attribute+",'"+dateFormat+"') >= "+inactiveDays;
			    	
	    	if (filterRUT != null && !filterRUT.isEmpty()) {
    		    sql += " and UPPER(usr.usr_udf_rut) = '" + filterRUT.toUpperCase() +"'";
    		}
	    	
	    	PreparedStatement stmt = null;
	    	try {
		    	stmt = oimdb.prepareStatement(sql);
			    ResultSet rs = stmt.executeQuery();
		
			    if (!rs.isBeforeFirst()) {
			    	logger.finer("No accounts found!");
			    } else {
			    	while (rs.next()) {
				    	long oiuKey = rs.getLong("oiu_key");
				    	
				    	targetAccounts.add(oiuKey);
				    }
			    	
			    }
			    
			    rs.close();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		logger.log(Level.SEVERE, "Unexpected error - findOIMAccouts", e);
			    
	    	} finally {
	    		if (stmt != null) {
					stmt.close();
				}
	    		if (oimdb != null ) {
					logger.finer("Closing database connection!");
				    oimdb.close();
				}
	    	}
    	}

		logger.exiting(className, "findOIMAccouts", targetAccounts.size());
		return targetAccounts;
    }
    
    
    
    /**
     * Finds all the target users entities of the IT Resource.
     * 
     * @param disableDays
     *            Number of days that an entity can not by active without active accounts.
     * @param filterRUT
     * 				Filter RUT
     * @return the found target accounts.
     * @throws SQLException
     */
    private Set<String> findOIMEntities(String disableDays, String filterRUT)
            throws SQLException {

    	logger.entering(className, "findOIMEntities");
    	Set<String> targetEntities = new HashSet<>();
    	
    	logger.fine("Getting OIM Connection");
    	Connection oimdb = Platform.getOperationalDS().getConnection();
		
    	logger.finer("Constructing the query");
    	String sql = "select usr.usr_login from usr where usr.usr_status = ? " + 
    			" and not exists ( select 1 from oiu join ost  on ost.ost_key = oiu.ost_key" + 
    			" join app_instance on app_instance.app_instance_key = oiu.app_instance_key" + 
    			" where ost.ost_status IN ('Provisioned','Enabled')" + 
    			" and app_instance_name != 'SSOTarget'" + 
    			" and trunc(sysdate) - trunc(oiu_update) >= "+disableDays + 
    			" and usr.usr_key = oiu.usr_key)";
    			
    	if (filterRUT != null && !filterRUT.isEmpty()) {
		    sql += " and UPPER(usr.usr_udf_rut) = '" + filterRUT.toUpperCase() +"'";
		}
    	
		PreparedStatement stmt = null;
		try {
		    stmt = oimdb.prepareStatement(sql);
		    stmt.setString(1, "Active");
		    ResultSet rs = stmt.executeQuery();
		    
		    if (!rs.isBeforeFirst()) {
		    	logger.finer("No accounts found!");
		    } else {
		    	while (rs.next()) {
			    	String usrLogin= rs.getString("usr_login");
			    	targetEntities.add(usrLogin);
			    }
		    	
		    }
		    
		    logger.finest("Closing result set");
		    rs.close();
	    
    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}
    		if (oimdb != null ) {
				logger.finer("Closing database connection!");
			    oimdb.close();
			}
    	}

		logger.exiting(className, "findOIMEntities", targetEntities.size());
		return targetEntities;
    }
    
    
   
    /**
	 * Sends a notification of the disabled accounts to the security and operations
	 * departments.
	 * 
	 * @param disabledAccounts
	 *            List of the oiu_key of disabled Accounts
	 */
	private void notifyDisabledAccounts(Set<Long> disabledAccounts) {

		logger.entering(className, "notifyDisabledAccounts");

		logger.fine("Building list of users whose accounts were disabled.");
		List<String> disabledAccountsList = getUsersDisabledAccounts(disabledAccounts);

		logger.fine("Building notification message.");
		// Construction of table with users to enable
		String html = toConstructHtmlTable("USUARIOS DESHABILITADOS", disabledAccountsList);

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

		sendEmailNotification("MoveDisabledADUsers Alert", templateParams, adressee);

		logger.exiting(className, "notifyDisabledAccounts");

	}

	/**
	 * Retriving the users of the disabled accounts.
	 * 
	 * @param disabledAccounts
	 *            list of accounts with oiu_key
	 * @return
	 */
	private List<String> getUsersDisabledAccounts(Set<Long> disabledAccounts) {

		logger.entering(className, "getUsersDisabledAccounts");

		List<String> disabledAccountsList = new ArrayList<String>();

		String oiulist = "";

		for (Long oiu_key : disabledAccounts) {

			oiulist.concat(oiu_key.toString()).concat(", ");

		}

		oiulist = oiulist.trim();

		if (!oiulist.isEmpty()) {

			if (oiulist.endsWith(",")) {
				oiulist = oiulist.substring(0, oiulist.length() - 1).trim();
			}

			// Retrieve the users of the oiu
			String sql = "select usr.usr_login from oiu join usr on oiu.usr_key = usr.usr_key where oiu.oiu_key in ('"
					+ oiulist + "')";

			PreparedStatement stmt = null;
			Connection oimdb = null;
			try {
				logger.fine("Getting OIM Connection");
				oimdb = Platform.getOperationalDS().getConnection();
				stmt = oimdb.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();

				if (!rs.isBeforeFirst()) {
					logger.finer("No accounts found!");
				} else {
					logger.fine("Connection established");
					while (rs.next()) {
						String userId = rs.getString("usr_login");
						disabledAccountsList.add(userId);
					}
					logger.fine("Data retrieved.");
				}

				rs.close();

			} catch (SQLException e) {
				logger.log(Level.SEVERE, "getUsersDisabledAccounts - SQLException: " + e.getMessage());
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						logger.log(Level.SEVERE, "getUsersDisabledAccounts - SQLException: " + e.getMessage());
					}
				}
				if (oimdb != null ) {
					try { 
						logger.finer("Closing database connection!");
					    oimdb.close();
					}  catch (SQLException e) {
						logger.log(Level.SEVERE, "getUsersDisabledAccounts - SQLException: " + e.getMessage());
					}
				}
			}
		}

		logger.exiting(className, "getUsersDisabledAccounts");

		return disabledAccountsList;
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

		logger.fine("Begins building content for notification.");

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
	 * Disable inactive account and user in OIM.
	 * @param appInstance
	 * @param attrLastLogin
	 * @param dateFormat
	 * @param inactiveDays
	 * @throws Exception
	 */
	public void processInactiveUsers(String appInstance, String attrLastLogin, String dateFormat, int inactiveDays) throws Exception {
		
		logger.entering(className, "processInactiveUsers", inactiveDays);
		try {
			
			logger.fine("Searching for "+appInstance+ " accounts");
			HashMap<String, Object> configParam = new HashMap<String, Object>();  
			SearchCriteria sc1 = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(),ProvisioningConstants.ObjectStatus.PROVISIONED.getId(), SearchCriteria.Operator.EQUAL);   
			SearchCriteria sc2 = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(),ProvisioningConstants.ObjectStatus.ENABLED.getId(), SearchCriteria.Operator.EQUAL);
			SearchCriteria sc = new SearchCriteria(sc1,sc2, SearchCriteria.Operator.OR);   
			List<Account> accountsList = psrv.getProvisionedAccountsForAppInstance(appInstance, sc, configParam);
			
			logger.fine("Looping over the accounts founds");
			for (Account account : accountsList) {
				
				logger.finer("Checking if account is Primary and NOT Service Account");
				if (account.getAccountType().getId().equalsIgnoreCase("Primary") && !account.isServiceAccount()) {
				
					logger.finer("Account: "+account.getAccountID()+".Getting last login");
					String lastLogin = (String)account.getAccountData().getData().get(attrLastLogin);
					logger.finest("Last Login foound: "+lastLogin);
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					Date lastLoginDate = sdf.parse(lastLogin);
					
					logger.finer("Getting days without connection");
					Calendar calLastLogin = Calendar.getInstance();
					calLastLogin.setTime(lastLoginDate);
					Calendar calNow = Calendar.getInstance();
					// Get the represented date in milliseconds
			        long millis1 = calLastLogin.getTimeInMillis();
			        long millis2 = calNow.getTimeInMillis();
			        // Calculate difference in milliseconds
			        long diff = millis2 - millis1;
					// Calculate difference in days
			        long diffDays = diff / (24 * 60 * 60 * 1000);
			        logger.finest("Days without connection: " + diffDays);
			        
			        logger.fine("Checking if max days without connection reached");
			        if (diffDays >= inactiveDays) {
			        	
			        	logger.finest("Max days reached. Getting user information");
			        	String usrKey = account.getUserKey();
						Set<String> searchAttrs = new HashSet<String>();
						searchAttrs.add(UserManagerConstants.AttributeName.ACCOUNT_STATUS.getId());
						User user = umgr.getDetails(usrKey, searchAttrs, false);
						
						logger.finer("Checking if user "+usrKey+" is not disabled");
						if (!user.getAccountStatus().equals(UserManagerConstants.AttributeValues.USER_STATUS_DISABLED.getId())) {
							logger.finest("User "+usrKey +" not disabled. Disabling...");
							umgr.disable(usrKey, false);
						} else {
							logger.finest("User "+usrKey +" not disabled. Disabling only the account");
							psrv.disable(Long.parseLong(account.getAccountID()));
						}
						
					}
					
				}
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error - processInactiveUsers", e);
			throw e;
		}
		
		logger.entering(className, "processInactiveUsers", inactiveDays);
	}
	
	/**
	 * Sends a notification of the disabled identities to the security and operations
	 * departments.
	 * @param toDisableEntities
	 */
	private void notifyDisabledEntities(Set<String> toDisableEntities) {
		
		logger.entering(className, "notifyDisabledAccounts");

		logger.fine("Building notification message.");
		// Construction of table with users to enable
		List<String> listEntities = new ArrayList<String>(toDisableEntities);
		String html = toConstructHtmlTable("ENTIDADES DESHABILITADAS", listEntities);

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

		sendEmailNotification("DisabledOIMIdentities Alert", templateParams, adressee);

		logger.exiting(className, "notifyDisabledAccounts");
		
	}
	

}