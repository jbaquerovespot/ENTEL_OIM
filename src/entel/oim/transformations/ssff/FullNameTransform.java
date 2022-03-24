package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;
import entel.oim.adapters.Utilities;


/**
 * Transform the FullName from SuccessFactors
 * @author Oracle
 *
 */
public class FullNameTransform {

	private final static String className = FullNameTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String fullName = (String) reqData.get(sField);
		
		logger.finer("Capitalizing String...");
		Utilities util = new Utilities();
		fullName = util.toCapitalizeString(fullName);
		
		logger.exiting(className, "transform",fullName);
		return fullName;
	}
	
	
}