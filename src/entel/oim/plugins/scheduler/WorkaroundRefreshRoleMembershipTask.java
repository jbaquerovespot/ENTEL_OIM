package entel.oim.plugins.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants.RoleAttributeName;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.entitymgr.vo.SearchRule;
import oracle.iam.scheduler.vo.TaskSupport;


/**
 * Execute the workaround for evaluation of membership rules
 * @author Oracle
 *
 */
public class WorkaroundRefreshRoleMembershipTask extends TaskSupport {
	
	private final static String className = WorkaroundRefreshRoleMembershipTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	// Services
    private final RoleManager rmgr = Platform.getService(RoleManager.class);
    
    
	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
		
		String roles = (String) hm.get("Roles");
		String[] rolesList = roles.split(",");
		
		logger.fine("Setting filters for search roles");
		SearchCriteria criteria = null;
		for (String role : rolesList) {
			logger.finest("Setting filter for role: " + role);
			SearchCriteria tmpCriteria = new SearchCriteria(RoleAttributeName.NAME.getId(), role, SearchCriteria.Operator.EQUAL);
	        if (criteria == null) {
				criteria = tmpCriteria;
			} else {
		        criteria = new SearchCriteria(criteria, tmpCriteria, SearchCriteria.Operator.OR);
			}

		}
		
		logger.finer("Searching roles");
        List<Role> roleList = rmgr.search(criteria, null, null);
    	if(roleList != null && roleList.size() > 0){
    		logger.finest("Roles founds: " + roleList.size());
            for (Role role : roleList) {  
              String roleName = role.getName();
              String rolekey=role.getEntityId();
              logger.finest("Getting membership rule of role " + roleName);
              SearchRule actual = rmgr.getUserMembershipRule(rolekey);
              logger.finest("Evaluating role " + roleName + " with key " + rolekey + "...");
              rmgr.setUserMembershipRule(rolekey, actual, false);
              System.out.println("Membership rule from "+ roleName+" was evaluated");
    
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
    
        
}
