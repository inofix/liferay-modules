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
import ch.inofix.referencemanager.web.configuration.ReferenceManagerConfiguration;

/**
 * Configuration of Inofix' reference-manager.
 * 
 * @author Christian Berndt
 * @created 2017-02-13 21:55
 * @modified 2017-02-13 21:55
 * @version 1.0.0
 */
@Component(
    configurationPid = "ch.inofix.referencemanager.web.configuration.ReferenceManagerConfiguration", 
    configurationPolicy = ConfigurationPolicy.OPTIONAL, 
    immediate = true, 
    property = {
        "javax.portlet.name=" + PortletKeys.REFERENCE_MANAGER 
    }, 
    service = ConfigurationAction.class
)
public class ReferenceManagerConfigurationAction extends DefaultConfigurationAction {

    @Override
    public String getJspPath(HttpServletRequest httpServletRequest) {
        return "/reference/configuration.jsp";
    }

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String columns = ParamUtil.getString(actionRequest, "columns");

        setPreference(actionRequest, "columns", columns.split(","));

        super.processAction(portletConfig, actionRequest, actionResponse);
    }

    @Override
    public void include(PortletConfig portletConfig, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {

        httpServletRequest.setAttribute(ReferenceManagerConfiguration.class.getName(),
                _referenceManagerConfiguration);

        super.include(portletConfig, httpServletRequest, httpServletResponse);
    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        _referenceManagerConfiguration = Configurable.createConfigurable(ReferenceManagerConfiguration.class,
                properties);
    }

    private volatile ReferenceManagerConfiguration _referenceManagerConfiguration;
}
