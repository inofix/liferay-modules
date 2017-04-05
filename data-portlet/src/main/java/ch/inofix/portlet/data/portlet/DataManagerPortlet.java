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
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
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
 * @modified 2017-04-05 12:29
 * @version 1.0.7
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

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Measurement.class.getName(), actionRequest);

        List<Measurement> measurements = MeasurementServiceUtil
                .deleteGroupMeasurements(serviceContext.getScopeGroupId());

        SessionMessages.add(actionRequest, "request_processed", PortletUtil
                .translate("successfully-deleted-x-measurements",
                        measurements.size()));

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

        String channelId = ParamUtil.getString(resourceRequest, "channelId"); // channel
                                                                              // by
                                                                              // id
        String channelName = ParamUtil
                .getString(resourceRequest, "channelName"); // channel by name
        long from = ParamUtil.getLong(resourceRequest, "from"); // begin of
                                                                // selected
                                                                // interval
        int limit = ParamUtil.getInteger(resourceRequest, "limit", 1000); // maximum
                                                                          // number
                                                                          // of
                                                                          // data
                                                                          // points
        long until = ParamUtil.getLong(resourceRequest, "until"); // end of
                                                                  // selected
                                                                  // interval

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

        String dataURL = ParamUtil.getString(uploadPortletRequest, "dataURL");

        File file = uploadPortletRequest.getFile("file");

        if (Validator.isNotNull(dataURL)) {

            URL url = new URL(dataURL);
            file = new File(dataURL);
            FileUtils.copyURLToFile(url, file);

        }

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        boolean privateLayout = themeDisplay.getLayout().isPrivateLayout();

        String servletContextName = request.getSession().getServletContext()
                .getServletContextName();

        String[] servletContextNames = new String[] { servletContextName };

        Map<String, String[]> parameterMap = new HashMap<String, String[]>(
                actionRequest.getParameterMap());
        parameterMap.put("servletContextNames", servletContextNames);

        if (Validator.isNotNull(file)) {

            String fileName = file.getName();

            com.liferay.portal.kernel.xml.Document document = SAXReaderUtil
                    .read(file);
            List<Node> nodes = document.selectNodes("/");

            String message = PortletUtil
                    .translate("no-measurements-were-found");

            if (nodes.size() > 0) {

                message = PortletUtil
                        .translate(
                                "found-x-measurements-the-import-will-finish-in-separate-thread",
                                nodes.size());

                MeasurementServiceUtil.importMeasurementsInBackground(userId,
                        fileName, groupId, privateLayout, parameterMap, file);
            }

            SessionMessages.add(actionRequest, "request_processed", message);

            String mvcPath = ParamUtil.getString(uploadPortletRequest,
                    "mvcPath");

            actionResponse.setRenderParameter("mvcPath", mvcPath);

        } else {

            SessionErrors.add(actionRequest, "file-not-found");

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

    private static Log _log = LogFactoryUtil.getLog(DataManagerPortlet.class
            .getName());

}
