package entel.oim.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;

public class AD {

	private final static String className = AD.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static UserManager usrMgrOps = Platform.getService(UserManager.class);
	private static ProvisioningService provisioningService = Platform.getService(ProvisioningService.class);

	/**
	 * Return date - hour when the user is created by OIM
	 * 
	 * @return message
	 */
	public String toCreateMessage() {

		logger.entering(className, "toCreateMessage");

		String message = "Creado OIM - ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message obtained is: "Creado OIM - <date> <time>"
		message = message + dateFormat.format(date);

		logger.exiting(className, "toCreateMessage", message);

		return message;
	}

	/**
	 * Return date - hour when the user is modified by OIM
	 * 
	 * @param info
	 * @param userLogin
	 * @param applicationName
	 * @return
	 */
	public void toUpdateMessage(String userLogin) {

		logger.entering(className, "toUpdateMessage", userLogin);

		String message = "Mod. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Mod. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccount(userKey);

			if (resourceAccount != null) {
				// Field(s) to modify in the resource account
				HashMap<String, Object> modParentData = new HashMap<String, Object>();
				String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADUSER_NOTAS"))
						? (String) resourceAccount.getAccountData().getData().get("UD_ADUSER_NOTAS")
						: "";

				// Concatenate the message and the information in the Notas field.
				if (info != null && !info.isEmpty()) {
					message = message + " || " + info;

				}
				if (message.length() > 254) {
					message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
				}

				modParentData.put("UD_ADUSER_NOTAS", message); // Key = Resource attribute column name

				// Modify the User Resource Account of the user with the new information
				modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
			}

		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toUpdateMessage", message);
		}
	}

	/**
	 * Return date - hour when the user is disabled by OIM
	 * 
	 * @param info
	 * @param userLogin
	 * @return
	 */
	public void toDisableMessage(String userLogin) {

		logger.entering(className, "toDisableMessage", userLogin);

		String message = "Desha. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Desha. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccount(userKey);

			if (resourceAccount != null) {

				// Modify resource account
				HashMap<String, Object> modParentData = new HashMap<String, Object>();

				String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADUSER_NOTAS"))
						? (String) resourceAccount.getAccountData().getData().get("UD_ADUSER_NOTAS")
						: "";

				if (info != null && !info.isEmpty()) {
					message = message + " || " + info;

				}

				if (message.length() > 254) {
					message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
				}
				modParentData.put("UD_ADUSER_NOTAS", message); // Key = Resource attribute column name
				// Modify the User Resource Account of the user with the new information
				modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);

			}

		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toDisableMessage", message);
		}

	}

	/**
	 * Return date - hour when the user is disabled by OIM
	 * 
	 * @param info
	 * @param userLogin
	 * @return
	 */
	public void toEnableMessage(String userLogin) {

		logger.entering(className, "toEnableMessage", userLogin);

		String message = "Hab. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Hab. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccount(userKey);

			if(resourceAccount!=null) {
			// Modify resource account
			HashMap<String, Object> modParentData = new HashMap<String, Object>();

			String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADUSER_NOTAS"))
					? (String) resourceAccount.getAccountData().getData().get("UD_ADUSER_NOTAS")
					: "";

			if (info != null && !info.isEmpty()) {
				message = message + " || " + info;

			}

			if (message.length() > 254) {
				message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
			}

			modParentData.put("UD_ADUSER_NOTAS", message); // Key = Resource attribute column name
			// Modify the User Resource Account of the user with the new information
			modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
			}
		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toEnableMessage", message);
		}

	}

	/**
	 * Return date - hour when the user is modified by OIM for extern accounts
	 * 
	 * @param info
	 * @param userLogin
	 * @return
	 */
	public void toUpdateMessageExternos(String userLogin) {

		logger.entering(className, "toUpdateMessageExternos", userLogin);

		String message = "Mod. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Mod. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccountExternos(userKey);

			if (resourceAccount != null) {
				// Field(s) to modify in the resource account
				HashMap<String, Object> modParentData = new HashMap<String, Object>();
				String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADEXTUSE_NOTAS"))
						? (String) resourceAccount.getAccountData().getData().get("UD_ADEXTUSE_NOTAS")
						: "";

				// Concatenate the message and the information in the Notas field.
				if (info != null && !info.isEmpty()) {
					message = message + " || " + info;

				}
				if (message.length() > 254) {
					message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
				}

				modParentData.put("UD_ADEXTUSE_NOTAS", message); // Key = Resource attribute column name

				// Modify the User Resource Account of the user with the new information
				modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
			}
		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toUpdateMessageExternos", message);
		}
	}

	/**
	 * Get a specific external user's resource account
	 * 
	 * @param userKey
	 *            OIM user's usr_key
	 * @return Resource account
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 */
	private static Account getUserResourceAccountExternos(String userKey)
			throws UserNotFoundException, GenericProvisioningException {

		logger.entering(className, "getUserResourceAccountExternos", userKey);

		List<Account> accounts = provisioningService.getAccountsProvisionedToUser(userKey, true);

		for (Account account : accounts) {

			if (account.getAppInstance().getApplicationInstanceName().equalsIgnoreCase("ActiveDirectoryExternos")) {

				logger.exiting(className, "getUserResourceAccountExternos", account.getAccountID());
				return account;
			}
		}

		logger.exiting(className, "getUserResourceAccount", null);
		return null;
	}

	/**
	 * Return date - hour when the external user is disabled by OIM
	 * 
	 * @param info
	 * @param userLogin
	 * @return
	 */
	public void toDisableMessageExternos(String userLogin) {

		logger.entering(className, "toDisableMessageExternos", userLogin);

		String message = "Desha. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Desha. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccountExternos(userKey);

			if (resourceAccount != null) {
				// Modify resource account
				HashMap<String, Object> modParentData = new HashMap<String, Object>();

				String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADEXTUSE_NOTAS"))
						? (String) resourceAccount.getAccountData().getData().get("UD_ADEXTUSE_NOTAS")
						: "";

				if (info != null && !info.isEmpty()) {
					message = message + " || " + info;

				}

				if (message.length() > 254) {
					message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
				}
				modParentData.put("UD_ADEXTUSE_NOTAS", message); // Key = Resource attribute column name
				// Modify the User Resource Account of the user with the new information
				modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);
			}

		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toDisableMessageExternos", message);
		}

	}

	/**
	 * Return date - hour when the external user is disabled by OIM
	 * 
	 * @param info
	 * @param userLogin
	 * @return
	 */
	public void toEnableMessageExternos(String userLogin) {

		logger.entering(className, "toEnableMessageExternos", userLogin);

		String message = "Hab. OIM ";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();

		// Message: "Hab. OIM <date> <time>"
		message = message + dateFormat.format(date);

		try {

			// Obtain the userKey from the user login
			String userKey = getUserKeyByUserLogin(userLogin);
			// Obtain the resourceAccount of the user for the resourceObjectName "AD User"
			Account resourceAccount = getUserResourceAccountExternos(userKey);

			if(resourceAccount!=null) {
			// Modify resource account
			HashMap<String, Object> modParentData = new HashMap<String, Object>();

			String info = (resourceAccount.getAccountData().getData().containsKey("UD_ADEXTUSE_NOTAS"))
					? (String) resourceAccount.getAccountData().getData().get("UD_ADEXTUSE_NOTAS")
					: "";

			if (info != null && !info.isEmpty()) {
				message = message + " || " + info;

			}

			if (message.length() > 254) {
				message = (message.substring(0, message.lastIndexOf("|") - 1)).trim();
			}

			modParentData.put("UD_ADEXTUSE_NOTAS", message); // Key = Resource attribute column name
			// Modify the User Resource Account of the user with the new information
			modifyUserResourceAccountParentData(userKey, resourceAccount, modParentData);

			}
			
		} catch (Exception e) {
			logger.finer("An error ocurred: " + e);
		} finally {
			logger.exiting(className, "toEnableMessageExternos", message);
		}

	}

	/**
	 * Get a specific user's resource account
	 * 
	 * @param userLogin
	 *            OIM UserLogin (USR_LOGIN)
	 * @return value of usr_key
	 * @throws NoSuchUserException
	 * @throws UserLookupException
	 */
	private static String getUserKeyByUserLogin(String userLogin) throws NoSuchUserException, UserLookupException {

		logger.entering(className, "getUserKeyByUserLogin", userLogin);

		boolean userLoginUsed = true;
		HashSet<String> attrsToFetch = new HashSet<String>();
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_KEY.getId());
		attrsToFetch.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
		User user = usrMgrOps.getDetails(userLogin, attrsToFetch, userLoginUsed);

		logger.exiting(className, "getUserKeyByUserLogin", user);

		return user.getEntityId();

	}

	/**
	 * Get a specific user's resource account
	 * 
	 * @param userKey
	 *            OIM user's usr_key
	 * @return Resource account
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 */
	private static Account getUserResourceAccount(String userKey)
			throws UserNotFoundException, GenericProvisioningException {

		logger.entering(className, "getUserResourceAccount", userKey);

		List<Account> accounts = provisioningService.getAccountsProvisionedToUser(userKey, true);

		for (Account account : accounts) {

			if (account.getAppInstance().getApplicationInstanceName().equalsIgnoreCase("ActiveDirectory")) {

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
	 * Return account type: INTERNO, EXTERNO, SISTEMA, SERVICIOS
	 * 
	 * @return accountType
	 */
	public String toAccountType(String organizationName) {

		logger.entering(className, "toAccountType", organizationName);

		String accountType = "";

		if (organizationName != null && !organizationName.isEmpty()) {
			if (organizationName.toLowerCase().equalsIgnoreCase("empleados")) {

				accountType = "INTERNO";

			} else {

				if (organizationName.toLowerCase().equalsIgnoreCase("cuentas de servicio")) {

					accountType = "SERVICIOS";

				} else {

					if (organizationName.toLowerCase().equalsIgnoreCase("cuentas genericas")) {

						accountType = "SISTEMA";

					} else {

						accountType = "EXTERNO";
					}
				}
			}
		}

		logger.exiting(className, "toAccountType", accountType);

		return accountType;
	}

}
