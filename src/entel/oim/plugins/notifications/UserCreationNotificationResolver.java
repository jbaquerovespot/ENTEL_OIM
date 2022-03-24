package entel.oim.plugins.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

import oracle.iam.notification.vo.NotificationAttribute;

public class UserCreationNotificationResolver extends BaseNotificationResolver {

	@Override
	public List<NotificationAttribute> getAvailableData(String string, Map<String, Object> map) throws Exception {
		List<NotificationAttribute> attrs = new ArrayList<>();
		return attrs;
	}

	@Override
	public HashMap<String, Object> getReplacedData(String string, Map<String, Object> map) throws Exception {

		HashMap<String, Object> resolvedData = new HashMap<>();

		if (map.containsKey("fullName")) {
			String content = (String) map.get("fullName");
			if (content != null) {
				resolvedData.put("fullName", content);
			}
		}

		if (map.containsKey("firstName")) {
			String content = (String) map.get("firstName");
			if (content != null) {
				resolvedData.put("firstName", content);
			}
		}

		if (map.containsKey("userLogin")) {
			String content = (String) map.get("userLogin");
			if (content != null) {
				resolvedData.put("userLogin", content);
			}
		}

		return resolvedData;
	}

}
