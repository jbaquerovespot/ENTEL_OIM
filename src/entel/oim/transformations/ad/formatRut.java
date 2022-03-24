package entel.oim.transformations.ad;

import java.util.HashMap;
import java.util.logging.Logger;

public class formatRut {

	private final static String className = formatRut.class.getName();
	private static Logger logger = Logger.getLogger(className);

	@SuppressWarnings("rawtypes")
	public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {

		logger.entering(className, "transform");
		String rut = "";

		if (reqData.containsKey(sField) && reqData.get(sField) != null) {
			rut = (String) reqData.get(sField);
			if (!rut.isEmpty()) {
				rut = rut.replaceAll("[^\\dA-Za-z]", "");
				rut = (rut.startsWith("0")) ? rut.replaceAll("^0+", "") : rut;
			}
		}

		logger.exiting(className, "transform", rut);
		return rut;
	}

}
