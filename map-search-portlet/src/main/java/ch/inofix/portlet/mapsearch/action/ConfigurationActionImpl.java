
package ch.inofix.portlet.mapsearch.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-07-21 22:23
 * @modified 2016-07-21 22:23
 * @version 1.0.0
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapHeight = ParamUtil.getString(actionRequest, "mapHeight");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");

        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapHeight", mapHeight);
        setPreference(actionRequest, "mapZoom", mapZoom);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }

}
