package ch.inofix.portlet.mapsearch.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-08-30 23:51
 * @modified 2016-08-30 23:51
 * @version 1.0.0
 *
 */
public class PortletUtil {

    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil.getLog(PortletUtil.class
            .getName());

    /**
     * @param key
     * @param locale
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Locale locale) {

        return translate(key, locale, null);
    }

    /**
     * @param key
     * @param object
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Locale locale, Object object) {

        return translate(key, locale, new Object[] { object });
    }

    /**
     * @param key
     * @param objects
     * @return
     * @since 1.0.0
     */
    public static String translate(String key, Locale locale, Object[] objects) {

        try {
            ResourceBundle bundle = ResourceBundle
                    .getBundle("Language", locale);
            return MessageFormat.format(bundle.getString(key), objects);
        } catch (MissingResourceException mre) {
            _log.warn(mre.getMessage());
            return key;
        }
    }

}
