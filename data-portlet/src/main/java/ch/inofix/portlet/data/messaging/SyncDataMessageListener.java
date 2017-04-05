package ch.inofix.portlet.data.messaging;

import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;

/**
 *
 * Import data in configured intervals
 *
 * @author Christian Berndt
 * @created 2017-03-27 23:53
 * @modified 2017-03-28 17:10
 * @version 1.0.1
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
                final String password = PrefsPropsUtil.getString(prefs,
                        companyId, "password");
                final String userName = PrefsPropsUtil.getString(prefs,
                        companyId, "userName");

                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password
                                .toCharArray());
                    }
                });

                File file = null;

                if (Validator.isNotNull(dataURL)) {

                    String tmpDir = SystemProperties
                            .get(SystemProperties.TMP_DIR)
                            + StringPool.SLASH
                            + Time.getTimestamp();

                    URL url = new URL(dataURL);
                    file = new File(tmpDir + "/data.xml");
                    FileUtils.copyURLToFile(url, file);

                    long userId = PrefsPropsUtil.getLong(prefs, companyId,
                            "userId");
                    String taskName = file.getName();
                    long groupId = PrefsPropsUtil.getLong(prefs, companyId,
                            "groupId");
                    boolean privateLayout = true;
                    Map<String, String[]> parameterMap = new HashMap<String, String[]>();

                    _log.info("taskName = " + taskName);
//                    _log.info("userId = " + userId);
//                    _log.info("groupId = " + groupId);

                    MeasurementLocalServiceUtil.importMeasurements(userId,
                            groupId, privateLayout, parameterMap, file);

                }
            }
        }
    }

    private static Log _log = LogFactoryUtil
            .getLog(SyncDataMessageListener.class.getName());
}
