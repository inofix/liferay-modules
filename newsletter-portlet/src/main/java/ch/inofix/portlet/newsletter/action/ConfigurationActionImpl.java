
package ch.inofix.portlet.newsletter.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-10-05 15:38
 * @modified 2016-10-07 14:27
 * @version 1.0.2
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String className = ParamUtil.getString(actionRequest, "className");
        String script = ParamUtil.getString(actionRequest, "script");

        setPreference(actionRequest, "className", className);
        setPreference(actionRequest, "script", script);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
