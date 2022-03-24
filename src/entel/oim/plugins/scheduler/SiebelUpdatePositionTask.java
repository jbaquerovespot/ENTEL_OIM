package entel.oim.plugins.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.configservice.api.Constants;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants.RoleAttributeName;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.scheduler.vo.TaskSupport;

public class SiebelUpdatePositionTask extends TaskSupport {

	private final static String className = SiebelUpdatePositionTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	// Services
	private final RoleManager rmgr = Platform.getService(RoleManager.class);
	private static ProvisioningService provisioningService = Platform.getService(ProvisioningService.class);
	private final tcLookupOperationsIntf lookupOprInf = Platform.getService(tcLookupOperationsIntf.class);

	@Override
	public void execute(HashMap hm) throws Exception {

		logger.entering(className, "execute", hm);
		// RolesNames
		String rolesMapping = (String) hm.get("Roles Mapping");
		// Name of the Resource
		String resourceName = (String) hm.get("Resource Name");
		String parameterName = (String) hm.get("Parameter Name");
		String attributeNames = (String) hm.get("Attribute Names");
		String lookUpName = (String) hm.get("LookUp Name");

		if (rolesMapping != null && resourceName != null && lookUpName != null && parameterName != null
				&& attributeNames != null && !rolesMapping.isEmpty() && !resourceName.isEmpty() && !lookUpName.isEmpty()
				&& !parameterName.isEmpty() && !attributeNames.isEmpty()) {

			String[] rolesMappingArray = rolesMapping.split(";");
			String[] attributeNamesArray = attributeNames.split(";");

			if (attributeNamesArray.length == 2) {

				logger.finer("Getting Cargos Siebel");
				tcResultSet cargoSet = lookupOprInf.getLookupValues("Lookup.Siebel.Dictonary.Cargo");
				HashMap<String, String> cargoMap = new HashMap<String, String>();
				for (int i = 0; i < cargoSet.getRowCount(); i++) {
					cargoSet.goToRow(i);
					cargoMap.put(cargoSet.getStringValue(Constants.TableColumns.LKV_ENCODED.toString()),
							cargoSet.getStringValue(Constants.TableColumns.LKV_DECODED.toString()));
				}
				logger.finer(cargoMap.size() + " Cargos Siebel Loaded.");

				logger.finer("Getting Division Siebel");
				tcResultSet divisionSet = lookupOprInf.getLookupValues("Lookup.Siebel.Dictonary.DivisionTiendaPDV");
				HashMap<String, String> divisionMap = new HashMap<String, String>();
				for (int i = 0; i < divisionSet.getRowCount(); i++) {
					divisionSet.goToRow(i);
					divisionMap.put(divisionSet.getStringValue(Constants.TableColumns.LKV_ENCODED.toString()),
							divisionSet.getStringValue(Constants.TableColumns.LKV_DECODED.toString()));
				}
				logger.finer(divisionMap.size() + " Division Siebel Loaded.");

				logger.finer("Getting Primary Position Siebel.");
				tcResultSet lookupSiebel = lookupOprInf.getLookupValues(lookUpName);
				logger.finer("Primary Position Siebel Loaded.");

				logger.finer("Looping over roles to check");
				for (String roleName : rolesMappingArray) {
					logger.finer("Getting roles");
					List<Role> roleList = getRole(roleName);
					if (roleList != null) {
						logger.finer("Looping over Roles to verify primary position.");
						for (Role role : roleList) {
							logger.finer("Getting users for Role '" + role.getDisplayName() + "'.");
							List<User> userList = rmgr.getRoleMembers(role.getEntityId(), true);
							logger.finer(userList.size() + " users found.");
							for (User usr : userList) {
								logger.finer("Getting the primary position for the user " + usr.getLogin() + " with Id "
										+ usr.getEntityId());
								Account resourceAccount = getUserResourceAccount(usr.getEntityId(), resourceName);
								if (resourceAccount != null) {
									logger.finer(
											"Account found for user " + usr.getLogin() + " - " + usr.getEntityId());

									HashMap<String, Object> accountData = (HashMap<String, Object>) resourceAccount
											.getAccountData().getData();

									if (accountData.containsKey(parameterName)) {
										
										//Checking user's information
										String primaryPosition = accountData.get(parameterName).toString();
										logger.finer(usr.getLogin() + "'s Primary Position: " + primaryPosition);
										String job = (String) usr.getAttribute(attributeNamesArray[0]);
										logger.finer(usr.getLogin() + "'s Unit: " + job);
										String unit = (String) usr.getAttribute(attributeNamesArray[1]);
										logger.finer(usr.getLogin() + "'s Cargo: " + unit);

										if (unit != null && job != null && !unit.isEmpty() && !job.isEmpty()) {

											//Checking correct Primary Position for user
											String jobSiebel = "";
											String unitSiebel = "";
											if (cargoMap.containsKey(job)) {
												jobSiebel = cargoMap.get(job);
											}
											if (divisionMap.containsKey(unit)) {
												unitSiebel = divisionMap.get(unit);
											}
											if (!jobSiebel.isEmpty() && !unitSiebel.isEmpty()) {

												logger.finer(
														"Cargo+Unidad Siebel: " + jobSiebel + " " + unitSiebel + ".");

												String pPostionSiebel = getPrimaryPositionSiebel(jobSiebel, unitSiebel,
														lookupSiebel);

												if (!pPostionSiebel.isEmpty()) {

													logger.finer("Primary Position Siebel Code: " + pPostionSiebel);

													if (primaryPosition.equalsIgnoreCase(pPostionSiebel)) {
														logger.finer(usr.getLogin()
																+ " has the correct primary position in Siebel.");
													} else {
														logger.finer(usr.getLogin()
																+ " has an incorrect primary position in Siebel. Account must be updated.");
														 modifyUserResourceAccountParentData(usr.getEntityId(),resourceAccount, parameterName, pPostionSiebel);
													}

												} else {
													logger.finer("Validate Cargo - Unidad " + usr.getLogin());
												}

											} else {
												logger.finer("Cargo o Unidad no encontrada en Siebel para "
														+ usr.getLogin());
											}

										} else {
											logger.finer("Missing parameters for " + usr.getLogin());
										}

									} else {
										logger.finer("Not found " + parameterName);
									}

								} else {
									logger.finer(
											"Account not found for user " + usr.getLogin() + " - " + usr.getEntityId());
								}

							}
						}
					} else {
						logger.finer("No roles found.");
					}
				}
			} else {
				logger.finer("User's Attribute Names are incorrect.");
			}
		} else {
			logger.finer("Missing parameters.");
		}
		logger.exiting(className, "execute");

	}

