package ch.inofix.portlet.data.portlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import ch.inofix.portlet.data.FileFormatException;
import ch.inofix.portlet.data.MeasurementXMLException;
import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;
import ch.inofix.portlet.data.service.MeasurementServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.DocumentException;
//import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-08 19:58
 * @modified 2017-06-26 11:56
 * @version 1.1.0
 *
 */
public class DataManagerPortlet extends MVCPortlet {

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.8
     * @throws Exception
     */
    public void deleteGroupMeasurements(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        _log.info("deleteGroupMeasurements");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Measurement.class.getName(), actionRequest);

        List<Measurement> measurements = MeasurementServiceUtil
                .deleteGroupMeasurements(serviceContext.getScopeGroupId());

        SessionMessages.add(actionRequest, "request_processed", PortletUtil
                .translate("successfully-deleted-x-measurements",
                        measurements.size()));

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        actionResponse.setRenderParameter("tabs1", tabs1);
    }

    /**
     *
     * @param resourceRequest
     * @param resourceResponse
     * @throws PortalException
     * @throws SystemException
     * @throws IOException
     */
    public void getJSON(ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws PortalException,
            SystemException, IOException {

        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest
                .getAttribute(WebKeys.THEME_DISPLAY);

        // channel by id
        String channelId = ParamUtil.getString(resourceRequest, "channelId");

        // channel by name
        String channelName = ParamUtil
                .getString(resourceRequest, "channelName");

        // begin of selected interval
        long from = ParamUtil.getLong(resourceRequest, "from");

        // maximum number of data points
        int limit = ParamUtil.getInteger(resourceRequest, "limit", 1000);

        // end of selected interval
        long until = ParamUtil.getLong(resourceRequest, "until");

        Hits hits = MeasurementLocalServiceUtil.search(
                themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
                channelId, channelName, from, until, true, 0, limit, null);

        List<Document> documents = hits.toList();
        Iterator<Document> iterator = documents.iterator();

        StringBuilder sb = new StringBuilder();

        sb.append(StringPool.OPEN_BRACKET);
        sb.append(StringPool.NEW_LINE);

        while (iterator.hasNext()) {

            Document document = iterator.next();
            String json = document.get("content");

            sb.append(json);
            if (iterator.hasNext()) {
                sb.append(StringPool.COMMA);
            }
            sb.append(StringPool.NEW_LINE);

        }

        sb.append(StringPool.CLOSE_BRACKET);

        PortletResponseUtil.write(resourceResponse, sb.toString().getBytes());

    }

    /**
     * Import measurements from an uploaded file.
     *
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void importMeasurements(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        UploadPortletRequest uploadPortletRequest = PortalUtil
                .getUploadPortletRequest(actionRequest);

        String mvcPath = ParamUtil.getString(uploadPortletRequest, "mvcPath");
        String redirect = ParamUtil.getString(uploadPortletRequest, "redirect");

        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);

        String dataURL = ParamUtil.getString(uploadPortletRequest, "dataURL");

        File file = uploadPortletRequest.getFile("file");

        _log.info(file.getName());

        String extension = FileUtil.getExtension(file.getName());

        _log.info(extension);

        if ("xml".equals(extension) || "xls".equals(extension)) {

            if (Validator.isNotNull(dataURL)) {

                String tmpDir = SystemProperties.get(SystemProperties.TMP_DIR)
                        + StringPool.SLASH + Time.getTimestamp();

                URL url = new URL(dataURL);
                file = new File(tmpDir + "/data." + extension);
                FileUtils.copyURLToFile(url, file);

            }

            long userId = themeDisplay.getUserId();
            long groupId = themeDisplay.getScopeGroupId();
            boolean privateLayout = themeDisplay.getLayout().isPrivateLayout();

            String servletContextName = request.getSession()
                    .getServletContext().getServletContextName();

            String[] servletContextNames = new String[] { servletContextName };

            Map<String, String[]> parameterMap = new HashMap<String, String[]>(
                    actionRequest.getParameterMap());

            parameterMap.put("servletContextNames", servletContextNames);

            if (Validator.isNotNull(file)) {

                String fileName = file.getName();

                String message = PortletUtil
                        .translate("no-measurements-were-found");

                if ("xml".equals(extension)) {

                    try {

                        com.liferay.portal.kernel.xml.Document document = SAXReaderUtil
                                .read(file);

                        // TODO: read xPath selector from configuration
                        String selector = "//VT";
                        List<Node> nodes = document.selectNodes(selector);

                        if (nodes.size() > 0) {

                            message = PortletUtil
                                    .translate(
                                            "found-x-measurements-the-import-will-finish-in-separate-thread",
                                            nodes.size());

                            MeasurementServiceUtil
                                    .importMeasurementsInBackground(userId,
                                            fileName, groupId, privateLayout,
                                            parameterMap, file);
                        } else {
                            SessionMessages.clear(actionRequest);
                        }
                    } catch (DocumentException de) {

                        actionResponse.setRenderParameter("mvcPath",
                                "/html/import.jsp");

                        _log.error(de);

                        throw new MeasurementXMLException();
                    }
                } else if ("xls".equals(extension)) {
                    
                    // TODO: preprocess upload: 
                    // -- check readability
                    // -- count measurements

                    message = PortletUtil
                            .translate("importing-measurements-from-xls-the-import-will-finish-in-a-separate-thread");

                    MeasurementServiceUtil.importMeasurementsInBackground(
                            userId, fileName, groupId, privateLayout,
                            parameterMap, file);

                }

                SessionMessages
                        .add(actionRequest, "request_processed", message);

            } else {

                actionResponse
                        .setRenderParameter("mvcPath", "/html/import.jsp");
                SessionErrors.add(actionRequest, "file-not-found");

            }

        } else {

            _log.error("unsupported format: " + extension);

            actionResponse.setRenderParameter("mvcPath", "/html/import.jsp");

            throw new FileFormatException();
        }

    }

    /**
     *
     */
    @Override
    public void serveResource(ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws PortletException,
            IOException {

        try {

            String resourceID = resourceRequest.getResourceID();

            if ("getJSON".equals(resourceID)) {
                getJSON(resourceRequest, resourceResponse);
            }
        } catch (Exception e) {
            _log.error(e);
        }
    }

    /**
     * Disable the get- / sendRedirect feature of LiferayPortlet.
     */
    @Override
    protected String getRedirect(ActionRequest actionRequest,
            ActionResponse actionResponse) {

        return null;
    }

    private static Log _log = LogFactoryUtil.getLog(DataManagerPortlet.class
            .getName());

}
