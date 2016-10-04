package ch.inofix.portlet.slider.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-04 21:35
 * @modified 2016-10-04 21:35
 * @version 1.0.0
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String structureId = ParamUtil.getString(actionRequest, "structureId");

        setPreference(actionRequest, "structureId", structureId);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }

}