	/**
	 * Method to obtain the Primary Position of a User
	 * 
	 * @param jobSiebel
	 * @param unitSiebel
	 * @param lookupSiebel
	 * @return
	 * @throws tcColumnNotFoundException
	 * @throws tcAPIException
	 */
	private String getPrimaryPositionSiebel(String jobSiebel, String unitSiebel, tcResultSet lookupSiebel)
			throws tcAPIException, tcColumnNotFoundException {
		String primaryPosition = "";

		for (int i = 0; i < lookupSiebel.getRowCount(); i++) {
			lookupSiebel.goToRow(i);

			String decode = lookupSiebel.getStringValue(Constants.TableColumns.LKV_DECODED.toString());
			if (decode != null && !decode.isEmpty()) {
				if (decode.contains(jobSiebel) && decode.contains(unitSiebel)) {
					primaryPosition = lookupSiebel.getStringValue(Constants.TableColumns.LKV_ENCODED.toString());
					logger.finer("Correct Siebel Primary Position: " + decode);
					break;
				}
			}
		}

		return primaryPosition;
	}

	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {

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
	private static Account getUserResourceAccount(String userKey, String resourceName)
			throws UserNotFoundException, GenericProvisioningException {

		logger.entering(className, "getUserResourceAccount", userKey);

		List<Account> accounts = provisioningService.getAccountsProvisionedToUser(userKey, true);

		for (Account account : accounts) {

			if (account.getAppInstance().getApplicationInstanceName().equalsIgnoreCase(resourceName)) {

				logger.exiting(className, "getUserResourceAccount", account.getAccountID());
				return account;
			}
		}

		logger.exiting(className, "getUserResourceAccount", null);
		return null;
	}

	/**
	 * Get a role object
	 * 
	 * @param roleName
	 * @return
	 */
	public List<Role> getRole(String roleName) {

		logger.entering(className, "getRole");
		List<Role> rolesList = null;

		try {

			logger.fine("Checking if role name was enter");
			if (null != roleName && !roleName.isEmpty()) {

				roleName = roleName.trim();

				logger.finer("Searching for object Role");
				SearchCriteria criteria = new SearchCriteria(RoleAttributeName.NAME.getId(), roleName,
						SearchCriteria.Operator.CONTAINS);
				List<Role> roleList = rmgr.search(criteria, null, null);

				if (null != roleList && roleList.size() > 0) {
					logger.finest("Successfully obtained " + roleList.size() + " roles.");
					rolesList = roleList;
				}

			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected Error - getRole", e);
		}

		logger.exiting(className, "getRole");
		return rolesList;
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
			String parameterName, String pPostionSiebel) throws AccountNotFoundException, GenericProvisioningException {

		logger.entering(className, "modifyUserResourceAccountParentData", userKey);

		HashMap<String, Object> modAttrs = new HashMap<String, Object>();
		modAttrs.put(parameterName, pPostionSiebel);

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

}