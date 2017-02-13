package ch.inofix.referencemanager.web.internal.portlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import aQute.bnd.annotation.metatype.Configurable;
import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.exception.NoSuchBibliographyException;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.BibRefRelationService;
import ch.inofix.referencemanager.service.BibliographyService;
import ch.inofix.referencemanager.service.ReferenceService;
import ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration;
import ch.inofix.referencemanager.web.internal.constants.BibliographyWebKeys;
import ch.inofix.referencemanager.web.internal.portlet.util.PortletUtil;

/**
 * View Controller of Inofix' bibliography-manager.
 * 
 * @author Christian Berndt
 * @created 2016-11-29 22:33
 * @modified 2017-02-13 22:50
 * @version 1.2.5
 */
@Component(
    configurationPid = "ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration",
    immediate = true, 
    property = { 
        "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.css-class-wrapper=bibliography-manager-portlet",
        "com.liferay.portlet.display-category=category.inofix", 
        "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.instanceable=false", 
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/user_bibliographies.jsp",
        "javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER, 
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" 
    }, 
    service = Portlet.class
)
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

        actionResponse.setRenderParameter("postDelete", "true");

    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.1.5
     * @throws Exception
     */
    public void deleteBibRefRelation(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");
        long bibliographyId = ParamUtil.getLong(actionRequest, "bibliographyId");

        _bibRefRelationService.deleteBibRefRelation(bibliographyId, referenceId);

        actionResponse.setRenderParameter("bibliographyId", String.valueOf(bibliographyId));
        actionResponse.setRenderParameter("mvcPath", "/edit_bibliography.jsp");

    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.1.5
     * @throws Exception
     */
    public void deleteReference(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");

        _referenceService.deleteReference(referenceId);

        String bibliographyId = ParamUtil.getString(actionRequest, "bibliographyId");

        actionResponse.setRenderParameter("bibliographyId", bibliographyId);
        actionResponse.setRenderParameter("mvcPath", "/edit_bibliography.jsp");

    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void importBibliography(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil.getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        File file = uploadPortletRequest.getFile("file");
        String fileName = file.getName();

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        boolean privateLayout = themeDisplay.getLayout().isPrivateLayout();

        Map<String, String[]> parameterMap = request.getParameterMap();

        if (Validator.isNotNull(file)) {

            String message = PortletUtil.translate("upload-successfull-import-will-finish-in-a-separate-thread");
            ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(),
                    uploadPortletRequest);

            _referenceService.importReferencesInBackground(userId, fileName, groupId, privateLayout, parameterMap, file,
                    serviceContext);

            SessionMessages.add(actionRequest, "request_processed", message);

        } else {

            SessionErrors.add(actionRequest, "file-not-found");

        }
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

        renderRequest.setAttribute(BibliographyManagerConfiguration.class.getName(),
                _bibliographyManagerConfiguration);

        super.render(renderRequest, renderResponse);
    }

    @Override
    public void sendRedirect(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {

        // Disable the default sendRedirect-behaviour of LiferayPortlet in order
        // to pass renderParameters via actionResponse's setRenderParameter()
        // methods.
    }

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws PortletException {

        try {
            String resourceID = resourceRequest.getResourceID();

            if (resourceID.equals("exportBibliography")) {
                exportBibliography(resourceRequest, resourceResponse);
            }
        } catch (Exception e) {
            throw new PortletException(e);
        }
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

        // Only available in imported database, see
        // ch.inofix.referencemanager.service.util.ReferenceImporter
        String comments = null;
        String preamble = null;
        String strings = null;

        if (bibliographyId <= 0) {
            bibliography = _bibliographyService.addBibliography(userId, title, description, urlTitle, comments,
                    preamble, strings, serviceContext);
        } else {
            bibliography = _bibliographyService.updateBibliography(bibliographyId, userId, title, description, urlTitle,
                    comments, preamble, strings, serviceContext);
        }

        String redirect = getEditBibliographyURL(actionRequest, actionResponse, bibliography);
        String tabs1 = ParamUtil.get(actionRequest, "tabs1", "settings");

        actionRequest.setAttribute(WebKeys.REDIRECT, redirect);
        actionRequest.setAttribute(BibliographyWebKeys.BIBLIOGRAPHY, bibliography);
        actionResponse.setRenderParameter("tabs1", tabs1);

    }
    
    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        _bibliographyManagerConfiguration = Configurable.createConfigurable(BibliographyManagerConfiguration.class,
                properties);
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
    
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        renderRequest.setAttribute(BibliographyManagerConfiguration.class.getName(),
                _bibliographyManagerConfiguration);

        super.doView(renderRequest, renderResponse);
    }

    /**
     * 
     * @param resourceRequest
     * @param resourceResponse
     * @since 1.1.6
     * @throws PortalException
     * @throws IOException
     */
    protected void exportBibliography(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws PortalException, IOException {

        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long bibliographyId = ParamUtil.getLong(resourceRequest, "bibliographyId");

        Bibliography bibliography = _bibliographyService.getBibliography(bibliographyId);

        String fileName = bibliography.getTitle() + ".bib";

        Hits hits = _referenceService.search(themeDisplay.getUserId(), 0, null, bibliography.getBibliographyId(), 0,
                Integer.MAX_VALUE, null);

        StringBuilder sb = new StringBuilder();

        sb.append(StringPool.PERCENT);
        sb.append(" Encoding: UTF-8");
        sb.append(StringPool.NEW_LINE);
        sb.append(StringPool.NEW_LINE);

        sb.append(bibliography.getPreamble());

        sb.append(bibliography.getStrings());

        List<Document> documents = hits.toList();

        for (Document document : documents) {

            long referenceId = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

            Reference reference = _referenceService.getReference(referenceId);
            String bibTeX = reference.getBibTeX();

            sb.append(bibTeX);
            sb.append(StringPool.NEW_LINE);

        }

        sb.append(bibliography.getComments());

        String export = sb.toString();

        PortletResponseUtil.sendFile(resourceRequest, resourceResponse, fileName, export.getBytes(),
                ContentTypes.TEXT_PLAIN_UTF8);

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
                templatePath + "edit_bibliography.jsp");
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
        boolean postDelete = ParamUtil.getBoolean(portletRequest, "postDelete");

        if (bibliographyId <= 0 || postDelete) {
            return;
        }

        Bibliography bibliography = _bibliographyService.getBibliography(bibliographyId);

        portletRequest.setAttribute(BibliographyWebKeys.BIBLIOGRAPHY, bibliography);
    }

    @org.osgi.service.component.annotations.Reference
    protected void setBibliographyService(BibliographyService bibliographyService) {
        this._bibliographyService = bibliographyService;
    }

    @org.osgi.service.component.annotations.Reference
    protected void setBibRefRelationService(BibRefRelationService bibRefRelationService) {
        this._bibRefRelationService = bibRefRelationService;
    }

    @org.osgi.service.component.annotations.Reference
    protected void setReferenceService(ReferenceService referenceService) {
        this._referenceService = referenceService;
    }

    private BibliographyService _bibliographyService;
    private BibRefRelationService _bibRefRelationService;
    private ReferenceService _referenceService;
    
    private volatile BibliographyManagerConfiguration _bibliographyManagerConfiguration;

    private static final String REQUEST_PROCESSED = "request_processed";

    private static Log _log = LogFactoryUtil.getLog(BibliographyManagerPortlet.class.getName());

}
