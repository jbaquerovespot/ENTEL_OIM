package entel.oim.connectors.ssff;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import entel.oim.connectors.utilities.SuccessFactorsConnection;


/**
 * Contains all the connector operations for SSFF application
 * @author Oracle
 *
 */
public class OperationsSSFF {

	private final static String className = OperationsSSFF.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	
	/**
     * Finds all the target users of the IT Resource.
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param itResource
     *            the name of the IT Resource.
     * @param roName
     *            the name of the Resource Object
     * @return 
     * 			The found target accounts.
     * @throws Exception 
     */
    public static HashMap<String,Object> getUser(Map<String, String> itResourceDetails,String reconUrl, String customURIs, String userid) throws Exception{
    		
    		
    	logger.entering(className, "getUsers");
    	HashMap<String,Object> attrMap = new HashMap<String,Object>();
    	try {
    		
    		logger.fine("Call to establish connection with SuccessFactors");
			String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
			logger.finest("Access token: " + accessToken);
			
			logger.finer("Constructing the filter for active users");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'"); 
			SimpleDateFormat sdfAsOfDated = new SimpleDateFormat("yyyy-MM-dd");
	    	Calendar cal = Calendar.getInstance();
	    	cal.add(Calendar.DATE, 1);
	    	
	    	logger.finer("getting default filter for active users");
			reconUrl = (reconUrl.replace("(Date)", sdf.format(cal.getTime()))) + "&userId%20eq%20'"+userid+"'";
			
			logger.fine("Calling the User service from Success Factors");
	    	String usersXML = SuccessFactorsConnection.getServiceResponse( itResourceDetails,reconUrl,accessToken);
			logger.finest("Response from Success Factors: " + usersXML);
			
			logger.fine("Constructing user id list");
			List<String> usersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken);
	    	
			logger.fine("Checking if user is candidate");
			if (usersIdList.size() > 0) {
				
				logger.finer("Getting the Configuration Lookup of the Original SuccessFacotrs connetor");
				String[] customURIsArray = customURIs.split("\"(\\s*),(\\s*)\"");
				
				logger.fine("Calling each service for the user: " + userid);
				for (int i=0; i < customURIsArray.length ; i++) {
				
					String customURI = customURIsArray[i].replace("\"", "");
					String alias=customURI.split("=",2)[0];
					String service=customURI.split("=",2)[1].replace("/odata/v2/", "");
					logger.finest("alias: " + alias + " | service: "+ service);
					
					logger.finer("Setting the values of the filter");
					service = service.replace("(Username)", userid);
					service = service +"&fromDate=" + sdfAsOfDated.format(cal.getTime());
					logger.finest("Final filter: " + service);
					
					logger.fine("Calling the "+service+" service from Success Factors");
			    	String responseXML = SuccessFactorsConnection.getServiceResponse( itResourceDetails,service,accessToken);
					logger.finest("Response from Success Factors: " + responseXML);
					
					logger.finer("Saving all the attributes of the current service for the user");
					attrMap.putAll(getAttributeValuesMap(responseXML,alias));
					
				}
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUser - Unexpected error", e);
			throw e;
		}
    	
		logger.exiting(className, "getUser", attrMap.size());
		return attrMap;
    }
    
	
	/**
     * Finds all the target users of the IT Resource.
     * 
     * @param itResourceDetails
     *            the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param itResource
     *            the name of the IT Resource.
     * @param roName
     *            the name of the Resource Object
     * @return 
     * 			The found target accounts.
     * @throws Exception 
     */
    public static HashMap<String,HashMap<String,Object>> getUsers(Map<String, String> itResourceDetails,String reconUrl, String customURIs) throws Exception{
    		
    		
    	logger.entering(className, "getUsers");
    	Set<String> target = new HashSet<>();
    	HashMap<String,HashMap<String,Object>> usersMap = new HashMap<String,HashMap<String,Object>>();
    	try {
    	
	    	logger.fine("Call to establish connection with SuccessFactors");
			String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
			logger.finest("Access token: " + accessToken);
			
			logger.finer("Constructing the filter for active users");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'"); 
			SimpleDateFormat sdfAsOfDated = new SimpleDateFormat("yyyy-MM-dd"); 
			Calendar cal = Calendar.getInstance();
	    	cal.add(Calendar.DATE, 1);
	    	
			logger.finer("getting default filter for active users");
			reconUrl = reconUrl.replace("(Date)", sdf.format(cal.getTime()));
			String serviceRecon = reconUrl.split("\\?")[0];
			
			logger.fine("Calling the User service from Success Factors");
	    	String usersXML = SuccessFactorsConnection.getServiceResponse( itResourceDetails,reconUrl,accessToken);
			logger.finest("Response from Success Factors: " + usersXML);
			
			logger.fine("Constructing user id list");
			List<String> usersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken);
	    	target.addAll(usersIdList);
	    	
	    	logger.fine("Checking if exists more results");
			String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(usersXML);
			while (moreCodeValues != null) {
				
				logger.finest("Extracting the filter for next values");
				String nextResultFilter = moreCodeValues.split(serviceRecon)[1];
				logger.finest("Filter for next values: "+ nextResultFilter);
				
				logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
				usersXML = SuccessFactorsConnection.getServiceResponse(itResourceDetails,"User"+nextResultFilter,accessToken);
				logger.finest("Response from Success Factors: " + usersXML);
				
				logger.fine("Constructing the new user id list");
				List<String> newUsersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken);
				
				logger.fine("Adding the new values to the original Map");
				target.addAll(newUsersIdList);
				
				logger.fine("Checking if exists more results");
				moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(usersXML);
				
			}
			
