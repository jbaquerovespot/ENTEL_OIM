package entel.oim.plugins.eventhandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PreProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;


/**
 * EventHandler that fill the information of Responsible Entel for external users
 * @author Oracle
 *
 */
public class AddEntelAttributes implements PreProcessHandler,ConditionalEventHandler {

	private final static String className = AddEntelAttributes.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	private static UserManager usrMgr = Platform.getService(UserManager.class);
	private static OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
	private AuthenticatedSelfService authSelfServ = Platform.getService(AuthenticatedSelfService.class);
    
	
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

			logger.info("Getting Parameters");
			HashMap<String, Serializable> parameters = orchestration.getParameters();
			
			String gestor = getStringParamaterValue(parameters, "Gestor");
			logger.info("Gestor: " + gestor);
			
			
			logger.fine("Checking if gestor found");
			if (gestor != null) {
			
				logger.finest("Setting the attributes to find");
				Set<String> searchAttrs = new HashSet<String>();
				searchAttrs.add("VicePresidency");
				searchAttrs.add("DivisionalManagement");
				searchAttrs.add("AreaManagement");
				searchAttrs.add("DepartamentManagement");
				searchAttrs.add("Submanagement");
				searchAttrs.add("Area");
			  
				logger.info("Getting Gestor info");
				User mgr = usrMgr.getDetails(gestor, searchAttrs, true);
				HashMap<String,Object> mapAttrs = mgr.getAttributes();
				
				
				logger.info("Setting values of the Gestor: " + mgr.getLogin());
				orchestration.addParameter("VicePresidency", (String) mapAttrs.get("VicePresidency"));
				orchestration.addParameter("DivisionalManagement", (String) mapAttrs.get("DivisionalManagement"));
				orchestration.addParameter("AreaManagement", (String) mapAttrs.get("AreaManagement"));
				orchestration.addParameter("DepartamentManagement", (String) mapAttrs.get("DepartamentManagement"));
				orchestration.addParameter("Submanagement", (String) mapAttrs.get("Submanagement"));
				orchestration.addParameter("Area", (String) mapAttrs.get("Area"));
				orchestration.addParameter("Generation Qualifier", "EXT");
				
				
				logger.finest("Getting the logged in user info");
				Set<String> userAttrs = new HashSet<String>();
				userAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
				User userInfo= authSelfServ.getProfileDetails(userAttrs);
				
				logger.info("Setting values of the responsible: " + userInfo.getLogin());
				orchestration.addParameter("CreatedBy", (String) userInfo.getLogin());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "execute - Unexpected error", e);
		}
			
		logger.exiting(className, "execute");
		return new EventResult();
		
	}
	
	@Override
	public boolean isApplicable(AbstractGenericOrchestration orchestration) {    // Request Context
	    
		logger.entering(className, "isApplicable");
		boolean isValid = false;
		
		try {
			
			logger.fine("Checking if it is CREATE operation");
			if (orchestration.getOperation().equals("CREATE")) {
				HashMap<String, Serializable> parameters = orchestration
		                .getParameters();
				
				long orgKey = getLongParamaterValue(parameters, "act_key");
				logger.finest("OrgKey: " +Long.toString(orgKey) );
				
				logger.finest("Setting the attributes to find of the organization");
				Set<String> searchAttrs = new HashSet<String>();
				searchAttrs.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
				searchAttrs.add(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId());
				
				Organization org = orgMgr.getDetails(Long.toString(orgKey), searchAttrs, false);
				logger.finest("Organization Name :: " + org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
				logger.finest("Parent Organization Name :: " + org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId()));
				
		        logger.info("Checking is creation is Extern");
				if ("Externos".equals(org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId()))) {
					isValid = true;
				}
			}
		
		} catch (OrganizationManagerException | AccessDeniedException e) {
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
	
	
	/**
	 * Get the string value of an attribute of the user created
	 * @param parameters Map of attributes
	 * @param key Key of the attribute to find
	 * @return String value of the attribute
	 */
	private long getLongParamaterValue(HashMap<String, Serializable> parameters, String key) {
		
		logger.entering(className, "getLongParamaterValue",key);
		long value = -1;
		
		logger.fine("Checking if parameters contains the value wanted");
        if(parameters.containsKey(key)){
        	value  = (parameters.get(key) instanceof ContextAware) ? (long) ((ContextAware) parameters
            .get(key)).getObjectValue() : (long) parameters.get(key);
        }
        
        logger.exiting(className, "getLongParamaterValue",value);
        return value;
	}


	@Override
	public BulkEventResult execute(long arg0, long arg1, BulkOrchestration arg2) {
		return null;
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

}

