package ch.inofix.referencemanager.web.internal.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.exception.NoSuchBibliographyException;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyService;
import ch.inofix.referencemanager.web.internal.constants.BibliographyWebKeys;

/**
 * View Controller of Inofix' bibliography-manager.
 * 
 * @author Christian Berndt
 * @created 2016-11-29 22:33
 * @modified 2016-12-01 19:57
 * @version 1.0.2
 */
@Component(immediate = true, property = { "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.css-class-wrapper=bibliography-manager-portlet",
        "com.liferay.portlet.display-category=category.inofix", "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.instanceable=false", "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view_bibliography.jsp",
        "javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER, "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class BibliographyManagerPortlet extends MVCPortlet {

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void deleteBibliography(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long bibliographyId = ParamUtil.getLong(actionRequest, "bibliographyId");

        _bibliographyService.deleteBibliography(bibliographyId);
    }

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            getBibliography(renderRequest);
        } catch (Exception e) {
            if (e instanceof NoSuchResourceException || e instanceof PrincipalException) {

                SessionErrors.add(renderRequest, e.getClass());
            } else {
                throw new PortletException(e);
            }
        }

        super.render(renderRequest, renderResponse);
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void updateBibliography(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long bibliographyId = ParamUtil.getLong(actionRequest, "bibliographyId");

        String title = ParamUtil.getString(actionRequest, "title");
        String description = ParamUtil.getString(actionRequest, "description");
        String urlTitle = ParamUtil.getString(actionRequest, "urlTitle");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Bibliography.class.getName(), actionRequest);

        long userId = serviceContext.getUserId();

        Bibliography bibliography = null;

        if (bibliographyId <= 0) {
            bibliography = _bibliographyService.addBibliography(userId, title, description, urlTitle, serviceContext);
        } else {
            bibliography = _bibliographyService.updateBibliography(bibliographyId, userId, title, description, urlTitle,
                    serviceContext);
        }

        String redirect = getEditBibliographyURL(actionRequest, actionResponse, bibliography);

        actionRequest.setAttribute(WebKeys.REDIRECT, redirect);
    }

    /**
     * 
     */
    @Override
    protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        if (SessionErrors.contains(renderRequest, PrincipalException.getNestedClasses())
                || SessionErrors.contains(renderRequest, NoSuchBibliographyException.class)) {
            include("/error.jsp", renderRequest, renderResponse);
        } else {
            super.doDispatch(renderRequest, renderResponse);
        }
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @param bibliography
     * @return
     * @throws Exception
     */
    protected String getEditBibliographyURL(ActionRequest actionRequest, ActionResponse actionResponse,
            Bibliography bibliography) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String editBibliographyURL = getRedirect(actionRequest, actionResponse);

        if (Validator.isNull(editBibliographyURL)) {
            editBibliographyURL = PortalUtil.getLayoutFullURL(themeDisplay);
        }

        String namespace = actionResponse.getNamespace();
        String windowState = actionResponse.getWindowState().toString();

        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, "p_p_id", PortletKeys.BIBLIOGRAPHY_MANAGER);
        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, "p_p_state", windowState);
        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, namespace + "mvcPath",
                templatePath + "view_bibliography.jsp");
        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, namespace + "redirect",
                getRedirect(actionRequest, actionResponse));
        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, namespace + "backURL",
                ParamUtil.getString(actionRequest, "backURL"));
        editBibliographyURL = HttpUtil.setParameter(editBibliographyURL, namespace + "bibliographyId",
                bibliography.getBibliographyId());

        return editBibliographyURL;
    }

    /**
     * 
     * @param portletRequest
     * @throws Exception
     */
    protected void getBibliography(PortletRequest portletRequest) throws Exception {

        long bibliographyId = ParamUtil.getLong(portletRequest, "bibliographyId");

        if (bibliographyId <= 0) {
            return;
        }

        Bibliography bibliography = _bibliographyService.getBibliography(bibliographyId);

        portletRequest.setAttribute(BibliographyWebKeys.BIBLIOGRAPHY, bibliography);
    }

    @org.osgi.service.component.annotations.Reference
    protected void setBibliographyService(BibliographyService bibliographyService) {
        this._bibliographyService = bibliographyService;
    }

    private BibliographyService _bibliographyService;

    private static final String REQUEST_PROCESSED = "request_processed";

    private static Log _log = LogFactoryUtil.getLog(BibliographyManagerPortlet.class.getName());

}
