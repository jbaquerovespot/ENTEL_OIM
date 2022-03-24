package entel.oim.plugins.scheduler;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platformservice.api.PlatformUtilsService;
import oracle.iam.scheduler.vo.TaskSupport;

public class CustomLookupUsersRoleTask extends TaskSupport {
	
	private final static String className = CustomLookupUsersRoleTask.class.getName();
	private static Logger logger = Logger.getLogger(className);


    private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
    private tcGroupOperationsIntf groupOprInf =  Platform.getService(tcGroupOperationsIntf.class);  
    private final UserManager umgr = Platform.getService(UserManager.class);
    private final PlatformUtilsService pltfSrv = Platform.getService(PlatformUtilsService.class);
    
	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
	
		logger.finer("Loading parameters");
		String role = (String) hm.get("Role");
		String lookupDefinition = (String) hm.get("Lookup Definition");

		logger.log(Level.FINE, "Processing all the lookups...");
		processLookupsValues(lookupDefinition,role);
		
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
     * Update all the lookups from users from OIM
     * @param lookupName
     * 			  Lookup that update
     * @param role
     * 			  Role for the users for the lookup. 
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	private void processLookupsValues(String lookupName, String role)
            throws Exception {

    	logger.entering(className, "processLookupsValues", lookupName);
    	HashMap<String, String> hmap = new HashMap<String, String>();
    	
    	try {
    		
    		logger.fine("Getting all the user members of the role: " + role);
		    List<User> usersList = getRoleMembers(role);
		    for (User user: usersList) {
		    	logger.finest("Reading  lookup Code: "+ user.getLogin() + " | lookup Value: "+ user.getDisplayName());
			    hmap.put(user.getLogin(), user.getDisplayName());
		    }
		    
			logger.fine("Getting lookup values from  "+ lookupName);
			tcResultSet rs = lookupOprInf.getLookupValues(lookupName);
			
			for (int i=0;i<rs.getRowCount();i++) {
				rs.goToRow(i);
				logger.finest("Deleting  "+ rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
			    lookupOprInf.removeLookupValue(lookupName,rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
			    
			}
			
			logger.fine("Let's sort the map in ascending order of value");
			HashMap<String, String> sorted = hmap
						.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

			for (Map.Entry me : sorted.entrySet()) {
		          logger.finest("Adding lookup-> Key: "+me.getKey() + " & Value: " + me.getValue());
		          lookupOprInf.addLookupValue(lookupName,me.getKey().toString(),me.getValue().toString(),"","");
	        }
			
			logger.finer("Purging the Lookup Vaues from OIM");
			pltfSrv.purgeCache("All");
			
		} catch (tcAPIException | tcInvalidLookupException | tcInvalidValueException | tcColumnNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error", e);
			throw e;
		} 
		

		logger.exiting(className, "processLookupsValues", lookupName);
    }
    
    
    
    /**
     * Return the list of user with an specific role
     * @param roleName
     * 			Role of the users
     * @return The list of users
     * @throws Exception
     */
    public List<User> getRoleMembers(String roleName) throws Exception {

    	logger.entering(className, "getRoleMembers", roleName);
        List<User> userList = new ArrayList<User>();

        Map<String, String> filter = new HashMap<String, String>();
        filter.put("Groups.Role Name", roleName);
        
        logger.finer("Calling the search of groups");
        tcResultSet role = groupOprInf.findGroups(filter);

        logger.finer("Getting the group key");
        String groupKey = role.getStringValue("Groups.Key");

        logger.finer("Getting all the menbers of the group");
        tcResultSet members = groupOprInf.getAllMemberUsers(Long.parseLong(groupKey));

        logger.finer("Looping over all the menbers of the group");
        for (int i = 0; i < members.getRowCount(); i++) {
        		
        		members.goToRow(i);
                long userKey = members.getLongValue("Users.Key");
                logger.finest("Member: " + userKey);

                logger.finest("Setting the attributes to find of the user");
    			Set<String> searchAttrs = new HashSet<String>();
    			searchAttrs.add(USER_LOGIN.getId());
    			searchAttrs.add("Display Name");
    			User member = umgr.getDetails(String.valueOf(userKey), searchAttrs, false);
    			userList.add(member);
    			logger.finest("UserLogin: " + member.getLogin());

        }

       logger.exiting(className, "getRoleMembers",userList);
       return userList;

    }

}
