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
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.osgi.service.component.annotations.Component;

import com.liferay.document.library.kernel.exception.FileSizeException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalService;
import ch.inofix.referencemanager.web.util.BibTeXUtil;

import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Christian Berndt
 * @created 2016-04-10 22:32
 * @modified 2016-11-18 19:32
 * @version 1.0.2
 */
@Component(immediate = true, property = { "com.liferay.portlet.css-class-wrapper=reference-manager-portlet",
        "com.liferay.portlet.display-category=category.inofix", "com.liferay.portlet.instanceable=false",
        "javax.portlet.security-role-ref=power-user,user", "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.resource-bundle=content.Language" }, service = Portlet.class)
public class ReferenceManagerPortlet extends MVCPortlet {

    @Override
    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
            throws IOException, PortletException {

        try {
            String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
            String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                updateReference(actionRequest);
            } else if (cmd.equals("importBibTeXFile")) {
                importBibTeXFile(actionRequest);
            } else if (cmd.equals("deleteAllReferences")) {
                deleteAllReferences(actionRequest);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteReference(actionRequest);
            }

            if (Validator.isNotNull(cmd)) {
                if (SessionErrors.isEmpty(actionRequest)) {
                    SessionMessages.add(actionRequest, "requestProcessed");
                }

                actionResponse.setRenderParameter("tabs1", tabs1);

            }
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }

    @Override
    public void render(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        request.setAttribute("referenceLocalService", getReferenceLocalService());

        super.render(request, response);
    }

    protected void deleteAllReferences(ActionRequest actionRequest) throws Exception {

        // TODO: use remote service
        List<Reference> references = getReferenceLocalService().getReferences(0, Integer.MAX_VALUE);

        for (Reference reference : references) {

            getReferenceLocalService().deleteReference(reference);
        }
    }

    protected void deleteReference(ActionRequest actionRequest) throws Exception {

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");

        getReferenceLocalService().deleteReference(referenceId);
    }

    protected void importBibTeXFile(ActionRequest actionRequest) throws Exception {

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

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

            _log.info("database.getEntries().size() = " + database.getEntries().size());

            Collection<BibTeXEntry> entries = database.getEntries().values();

            for (BibTeXEntry bibTeXEntry : entries) {

                String bibTeX = BibTeXUtil.format(bibTeXEntry);

                // TODO: check whether a reference with the same uid has
                // already been uploaded

                // TODO: perform upload in background thread

                getReferenceLocalService().addReference(userId, groupId, bibTeX, serviceContext);

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

    protected void updateReference(ActionRequest actionRequest) throws Exception {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long referenceId = ParamUtil.getLong(actionRequest, "referenceId");
        String bibTeX = ParamUtil.getString(actionRequest, "bibTeX");

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        if (referenceId <= 0) {

            // TODO: use remote service
            getReferenceLocalService().addReference(userId, groupId, bibTeX, serviceContext);
        } else {

            // TODO: use remote service
            //

        }
    }

    public ReferenceLocalService getReferenceLocalService() {

        return _referenceLocalService;
    }

    @org.osgi.service.component.annotations.Reference
    public void setReferenceLocalService(ReferenceLocalService referenceLocalService) {

        this._referenceLocalService = referenceLocalService;
    }

    private ReferenceLocalService _referenceLocalService;

    private static Log _log = LogFactoryUtil.getLog(ReferenceManagerPortlet.class.getName());

}
