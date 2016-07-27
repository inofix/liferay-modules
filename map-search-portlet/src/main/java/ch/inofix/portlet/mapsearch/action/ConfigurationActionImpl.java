
package ch.inofix.portlet.mapsearch.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-07-21 22:23
 * @modified 2016-07-27 14:22
 * @version 1.0.4
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String classNames = ParamUtil.getString(actionRequest, "classNames");
        String dataTableColumnDefs =  ParamUtil.getString(actionRequest, "dataTableColumnDefs");
        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapHeight = ParamUtil.getString(actionRequest, "mapHeight");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");
        String markerIconConfig = ParamUtil.getString(actionRequest, "markerIconConfig");
        String tilesCopyright = ParamUtil.getString(actionRequest, "tilesCopyright");
        String tilesURL = ParamUtil.getString(actionRequest, "tilesURL");
        String viewByDefault = ParamUtil.getString(actionRequest, "viewByDefault");
        String viewInContext = ParamUtil.getString(actionRequest, "viewInContext");

        setPreference(actionRequest, "classNames", classNames);
        setPreference(actionRequest, "dataTableColumnDefs", dataTableColumnDefs);
        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapHeight", mapHeight);
        setPreference(actionRequest, "mapZoom", mapZoom);
        setPreference(actionRequest, "markerIconConfig", markerIconConfig);
        setPreference(actionRequest, "tilesCopyright", tilesCopyright);
        setPreference(actionRequest, "tilesURL", tilesURL);
        setPreference(actionRequest, "viewByDefault", viewByDefault);
        setPreference(actionRequest, "viewInContext", viewInContext);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }

}
