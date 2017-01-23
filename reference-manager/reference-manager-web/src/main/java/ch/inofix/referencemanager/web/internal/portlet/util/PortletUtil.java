package ch.inofix.referencemanager.web.internal.portlet.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.Value;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import ch.inofix.referencemanager.service.util.BibTeXUtil;

/**
 * Utility methods for the ReferenceManagerPortlet.
 * 
 * @author Christian Berndt
 * @created 2016-11-28 23:26
 * @modified 2017-01-23 23:32
 * @version 1.0.4
 */
public class PortletUtil {

    /**
     * 
     * @param actionRequest
     * @return
     * @since 1.0.1
     * @throws Exception
     */
    public static String getBibTeX(ActionRequest actionRequest) throws Exception {

        String bibTeX = ParamUtil.getString(actionRequest, "bibTeX");
        String label = ParamUtil.getString(actionRequest, "label");
        String type = ParamUtil.getString(actionRequest, "type");

        // Read bibTeXEntry from source

        BibTeXEntry srcEntry = BibTeXUtil.getBibTeXEntry(bibTeX); 

        _log.info("srcEntry = " + srcEntry);

        // selected entry type overrides entry from src

        if (Validator.isNull(type)) {
            if (srcEntry != null) {
                if (srcEntry.getType() != null) {
                    type = srcEntry.getType().getValue();
                }
            } else {
                type = "misc";
            }
        }

        _log.info("type = " + type);

        Key typeKey = new Key(type);

        // entered label overrides label of entry from src

        if (Validator.isNull(label)) {
            if (srcEntry != null) {
                if (srcEntry.getKey() != null) {
                    label = srcEntry.getKey().getValue();
                }
            }
        }

        _log.info("label = " + label);

        Key labelKey = new Key(label);

        BibTeXEntry bibTeXEntry = new BibTeXEntry(typeKey, labelKey);

        // structured field values override bibTeX source

        String[] fields = actionRequest.getParameterValues("name");
        String[] values = actionRequest.getParameterValues("value");

        _log.info(fields.length);
        _log.info(values.length);

        for (int i = 0; i < fields.length; i++) {

            Key key = new Key(fields[i]);

            if (Validator.isNotNull(values[i])) {

                // TODO: test string for style
                Style style = Style.BRACED;

                Value value = new StringValue(values[i], style);

                bibTeXEntry.addField(key, value);

            } else {

                // get value from src
                
                Value value = srcEntry.getField(key);
                
                if (value != null) {
                    bibTeXEntry.addField(key, value);
                }
            }
        }

        bibTeX = BibTeXUtil.format(bibTeXEntry);

        _log.info(bibTeX);

        return bibTeX;
    }

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
            _log.warn(mre.getMessage());
            return key;
        }
    }

    private static Log _log = LogFactoryUtil.getLog(PortletUtil.class.getName());

}
