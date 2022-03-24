package entel.oim.plugins.namegenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.UserNameGenerationException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.AbstractUserNameGenerationPolicy;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.api.UserNameGenerationPolicy;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

/**
 * Contains the policy for userlogin generation
 * 
 * @author Oracle
 *
 */
public class CustomUsernamePolicy extends AbstractUserNameGenerationPolicy implements UserNameGenerationPolicy {

	private final static String className = CustomUsernamePolicy.class.getName();
	private static Logger logger = Logger.getLogger(className);

	// Max size of userlogin
	private final static int MAX_CHARACTERS = 15;

	// Service for system properties
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private static OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);
	private static tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);

	// Attr Keys
	private final static String ATTR_FIRSTNAME_ID = UserManagerConstants.AttributeName.FIRSTNAME.getId();
	private final static String ATTR_MIDDLENAME_ID = UserManagerConstants.AttributeName.MIDDLENAME.getId();
	private final static String ATTR_LASTNAME_ID = UserManagerConstants.AttributeName.LASTNAME.getId();
	private final static String ATTR_SURNAME_ID = "SurName";
	private final static String ATTR_ORIGIN_ID = "Origin";
	private final static String ATTR_RUT_ID = "RUT";
	private final static String ATTR_USER_ORG_ID = UserManagerConstants.AttributeName.USER_ORGANIZATION.getId();
	private final static String ATTR_ORG_NAME_ID = OrganizationManagerConstants.AttributeName.ORG_NAME.getId();
	private final static String ATTR_ORG_PREFIX_ID = "Prefix";

	// Origins of creation of users
	public final static String SUCCESSFACTORS_ENTEL = "SSFF-TRUSTED";

	// Reserved words list for user login
	public static List<String> RESERVED_WORDS_LIST = Arrays.asList("FILHO", "NETO", "JUNIOR", "JR", "HIJO", "DEL", "EL",
			"LAS", "LOS", "LO", "LA", "DE", "DAS", "DI", "DOS", "DU", "DO", "DA", "CON");
	public static List<String> USER_BLACKLIST;

	@Override
	public String getUserName(Map<String, Object> reqData) throws UserNameGenerationException {

		logger.entering(className, "getUserName");

		logger.fine("Getting the values of fields");
		String firstName = reqData.get(ATTR_FIRSTNAME_ID) == null ? null : reqData.get(ATTR_FIRSTNAME_ID).toString();
		String secondName = reqData.get(ATTR_MIDDLENAME_ID) == null ? null : reqData.get(ATTR_MIDDLENAME_ID).toString();
		String lastName = reqData.get(ATTR_LASTNAME_ID) == null ? null : reqData.get(ATTR_LASTNAME_ID).toString();
		String surname = reqData.get(ATTR_SURNAME_ID) == null ? null : reqData.get(ATTR_SURNAME_ID).toString();
		String ou = reqData.get(ATTR_USER_ORG_ID) == null ? null : reqData.get(ATTR_USER_ORG_ID).toString();
		String origin = reqData.get(ATTR_ORIGIN_ID) == null ? null : reqData.get(ATTR_ORIGIN_ID).toString();
		String rut = reqData.get(ATTR_RUT_ID) == null ? null : reqData.get(ATTR_RUT_ID).toString();

		logger.finest("Values of the requets: " + "	firstName: " + firstName + " | secondName:  " + secondName
				+ " | lastName:  " + lastName + " | surname:  " + surname + " | ou:  " + ou + " | origin:  " + origin
				+ " | rut:  " + rut);

		logger.fine("Calling the procedure to create the custom userlogin");
		String userLogin;
		try {
			userLogin = getCustomUsername(firstName, secondName, lastName, surname, rut, ou, origin);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected Error - getUserName", e);
			e.printStackTrace();
			throw new UserNameGenerationException(e, "Unexpected Error - getUserName");
		}

		logger.exiting(className, "getUserName", userLogin);
		return userLogin;

	}

	@Override
	public boolean isGivenUserNameValid(String userName, Map<String, Object> reqData) {

		logger.entering(className, "isGivenUserNameValid", userName);
		boolean valid = true;

		if (userName == null || "-1".equals(userName))
			valid = false;

		logger.exiting(className, "isGivenUserNameValid", valid);
		return valid;

	}

	@Override
	public String getDescription(Locale locale) {
		return "User Name Generation Policy using custom Re rules";
	}

	/**
	 * Construct the custom username
	 * 
	 * @param firstName
	 *            First Name of the user to create
	 * @param secondName
	 *            Second Name of the user to create
	 * @param lastName
	 *            Last Name of the user to create
	 * @param surname
	 *            Sur Name of the user to create
	 * @param rut
	 *            RUT of the user to create
	 * @param ou
	 *            Organization of the user to create
	 * @param origin
	 *            Origin of the user to create
	 * @return The username to use
	 * @throws Exception
	 */
	public static String getCustomUsername(String firstName, String secondName, String lastName, String surname,
			String rut, String ou, String origin) throws Exception {

		logger.entering(className, "getCustomUsername");

		Properties properties = new Properties();
		String migrationOIMPS2;
		String userLogin = null;

		try {
			properties.load(
					new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
			migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");

			logger.fine("Checking if migration OIM PS2 flag is true");
			if (Boolean.parseBoolean(migrationOIMPS2) && origin != null
					&& origin.equalsIgnoreCase(SUCCESSFACTORS_ENTEL)) {
				logger.fine("Checking the userlogin of the user if exists in OIM PS2 ");
				userLogin = getUserLoginFromOIMPS2(rut);
				logger.finest("User login found in OIM PS2:" + userLogin);

				if (userLogin != null && "-1".equals(userLogin)) {
					logger.severe("User login cannot be empty");
					logger.exiting(className, "getCustomUsername", userLogin);
					return null;
				}

			}

			logger.fine("Checking if we must continue with the user login generation");
			if (userLogin == null) {

				logger.fine("Getting the Organization fieds");
				Organization org = findOrganizationById(ou);
				String prefix = (String) org.getAttribute(ATTR_ORG_PREFIX_ID);
				logger.finest("Organization found: " + org.getAttribute(ATTR_ORG_NAME_ID) + " | Prefix: " + prefix);

				logger.fine("Getting the extension of user login");
				String extension = "";
				if (isExternOrg(org.getEntityId())) {
					if (prefix != null && !("".equals(prefix)))
						extension = prefix + "_";
					else
						extension = "E_";
				}
				logger.finest("Extension: " + extension);

				logger.fine("Call the generation of the user login");
				userLogin = createUserLogin(firstName, secondName, lastName, surname, extension, true);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected Error - getCustomUsername", e);
			e.printStackTrace();
			throw e;
		}

		logger.finest("User Login generated: " + userLogin);
		logger.exiting(className, "getCustomUsername", userLogin);
		return userLogin;

	}

	/**
	 * Creates the user login
	 * 
	 * @param firstName
	 *            First name of the user
	 * @param secondName
	 *            Second name of the user
	 * @param lastName
	 *            Last name of the user
	 * @param surname
	 *            Surname of the user
	 * @param extension
	 *            Organization extension
	 * @param indRemoveReservedWords
	 *            Flag to determinate if remove reserved words
	 * @return User login of the user
	 * @throws Exception
	 */
	public static String createUserLogin(String firstName, String secondName, String lastName, String surname,
			String extension, boolean indRemoveReservedWords) throws Exception {

		logger.entering(className, "createUserLogin");
		String userLogin = null;

		try {
			logger.fine("Remove special characters from attributes");
			firstName = removeSpecialCharacters(firstName);
			secondName = removeSpecialCharacters(secondName);
			lastName = removeSpecialCharacters(lastName);
			surname = removeSpecialCharacters(surname);
			logger.finest("Values of the user: " + "	firstName: " + firstName + " | secondName:  " + secondName
					+ " | lastName:  " + lastName + " | surname:  " + surname);

			logger.fine("Remove reserved words");
			if (indRemoveReservedWords) {
				firstName = removeReservedWords(firstName);
				secondName = removeReservedWords(secondName);
				lastName = removeReservedWords(lastName);
				surname = removeReservedWords(surname);
			}
			logger.finest("Values of the user: " + "	firstName: " + firstName + " | secondName:  " + secondName
					+ " | lastName:  " + lastName + " | surname:  " + surname);

			logger.fine("Choosing the words in case of multiples exists");
			firstName = getFirstOrLastWord(firstName, false);
			secondName = getFirstOrLastWord(secondName, false);
			lastName = getFirstOrLastWord(lastName, false);
			surname = getFirstOrLastWord(surname, true);
			logger.finest("Values of the user: " + "	firstName: " + firstName + " | secondName:  " + secondName
					+ " | lastName:  " + lastName + " | surname:  " + surname);

			
			logger.fine("Starting blackList");
			/**
			 * Updated by @oracle. Consult blacklist.
			 */

			String lookupName = "Lookup.Users.UserBlackList";
			try {

				USER_BLACKLIST = new ArrayList<String>();
				logger.fine("Getting lookup values from  " + lookupName);
				tcResultSet rs = lookupOps.getLookupValues(lookupName);

				logger.finer("Looping over users login to updates");
				for (int i = 0; i < rs.getRowCount(); i++) {
					rs.goToRow(i);
					String code = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
					String decode = rs.getStringValue("Lookup Definition.Lookup Code Information.Decode");
					logger.finer("Code: " + code + " | Decode: " + decode);
					USER_BLACKLIST.add(decode);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "updateUserAttributeSuccessFactorsUsername - Unexpected error", e);
			}
			
			logger.fine("USER_BLACKLIST contains "+USER_BLACKLIST.size()+" user logins.");
			
			
			logger.fine("Calling the generation of the ID");
			/**
			 * Updated by @oracle. Solicitude by Entel (01-04-2019) and start with the
			 * 4th attempt New attempts were added...
			 */

			int attempts = 1;
			//int attempts = 4;
			while (true) {

				logger.fine("Checking if userlogin \"" + userLogin + "\" exists");
				if (userLogin != null && !existsUser(userLogin)) {

					logger.fine("UserLogin \"" + userLogin + "\" not exist in OIM");
					break;

				} else {

					logger.fine("Constructing userLogin with attempt: " + attempts);
					userLogin = constructUserLogin(attempts, firstName, secondName, lastName, surname, extension);
					logger.fine("Constructed userLogin: " + userLogin);

					attempts++;

					logger.fine("Checking max attempts");
					if ("-1".equals(userLogin)) {
						logger.severe("Cannot construct userLogin. Reached max attempts");
						userLogin = null;
						break;
					}

				}

			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error - createUserLogin", e);
			e.printStackTrace();
			throw e;
		}

		logger.exiting(className, "createUserLogin");
		return userLogin;
	}

	/**
	 * Cut the lastname if it excedes the MAX
	 * 
	 * @param lastName
	 *            Last name of the user
	 * @param extraCharacters
	 *            Characters in the user login tahat not belongs to the lastname
	 * @return Lastname with the correct size
	 */
	public static String getLastName(String lastName, int extraCharacters) {

		logger.entering(className, "getLastName", lastName);

		int maxLastNameSize = MAX_CHARACTERS - extraCharacters;
		if (lastName.length() > maxLastNameSize) {
			lastName = lastName.substring(0, maxLastNameSize);
		}

		logger.exiting(className, "getLastName", lastName);
		return lastName;
	}

	/**
	 * Construct the user login of the user using predefined rules
	 * 
	 * @param attempts
	 *            Attempts to constructs the user login of the user
	 * @param firstName
	 *            First Name of the user
	 * @param secondName
	 *            Second Name of the user
	 * @param lastName
	 *            Last Name of the user
	 * @param surname
	 *            SurName of the user
	 * @param extension
	 *            Extension of the user login
	 * @return The user login of the user
	 */
	public static String constructUserLogin(int attempts, String firstName, String secondName, String lastName,
			String surname, String extension) {

		logger.entering(className, "constructUserLogin");
		String userLogin = null;

		if (attempts == 1) {

			logger.finest("First character of firstname + lastname");
			userLogin = firstName.substring(0, 1);
			lastName = getLastName(lastName, extension.length() + 1);
			userLogin = userLogin.concat(lastName);

		} else if (attempts == 2 && surname != null) {

			logger.finest("First character of firstname + lastname + first character of surname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				lastName = getLastName(lastName, extension.length() + 2); // char of firstname + char of surname
				userLogin = userLogin.concat(lastName);
				userLogin = userLogin.concat(surname.substring(0, 1));
			}

		} else if (attempts == 3 && secondName != null) {

			logger.finest("First character of firstname + first character of secondName + lastname");
			if (!secondName.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				userLogin = userLogin.concat(secondName.substring(0, 1));
				lastName = getLastName(lastName, extension.length() + 2); // char of firstName + char of secondName
				userLogin = userLogin.concat(lastName);
			}
		} else if (attempts == 4 && secondName != null && surname != null) {
			logger.finest(
					"First character of firstname + first character of secondName + lastname + first character of surname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				userLogin = userLogin.concat(secondName.substring(0, 1));
				lastName = getLastName(lastName, extension.length() + 3); // char of firstName + char of secondName +
																			// char of surname
				userLogin = userLogin.concat(lastName);
				userLogin = userLogin.concat(surname.substring(0, 1));
			}
		} else if (attempts == 5 && secondName != null && surname != null) {
			logger.finest(
					"First character of firstname + first character of secondName + first character of surname + lastname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				userLogin = userLogin.concat(secondName.substring(0, 1));
				userLogin = userLogin.concat(surname.substring(0, 1));
				lastName = getLastName(lastName, extension.length() + 3); // char of firstName + char of secondName +
																			// char of surname
				userLogin = userLogin.concat(lastName);
			}
		}

		else if (attempts == 6 && surname != null) {
			logger.finest("First character of firstname + first character of surname + lastname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				userLogin = userLogin.concat(surname.substring(0, 1));
				lastName = getLastName(lastName, extension.length() + 2); // char of firstName + char of surname
				userLogin = userLogin.concat(lastName);

			}
		}

		else if (attempts == 7 && secondName != null && surname != null) {
			logger.finest(
					"First character of firstname + first character of surname + lastname  + first character of secondName ");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 1);
				userLogin = userLogin.concat(surname.substring(0, 1));
				lastName = getLastName(lastName, extension.length() + 3); // char of firstName + char of surname + char
																			// of secondName
				userLogin = userLogin.concat(lastName);
				userLogin = userLogin.concat(secondName.substring(0, 1));

			}
		} else if(attempts > 7) {
		  
		  
		  String errorMsg
		  ="Cannot create userlogin with all the rules defined. All options were used."
		  ; logger.severe(errorMsg); return "-1"; }
		 

		/*
		else if (attempts == 8) {
			logger.finest("First two characters of firstname + dot + lastname");
			userLogin = firstName.substring(0, 2);
			lastName = getLastName(lastName, extension.length() + 3); // 2 char of firstName + .
			userLogin = userLogin.concat(".").concat(lastName);
		} else if (attempts == 9 && surname != null) {
			logger.finest("First two characters of firstname + dot + lastname + first character of surname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 4); // 2 char of firstName + . + char of surname
				userLogin = userLogin.concat(".").concat(lastName).concat(surname.substring(0, 1));
			}
		} else if (attempts == 10 && secondName != null) {
			logger.finest("First two characters of firstname + dot + first character of middle name +lastname");
			if (!secondName.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 4); // 2 char of firstName + . + char of middle
																			// name
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(lastName);
			}
		} else if (attempts == 11 && secondName != null && surname != null) {
			logger.finest(
					"First two characters of firstname + dot + first character of middle name +lastname + first character of surname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 5); // 2 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(lastName)
						.concat(surname.substring(0, 1));
			}
		} else if (attempts == 12 && secondName != null && surname != null) {
			logger.finest(
					"First two characters of firstname + dot + first character of middle name + first character of surname + lastname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 5); // 2 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(surname.substring(0, 1))
						.concat(lastName);
			}
		} else if (attempts == 13 && surname != null) {
			logger.finest("First two characters of firstname + dot + first character of surname + lastname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 4); // 2 char of firstName + . + char of surname
				userLogin = userLogin.concat(".").concat(surname.substring(0, 1)).concat(lastName);
			}
		} else if (attempts == 14 && secondName != null && surname != null) {
			logger.finest(
					"First two characters of firstname + dot + first character of surname + lastname + first character of middle name");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 2);
				lastName = getLastName(lastName, extension.length() + 5); // 2 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(surname.substring(0, 1)).concat(lastName)
						.concat(secondName.substring(0, 1));
			}
		} else if (attempts == 15) {
			logger.finest("First four characters of firstname + dot + lastname");
			userLogin = firstName.substring(0, 4);
			lastName = getLastName(lastName, extension.length() + 5); // 4 char of firstName + .
			userLogin = userLogin.concat(".").concat(lastName);
		} else if (attempts == 16 && surname != null) {
			logger.finest("First four characters of firstname + dot + lastname + first character of surname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 6); // 4 char of firstName + . + char of surname
				userLogin = userLogin.concat(".").concat(lastName).concat(surname.substring(0, 1));
			}
		} else if (attempts == 17 && secondName != null) {
			logger.finest("First four characters of firstname + dot + first character of middle name +lastname");
			if (!secondName.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 6); // 4 char of firstName + . + char of middle
																			// name
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(lastName);
			}
		} else if (attempts == 18 && secondName != null && surname != null) {
			logger.finest(
					"First four characters of firstname + dot + first character of middle name +lastname + first character of surname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 7); // 4 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(lastName)
						.concat(surname.substring(0, 1));
			}
		} else if (attempts == 19 && secondName != null && surname != null) {
			logger.finest(
					"First four characters of firstname + dot + first character of middle name + first character of surname + lastname");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 7); // 4 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(secondName.substring(0, 1)).concat(surname.substring(0, 1))
						.concat(lastName);
			}
		} else if (attempts == 20 && surname != null) {
			logger.finest("First four characters of firstname + dot + first character of surname + lastname");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 6); // 4 char of firstName + . + char of surname
				userLogin = userLogin.concat(".").concat(surname.substring(0, 1)).concat(lastName);
			}
		} else if (attempts == 21 && secondName != null && surname != null) {
			logger.finest(
					"First four characters of firstname + dot + first character of surname + lastname + first character of middle name");
			if (!secondName.isEmpty() && !surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				lastName = getLastName(lastName, extension.length() + 7); // 4 char of firstName + . + char of middle
																			// name + char of surname
				userLogin = userLogin.concat(".").concat(surname.substring(0, 1)).concat(lastName)
						.concat(secondName.substring(0, 1));
			}
		} else if (attempts > 21 && surname != null) {
			logger.finest(
					"First four characters of firstname + dot + first character of surname + lastname + sequential number");
			if (!surname.isEmpty()) {
				userLogin = firstName.substring(0, 4);
				String seqN = String.valueOf(attempts - 21);
				lastName = getLastName(lastName, extension.length() + 6 + seqN.length()); // 4 char of firstName + . +
																							// char of surname +
																							// sequential number
				userLogin = userLogin.concat(".").concat(surname.substring(0, 1)).concat(lastName).concat(seqN);
			}
		} else {

			String errorMsg = "Cannot create userlogin with all the rules defined. All options were used.";
			logger.severe(errorMsg);
			return "-1";
		}
		*/

		if (userLogin != null) {

			logger.finest("Putting the extension \"" + extension + "\"in the user login: " + userLogin);
			userLogin = extension + userLogin;

			logger.finest("Putting UpperCase the user login: " + userLogin);
			userLogin = userLogin.toUpperCase();
		}
		
		if(USER_BLACKLIST.contains(userLogin)) {
			logger.finest("User Login is contained in Black List");
			userLogin=null;
		}

		logger.exiting(className, "constructUserLogin", userLogin);
		return userLogin;
	}

	/**
	 * Check if the user login exists in OIM
	 * 
	 * @param userLogin
	 *            User login to check
	 * @return True if the user login exist in OIM. False if the user login doesn't
	 *         exist in OIM
	 */
	public static boolean existsUser(String userLogin) throws Exception {

		logger.entering(className, "existsUser", userLogin);
		boolean exist = false;
		PreparedStatement stmt = null;
		Connection oimdb = null;
		try {

			logger.fine("Getting OIM Connection");
			oimdb = Platform.getOperationalDS().getConnection();

			logger.fine("Constructing SQL");
			String sql = "select 1 from usr where UPPER(usr_login) = UPPER('" + userLogin + "')";
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
			logger.log(Level.SEVERE, "Unexpected error - existsUser", e);
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

		logger.exiting(className, "existsUser", exist);
		return exist;

	}

	/**
	 * Returns the organization in OIM
	 * 
	 * @param organizationId
	 *            Organization id to find
	 * @return Get the Organization object in OIM
	 */
	public static Organization findOrganizationById(String organizationId) {

		logger.entering(className, "findOrganizationById", organizationId);
		Organization org = null;

		logger.fine("Setting the search criteria of the organization id: " + organizationId);
		SearchCriteria criteria = new SearchCriteria("act_key", organizationId, SearchCriteria.Operator.EQUAL);

		logger.fine("Setting attributes to return");
		Set<String> retAttrs = new HashSet<String>();
		retAttrs.add(ATTR_ORG_PREFIX_ID);
		retAttrs.add(ATTR_ORG_NAME_ID);

		try {

			logger.fine("Calling the OrganizationManager service");
			OrganizationManager orgmgr = Platform.getService(OrganizationManager.class);
			List<Organization> orgs = orgmgr.search(criteria, retAttrs, null);
			if (!orgs.isEmpty())
				org = orgs.get(0);
		} catch (Exception e) {

			logger.severe("findOrganization: Error calling OrganizationManager service: " + e.getMessage());
			e.printStackTrace();
		}

		logger.exiting(className, "findOrganizationById");
		return org;
	}

	/**
	 * Remove specials characters from a string
	 * 
	 * @param words
	 *            String to format
	 * @return Words without specials characters
	 */
	public static String removeSpecialCharacters(String words) {

		logger.entering(className, "removeSpecialCharacters", words);

		if (words != null) {
			words = words.replaceAll("[á,ä,à]", "a");
			words = words.replaceAll("[Á,Ä,À]", "A");
			words = words.replaceAll("[é,ë,è]", "e");
			words = words.replaceAll("[É,Ë,È]", "E");
			words = words.replaceAll("[í,ï,ì]", "i");
			words = words.replaceAll("[Í,Ï,Ì]", "I");
			words = words.replaceAll("[ó,ö,ò]", "o");
			words = words.replaceAll("[Ó,Ö,Ò]", "O");
			words = words.replaceAll("[ú,ü,ù]", "u");
			words = words.replaceAll("[Ú,Ü,Ù]", "U");
			words = words.replaceAll("[ñ]", "n");
			words = words.replaceAll("[Ñ]", "N");
			words = words.replaceAll("[^a-zA-Z]+", "");
		}

		logger.exiting(className, "removeSpecialCharacters", words);
		return words;
	}

	/**
	 * Get a part of a string split by spaces
	 * 
	 * @param words
	 *            String to split
	 * @param lastOcurrence
	 *            Flag to return the first or the last occurrence
	 * @return Get the first/last occurrence part of a string
	 */
	public static String getFirstOrLastWord(String words, boolean lastOcurrence) {

		logger.entering(className, "getFirstOrLastWord", words);
		if (words != null && !words.isEmpty()) {
			words = words.trim();

			logger.finest("Checking if the string contains spaces");
			if (words.indexOf(" ") != -1) {
				if (!lastOcurrence) {
					logger.finest("Getting the first ocurrence");
					words = words.substring(0, words.indexOf(' '));
				} else
					logger.finest("Getting the last ocurrence");
				words = words.substring(words.lastIndexOf(' ') + 1);
			}
		}

		logger.exiting(className, "getFirstOrLastWord", words);
		return words;
	}

	/**
	 * Remove reserved words from a string
	 * 
	 * @param words
	 *            String to check
	 * @return Transformed string
	 */
	public static String removeReservedWords(String words) {

		logger.entering(className, "removeReservedWords", words);

		if (words != null) {
			for (int i = 0; i < RESERVED_WORDS_LIST.size(); i++) {

				String reservedWord = RESERVED_WORDS_LIST.get(i);

				if (words.startsWith(reservedWord + " ")) {
					words = words.replace(reservedWord + " ", "");
				}
				if (words.contains(" " + reservedWord + " ")) {
					words = words.replace(" " + reservedWord + " ", "");
				}
				if (words.endsWith(" " + reservedWord)) {
					words = words.replace(reservedWord + " ", "");
				}

			}
		}

		logger.exiting(className, "removeReservedWords", words);
		return words;
	}

	public static String getUserLoginFromOIMPS2(String rut) {

		logger.entering(className, "getUserLoginFromOIMPS2", rut);
		String userLogin = null;

		try {

			Properties properties = new Properties();
			String jdbcURLDatabaseOIMPS2;
			String userDatabaseOIMPS2;
			String passDatabaseOIMPS2;
			String ownerSchemeOIMPS2;

			try {

				properties.load(new FileInputStream(
						systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
				jdbcURLDatabaseOIMPS2 = properties.getProperty("entel.oim.migration.jdbcurl.ps2.bd");
				userDatabaseOIMPS2 = properties.getProperty("entel.oim.migration.user.ps2.bd");
				passDatabaseOIMPS2 = properties.getProperty("entel.oim.migration.pass.ps2.bd");
				ownerSchemeOIMPS2 = properties.getProperty("entel.oim.migration.owner.ps2.bd");

			} catch (IOException e) {
				logger.severe("Error trying to open the properties file.");
				throw new RuntimeException(e);
			} catch (SystemConfigurationServiceException e) {
				logger.severe("Error trying to call system property service.");
				throw new RuntimeException(e);
			}

			logger.fine("Looking for driver for Oracle Database");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.severe("Oracle JDBC Driver not found");
				throw new RuntimeException(e);
			}
			logger.fine("Oracle JDBC Driver Registered!");

			logger.fine("Trying connection!");
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(jdbcURLDatabaseOIMPS2, userDatabaseOIMPS2, passDatabaseOIMPS2);
			} catch (SQLException e) {
				logger.severe("Connection Failed! Check output console: " + e.getMessage());
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			if (connection != null) {
				logger.fine("Connection success. Preparing query");

				Statement stmtSelect = null;
				rut = rut.replace("-", "").replace(".", "").trim().replaceFirst("^0+(?!$)", "").toUpperCase();
				String query = "select distinct us.usr_login usr_login " + " from " + ownerSchemeOIMPS2 + ".usr us "
						+ " join " + ownerSchemeOIMPS2 + ".act ac on ac.act_key = us.act_key "
						+ " where us.usr_status = 'Active' and ac.act_name IN  ('Empleados') "
						+ " and upper(ltrim(trim(replace(replace(us.usr_udf_rut,'.',''),'-','')),'0'))='" + rut + "'";
				logger.finest("Query to execute: " + query);
				try {

					logger.fine("Executing query...");
					stmtSelect = connection.createStatement();
					ResultSet rs = stmtSelect.executeQuery(query);

					if (rs.next()) {
						userLogin = rs.getString("USR_LOGIN");
						logger.finest("User Login found: " + userLogin);

					} else {
						logger.finest("User Login NOT found");
					}

				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} finally {
					if (stmtSelect != null) {
						stmtSelect.close();
						connection.close();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Unexpected exception: " + e.getMessage());
			userLogin = "-1";
		}

		logger.exiting(className, "getUserLoginFromOIMPS2", userLogin);
		return userLogin;

	}

	/**
	 * Get the keys of the organization of type Extern
	 * 
	 * @return External Organization list key
	 * @throws Exception
	 */
	private static boolean isExternOrg(String orgKey) {

		logger.entering(className, "isExternOrg", orgKey);
		boolean isExtern = false;

		try {

			logger.finer("Setting the attributes to find of the organization");
			Set<String> searchAttrs = new HashSet<String>();
			searchAttrs.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());

			logger.finer("Getting Organization Key of Extern");
			Organization orgExtern = orgMgr.getDetails("Externos", searchAttrs, true);

			logger.finer("Getting Organizations Keys Childs for Extern");
			HashMap<String, Object> configParams = new HashMap<String, Object>();
			List<Organization> childOrgExtern = orgMgr.getChildOrganizations(orgExtern.getEntityId(), searchAttrs,
					configParams);

			logger.finer("Looping over the organization list");
			for (Organization org : childOrgExtern) {
				logger.finer("Organization found: Key -> " + org.getEntityId() + " | Name -> "
						+ org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
				if (org.getEntityId().equals(orgKey)) {
					logger.finest("Organization Extern Found");
					isExtern = true;
					break;

				}
			}

		} catch (OrganizationManagerException | AccessDeniedException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "isExternOrg - Unexpected error", e);
		}

		logger.exiting(className, "isExternOrg", isExtern);
		return isExtern;
	}

}