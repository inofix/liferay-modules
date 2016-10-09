package ch.inofix.portlet.newsletter.portlet.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * Utility methods used by the newsletter-portlets. Based on the model of the
 * ActionUtil of Liferay proper.
 *
 * @author Christian Berndt
 * @created 2016-10-08 16:38
 * @modified 2016-10-08 16:38
 * @version 1.0.0
 *
 */
public class PortletUtil {

    /**
     *
     * @param key
     * @return
     * @since 1.0.0
     */
    public static String translate(String key) {
        return translate(key, null);
    }

    /**
     *
     * @param key
     * @param object
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Object object) {
        return translate(key, new Object[] { object });
    }

    /**
     *
     * @param key
     * @param objects
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Object[] objects) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Language");
            return MessageFormat.format(bundle.getString(key), objects);
        } catch (MissingResourceException mre) {
            _log.warn(mre.getMessage());
            return key;
        }
    }

    private static Log _log = LogFactoryUtil
            .getLog(PortletUtil.class.getName());

}
