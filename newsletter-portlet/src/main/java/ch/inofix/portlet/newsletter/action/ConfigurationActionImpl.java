package ch.inofix.portlet.newsletter.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

/**
 * @author Christian Berndt
 * @created 2016-10-05 15:38
 * @modified 2016-10-20 17:33
 * @version 1.0.4
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String newsletterStructureId = actionRequest
                .getParameter("newsletterStructureId");

        setPreference(actionRequest, "newsletterStructureId", newsletterStructureId);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
