package ch.inofix.referencemanager.web.internal.asset;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.constants.BibliographyActionKeys;
import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyLocalService;
import ch.inofix.referencemanager.service.permission.BibliographyManagerPortletPermission;
import ch.inofix.referencemanager.service.permission.BibliographyPermission;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-01 12:56
 * @modified 2016-12-15 17:28
 * @version 1.0.4
 *
 */
@Component(immediate = true, property = {
        "javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER }, service = AssetRendererFactory.class)
public class BibliographyAssetRendererFactory extends BaseAssetRendererFactory<Bibliography> {

    public static final String TYPE = "bibliography";

    public BibliographyAssetRendererFactory() {
        setClassName(Bibliography.class.getName());
        setLinkable(true);
        setPortletId(PortletKeys.BIBLIOGRAPHY_MANAGER);
        setSearchable(true);
    }

    @Override
    public AssetRenderer<Bibliography> getAssetRenderer(long classPK, int type) throws PortalException {

        Bibliography bibliography = _bibliographyLocalService.getBibliography(classPK);

        BibliographyAssetRenderer bibliographyAssetRenderer = new BibliographyAssetRenderer(bibliography);

        bibliographyAssetRenderer.setAssetRendererType(type);
        bibliographyAssetRenderer.setServletContext(_servletContext);

        return bibliographyAssetRenderer;

    }

    @Override
    public String getClassName() {
        return Bibliography.class.getName();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse) throws PortalException {

        ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);

        User user = themeDisplay.getUser();

        Group group = user.getGroup();

        if (group != null) {

            long portletPlid = PortalUtil.getPlidFromPortletId(group.getGroupId(), false,
                    PortletKeys.BIBLIOGRAPHY_MANAGER);

            PortletURL portletURL = PortletURLFactoryUtil.create(liferayPortletRequest,
                    PortletKeys.BIBLIOGRAPHY_MANAGER, portletPlid, PortletRequest.RENDER_PHASE);

            portletURL.setParameter("mvcPath", "/edit_bibliography.jsp");

            String redirect = (String) liferayPortletRequest.getAttribute("redirect");
            
            if (Validator.isNotNull(redirect)) {
                portletURL.setParameter("redirect", redirect);
            }

            return portletURL;
            
        } else {
            
            return null;
            
        }
    }

    @Override
    public boolean hasAddPermission(PermissionChecker permissionChecker, long groupId, long classTypeId)
            throws Exception {

        return BibliographyManagerPortletPermission.contains(permissionChecker, groupId,
                BibliographyActionKeys.ADD_BIBLIOGRAPHY);
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker, long classPK, String actionId) throws Exception {

        Bibliography bibliography = _bibliographyLocalService.getBibliography(classPK);

        return BibliographyPermission.contains(permissionChecker, bibliography.getBibliographyId(), actionId);
    }

    @Reference(target = "(osgi.web.symbolicname=reference-manager-web)", unbind = "-")
    public void setServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
    }

    @Reference(unbind = "-")
    protected void setBibliographyLocalService(BibliographyLocalService bibliographyLocalService) {

        _bibliographyLocalService = bibliographyLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyAssetRendererFactory.class);

    private BibliographyLocalService _bibliographyLocalService;
    private ServletContext _servletContext;

}
