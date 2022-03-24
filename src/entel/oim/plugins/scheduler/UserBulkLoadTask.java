package entel.oim.plugins.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.RequestEntity;
import oracle.iam.request.vo.RequestEntityAttribute;
import oracle.iam.scheduler.vo.TaskSupport;
import org.apache.poi.ss.usermodel.*;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import entel.oim.adapters.Utilities;

public class UserBulkLoadTask extends TaskSupport {

	private static final String className = UserBulkLoadTask.class.getName();
	private static final Logger logger = Logger.getLogger(className);

	private final static String FILES_DATE_FORMAT = "ddMMyyyyHH_mm_ss";
	private final static String LOOKUP_USERS_INTERNALCONTROLLIST = "Lookup.Users.InternalControlList";
	private static String gestorLogin;
	private static String solicitanteLogin;

	private UserManager umgr = Platform.getService(UserManager.class);
	private static UserManager usrMgrOps = Platform.getService(UserManager.class);
	private RequestService rsrv = Platform.getService(RequestService.class);
	private RoleManager rmgr = Platform.getService(RoleManager.class);
	private NotificationService nsrv = Platform.getService(NotificationService.class);
	private tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);

	@Override
	public void execute(HashMap hm) throws Exception {
		logger.entering(className, "execute", hm);

		String ufp = (String) hm.get("Users File Directory");
		String key = (String) hm.get("Key");
		String templateFolder = (String) hm.get("Template Folder");

		if ((ufp == null) || (ufp != null && ufp.isEmpty())) {
			throw new Exception("No files specified");
		}

		if (ufp != null && !ufp.isEmpty() && (!Files.exists(Paths.get(ufp)) || !Files.isDirectory(Paths.get(ufp)))) {
			throw new Exception("Users File Directory must be a directory and it should exist");
		}

		if (ufp != null && !ufp.isEmpty()) {
			File dir = new File(ufp);

			logger.finest("Looking for user directories in folder. Directories " + Arrays.toString(dir.list()));
			for (File userDir : dir.listFiles()) {
				logger.finest("looping over user directory: " + userDir.getName());
				if (userDir.isDirectory() && !userDir.getName().equalsIgnoreCase(templateFolder)) {
					logger.finest("Looking for user files in folder. Files " + Arrays.toString(userDir.list()));
					for (File f : userDir.listFiles()) {

						if (!f.isDirectory()) {

							logger.finer("Checking if file " + f.getName() + " is valid");
							boolean isValid = checkIfValidFile(f.getAbsolutePath(), key);
							if (isValid) {
								logger.finest("Loading users file " + f.getName());
								loadExternalUsers(f.getAbsolutePath());
							}
							logger.finest("Moving to the old directory " + f.getName());
							moveFile(f.getAbsolutePath());
						}
					}
				}
			}
		}

	}

	/**
	 * Moves a file to an old folder
	 * 
	 * @param filePath
	 *            the original file
	 * @throws IOException
	 *             if an error occurs
	 */
	private void moveFile(String filePath) throws IOException {

		logger.entering(className, "moveFile", filePath);
		SimpleDateFormat dateFormat = new SimpleDateFormat(FILES_DATE_FORMAT);
		File f = new File(filePath);
		String folder = f.getParent();

		Path newPath = Paths.get(folder, "old",
				dateFormat.format(new Date()) + f.getName().substring(f.getName().lastIndexOf(".")));

		if (!Files.exists(Paths.get(folder, "old"))) {
			Files.createDirectory(Paths.get(folder, "old"));
		}

		logger.fine("Moving file [" + filePath + "] to [" + newPath.toString() + "]");
		Files.move(Paths.get(f.getAbsolutePath()), newPath, StandardCopyOption.REPLACE_EXISTING);
		logger.exiting(className, "moveFile", newPath);
	}

	/**
	 * Finds the usr_key of a user using its rut.
	 * 
	 * @param rut
	 *            the rut to use as the query criteria
	 * @return the usr_key of the found user, or null if none are found.
	 */
	private String searchUserInOIMByRUT(String rut) {
		logger.entering(className, "searchUserInOIMByRUT", new Object[] { rut });

		SearchCriteria usrCriteria = new SearchCriteria("RUT", rut, SearchCriteria.Operator.EQUAL);

		Set<String> userCritAttrs = new HashSet<>();
		userCritAttrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());

		String usrManager = null;

		HashMap<String, Object> sParam = new HashMap<>();
		sParam.put("STARTROW", 0);
		sParam.put("ENDROW", 1);

		try {
			List<User> usrs = umgr.search(usrCriteria, userCritAttrs, sParam);
			if (usrs != null && !usrs.isEmpty()) {
				usrManager = usrs.get(0).getEntityId();
			}

		} catch (UserSearchException | AccessDeniedException e) {
			logger.log(Level.SEVERE, null, e);
		}

		logger.exiting(className, "searchUserInOIMByRUT", usrManager);
		return usrManager;
	}

	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {

	}

	/**
	 * Read an excel file
	 * 
	 * @param absFilePath
	 * @throws Exception
	 */
	public void loadExternalUsers(String absFilePath) throws Exception {

		logger.entering(className, "loadExternalUsers", absFilePath);
		Map<String, List<String>> errorMap = new HashMap<String, List<String>>();

		logger.finer("Creating a Workbook from an Excel file (.xls or .xlsx)");
		Workbook workbook = WorkbookFactory.create(new File(absFilePath));

		logger.finest("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		logger.finer("Looping over Sheets looking for Usuarios");
		int sheetNumber = -1;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			String sheetName = workbook.getSheetAt(i).getSheetName();
			logger.finest("=> " + sheetName);
			if (sheetName != null && sheetName.equals("Usuarios")) {
				sheetNumber = i;
			}
		}

		logger.finer("Get the Sheet: Usuarios");
		Sheet sheet = workbook.getSheetAt(sheetNumber);

		logger.finer("Iterating over Rows and Columns beginning in the first users lines (line #5)");
		for (int i = 5; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			logger.finer("Check if not empty row");
			if (!isEmptyRow(row)) {
				logger.finer("Call process user from row #" + i);
				boolean success = processUser(row, errorMap);
				logger.finest("Process success: " + success);
			} else {
				break;
			}

		}

		logger.finer("Closing workbook");
		workbook.close();

		logger.finer("Sending notifications with errors");
		if (!errorMap.isEmpty()) {
			StringBuilder html = new StringBuilder(
					"<table border=\"1\"><tr><th><center><b>Fila</b></center></th><th><center><b>Error</b></center></th></tr><tr>");
			logger.severe("Errors processing file " + absFilePath);
			for (Map.Entry<String, List<String>> entry : errorMap.entrySet()) {
				String rowNum = entry.getKey();
				List<String> errrMsgList = entry.getValue();
				logger.severe("Errors in row " + rowNum + ":");
				html.append("<td rowspan=\"").append(errrMsgList.size()).append("\"><center><b>").append(rowNum)
						.append("</b></center></td>");
				for (String errorMsg : errrMsgList) {
					logger.severe(errorMsg);
					html.append("<td>").append(errorMsg).append("</td></tr><tr>");
				}
			}
			html.append("<td colspan=\"2\"><center>").append(errorMap.size())
					.append(" Usuarios con Errores</center></td></tr>").append("</table>");

			sendNotification(html);
		}

		logger.exiting(className, "loadExternalUsers", absFilePath);
	}

	/**
	 * Check if a row is empty
	 * 
	 * @param row
	 *            Row to check
	 * @return True if the row is empty. False if not.
	 */
	private static boolean isEmptyRow(Row row) {

		logger.entering(className, "isEmptyRow");
		boolean isEmpty = true;

		logger.finest("Looping over all cells of the row");
		for (Cell cell : row) {
			if (cell.getCellType() != Cell.CELL_TYPE_FORMULA && cell.getCellType() != Cell.CELL_TYPE_BLANK
					&& cell.getCellType() != Cell.CELL_TYPE_ERROR) {
				logger.finest("Cell with value found: " + cell);

				isEmpty = false;
				break;
			}
		}

		logger.exiting(className, "isEmptyRow", isEmpty);
		return isEmpty;

	}

	/**
	 * Process an user
	 * 
	 * @param row
	 *            Row of the sheet with the user info to process
	 * @return Flag for success of the operation
	 * @throws Exception
	 */
	private boolean processUser(Row row, Map<String, List<String>> errorMap) throws Exception {

		logger.entering(className, "processUser", row);
		boolean success = false;
		List<RequestEntityAttribute> attrs = new ArrayList<RequestEntityAttribute>();
		Utilities util = new Utilities();
		int rowNum = row.getRowNum() + 1;
		String firstName = null;
		String middleName = null;
		String lastName = null;
		String surName = null;
		String mgrRut = null;
		String mgrKey = null;
		String actKey = null;
		String rut = null;
		String contractType = null;
		String workCountry = null;
		String city = null;
		String address = null;
		String phone = null;
		String mobile = null;
		String email = null;
		String gestor = null;
		String channel = null;
		String supervisor = null;
		String rutSupervisor = null;
		String coordinator = null;
		String rutCoordinator = null;
		String externJob = null;
		String ability = null;
		String responsability = null;
		String stratum = null;
		String society = null;
		String unit = null;
		Date hireDate = null;
		Date endDate = null;
		String justification = null;
		String requesterRut = null;
		String requesterKey = null;
		String requesterDisplayName = null;
		String action = null;

		logger.finer("Getting all the atttributes of the user in the row");

		Cell firstNameCell = row.getCell(0);
		if (firstNameCell != null) {
			firstName = util.toCapitalizeString(getStringValueOfCell(firstNameCell));
			if (firstName != null && !firstName.isEmpty()) {
				logger.finest(UserManagerConstants.AttributeName.FIRSTNAME.getId() + ": " + firstName);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.FIRSTNAME.getId(), firstName,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}
		}

		Cell middleNameCell = row.getCell(1);
		if (middleNameCell != null) {
			middleName = util.toCapitalizeString(getStringValueOfCell(middleNameCell));
			if (middleName != null && !middleName.isEmpty()) {
				logger.finest(UserManagerConstants.AttributeName.MIDDLENAME.getId() + ": " + middleName);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.MIDDLENAME.getId(), middleName,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell lastNameCell = row.getCell(2);
		if (lastNameCell != null) {
			lastName = util.toCapitalizeString(getStringValueOfCell(lastNameCell));
			if (lastName != null && !lastName.isEmpty()) {
				logger.finest(UserManagerConstants.AttributeName.LASTNAME.getId() + ": " + lastName);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.LASTNAME.getId(), lastName,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell surNameCell = row.getCell(3);
		if (surNameCell != null) {
			surName = util.toCapitalizeString(getStringValueOfCell(surNameCell));
			if (surName != null && !surName.isEmpty()) {
				logger.finest("SurName: " + surName);
				RequestEntityAttribute attr = new RequestEntityAttribute("SurName", surName,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell mgrRutCell = row.getCell(4);
		if (mgrRutCell != null) {
			mgrRut = getStringValueOfCell(mgrRutCell).toUpperCase();
			if (mgrRut != null && !mgrRut.isEmpty()) {
				logger.finest("User Manager RUT: " + mgrRut);
				mgrKey = searchUserInOIMByRUT(mgrRut);
				logger.finest("User Manager Key: " + mgrKey);
				if (mgrKey != null) {
					RequestEntityAttribute attr = new RequestEntityAttribute("User Manager", Long.parseLong(mgrKey),
							RequestEntityAttribute.TYPE.Long);
					attrs.add(attr);
				}
			}
		}

		Cell actKeyCell = row.getCell(31); // Cell 5 - Organization Name
		if (actKeyCell != null) {
			actKey = getStringValueOfCell(actKeyCell);
			if (actKey != null && !actKey.isEmpty()) {
				logger.finest("Organization: " + actKey);
				RequestEntityAttribute attr = new RequestEntityAttribute("Organization", Long.parseLong(actKey),
						RequestEntityAttribute.TYPE.Long);
				attrs.add(attr);
			}

		}

		Cell contractTypeCell = row.getCell(32); // Cell 6 - Contract Type Name
		if (contractTypeCell != null) {
			contractType = getStringValueOfCell(contractTypeCell);
			if (contractType != null && !contractType.isEmpty()) {
				logger.finest("ContractType: " + contractType);
				RequestEntityAttribute attr = new RequestEntityAttribute("ContractType", contractType,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell rutCell = row.getCell(7);
		if (rutCell != null) {
			rut = getStringValueOfCell(rutCell).toUpperCase();
			if (rut != null && !rut.isEmpty()) {
				logger.finest("RUT: " + rut);
				RequestEntityAttribute attr = new RequestEntityAttribute("RUT", rut,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell workCountryCell = row.getCell(33); // Cell 8 - WorkCountry Name
		if (workCountryCell != null) {
			workCountry = getStringValueOfCell(workCountryCell);
			if (workCountry != null && !workCountry.isEmpty()) {
				logger.finest("WorkCountry: " + workCountry);
				RequestEntityAttribute attr = new RequestEntityAttribute("WorkCountry", workCountry,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell cityCell = row.getCell(9);
		if (cityCell != null) {
			city = (getStringValueOfCell(cityCell)).toUpperCase();
			if (city != null && !city.isEmpty()) {
				logger.finest("City: " + city);
				RequestEntityAttribute attr = new RequestEntityAttribute("City", city,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell addressCell = row.getCell(10);
		if (addressCell != null) {
			address = (getStringValueOfCell(addressCell)).toUpperCase();
			if (address != null && !address.isEmpty()) {
				logger.finest("Address: " + address);
				RequestEntityAttribute attr = new RequestEntityAttribute("Address", address,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell phoneCell = row.getCell(11);
		if (phoneCell != null) {
			phone = String.format("%.0f", phoneCell.getNumericCellValue());
			if (phone != null && !phone.isEmpty() && !phone.equals("0")) {
				logger.finest(UserManagerConstants.AttributeName.PHONE_NUMBER.getId() + ": " + phone);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.PHONE_NUMBER.getId(), phone,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell mobileCell = row.getCell(12);
		if (mobileCell != null) {
			mobile = String.format("%.0f", mobileCell.getNumericCellValue());
			if (mobile != null && !mobile.isEmpty() && !mobile.equals("0")) {
				logger.finest(UserManagerConstants.AttributeName.MOBILE.getId() + ": " + mobile);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.MOBILE.getId(), mobile, RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell emailCell = row.getCell(13);
		if (emailCell != null) {
			email = (getStringValueOfCell(emailCell)).toLowerCase();
			if (email != null && !email.isEmpty()) {
				logger.finest(UserManagerConstants.AttributeName.EMAIL.getId() + ": " + email);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.EMAIL.getId(), email, RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell gestorCell = row.getCell(34); // Cell 14 - Gestor Name
		if (gestorCell != null) {
			gestor = getStringValueOfCell(gestorCell);
			if (gestor != null && !gestor.isEmpty()) {
				logger.finest("Gestor: " + gestor);
				RequestEntityAttribute attr = new RequestEntityAttribute("Gestor", gestor,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
				gestorLogin = gestor;
			}

		}

		Cell channelCell = row.getCell(35); // Cell 15 - Channel Name
		if (channelCell != null) {
			channel = getStringValueOfCell(channelCell);
			if (channel != null && !channel.isEmpty()) {
				logger.finest("Channel: " + channel);
				RequestEntityAttribute attr = new RequestEntityAttribute("Channel", channel,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell supervisorCell = row.getCell(16);
		if (supervisorCell != null) {
			supervisor = util.toCapitalizeString(getStringValueOfCell(supervisorCell));
			if (supervisor != null && !supervisor.isEmpty()) {
				logger.finest("Supervisor: " + supervisor);
				RequestEntityAttribute attr = new RequestEntityAttribute("Supervisor", supervisor,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell rutSupervisorCell = row.getCell(17);
		if (rutSupervisorCell != null) {
			rutSupervisor = getStringValueOfCell(rutSupervisorCell).toUpperCase();
			if (rutSupervisor != null && !rutSupervisor.isEmpty()) {
				logger.finest("RUTSupervisor: " + rutSupervisor);
				RequestEntityAttribute attr = new RequestEntityAttribute("RUTSupervisor", rutSupervisor,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell coordinatorCell = row.getCell(18);
		if (coordinatorCell != null) {
			coordinator = util.toCapitalizeString(getStringValueOfCell(coordinatorCell));
			if (coordinator != null && !coordinator.isEmpty()) {
				logger.finest("Coordinator: " + coordinator);
				RequestEntityAttribute attr = new RequestEntityAttribute("Coordinator", coordinator,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell rutCoordinatorCell = row.getCell(19);
		if (rutCoordinatorCell != null) {
			rutCoordinator = getStringValueOfCell(rutCoordinatorCell).toUpperCase();
			if (rutCoordinator != null && !rutCoordinator.isEmpty()) {
				logger.finest("RUTCoordinator: " + rutCoordinator);
				RequestEntityAttribute attr = new RequestEntityAttribute("RUTCoordinator", rutCoordinator,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell externJobCell = row.getCell(36); // Cell 20 - ExternJob Name
		if (externJobCell != null) {
			externJob = getStringValueOfCell(externJobCell);
			if (externJob != null && !externJob.isEmpty()) {
				logger.finest("ExternJob: " + externJob);
				RequestEntityAttribute attr = new RequestEntityAttribute("ExternJob", externJob,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell abilityCell = row.getCell(37); // Cell 21 - ExternJob Name
		if (abilityCell != null) {
			ability = getStringValueOfCell(abilityCell);
			if (ability != null && !ability.isEmpty()) {
				logger.finest("Ability: " + ability);
				RequestEntityAttribute attr = new RequestEntityAttribute("Ability", ability,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell responsabilityCell = row.getCell(38); // Cell 22 - Responsability Name
		if (responsabilityCell != null) {
			responsability = getStringValueOfCell(responsabilityCell);
			if (responsability != null && !responsability.isEmpty()) {
				logger.finest("Responsability: " + responsability);
				RequestEntityAttribute attr = new RequestEntityAttribute("Responsability", responsability,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}
		}

		Cell stratumCell = row.getCell(39); // Cell 23 - Stratum Name
		if (stratumCell != null) {
			stratum = getStringValueOfCell(stratumCell);
			if (stratum != null && !stratum.isEmpty()) {
				logger.finest("Stratum: " + stratum);
				RequestEntityAttribute attr = new RequestEntityAttribute("Stratum", stratum,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell societyCell = row.getCell(40); // Cell 24 - Society Name
		if (societyCell != null) {
			society = getStringValueOfCell(societyCell);
			if (society != null && !society.isEmpty()) {
				logger.finest("Society: " + society);
				RequestEntityAttribute attr = new RequestEntityAttribute("Society", society,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell unitCell = row.getCell(41); // Cell 25 - Unit Name
		if (unitCell != null) {
			unit = getStringValueOfCell(unitCell);
			if (unit != null && !unit.isEmpty()) {
				logger.finest("Unit: " + unit);
				RequestEntityAttribute attr = new RequestEntityAttribute("Unit", unit,
						RequestEntityAttribute.TYPE.String);
				attrs.add(attr);
			}

		}

		Cell hireDateCell = row.getCell(26);
		if (hireDateCell != null) {
			hireDate = hireDateCell.getDateCellValue();
			if (hireDate != null) {
				logger.finest(UserManagerConstants.AttributeName.HIRE_DATE.getId() + ": " + hireDate);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.HIRE_DATE.getId(), hireDate,
						RequestEntityAttribute.TYPE.Date);
				attrs.add(attr);
			}

		}

		Cell endDateCell = row.getCell(27);
		if (endDateCell != null) {
			endDate = endDateCell.getDateCellValue();
			if (endDate != null) {
				logger.finest(UserManagerConstants.AttributeName.HIRE_DATE.getId() + ": " + endDate);
				RequestEntityAttribute attr = new RequestEntityAttribute(
						UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), endDate,
						RequestEntityAttribute.TYPE.Date);
				attrs.add(attr);
			}

		}

		Cell justificationCell = row.getCell(28);
		if (justificationCell != null) {
			justification = getStringValueOfCell(justificationCell);
			if (justification != null && !justification.isEmpty()) {
				logger.finest("Justification: " + justification);
			}
		}

		Cell requesterCell = row.getCell(29);
		if (requesterCell != null) {
			requesterRut = getStringValueOfCell(requesterCell).toUpperCase();
			if (requesterRut != null && !requesterRut.isEmpty()) {
				logger.finest("Requester Rut: " + requesterRut);
				requesterKey = searchUserInOIMByRUT(requesterRut);
				logger.finest("Requester Key: " + requesterKey);

				if (requesterKey != null) {
					Set<String> retAttrs = new HashSet<>();
					retAttrs.add(UserManagerConstants.AttributeName.DISPLAYNAME.getId());

					try {
						String requesterLogin = getUser(requesterKey, false);
						solicitanteLogin = requesterLogin;

					} catch (Exception e) {
						logger.log(Level.SEVERE, "Unexpected error checking addressee login for user " + rut, e);
					}

					try {
						User usr = umgr.getDetails(requesterKey, retAttrs, false);
						logger.finest("DisplayName of Requester  " + requesterKey + " is: " + usr.getDisplayName());
						requesterDisplayName = usr.getDisplayName();

					} catch (Exception e) {
						logger.log(Level.SEVERE, "Unexpected error checking organization for user " + rut, e);
					}
				}
			}
		}

		Cell actionCell = row.getCell(30);
		if (actionCell != null) {
			action = getStringValueOfCell(actionCell);
			if (action != null && !action.isEmpty()) {
				logger.finest("Action: " + action);
			}
		}

		logger.finer("Adding extra attributes for all users");
		RequestEntityAttribute attr = new RequestEntityAttribute(UserManagerConstants.AttributeName.EMPTYPE.getId(),
				"Consultant", RequestEntityAttribute.TYPE.String);
		logger.finest(UserManagerConstants.AttributeName.EMPTYPE.getId() + ": Consultant");
		attrs.add(attr);
		attr = new RequestEntityAttribute("User Type", false, RequestEntityAttribute.TYPE.Boolean);
		logger.finest("User Type: false");
		attrs.add(attr);
		attr = new RequestEntityAttribute("Origin", "EXCEL", RequestEntityAttribute.TYPE.String);
		logger.finest("Origin: EXCEL");
		attrs.add(attr);

		logger.finer("Checking for required attributes based in the operation to execute");
		boolean isValid = checkFieldsValues(firstName, middleName, lastName, surName, mgrRut, mgrKey, actKey, rut,
				contractType, workCountry, city, address, phone, mobile, email, gestor, channel, supervisor,
				rutSupervisor, coordinator, rutCoordinator, externJob, ability, responsability, stratum, society, unit,
				hireDate, endDate, justification, requesterRut, requesterKey, action, rowNum, errorMap);

		logger.fine("Checking if all attributes are valid");
		if (!isValid) {
			logger.severe("Problem with input values from user: " + rut + " in row: " + rowNum);
			return success;
		}

		if (action != null && action.equalsIgnoreCase("Crear Usuario")) {

			logger.finer("Calling the creation request procedure");
			success = requestUserCreation(attrs, justification, requesterDisplayName, rowNum, errorMap);

		} else if (action != null && action.equalsIgnoreCase("Modificar Usuario")) {

			logger.finer("Getting the userKey of the user with RUT: " + rut);
			String usrKey = searchUserInOIMByRUT(rut);

			logger.finer("Calling the modification request procedure");
			success = requestUserModification(usrKey, attrs, justification, requesterDisplayName, rowNum, errorMap);

		} else {

			logger.severe("Unespecified action for user " + rut + " in row: " + rowNum);
		}

		logger.exiting(className, "processUser", success);
		return success;

	}

	/**
	 * Return the string value of a cell
	 * 
	 * @param cell
	 *            Cell to convert
	 * @return String value of a cell
	 */
	private String getStringValueOfCell(Cell cell) {

		logger.entering(className, "getStringValueOfCell", cell);
		String value = null;

		logger.finest("Checking if not null the cell");
		if (cell != null) {

			logger.finest("Checking cell type");
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				logger.finest("It is a String");
				value = cell.getStringCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				logger.finest("It is a Number. Converting...");
				value = String.valueOf((int) cell.getNumericCellValue());
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				logger.finest("It is Empty");
				value = "";
			}
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				logger.finest("It is a Formula. Checking type of Value");
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
					logger.finest("It is a String");
					value = cell.getStringCellValue();
				}
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
					logger.finest("It is a Number. Converting...");
					value = String.valueOf((int) cell.getNumericCellValue());
				}
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_BLANK) {
					logger.finest("It is Empty");
					value = "";
				}
			}

		}

		logger.exiting(className, "getStringValueOfCell", value);
		return value;

	}

	/**
	 * Creates the user creation request for a user.
	 * 
	 * @param attrs
	 *            Attributes of the users to create
	 * @param justification
	 *            Justification of the Request
	 * @param requester
	 *            Requester of the creation
	 * @return Flag of success of the create operation.
	 */
	private boolean requestUserCreation(List<RequestEntityAttribute> attrs, String justification, String requester,
			int rowNum, Map<String, List<String>> errorMap) {

		logger.entering(className, "requestUserCreation", attrs);
		boolean success = false;

		try {

			logger.finer("Creating Objects for the request");
			RequestData requestData = new RequestData();
			RequestEntity requestEntity = new RequestEntity();
			List<RequestEntity> entities = new ArrayList<RequestEntity>();

			logger.finer("Setting attrs for the user");
			requestEntity.setRequestEntityType(OIMType.User);
			requestEntity.setOperation(RequestConstants.MODEL_CREATE_OPERATION);
			requestEntity.setEntityData(attrs);

			logger.finer("Setting entities and justification for the request");
			entities.add(requestEntity);
			requestData.setTargetEntities(entities);
			requestData.setJustification(
					justification + "\nSolicitud hecha por: " + requester + "\nCarga Masiva de Usuarios");

			logger.finer("Submit the request");
			rsrv.submitRequest(requestData);

			logger.finer("Setting success flag");
			success = true;

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			List<String> errorMsgList = new ArrayList<String>();
			errorMsgList.add("Error inesperado en la creación en OIM del usuario de la fila " + rowNum
					+ ". Contacte al administrador del sistema.");
			errorMap.put(String.valueOf(rowNum), errorMsgList);
			logger.log(Level.SEVERE, "requestUserCreation - Unexpected error", e);

		}

		logger.exiting(className, "requestUserCreation", success);
		return success;
	}

	/**
	 * Creates the user modification request for a user.
	 * 
	 * @param attrs
	 *            Attributes of the users to update
	 * @param justification
	 *            Justification of the Request
	 * @param requester
	 *            Requester of the modification
	 * @return Flag of success of the Update operation.
	 */
	private boolean requestUserModification(String usrKey, List<RequestEntityAttribute> attrs, String justification,
			String requester, int rowNum, Map<String, List<String>> errorMap) {

		logger.entering(className, "requestUserModification", new Object[] { usrKey, attrs });
		boolean success = false;

		try {

			logger.finer("Creating Objects for the request");
			RequestData requestData = new RequestData();
			RequestEntity requestEntity = new RequestEntity();
			List<RequestEntity> entities = new ArrayList<RequestEntity>();

			logger.finer("Setting attrs for the user");
			requestEntity.setRequestEntityType(OIMType.User);
			requestEntity.setEntityKey(usrKey);
			requestEntity.setOperation(RequestConstants.MODEL_MODIFY_OPERATION);
			requestEntity.setEntityData(attrs);

			logger.finer("Setting entities and justification for the request");
			entities.add(requestEntity);
			requestData.setTargetEntities(entities);
			requestData.setJustification(
					justification + "\nSolicitud hecha por: " + requester + "\nCarga Masiva de Usuarios");

			logger.finer("Submit the request");
			rsrv.submitRequest(requestData);

			logger.finer("Setting success flag");
			success = true;

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			List<String> errorMsgList = new ArrayList<String>();
			errorMsgList.add("Error inesperado en la modificación en OIM del usuario de la fila " + rowNum
					+ ". Contacte al administrador del sistema.");
			errorMap.put(String.valueOf(rowNum), errorMsgList);
			logger.log(Level.SEVERE, "requestUserModification - Unexpected error", e);

		}

		logger.exiting(className, "requestUserModification", success);
		return success;
	}

	/**
	 * CHeck for a valid rut
	 * 
	 * @param rut
	 *            Rut to validate
	 * @return FLag if it is valid
	 */
	private static boolean validateRut(String rut) {

		logger.entering(className, "validateRut", rut);
		boolean validacion = false;

		try {

			logger.finer("Removing separator characters");
			rut = rut.toUpperCase();
			rut = rut.replace(".", "");
			rut = rut.replace("-", "");

			logger.finer("Checking if number (without verification digit");
			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
			char dv = rut.charAt(rut.length() - 1);

			logger.finer("Checking algorith");
			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
				s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			if (dv == (char) (s != 0 ? s + 47 : 75)) {
				logger.finest("Is valid: " + rut);
				validacion = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "validateRut - Unexpected error", e);
		}

		logger.exiting(className, "validateRut", validacion);
		return validacion;
	}

	/**
	 * Check if all value are valid for the operation
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param surName
	 * @param mgrRut
	 * @param mgrKey
	 * @param actKey
	 * @param rut
	 * @param contractType
	 * @param workCountry
	 * @param city
	 * @param address
	 * @param phone
	 * @param mobile
	 * @param email
	 * @param gestor
	 * @param channel
	 * @param supervisor
	 * @param rutSupervisor
	 * @param coordinator
	 * @param rutCoordinator
	 * @param externJob
	 * @param ability
	 * @param responsability
	 * @param stratum
	 * @param society
	 * @param unit
	 * @param hireDate
	 * @param endDate
	 * @param justification
	 * @param requester
	 * @param action
	 * @return
	 */
	private boolean checkFieldsValues(String firstName, String middleName, String lastName, String surName,
			String mgrRut, String mgrKey, String actKey, String rut, String contractType, String workCountry,
			String city, String address, String phone, String mobile, String email, String gestor, String channel,
			String supervisor, String rutSupervisor, String coordinator, String rutCoordinator, String externJob,
			String ability, String responsability, String stratum, String society, String unit, Date hireDate,
			Date endDate, String justification, String requesterRut, String requesterKey, String action, int rowNum,
			Map<String, List<String>> errorMap) {

		logger.entering(className, "checkFieldsValues",
				new Object[] { firstName, middleName, lastName, surName, mgrRut, mgrKey, actKey, rut, contractType,
						workCountry, city, address, phone, mobile, email, gestor, channel, supervisor, rutSupervisor,
						coordinator, rutCoordinator, externJob, ability, responsability, stratum, society, unit,
						hireDate, endDate, justification, requesterRut, requesterKey, action, rowNum });

		boolean isValidRow = true;
		List<String> errorMsgList = new ArrayList<String>();

		logger.finer("Validating RUT");
		if (rut == null || rut.isEmpty()) {
			logger.severe("Empty RUT");
			errorMsgList.add("El campo RUT se encuentra vacío.");
			isValidRow = false;
		} else {
			boolean isValid = validateRut(rut);
			if (!isValid) {
				logger.severe("Invalid RUT");
				errorMsgList.add("El campo RUT no es un RUT válido");
				isValidRow = false;
			}
		}

		logger.finer("Validating Action");
		if (action == null || action.isEmpty()) {
			logger.severe("Empty Action");
			errorMsgList.add("El campo Acción a Ejecutar se encuentra vacío.");
			isValidRow = false;
		}

		logger.finer("Validating Justification");
		if (justification == null || justification.isEmpty()) {
			logger.severe("Empty Justification");
			errorMsgList.add("El campo Justificación se encuentra vacío.");
			isValidRow = false;
		}

		logger.finer("Validating Requester");
		if (requesterRut == null || requesterRut.isEmpty()) {
			logger.severe("Empty requesterRut");
			errorMsgList.add("El campo RUT Solicitante se encuentra vacío.");
			isValidRow = false;
		} else {
			boolean isValid = validateRut(requesterRut);
			if (!isValid) {
				logger.severe("Invalid requesterRut");
				errorMsgList.add("El campo RUT Solicitante no es un RUT válido");
				isValidRow = false;
			} else {
				if (requesterKey == null || requesterKey.isEmpty()) {
					logger.severe("Requester not exists in OIM");
					errorMsgList.add("El Solicitante no existe en OIM");
					isValidRow = false;
				}
			}
		}

		logger.finer("Checking for required fields in Create Operation");
		if (action != null && action.equalsIgnoreCase("Crear Usuario")) {

			logger.finer("Validating RUT");
			if (isInternalControlList(rut)) {
				logger.severe("RUT in Internal Control List");
				String reason = getReasonForInternalList(rut);
				errorMsgList.add(reason);
				isValidRow = false;

			}
			if (existsUserRUT(rut)) {
				logger.severe("Duplicate RUT");
				String reason = "El rut existe ya en OIM para otro usuario";
				errorMsgList.add(reason);
				isValidRow = false;

			}

			logger.finer("Validating FirstName");
			if (firstName == null || firstName.isEmpty()) {
				logger.severe("Empty FirstName");
				errorMsgList.add("El campo Primer Nombre se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating LastName");
			if (lastName == null || lastName.isEmpty()) {
				logger.severe("Empty LastName");
				errorMsgList.add("El campo Apellido Paterno se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating mgrRut");
			if (mgrRut == null || mgrRut.isEmpty()) {
				logger.severe("Empty mgrRut");
				errorMsgList.add("El campo RUT Jefe Directo se encuentra vacío.");
				isValidRow = false;
			} else {
				boolean isValid = validateRut(mgrRut);
				if (!isValid) {
					logger.severe("Invalid mgrRut");
					errorMsgList.add("El campo RUT Jefe Directo no es un RUT válido");
					isValidRow = false;
				} else {
					if (mgrKey == null || mgrKey.isEmpty()) {
						logger.severe("Manager not exists in OIM");
						errorMsgList.add("El Jefe Directo no existe en OIM");
						isValidRow = false;
					}
				}
			}

			logger.finer("Validating Organization");
			if (actKey == null || actKey.isEmpty()) {
				logger.severe("Empty Organization");
				errorMsgList.add("El campo Organización se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Contract Type");
			if (contractType == null || contractType.isEmpty()) {
				logger.severe("Empty Contract Type");
				errorMsgList.add("El campo Tipo de Contrato se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Work Country");
			if (workCountry == null || workCountry.isEmpty()) {
				logger.severe("Empty Work Country");
				errorMsgList.add("El campo País se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating City");
			if (city == null || city.isEmpty()) {
				logger.severe("Empty City");
				errorMsgList.add("El campo Ciudad se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Tlf Mobile");
			if (mobile == null || mobile.isEmpty()) {
				logger.severe("Empty Mobile");
				errorMsgList.add("El campo Tlf Movil se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Email");
			if (email == null || email.isEmpty()) {
				logger.severe("Empty Email");
				errorMsgList.add("El campo Email se encuentra vacío.");
				isValidRow = false;
			} else { 
				if (!isValid(email)) {
					logger.severe("Invalid Email");
					errorMsgList.add("El campo Email es inválido.");
					isValidRow = false;
				}
				if (existsEmail(email)) {
					logger.severe("Email already exists");
					errorMsgList.add("El email ya existe registrado para otro usuario.");
					isValidRow = false;
				}
				
				
			}

			logger.finer("Validating Gestor");
			if (gestor == null || gestor.isEmpty()) {
				logger.severe("Empty Gestor");
				errorMsgList.add("El campo Gestor se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Extern Job");
			if (externJob == null || externJob.isEmpty()) {
				logger.severe("Empty Extern Job");
				errorMsgList.add("El campo Cargo se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating Hire Date");
			if (hireDate == null) {
				logger.severe("Empty Hire Date");
				errorMsgList.add("El campo Fecha de Ingreso se encuentra vacío.");
				isValidRow = false;
			}

			logger.finer("Validating End Date");
			if (endDate == null) {
				logger.severe("Empty End Date");
				errorMsgList.add("El campo Fecha de Fin de Contrato se encuentra vacío.");
				isValidRow = false;
			} else {
				
				logger.finer("Setting the EndDate");
				Properties properties = new Properties();
				try {
					properties.load(
							new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
					int maxDays = Integer.parseInt(properties.getProperty("entel.oim.creation.external.users.max.days"));
					Calendar calToday = Calendar.getInstance();
					Calendar calEndDate = Calendar.getInstance();
					calEndDate.setTime(endDate);
					// Get the represented date in milliseconds
					long millis1 = calToday.getTimeInMillis();
					long millis2 = calEndDate.getTimeInMillis();
					// Calculate difference in milliseconds
					long diff = millis2 - millis1;
					// Calculate difference in days
					long diffDays = diff / (24 * 60 * 60 * 1000);
					logger.finest("Duration for the user: " + diffDays);
	
					logger.fine("Checking if it is more than one year");
					if (diffDays >= maxDays) {
						logger.fine("Max days reached");
						errorMsgList.add("La cuenta no puede tener una vigencia superior a los 365 dias a la fecha de hoy");
						isValidRow = false;
					}
				
				} catch (Exception e) {
					logger.log(Level.SEVERE, "checkFieldsValues - Unexpected Error", e);
					errorMsgList.add("No se puede determinar la fecha fin del usuario");
					isValidRow = false;
				}
			}

		}

		logger.finer("Checking for required fields in Update Operation");
		if (action != null && action.equalsIgnoreCase("Modificar Usuario")) {

			logger.finer("Getting the userKey of the user with RUT: " + rut);
			String usrKey = searchUserInOIMByRUT(rut);

			logger.finer("Checking if usrKey found");
			if (usrKey != null) {
				Set<String> retAttrs = new HashSet<>();
				retAttrs.add(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId());
				try {
					User usr = umgr.getDetails(usrKey, retAttrs, false);
					logger.finest("User Organization for " + usrKey + " is: " + usr.getOrganizationKey());

					if (!usr.getOrganizationKey().equals(actKey)) {
						logger.severe("User " + rut + " in not belongs to the Organization to Update");
						errorMsgList.add("El usuario con RUT " + rut + " no puede actualizarle la Organización");
						isValidRow = false;
					}

				} catch (Exception e) {
					logger.severe("Unexpected error checking organization for user " + rut);
					errorMsgList.add("No se pudo determinar la Organización del usuario con RUT " + rut);
					isValidRow = false;
				}

			} else {
				logger.severe("UsrKey not found for user " + rut);
				errorMsgList.add("El usuario con RUT " + rut + " no existe en OIM");
				isValidRow = false;
			}

		}

		logger.finer("Checking if error to add the error list");
		if (!isValidRow) {
			errorMap.put(String.valueOf(rowNum), errorMsgList);
		}

		logger.exiting(className, "checkFieldsValues", isValidRow);
		return isValidRow;

	}

	private void sendNotification(StringBuilder html) {

		logger.entering(className, "sendNotification");

		// Construction of template parameters
		HashMap<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("info_html", html.toString());

		logger.fine("Begins building list of addressees.");
		String[] adresseeOperaciones = usersLogins("Operaciones");
		String[] adresseeSeguridad = usersLogins("Seguridad");

		String[] adressee = new String[adresseeOperaciones.length + adresseeSeguridad.length + 2];

		for (int i = 0; i < adresseeOperaciones.length; i++) {
			adressee[i] = adresseeOperaciones[i];
		}

		for (int i = 0; i < adresseeSeguridad.length; i++) {
			adressee[adresseeOperaciones.length + i] = adresseeSeguridad[i];
		}
		
		adressee[adresseeOperaciones.length + adresseeSeguridad.length] = (gestorLogin != null) ? gestorLogin : "";

		
		adressee[adresseeOperaciones.length + adresseeSeguridad.length + 1] = (solicitanteLogin!=null)?solicitanteLogin : "";
		
		logger.fine("List of adressees build.");

		sendEmailNotification("UserBulkLoad Alert", templateParams, adressee);

		logger.exiting(className, "sendNotification");

	}

	/**
	 * List of users logins that belong to a group
	 * 
	 * @param groupName
	 *            Group name to retrieve users
	 * @return
	 */
	private String[] usersLogins(String groupName) {

		logger.entering(className, "usersLogins", groupName);

		List<String> ids = new ArrayList<>();
		try {
			Set<String> attrNames = new HashSet<>();
			attrNames.add(RoleManagerConstants.ROLE_KEY);

			Role role = rmgr.getDetails(RoleManagerConstants.ROLE_NAME, groupName, attrNames);
			List<User> users = rmgr.getRoleMembers((String) role.getAttribute(RoleManagerConstants.ROLE_KEY), true);

			for (User user : users) {
				ids.add(user.getLogin());
			}

		} catch (SearchKeyNotUniqueException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (NoSuchRoleException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - NoSuchRoleException error", e);
		} catch (RoleLookupException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - RoleLookupException error", e);
		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (RoleMemberException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}

		return ids.toArray(new String[ids.size()]);

	}

	/**
	 * Sending email with information to group's addressees
	 * 
	 * @param templateName
	 *            The template name that will be used
	 * @param templateParams
	 *            Parameters to be replaced in the template
	 * @param addressee
	 *            List of addressees
	 */
	private void sendEmailNotification(String templateName, HashMap<String, Object> templateParams,
			String[] addressee) {

		logger.entering(className, "sendEmailNotification", templateName);

		NotificationEvent ev = new NotificationEvent();

		try {

			// Setting template through the property
			ev.setTemplateName(templateName);
			// Loading users
			ev.setUserIds(addressee);
			// Setting params to replace in the template
			ev.setParams(templateParams);

			String finalAddressees = "";
			for(String a : ev.getUserIds()) {
				finalAddressees=finalAddressees.concat(a).concat(", ");
			}
			finalAddressees=finalAddressees.substring(0, finalAddressees.length()-2);
			
			logger.fine("Sending subordinated manager notification [UserBulkLoad Alert] to addressees: "
					+ finalAddressees);

			nsrv.notify(ev);

		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}
	}

	/**
	 * Check if the RUT is in the Internal Control List
	 * 
	 * @param rut
	 *            RUT to ckeck
	 * @return true if user is in Internal Control List. false if not.
	 */
	public boolean isInternalControlList(String rut) {
		logger.entering(className, "isInternalControlList", rut);
		boolean isInList = false;

		try {

			logger.finer("Getting lookup values from " + LOOKUP_USERS_INTERNALCONTROLLIST);
			tcResultSet rs = lookupOps.getLookupValues(LOOKUP_USERS_INTERNALCONTROLLIST);

			logger.finer("Looping over results");
			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				String tmpRut = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
				logger.finest("Checking value: " + tmpRut);
				if (tmpRut != null && tmpRut.equalsIgnoreCase(rut)) {
					logger.finer("User " + rut + " is in Internal Control List");
					isInList = true;
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "isInternalControlList - Unexpected error", e);
		}

		logger.exiting(className, "isInternalControlList", isInList);
		return isInList;
	}

	/**
	 * Get the reason for an user is in the Internal Control List
	 * 
	 * @param rut
	 *            RUT to find
	 * @return Reason of the user in the list
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
	 * Check if is a valid Excel File
	 * 
	 * @param absFilePath
	 * @throws Exception
	 */
	public boolean checkIfValidFile(String absFilePath, String key) throws Exception {

		logger.entering(className, "checkIfValidFile", absFilePath);
		boolean isValid = false;
		Map<String, List<String>> errorMap = new HashMap<String, List<String>>();

		logger.finer("Creating a Workbook from an Excel file (.xls or .xlsx)");
		Workbook workbook = WorkbookFactory.create(new File(absFilePath));

		logger.finest("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		logger.finer("Looping over Sheets looking for Key");
		int sheetNumber = -1;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			String sheetName = workbook.getSheetAt(i).getSheetName();
			logger.finest("=> " + sheetName);
			if (sheetName != null && sheetName.equals("Key")) {
				sheetNumber = i;
			}
		}

		logger.fine("Checking if sheet found");
		if (sheetNumber != -1) {

			logger.finer("Get the Sheet: Key");
			Sheet sheet = workbook.getSheetAt(sheetNumber);

			logger.finer("Get the row with the key value");
			Row row = sheet.getRow(0);
			Cell keyCell = row.getCell(0);
			String keyString = getStringValueOfCell(keyCell);

			logger.finer("Checking the key " + keyString + " with the configuration key");
			if (key.equals(keyString)) {
				logger.finest("It is the same key. It is a valid Excel file");
				isValid = true;
			} else {
				logger.severe("Keys are different.");
			}

		} else {
			logger.severe("Sheet Key not found!");
		}

		logger.finer("Closing workbook");
		workbook.close();

		if (!isValid) {
			String index = "0";
			String error = "Invalid source file. Please use the last revision of the template";
			logger.severe(error);
			List<String> errorMsjList = new ArrayList<String>();
			errorMsjList.add(error);
			errorMap.put(index, errorMsjList);

			logger.finer("Sending notifications with errors");
			if (!errorMap.isEmpty()) {
				StringBuilder html = new StringBuilder(
						"<table><tr><th><center><b>Fila</b></center></th><th><center><b>Error</b></center></th></tr><tr>");
				logger.severe("Errors processing file " + absFilePath);
				for (Map.Entry<String, List<String>> entry : errorMap.entrySet()) {
					String rowNum = entry.getKey();
					List<String> errrMsgList = entry.getValue();
					logger.severe("Errors in row " + rowNum + ":");
					html.append("<td rowspan=\"").append(errrMsgList.size()).append("\"><center><b>").append(rowNum)
							.append("</b></center></td>");
					for (String errorMsg : errrMsgList) {
						logger.severe(errorMsg);
						html.append("<td>").append(errorMsg).append("</td></tr><tr>");
					}
				}
				html.append("<td colspan=\"2\"><center>").append(errorMap.size())
						.append(" Usuarios con Errores</center></td></tr>").append("</table>");

				sendNotification(html);
			}
		}

		logger.exiting(className, "checkIfValidFile", isValid);
		return isValid;
	}

	/**
	 * Check if the user rut exists in OIM
	 * 
	 * @param rut
	 *            User rut to check
	 * @return True if the user rut exist in OIM. False if the user login doesn't
	 *         exist in OIM
	 */
	public static boolean existsUserRUT(String rut) {

		logger.entering(className, "existsUserRUT", rut);
		boolean exist = false;
		PreparedStatement stmt = null;
		Connection oimdb = null;
		try {

			logger.fine("Getting OIM Connection");
			oimdb = Platform.getOperationalDS().getConnection();

			logger.fine("Constructing SQL");
			String sql = "select 1 from usr where TRIM(UPPER(usr_udf_rut)) = UPPER('" + rut.trim()
					+ "') and usr_status in ('Active','Disabled','Disabled Until Start Date') and usr_automatically_delete_on is null";
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
		} finally {
			if (stmt != null) {
				try {
					logger.finest("Closing Prepared Statement...");
					stmt.close();
				} catch (Exception e2) {
					logger.log(Level.SEVERE, "Unexpected error - existsUserRUT", e2);
					e2.printStackTrace();
				}
			}
			if (oimdb != null ) {
				try {
					logger.finer("Closing database connection!");
					oimdb.close();
				} catch (Exception e2) {
					logger.log(Level.SEVERE, "Unexpected error - existsUserRUT", e2);
					e2.printStackTrace();
				}
			}
		}

		logger.exiting(className, "existsUserRUT", exist);
		return exist;

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
	 * Check if an email is valid
	 * @param email Email to check
	 * @return True if email is valid. False if not
	 */
	private boolean isValid(String email) { 
        
		logger.entering(className, "isValid",email);
		boolean valid = false;
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        if (email != null){
        	logger.finer("Checking if valid email");
        	Pattern pat = Pattern.compile(emailRegex); 
            valid = pat.matcher(email).matches();
        }
        
        logger.exiting(className, "isValid",valid);
		return valid;
    }
	
	
	
	/**
	 * Check if the user email exists in OIM
	 * @param email User Email to check
	 * @return True if the user email exist in OIM. False if the user login doesn't exist in OIM
	 */
	public static boolean existsEmail(String email)  {
		
		logger.entering(className, "existsEmail",email);
		boolean exist = false;
		PreparedStatement stmt = null;
		Connection oimdb= null;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.fine("Constructing SQL");
	    	String sql = "select 1 from usr where UPPER(usr_email) = UPPER('" +email+"') and usr_status != 'Deleted'";
			logger.finest("SQL to execute: " + sql);
					
	    	logger.finer("Executing query");
	    	stmt = oimdb.prepareStatement(sql);
	    	ResultSet rs = stmt.executeQuery();
	
		    if (!rs.isBeforeFirst()) {
		    	logger.finer("No emails found!");
		    } else {
		    	exist = true;
			}
				    
		    logger.finest("Closing result set...");
		    rs.close();
	    	
	    } catch (SQLException e) {
			logger.log(Level.SEVERE, "Unexpected error - existsEmail", e);
			e.printStackTrace();
		} finally {
    		if (stmt != null) {
    			logger.finest("Closing Prepared Statement...");
    			try {
    				stmt.close();
    			} catch (Exception exp) {
    				exp.printStackTrace();
					logger.log(Level.SEVERE, "Unexpected Error - existsEmail", exp);
    			}
		    }
    		if (oimdb != null) {
    			logger.finer("Closing database connection!");
    			try {
    				oimdb.close();
	    		} catch (Exception exp) {
	    			exp.printStackTrace();
					logger.log(Level.SEVERE, "Unexpected Error - existsEmail", exp);
				}
		    }
    	}
    	
    	logger.exiting(className, "existsEmail",exist);
		return exist;
		
	}

}
