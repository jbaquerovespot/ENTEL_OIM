package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

/**
 * Transform the RUT from SuccessFactors
 * @author Oracle
 *
 */
public class ManagerTransform {

	private final static String className = ManagerTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	private final static String ATTR_RUT_ID = "RUT";

	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		String rut = (String) reqData.get(ATTR_RUT_ID);
		String rutManager = (String) reqData.get(sField);
		
		if (rutManager != null) {
			
			if (rutManager.equalsIgnoreCase("NO_MANAGER")) {
				logger.finer("Mgr is null");
				logger.exiting(className, "transform");
				return null;
				
			} else {
			
				logger.finer("Transforming RUT: " + rut);
				rut = getRut(rut);
				logger.finer("Transforming Manager RUT: " + rutManager);
				rutManager = getRut(rutManager);
					
					
				if (rutManager != null && rut != null && rut.equals(rutManager)) {
					logger.finer("Mgr is the same user: " + rutManager);
					logger.exiting(className, "transform");
					return null;
					
				}
				
				String managerLogin = searchUserInOIMByRUT(rutManager);
				if (managerLogin != null) {
					logger.finer("Mgr found: " + managerLogin);
					logger.exiting(className, "transform");
					return managerLogin;
	
				}	
			
			}
				
		}
				
		logger.exiting(className, "transform");
		return null;
	}
	
	/**
	 * Remove special characters from RUT
	 * @param rut Rut to transform
	 * @return Formated RUT
	 */
	public String getRut(String rut) {
		logger.entering(className, "getRut",rut);
		if (rut != null) {
			rut = rut.replace(".", "").replace("-", "");
			rut = rut.replaceFirst("^0+(?!$)", "");
			logger.finer("Transformed RUT: " + rut);
			logger.exiting(className, "getRut");
			return rut.toUpperCase();
		}
		logger.exiting(className, "getRut");
		return null;

	}
	
	
	/**
	 * Find an user in OIm by RUT
	 * @param RUT Rut to find in OIM
	 * @return User login of the user found
	 */
	public String searchUserInOIMByRUT(String rut) {

		logger.entering(className, "searchUserInOIMByRUT");
		String usrManager = null;
		
		logger.fine("Setting the search criteria of the user rut: " + rut);
		SearchCriteria usrCriteria = new SearchCriteria("RUT", rut, SearchCriteria.Operator.EQUAL);
		
		logger.fine("Setting attributes to return");
		Set<String> userCritAttrs = new HashSet<>();
		userCritAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		
		try {
			logger.fine("Calling the UserManager service");
			List<User> usrs;
			UserManager usermanager = Platform.getService(UserManager.class);
			usrs = usermanager.search(usrCriteria, userCritAttrs, null);
			
			logger.fine("Checking if user foound");
			if (usrs != null && !usrs.isEmpty()) {
				logger.finest("Found: " + usrs.get(0).getLogin());
				usrManager = usrs.get(0).getLogin();
			}

		} catch (UserSearchException | AccessDeniedException e) {

			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error searching manaager of the user by RUT: " +e.getMessage(), e);
		}

		logger.exiting(className, "searchUserInOIMByRUT");
		return usrManager;
	
	}
	
}