package ch.inofix.portlet.newsletter.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

/**
 * @author Christian Berndt
 * @created 2016-10-05 15:38
 * @modified 2016-10-11 22:46
 * @version 1.0.3
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String contextProperties = actionRequest
                .getParameter("contextProperties");

        setPreference(actionRequest, "contextProperties", contextProperties);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
