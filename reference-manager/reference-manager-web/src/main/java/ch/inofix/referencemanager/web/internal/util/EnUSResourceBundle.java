package ch.inofix.referencemanager.web.internal.util;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.UTF8Control;

/**
 * Append the reference-manager's Language.properties to Liferay's core
 * Language.properties. This is required to access the social-activtiy keys
 * within Liferay's social activity framework.
 * 
 * @author Christian Berndt
 * @created 2016-12-04 13:21
 * @modified 2016-12-04 13:21
 * @version 1.0.0
 */
@Component(
    property = { "language.id=en_US" }, 
    service = ResourceBundle.class
)
public class EnUSResourceBundle extends ResourceBundle {

    @Override
    protected Object handleGetObject(String key) {
        return _resourceBundle.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return _resourceBundle.getKeys();
    }

    private final ResourceBundle _resourceBundle = ResourceBundle.getBundle(
        "content.Language", UTF8Control.INSTANCE);

}
