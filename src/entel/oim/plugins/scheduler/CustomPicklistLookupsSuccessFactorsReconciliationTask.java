package entel.oim.plugins.scheduler;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;

import entel.oim.connectors.utilities.SuccessFactorsConnection;
import oracle.iam.platform.Platform;
import oracle.iam.platformservice.api.PlatformUtilsService;
import oracle.iam.scheduler.vo.TaskSupport;


/**
 * Execute the job to get the values for picklist lookups from SuccessFactors Connector
 * @author Oracle
 *
 */
public class CustomPicklistLookupsSuccessFactorsReconciliationTask extends TaskSupport {
	
	private final static String className = CustomPicklistLookupsSuccessFactorsReconciliationTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

    private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
    private final PlatformUtilsService pltfSrv = Platform.getService(PlatformUtilsService.class);
    private final tcITResourceInstanceOperationsIntf itResOps = Platform.getService(tcITResourceInstanceOperationsIntf.class);


	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
	
		logger.finer("Loading parameters");
		String itResource = (String) hm.get("IT Resource Name");
		String lookupDefinition = (String) hm.get("Lookup Definition");
		String picklist = (String) hm.get("Picklist");
		
		logger.log(Level.FINER, "Getting details of IT Resource [{0}]", itResource);
		Map<String, String> itResourceDetails = getITResourceDetails(itResource);

		logger.log(Level.FINE, "Processing the lookup...");
		processLookupsValues(itResourceDetails, lookupDefinition,picklist);
		
