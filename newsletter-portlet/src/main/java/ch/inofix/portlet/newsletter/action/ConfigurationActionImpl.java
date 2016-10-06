
package ch.inofix.portlet.newsletter.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-10-05 15:38
 * @modified 2016-10-06 17:35
 * @version 1.0.1
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String script = ParamUtil.getString(actionRequest, "script");

        setPreference(actionRequest, "script", script);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