			logger.log(Level.FINE, "Found [{0}] users", new Object[] { target.size()});
			
			logger.finer("Getting the Configuration Lookup of the Original SuccessFacotrs connetor");
			String[] customURIsArray = customURIs.split("\"(\\s*),(\\s*)\"");
			
			logger.fine("Getting the details of each user");
			for (String userid : target) {
				HashMap<String,Object> attrMap = new HashMap<String,Object>();
				
				logger.fine("Calling each service for the user: " + userid);
				for (int i=0; i < customURIsArray.length ; i++) {
				
					String customURI = customURIsArray[i].replace("\"", "");
					String alias=customURI.split("=",2)[0];
					String service=customURI.split("=",2)[1].replace("/odata/v2/", "");
					logger.finest("alias: " + alias + " | service: "+ service);
					
					logger.finer("Setting the values of the filter");
					service = service.replace("(Username)", userid);
					service = service +"&fromDate=" + sdfAsOfDated.format(cal.getTime());
					logger.finest("Final filter: " + service);
					
					logger.fine("Calling the "+service+" service from Success Factors");
			    	String responseXML = SuccessFactorsConnection.getServiceResponse( itResourceDetails,service,accessToken);
					logger.finest("Response from Success Factors: " + responseXML);
					
					logger.finer("Saving all the attributes of the current service for the user");
					attrMap.putAll(getAttributeValuesMap(responseXML,alias));
					
				}
				
				logger.finer("Saving all the details of the curen user with all the attributes of the user");
				usersMap.put(userid, attrMap);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsers - Unexpected error", e);
			throw e;
		}
    	
