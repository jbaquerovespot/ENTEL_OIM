package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;

import entel.oim.adapters.Utilities;
import entel.oim.plugins.namegenerator.CustomUsernamePolicy;

/**
 * Transform the MiddleName from SuccessFactors
 * @author Oracle
 *
 */
public class MiddleNameTransform {

	private final static String className = MiddleNameTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String names = (String) reqData.get(sField);
		
		logger.fine("Calling the getMiddleName method");
		String middleName = 	getMiddleName(names);
		
		logger.exiting(className, "transform",middleName);
		return middleName;
	}
	
	
	/**
	 * Split the name and get the middle name of a person
	 * @param names List of names of the users
	 * @return Middle Name
	 * @author Oracle
	 */
	public static String getMiddleName(String names) {
		
		logger.entering(className, "getMiddleName",names);
		String middleName = "";
		
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
						
						logger.finer("Checking if " + words[i]+ " is reserved word");
						if (!CustomUsernamePolicy.RESERVED_WORDS_LIST.contains(words[i])) {
							
							logger.finest("It is not reserved. Looping over the rest over the names");
							for (int j = i+1; j< words.length; j++) {
								middleName = middleName + " " + words[j];
								logger.finest("Current middleName: " + middleName);
							}
							middleName = middleName.trim();
							logger.finest("Final middleName: " + middleName);
							break;
						} 
					
					}
					
				} else {
					logger.fine("Only two names. We take the last one");
					middleName = names.substring(names.lastIndexOf(" ")).trim();
				}
				
			} 
		}
		
		logger.finer("Capitalizing String...");
		Utilities util = new Utilities();
		middleName = util.toCapitalizeString(middleName);
		
		logger.exiting(className, "getMiddleName",middleName);
		return middleName;
		
	}
	
}