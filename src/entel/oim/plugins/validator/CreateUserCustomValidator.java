 package entel.oim.plugins.validator;

import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ID_FIELD;
import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ORG_NAME;
import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_KEY;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserMembershipException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platformservice.api.AdminRoleService;
import oracle.iam.request.exception.InvalidRequestDataException;
import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.RequestEntity;
import oracle.iam.request.vo.RequestEntityAttribute;
import oracle.iam.requestactions.plugins.datavalidator.CreateUserDataValidator;
import oracle.iam.selfservice.exception.UserLookupException;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;


/**
 * 
 * Execute the custom validations of the create and update user operation
* @author Oracle
 */
public class CreateUserCustomValidator extends CreateUserDataValidator
{
	private final static String className = CreateUserCustomValidator.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	private static OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
	private static UserManager usrMgr = Platform.getService(UserManager.class);
	private static tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private static AuthenticatedSelfService authSelfSvc = Platform.getService(AuthenticatedSelfService.class);
	private static RoleManager roleMgr = Platform.getService(RoleManager.class);
	private static AdminRoleService adminRoleMgr= Platform.getService(AdminRoleService.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private final static String LOOKUP_USERS_INTERNALCONTROLLIST = "Lookup.Users.InternalControlList";
	
    /**
     * Check if the Organization is forbidden to create.
     * @param actKey   
     * 			Organization Key to ckeck
     * @return true if organization is forbidden. false if not.
     */
    public boolean isForbidden(long actKey) 
    {
    	logger.entering(className, "isForbidden", actKey);
    	boolean isForbidden = false;
        
    	try {
    		
	        logger.finest("Setting the attributes to find of the organization");
			Set<String> searchAttrs = new HashSet<String>();
			searchAttrs.add(ORG_NAME.getId());
			
			logger.finest("Getting Organization Name");
			Organization org = orgMgr.getDetails(ID_FIELD.getId(), actKey, searchAttrs);
			HashMap<String,Object> mapAttrs = org.getAttributes();
	        String orgName = (String) mapAttrs.get(ORG_NAME.getId());
	        
	        logger.finest("Checking if Empleados or Externos");
			if (orgName.equalsIgnoreCase("Externos")) {
					isForbidden = true;
			
			} else if (orgName.equalsIgnoreCase("Empleados")) {
				
				logger.finest("Getting the attributes of the authenticated user");
				Set<String> searchAttrsUsr = new HashSet<String>();
				searchAttrsUsr.add(USER_KEY.getId());
		        User authUser = authSelfSvc.getProfileDetails(searchAttrsUsr);
		        
		        logger.finest("Getting the roles of the authenticated user");
		        List<Role> rolesList= roleMgr.getUserMemberships(authUser.getEntityId(), true);
		        
		        logger.finest("Getting the AdminRoles of the authenticated user");
		        List<AdminRole> adminRolesList = adminRoleMgr.getAdminRolesForUser(authUser.getEntityId(), null);
		        
		        logger.finest("Checking if user is Admin");
		        boolean isAdmin=false;
		        for (Role role : rolesList) {
		        	if (role.getName().equals("OPERACIONES") || role.getName().equals("SYSTEM ADMINISTRATORS")) {
		        		isAdmin = true;
		        		break;
		        	}
		        }
		        for (AdminRole role : adminRolesList) {
		        	if (role.getRoleName().equals("OrclOIMSystemAdministrator")) {
		        		isAdmin = true;
		        		break;
		        	}
		        }
		        if (!isAdmin) {
		        	logger.finest("User is not Admin");
		        	isForbidden = true;
		        }
		        
		    }
			
		} catch (SearchKeyNotUniqueException | OrganizationManagerException | AccessDeniedException | UserLookupException | UserMembershipException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"isForbidden - Unexpected error",e);
		}
		
        logger.exiting(className, "isForbidden", isForbidden);
        return isForbidden;
    }
    
    

