package ch.inofix.portlet.data.messaging;

import java.util.List;

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
 * Import data in configured intervals
 *
 * @author Christian Berndt
 * @created 2017-03-27 23:53
 * @modified 2017-03-27 23:53
 * @version 1.0.0
 *
 */
public class SyncDataMessageListener extends BaseMessageListener {

    @Override
    protected void doReceive(Message message) throws Exception {

        String portletId = (String) message.get("PORTLET_ID");
        long companyId = GetterUtil.getLong(message.get("companyId"));

        List<PortletPreferences> portletPreferences = PortletPreferencesLocalServiceUtil
                .getPortletPreferences();

        // Loop over the preferences of all deployed portlets

        for (PortletPreferences preferences : portletPreferences) {

            // Only consider data-portlets.

            if (portletId.equals(preferences.getPortletId())) {

                javax.portlet.PortletPreferences prefs = PortletPreferencesLocalServiceUtil
                        .getPreferences(companyId, preferences.getOwnerId(),
                                preferences.getOwnerType(),
                                preferences.getPlid(), portletId);

                // Retrieve the configuration parameter
                String dataURL = PrefsPropsUtil.getString(prefs, companyId,
                        "dataURL");
                String password = PrefsPropsUtil.getString(prefs, companyId,
                        "password");
                String username = PrefsPropsUtil.getString(prefs, companyId,
                        "username");

                log.info("dataURL = " + dataURL);
                log.info("password = " + password);
                log.info("username = " + username);

                // try {
                // SyncUtil.syncWithCalDAVServer(calendarId,
                // configuredCalendar, servername, domain, username,
                // password, restoreFromTrash, syncOnlyUpcoming);
                // } catch (Exception e) {
                // log.error(e.getMessage());
                // }

            }

        }
    }

    private static Log log = LogFactoryUtil
            .getLog(SyncDataMessageListener.class.getName());
}
