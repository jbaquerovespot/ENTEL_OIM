package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;

import entel.oim.adapters.Utilities;
import entel.oim.plugins.namegenerator.CustomUsernamePolicy;

/**
 * Transform the FirstName from SuccessFactors
 * @author Oracle
 *
 */
public class FirstNameTransform {

	private final static String className = FirstNameTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String names = (String) reqData.get(sField);
		
		logger.fine("Calling the getFirstName method");
		String firstName = 	getFirstName(names);
		
		logger.exiting(className, "transform",firstName);
		return firstName;
	}
	
	
	/**
	 * Split the name and get the first name of a person
	 * @param names List of names of the users
	 * @return First Name
	 * @author Oracle
	 */
	public static String getFirstName(String names) {
		
		logger.entering(className, "getFirstName",names);
		String firstName = "";
		
		logger.fine("Checking not null names");
		if (names != null)	{ 
			
			logger.fine("Trim spaces");
			names = names.trim();
		
			logger.fine("Checking if multiple names found");
			if (names.lastIndexOf(" ") != -1) {
				
				logger.fine("Checking if more than 2 names found");
				String[] words = names.split(" ");
				if (words.length > 2) {
										
					logger.finest("Looping over all the words in the names");
					for (int i = 0; i < words.length; i++) {
						firstName = firstName + " " + words[i];
						logger.finest("Current firstName: " + firstName);
						logger.finer("Checking if " + words[i]+ " is reserved word");
						if (!CustomUsernamePolicy.RESERVED_WORDS_LIST.contains(words[i])) {
							firstName = firstName.trim();
							logger.finest("Final firstName: " + firstName);
							break;
						} 
					}
					
				} else {
					logger.fine("Only two names. We take the first one");
					firstName = names.substring(0,names.lastIndexOf(" ")).trim();
				}
				
			} else {
				logger.fine("Only one name. It is the same");
				firstName= names; 
			}
		}
		
		logger.finer("Capitalizing String...");
		Utilities util = new Utilities();
		firstName = util.toCapitalizeString(firstName);
		
		logger.exiting(className, "getFirstName",firstName);
		return firstName;
		
	}
	
}