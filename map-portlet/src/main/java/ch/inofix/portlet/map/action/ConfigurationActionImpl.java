
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
 * @modified 2016-05-04 21:19
 * @version 1.0.5
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
        List<String> latLongs = new ArrayList<String>();

        File file = uploadPortletRequest.getFile("file");

        if (Validator.isNotNull(file) && file.length() > 0) {

            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

            for (CSVRecord record : records) {

                labels.add(record.get(0));

                StringBuilder sb = new StringBuilder(5);
                sb.append("[");
                sb.append(record.get(1));
                sb.append(",");
                sb.append(record.get(2));
                sb.append("]");

                latLongs.add(sb.toString());
            }
        }

        String claim = ParamUtil.getString(actionRequest, "claim");
        String mapCenter = ParamUtil.getString(actionRequest, "mapCenter");
        String mapHeight = ParamUtil.getString(actionRequest, "mapHeight");
        String mapZoom = ParamUtil.getString(actionRequest, "mapZoom");
        String markerIconConfig =
            ParamUtil.getString(actionRequest, "markerIconConfig");
        String[] markerLabels =
            ParamUtil.getParameterValues(actionRequest, "markerLabels");
        String[] markerLatLongs =
            actionRequest.getParameterValues("markerLatLongs");
        String tilesCopyright =
            ParamUtil.getString(actionRequest, "tilesCopyright");
        String tilesURL = ParamUtil.getString(actionRequest, "tilesURL");
        String useDivIcon = ParamUtil.getString(actionRequest, "useDivIcon");

        // Append markers from file to markers from request
        markerLabels =
            (String[]) ArrayUtil.append(markerLabels, labels.toArray());
        markerLatLongs =
            (String[]) ArrayUtil.append(markerLatLongs, latLongs.toArray());

        setPreference(actionRequest, "claim", claim);
        setPreference(actionRequest, "mapCenter", mapCenter);
        setPreference(actionRequest, "mapHeight", mapHeight);
        setPreference(actionRequest, "mapZoom", mapZoom);
        setPreference(actionRequest, "markerIconConfig", markerIconConfig);
        setPreference(actionRequest, "markerLabels", markerLabels);
        setPreference(actionRequest, "markerLatLongs", markerLatLongs);
        setPreference(actionRequest, "tilesCopyright", tilesCopyright);
        setPreference(actionRequest, "tilesURL", tilesURL);
        setPreference(actionRequest, "useDivIcon", useDivIcon);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
