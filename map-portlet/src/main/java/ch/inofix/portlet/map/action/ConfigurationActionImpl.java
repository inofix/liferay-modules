
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
 * @modified 2016-03-14 21:06
 * @version 1.0.3
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

        String claim = ParamUtil.getString(actionRequest, "claim");
        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapHeight = ParamUtil.getString(actionRequest, "mapHeight");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");
        String[] markerLabels = ParamUtil.getParameterValues(actionRequest, "markerLabels");
        String[] markerLatLongs = ParamUtil.getParameterValues(actionRequest, "markerLatLongs");
        String tilesCopyright = ParamUtil.getString(actionRequest, "tilesCopyright");
        String tilesURL = ParamUtil.getString(actionRequest, "tilesURL");

        setPreference(actionRequest, "claim", claim);
        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapHeight", mapHeight);
        setPreference(actionRequest, "mapZoom", mapZoom);
        setPreference(actionRequest, "markerLabels", markerLabels);
        setPreference(actionRequest, "markerLatLongs", markerLatLongs);
        setPreference(actionRequest, "tilesCopyright", tilesCopyright);
        setPreference(actionRequest, "tilesURL", tilesURL);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
