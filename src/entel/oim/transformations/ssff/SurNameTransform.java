package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;

import entel.oim.adapters.Utilities;
import entel.oim.plugins.namegenerator.CustomUsernamePolicy;

/**
 * Transform the SurName from SuccessFactors
 * @author Oracle
 *
 */
public class SurNameTransform {

	private final static String className = SurNameTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String lastnames = (String) reqData.get(sField);
		
		logger.fine("Calling the getLastName method");
		String surName = getSurName(lastnames);
		
		logger.exiting(className, "transform",surName);
		return surName;
	}
	
	
	/**
	 * Split the lastnames and get the sur name of a person
	 * @param names List of names of the users
	 * @return Sur Name
	 * @author Oracle
	 */
	public static String getSurName(String lastnames) {
		
		logger.entering(className, "getSurName",lastnames);
		String surName = "";
		
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
						
						logger.finer("Checking if " + words[i]+ " is reserved word");
						if (!CustomUsernamePolicy.RESERVED_WORDS_LIST.contains(words[i])) {
							
							logger.finest("It is not reserved. Looping over the rest over the lastnames");
							for (int j = i+1; j< words.length; j++) {
								surName = surName + " " + words[j];
								logger.finest("Current surName: " + surName);
							}
							surName = surName.trim();
							logger.finest("Final surName: " + surName);
							break;
						} 
						
					}

				} else {
					logger.fine("Only two lastnames. We take the last one");
					surName = lastnames.substring(lastnames.lastIndexOf(" ")).trim();
				}
				
			}
			
		}
		
		logger.finer("Capitalizing String...");
		Utilities util = new Utilities();
		surName = util.toCapitalizeString(surName);
		
		logger.exiting(className, "getSurName",surName);
		return surName;
		
	}
	
}