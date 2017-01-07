package ch.inofix.referencemanager.web.internal.asset;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.PortalUtil;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.permission.ReferencePermission;
import ch.inofix.referencemanager.web.internal.constants.ReferenceWebKeys;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-19 19:56
 * @modified 2017-01-07 00:04
 * @version 1.0.3
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
        return null;
    }

    @Override
    public String getTitle(Locale locale) {
        return _reference.getCitation();
    }

    public String getType() {
        return ReferenceAssetRendererFactory.TYPE;
    }

    @Override
    public PortletURL getURLEdit(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse) throws Exception {

        PortletURL portletURL = locateReferenceEditor(liferayPortletRequest);

        return portletURL;

    }

    @Override
    public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) {

        try {

            PortletURL portletURL = locateReferenceEditor(liferayPortletRequest);

            return portletURL.toString();

        } catch (Exception e) {
            _log.error(e);
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

        return ReferencePermission.contains(permissionChecker, _reference, ActionKeys.VIEW);
    }

    @Override
    public boolean include(HttpServletRequest request, HttpServletResponse response, String template) throws Exception {
        request.setAttribute(ReferenceWebKeys.REFERENCE, _reference);

        return super.include(request, response, template);
    }

    private PortletURL locateReferenceEditor(LiferayPortletRequest liferayPortletRequest) throws PortalException {

        long portletPlid = PortalUtil.getPlidFromPortletId(_reference.getGroupId(), false,
                PortletKeys.REFERENCE_EDITOR);

        PortletURL portletURL = PortletURLFactoryUtil.create(liferayPortletRequest, PortletKeys.REFERENCE_EDITOR,
                portletPlid, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("referenceId", String.valueOf(_reference.getReferenceId()));

        return portletURL;
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceAssetRenderer.class);

    private final Reference _reference;

}
