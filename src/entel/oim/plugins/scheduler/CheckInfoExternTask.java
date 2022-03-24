package entel.oim.plugins.scheduler;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.scheduler.vo.TaskSupport;

public class CheckInfoExternTask extends TaskSupport {

private final static String className = CheckInfoExternTask.class.getName();
private static Logger logger = Logger.getLogger(className);

// Services
private final UserManager umgr = Platform.getService(UserManager.class);
private final OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
private final NotificationService nsrv = Platform.getService(NotificationService.class);
private final RoleManager rmgr = Platform.getService(RoleManager.class);


@Override
public void execute(HashMap hm) throws Exception {
	
	logger.entering(className, "execute", hm);
	
	logger.log(Level.FINE, "Processing the users without Active Gestor...");
	List<String> usersWithoutActiveGestorList =  getExternalUsersWithoutActiveGestor();

	logger.fine("Calling the remove Gestor process from users");
	removeGestorFromUsers(usersWithoutActiveGestorList);
	
	// Send mail of users without Gestor
	notifyUsersWithoutGestor(usersWithoutActiveGestorList);
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
 * Get the keys of the organization of type Extern
 * @return External Organization list key 
 * @throws Exception
 */
private List<String> getExternOrgKeys() throws Exception {
	
	logger.entering(className,"getExternOrgKeys");
	List<String> externOrgKeysList = new ArrayList<String>();
	
	try {
		
		logger.finer("Setting the attributes to find of the organization");
		Set<String> searchAttrs = new HashSet<String>();
		searchAttrs.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
	  
		logger.finer("Getting Organization Key of Extern");
		Organization orgExtern = orgMgr.getDetails("Externos", searchAttrs, true);
		
		logger.finer("Getting Organizations Keys Childs for Extern");
		HashMap<String, Object> configParams = new HashMap<String, Object>();
		List<Organization> childOrgExtern = orgMgr.getChildOrganizations(orgExtern.getEntityId(), searchAttrs, configParams);
	    
		logger.finer("Constructing ths list");
		for (Organization org : childOrgExtern) {
			logger.finer("Organization found: Key -> " + org.getEntityId()+ " | Name -> "+org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
			externOrgKeysList.add(org.getEntityId());
		}
	
	} catch (OrganizationManagerException | AccessDeniedException e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "getExternOrgKeys - Unexpected error", e);
		throw e;
	}
	
	logger.exiting(className,"getExternOrgKeys",externOrgKeysList);
	return externOrgKeysList;
}


/**
 * Set of Accounts Id of the user
 * @return Set of Accounts Id of the user 
 * @throws SQLException
 */
private List<User> getExternalUsers() throws Exception {

	logger.entering(className, "getExternalUsers");
	List<User> usersExtList = new ArrayList<User>();
	
	try {
	
		logger.fine("Calling to get External Organizations keys");
		List<String> externOrgKeysList = getExternOrgKeys();
		
		logger.finer("Setting filter to search external users");
		SearchCriteria criteriaOrg = new SearchCriteria(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId(), externOrgKeysList, SearchCriteria.Operator.IN);
		SearchCriteria creteriaStatus = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), UserManagerConstants.AttributeValues.USER_STATUS_ACTIVE.getId(), SearchCriteria.Operator.EQUAL);
		SearchCriteria criteria = new SearchCriteria(criteriaOrg, creteriaStatus, SearchCriteria.Operator.AND);
		
		logger.fine("Setting attributes to return");
		Set<String> retAttrs = new HashSet<String>();
		retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		retAttrs.add("Gestor");

		logger.fine("Calling the UserManager service");
		usersExtList = umgr.search(criteria, retAttrs, null);
		
	} catch (Exception e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "getExternalUsers - Unexpected error", e);
		throw e;
	}
			    	
	logger.exiting(className, "getExternalUsers", usersExtList.size());
	return usersExtList;
}


/**
 * Set of Accounts Id of the user
 * @return Set of Accounts Id of the user 
 * @throws SQLException
 */
