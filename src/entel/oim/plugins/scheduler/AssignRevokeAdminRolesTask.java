package entel.oim.plugins.scheduler;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.iam.platformservice.api.AdminRoleService;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants.RoleAttributeName;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.scheduler.vo.TaskSupport;
import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleMembership;

public class AssignRevokeAdminRolesTask extends TaskSupport {

	private final static String className = AssignRevokeAdminRolesTask.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	// Services
	private final OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
	private final RoleManager rmgr = Platform.getService(RoleManager.class);
	private final AdminRoleService adminRolesrv = Platform.getService(AdminRoleService.class);   

	@Override
	public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
		String rolesMapping = (String) hm.get("Roles Mapping");
		String[] rolesMappingArray = rolesMapping.split(";");
		
		logger.finer("Looping over roles to check");
		for (int i=0; i< rolesMappingArray.length ; i++) {
			String[] roleInfo = rolesMappingArray[i].split("\\|");
			String roleName = roleInfo[0];
			String adminRoleName = roleInfo[1];
			List<String> scopesList = Arrays.asList(roleInfo[2].split(","));
			
			logger.finer("Looping over Roles to add the corresponding AdminRole");
			Role role = getRole(roleName);
			List<User> userList = rmgr.getRoleMembers(role.getEntityId(), true);
			for (User usr : userList) {
				logger.finer("Getting the list of admin roles for the user " + usr.getLogin() + " with Id " +usr.getEntityId());
				List<AdminRole> adminRoleList = adminRolesrv.getAdminRolesForUser(usr.getEntityId(),null);
				if (adminRoleList == null || adminRoleList.size()== 0 || !isAdminRoleGranted(adminRoleList,adminRoleName) ) {
					logger.finer("Adding AdminRole "+ adminRoleName + " for user " + usr.getEntityId());
					assignAdminRole(usr.getEntityId(),adminRoleName,scopesList);
				}
				
			}
			
			logger.finer("Looping over AdminRoles to revoke the ones that do not apply any more");
			for (String scopeName : scopesList) {
				logger.finest("AdminRole: "+ adminRoleName+ " with Scope " + scopeName);
				String scopeId = searchOrgByName(scopeName);
				List<AdminRoleMembership> admRoleMembershipList = adminRolesrv.listMembershipsInScope(scopeId, adminRoleName, false, null);
				for (AdminRoleMembership admRoleMem : admRoleMembershipList) {
					logger.finest("AdminRoleMembership "+ adminRoleName + " with scopeID " + admRoleMem.getScopeId()+ " for user " + admRoleMem.getUserId());
					if (!rmgr.isRoleGranted(role.getEntityId(), admRoleMem.getUserId(), true)) {
						logger.finer("Removing "+ admRoleMem.getAdminRoleName() + " for scopeID " + admRoleMem.getScopeId()+ " for user " + admRoleMem.getUserId());
						boolean success= adminRolesrv.removeAdminRoleMembership(admRoleMem);
						logger.finest("Remove "+ success);
					}
				}
			}
			
		}
		
		
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
	 * Assigning OIM Admin role to a user.
	 * @param usrKey
	 * @param adminRoleName
	 * @param scopeOrgList
	 */
	public void assignAdminRole(String usrKey, String adminRoleName, List<String> scopeOrgList){
	
		logger.entering(className, "assignAdminRole", usrKey);
		try {
	
			logger.finer("initialize API and get the details of a specified admin role");
			AdminRole admRole = adminRolesrv.getAdminRole(adminRoleName);
	 
			logger.finer("Looping over scopes to Add");
			for (String scopeOrg : scopeOrgList) {
			
				logger.finer("Create a new membership object");
				AdminRoleMembership admMembership = new  AdminRoleMembership();
				admMembership.setAdminRole(admRole);
				admMembership.setUserId(usrKey);
				admMembership.setScopeId(searchOrgByName(scopeOrg));
				admMembership.setHierarchicalScope(true);
				logger.finest("New set Scope ::"+admMembership.getScopeId());
	
				try {
					AdminRoleMembership newMemberShip = adminRolesrv.addAdminRoleMembership(admMembership);
					logger.finest("Admin Role "+adminRoleName+" Successfully Assigned to the UserKey: "+newMemberShip.getUserId());
				  } catch (Exception e) {
					  logger.severe("User already has the admin role");
				}  
	
			}
	
			logger.exiting(className, "assignAdminRole");
			
		 } catch (Exception e) {
		  logger.log(Level.SEVERE, "Unexpected error - assignAdminRole", e);
		 }
	
	}
	
	
	
