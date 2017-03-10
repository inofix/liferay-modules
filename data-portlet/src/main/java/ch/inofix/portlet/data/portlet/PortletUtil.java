package ch.inofix.portlet.data.portlet;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 18:37
 * @modified 2017-03-09 18:37
 * @version 1.0.0
 *
 *
 */
public class PortletUtil {

    /**
     * @param key
     * @return
     * @since 1.0.0
     */
    public static String translate(String key) {

        return translate(key, null);
    }

    /**
     * @param key
     * @param object
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Object object) {

        return translate(key, new Object[] { object });
    }

    /**
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
            log.warn(mre.getMessage());
            return key;
        }
    }

    // Enable logging for this class
    private static Log log = LogFactoryUtil.getLog(PortletUtil.class.getName());

}
