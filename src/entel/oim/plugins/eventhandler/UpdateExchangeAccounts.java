package entel.oim.plugins.eventhandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
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
import oracle.iam.platform.kernel.OrchestrationEngine;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;

public class UpdateExchangeAccounts implements PostProcessHandler, ConditionalEventHandler {

	private final static String className = UpdateExchangeAccounts.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static UserManager usrMgrOps = Platform.getService(UserManager.class);
	private static ProvisioningService provisioningService = Platform.getService(ProvisioningService.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private static tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);

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
	public EventResult execute(long processId, long eventId, Orchestration orchestration) {

		logger.entering(className, "execute", processId);
		System.out.println("EventResult - Execute: UpdateExchangeAccounts.");
		try {

			if (orchestration.getOperation().equals("MODIFY")) {

				logger.info("ProcessId: " + processId + " - EventId: " + eventId + "- Operation: MODIFY");

				HashMap<String, Serializable> parameters = orchestration.getParameters();

				// Obtain the userKey from the user
				String userKey = getUserKey(processId, orchestration);

				// if (parameters.containsKey("User Login")) {
				if (!userKey.isEmpty()) {

					// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
					Account resourceAccount = getUserResourceAccount(userKey);
					if (resourceAccount != null) {
						HashMap<String, Object> modParentData = updateAccountInformation(parameters, userKey);

						if (!modParentData.isEmpty()) {
							logger.info("Some information must be updated in Exchange for User Key: " + userKey);
							// Modify the User Resource Account of the user with the new information
							modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - execute: " + e.getMessage(), e);
		}

		logger.exiting(className, "execute UpdateExchangeAccounts");
		return new EventResult();
	}

	@Override
	public BulkEventResult execute(long processId, long eventId, BulkOrchestration orchestration) {

		logger.entering(className, "BulkEventResult");
		System.out.println("BulkEventResult - Execute: UpdateExchangeAccounts.");
		try {

			if (orchestration.getOperation().equals("MODIFY")) {

				logger.info("ProcessId: " + processId + " - EventId: " + eventId + "- Operation: MODIFY");

				HashMap<String, Serializable> parameters = orchestration.getParameters();
				/*
				 * Set<String> KeySet = parameters.keySet(); List of parameters updated. for
				 * (String key : KeySet) { logger.fine("--" + key + ": " + parameters.get(key));
				 * }
				 */

				// Obtain the userKey from the user
				String userKey = getUserKeyBulk(processId, orchestration);

				// if (parameters.containsKey("User Login")) {
				if (!userKey.isEmpty()) {

					// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
					Account resourceAccount = getUserResourceAccount(userKey);
					if (resourceAccount != null) {
						HashMap<String, Object> modParentData = updateAccountInformation(parameters, userKey);
						if (!modParentData.isEmpty()) {
							logger.info("Some information must be updated in Exchange for User Key: " + userKey);
							// Modify the User Resource Account of the user with the new information
							modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - execute: " + e.getMessage(), e);
		}

		logger.exiting(className, "BulkEventResult");

		return new BulkEventResult();
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
	private HashMap<String, Object> updateAccountInformation(HashMap<String, Serializable> parameters, String userKey)
			throws NoSuchUserException, UserLookupException {

		logger.entering(className, "updateAccountInformation");

		HashMap<String, Object> modParentData = new HashMap<String, Object>();

		if (parameters.containsKey("Display Name") || parameters.containsKey("Common Name")
				|| parameters.containsKey("First Name") || parameters.containsKey("Middle Name")
				|| parameters.containsKey("Last Name") || parameters.containsKey("SurName")) {

			String[] userNames = getNameLName(userKey);

			String fullName = fullNameInverted(userNames[0], userNames[1], userNames[2], userNames[3]);

			modParentData.put("UD_EXCHANGE_DISPLAYNAME", fullName);
		}

		logger.exiting(className, "updateAccountInformation");

		return modParentData;
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
	 * Get a specific user's resource account
	 * 
	 * @param userKey
	 *            OIM user's usr_key
	 * @param resourceObjectName
	 *            Name of the resource object
	 * @return Resource account
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 */
	private static Account getUserResourceAccount(String userKey)
			throws UserNotFoundException, GenericProvisioningException {

		logger.entering(className, "getUserResourceAccount", userKey);

		List<Account> accounts = provisioningService.getAccountsProvisionedToUser(userKey, true);

		for (Account account : accounts) {

			if (account.getAppInstance().getApplicationInstanceName().equalsIgnoreCase("Exchange")) {

				logger.exiting(className, "getUserResourceAccount", account.getAccountID());
				return account;
			}
		}

		logger.exiting(className, "getUserResourceAccount", null);
		return null;
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
	 * Return userKey
	 * 
	 * @param processID
	 * @param orchestration
	 * @return
	 */
	private String getUserKey(long processID, Orchestration orchestration) {

		String userKey;

		logger.entering(className, "getUserKey");

		if (!orchestration.getOperation().equals("CREATE")) {
			userKey = orchestration.getTarget().getEntityId();
		} else {
			OrchestrationEngine orchEngine = Platform.getService(OrchestrationEngine.class);
			userKey = (String) orchEngine.getActionResult(processID);
		}

		logger.exiting(className, "getUserKey");

		return userKey;
	}

	/**
	 * Return userKey by Bulk process
	 * 
	 * @param processId
	 * @param orchestration
	 * @return
	 */
	private String getUserKeyBulk(long processId, BulkOrchestration orchestration) {

		String userKey;

		logger.entering(className, "getUserKey");

		if (!orchestration.getOperation().equals("CREATE")) {
			userKey = orchestration.getTarget().getEntityId();
		} else {
			OrchestrationEngine orchEngine = Platform.getService(OrchestrationEngine.class);
			userKey = (String) orchEngine.getActionResult(processId);
		}

		logger.exiting(className, "getUserKey");

		return userKey;
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

	///**
		// * Format Sentence string and strip accents
		// * 
		// * @param str
		// * @return
		// */
		//public String toCapitalizeStripAccentString(String str) {
	    //
		//	logger.entering(className, "toCapitalizeStripAccentString", str);
	    //
		//	logger.finer("Checking null value");
		//	if (str == null)
		//		str = "";
	    //
		//	logger.finer("Split the String into words");
		//	String[] words = str.split("\\s");
		//	StringBuilder formatText = new StringBuilder();
	    //
		//	logger.finer("Loop over all words of the String");
		//	for (String word : words) {
		//		if (word != null && word.length() > 0) {
		//			formatText.append(
		//					word.substring(0, 1).toUpperCase().concat(word.substring(1, word.length()).toLowerCase()));
		//			formatText.append(" ");
		//		}
		//	}
	    //
		//	String value = formatText.toString();
		//	logger.finer("Formated text: " + value);
	    //
		//	value = toStripAccents(value);
		//	logger.finer("Striped Accent text: " + value);
	    //
		//	logger.finer("Trim spaces to the word");
		//	value = value.trim();
	    //
		//	logger.exiting(className, "toCapitalizeStripAccentString", value);
		//	return value;
		//}
	    //
		///**
		// * Replace special characters from string
		// * 
		// * @param s
		// *            String to transform
		// * @return String formated
		// */
		//public String toStripAccents(String str) {
	    //
		//	logger.entering(className, "toStripAccents", str);
	    //
		//	// Variable to return
		//	String newVal = str;
	    //
		//	logger.finer("Checking null value");
		//	if (newVal == null)
		//		newVal = "";
	    //
		//	logger.finer("Normalizer for: " + newVal);
		//	// Normalize the string
		//	newVal = Normalizer.normalize(newVal, Normalizer.Form.NFD);
		//	newVal = newVal.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		//	newVal = newVal.trim();
		//	logger.finer("Normalized string: " + newVal);
	    //
		//	// Return the format string
		//	logger.exiting(className, "toStripAccents", newVal);
		//	return newVal;
		//}

}
