package entel.oim.validations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Validates the Job send in SuccessFactor
 * @author Oracle
 *
 */
public class JobValidation {

	private final static String className = JobValidation.class.getName();
	private static Logger logger = Logger.getLogger(className);

	
	@SuppressWarnings("rawtypes")
	public boolean validate(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "validate",sField);

		logger.finer("Getting the Job for validation: " + sField);
		String job = (String) reqData.get(sField);
		boolean isValid = true;
		
		logger.finest("Checking value of Job");
		if (job == null || job.isEmpty()) {
			isValid = false;
		}
		
		logger.exiting(className, "validate",isValid);
		return isValid;

	}
		
}
