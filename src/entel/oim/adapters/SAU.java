package entel.oim.adapters;

import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ID_FIELD;
import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ORG_NAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_KEY;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.configservice.api.Constants;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.utils.NoSuchServiceException;
import oracle.iam.selfservice.exception.UserLookupException;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;

/**
 * Contains generics adapters for SAU connectors
 * 
 * @author Oracle
 *
 */
public class SAU {
	
	private final static String className = SAU.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static AuthenticatedSelfService authSelfSvc = Platform.getService(AuthenticatedSelfService.class);
	private static OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
	
	public SAU() {
		super();
	}

	/**
	 * Return the persond who did the operation
	 * 
	 * @param words
	 * @return  Upper Case of the words
	 */
	public String getUserOperation() {
		
		logger.entering(className, "getUserOperation");
		String userName = null;
		
		try {
			logger.finest("Getting the attributes of the authenticated user");
			Set<String> searchAttrsUsr = new HashSet<String>();
			searchAttrsUsr.add(USER_KEY.getId());
			searchAttrsUsr.add(USER_LOGIN.getId());
	        User authUser = authSelfSvc.getProfileDetails(searchAttrsUsr);
	        userName = authUser.getLogin();
	        
	        logger.finer("Setting lowercase to: " + userName);
	        if ( userName != null ) {
	        	userName = userName.toLowerCase();
	        }
	        
		} catch (UserLookupException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUserOperation - Unexpected error", e);
		}
		
		logger.exiting(className, "getUserOperation",userName);
		return userName;
		
	}
	
	
	/**
	 * Lookup for the default company for user
	 * 
	 * @param userLogin
	 *            User Login from OIM
	 * @param orgKey
	 *            Organization of the user
	 * @return 
	 *            The required default company of the user
	 */
	public String getDefaultCompany(String orgKey) {

		logger.entering(className, "getDefaultCompany", orgKey);
		String defaultCompany = null;
		String companyEntel = null;
		String companyDealers = null;
		
		try {
			logger.finer("Getting companies");
			tcLookupOperationsIntf lkOps = Platform.getService(tcLookupOperationsIntf.class);
			tcResultSet companies = lkOps.getLookupValues("Lookup.SAU Target.COMPANYTYPE");

			logger.finer("Looking up possible companies. Number of Lookups: " +companies.getRowCount());
			for (int i = 0; i < companies.getRowCount(); i++) {
				companies.goToRow(i);

				String code = companies.getStringValue(Constants.TableColumns.LKV_ENCODED.toString());
				String decode = companies.getStringValue(Constants.TableColumns.LKV_DECODED.toString());

				// CHeck for the default in case that not found
				if (decode.contains("ENTEL PCS")) {
					companyEntel = code;
				} else if (decode.contains("DEALERS")) {
					companyDealers = code;
				}
				
			}
			
			logger.finest("Setting the attributes to find of the organization");
			Set<String> searchAttrs = new HashSet<String>();
			searchAttrs.add(ORG_NAME.getId());
			
			logger.finest("Getting Organization Name");
			Organization org = orgMgr.getDetails(ID_FIELD.getId(), orgKey, searchAttrs);
			HashMap<String,Object> mapAttrs = org.getAttributes();
	        String orgName = (String) mapAttrs.get(ORG_NAME.getId());
	        
	        logger.finer("Checking organization oif the user");
			if (orgName.equalsIgnoreCase("Empleados")) {
				defaultCompany = companyEntel;
			} else {
				defaultCompany = companyDealers;
			}
			
			logger.exiting(className, "getDefaultCompany", defaultCompany);
			return defaultCompany;

		} catch (AccessDeniedException | NoSuchServiceException
				| tcAPIException | tcInvalidLookupException | tcColumnNotFoundException | SearchKeyNotUniqueException | OrganizationManagerException e) {
			logger.log(Level.SEVERE, "Unexpected error - getDefaultCompany", e);
			throw new RuntimeException(e);
		}
		
	}


}
