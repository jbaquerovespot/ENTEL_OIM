package entel.oim.plugins.eventhandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;

public class UpdateUsrAttrsInAccounts implements PostProcessHandler, ConditionalEventHandler {

	private final static String className = UpdateUsrAttrsInAccounts.class.getName();
	private final static String EXCHANGE_APP_INST= "Exchange";
	private final static String ACTIVE_DRECTORY_APP_INST= "ActiveDirectory";
	private final static String ACTIVE_DRECTORY_EXT_APP_INST= "ActiveDirectoryExternos";
	private static Logger logger = Logger.getLogger(className);
	private static UserManager usrMgrOps = Platform.getService(UserManager.class);
	private static ProvisioningService provisioningService = Platform.getService(ProvisioningService.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private static tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private static ApplicationInstanceService appInstService = Platform.getService(ApplicationInstanceService.class);

	@Override
	public void initialize(HashMap<String, String> arg0) {
	}

	@Override
	public void compensate(long processId, long eventId, AbstractGenericOrchestration orchestration) {
	}

	@Override
	public boolean cancel(long processId, long eventId, AbstractGenericOrchestration orchestration) {
		return false;
	}

	@Override
	public boolean isApplicable(AbstractGenericOrchestration orchestration) { // Request Context

		logger.entering(className, "isApplicable");
		boolean isValid = false;
		Properties properties = new Properties();
		String migrationOIMPS2;

		try {

			logger.fine("Checking if it is MODIFY operation");
			if (orchestration.getOperation().equals("MODIFY")) {

				logger.info("Checking if Migration of OIM is active");
				properties.load(new FileInputStream(
						systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
				migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");

				logger.info("Checking if update for Employee is available and we are not in Migration process");
				if (!Boolean.parseBoolean(migrationOIMPS2)) {
					isValid = true;
				}
			}

		} catch (AccessDeniedException | IOException | SystemConfigurationServiceException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "isApplicable - Unexpected error", e);

		}

		logger.exiting(className, "isApplicable", isValid);
		return isValid;

	}


	@Override
	public EventResult execute(long processId, long eventId, Orchestration orchestration) {

		logger.entering(className, "execute",processId);
		try {

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
	public BulkEventResult execute(long processId, long eventId, BulkOrchestration orchestration) {

		logger.entering(className, "execute");
		
		logger.finer("Executing BULK operation");
        HashMap<String, Serializable>[] bulkParameters = orchestration.getBulkParameters();  
        
        logger.finer("Getting identities");
        String[] entityIds = orchestration.getTarget().getAllEntityId();  
           
        logger.finer("Looping over Identitites");
        for (int i = 0; i < bulkParameters.length; i++) {  
        	 logger.finer("Executing event for identity: " + entityIds[i]);
             executeEvent(bulkParameters[i],orchestration.getTarget().getType(), entityIds[i]);  
        }  
   
        logger.exiting(className, "execute");
        return null; //Async
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

			if (targetId != null && !targetId.isEmpty()) {
				logger.fine("Executing event for " + ACTIVE_DRECTORY_APP_INST);
				executeEventAppInstance(parameters,targetType,targetId,ACTIVE_DRECTORY_APP_INST);
				logger.fine("Executing event for " + ACTIVE_DRECTORY_EXT_APP_INST);
				executeEventAppInstance(parameters,targetType,targetId,ACTIVE_DRECTORY_EXT_APP_INST);
				logger.fine("Executing event for " + EXCHANGE_APP_INST);
				executeEventAppInstance(parameters,targetType,targetId,EXCHANGE_APP_INST);
			
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - executeEvent: " + e.getMessage(), e);
		}

		logger.exiting(className, "executeEvent");

    }

	
	/**
	 * Execute the event handler for an app instance
	 * @param parameters Parameters of the entity
	 * @param targetType target Type of the event
	 * @param targetId target id of the event
	 * @param appInstanceName Application Instance Name to update
	 */
	private void executeEventAppInstance(HashMap<String, Serializable> parameters, String targetType, String targetId,String appInstanceName) {  
        
		logger.entering(className, "executeEventAppInstance",targetId);
		try {

			if (targetId != null && !targetId.isEmpty()) {
				
				logger.fine("Obtain the key for the application "+appInstanceName);
   			    ApplicationInstance appInstance = appInstService.findApplicationInstanceByName(appInstanceName);
				
   			    logger.fine("Obtain the list of accounts of the user for the application instance");
			    List<Account> accountsList = provisioningService.getUserAccountDetailsInApplicationInstance(targetId, appInstance.getApplicationInstanceKey(), true);

			    logger.finer("Checking if accounts found");
				if (accountsList != null && accountsList.size() > 0) {
					
					logger.finer("Constructing Details");
					HashMap<String, Object> modParentData = updateAccountInformation(parameters, targetId, appInstanceName);
					if (!modParentData.isEmpty()) {
					
						logger.finer("Looping over accounts of the user");
						for(Account account : accountsList) {
							logger.info("Some information must be updated in AD for User Key: " + targetId);
							logger.finest("Modify the User Resource Account of the user with the new information");
							modifyUserResourceAccountParentData(targetId, account, modParentData);
						}
					
					}

				}
			
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - executeEventAppInstance: " + e.getMessage(), e);
		}

		logger.exiting(className, "executeEventAppInstance");

    }

	
	/**
	 * Method to check if any update is needed.
	 * 
	 * @param parameters
	 * @param userKey
	 * @return
	 * @throws UserLookupException
	 * @throws NoSuchUserException
	 */
	private HashMap<String, Object> updateAccountInformation(HashMap<String, Serializable> parameters, String userKey,String appInstanceName)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "updateAccountInformation",appInstanceName);
		HashMap<String, Object> modParentData = null;
		
		if (appInstanceName != null && appInstanceName.equals(ACTIVE_DRECTORY_APP_INST)) {
			modParentData = updateAccountInformationActiveDirectory(parameters, userKey);
		} else if (appInstanceName != null && appInstanceName.equals(ACTIVE_DRECTORY_EXT_APP_INST)) {
			modParentData = updateAccountInformationActiveDirectoryExt(parameters, userKey);
		} else if (appInstanceName != null && appInstanceName.equals(EXCHANGE_APP_INST)) {
			modParentData = updateAccountInformationExchange(parameters, userKey);
		} 
		
		logger.exiting(className, "updateAccountInformation",modParentData);
		return modParentData;
	}

	
	
	/**
	 * Method to check if any update is needed for Active Directory
	 * 
	 * @param parameters
	 * @param userKey
	 * @return
	 * @throws UserLookupException
	 * @throws NoSuchUserException
	 */
	private HashMap<String, Object> updateAccountInformationActiveDirectory(HashMap<String, Serializable> parameters, String userKey)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "updateAccountInformationActiveDirectory");
		HashMap<String, Object> modParentData = new HashMap<String, Object>();

		if (parameters.containsKey("Display Name") || parameters.containsKey("Common Name")
				|| parameters.containsKey("First Name") || parameters.containsKey("Middle Name")
				|| parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {

			String[] userNames = getNameLName(userKey);

			String fullName = fullNameInverted(userNames[0], userNames[1], userNames[2], userNames[3]);

			modParentData.put("UD_ADUSER_FULLNAME", fullName);
			modParentData.put("UD_ADUSER_COMMONNAME", fullName);

			if (parameters.containsKey("First Name")) {
				//modParentData.put("UD_ADUSER_FNAME", toCapitalizeStripAccentString(userNames[0]));
				modParentData.put("UD_ADUSER_FNAME", (userNames[0]));
			}

			if (parameters.containsKey("Middle Name")) {
				//modParentData.put("UD_ADUSER_MNAME", toCapitalizeStripAccentString(userNames[1]));
				modParentData.put("UD_ADUSER_MNAME", (userNames[1]));
			}

			if (parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {
				modParentData.put("UD_ADUSER_LNAME", toConcatNames(userNames[2], userNames[3]));
			}

		}

		if (parameters.containsKey("RUT")) {
			modParentData.put("UD_ADUSER_RUT", (parameters.get("RUT") != null ? parameters.get("RUT") : ""));
		}

		if (parameters.containsKey("usr_manager_key")) {
			if (parameters.get("usr_manager_key") != null && !parameters.get("usr_manager_key").toString().isEmpty()) {
				modParentData.put("UD_ADUSER_EXTENSIONATTRIBUTE12",
						getUser(parameters.get("usr_manager_key").toString(), false));
			}
		}

		if (parameters.containsKey("ExtendedPermission")) {
			if (parameters.get("ExtendedPermission") != null
					&& !parameters.get("ExtendedPermission").toString().isEmpty()) {
				modParentData.put("UD_ADUSER_EXTENDEDATTRIBUTE14", getDecodeValueFromLookup(
						"Lookup.Users.ExtendedPermission", parameters.get("ExtendedPermission").toString()));
			}
		}

		if (parameters.containsKey("DivisionalManagement")) {
			if (parameters.get("DivisionalManagement") != null
					&& !parameters.get("DivisionalManagement").toString().isEmpty()) {
				modParentData.put("UD_ADUSER_DESCRIPCION", getDecodeValueFromLookup("Lookup.Users.DivisionalManagement",
						parameters.get("DivisionalManagement").toString()));
			}
		}

		if (parameters.containsKey("End Date")) {
			modParentData.put("UD_ADUSER_EXPIRATIONDATE",
					(parameters.get("End Date") != null ? parameters.get("End Date") : ""));
		}

		if (parameters.containsKey("Position")) {
			if (parameters.get("Position") != null && !parameters.get("Position").toString().isEmpty()) {
				modParentData.put("UD_ADUSER_DESCRIPTION",
						getDecodeValueFromLookup("Lookup.Users.Position", parameters.get("Position").toString()));
			}
		}

		if (parameters.containsKey("Address")) {
			modParentData.put("UD_ADUSER_OFFICE", (parameters.get("Address") != null ? parameters.get("Address") : ""));
			modParentData.put("UD_ADUSER_STREET", (parameters.get("Address") != null ? parameters.get("Address") : ""));
		}

		if (parameters.containsKey("City")) {
			modParentData.put("UD_ADUSER_CITY", (parameters.get("City") != null ? parameters.get("City") : ""));
		}

		if (parameters.containsKey("Telephone Number")) {
			modParentData.put("UD_ADUSER_TELEPHONE",
					(parameters.get("Telephone Number") != null ? parameters.get("Telephone Number") : ""));
		}

		if (parameters.containsKey("Mobile")) {
			modParentData.put("UD_ADUSER_MOBILE", (parameters.get("Mobile") != null ? parameters.get("Mobile") : ""));
		}

		logger.exiting(className, "updateAccountInformationActiveDirectory");
		return modParentData;
	}

	
	/**
	 * Method to check if any update is needed in Active Directory Externos
	 * 
	 * @param parameters
	 * @param userKey
	 * @return
	 * @throws UserLookupException
	 * @throws NoSuchUserException
	 */
	private HashMap<String, Object> updateAccountInformationActiveDirectoryExt(HashMap<String, Serializable> parameters, String userKey)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "updateAccountInformationActiveDirectoryExt");

		HashMap<String, Object> modParentData = new HashMap<String, Object>();

		if (parameters.containsKey("Display Name") || parameters.containsKey("Common Name")
				|| parameters.containsKey("First Name") || parameters.containsKey("Middle Name")
				|| parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {

			String[] userNames = getNameLName(userKey);

			String fullName = fullNameInverted(userNames[0], userNames[1], userNames[2], userNames[3]);
			// AD
			modParentData.put("UD_ADEXTUSE_FULLNAME", fullName);
			modParentData.put("UD_ADEXTUSE_COMMONNAME", fullName);

			if (parameters.containsKey("First Name")) {
				//modParentData.put("UD_ADEXTUSE_FNAME", toCapitalizeStripAccentString(userNames[0]));
				modParentData.put("UD_ADEXTUSE_FNAME", (userNames[0]));
			}

			if (parameters.containsKey("Middle Name")) {
				//modParentData.put("UD_ADEXTUSE_MNAME", toCapitalizeStripAccentString(userNames[1]));
				modParentData.put("UD_ADEXTUSE_MNAME", (userNames[1]));
			}

			if (parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {
				modParentData.put("UD_ADEXTUSE_LNAME", toConcatNames(userNames[2], userNames[3]));
			}

		}

		if (parameters.containsKey("RUT")) {
			modParentData.put("UD_ADEXTUSE_RUT", (parameters.get("RUT") != null ? parameters.get("RUT") : ""));
		}

		if (parameters.containsKey("usr_manager_key")) {
			if (parameters.get("usr_manager_key") != null && !parameters.get("usr_manager_key").toString().isEmpty()) {
				modParentData.put("UD_ADEXTUSE_EXTENSIONATTRIBU12",
						getUser(parameters.get("usr_manager_key").toString(), false));
			}
		}

		if (parameters.containsKey("ExtendedPermission")) {
			if (parameters.get("ExtendedPermission") != null
					&& !parameters.get("ExtendedPermission").toString().isEmpty()) {
				modParentData.put("UD_ADEXTUSE_EXTENDEDATTRIBU14", getDecodeValueFromLookup(
						"Lookup.Users.ExtendedPermission", parameters.get("ExtendedPermission").toString()));
			}
		}

		if (parameters.containsKey("DivisionalManagement")) {
			if (parameters.get("DivisionalManagement") != null
					&& !parameters.get("DivisionalManagement").toString().isEmpty()) {
				modParentData.put("UD_ADEXTUSE_DESCRIPCION", getDecodeValueFromLookup(
						"Lookup.Users.DivisionalManagement", parameters.get("DivisionalManagement").toString()));
			}
		}

		if (parameters.containsKey("End Date")) {
			modParentData.put("UD_ADEXTUSE_EXPIRATIONDATE",
					(parameters.get("End Date") != null ? parameters.get("End Date") : ""));
		}

		if (parameters.containsKey("Position")) {
			if (parameters.get("Position") != null && !parameters.get("Position").toString().isEmpty()) {
				modParentData.put("UD_ADEXTUSE_DESCRIPTION",
						getDecodeValueFromLookup("Lookup.Users.Position", parameters.get("Position").toString()));
			}
		}

		if (parameters.containsKey("Address")) {
			modParentData.put("UD_ADEXTUSE_OFFICE",
					(parameters.get("Address") != null ? parameters.get("Address") : ""));
			modParentData.put("UD_ADUSER_STREET", (parameters.get("Address") != null ? parameters.get("Address") : ""));
		}

		if (parameters.containsKey("City")) {
			modParentData.put("UD_ADEXTUSE_CITY", (parameters.get("City") != null ? parameters.get("City") : ""));
		}

		if (parameters.containsKey("Telephone Number")) {
			modParentData.put("UD_ADEXTUSE_TELEPHONE",
					(parameters.get("Telephone Number") != null ? parameters.get("Telephone Number") : ""));
		}

		if (parameters.containsKey("Mobile")) {
			modParentData.put("UD_ADEXTUSE_MOBILE", (parameters.get("Mobile") != null ? parameters.get("Mobile") : ""));
		}

		logger.exiting(className, "updateAccountInformationActiveDirectoryExt");
		return modParentData;
	}
	
	
	/**
	 * Method to check if any update is needed in Exchange.
	 * 
	 * @param parameters
	 * @param userKey
	 * @return
	 * @throws UserLookupException
	 * @throws NoSuchUserException
	 */
	private HashMap<String, Object> updateAccountInformationExchange(HashMap<String, Serializable> parameters, String userKey)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "updateAccountInformationExchange");
		HashMap<String, Object> modParentData = new HashMap<String, Object>();

		if (parameters.containsKey("Display Name") || parameters.containsKey("Common Name")
				|| parameters.containsKey("First Name") || parameters.containsKey("Middle Name")
				|| parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {

			String[] userNames = getNameLName(userKey);

			String fullName = fullNameInverted(userNames[0], userNames[1], userNames[2], userNames[3]);
			modParentData.put("UD_EXCHANGE_DISPLAYNAME", fullName);
		}

		logger.exiting(className, "updateAccountInformationExchange");
		return modParentData;
	}

	
	
	/**
	 * Modifies a resource account on an OIM user
	 * 
	 * @param userKey
	 *            OIM user_key
	 * @param resourceAccount
	 *            Existing resource account to modify
	 * @param modAttrs
	 *            Attributes to modify on the parent form
	 * @throws AccountNotFoundException
	 * @throws GenericProvisioningException
	 */
	private static void modifyUserResourceAccountParentData(String userKey, Account resourceAccount,
			HashMap<String, Object> modAttrs) throws AccountNotFoundException, GenericProvisioningException {

		logger.entering(className, "modifyUserResourceAccountParentData", userKey);

		// Stage resource account modifications
		String accountId = resourceAccount.getAccountID();
		String processFormInstanceKey = resourceAccount.getProcessInstanceKey();
		Account modAccount = new Account(accountId, processFormInstanceKey, userKey);

		String formKey = resourceAccount.getAccountData().getFormKey();
		String udTablePrimaryKey = resourceAccount.getAccountData().getUdTablePrimaryKey();

		AccountData accountData = new AccountData(formKey, udTablePrimaryKey, modAttrs);

		// Set necessary information to modified account
		modAccount.setAccountData(accountData);
		modAccount.setAppInstance(resourceAccount.getAppInstance());

		logger.info("Starting to modify user's [" + userKey + "] account.");

		// Modify resource account
		provisioningService.modify(modAccount);

		logger.info("The user's [" + userKey + "] account was modified.");

		logger.exiting(className, "modifyUserResourceAccountParentData");

	}

	
	
	/**
	 * Return the first name, middle name, last name and surname of an user.
	 * 
	 * @param userKey
	 * @return
	 * @throws NoSuchUserException
	 * @throws UserLookupException
	 * @throws oracle.iam.platform.authz.exception.AccessDeniedException
	 */
	private String[] getNameLName(String userKey)
			throws NoSuchUserException, UserLookupException, oracle.iam.platform.authz.exception.AccessDeniedException {

		logger.entering(className, "getNameLName", userKey);
		String[] names = new String[4];

		HashSet<String> attrsToFetch = new HashSet<String>();
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_KEY.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.FIRSTNAME.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.MIDDLENAME.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.LASTNAME.getId());
		attrsToFetch.add("SurName");
		User user = usrMgrOps.getDetails(userKey, attrsToFetch, false);

		names[0] = (user.getFirstName() != null ? user.getFirstName() : "");
		names[1] = (user.getMiddleName() != null ? user.getMiddleName() : "");
		names[2] = (user.getLastName() != null ? user.getLastName() : "");
		names[3] = (user.getAttribute("SurName") != null ? user.getAttribute("SurName").toString() : "");

		logger.exiting(className, "getNameLName", names);
		return names;
	}

	
	/**
	 * Get a specific user's resource account
	 * 
	 * @param userId
	 *            OIM UserLogin / User Key
	 * @param userLoginUsed
	 *            True for user login. False for user key.
	 * @return value of usr_key
	 * @throws NoSuchUserException
	 * @throws UserLookupException
	 */
	private static String getUser(String userId, boolean userLoginUsed)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "getUser", userId);

