package ch.inofix.portlet.cdav.messaging;

import java.util.List;
import ch.inofix.portlet.cdav.util.SyncUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;

/**
 * 
 * Sync calendar(s) in configured interval(s)
 * 
 * @author Christian Berndt
 * @created 2015-06-12 09:55
 * @modified 2015-06-12 09:55
 * @version 1.0.0
 * 
 */
public class SyncCalendarMessageListener extends BaseMessageListener {

	// Enable logging for this class
	private static Log log = LogFactoryUtil
			.getLog(SyncCalendarMessageListener.class.getName());

	@Override
	protected void doReceive(Message message) throws Exception {

		String portletId = (String) message.get("PORTLET_ID");
		long companyId = GetterUtil.getLong(message.get("companyId"));

		List<PortletPreferences> portletPreferences = PortletPreferencesLocalServiceUtil
				.getPortletPreferences();

		// Loop over the preferences of all deployed portlets

		for (PortletPreferences preferences : portletPreferences) {

			// Only consider cdav-manager portlets

			if (portletId.equals(preferences.getPortletId())) {

				javax.portlet.PortletPreferences prefs = PortletPreferencesLocalServiceUtil
						.getPreferences(companyId, preferences.getOwnerId(),
								preferences.getOwnerType(),
								preferences.getPlid(), portletId);

				// Retrieve the configuration parameter

				long calendarId = PrefsPropsUtil.getLong(prefs, companyId,
						"calendarId");
				String domain = PrefsPropsUtil.getString(prefs, companyId,
						"domain");
				boolean restoreFromTrash = PrefsPropsUtil.getBoolean(prefs,
						companyId, "restoreFromTrash", false);
				String servername = PrefsPropsUtil.getString(prefs, companyId,
						"servername");
				boolean syncOnlyUpcoming = PrefsPropsUtil.getBoolean(prefs,
						companyId, "syncOnlyUpcoming", true);
				String password = PrefsPropsUtil.getString(prefs, companyId,
						"password");
				String username = PrefsPropsUtil.getString(prefs, companyId,
						"username");

				// log.info("calendarId = " + calendarId);
				// log.info("domain = " + domain);
				// log.info("restoreFromTrash = " + restoreFromTrash);
				// log.info("servername = " + servername);
				// log.info("syncOnlyUpcoming = " + syncOnlyUpcoming);
				// log.info("password = " + password);
				// log.info("username = " + username);

				try {
					SyncUtil.syncWithCalDAVServer(calendarId, servername,
							domain, username, password, restoreFromTrash,
							syncOnlyUpcoming);
				} catch (Exception e) {
					log.error(e.getMessage());
				}

			}

		}

	}
}