		logger.exiting(className, "execute");

	}
	

	/**
	 * Process the lookup values
	 * @param itResourceDetails
	 * 			  the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
	 * @param lookupDefinition
	 * 				Lookup to update
	 * @param picklist
	 * 				Picklist to check for values
	 * @throws Exception
	 */
    private void processLookupsValues(Map<String, String> itResourceDetails, String lookupDefinition, String picklist)
            throws Exception {

    	logger.entering(className, "processLookupsValues", picklist);
    	
    	logger.fine("Call to establish connection with SuccessFactors");
		String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
		logger.finest("Access token: " + accessToken);
		
		logger.fine("Call the update Lookup Values from " + lookupDefinition);
		updateLookupsValues(itResourceDetails,picklist,accessToken,lookupDefinition);
		
		logger.exiting(className, "processLookupsValues",picklist);
    }
    
    
    /**
     * Update the lookup values
     * @param itResourceDetails
	 * 			    the map of IT Resource details obtained from the
	 * 				getITResourceDetails(String) method.
     * @param picklist 
     * 				Picklist to check for values
     * @param accessToken 
     * 				Token to invoke the service of SuccessFactors
     * @param lookupName 
     * 				Lookup to update.
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	private void updateLookupsValues (Map<String, String> itResourceDetails,String picklist, String accessToken, String lookupName) throws Exception {
    	
    	logger.entering(className, "updateLookupsValues",lookupName);
    	
    	logger.fine("Calling the Picklist "+picklist+" service from Success Factors");
		String xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,"Picklist('"+picklist+"')/picklistOptions",accessToken);
		logger.finest("Response from Success Factors: " + xmlService);
		
		logger.fine("Constructing the Map");
		HashMap<String, String> hmap = getPicklist(itResourceDetails,picklist, accessToken, lookupName, xmlService);
		
		logger.fine("Checking if exists more results");
		String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
		while (moreCodeValues != null) {
			
			logger.finest("Extracting the filter for next values");
			String nextResultFilter = moreCodeValues.split("Picklist")[1];
			logger.finest("Filter for next values: "+ nextResultFilter);
			
			logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
			xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,"Picklist"+nextResultFilter,accessToken);
			logger.finest("Response from Success Factors: " + xmlService);
			
			
			logger.fine("Constructing the Map for new values");
			HashMap<String, String> nextHmap = getPicklist(itResourceDetails,picklist, accessToken, lookupName, xmlService);
			
			logger.fine("Adding the new values to the original Map");
			hmap.putAll(nextHmap);
			
			logger.fine("Checking if exists more results");
			moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
			
		}
		
		try {
			
			logger.fine("Getting lookup values from  "+ lookupName);
			tcResultSet rs = lookupOprInf.getLookupValues(lookupName);
			
			for (int i=0;i<rs.getRowCount();i++) {
				rs.goToRow(i);
				logger.finest("Deleting  "+ rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
			    lookupOprInf.removeLookupValue(lookupName,rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
			    
			}
			
			logger.fine("Let's sort the map in ascending order of value");
			HashMap<String, String> sorted = hmap
						.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

			for (Map.Entry me : sorted.entrySet()) {
		          logger.finest("Adding lookup-> Key: "+me.getKey() + " & Value: " + me.getValue());
		          lookupOprInf.addLookupValue(lookupName,me.getKey().toString(),me.getValue().toString(),"","");
	        }
			
			logger.finer("Purging the Lookup Vaues from OIM");
			pltfSrv.purgeCache("LookupValues");
			
		} catch (tcAPIException | tcInvalidLookupException | tcInvalidValueException | tcColumnNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateLookupsValues - Unexpected error ", e);
			throw e;
		} 
		
		logger.exiting(className, "updateLookupsValues",lookupName);
	    
    }
    
    
    

    
    /**
     * Get the map with all the options for a picklist
     * @param itResourceDetails
	 * 			    the map of IT Resource details obtained from the
	 * 				getITResourceDetails(String) method.
     * @param picklist 
     * 				Picklist to check for values
     * @param accessToken 
     * 				Token to invoke the service of SuccessFactors
     * @param lookupName 
     * 				Lookup to update.
     * @param xml
     * 				XML Response from Picklist service
     * @return 
     * 			Map with code/values for picklist in SuccessFactors
     * @throws Exception
     */
	public static HashMap<String, String> getPicklist (Map<String, String> itResourceDetails,String picklist, String accessToken, String lookupName, String xml) throws Exception {
		
		logger.entering(className, "getPicklist");
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xml));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the id and status of the current element");
					Element entry = (Element )list.get(i);
			        Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element status = (Element) properties.element("status");
			        Element id = (Element) properties.element("id");
			        			        
			        if ("ACTIVE".equalsIgnoreCase(status.getStringValue())) {
			        	
			        	logger.finest("Getting labels for option: \"" + id.getStringValue());
						HashMap<String, String> hmapOptions = getOptionsLabelValues (itResourceDetails,"PicklistOption", "picklistLabels", id.getStringValue(), accessToken);
			        	
						logger.finer("Adding the new labels to the map");
			        	hmap.putAll(hmapOptions);
						
			        } else {
			        	logger.finest("Skipping the element: \"" + id.getStringValue()+ "\" because is Inactive");
					}
			        
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getPicklist - Unexpected error", e);
			throw e;
		}
		
		logger.exiting(className, "getPicklist");
		return hmap;
		
	}

	
	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
	
	

    /**
     * Get all the labels for a specific option of the picklist
     * @param itResourceDetails
	 * 			    the map of IT Resource details obtained from the
	 * 				getITResourceDetails(String) method.
     * @param service 
     * 				Service to call in SuccessFactors
     * @param subservice 
     * 				Subservice to call in SuccessFactors
     * @param picklist 
     * 				Picklist to check for values
     * @param accessToken 
     * 				Token to invoke the service of SuccessFactors
     * @return 
     * 				Map of all the label for a specific option of the picklist
     * @throws Exception
     */
    private static HashMap<String, String> getOptionsLabelValues (Map<String, String> itResourceDetails,String service, String subservice,String picklist, String accessToken) throws Exception {
    	
    	logger.entering(className, "getOptionsLabelValues",picklist);
    	
    	logger.fine("Calling the Picklist "+picklist+" service from Success Factors");
		String xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service+"('"+picklist+"')/"+subservice,accessToken);
		logger.finest("Response from Success Factors: " + xmlService);
		
		logger.fine("Constructing the Map");
		HashMap<String, String> hmap = getMapCodeLabelsValues(xmlService);
		
		logger.fine("Checking if exists more results");
		String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
		while (moreCodeValues != null) {
			
			logger.finest("Extracting the filter for next values");
			String nextResultFilter = moreCodeValues.split(service)[1];
			logger.finest("Filter for next values: "+ nextResultFilter);
			
			logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
			xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service+nextResultFilter,accessToken);
			
			logger.finest("Response from Success Factors: " + xmlService);
			
			logger.fine("Constructing the Map for new values");
			HashMap<String, String> nextHmap = getMapCodeLabelsValues(xmlService);
			
			logger.fine("Adding the new values to the original Map");
			hmap.putAll(nextHmap);
			
			logger.fine("Checking if exists more results");
			moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
			
		}
		
		logger.exiting(className, "getOptionsLabelValues",picklist);
		return hmap;
	    
    }
    
    
    
    /**
	 * Return the map of the xml of Success Factors
	 * @param xml 
	 * 			XML response from service of Success Factors
	 * @return 
	 * 			A map with values of Success Factors
     * @throws DocumentException 
	 */
	public static HashMap<String, String> getMapCodeLabelsValues (String xml) throws DocumentException {
		
		logger.entering(className, "getMapCodeLabelsValues");
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xml));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "entry".equals(list.get(i).getName())) {
					
					logger.fine("Get the locale,optionId and label of the current element");
					Element entry = (Element )list.get(i);
			        Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        Element locale = (Element) properties.element("locale");
			        Element optionId = (Element) properties.element("optionId");
			        Element label = (Element) properties.element("label");
			        			        
			        if ("es_ES".equalsIgnoreCase(locale.getStringValue())) {
			        	
			        	logger.finest("Adding the element: \"" + label.getStringValue()+ "\" to the map");
						hmap.put(optionId.getStringValue(), label.getStringValue());
						break;
						
					} else {
			        	logger.finest("Skipping the element: \"" + optionId.getStringValue()+ "\" because is not spanish");
					}
			        
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getMapCodeLabelsValues - Unexpected error: ", e);
			throw e;
		}
		
		logger.exiting(className, "getMapCodeLabelsValues");
		return hmap;
		
	}
	
	/**
     * Finds the specified IT Resource details
     * 
     * @param itResourceName
     *            the name of the IT Resource to find. Case sensitive
     * @return a map with the found IT Resource details
     * @throws tcAPIException
     * @throws tcColumnNotFoundException
     * @throws tcITResourceNotFoundException
     */
    public Map<String, String> getITResourceDetails(String itResourceName)
            throws tcAPIException, tcColumnNotFoundException, tcITResourceNotFoundException {

    	logger.entering(className, "getITResourceDetails");
    	
		HashMap<String, String> searchcriteria = new HashMap<>();
		searchcriteria.put("IT Resources.Name", itResourceName);
	
		tcResultSet trs = itResOps.findITResourceInstances(searchcriteria);
		int rowCount = trs.getRowCount();
		long itResourceKey = -1;
	
		if (rowCount > 0) {
		    trs.goToRow(0);
		    itResourceKey = trs.getLongValue("IT Resource.Key");
		}

		Map<String, String> itAttrib = new HashMap<>();
		if (itResourceKey > 0) {
		    tcResultSet paramsRs = itResOps.getITResourceInstanceParameters(itResourceKey);
		    for (int i = 0; i < paramsRs.getRowCount(); i++) {
			paramsRs.goToRow(i);
	
			String name = paramsRs.getStringValue("IT Resources Type Parameter.Name");
			String value = paramsRs.getStringValue("IT Resources Type Parameter Value.Value");
	
			itAttrib.put(name, value);
		    }
		}
		
		logger.exiting(className, "getITResourceDetails");
		return itAttrib;
    }

    
}