    /**
     * Performs validation.
     * @param reqData
     * @throws InvalidRequestDataException 
     */
    public void validate(RequestData reqData) throws InvalidRequestDataException { 
    
        logger.entering(className, "validate", new Object[]{reqData});
        boolean isForbidden = false;
        boolean isInternalControlList = false;
        boolean isInvalidEndDate = false;
        boolean isDuplicateRUT = false;
        String rut = null;
        Date endDate = null;
        long actKey = 0;
        
        try {
        	logger.fine("Caling the original OIM validator");
            super.validate(reqData);
            
            logger.finer("Getting the max end date allowed");
            Properties properties = new Properties();
    		properties.load(new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
    		int maxDays = Integer.parseInt(properties.getProperty("entel.oim.creation.external.users.max.days"));
    		Calendar calMaxEndDate = Calendar.getInstance();
            calMaxEndDate.add(Calendar.DATE, maxDays);
            
            
            logger.finer("Getting the targets of the creation");
            List<RequestEntity> reqEntList = reqData.getTargetEntities(); 
            
            logger.finer("Loop over traget and check if one Organization is forbidden");
            for( RequestEntity reqEnt: reqEntList){ 
            	List<RequestEntityAttribute> attributes = reqEnt.getEntityData(); 
            	for(RequestEntityAttribute attribute : attributes){ 
             
		            if (attribute.getName().equalsIgnoreCase("Organization")) {
		            	Long actKeyLong = (Long)attribute.getValue();
		     	        actKey = actKeyLong.longValue();
		     	        
		            }
		            
		            if (attribute.getName().equalsIgnoreCase("RUT")) {
		            	 rut = (String) attribute.getValue();
		            	 if (rut != null) {
		            		 rut = rut.toUpperCase();
		            	 }
		     	    }
		            
		            if (attribute.getName().equalsIgnoreCase("End Date")) {
		            	endDate = (Date) attribute.getValue();
		            }
		        }
            	
            	if (isForbidden(actKey)) {
          		 logger.finer("Found one organization forbidden");
          		 isForbidden = true;
          	    }
               
                if (isExternOrg(actKey)) {
                	if (isInternalControlList(rut)) {
		          		 logger.finer("Found the user inside the Internal Control List");
		          		 isInternalControlList = true;
                	}
                	
                	if ( endDate == null || endDate.after(calMaxEndDate.getTime()) ) {
                		logger.finer("End Date of the user is invalid");
		          		isInvalidEndDate = true;
                	}
              	}
                
                if (existsUserRUT(rut)) {
             		 logger.finer("Found one active user with the same rut");
             		 isDuplicateRUT = true;
             	    }
                  
                
                
            }
            
            logger.fine("Checking if priviledge organization");
            if (isForbidden) {
            	String reason = "You don't have permission to create users in this Organization"; 
				logger.log(Level.SEVERE, reason);
		        logger.exiting(className, "validate");
		        throw new InvalidRequestDataException(reason);
            }
            
            
            logger.fine("Checking if Internal Control List");
            if (isInternalControlList) {
            	String reason = getReasonForInternalList(rut);
                logger.exiting(className, "validate");
		        throw new InvalidRequestDataException("User in Internal Control List. Reason: " + reason );
            }
            
            logger.fine("Checking if Invalid End Date");
            if (isInvalidEndDate) {
            	String reason = "End Date cannot be more than "+ maxDays +" days";
            	logger.log(Level.SEVERE, reason);
		        logger.exiting(className, "validate");
		        throw new InvalidRequestDataException(reason);
            }
            
            logger.fine("Checking if Duplicate RUT");
            if (isDuplicateRUT) {
            	String reason = "Duplicate RUT.";
            	logger.log(Level.SEVERE, reason);
		        logger.exiting(className, "validate");
		        throw new InvalidRequestDataException(reason);
            }
            
            
			logger.fine("The user can create the account.");
			logger.exiting(className, "validate");
	        
	    } catch (IOException | SystemConfigurationServiceException | SQLException  e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
			logger.exiting(className, "validate");
			throw new InvalidRequestDataException("validate - Unexpected error",e);
	    }
    	
    }

    
    
    /**
     * Check if the RUT is in the Internal Control List
     * @param rut   
     * 			RUT to ckeck
     * @return true if user is in Internal Control List. false if not.
     */
    public boolean isInternalControlList(String rut) 
    {
    	logger.entering(className, "isInternalControlList", rut);
    	boolean isInList = false;
        
    	try {
    		
    		logger.finer("Getting lookup values from " + LOOKUP_USERS_INTERNALCONTROLLIST);
    		tcResultSet rs = lookupOps.getLookupValues(LOOKUP_USERS_INTERNALCONTROLLIST);
    		
    		logger.finer("Looping over results");
    		for (int i=0;i<rs.getRowCount();i++) {
    			rs.goToRow(i);
    			String tmpRut = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
    			logger.finest("Checking value: "+tmpRut);
    			if (tmpRut != null && tmpRut.equalsIgnoreCase(rut)) {
    				logger.finer("User "+rut + " is in Internal Control List");
    				isInList = true;
    				break;
    			}

    		}
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"isInternalControlList - Unexpected error",e);
		}
		
        logger.exiting(className, "isInternalControlList", isInList);
        return isInList;
    }
    

	
	
