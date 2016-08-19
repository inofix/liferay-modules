
package ch.inofix.portlet.map.action;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Christian Berndt
 * @created 2016-03-01 23:44
 * @modified 2016-08-08 18:53
 * @version 1.1.6
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(ConfigurationActionImpl.class.getName());

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        UploadPortletRequest uploadPortletRequest =
            PortalUtil.getUploadPortletRequest(actionRequest);

        List<String> labels = new ArrayList<String>();
        List<String> locations = new ArrayList<String>();

        File file = uploadPortletRequest.getFile("file");

        if (Validator.isNotNull(file) && file.length() > 0) {

            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

            for (CSVRecord record : records) {

                locations.add(record.get(0));
                labels.add(record.get(1));

            }
        }

        String addressResolverURL =
            ParamUtil.getString(actionRequest, "addressResolverURL");
        String claim = ParamUtil.getString(actionRequest, "claim");
        String customAddressAndLabel =
            ParamUtil.getString(actionRequest, "customAddressAndLabel");
        String customLatLon =
            ParamUtil.getString(actionRequest, "customLatLon");
        String dataTableColumnDefs =
            ParamUtil.getString(actionRequest, "dataTableColumnDefs");
        String dataTableColumns =
            ParamUtil.getString(actionRequest, "dataTableColumns");
        String dataTablePaging =
            ParamUtil.getString(actionRequest, "dataTablePaging", "false");
        String[] filterColumns =
            actionRequest.getParameterValues("filterColumns");
        String[] filterDataURLs =
            actionRequest.getParameterValues("filterDataURLs");
        String[] filterPlaceholders =
            actionRequest.getParameterValues("filterPlaceholders");
        String filter1DataURL =
            ParamUtil.getString(actionRequest, "filter1DataURL");
        String filter1Values =
            ParamUtil.getString(actionRequest, "filter1Values");
        String labelValueMapping =
            ParamUtil.getString(actionRequest, "labelValueMapping");
        String labelValueMappings =
            ParamUtil.getString(actionRequest, "labelValueMappings");
        String locationsURL =
            ParamUtil.getString(actionRequest, "locationsURL");
        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapHeight = ParamUtil.getString(actionRequest, "mapHeight");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");
        String markerIconConfig =
            ParamUtil.getString(actionRequest, "markerIconConfig");
        String[] markerLabels =
            actionRequest.getParameterValues("markerLabels");
        String[] markerLocations =
            actionRequest.getParameterValues("markerLocations");
        String showTable = ParamUtil.getString(actionRequest, "showTable");
        String tilesCopyright =
            ParamUtil.getString(actionRequest, "tilesCopyright");
        String tilesURL = ParamUtil.getString(actionRequest, "tilesURL");
        String useAddressResolver =
            ParamUtil.getString(actionRequest, "useAddressResolver");
        String useDivIcon = ParamUtil.getString(actionRequest, "useDivIcon");
        String useGlobalJQuery =
            ParamUtil.getString(actionRequest, "useGlobalJQuery");

        // Append markers from file to markers from request
        markerLabels =
            (String[]) ArrayUtil.append(markerLabels, labels.toArray());
        markerLocations =
            (String[]) ArrayUtil.append(markerLocations, locations.toArray());

        setPreference(actionRequest, "addressResolverURL", addressResolverURL);
        setPreference(actionRequest, "claim", claim);
        setPreference(actionRequest, "customLatLon", customLatLon);
        setPreference(
            actionRequest, "customAddressAndLabel", customAddressAndLabel);
        setPreference(actionRequest, "dataTableColumnDefs", dataTableColumnDefs);
        setPreference(actionRequest, "dataTableColumns", dataTableColumns);
        setPreference(actionRequest, "dataTablePaging", dataTablePaging);
        setPreference(actionRequest, "filterColumns", filterColumns);
        setPreference(actionRequest, "filterDataURLs", filterDataURLs);
        setPreference(actionRequest, "filterPlaceholders", filterPlaceholders);
        setPreference(actionRequest, "filter1DataURL", filter1DataURL);
        setPreference(actionRequest, "filter1Values", filter1Values);
        setPreference(actionRequest, "labelValueMapping", labelValueMapping);
        setPreference(actionRequest, "labelValueMappings", labelValueMappings);
        setPreference(actionRequest, "locationsURL", locationsURL);
        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapHeight", mapHeight);
        setPreference(actionRequest, "mapZoom", mapZoom);
        setPreference(actionRequest, "markerIconConfig", markerIconConfig);
        setPreference(actionRequest, "markerLabels", markerLabels);
        setPreference(actionRequest, "markerLocations", markerLocations);
        setPreference(actionRequest, "showTable", showTable);
        setPreference(actionRequest, "tilesCopyright", tilesCopyright);
        setPreference(actionRequest, "tilesURL", tilesURL);
        setPreference(actionRequest, "useAddressResolver", useAddressResolver);
        setPreference(actionRequest, "useDivIcon", useDivIcon);
        setPreference(actionRequest, "useGlobalJQuery", useGlobalJQuery);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