		logger.exiting(className, "getUsers", usersMap.size());
		return usersMap;
    }
    
    
    /**
	 * Return the user id list of employee of Success Factors
	 * @param xmlUsers XML response from service User of Success Factors
	 * @param itResourceDetails The it resource details
	 * @param accessToken Token to call the service
	 * @return The user id list (rut) of employee of Success Factors
	 */
	public static List<String> getUsersIdList (String xmlUsers, Map<String, String> itResourceDetails, String accessToken) {
		
		logger.entering(className, "getUsersIdList");
		List<String> usersIdList = new ArrayList<String>();
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUsers));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the userId of the current user");
					Element entry = (Element )list.get(i);
			        Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element userId = (Element) properties.element("userId");
			        
			        logger.finest("Adding the userId: \"" + userId.getStringValue()+ "\" to the list");
					usersIdList.add(userId.getStringValue());
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsersIdList - Unexpected error" ,e);
			
		}
		
		
		logger.exiting(className, "getUsersIdList");
		return usersIdList;
		
	}
	
	
	
	
	
	/**
	 * Return the value of attributes of one service of Success Factors
	 * @param xmlResponse XML response from service User of Success Factors
	 * @return The user attribute map of Success Factors
	 */
	public static HashMap<String,Object> getAttributeValuesMap (String xmlResponse,String alias) {
		
		logger.entering(className, "getAttributeValuesMap");
		HashMap<String,Object> userAttrMap = new HashMap<String,Object>();
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlResponse));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Constructing the list of Entries");
			List<Element> elementList = new ArrayList<Element>();
			for (Node node : list ) {
				if ( "entry".equals(node.getName())) {
					elementList.add((Element) node);
				}
			}
			
			logger.fine("Processing the list of Entries");
			processEntryElements(userAttrMap,alias, elementList);
        	
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getAttributeValuesMap - Unexpected error" ,e);
			
		}
		
		
		logger.exiting(className, "getAttributeValuesMap",userAttrMap);
		return userAttrMap;
		
	}
	
	
	/**
	 * Convert a String date to Long value format
	 * @param dateValue
	 * @return
	 * @throws ParseException 
	 */
	private static Long convertLongFormat(String dateValue) throws ParseException {
	 
		logger.entering(className, "convertLongFormat", dateValue);
		Long dateLong = null;
		
		if (dateValue != null && !dateValue.isEmpty()) {
			logger.finest("Parsing date...");
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    Date date = formater.parse(dateValue);
			
			logger.finest("Getting Long value...");
			dateLong = Long.valueOf(date.getTime());
		}
		
		logger.exiting(className, "convertLongFormat",dateLong);
	    return dateLong;
	  }
	
	
	
	/**
	 * Process the list of element in the response of a service in SuccessFactors
	 * @param userAttrMap Map to add the property values
	 * @param alias Alias of the property to save
	 * @param entryList List of entry element to process
	 * @throws ParseException 
	 */
	private static void processEntryElements(HashMap<String,Object> userAttrMap,String alias, List<Element> entryList) throws ParseException {
		
		try {
			logger.entering(className, "processEntryElements");
			
			logger.fine("Loop over the elements in the XML");
			for (int i = 0; i < entryList.size(); i++) {
					
				logger.fine("Get the element entry");
				Element entry = (Element) entryList.get(i);
				
				logger.fine("Get the elements of the response");
				addPropertiesValues(userAttrMap,alias, entry);
				
		        logger.finer("Looping over all link attributes");
		        List<Element> linkElements= (List<Element>) entry.elements("link"); 
		        for (Element link : linkElements) {
		        	logger.finer("Checking if inline found");
		        	Element inline = link.element("inline");
		        	if ( inline != null) {
		        		String title= link.attributeValue("title");
		        		logger.finest("Inline attribute found: " + title);
			        	
		        		logger.fine("Get the elements of the inline element");
						processEntryElements(userAttrMap,alias+"."+title, inline.elements("entry"));
			        	
			        }
			        
		        }
		    
			}
			logger.exiting(className, "processEntryElements");
			
		} catch (ParseException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error - processEntryElements", e);
			throw e;
		}
		
	}
	
	
	/**
	 * Add all the properties values of one entry
	 * @param userAttrMap Map to add the property values
	 * @param alias Alias of the property to save
	 * @param entry Entry element to analyze
	 * @throws ParseException
	 */
	private static void addPropertiesValues(HashMap<String,Object> userAttrMap,String alias, Element entry) throws ParseException {
		
		try {
			logger.entering(className, "addPropertiesValues",entry);
			logger.fine("Get the elements of the response");
			Element content = (Element) entry.element("content");
	        Element properties = (Element) content.element("properties");
	        List<Element> elementList = properties.elements();
	        
	        logger.finer("Looping over all the attributes");
	        for (Element element : elementList) {
	        	String name = element.getName();
	        	String value = element.getStringValue();
	
	        	if (name.toUpperCase().contains("DATE")) {
	        		logger.finest("Converting to Long value");
	        		Long valueDate;
					
						valueDate = convertLongFormat(value);
					
	        		
	        		logger.finest("Adding Date attribute: " + alias + "." + name + " with value " + value);
		        	userAttrMap.put(alias + "." + name, valueDate);
		        	
	        	} else {
	        		
	        		logger.finest("Adding attribute: " + alias + "." + name + " with value " + value);
		        	userAttrMap.put(alias + "." + name, value);
		        }
	        	
	        	logger.exiting(className, "addPropertiesValues");
	        	
	        }
        
		} catch (ParseException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error - addPropertiesValues", e);
			throw e;
		}
        
	}
	
}