	/**
     * Check if the Organization is External
     * @param actKey   
     * 			Organization Key to ckeck
     * @return true if the organization is extern. false if not.
     */
    public boolean isExternOrg(long actKey) 
    {
    	logger.entering(className, "isExternOrg", actKey);
    	boolean isExternal = false;
        
    	try {
    		
    		logger.finest("Setting the attributes to find of the organization");
			Set<String> searchAttrs = new HashSet<String>();
			searchAttrs.add(ORG_NAME.getId());
			searchAttrs.add(ORG_PARENT_NAME.getId());
		  
			logger.finest("Getting Parent Organization Name");
			Organization org = orgMgr.getDetails(ID_FIELD.getId(), actKey, searchAttrs);
			HashMap<String,Object> mapAttrs = org.getAttributes();
	        String orgParentName = (String) mapAttrs.get(ORG_PARENT_NAME.getId());

	        logger.finest("Checking if Externos");
			if (orgParentName != null && orgParentName.equalsIgnoreCase("Externos")) {
				isExternal = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"isExternOrg - Unexpected error",e);
		}
		
        logger.exiting(className, "isExternOrg", isExternal);
        return isExternal;
    }
    
    /**
     * Get the reason for an user is in the Internal Control List
     * @param rut
     * 			RUT to find
     * @return
     * 			Reason of the user in the list
     */
    private String getReasonForInternalList(String rut) {
    	
    	logger.entering(className, "getReasonForInternalList", rut);
    	String reason = null;
		
    	try {
			logger.finer("Calling to Lookup Service");
			reason = lookupOps.getDecodedValueForEncodedValue(LOOKUP_USERS_INTERNALCONTROLLIST, rut);
		} catch (tcAPIException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getReasonForInternalList - Unexpected error", e);
		}
		
		logger.exiting(className, "getReasonForInternalList", reason);
		return reason;
    }
    
    
    /**
	 * Check if the user rut exists in OIM
	 * @param rut User rut to check
	 * @return True if the user rut exist in OIM. False if the user login doesn't exist in OIM
     * @throws SQLException 
	 */
	public static boolean existsUserRUT(String rut) throws SQLException {
		
		logger.entering(className, "existsUserRUT",rut);
		boolean exist = false;
		PreparedStatement stmt = null;
    	Connection oimdb = null;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.fine("Constructing SQL");
	    	String sql = "select 1 from usr where TRIM(UPPER(usr_udf_rut)) = UPPER('" +rut.trim()+"') and usr_status in ('Active','Disabled','Disabled Until Start Date') and usr_automatically_delete_on is null";
			logger.finest("SQL to execute: " + sql);
					
	    	logger.finer("Executing query");
	    	stmt = oimdb.prepareStatement(sql);
	    	ResultSet rs = stmt.executeQuery();
	
		    if (!rs.isBeforeFirst()) {
		    	logger.finer("No users found!");
		    } else {
		    	exist = true;
			}
				    
		    logger.finest("Closing result set...");
		    rs.close();
	    	
	    } catch (SQLException e) {
			logger.log(Level.SEVERE, "Unexpected error - existsUserRUT", e);
			e.printStackTrace();
			throw e;
		} finally {
    		if (stmt != null) {
    			logger.finest("Closing Prepared Statement...");
    			stmt.close();
		    }
    		if (oimdb != null) {
    			logger.finer("Closing database connection!");
    			oimdb.close();
		    }
    	}
    	
    	logger.exiting(className, "existsUserRUT",exist);
		return exist;
		
	}
    
    

}
