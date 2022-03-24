package entel.oim.transformations.ad;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MilisDateTransform {

                private final static String className = LongDateTransform.class.getName();
                private static Logger logger = Logger.getLogger(className);

                /**
                * Transform to get the date in a String (dd-MM-yyyy)
                * 
                 * @param reqData
                * @param hmEntitlementDetails
                * @param sField
                * @return
                */
                @SuppressWarnings("rawtypes")
                public Object transform(HashMap reqData, HashMap hmEntitlementDetails, String sField) {

                                logger.entering(className, "transform");

                                String dateTransform = "";
                                String timeStamp = (String) reqData.get(sField);
                                if (timeStamp != null && !timeStamp.isEmpty() && !timeStamp.equalsIgnoreCase("0")) {
                                                try {
                                                                long fileTime = (Long.parseLong(timeStamp));
                                                                Date lastLogonDate = new Date(fileTime);
                                                                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
                                                                dateTransform = DATE_FORMAT.format(lastLogonDate);
                                                                return dateTransform;
                                                } catch (Exception e) {
                                                                logger.log(Level.SEVERE, null, e);
                                                } finally {
                                                                logger.exiting(className, "transform", dateTransform);
                                                }
                                }
                                logger.exiting(className, "transform");
                                return dateTransform;
                }

}
