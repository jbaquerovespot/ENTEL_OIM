package entel.oim.plugins.eventhandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;


public class UpdateSuccessFactorsLogin implements PostProcessHandler,ConditionalEventHandler {

	private final static String className = UpdateSuccessFactorsLogin.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	
	
	/**
	 * Method for sending notifications to Human Resources when a User is created.
	 * 
	 * @param processId
	 * @param eventId
	 * @param orchestration
	 * @return
	 * 
	 */
	@Override
	public EventResult execute(long processId, long eventId, Orchestration orchestration) {

		logger.entering(className, "execute",processId);
		try {

			System.out.println("Executing CRUD operation "); 
			logger.finer("Getting parameters");
	        HashMap<String, Serializable> parameters = orchestration.getParameters();  
	        
	        logger.finer("Executing Event");
	        executeEvent(parameters,orchestration.getTarget().getType(), orchestration.getTarget().getEntityId());  
	               
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "execute - Unexpected Error",e);
		}
			
		logger.exiting(className, "execute");
		return null; //Async
		
	}
	
	@Override
	public boolean isApplicable(AbstractGenericOrchestration orchestration) {    // Request Context
	    
		logger.entering(className, "isApplicable");
		boolean isValid = false;
		Properties properties = new Properties();
		String migrationOIMPS2;
		
		try {
			
			logger.fine("Checking if it is CREATE operation");
			if (orchestration.getOperation().equals("CREATE")) {
				HashMap<String, Serializable> parameters = orchestration
		                .getParameters();
				
				String origin = getStringParamaterValue(parameters, "Origin");
				logger.finest("Origin: " + origin );
				
				logger.info("Checking if Migration of OIM is active");
				properties.load(new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
				migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");
				
				logger.info("Checking if creation is Employee and we are not in Migration process");
				if (origin != null && origin.equalsIgnoreCase("SSFF-TRUSTED") && !Boolean.parseBoolean(migrationOIMPS2)) {
					isValid = true;
				}
			}
		
		} catch (AccessDeniedException | IOException | SystemConfigurationServiceException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"isApplicable - Unexpected error", e);
			
		}
		
		logger.exiting(className, "isApplicable",isValid);
	    return isValid;
	 
	}
	
	

	/**
	 * Get the string value of an attribute of the user created
	 * @param parameters Map of attributes
	 * @param key Key of the attribute to find
	 * @return String value of the attribute
	 */
	private String getStringParamaterValue(HashMap<String, Serializable> parameters, String key) {
		
		logger.entering(className, "getStringParamaterValue",key);
		String value = null;
		
		logger.fine("Checking if parameters contains the value wanted");
        if(parameters.containsKey(key)){
        	value  = (parameters.get(key) instanceof ContextAware) ? (String) ((ContextAware) parameters
            .get(key)).getObjectValue() : (String) parameters.get(key);
        }
        
        logger.exiting(className, "getStringParamaterValue",value);
        return value;
	}
	
	
	@Override
	public BulkEventResult execute(long arg0, long arg1, BulkOrchestration orch) {
		
		logger.entering(className, "execute");
		
		logger.finer("Executing BULK operation");
        HashMap<String, Serializable>[] bulkParameters = orch.getBulkParameters();  
        
        logger.finer("Getting identities");
        String[] entityIds = orch.getTarget().getAllEntityId();  
           
        logger.finer("Looping over Identitites");
        for (int i = 0; i < bulkParameters.length; i++) {  
        	 logger.finer("Executing event for identity: " + entityIds[i]);
             executeEvent(bulkParameters[i],orch.getTarget().getType(), entityIds[i]);  
        }  
   
        logger.exiting(className, "execute");
        return null; //Async
		
	}
	

	@Override
	public void initialize(HashMap<String, String> arg0) {
	}

	@Override
	public void compensate(long arg0, long arg1, AbstractGenericOrchestration arg2) {
	}

	@Override
	public boolean cancel(long arg0, long arg1, AbstractGenericOrchestration arg2) {
		return false;
	}
	
	
	/**
	 * Execute the event handler
	 * @param parameters Parameters of the entity
	 * @param targetType target Type of the event
	 * @param targetId target id of the event
	 */
	private void executeEvent(HashMap<String, Serializable> parameters, String targetType, String targetId) {  
        
		logger.entering(className, "executeEvent",targetId);
		try {

				logger.finer("Getting Parameters");
				String usrLogin = getStringParamaterValue(parameters, "User Login");
				logger.finer("User Login: " + usrLogin);
				String rut = getStringParamaterValue(parameters, "RUT");
				logger.finer("RUT: " + rut);
				
				logger.finer("Adding the RUT "+ rut + " and User Login " +usrLogin+ " to the pending lookup");
				lookupOprInf.addLookupValue("Lookup.Users.PendingUserLogin",rut,usrLogin,"","");

					
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "executeEvent -Unexpected Error",e);
		}
			
		logger.exiting(className, "executeEvent");

    }

}

