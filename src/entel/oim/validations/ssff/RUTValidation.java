package entel.oim.validations.ssff;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Validates the RUT send in SuccessFactor
 * @author Oracle
 *
 */
public class RUTValidation {

	private final static String className = RUTValidation.class.getName();
	private static Logger logger = Logger.getLogger(className);

	
	@SuppressWarnings("rawtypes")
	public boolean validate(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "validate",sField);

		logger.finer("Getting the RUT for validation: " + sField);
		String rut = (String) reqData.get(sField);
		if (rut != null) {
			rut = rut.toString().trim();
		} else {
			logger.finer("RUT not valid. NULL value found");
			logger.exiting(className, "validate");
			return false;
		}
		logger.finer("RUT to validate: " + rut);

		boolean isValid = rutIsValid(rut);
		logger.finer("RUT is valid: " + isValid);
		
		logger.exiting(className, "validate",isValid);
		return isValid;

	}
	
	
	/**
	 * Validate if a RUT is valid
	 * @param rut Rut to check
	 * @return Flag to determinate if RUT is valid
	 */
	private boolean rutIsValid(String rut) {
		
		logger.entering(className, "rutIsValid",rut);
		boolean isValid = false;
		
		try {
			rut =  rut.toUpperCase().replace(".", "").replace("-", "");
			logger.fine("RUT to check: " + rut);
			
			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
			logger.finest("Rut without verification digit: " + rutAux);
			
			char dv = rut.charAt(rut.length() - 1);
			logger.finest("Verification Digit: " + dv);
			
			logger.fine("Calculating RUT");
			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
					s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			if (dv == (char) (s != 0 ? s + 47 : 75)) {
				logger.fine("The RUT " + rut+ " is valid!");
				isValid = true;
			}
		 
		} catch (java.lang.NumberFormatException e) {
			logger.log(Level.SEVERE,"Rut without verification digit contains non digits characters",e);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Unexpected error in method rutIsValid: " +e.getMessage(),e);
		}
		
		logger.exiting(className, "rutIsValid",isValid);
		return isValid;

	}
	
}
