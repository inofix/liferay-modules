package ch.inofix.referencemanager.web.internal.asset;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.referencemanager.constants.ReferencePortletKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.permission.ReferencePermission;
import ch.inofix.referencemanager.web.internal.constants.ReferenceWebKeys;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-19 19:56
 * @modified 2016-11-19 19:56
 * @version 1.0.0
 *
 */
public class ReferenceAssetRenderer extends BaseJSPAssetRenderer<Reference> {

    public ReferenceAssetRenderer(Reference reference) {
        _reference = reference;
    }

    @Override
    public Reference getAssetObject() {
        return _reference;
    }

    @Override
    public String getClassName() {
        return Reference.class.getName();
    }

    @Override
    public long getClassPK() {
        return _reference.getReferenceId();
    }

    @Override
    public long getGroupId() {
        return _reference.getGroupId();
    }

    @Override
    public String getJspPath(HttpServletRequest request, String template) {
        if (template.equals(TEMPLATE_ABSTRACT) || template.equals(TEMPLATE_FULL_CONTENT)) {

            _log.info("getJspPath()");
            _log.info("template = " + template);

            return "/asset/" + template + ".jsp";
        } else {
            return null;
        }
    }

    public String getPortletId() {
        AssetRendererFactory<Reference> assetRendererFactory = getAssetRendererFactory();

        return assetRendererFactory.getPortletId();
    }

    @Override
    public int getStatus() {
        return _reference.getStatus();
    }

    @Override
    public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {
        return "Reference Summary";
        // return _reference.getCitation();
    }

    @Override
    public String getTitle(Locale locale) {
        return "Reference Title";
        // return _reference.getTitle();
    }

    public String getType() {
        return ReferenceAssetRendererFactory.TYPE;
    }

    @Override
    public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) {

        _log.info("getURLViewInContext()");

        try {
            PortletURL portletURL = liferayPortletResponse.createRenderURL(ReferencePortletKeys.REFERENCE_MANAGER);

            portletURL.setParameter("mvcPath", "/view_reference.jsp");
            portletURL.setParameter("referenceId", String.valueOf(_reference.getReferenceId()));
            portletURL.setWindowState(WindowState.MAXIMIZED);

            return portletURL.toString();

        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public long getUserId() {
        return _reference.getUserId();
    }

    @Override
    public String getUserName() {
        return _reference.getUserName();
    }

    @Override
    public String getUuid() {
        return _reference.getUuid();

    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker) {

        _log.info("hasViewPermission()");
        return true;
        // TODO
//        return ReferencePermission.contains(permissionChecker, _reference, ActionKeys.VIEW);
    }

    @Override
    public boolean include(HttpServletRequest request, HttpServletResponse response, String template) throws Exception {

        _log.info("include()");

        request.setAttribute(ReferenceWebKeys.REFERENCE, _reference);

        return super.include(request, response, template);
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceAssetRenderer.class);

    private final Reference _reference;

}
