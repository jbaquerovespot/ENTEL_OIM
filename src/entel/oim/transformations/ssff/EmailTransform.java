package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import entel.oim.adapters.Utilities;


/**
 * Transform the Email from SuccessFactors
 * @author Oracle
 *
 */ 
public class EmailTransform {

	private final static String className = EmailTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String email = (String) reqData.get(sField);
		
		logger.finer("LowerCase String...");
		Utilities util = new Utilities();
		email = util.toLowerCase(email);
		
		if (email != null && !email.isEmpty()) {
			logger.finer("Removing special characters");
			email = email.replaceAll("[^a-zA-Z0-9!#$%&@'*+-/=?^_`{|}~.]+", "");
		}
		
		if (!isValid(email)) {
			logger.finer("Email is invalid. Setting null value");
			email = null;
		}
		
		logger.exiting(className, "transform",email);
		return email;
	}
	
	
	/**
	 * Check if an email is valid
	 * @param email Email to check
	 * @return True if email is valid. False if not
	 */
	private boolean isValid(String email) { 
        
		logger.entering(className, "isValid",email);
		boolean valid = false;
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        if (email != null){
        	logger.finer("Checking if valid email");
        	Pattern pat = Pattern.compile(emailRegex); 
            valid = pat.matcher(email).matches();
        }
        
        logger.exiting(className, "isValid",valid);
		return valid;
    } 
	
	
}