package entel.oim.plugins.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

import oracle.iam.notification.vo.NotificationAttribute;

public class TrustedUserDeleteReconciliationResolver extends BaseNotificationResolver {

	@Override
	public List<NotificationAttribute> getAvailableData(String string, Map<String, Object> map) throws Exception {
		List<NotificationAttribute> attrs = new ArrayList<>();
		return attrs;
	}

	@Override
	public HashMap<String, Object> getReplacedData(String string, Map<String, Object> map) throws Exception {

		HashMap<String, Object> resolvedData = new HashMap<>();

		if (map.containsKey("info_html")) {
			String content = (String) map.get("info_html");
			if (content != null) {
				content = StringEscapeUtils.unescapeHtml4(content);
				resolvedData.put("info_html", content);
			}
		}
		
		if (map.containsKey("info_htmlF")) {
			String content = (String) map.get("info_htmlF");
			if (content != null) {
				content = StringEscapeUtils.unescapeHtml4(content);
				resolvedData.put("info_htmlF", content);
			}
		}

		return resolvedData;
	}

}
