package entel.oim.plugins.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

import oracle.iam.notification.vo.NotificationAttribute;

public class TrustedUserMassiveDeleteReconciliationResolver extends BaseNotificationResolver {

	@Override
	public List<NotificationAttribute> getAvailableData(String string, Map<String, Object> map) throws Exception {
		List<NotificationAttribute> attrs = new ArrayList<>();
		return attrs;
	}

	@Override
	public HashMap<String, Object> getReplacedData(String string, Map<String, Object> map) throws Exception {

		HashMap<String, Object> resolvedData = new HashMap<>();

		if (map.containsKey("Usrs_Html")) {
			String content = (String) map.get("Usrs_Html");
			if (content != null) {
				content = StringEscapeUtils.unescapeHtml4(content);
				resolvedData.put("Usrs_Html", content);
			}
		}
		

		return resolvedData;
	}

}
