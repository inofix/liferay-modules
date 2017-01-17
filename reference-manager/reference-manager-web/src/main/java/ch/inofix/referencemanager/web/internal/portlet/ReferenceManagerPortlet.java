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

package ch.inofix.referencemanager.web.internal.portlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

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
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.exception.NoSuchReferenceException;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceService;
import ch.inofix.referencemanager.setup.SampleDataUtil;
import ch.inofix.referencemanager.web.internal.constants.ReferenceWebKeys;
import ch.inofix.referencemanager.web.internal.portlet.util.PortletUtil;

/**
 * View Controller of Inofix' reference-manager.
 * 
 * @author Christian Berndt
 * @created 2016-04-10 22:32
 * @modified 2017-01-17 15:23
 * @version 1.1.1
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

        List<Reference> references = _referenceService.deleteReferences();

        SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                PortletUtil.translate("successfully-deleted-x-references", references.size()));

        actionResponse.setRenderParameter("tabs1", tabs1);
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.8
     * @throws Exception
     */
    public void deleteGroupReferences(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        List<Reference> references = _referenceService.deleteGroupReferences(serviceContext.getScopeGroupId());

        SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                PortletUtil.translate("successfully-deleted-x-references", references.size()));

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

        _log.info("importBibTeXFile()");

        HttpServletRequest request = PortalUtil.getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        File file = uploadPortletRequest.getFile("file");
        String fileName = file.getName();

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        boolean privateLayout = themeDisplay.getLayout().isPrivateLayout();

        String servletContextName = request.getSession().getServletContext().getServletContextName();

        String[] servletContextNames = new String[] { servletContextName };

        Map<String, String[]> parameterMap = new HashMap<String, String[]>(actionRequest.getParameterMap());
        parameterMap.put("servletContextNames", servletContextNames);

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

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.7
     * @throws Exception
     */
    public void importSampleData(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        SampleDataUtil.importSampleData();

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
