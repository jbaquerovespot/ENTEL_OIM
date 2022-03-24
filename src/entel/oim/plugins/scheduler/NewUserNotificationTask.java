package entel.oim.plugins.scheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.sql.DataSource;

import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.scheduler.vo.TaskSupport;
import oracle.iam.identity.exception.AccessDeniedException;

public class NewUserNotificationTask extends TaskSupport {
	
	private final static String className = NewUserNotificationTask.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private final NotificationService nsrv = Platform.getService(NotificationService.class);

	@Override
	public void execute(HashMap arg0) throws Exception {
		// TODO Auto-generated method stub
		DataSource ds = Platform.getOperationalDS();
        Connection connection = ds.getConnection();
        Statement st = connection.createStatement();
        ResultSet result = st.executeQuery("SELECT usr_login FROM usr WHERE usr_hire_date BETWEEN SYSDATE-1 AND SYSDATE");
        
        List<String> userLogins = new ArrayList<>();
        while (result.next()) {
            String usrLogin = result.getString(1);
            userLogins.add(usrLogin);
        }
        if(userLogins.size() > 0) {
        	String[] users = new String[userLogins.size()];
        	userLogins.toArray(users);
        	sendEmailNotification("Email Bienvenida", users);
        }
	}

	@Override
	public HashMap getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributes() {
		// TODO Auto-generated method stub
		
	}
	
	private void sendEmailNotification(String templateName, String[] addressee) {
		NotificationEvent ev = new NotificationEvent();
		try {
			ev.setTemplateName(templateName);
			ev.setUserIds(addressee);
			ev.setParams(new HashMap<>());

			nsrv.notify(ev);
			System.out.println("Enviando notificacion de bienvenida a " + ev.getUserIds().toString());

		} catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "sendEmailNotification - AccessDeniedException error", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "sendEmailNotification - Unexpected error", e);
		} finally {
			logger.exiting(className, "sendEmailNotification");
		}
	}

}
