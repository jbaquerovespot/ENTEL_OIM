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
 * Execute the job to get the values for lookups from SuccessFactors Connector
 * @author Oracle
 *
 */
public class CustomLookupsSuccessFactorsReconciliationTask extends TaskSupport {
	
	private final static String className = CustomLookupsSuccessFactorsReconciliationTask.class.getName();
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
		String service = (String) hm.get("Service");
		logger.log(Level.FINER, "Getting details of IT Resource [{0}]", itResource);
		Map<String, String> itResourceDetails = getITResourceDetails(itResource);

		logger.log(Level.FINE, "Processing all the lookups...");
		processLookupsValues(itResourceDetails, lookupDefinition, service);
		
		logger.exiting(className, "execute");

	}
	
	
	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
	
	
	/**
     * Finds update all the lookups from Success Factors
     * @param itResourceDetails
     * 			  the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param lookupDefinition
     * 			  Lookup that update
     * @param service
     * 			  Service to call for lookups. 
     * @throws Exception
     */
    private void processLookupsValues(Map<String, String> itResourceDetails, String lookupDefinition, String service)
            throws Exception {

    	logger.entering(className, "processLookupsValues", service);
    	
    	logger.fine("Call to establish connection with SuccessFactors");
		String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);
		
		logger.fine("Call the update Lookup Values from " + lookupDefinition);
		updateLookupsValues(itResourceDetails,
							service,
							accessToken,
							lookupDefinition);
		
		logger.exiting(className, "processLookupsValues", service);
    }
    

    /**
     * Update the lookup values
     * @param itResourceDetails
     * 			  the map of IT Resource details obtained from the
     *            getITResourceDetails(String) method.
     * @param service 
     * 				Service to call in SuccessFactors
     * @param accessToken 
     * 				Token to invoke the service of SuccessFactors
     * @param lookupName 
     * 				Lookup to update.
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	private void updateLookupsValues (Map<String, String> itResourceDetails ,String service, String accessToken, String lookupName) throws Exception {
    	
    	logger.entering(className, "updateLookupsValues",lookupName);
    	
    	logger.fine("Calling the "+service+" service from Success Factors");
    	String xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service,accessToken);
		logger.finest("Response from Success Factors: " + xmlService);
		
		logger.fine("Constructing the Map");
		HashMap<String, String> hmap = getMapCodeValues(xmlService);
		
		logger.fine("Checking if exists more results");
		String moreCodeValues = SuccessFactorsConnection.getMoreCodeValues(xmlService);
		while (moreCodeValues != null) {
			
			logger.finest("Extracting the filter for next values");
			service = service.split("\\?")[0];
			String nextResultFilter = moreCodeValues.split(service)[1];
			logger.finest("Filter for next values: "+ nextResultFilter);
			
			logger.fine("Calling the next values from "+moreCodeValues+" service from Success Factors");
			xmlService = SuccessFactorsConnection.getServiceResponse(itResourceDetails,service+nextResultFilter,accessToken);
			
			logger.finest("Response from Success Factors: " + xmlService);
			
			logger.fine("Constructing the Map for new values");
			HashMap<String, String> nextHmap = getMapCodeValues(xmlService);
			
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
			pltfSrv.purgeCache("All");
			
		} catch (tcAPIException | tcInvalidLookupException | tcInvalidValueException | tcColumnNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error", e);
			throw e;
		} 
		
		logger.exiting(className, "updateLookupsValues",lookupName);
	    
    }
    
    
    /**
	 * Return the map of the xml of Success Factors
	 * @param xml 
	 * 			XML response from service of Success Factors
	 * @return 
	 * 			A map with values of Success Factors
     * @throws DocumentException 
	 */
	private static HashMap<String, String> getMapCodeValues (String xml) throws DocumentException {
		
		logger.entering(className, "getMapCodeValues");
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
					
					logger.fine("Get the code and name of the current element");
					Element entry = (Element )list.get(i);
			        Element content = (Element) entry.element("content");
			        Element properties = (Element) content.element("properties");
			        
			        logger.finer("Getting externalCode");
			        Element externalCode = (Element) properties.element("externalCode");
			        if (externalCode == null) {
			        	externalCode = (Element) properties.element("code");
			        }
			        
			        logger.finer("Getting Name");
			        Element name = (Element) properties.element("name");
			        if (name == null) {
			        	name = (Element) properties.element("externalName_defaultValue");
			        	if (name == null) {
				        	name = (Element) properties.element("label_defaultValue");
				        }
			        }
			        
			        logger.finer("Getting Status");
			        Element status = (Element) properties.element("status");
			        if (status == null) {
			        	status = (Element) properties.element("mdfSystemStatus");
			        	if (status == null) {
			        		status = (Element) properties.element("effectiveStatus");
				        }
			        	
			        }
			        
			        if ("A".equalsIgnoreCase(status.getStringValue())) {
			        	logger.finest("Adding the element: \"" + name.getStringValue()+ "\" to the map");
						hmap.put(externalCode.getStringValue(), name.getStringValue());
			        } else {
			        	logger.finest("Skipping the element: \"" + name.getStringValue()+ "\" because is Inactive");
					}
			        
				}
				
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getMapCodeValues - Unexpected error: ", e);
			throw e;
		}
		
		logger.exiting(className, "getMapCodeValues");
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
