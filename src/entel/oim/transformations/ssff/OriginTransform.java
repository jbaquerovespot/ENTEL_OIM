package entel.oim.transformations.ssff;

import java.util.HashMap;
import java.util.logging.Logger;


/**
 * Transform the Origin of an user by SuccessFactors
 * @author Oracle
 *
 */
public class OriginTransform {

	private final static String className = OriginTransform.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	// Origins of creation of users
	public final static String SUCCESSFACTORS_ENTEL = "SSFF-TRUSTED";
	
	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {
		logger.entering(className, "transform");
		logger.exiting(className, "transform",SUCCESSFACTORS_ENTEL);
		return SUCCESSFACTORS_ENTEL;
	}
}
