package entel.oim.adapters;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;

public class Exchange {

	private final static String className = Exchange.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);
	private final NotificationService nsrv = Platform.getService(NotificationService.class);
	
	public Exchange() {
		super();
	}

	/**
	 * Execute the update of the Email in SuccessFactors
	 * 
	 * @param rut
	 *            The user to be updated
	 * @param email
	 *            The email to be updated
	 * @throws Exception
	 */
	public void updateSuccessFactorsEmail(String rut, String email) throws Exception {

		logger.entering(className, "updateSuccessFactorsEmail", new Object[] { rut, email });
		try {

			logger.finer("Adding the RUT "+ rut + " and Email " +email+ " to the pending lookup");
			lookupOprInf.addLookupValue("Lookup.Users.PendingUserEmail",rut,email,"","");

			logger.exiting(className, "updateSuccessFactorsEmail");

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateSuccessFactorsEmail - Unexpected error", e);
			throw e;

		}

	}
	
	
	
	/**
	 * Execute the update of the Email in SuccessFactors
	 * 
	 * @param rut
	 *            The user to be updated
	 * @param email
	 *            The email to be updated
	 * @throws Exception
	 */
	public void updateSuccessFactorsEmailOrigin(String rut, String email,String origin) throws Exception {

		logger.entering(className, "updateSuccessFactorsEmailOrigin", new Object[] { rut, email, origin });
		Properties properties = new Properties();
		String migrationOIMPS2;
		try {

			logger.info("Checking if Migration of OIM is active");
			properties.load(new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
			migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");
			
			logger.info("Checking if creation is Employee and we are not in Migration process");
			if (origin != null && origin.equalsIgnoreCase("SSFF-TRUSTED") && !Boolean.parseBoolean(migrationOIMPS2)) {
				logger.finer("Adding the RUT "+ rut + " and Email " +email+ " to the pending lookup");
				lookupOprInf.addLookupValue("Lookup.Users.PendingUserEmail",rut,email,"","");
			}
			
			logger.exiting(className, "updateSuccessFactorsEmailOrigin");

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateSuccessFactorsEmailOrigin - Unexpected error", e);
			throw e;

		}

	}

	
	
	/**
	 * Execute the update of the Email and User in SuccessFactors
	 * 
	 * @param rut
	 *            The user to be updated
	 * @param email
	 *            The email to be updated
	 * @throws Exception
	 */
	public void updateSuccessFactorsUserEmailOrigin(String rut, String email,String origin, String usrLogin) throws Exception {

		logger.entering(className, "updateSuccessFactorsEmailOrigin", new Object[] { rut, email, origin });
		Properties properties = new Properties();
		String migrationOIMPS2;
		try {

			logger.info("Checking if Migration of OIM is active");
			properties.load(new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue()));
			migrationOIMPS2 = properties.getProperty("entel.oim.migration.userlogin.ps2");
			
			logger.info("Checking if creation is Employee and we are not in Migration process");
			if (origin != null && origin.equalsIgnoreCase("SSFF-TRUSTED") && !Boolean.parseBoolean(migrationOIMPS2)) {
				logger.finer("Adding the RUT "+ rut + " and Email " +email+ " to the pending lookup");
				lookupOprInf.addLookupValue("Lookup.Users.PendingUserEmail",rut,email,"","");
				lookupOprInf.addLookupValue("Lookup.Users.PendingUserLogin",rut,usrLogin,"","");
			}
			
			logger.exiting(className, "updateSuccessFactorsEmailOrigin");

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateSuccessFactorsEmailOrigin - Unexpected error", e);
			throw e;

		}

	}

	

	/**
	 * Create Primary SMTP Address
	 * @param userLogin
	 * @param domain
	 * @return
	 * @throws NotificationException 
	 * @throws NotificationResolverNotFoundException 
	 * @throws MultipleTemplateException 
	 * @throws TemplateNotFoundException 
	 * @throws UnresolvedNotificationDataException 
	 * @throws EventException 
	 * @throws UserDetailsNotFoundException 
	 */
	public String toCreatePrimarySMTPAddress(String userLogin, String domain) throws SQLException, UserDetailsNotFoundException, EventException, UnresolvedNotificationDataException, TemplateNotFoundException, MultipleTemplateException, NotificationResolverNotFoundException, NotificationException {

		logger.entering(className, "toCreatePrimarySMTPAddress");
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		Connection oimdb = null;
		String message = "";
		System.out.println("toCreatePrimarySMTPAddress:::: ");
		
		try {
			logger.fine("Getting OIM Connection");
			oimdb = Platform.getOperationalDS().getConnection();
			logger.fine("Constructing SQL");
			String sql = "select cantidad_disponible, correo_notificacion from UTOIM_OIM.control_licencias where tipo = 'Licencias Office365'";
			String sqlUpdate = "update UTOIM_OIM.control_licencias set cantidad_disponible = cantidad_disponible - 1 where tipo = 'Licencias Office365'";
			logger.finest("SQL to execute: " + sql);
			
			logger.finer("Executing query");
			System.out.println("Executing:::: " + sql);
			stmt = oimdb.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("Entro al while::::");
				int totalLicencias = rs.getInt("cantidad_disponible");
				String correoNot = rs.getString("correo_notificacion");
				String[] notUser = correoNot.split(";"); 
				
				System.out.println("totalLicencias:::: " + totalLicencias);
				System.out.println("correoNot:::: " + correoNot);
				
				if (totalLicencias > 2) {
					System.out.println("Entro a totalLicencias > 2::::");
					
					logger.finer("Executing update");
					stmt1 = oimdb.prepareStatement(sqlUpdate);
					stmt1.executeUpdate();
					
					if(userLogin!=null) {
						message=userLogin.toLowerCase();
					}
					
					if(domain!=null) {
						message=message.concat(domain.toLowerCase());
					}
					
				}else if (totalLicencias <= 2) {
					
					System.out.println("Entro a totalLicencias <= 2::::");
					logger.fine("Preparing notification event");
				    NotificationEvent ev = new NotificationEvent();
				    
				    HashMap<String, Object> templateParams = new HashMap<String, Object>();
				    templateParams.put("LicenciasDisponibles", totalLicencias);
				    
				    ev.setTemplateName("LicenceNotificationOffice365 Alert");
				    ev.setUserIds(notUser);
				    ev.setParams(templateParams);
				    
					if (totalLicencias == 0) {
						nsrv.notify(ev);
					}else {
						System.out.println("Entro a else ::::");
						logger.finer("Executing update");
						stmt1 = oimdb.prepareStatement(sqlUpdate);
						stmt1.executeUpdate();
						
						nsrv.notify(ev);
						
						if(userLogin!=null) {
							message=userLogin.toLowerCase();
						}
						
						if(domain!=null) {
							message=message.concat(domain.toLowerCase());
						}
					}
				}
			}
		} finally {
			if (stmt != null) {
				logger.finest("Closing Prepared Statement...");
				stmt.close();
			}
			if (stmt1 != null) {
				logger.finest("Closing Prepared Statement 1...");
				stmt1.close();
			}
			if (oimdb != null) {
				logger.finer("Closing database connection!");
				oimdb.close();
			}
		}
		
		logger.exiting(className, "toCreatePrimarySMTPAddress", message);
		return message;

	}

}
