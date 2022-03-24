package entel.oim.transformations.ssff;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_KEY;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.selfservice.exception.UserLookupException;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;


/**
 * Transform the Create By of an user by SuccessFactors
 * @author Oracle
 *
 */
public class CreatedByTransform {

	private final static String className = CreatedByTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	private AuthenticatedSelfService authSelfServ = Platform.getService(AuthenticatedSelfService.class);
    
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		String createdBy = null;
		try {
			logger.finest("Getting the logged in user info");
			Set<String> userAttrs = new HashSet<String>();
			userAttrs.add(USER_KEY.getId());
			userAttrs.add(USER_LOGIN.getId());
			User userInfo = authSelfServ.getProfileDetails(userAttrs);
			
			logger.finer("Checking not null User");
			if ( userInfo != null )	createdBy = userInfo.getLogin();
			
		} catch (UserLookupException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"Unexpected error: " + e.getErrorMessage(),e);
		}
		
		logger.exiting(className, "transform", createdBy);
		return createdBy;
	}
}
