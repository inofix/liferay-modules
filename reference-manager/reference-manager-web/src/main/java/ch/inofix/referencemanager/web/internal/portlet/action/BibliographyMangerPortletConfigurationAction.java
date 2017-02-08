package ch.inofix.referencemanager.web.internal.portlet.action;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

import aQute.bnd.annotation.metatype.Configurable;
import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.web.configuration.BibliographyManagerPortletInstanceConfiguration;

/**
 * Configuration of Inofix' bibliography-manager.
 * 
 * @author Christian Berndt
 * @created 2017-02-08 23:31
 * @modified 2017-02-08 23:31
 * @version 1.0.0
 */
@Component(
    configurationPid = "ch.inofix.referencemanager.web.configuration.BibliographyManagerPortletInstanceConfiguration",
    configurationPolicy = ConfigurationPolicy.OPTIONAL,
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER_CONFIGURATION, 

    },
    service = ConfigurationAction.class
)
public class BibliographyMangerPortletConfigurationAction extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String favoriteColor = ParamUtil.getString(actionRequest, "favoriteColor");
        setPreference(actionRequest, "favoriteColor", favoriteColor);

        super.processAction(portletConfig, actionRequest, actionResponse);
    }

    @Override
    public void include(PortletConfig portletConfig, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {

        httpServletRequest.setAttribute(BibliographyManagerPortletInstanceConfiguration.class.getName(),
                _instanceConfiguration);

        super.include(portletConfig, httpServletRequest, httpServletResponse);
    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        _instanceConfiguration = Configurable.createConfigurable(BibliographyManagerPortletInstanceConfiguration.class,
                properties);
    }

    private volatile BibliographyManagerPortletInstanceConfiguration _instanceConfiguration;

}
