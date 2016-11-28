package ch.inofix.portlet.newsletter.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

/**
 * @author Christian Berndt
 * @created 2016-10-05 15:38
 * @modified 2016-11-28 12:17
 * @version 1.0.5
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String articleGroupId = actionRequest.getParameter("articleGroupId");

        String newsletterStructureId = actionRequest
                .getParameter("newsletterStructureId");

        setPreference(actionRequest, "articleGroupId", articleGroupId);
        setPreference(actionRequest, "newsletterStructureId",
                newsletterStructureId);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
