package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;


/**
 * Transform the Status from SuccessFactors
 * @author Oracle
 *
 */ 
public class StatusTransform {

	private final static String className = StatusTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		
		logger.fine("Getting the values of fields");
		String status = (String) reqData.get(sField);
		
		logger.fine("Original Status: " + status);
		if (status != null && status.equalsIgnoreCase("Enabled")) {
			logger.finer("Changing status to Active");
			status = "Active";
		}
		
		logger.exiting(className, "transform", status);
		return status;
	}
	
	
}