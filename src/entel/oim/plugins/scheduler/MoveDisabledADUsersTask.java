package entel.oim.plugins.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
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
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.scheduler.vo.TaskSupport;


public class MoveDisabledADUsersTask extends TaskSupport {

private final static String className = MoveDisabledADUsersTask.class.getName();
private static Logger logger = Logger.getLogger(className);

//Services
private final UserManager umgr = Platform.getService(UserManager.class);
private final ProvisioningService psrv = Platform.getService(ProvisioningService.class);
private final RoleManager rmgr = Platform.getService(RoleManager.class);
private final NotificationService nsrv = Platform.getService(NotificationService.class);
private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);


@Override
public void execute(HashMap hm) throws Exception {
	
	logger.entering(className, "execute", hm);
	
	String appInstanceName = (String) hm.get("Active Directory App Instance Name");
	String lookup = (String) hm.get("Lookup");
	String ou = (String) hm.get("Organizational Unit");
	
	logger.log(Level.FINE, "Processing the users marked to be deleted...");
	List<String> usersDisabledList =  getUsersToDelete();
	
	logger.log(Level.FINE, "Getting");
	String ouKey = getLookupCode(lookup,ou);

	logger.fine("Calling the procedure to move the AD accounts of users disabled");
	moveActiveDirectoryAccount(usersDisabledList,appInstanceName,ouKey);
	
	// Send mail of users without Gestor
	logger.fine("Calling the procedure to notify about the AD accounts moved of users disabled");
	notifyUsersMoved(usersDisabledList);
	
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
 * Find the code of one lookup value
 * @param lookupName Lookup in where to look up
 * @param decode Value to find
 * @return Code of the value
 * @throws Exception
 */
private String getLookupCode(String lookupName, String decode) throws Exception {
	logger.entering(className, "getLookupCode", decode);
	String code = null;
	
	try {
		logger.fine("Getting lookup values from  "+ lookupName);
		tcResultSet rs = lookupOprInf.getLookupValues(lookupName);
	
		logger.finer("ooping over lookup values");
		for (int i=0;i<rs.getRowCount();i++) {
			rs.goToRow(i);
			logger.finer("Looping over row " +i);
			String currentDecode= rs.getStringValue("Lookup Definition.Lookup Code Information.Decode");
			logger.finer("Current lookup value " + currentDecode);
			if (currentDecode != null && currentDecode.split("~")[1].equalsIgnoreCase(decode)) {
				code = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
				logger.finest("Lookup Found! The code is: " + code);
				break;
			}
		    
		}
	} catch (tcAPIException | tcInvalidLookupException | tcColumnNotFoundException e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "getLookupCode - Unexpected error", e);
		throw e;
	} 
	logger.exiting(className, "getLookupCode",code);
	return code;
}

/**
 * Return the List of users marked to delete
 * @return List of User Key
 * @throws Exception
 */
private List<String> getUsersToDelete() throws Exception{
	
	logger.entering(className, "getUsersToDelete");
	List<String> usrKeyList = new ArrayList<String>();
	
	try {
		
		logger.finer("Constructing search criteria...");
		SearchCriteria disableSc = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(),
				UserManagerConstants.AttributeValues.USER_STATUS_DISABLED, Operator.EQUAL);
		SearchCriteria deleteSc = new SearchCriteria(UserManagerConstants.AttributeName.AUTOMATICALLY_DELETED_ON.getId(),
		        "null", Operator.NOT);
		SearchCriteria join = new SearchCriteria(disableSc, deleteSc, Operator.AND);
		Set<String> usrAttrs = new HashSet<>();
		usrAttrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());
	
		logger.finer("Calling the User Manager Service to find users");
		List<User> usrList = umgr.search(join, usrAttrs, null);
		
		logger.finer("Checking if not null the users list");
		if (usrList != null) { 
			
			logger.finer("Looping over the users List");
			for (User usr : usrList) {
				logger.finest("Adding the userKey: " + usr.getEntityId());
				usrKeyList.add(usr.getEntityId());
			}
			
		} 
		
	} catch (UserSearchException | AccessDeniedException e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "getUsersToDelete - Unexpected error", e);
		throw e;
	}
	
	logger.exiting(className, "getUsersToDelete", usrKeyList);
	return usrKeyList;

}


/**
 * Move an Active Directory Account to the specified OU
 * @param usersDisabledList List of disabled users in OIM
 * @param appInstanceName Application Instance Name to move
 * @param ouKey Organizational Unit destiny of the account
 * @throws Exception
 */
