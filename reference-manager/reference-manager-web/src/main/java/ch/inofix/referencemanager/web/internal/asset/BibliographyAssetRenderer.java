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
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.permission.BibliographyPermission;
import ch.inofix.referencemanager.web.internal.constants.BibliographyWebKeys;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-01 12:50
 * @modified 2016-12-26 01:06
 * @version 1.0.5
 *
 */
public class BibliographyAssetRenderer extends BaseJSPAssetRenderer<Bibliography> {

    public BibliographyAssetRenderer(Bibliography bibliography) {
        _bibliography = bibliography;
    }

    @Override
    public Bibliography getAssetObject() {
        return _bibliography;
    }

    @Override
    public String getClassName() {
        return Bibliography.class.getName();
    }

    @Override
    public long getClassPK() {
        return _bibliography.getBibliographyId();
    }

    @Override
    public long getGroupId() {
        return _bibliography.getGroupId();
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
        AssetRendererFactory<Bibliography> assetRendererFactory = getAssetRendererFactory();

        return assetRendererFactory.getPortletId();
    }

    @Override
    public int getStatus() {
        return _bibliography.getStatus();
    }

    @Override
    public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {

        return _bibliography.getDescription();
    }

    @Override
    public String getTitle(Locale locale) {

        return _bibliography.getTitle();
    }

    public String getType() {
        return BibliographyAssetRendererFactory.TYPE;
    }

    @Override
    public PortletURL getURLEdit(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse) throws Exception {

        PortletURL portletURL = locateBibliographyManager(liferayPortletRequest);

        portletURL.setParameter("tabs1", "settings");

        return portletURL;
    }

    @Override
    public String getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) {

        try {

            long portletPlid = PortalUtil.getPlidFromPortletId(_bibliography.getGroupId(), false,
                    PortletKeys.BIBLIOGRAPHY_MANAGER);

            PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(portletPlid,
                    PortletKeys.BIBLIOGRAPHY_MANAGER, PortletRequest.RENDER_PHASE);

            portletURL.setParameter("mvcPath", "/edit_bibliography.jsp");

            portletURL.setParameter("bibliographyId", String.valueOf(_bibliography.getBibliographyId()));

            return portletURL.toString();

        } catch (Exception e) {
            _log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) {

        try {

            PortletURL portletURL = locateBibliographyManager(liferayPortletRequest);

            return portletURL.toString();

        } catch (Exception e) {
            _log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public long getUserId() {
        return _bibliography.getUserId();
    }

    @Override
    public String getUserName() {
        return _bibliography.getUserName();
    }

    @Override
    public String getUuid() {
        return _bibliography.getUuid();
    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker) {

        return BibliographyPermission.contains(permissionChecker, _bibliography, ActionKeys.VIEW);
    }

    @Override
    public boolean include(HttpServletRequest request, HttpServletResponse response, String template) throws Exception {

        request.setAttribute(BibliographyWebKeys.BIBLIOGRAPHY, _bibliography);

        return super.include(request, response, template);
    }

    private PortletURL locateBibliographyManager(LiferayPortletRequest liferayPortletRequest) throws PortalException {

        long portletPlid = PortalUtil.getPlidFromPortletId(_bibliography.getGroupId(), false,
                PortletKeys.BIBLIOGRAPHY_MANAGER);

        PortletURL portletURL = PortletURLFactoryUtil.create(liferayPortletRequest, PortletKeys.BIBLIOGRAPHY_MANAGER,
                portletPlid, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcPath", "/edit_bibliography.jsp");

        portletURL.setParameter("bibliographyId", String.valueOf(_bibliography.getBibliographyId()));

        return portletURL;
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyAssetRenderer.class);

    private final Bibliography _bibliography;

}