		HashSet<String> attrsToFetch = new HashSet<String>();
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_KEY.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		User user = usrMgrOps.getDetails(userId, attrsToFetch, userLoginUsed);

		logger.exiting(className, "getUser", user);

		if (userLoginUsed) {
			return user.getEntityId();
		} else {
			return user.getLogin();
		}

	}

	
	/**
	 * Get the decode value for a code in a Lookup
	 * 
	 * @param code
	 *            Code to find
	 * @return Decode Value
	 */
	public String getDecodeValueFromLookup(String lookup, String code) {

		logger.entering(className, "getDecodeValueFromLookup", code);
		String decode = null;

		try {
			logger.finer("Calling to Lookup Service");
			decode = lookupOps.getDecodedValueForEncodedValue(lookup, code);
		} catch (tcAPIException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getDecodeValueFromLookup - Unexpected error", e);
		}

		logger.exiting(className, "getDecodeValueFromLookup", decode);
		return decode;
	}


	
	/**
	 * Full name of an user. Format: Last names + Names
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param mmName
	 * @return fullName
	 */
	public String fullNameInverted(String firstName, String middleName, String lastName, String mmName) {

		logger.entering(className, "fullNameInverted", new Object[] { firstName, middleName, lastName, mmName });
		String names = toConcatNames(firstName, middleName);
		String lastNames = toConcatNames(lastName, mmName);
		String fullName = toConcatNames(lastNames, names);

		logger.exiting(className, "fullNameInverted", fullName);
		return fullName;

	}

	
	
	/**
	 * Return all names of an user in a single string
	 * 
	 * @param firstName
	 * @param middleName
	 * @return First and middlename string
	 */
	public String toConcatNames(String firstName, String middleName) {

		logger.entering(className, "toConcatNames", firstName + " " + middleName);
		// variable to return
		String newVal = "";

		if (firstName == null) {
			firstName = "";
		} 
		//else {
		//	firstName = toCapitalizeStripAccentString(firstName);
			
		//}
		if (middleName == null) {
			middleName = "";
		} 
		//else {
		//	middleName = toCapitalizeStripAccentString(middleName);
		//}

		if ("".equals(middleName)) {
			newVal = firstName;
		} else {
			newVal = firstName + " " + middleName;
		}

		logger.exiting(className, "toConcatNames", newVal);
		return newVal.trim();

	}


}
