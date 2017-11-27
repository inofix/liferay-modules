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
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 *
 * Import data in configured intervals
 *
 * @author Christian Berndt
 * @created 2017-03-27 23:53
 * @modified 2017-11-20 17:43
 * @version 1.0.5
 *
 */
public class SyncDataMessageListener extends BaseMessageListener {

    @Override
    protected void doReceive(Message message) throws Exception {

        // Read preferences of all deployed data-manager portlets
        
        String portletId = (String) message.get("PORTLET_ID");
        long companyId = GetterUtil.getLong(message.get("companyId"));
        int ownerType = PortletKeys.PREFS_OWNER_TYPE_GROUP;
        long plid = 0;

        List<PortletPreferences> portletPreferencesList = PortletPreferencesLocalServiceUtil
                .getPortletPreferences(ownerType, plid, portletId);

        for (PortletPreferences portletPreferences : portletPreferencesList) {

            long groupId = portletPreferences.getOwnerId();

            javax.portlet.PortletPreferences preferences = PortletPreferencesFactoryUtil
                    .fromDefaultXML(portletPreferences.getPreferences());

            // Retrieve the configuration parameter
            String dataURL = PrefsPropsUtil.getString(preferences, companyId,
                    "dataURL");
            String idField = PrefsPropsUtil.getString(preferences, companyId,
                    "idField");
            String nameField = PrefsPropsUtil.getString(preferences, companyId,
                    "nameField");
            String timestampField = PrefsPropsUtil.getString(preferences, companyId,
                    "timestampField");
            String password = PrefsPropsUtil.getString(preferences, companyId,
                    "password");
            String userId = PrefsPropsUtil.getString(preferences, companyId,
                    "userId");
            String userName = PrefsPropsUtil.getString(preferences, companyId,
                    "userName");

            String[] dataURLs = null;

            if (dataURL.endsWith(StringPool.COMMA)) {
                dataURLs = (dataURL + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                dataURLs = dataURL.split(StringPool.COMMA);
            }
            
            String[] idFields = null;

            if (idField.endsWith(StringPool.COMMA)) {
                idFields = (idField + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                idFields = idField.split(StringPool.COMMA);
            }
            
            String[] nameFields = null;

            if (nameField.endsWith(StringPool.COMMA)) {
                nameFields = (nameField + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                nameFields = nameField.split(StringPool.COMMA);
            }
            
            String[] timestampFields = null;

            if (timestampField.endsWith(StringPool.COMMA)) {
                timestampFields = (timestampField + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                timestampFields = timestampField.split(StringPool.COMMA);
            }
            
            String[] passwords = null;

            if (password.endsWith(StringPool.COMMA)) {
                passwords = (password + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                passwords = password.split(StringPool.COMMA);
            }

            String[] userIds = null;

            if (userId.endsWith(StringPool.COMMA)) {
                userIds = (userId + "0").split(StringPool.COMMA);
            } else {
                userIds = userId.split(StringPool.COMMA);
            }
            
            String[] userNames = null;

            if (userName.endsWith(StringPool.COMMA)) {
                userNames = (userName + StringPool.SPACE).split(StringPool.COMMA);
            } else {
                userNames = userName.split(StringPool.COMMA);
            }

            // loop over configured data sources

            for (int i = 0; i < dataURLs.length; i++) {

                final String user = userNames[i];
                final String pw = passwords[i];

                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pw
                                .toCharArray());
                    }
                });

                File file = null;

                if (Validator.isNotNull(dataURLs[i])) {

                    String tmpDir = SystemProperties
                            .get(SystemProperties.TMP_DIR)
                            + StringPool.SLASH
                            + Time.getTimestamp();

                    URL url = new URL(dataURLs[i]);

                    String fileName = url.getFile();

                    String extension = FileUtil.getExtension(fileName);

                    file = new File(tmpDir + "/data." + extension);
                    FileUtils.copyURLToFile(url, file);

                    Map<String, String[]> parameterMap = new HashMap<String, String[]>();
                    parameterMap.put("idField", new String[] {idFields[i]}); 
                    parameterMap.put("nameField", new String[] {nameFields[i]}); 
                    parameterMap.put("timestampField", new String[] {timestampFields[i]}); 

                    MeasurementLocalServiceUtil.importMeasurements(
                            Long.valueOf(userIds[i]), groupId,
                            parameterMap, file);

                }
            }
        }
    }

    private static Log _log = LogFactoryUtil
            .getLog(SyncDataMessageListener.class.getName());
}
