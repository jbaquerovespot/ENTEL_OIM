package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;
import entel.oim.adapters.Utilities;


/**
 * Transform the Telephone from SuccessFactors
 * @author Oracle
 *
 */ 
public class BusinessPhoneTransform {

	private final static String className = BusinessPhoneTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String  telephone= (String) reqData.get(sField);
		
		if (telephone != null && !telephone.isEmpty()) {
			logger.finer("Replacing all non numeric values");
			telephone = telephone.replaceAll("[^\\d]", "");
		}
		
		logger.exiting(className, "transform",telephone);
		return telephone;
	}
	
	
}