/**
 * Copyright 2016-present Inofix GmbH, Luzern.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.inofix.referencemanager.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.osgi.service.component.annotations.Component;

import com.liferay.document.library.kernel.exception.FileSizeException;
import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.exception.NoSuchReferenceException;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceService;
import ch.inofix.referencemanager.web.internal.constants.ReferenceWebKeys;
import ch.inofix.referencemanager.web.internal.portlet.util.PortletUtil;
import ch.inofix.referencemanager.web.util.BibTeXUtil;

import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

/**
 * View Controller of Inofix' reference-manager.
 * 
 * @author Christian Berndt
 * @created 2016-04-10 22:32
 * @modified 2016-11-29 21:20
 * @version 1.0.6
 */
@Component(immediate = true, property = { "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.css-class-wrapper=reference-manager-portlet",
        "com.liferay.portlet.display-category=category.inofix", "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.instanceable=false", "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp", "javax.portlet.name=" + PortletKeys.REFERENCE_MANAGER,
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class ReferenceManagerPortlet extends MVCPortlet {

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.4
     * @throws Exception
     */
    public void deleteAllReferences(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        List<Reference> references = _referenceService.deleteGroupReferences(serviceContext.getScopeGroupId());

        SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                PortletUtil.translate("successfully-deleted-x-task-records", references.size()));

        actionResponse.setRenderParameter("tabs1", tabs1);
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void deleteReference(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");

        _referenceService.deleteReference(referenceId);
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void importBibTeXFile(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();

        String sourceFileName = uploadPortletRequest.getFileName("file");

        StringBundler sb = new StringBundler(5);

        String extension = FileUtil.getExtension(sourceFileName);

        if (Validator.isNotNull(extension)) {
            sb.append(StringPool.PERIOD);
            sb.append(extension);
        }

        InputStream inputStream = null;

        try {

            inputStream = uploadPortletRequest.getFileAsStream("file");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            BibTeXParser bibTeXParser = new BibTeXParser();

            BibTeXDatabase database = bibTeXParser.parse(bufferedReader);

            Collection<BibTeXEntry> bibTeXEntries = database.getEntries().values();

            for (BibTeXEntry bibTeXEntry : bibTeXEntries) {

                String bibTeX = "";

                bibTeX = BibTeXUtil.format(bibTeXEntry);

                // TODO: check whether a reference with the same uid has
                // already been uploaded

                // TODO: perform upload in background thread

                _referenceService.addReference(userId, bibTeX, serviceContext);

            }

        } catch (Exception e) {
            UploadException uploadException = (UploadException) actionRequest.getAttribute(WebKeys.UPLOAD_EXCEPTION);

            // if ((uploadException != null) &&
            // (uploadException.getCause() instanceof
            // FileUploadBase.IOFileUploadException)) {
            //
            // // Cancelled a temporary upload
            //
            // }
            // else if ((uploadException != null) &&
            if ((uploadException != null) && uploadException.isExceededSizeLimit()) {

                throw new FileSizeException(uploadException.getCause());

            } else {

                // TODO: What else can go wrong?
                // - the uploaded file is not BibTeX-Database
                // - the uploaded file contains syntax errors

                throw e;
            }
        } finally {
            StreamUtil.cleanUp(inputStream);
        }

    }

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            getReference(renderRequest);
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
    public void updateReference(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");

        String bibTeX = ParamUtil.getString(actionRequest, "bibTeX");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        long userId = serviceContext.getUserId();

        Reference reference = null;

        if (referenceId <= 0) {
            reference = _referenceService.addReference(userId, bibTeX, serviceContext);
        } else {
            reference = _referenceService.updateReference(referenceId, userId, bibTeX, serviceContext);
        }

        String redirect = getEditReferenceURL(actionRequest, actionResponse, reference);

        actionRequest.setAttribute(WebKeys.REDIRECT, redirect);
    }

    /**
     * 
     */
    @Override
    protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        if (SessionErrors.contains(renderRequest, PrincipalException.getNestedClasses())
                || SessionErrors.contains(renderRequest, NoSuchReferenceException.class)) {
            include("/error.jsp", renderRequest, renderResponse);
        } else {
            super.doDispatch(renderRequest, renderResponse);
        }
    }

    protected String getEditReferenceURL(ActionRequest actionRequest, ActionResponse actionResponse,
            Reference reference) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String editReferenceURL = getRedirect(actionRequest, actionResponse);

        if (Validator.isNull(editReferenceURL)) {
            editReferenceURL = PortalUtil.getLayoutFullURL(themeDisplay);
        }

        String namespace = actionResponse.getNamespace();
        String windowState = actionResponse.getWindowState().toString();

        editReferenceURL = HttpUtil.setParameter(editReferenceURL, "p_p_id", PortletKeys.REFERENCE_MANAGER);
        editReferenceURL = HttpUtil.setParameter(editReferenceURL, "p_p_state", windowState);
        editReferenceURL = HttpUtil.setParameter(editReferenceURL, namespace + "mvcPath",
                templatePath + "edit_reference.jsp");
        editReferenceURL = HttpUtil.setParameter(editReferenceURL, namespace + "redirect",
                getRedirect(actionRequest, actionResponse));
        editReferenceURL = HttpUtil.setParameter(editReferenceURL, namespace + "backURL",
                ParamUtil.getString(actionRequest, "backURL"));
        editReferenceURL = HttpUtil.setParameter(editReferenceURL, namespace + "referenceId",
                reference.getReferenceId());

        return editReferenceURL;
    }

    /**
     * 
     * @param portletRequest
     * @throws Exception
     */
    protected void getReference(PortletRequest portletRequest) throws Exception {

        long referenceId = ParamUtil.getLong(portletRequest, "referenceId");

        if (referenceId <= 0) {
            return;
        }

        Reference reference = _referenceService.getReference(referenceId);

        portletRequest.setAttribute(ReferenceWebKeys.REFERENCE, reference);
    }

    @org.osgi.service.component.annotations.Reference
    protected void setReferenceService(ReferenceService referenceService) {
        this._referenceService = referenceService;
    }

    private ReferenceService _referenceService;

    private static final String REQUEST_PROCESSED = "request_processed";

    private static Log _log = LogFactoryUtil.getLog(ReferenceManagerPortlet.class.getName());

}
