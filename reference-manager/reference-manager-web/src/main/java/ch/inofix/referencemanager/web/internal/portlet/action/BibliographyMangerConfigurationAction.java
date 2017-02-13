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
import ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration;

/**
 * Configuration of Inofix' bibliography-manager.
 * 
 * @author Christian Berndt
 * @created 2017-02-08 23:31
 * @modified 2017-02-11 18:47
 * @version 1.0.2
 */
@Component(
    configurationPid = "ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration", 
    configurationPolicy = ConfigurationPolicy.OPTIONAL, 
    immediate = true, 
    property = {
        "javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER 
    }, 
    service = ConfigurationAction.class
)
public class BibliographyMangerConfigurationAction extends DefaultConfigurationAction {

    @Override
    public String getJspPath(HttpServletRequest httpServletRequest) {
        return "/bibliography/configuration.jsp";
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

        httpServletRequest.setAttribute(BibliographyManagerConfiguration.class.getName(),
                _bibliographyManagerConfiguration);

        super.include(portletConfig, httpServletRequest, httpServletResponse);
    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        _bibliographyManagerConfiguration = Configurable.createConfigurable(BibliographyManagerConfiguration.class,
                properties);
    }

    private volatile BibliographyManagerConfiguration _bibliographyManagerConfiguration;

}
