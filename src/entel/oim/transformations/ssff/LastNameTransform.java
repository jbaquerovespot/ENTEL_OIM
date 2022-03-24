package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;

import entel.oim.adapters.Utilities;
import entel.oim.plugins.namegenerator.CustomUsernamePolicy;

/**
 * Transform the LastName from SuccessFactors
 * @author Oracle
 *
 */
public class LastNameTransform {

	private final static String className = LastNameTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String lastnames = (String) reqData.get(sField);
		
		logger.fine("Calling the getLastName method");
		String lastName = 	getLastName(lastnames);
		
		logger.exiting(className, "transform",lastName);
		return lastName;
	}
	
	
	/**
	 * Split the lastnames and get the last name of a person
	 * @param names List of names of the users
	 * @return Last Name
	 * @author Oracle
	 */
	public static String getLastName(String lastnames) {
		
		logger.entering(className, "getLastName",lastnames);
		String lastName = "";
		
		logger.fine("Checking not null lastnames");
		if (lastnames != null)	{ 
			
			logger.fine("Trim spaces");
			lastnames = lastnames.trim();
			
			logger.fine("Checking if multiple lastnames found");
			if (lastnames.lastIndexOf(" ") != -1) {
				
				logger.fine("Checking if more than 2 lastnames found");
				String[] words = lastnames.split(" ");
				if (words.length > 2) {
					
					logger.finest("Looping over all the words in the lastnames");
					for (int i = 0; i < words.length; i++) {
						lastName = lastName + " " + words[i];
						logger.finest("Current lastname: " + lastName);
						logger.finer("Checking if " + words[i]+ " is reserved word");
						if (!CustomUsernamePolicy.RESERVED_WORDS_LIST.contains(words[i])) {
							lastName = lastName.trim();
							logger.finest("Final lastName: " + lastName);
							break;
						} 
					}
					
				} else {
					logger.fine("Only two lastnames. We take the first one");
					lastName = lastnames.substring(0,lastnames.lastIndexOf(" ")).trim();
				}
				
			} else {
				logger.fine("Only one lastName. It is the same");
				lastName= lastnames; 
			}

		}
		
		logger.finer("Capitalizing String...");
		Utilities util = new Utilities();
		lastName = util.toCapitalizeString(lastName);
		
		logger.exiting(className, "getLastName",lastName);
		return lastName;
		
	}
	
}