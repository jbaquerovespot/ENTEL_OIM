package entel.oim.transformations.ssff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import entel.oim.plugins.namegenerator.CustomUsernamePolicy;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;


/**
 * Transform the userLogin from SuccessFactors
 * @author Oracle
 *
 */
public class UserLoginTransform {

	private final static String className = UserLoginTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	// Attr Keys
	private final static String ATTR_ALL_NAMES_ID = "All Names";
	private final static String ATTR_ALL_LASTNAMES_ID = "All LastNames";
	private final static String ATTR_ORG_NAME_ID = OrganizationManagerConstants.AttributeName.ORG_NAME.getId();
	private final static String ATTR_ORG_PREFIX_ID = "Prefix";
	private final static String ATTR_RUT_ID = "RUT";
	private final static String ATTR_PERSON_ID = "Person Id";
	private final static String ORG_NAME = "Empleados";
	
	
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform",sField);
		
		logger.fine("Getting the value of the Id of the user (Person ID) and the Login (if exists)");
		String personId = (String) reqData.get(ATTR_PERSON_ID);
		
		logger.finer("Checking if user exist in OIM for reuse UserLogin");
		String userLogin = getUserLoginInOIM(personId);
		
		logger.fine("Check if the user exist for a Create or Update Operation");
		if (personId != null && userLogin != null) {
			logger.finer("Person Id " + personId+ " exists. User Login is " + userLogin);
			logger.finer("It is an Update Operation");
			logger.finer("Returning original UserLogin");
			return userLogin;
		
		} else {
			logger.finer("UserLogin empty.");
			
			logger.finer("user not exist in OIM. It is a Create Operation.");
			logger.fine("Getting the values of fields");
			String all_names = (String) reqData.get(ATTR_ALL_NAMES_ID);
			String all_lastnames = (String) reqData.get(ATTR_ALL_LASTNAMES_ID);
			String ou = findOrganizationByName(ORG_NAME).getEntityId();
			String origin = OriginTransform.SUCCESSFACTORS_ENTEL;
			String firstName = FirstNameTransform.getFirstName(all_names);
			String secondName = MiddleNameTransform.getMiddleName(all_names);
			String lastName = LastNameTransform.getLastName(all_lastnames);
			String surname = SurNameTransform.getSurName(all_lastnames);
			String rut = (String) reqData.get(ATTR_RUT_ID);
			
			logger.fine("Calling the procedure to create the custom userlogin");
			try {
				userLogin = CustomUsernamePolicy.getCustomUsername(firstName,secondName,lastName,surname, rut, ou, origin);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "Unexpected error - transform", e);
			}
					
			logger.exiting(className, "transform",userLogin);
			return userLogin;
			
		}
		
		
	}
	
	
	/**
	 * Returns the organization in OIM
	 * @param organizationName Organization name to find
	 * @return Get the Organization object in OIM
	 */
	public static Organization findOrganizationByName(String organizationName) {
		
		logger.entering(className, "findOrganizationByName",organizationName);
		Organization org = null;
		
		logger.fine("Setting the search criteria of the organization name: " + organizationName);
		SearchCriteria criteria = new SearchCriteria(ATTR_ORG_NAME_ID, organizationName, SearchCriteria.Operator.EQUAL);
		
		logger.fine("Setting attributes to return");
		Set<String> retAttrs = new HashSet<String>();
		retAttrs.add(ATTR_ORG_PREFIX_ID);
		retAttrs.add(ATTR_ORG_NAME_ID);

		try {
			
			logger.fine("Calling the OrganizationManager service");
			OrganizationManager orgmgr = Platform.getService(OrganizationManager.class);
			List<Organization> orgs = orgmgr.search(criteria, retAttrs, null);
			if(!orgs.isEmpty())
				org = orgs.get(0);
		} catch (Exception e) {
			
			logger.severe("findOrganization: Error calling OrganizationManager service: " + e.getMessage());
			e.printStackTrace();
		}

		logger.exiting(className, "findOrganizationByName");
		return org;
	}
	
	
	/**
	 * Check if the user login exists in OIM and return his UserLogin
	 * @param personId PersonId to check
	 * @return UserLogin from OIM
	 */
	public static String getUserLoginInOIM(String personId) {
		
		logger.entering(className, "getUserLoginInOIM",personId);
		List<User> users = null;
		String userLogin = null;
		
		List<String> statusUserList = new ArrayList<String>();
		statusUserList.add(UserManagerConstants.AttributeValues.USER_STATUS_ACTIVE.getId());
		statusUserList.add(UserManagerConstants.AttributeValues.USER_STATUS_DISABLED.getId());
		statusUserList.add(UserManagerConstants.AttributeValues.USER_STATUS_DISABLED_UNTIL_START_DATE.getId());
		
		logger.fine("Setting the search criteria of the user personId: " + personId);
		SearchCriteria criteriaLogin = new SearchCriteria("PersonId", personId, SearchCriteria.Operator.EQUAL);
		SearchCriteria anyStatus = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), statusUserList, SearchCriteria.Operator.IN);
		SearchCriteria criteria = new SearchCriteria(criteriaLogin, anyStatus, SearchCriteria.Operator.AND);
		
		
		logger.fine("Setting attributes to return");
		Set<String> retAttrs = new HashSet<String>();
		retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());

		try {
			
			logger.fine("Calling the UserManager service");
			UserManager usermgr = Platform.getService(UserManager.class);
			users = usermgr.search(criteria, retAttrs, null);
			
			logger.finer("Checkign if user exists in OIM");
			if ( users != null && !users.isEmpty()) { 
				userLogin = users.get(0).getLogin();
				logger.finest("User exists in OIM. PersonId " +personId+" belongs to " +userLogin);
			} else {
				logger.finest("PersonId not found in OIM");
					
			}
			
		} catch (Exception e) {
			
			logger.severe("getUserLoginInOIM: Error calling UserManager service: " + e.getMessage());
			e.printStackTrace();
		}
		
		logger.exiting(className, "getUserLoginInOIM",!users.isEmpty());
		return userLogin;
		
	}
			
}