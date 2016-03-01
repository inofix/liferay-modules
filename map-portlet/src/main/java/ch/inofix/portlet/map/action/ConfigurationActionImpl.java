
package ch.inofix.portlet.map.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-03-01 23:44
 * @modified 2016-03-01 23:44
 * @version 1.0.0
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    // Enable logging for this class
    private static Log log =
        LogFactoryUtil.getLog(ConfigurationActionImpl.class.getName());

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");

        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapZoom", mapZoom);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
