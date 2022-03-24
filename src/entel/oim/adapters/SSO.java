package entel.oim.adapters;

import java.util.logging.Logger;


/**
 * Contains adapters share by SSO connector
 * 
 * @author Oracle
 *
 */
public class SSO  {
	
	private final static String className = SSO.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	public SSO() {
		super();
	}
	
	/**
	 * Return the upper case of a string
	 * 
	 * @param words
	 * @return  Upper Case of the words
	 */
	public String toTextValue(Object obj) {
		
		logger.entering(className, "toTextValue",obj);
		String value= ""; 
		
		logger.finer("Ckecking if null value");
		if (obj != null) {
			logger.finest("Not null");
			value = String.valueOf(obj);
		}
		
		logger.exiting(className, "toTextValue",value);
		return value;
		
	}

}
