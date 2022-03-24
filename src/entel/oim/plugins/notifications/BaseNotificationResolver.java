package entel.oim.plugins.notifications;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.impl.NotificationEventResolver;
import oracle.iam.platform.Platform;

public abstract class BaseNotificationResolver implements NotificationEventResolver {

    private static final Logger logger = Logger.getLogger(BaseNotificationResolver.class.getName());

    protected HashMap<String, Object> resolveUserData(Long userKey) throws Exception {
	HashMap<String, Object> resolvedData = new HashMap<>();
	UserManager umgr = Platform.getService(UserManager.class);

	logger.finer("Looking up user with usr_key [" + userKey + "]");
	if (userKey != null) {
	    User u = umgr.getDetails(String.valueOf(userKey), null, false);
	    logger.finer("Found user [" + u.getLogin() + "]. Resolving notification data");

	    Map<String, Object> userAttrs = u.getAttributes();

	    /**
	     * Convierte los atributos definidos en los mismos que aparecen en
	     * el template (Los espacios son '_')
	     */
	    for (Map.Entry<String, Object> entry : userAttrs.entrySet()) {
		resolvedData.put(entry.getKey().replace(' ', '_'), entry.getValue());
	    }
	}

	return resolvedData;
    }

    protected HashMap<String, Object> resolveUserData(String userLogin) throws Exception {
	HashMap<String, Object> resolvedData = new HashMap<>();
	UserManager umgr = Platform.getService(UserManager.class);

	logger.finer("Looking up user with User Login [" + userLogin + "]");
	if (userLogin != null && !userLogin.isEmpty()) {
	    User u = umgr.getDetails(userLogin, null, true);
	    logger.finer("Found user [" + u.getEntityId() + "]. Resolving notification data");
	    
	    Map<String, Object> userAttrs = u.getAttributes();

	    /**
	     * Convierte los atributos definidos en los mismos que aparecen en
	     * el template (Los espacios son '_')
	     */
	    for (Map.Entry<String, Object> entry : userAttrs.entrySet()) {
		resolvedData.put(entry.getKey().replace(' ', '_'), entry.getValue());
	    }
	}

	return resolvedData;
    }
}
