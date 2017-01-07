package ch.inofix.referencemanager.web.internal.asset;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;

import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalService;
import ch.inofix.referencemanager.service.permission.ReferencePermission;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-18 21:49
 * @modified 2017-01-07 12:54
 * @version 1.0.1
 *
 */
@Component(
    immediate = true, 
    property = { "javax.portlet.name=" + PortletKeys.REFERENCE_MANAGER }, 
    service = AssetRendererFactory.class
)
public class ReferenceAssetRendererFactory extends BaseAssetRendererFactory<Reference> {

    public static final String TYPE = "reference";

    public ReferenceAssetRendererFactory() {
        setLinkable(true);
        setClassName(Reference.class.getName());
        setPortletId(PortletKeys.REFERENCE_MANAGER);
        setSearchable(true);
    }

    @Override
    public AssetRenderer<Reference> getAssetRenderer(long classPK, int type) throws PortalException {

        Reference reference = _referenceLocalService.getReference(classPK);

        ReferenceAssetRenderer referenceAssetRenderer = new ReferenceAssetRenderer(reference);

        referenceAssetRenderer.setAssetRendererType(type);
        referenceAssetRenderer.setServletContext(_servletContext);

        return referenceAssetRenderer;

    }

    @Override
    public String getClassName() {
        return Reference.class.getName();
    }

    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, long classTypeId) {

        long groupId = GetterUtil.getLong(PropsUtil.get("reference.common.group"));

        PortletURL portletURL = null;

        if (groupId > 0) {

            try {
                
                long portletPlid = PortalUtil.getPlidFromPortletId(groupId, false, PortletKeys.REFERENCE_EDITOR);

                portletURL = PortletURLFactoryUtil.create(liferayPortletRequest, PortletKeys.REFERENCE_EDITOR,
                        portletPlid, PortletRequest.RENDER_PHASE);
                
            } catch (Exception e) {
                _log.error(e);
            }

        } else {
            portletURL = PortalUtil.getControlPanelPortletURL(liferayPortletRequest, PortletKeys.REFERENCE_EDITOR,
                    PortletRequest.RENDER_PHASE);
        }

        return portletURL;
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker, long classPK, String actionId) throws Exception {

        Reference reference = _referenceLocalService.getReference(classPK);

        return ReferencePermission.contains(permissionChecker, reference.getReferenceId(), actionId);
    }

    @org.osgi.service.component.annotations.Reference(target = "(osgi.web.symbolicname=reference-manager-web)", unbind = "-")
    public void setServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
    }

    @org.osgi.service.component.annotations.Reference(unbind = "-")
    protected void setReferenceLocalService(ReferenceLocalService referenceLocalService) {

        _referenceLocalService = referenceLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceAssetRendererFactory.class);

    private ReferenceLocalService _referenceLocalService;
    private ServletContext _servletContext;

}
