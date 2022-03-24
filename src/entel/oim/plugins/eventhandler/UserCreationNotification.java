package entel.oim.plugins.eventhandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;

public class UserCreationNotification implements PostProcessHandler, ConditionalEventHandler {

	private final static String className = UserCreationNotification.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private final NotificationService nsrv = Platform.getService(NotificationService.class);
	private final RoleManager rmgr = Platform.getService(RoleManager.class);

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

		logger.entering(className, "EventResult");

		if (orchestration.getOperation().equals("CREATE")) {
			try {

				logger.info("ProcessId: " + processId + " - EventId: " + eventId + "- Operacion: CREATE");

				HashMap<String, Serializable> parameters = orchestration.getParameters();

				logger.fine("Getting new user information...");

				String fullName = "";
				String firstName = "";
				String userLogin = "";

				if (parameters.containsKey("Full Name")) {
					fullName = parameters.get("Full Name").toString();
				}

				if (parameters.containsKey("First Name")) {
					firstName = parameters.get("First Name").toString();
				}

				if (parameters.containsKey("User Login")) {
					userLogin = parameters.get("User Login").toString();
				}

				HashMap<String, Object> templateParams = new HashMap<String, Object>();
				templateParams.put("fullName", fullName);
				templateParams.put("firstName", firstName);
				templateParams.put("userLogin", userLogin);

				logger.fine("Getting list of adressees.");
				String[] adressee = usersLogins("Recursos Humanos");
				
				sendEmailNotification("UserCreationNotification Alert", templateParams, adressee);

			} catch (Exception e) {
				logger.info("Exeception during EventResult - Execute: UserCreationNotification: " + e.getMessage());
				System.out.println(
						"Exeception during EventResult - Execute: UserCreationNotification: " + e.getMessage());
				e.printStackTrace();
			}

		}

		logger.exiting(className, "ExecuteEvent");
		System.out.println("Exiting EventResult - UserCreationNotification");
		return null;//Async

	}

	@Override
	public boolean isApplicable(AbstractGenericOrchestration orchestration) { // Request Context

		logger.entering(className, "isApplicable");
		boolean isValid = false;
		Properties properties = new Properties();
		String migrationOIMPS2;

		try {

			logger.fine("Checking if it is CREATE operation");
			if (orchestration.getOperation().equals("CREATE")) {
				HashMap<String, Serializable> parameters = orchestration.getParameters();

				String origin = getStringParamaterValue(parameters, "Origin");
				String society = getStringParamaterValue(parameters, "Society");
				logger.finest("Origin: " + origin);

//				logger.info("Checking if Migration of OIM is active");
//				properties.load(new FileInputStream(
//						systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
//				migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");

				logger.info("Checking if creation is Employee and we are not in Migration process");
				if(origin != null) {
					if(origin.equalsIgnoreCase("SSFF-TRUSTED")) {
						if(society.equals("ECC")) {
							isValid = true;
						}
					} else {
						isValid = true;
					}
				}
				
//				if (origin != null && !origin.equalsIgnoreCase("SSFF-TRUSTED")) {
//					isValid = true;
//				} else if(origin != null && origin.equalsIgnoreCase("SSFF-TRUSTED") && society.equals("ECC")){
//					isValid = true;
//				}
			}

//		} catch (AccessDeniedException | IOException | SystemConfigurationServiceException e) {
		} catch (AccessDeniedException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "isApplicable - Unexpected error", e);

		}

		logger.exiting(className, "isApplicable", isValid);
		return isValid;

	}

	/**
	 * Get the string value of an attribute of the user created
	 * 
	 * @param parameters
	 *            Map of attributes
	 * @param key
	 *            Key of the attribute to find
	 * @return String value of the attribute
	 */
	private String getStringParamaterValue(HashMap<String, Serializable> parameters, String key) {

		logger.entering(className, "getStringParamaterValue", key);
		String value = null;

		logger.fine("Checking if parameters contains the value wanted");
		if (parameters.containsKey(key)) {
			value = (parameters.get(key) instanceof ContextAware)
					? (String) ((ContextAware) parameters.get(key)).getObjectValue()
					: (String) parameters.get(key);
		}

		logger.exiting(className, "getStringParamaterValue", value);
		return value;
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

			logger.fine("Sending subordinated manager notification [CheckInfoExtern Alert] to reviewers "
					+ ev.getUserIds().toString());

			nsrv.notify(ev);

		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}
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
