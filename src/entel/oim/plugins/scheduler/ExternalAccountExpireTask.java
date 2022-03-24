package entel.oim.plugins.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.scheduler.vo.TaskSupport;

public class ExternalAccountExpireTask extends TaskSupport {

	private final static String className = ExternalAccountExpireTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	// Services
	private final NotificationService nsrv = Platform.getService(NotificationService.class);

	@Override
	public void execute(HashMap hm) throws Exception {
		logger.entering(className, "execute", hm);
		int days = (int) hm.get("Expiration Days");

		try {

			logger.log(Level.FINER, "Getting accounts that will expire in " + days + " days.");
			HashMap<String, StringBuilder> oimAccounts = findOIMAccounts(days);

			// Send mail of users with absence
			logger.fine("Calling the procedure to notify the managers with accounts about to expire.");
			notifyGestores(oimAccounts);

		} catch (SQLException e) {

			logger.log(Level.SEVERE, "SQLException error", e);

		} catch (Exception e) {

			logger.log(Level.SEVERE, "Unknown Exception error", e);

		} finally {

			logger.exiting(className, "execute");

		}

	}

	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {
	}

	/**
	 * HashMap of Accounts that can expire in n days
	 * 
	 * @param days
	 *            Days before the end date of the user's account
	 * @return Set of HashMap with the information of user's accounts to expire.
	 *         Key: Gestor, Object: Display Name, Login, RUT, End Date
	 * @throws SQLException
	 */
	private HashMap<String, StringBuilder> findOIMAccounts(int days) throws SQLException {

		logger.entering(className, "findOIMAccounts");
		HashMap<String, StringBuilder> accountsToExpire = new HashMap<String, StringBuilder>();
		PreparedStatement stmt = null;
		Connection oimdb = null;
		try {

			logger.fine("Getting OIM Connection");
			oimdb = Platform.getOperationalDS().getConnection();
			String sql = "select usr_udf_gestor, usr_display_name, usr_login, usr_udf_rut, usr_end_date from usr "
					+ "where usr_udf_origin not in ('SSFF-TRUSTED') and trunc(usr_end_date) = trunc(sysdate + " + days
					+ ") order by usr_udf_gestor,usr_login";

			logger.finest("SQL to execute: " + sql);

			logger.finer("Executing query");
			stmt = oimdb.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (!rs.isBeforeFirst()) {
				logger.finer("No accounts found!");
			} else {

				while (rs.next()) {
					String gestor = rs.getString("usr_udf_gestor");
					String display_name = rs.getString("usr_display_name");
					String login = rs.getString("usr_login");
					String rut = rs.getString("usr_udf_rut");
					Date end_date = rs.getDate("usr_end_date");
					logger.finest("Adding account: " + login + "to manager: " + gestor);

					if (accountsToExpire.containsKey(gestor)) {

						StringBuilder html = accountsToExpire.get(gestor);
						html.append("<tr><td><center>").append(display_name).append("</center></td>");
						html.append("<td><center>").append(login).append("</center></td>");
						html.append("<td><center>").append(rut).append("</center></td>");
						html.append("<td><center>").append(end_date.toString()).append("</center></td></tr>");
						accountsToExpire.put(gestor, html);

					} else {

						StringBuilder html = new StringBuilder("<tr><td><center>" + display_name + "</center></td>");
						html.append("<td><center>").append(login).append("</center></td>");
						html.append("<td><center>").append(rut).append("</center></td>");
						html.append("<td><center>").append(end_date.toString()).append("</center></td></tr>");
						accountsToExpire.put(gestor, html);

					}

				}

			}

			logger.finest("Closing result HashMap...");
			rs.close();

		} finally {
			if (stmt != null) {
				logger.finest("Closing Prepared Statement...");
				stmt.close();
			}
			if (oimdb != null ) {
				logger.finer("Closing database connection!");
			    oimdb.close();
			}
		}

		logger.exiting(className, "findOIMAccouts", accountsToExpire.size());
		return accountsToExpire;
	}

	/**
	 * Method to send notifications with the accounts about to expire
	 * 
	 * @param oimAccounts
	 */
	private void notifyGestores(HashMap<String, StringBuilder> oimAccounts) {
		logger.entering(className, "notifyGestores");

		for (Entry<String, StringBuilder> entry : oimAccounts.entrySet()) {

			logger.fine("Constructing notification for manager: '" + entry.getKey() + "'.");
			// Construction of template parameters
			HashMap<String, Object> templateParams = new HashMap<String, Object>();
			templateParams.put("info_html", entry.getValue().toString());

			// Getting addressee login
			String[] addressee = { entry.getKey().toString() };

			sendEmailNotification("ExternalAccountExpire Alert", templateParams, addressee);
		}
		logger.exiting(className, "notifyGestores");
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
			// Setting parameters to replace in the template
			ev.setParams(templateParams);

			logger.fine("Sending subordinated manager notification [ExternalAccountExpire Alert] to reviewers "
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
}
