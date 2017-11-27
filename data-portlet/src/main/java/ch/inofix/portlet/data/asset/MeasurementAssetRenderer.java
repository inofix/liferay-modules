
package ch.inofix.portlet.data.asset;

import java.util.Locale;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import ch.inofix.portlet.data.model.Measurement;
//import ch.inofix.portlet.data.service.permission.MeasurementPermission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.asset.model.BaseAssetRenderer;

/**
 * @author Christian Berndt
 * @created 2015-05-19 17:25
 * @modified 2016-03-16 20:50
 * @version 1.0.9
 */
public class MeasurementAssetRenderer extends BaseAssetRenderer {

    private Measurement measurement;

    public MeasurementAssetRenderer(Measurement measurement) {

        this.measurement = measurement;
    }

    @Override
    public String getClassName() {

        return Measurement.class.getName();
    }

    @Override
    public long getClassPK() {

        return measurement.getMeasurementId();
    }

    @Override
    public long getGroupId() {

        return measurement.getGroupId();
    }

    @Override
    public String getSummary(Locale locale) {
        
        return null; 

    }

    @Override
    public String getTitle(Locale locale) {
        
        return String.valueOf(measurement.getMeasurementId());

    }

    @Override
    public PortletURL getURLEdit(
        LiferayPortletRequest liferayPortletRequest,
        LiferayPortletResponse liferayPortletResponse)
        throws Exception {

        PortletURL portletURL =
            liferayPortletResponse.createLiferayPortletURL(
                getControlPanelPlid(liferayPortletRequest),
                "dataportlet_WAR_dataportlet",
                PortletRequest.ACTION_PHASE);

        String backURL = (String) liferayPortletRequest.getAttribute("backURL");

        portletURL.setParameter(
            "measurementId", String.valueOf(measurement.getMeasurementId()));
        portletURL.setParameter("javax.portlet.action", "editMeasurement");
        portletURL.setParameter("mvcPath", "/html/edit_measurement.jsp");
        if (Validator.isNotNull(backURL)) {
            portletURL.setParameter("backURL", backURL);
        }

        return portletURL;

    }

//    @Override
//    public String getUrlTitle() {
//
//        StringBuilder sb = new StringBuilder();
//
//        if (Validator.isNotNull(measurement.getFullName(false))) {
//            sb.append(measurement.getFullName(false).toLowerCase());
//        }
//
//        String urlTitle = sb.toString();
//
//        Pattern _friendlyURLPattern = Pattern.compile("[^a-z0-9_-]");
//
//        urlTitle = FriendlyURLNormalizerUtil.normalize(urlTitle, _friendlyURLPattern);
//
//        return urlTitle;
//
//    }

    @Override
    public String getURLViewInContext(
        LiferayPortletRequest liferayPortletRequest,
        LiferayPortletResponse liferayPortletResponse,
        String noSuchEntryRedirect) {

        try {
            PortletURL portletURL =
                liferayPortletResponse.createActionURL("dataportlet_WAR_dataportlet");

            portletURL.setParameter("mvcPath", "/html/view_measurement.jsp");
            portletURL.setParameter(
                "measurementId", String.valueOf(measurement.getMeasurementId()));
            portletURL.setParameter("javax.portlet.action", "viewMeasurement");
            portletURL.setWindowState(WindowState.MAXIMIZED);

            return portletURL.toString();

        }
        catch (Exception e) {
        }

        return null;

    }

    @Override
    public long getUserId() {

        return measurement.getUserId();
    }

    @Override
    public String getUserName() {

        return measurement.getUserName();
    }

    @Override
    public String getUuid() {

        return measurement.getUuid();
    }

    @Override
    public boolean hasEditPermission(PermissionChecker permissionChecker)
        throws PortalException, SystemException {
        
        return false; 

    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker)
        throws PortalException, SystemException {

        return true; 
    }

    @Override
    public boolean isPrintable() {

        return true;
    }

    @Override
    public String render(
        RenderRequest renderRequest, RenderResponse renderResponse,
        String template)
        throws Exception {
        
        _log.info("render()");

        if (template.equals(TEMPLATE_FULL_CONTENT)) {

            renderRequest.setAttribute("MEASUREMENT", measurement);

            return "/html/asset/" + template + ".jsp";

        }
        else {
            return null;
        }
    }
    
    private static Log _log =
            LogFactoryUtil.getLog(MeasurementAssetRenderer.class.getName());


}