	/**
	 * Revok an Admin Role from user.
	 * @param usrKey
	 * @param adminRoleName
	 */
	public void revokeAdminRole(String usrKey, String adminRoleName) {
	
		logger.entering(className, "revokeAdminRole", usrKey);
		List<String> adminRoles = new ArrayList<String>();
		adminRoles.add(adminRoleName);
	
		logger.fine("Get the user and admin role membership object.");
		List<AdminRoleMembership> adminMemberShipList = adminRolesrv.listMembershipsForUserByRoleName(usrKey, adminRoles);
	
		for (AdminRoleMembership adminMemberShip : adminMemberShipList) {
		 logger.finer("Revoking admin role.");
			  boolean status = adminRolesrv.removeAdminRoleMembership(adminMemberShip);
			  logger.finest("Admin Role Successfully revoke from User: "+usrKey+" Status: "+status);
		}
		
		logger.exiting(className, "revokeAdminRole", adminRoleName);
	}
	
	
	/**
	 * Find the id of an organization.
	 * @param usrKey
	 * @param adminRoleName
	 */
	public String searchOrgByName(String orgName) {
		 
		logger.entering(className, "searchOrgByName", orgName);
		String res = null;
		try {
			logger.finer("Setting filter...");
			Set<String> orgCritAttrs = new HashSet<String>();
			orgCritAttrs.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
			orgCritAttrs.add(OrganizationManagerConstants.AttributeName.ID_FIELD.getId());
			SearchCriteria orcriteria = new SearchCriteria("Organization Name", orgName, SearchCriteria.Operator.EQUAL);
			
			logger.finer("Searching: " + orgName);
			List<Organization> orgsList = orgMgr.search(orcriteria, orgCritAttrs, null);
			if (orgsList != null) {
				logger.finest("Organization Result Set Size:: " + orgsList.size() + " "
						+ Arrays.toString(((Organization) orgsList.get(0)).getAttributeNames().toArray()));
	
				String organizationName = (String) ((Organization) orgsList.get(0)).getAttribute("Organization Name");
				String organizationID = ((Long) ((Organization) orgsList.get(0)).getAttribute("act_key")).toString();
				logger.finest("Organization Name :: " + organizationName + " " + organizationID);
				res = ((Organization) orgsList.get(0)).getEntityId();
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - searchOrgByName", e);
		} 
				
		logger.exiting(className, "searchOrgByName", res);
		return res;
	}
	
	
	/**
	 * Get a role object
	 * @param roleName
	 * @return
	 */
	 public Role getRole(String roleName){

		 logger.entering(className, "getRole");
		 Role role = null;
	      
		 try {
			 
			 logger.fine("Chekcing if role name was enter");
			 if (null != roleName && !roleName.isEmpty()) {
	        
				logger.finer("Trimming spaces...");
		        roleName = roleName.trim();
		        
		        logger.finer("Searching for object Role");
	        	SearchCriteria criteria = new SearchCriteria(RoleAttributeName.NAME.getId(), roleName, SearchCriteria.Operator.EQUAL);
	        	List<Role> roleList = rmgr.search(criteria, null, null);

	            if(null != roleList && roleList.size() == 1) {
	                role = roleList.get(0);
	                logger.finest("Successfully obtained role for role name " + roleName);
	            }
	        
	        }
		 
		 } catch(Exception e){
             logger.log(Level.SEVERE,"Unexpected Error - getRole", e);
		 }
	        
		logger.exiting(className, "getRole");
		return role;
	 }
	 
	 
	 /**
	  * Check if an user is granted for an adminrole
	  * @param adminRoleList
	  * @param adminRoleName
	  * @return
	  */
	 public boolean isAdminRoleGranted(List<AdminRole> adminRoleList, String adminRoleName) {
		 
		 logger.entering(className, "isAdminRoleGranted",adminRoleName);
		 boolean isGranted = false;
		 
		 logger.fine("Looping ober the list of Admin Roles");
		 for (AdminRole admRole : adminRoleList) {
			 logger.finest("Admin Role: " + admRole.getRoleName());
			 if (admRole.getRoleName().equalsIgnoreCase(adminRoleName)) {
				 isGranted =true;
				 logger.finer("It is granted the Admin Role: " + admRole.getRoleName());
				 break;
			 }
			 
		 }
		 
		 logger.exiting(className, "isAdminRoleGranted",isGranted);
		 return isGranted;
	 }

}