private boolean isUserActive(String usrLogin) throws Exception {

	logger.entering(className, "isUserActive",usrLogin);
	boolean isActive = false;
	
	try {
	
		logger.finer("Setting filter to search the user " + usrLogin);
		SearchCriteria criteriaLogin = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(),usrLogin,SearchCriteria.Operator.EQUAL);
		SearchCriteria creteriaStatus = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), UserManagerConstants.AttributeValues.USER_STATUS_ACTIVE.getId(), SearchCriteria.Operator.EQUAL);
		SearchCriteria criteria = new SearchCriteria(criteriaLogin, creteriaStatus, SearchCriteria.Operator.AND);
		
		logger.fine("Setting attributes to return");
		Set<String> retAttrs = new HashSet<String>();
		retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());

		logger.fine("Calling the UserManager service");
		List<User> usersExtList = umgr.search(criteria, retAttrs, null);
		
		if (usersExtList != null && usersExtList.size() > 0) {
			logger.finest("The user " + usrLogin +" is Active");
			isActive = true;
		} else {
			logger.finest("The user " + usrLogin +" is NOT Active");
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		logger.log(Level.SEVERE, "isUserActive - Unexpected error", e);
		throw e;
	}
			    	
	logger.exiting(className, "isUserActive", isActive);
	return isActive;
}




	/**
	 * Remove the FIled Gestor tothe users list
	 * @param userKeyList
	 * 				List of Users Keys
	 * @throws SQLException
	 */
	private void removeGestorFromUsers(List<String> userKeyList) throws Exception {
	
		logger.entering(className, "removeGestorFromUsers");
		List<String> usersKeysList = new ArrayList<String>();
		
		try {
		
			logger.finer("Looping over all the users");
			for (String usrKey : userKeyList ) {
				
				logger.finest("Looping over userKey: " + usrKey);
				User uo = new User(usrKey);
				uo.setAttribute("Gestor", null);
				umgr.modify(uo);
				logger.finer("Removed Gestor of user [" + usrKey + "]");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "removeGestorFromUsers - Unexpected error", e);
			throw e;
		}
				    	
		logger.exiting(className, "removeGestorFromUsers", usersKeysList.size());

	}
	

	/**
	 * Set of Accounts Id of the user
	 * @return Set of Accounts Id of the user 
	 * @throws SQLException
	 */
	private List<String> getExternalUsersWithoutActiveGestor() throws Exception {

		logger.entering(className, "getExternalUsersWithoutActiveGestor");
		List<String> usersKeysList = new ArrayList<String>();
		
		try {
		
			logger.fine("Calling to get External Users");
			List<User> externalUsersList = getExternalUsers();
			
			logger.finer("Looping over all the externs");
			for (User usr : externalUsersList ) {
				String gestor = (String)usr.getAttribute("Gestor");
				logger.finest("Gestor of user " + usr.getLogin() + " is: " + gestor);
				if (gestor == null) {
					logger.finest("The user " +usr.getLogin()+ " doesn't have Gestor." );
					usersKeysList.add(usr.getEntityId());
				} else if (!isUserActive(gestor)) {
					logger.finest("The Gestor " +gestor+ " is not Active." );
					usersKeysList.add(usr.getEntityId());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getExternalUsersWithoutActiveGestor - Unexpected error", e);
			throw e;
		}
				    	
		logger.exiting(className, "getExternalUsersWithoutActiveGestor", usersKeysList.size());
		return usersKeysList;
	}
    
	/**
	 * Send notificaation to 
	 * @param usersWithoutActiveGestorList
	 */
	private void notifyUsersWithoutGestor(List<String> usersWithoutActiveGestorList) {

		logger.entering(className, "notifyUsersWithoutGestor");

		// Construction of table with users without gestor
		StringBuilder html = new StringBuilder(
				"<table border><tr><td><center><b>USUARIOS EXTERNOS</b></center></td></tr>");
		for (String user : usersWithoutActiveGestorList) {

			html.append("<tr><td><center>").append(user).append("</center></td></tr>");

		}
		html.append("</table>");

		NotificationEvent ev = new NotificationEvent();

		try {

			logger.fine("Begins building of notification with extern users without gestor.");

			// Setting template through the property
			ev.setTemplateName("CheckInfoExtern Alert");
			// Loading users
			ev.setUserIds(caUsersLogins());
			// Setting params to replace in the template
			HashMap<String, Object> templateParams = new HashMap<String, Object>();
			templateParams.put("info_html", html.toString());
			ev.setParams(templateParams);

			logger.fine("Sending subordinated manager notification [CheckInfoExtern Alert] to reviewers "
					+ ev.getUserIds().toString());

			nsrv.notify(ev);

		} catch (SearchKeyNotUniqueException e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - SearchKeyNotUniqueException error", e);
		} catch (NoSuchRoleException e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - NoSuchRoleException error", e);
		} catch (RoleLookupException e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - RoleLookupException error", e);
		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - AccessDeniedException error", e);
		} catch (RoleMemberException e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - RoleMemberException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "notifyUsersWithoutGestor - Unexpected error", e);
		} finally {
			logger.exiting(className, "notifyUsersWithoutGestor");
		}
	}

	private String[] caUsersLogins() throws SearchKeyNotUniqueException, NoSuchRoleException, RoleLookupException,
			AccessDeniedException, RoleMemberException {

		Set<String> attrNames = new HashSet<>();
		attrNames.add(RoleManagerConstants.ROLE_KEY);

		Role role = rmgr.getDetails(RoleManagerConstants.ROLE_NAME, "Control Acceso", attrNames);
		List<User> users = rmgr.getRoleMembers((String) role.getAttribute(RoleManagerConstants.ROLE_KEY), true);

		List<String> ids = new ArrayList<>();
		for (User user : users) {
			ids.add(user.getLogin());
		}

		return ids.toArray(new String[ids.size()]);

	}
}
