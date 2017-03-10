package ch.inofix.portlet.data.portlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.data.service.MeasurementServiceUtil;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-08 19:58
 * @modified 2017-03-10 09:34
 * @version 1.0.1
 *
 */
public class DataManagerPortlet extends MVCPortlet {

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

        File file = uploadPortletRequest.getFile("file");
        String fileName = file.getName();

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

            Document document = SAXReaderUtil.read(file);
            List<Node> nodes = document.selectNodes("/");

            String message = PortletUtil
                    .translate("no-measurements-were-found");

            if (nodes.size() > 0) {

                message = PortletUtil
                        .translate(
                                "received-x-measurements-the-import-will-finish-in-separate-thread",
                                nodes.size());

                MeasurementServiceUtil.importMeasurementsInBackground(userId,
                        fileName, groupId, privateLayout, parameterMap, file);
            }

            SessionMessages.add(actionRequest, "request_processed", message);

        } else {

            SessionErrors.add(actionRequest, "file-not-found");

        }

    }

}