private void moveActiveDirectoryAccount(List<String> usersDisabledList,String appInstanceName, String ouKey) throws Exception {

	logger.entering(className, "moveActiveDirectoryAccount",appInstanceName);
	logger.finer("Looping over all the disabled users");
	
	try {
		
		for (String usrKey: usersDisabledList) {
			logger.finest("Looping over userKey: " + usrKey);
			List<Account> accountsList = psrv.getAccountsProvisionedToUser(usrKey,true); 
			logger.log(Level.FINEST,"User's Accounts: {0}", new Object[]{accountsList});
			
			logger.finer("Checking if not null the account list of the user");
			if (accountsList != null) {
				logger.finer("Looping over all the accounts of the user");
				for (Account account: accountsList) {
					logger.finer("It is an application instance from type " + appInstanceName);
					
					String currentOU = (String)account.getAccountData().getData().get("UD_ADUSER_ORGNAME");
					logger.finest("Current OU for the account is: " + currentOU);
					
					logger.finer("Checking if we must move the accunt to the new OU");
					if (currentOU != null && !currentOU.equals(ouKey)) {
						logger.finer("The account must be moved");
						logger.finer("Constructing the attributes to modified");
			            HashMap<String, Object> modParentData = new HashMap<String, Object>();
			            modParentData.put("UD_ADUSER_ORGNAME", ouKey); // Key = Resource attribute column name
			            
			            logger.finer("Calling the procedure to modify the account");
						modifyUserResourceAccountParentData(usrKey, account, modParentData);
					
					}
		        
				}
			}
		
		}
	
	} catch (Exception e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "moveActiveDirectoryAccount - Unexpected error", e);
		throw e;
	}
	
	logger.exiting(className, "moveActiveDirectoryAccount");
}
	
	
	/**
     * Modifies a resource account on an OIM user
     * @param userKey           OIM usr_key
     * @param resourceAccount   Existing resource account to modify
     * @param modAttrs          Attributes to modify on the paraent form
	 * @throws Exception 
     * @throws AccountNotFoundException
     * @throws GenericProvisioningException
     */
    private void modifyUserResourceAccountParentData(String userKey, Account resourceAccount, HashMap<String, Object> modAttrs) throws Exception  {
        
    	logger.entering(className, "modifyUserResourceAccountParentData");
    	try {
    		
	    	// Stage resource account modifcations
	        String accountId  = resourceAccount.getAccountID();
	        String processFormInstanceKey = resourceAccount.getProcessInstanceKey();
	        Account modAccount = new Account(accountId, processFormInstanceKey, userKey);
	        logger.log(Level.FINEST, "Account Id: [{0}], Process Form Instance Key: [{1}]", new Object[]{accountId, processFormInstanceKey});
	 
	        String formKey = resourceAccount.getAccountData().getFormKey();
	        String udTablePrimaryKey = resourceAccount.getAccountData().getUdTablePrimaryKey();
	        AccountData accountData = new AccountData(formKey, udTablePrimaryKey , modAttrs);
	        logger.log(Level.FINEST, "Form Key: [{0}], UD Table Primary Key: [{1}]", new Object[]{formKey, udTablePrimaryKey});
	         
	        // Set necessary information to modified account
	        modAccount.setAccountData(accountData);
	        modAccount.setAppInstance(resourceAccount.getAppInstance());
	 
	        // Modify resource account
	        logger.log(Level.FINER, "Executing the modification");
	        psrv.modify(modAccount);
	        logger.log(Level.FINER, "Modification successful.");
        
		} catch (oracle.iam.platform.authopss.exception.AccessDeniedException | AccountNotFoundException
				| GenericProvisioningException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "modifyUserResourceAccountParentData - Unexpected error", e);
			throw e;
		}
        
        logger.exiting(className, "modifyUserResourceAccountParentData");
        
    }

    /**
	 * Notification to alert the list of disabled users
	 * 
	 * @param usersDisabledList
	 *            List of disabled users
     * @throws UserLookupException 
     * @throws NoSuchUserException 
	 */
	private void notifyUsersMoved(List<String> usersDisabledList) throws NoSuchUserException, UserLookupException {

		logger.entering(className, "notifyUsersWithoutGestor");

		// Construction of table with users to enable
		String html = toConstructHtmlTable("USUARIOS DESHABILITADOS", usersDisabledList);

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

		logger.exiting(className, "notifyUsersWithoutGestor");

	}

	/**
	 * Construction of HTML with table of users
	 * 
	 * @param title
	 *            Table's title
	 * @param users
	 *            List of users
	 * @return
	 * @throws UserLookupException 
	 * @throws NoSuchUserException 
	 */
	private String toConstructHtmlTable(String title, List<String> users) throws NoSuchUserException, UserLookupException {

		logger.entering(className, "toConstructHtmlTable");

		logger.fine("Begins building content for notification.");

		// Construction of table with users without gestor
		StringBuilder html = new StringBuilder("<table border><tr><td><center><b>" + title + "</b></center></td></tr>");

		
		for (String user : users) {

			html.append("<tr><td><center>").append(getUser(user, false)).append("</center></td></tr>");

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
	 * Get a specific user's resource account
	 * 
	 * @param userId
	 *            OIM UserLogin / User Key
	 * @param userLoginUsed
	 *            True for user login. False for user key.
	 * @return value of usr_key
	 * @throws NoSuchUserException
	 * @throws UserLookupException
	 */
	private String getUser(String userId, boolean userLoginUsed)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "getUser", userId);

		HashSet<String> attrsToFetch = new HashSet<String>();
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_KEY.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		User user = umgr.getDetails(userId, attrsToFetch, userLoginUsed);

		logger.exiting(className, "getUser", user);

		if (userLoginUsed) {
			return user.getEntityId();
		} else {
			return user.getLogin();
		}

	}
		
	
}
