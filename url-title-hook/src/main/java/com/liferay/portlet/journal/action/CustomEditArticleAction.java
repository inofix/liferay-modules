package com.liferay.portlet.journal.action;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.AssetCategoryException;
import com.liferay.portlet.asset.AssetTagException;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.dynamicdatamapping.NoSuchStructureException;
import com.liferay.portlet.dynamicdatamapping.NoSuchTemplateException;
import com.liferay.portlet.dynamicdatamapping.StorageFieldRequiredException;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;
import com.liferay.portlet.dynamicdatamapping.util.DDMUtil;
import com.liferay.portlet.journal.ArticleContentException;
import com.liferay.portlet.journal.ArticleContentSizeException;
import com.liferay.portlet.journal.ArticleDisplayDateException;
import com.liferay.portlet.journal.ArticleExpirationDateException;
import com.liferay.portlet.journal.ArticleIdException;
import com.liferay.portlet.journal.ArticleSmallImageNameException;
import com.liferay.portlet.journal.ArticleSmallImageSizeException;
import com.liferay.portlet.journal.ArticleTitleException;
import com.liferay.portlet.journal.ArticleTypeException;
import com.liferay.portlet.journal.ArticleVersionException;
import com.liferay.portlet.journal.DuplicateArticleIdException;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;
import com.liferay.portlet.journal.util.JournalConverterUtil;

public class CustomEditArticleAction extends BaseStrutsPortletAction {

    @Override
    public void processAction(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        _log.info("processAction()");

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

        JournalArticle article = null;
        String oldUrlTitle = StringPool.BLANK;

        try {
            UploadException uploadException = (UploadException) actionRequest
                    .getAttribute(WebKeys.UPLOAD_EXCEPTION);

            if (Validator.isNull(cmd)) {
                return;
            } else if (cmd.equals(Constants.ADD)
                    || cmd.equals(Constants.PREVIEW)
                    || cmd.equals(Constants.TRANSLATE)
                    || cmd.equals(Constants.UPDATE)) {

                Object[] contentAndImages = updateArticle(actionRequest);

                article = (JournalArticle) contentAndImages[0];
                oldUrlTitle = ((String) contentAndImages[1]);
            }
        } catch (Exception e) {
            if (e instanceof NoSuchArticleException
                    || e instanceof NoSuchStructureException
                    || e instanceof NoSuchTemplateException
                    || e instanceof PrincipalException) {

                SessionErrors.add(actionRequest, e.getClass());

                // TODO
                // setForward(actionRequest, "portlet.journal.error");
            } else if (e instanceof ArticleContentException
                    || e instanceof ArticleContentSizeException
                    || e instanceof ArticleDisplayDateException
                    || e instanceof ArticleExpirationDateException
                    || e instanceof ArticleIdException
                    || e instanceof ArticleSmallImageNameException
                    || e instanceof ArticleSmallImageSizeException
                    || e instanceof ArticleTitleException
                    || e instanceof ArticleTypeException
                    || e instanceof ArticleVersionException
                    || e instanceof DuplicateArticleIdException
                    || e instanceof FileSizeException
                    || e instanceof LiferayFileItemException
                    || e instanceof StorageFieldRequiredException) {

                SessionErrors.add(actionRequest, e.getClass());
            } else if (e instanceof AssetCategoryException
                    || e instanceof AssetTagException
                    || e instanceof LocaleException) {

                SessionErrors.add(actionRequest, e.getClass(), e);
            } else {
                throw e;
            }
        }

        originalStrutsPortletAction.processAction(originalStrutsPortletAction,
                portletConfig, actionRequest, actionResponse);
    }

    @Override
    public String render(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        return originalStrutsPortletAction.render(null, portletConfig,
                renderRequest, renderResponse);

    }

    @Override
    public void serveResource(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws Exception {

        originalStrutsPortletAction.serveResource(originalStrutsPortletAction,
                portletConfig, resourceRequest, resourceResponse);

    }

    protected Object[] updateArticle(ActionRequest actionRequest)
            throws Exception {

        _log.info("updateArticle()");

        UploadPortletRequest uploadPortletRequest = PortalUtil
                .getUploadPortletRequest(actionRequest);

        if (_log.isDebugEnabled()) {
            _log.debug("Updating article "
                    + MapUtil.toString(uploadPortletRequest.getParameterMap()));
        }

        String cmd = ParamUtil.getString(uploadPortletRequest, Constants.CMD);

        long groupId = ParamUtil.getLong(uploadPortletRequest, "groupId");
        long folderId = ParamUtil.getLong(uploadPortletRequest, "folderId");
        long classNameId = ParamUtil.getLong(uploadPortletRequest,
                "classNameId");
        long classPK = ParamUtil.getLong(uploadPortletRequest, "classPK");

        String articleId = ParamUtil.getString(uploadPortletRequest,
                "articleId");

        boolean autoArticleId = ParamUtil.getBoolean(uploadPortletRequest,
                "autoArticleId");
        double version = ParamUtil.getDouble(uploadPortletRequest, "version");
        boolean localized = ParamUtil.getBoolean(uploadPortletRequest,
                "localized");

        String defaultLanguageId = ParamUtil.getString(uploadPortletRequest,
                "defaultLanguageId");

        Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

        String toLanguageId = ParamUtil.getString(uploadPortletRequest,
                "toLanguageId");

        Locale toLocale = null;

        String title = StringPool.BLANK;
        String description = StringPool.BLANK;

        if (Validator.isNull(toLanguageId)) {
            title = ParamUtil.getString(uploadPortletRequest, "title_"
                    + defaultLanguageId);
            description = ParamUtil.getString(uploadPortletRequest,
                    "description_" + defaultLanguageId);
        } else {
            toLocale = LocaleUtil.fromLanguageId(toLanguageId);

            title = ParamUtil.getString(uploadPortletRequest, "title_"
                    + toLanguageId);
            description = ParamUtil.getString(uploadPortletRequest,
                    "description_" + toLanguageId);
        }

        String content = ParamUtil.getString(uploadPortletRequest,
                "articleContent");

        Map<String, byte[]> images = new HashMap<String, byte[]>();

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                JournalArticle.class.getName(), uploadPortletRequest);

        String urlTitle = (String) serviceContext.getAttribute("urlTitle");

        _log.info("urlTitle = " + urlTitle);

        String structureId = ParamUtil.getString(uploadPortletRequest,
                "structureId");

        DDMStructure ddmStructure = null;

        if (Validator.isNotNull(structureId)) {
            ddmStructure = DDMStructureLocalServiceUtil.getStructure(
                    PortalUtil.getSiteGroupId(groupId),
                    PortalUtil.getClassNameId(JournalArticle.class),
                    structureId, true);

            String languageId = toLanguageId;

            if (Validator.isNull(languageId)) {
                languageId = defaultLanguageId;
            }

            Locale locale = LocaleUtil.fromLanguageId(languageId);

            Object[] contentAndImages = ActionUtil.getContentAndImages(
                    ddmStructure, locale, serviceContext);

            content = (String) contentAndImages[0];
            images = (HashMap<String, byte[]>) contentAndImages[1];
        }

        Boolean fileItemThresholdSizeExceeded = (Boolean) uploadPortletRequest
                .getAttribute(WebKeys.FILE_ITEM_THRESHOLD_SIZE_EXCEEDED);

        if ((fileItemThresholdSizeExceeded != null)
                && fileItemThresholdSizeExceeded.booleanValue()) {

            throw new ArticleContentSizeException();
        }

        String type = ParamUtil.getString(uploadPortletRequest, "type");
        String templateId = ParamUtil.getString(uploadPortletRequest,
                "templateId");
        String layoutUuid = ParamUtil.getString(uploadPortletRequest,
                "layoutUuid");

        // The target page and the article must belong to the same group

        Layout targetLayout = LayoutLocalServiceUtil
                .fetchLayoutByUuidAndGroupId(layoutUuid, groupId, false);

        if (targetLayout == null) {
            targetLayout = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
                    layoutUuid, groupId, true);
        }

        if (targetLayout == null) {
            layoutUuid = null;
        }

        int displayDateMonth = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateMonth");
        int displayDateDay = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateDay");
        int displayDateYear = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateYear");
        int displayDateHour = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateHour");
        int displayDateMinute = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateMinute");
        int displayDateAmPm = ParamUtil.getInteger(uploadPortletRequest,
                "displayDateAmPm");

        if (displayDateAmPm == Calendar.PM) {
            displayDateHour += 12;
        }

        int expirationDateMonth = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateMonth");
        int expirationDateDay = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateDay");
        int expirationDateYear = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateYear");
        int expirationDateHour = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateHour");
        int expirationDateMinute = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateMinute");
        int expirationDateAmPm = ParamUtil.getInteger(uploadPortletRequest,
                "expirationDateAmPm");
        boolean neverExpire = ParamUtil.getBoolean(uploadPortletRequest,
                "neverExpire");

        if (expirationDateAmPm == Calendar.PM) {
            expirationDateHour += 12;
        }

        int reviewDateMonth = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateMonth");
        int reviewDateDay = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateDay");
        int reviewDateYear = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateYear");
        int reviewDateHour = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateHour");
        int reviewDateMinute = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateMinute");
        int reviewDateAmPm = ParamUtil.getInteger(uploadPortletRequest,
                "reviewDateAmPm");
        boolean neverReview = ParamUtil.getBoolean(uploadPortletRequest,
                "neverReview");

        if (reviewDateAmPm == Calendar.PM) {
            reviewDateHour += 12;
        }

        boolean indexable = ParamUtil.getBoolean(uploadPortletRequest,
                "indexable");

        boolean smallImage = ParamUtil.getBoolean(uploadPortletRequest,
                "smallImage");
        String smallImageURL = ParamUtil.getString(uploadPortletRequest,
                "smallImageURL");
        File smallFile = uploadPortletRequest.getFile("smallFile");

        String articleURL = ParamUtil.getString(uploadPortletRequest,
                "articleURL");

        _log.info("articleURL = " + articleURL);

        serviceContext.setAttribute("defaultLanguageId", defaultLanguageId);

        JournalArticle article = null;
        String oldUrlTitle = StringPool.BLANK;

        if (cmd.equals(Constants.ADD)) {
            Map<Locale, String> titleMap = new HashMap<Locale, String>();

            titleMap.put(defaultLocale, title);

            Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

            descriptionMap.put(defaultLocale, description);

            if (Validator.isNull(structureId)) {
                content = LocalizationUtil.updateLocalization(StringPool.BLANK,
                        "static-content", content, defaultLanguageId,
                        defaultLanguageId, true, localized);
            }

            // Add article

            article = JournalArticleServiceUtil.addArticle(groupId, folderId,
                    classNameId, classPK, articleId, autoArticleId, titleMap,
                    descriptionMap, content, type, structureId, templateId,
                    layoutUuid, displayDateMonth, displayDateDay,
                    displayDateYear, displayDateHour, displayDateMinute,
                    expirationDateMonth, expirationDateDay, expirationDateYear,
                    expirationDateHour, expirationDateMinute, neverExpire,
                    reviewDateMonth, reviewDateDay, reviewDateYear,
                    reviewDateHour, reviewDateMinute, neverReview, indexable,
                    smallImage, smallImageURL, smallFile, images, articleURL,
                    serviceContext);

            AssetPublisherUtil.addAndStoreSelection(actionRequest,
                    JournalArticle.class.getName(),
                    article.getResourcePrimKey(), -1);
        } else {

            // Merge current content with new content

            JournalArticle curArticle = JournalArticleServiceUtil.getArticle(
                    groupId, articleId, version);

            if (Validator.isNull(structureId)) {
                if (!curArticle.isTemplateDriven()) {
                    String curContent = StringPool.BLANK;

                    curContent = curArticle.getContent();

                    if (cmd.equals(Constants.TRANSLATE)) {
                        content = LocalizationUtil.updateLocalization(
                                curContent, "static-content", content,
                                toLanguageId, defaultLanguageId, true, true);
                    } else {
                        content = LocalizationUtil.updateLocalization(
                                curContent, "static-content", content,
                                defaultLanguageId, defaultLanguageId, true,
                                localized);
                    }
                }
            } else {
                if (curArticle.isTemplateDriven()) {
                    Fields newFields = DDMUtil.getFields(
                            ddmStructure.getStructureId(), serviceContext);

                    Fields existingFields = JournalConverterUtil.getDDMFields(
                            ddmStructure, curArticle.getContent());

                    Fields mergedFields = DDMUtil.mergeFields(newFields,
                            existingFields);

                    content = JournalConverterUtil.getContent(ddmStructure,
                            mergedFields);
                }
            }

            // Update article

            _log.info("Update article");
            _log.info("urlTitle = " + urlTitle);


            article = JournalArticleServiceUtil.getArticle(groupId, articleId,
                    version);

            Map<Locale, String> titleMap = article.getTitleMap();
            Map<Locale, String> descriptionMap = article.getDescriptionMap();

            String tempOldUrlTitle = article.getUrlTitle();

            articleURL = (String) serviceContext.getAttribute("urlTitle");

            _log.info("tempOldUrlTitle = " + tempOldUrlTitle);
            _log.info("articleURL = " + articleURL);

            if (cmd.equals(Constants.PREVIEW) || cmd.equals(Constants.UPDATE)) {
                titleMap.put(defaultLocale, title);
                descriptionMap.put(defaultLocale, description);

                article = JournalArticleServiceUtil.updateArticle(groupId,
                        folderId, articleId, version, titleMap, descriptionMap,
                        content, type, structureId, templateId, layoutUuid,
                        displayDateMonth, displayDateDay, displayDateYear,
                        displayDateHour, displayDateMinute,
                        expirationDateMonth, expirationDateDay,
                        expirationDateYear, expirationDateHour,
                        expirationDateMinute, neverExpire, reviewDateMonth,
                        reviewDateDay, reviewDateYear, reviewDateHour,
                        reviewDateMinute, neverReview, indexable, smallImage,
                        smallImageURL, smallFile, images, articleURL,
                        serviceContext);
            } else if (cmd.equals(Constants.TRANSLATE)) {
                article = JournalArticleServiceUtil.updateArticleTranslation(
                        groupId, articleId, version, toLocale, title,
                        description, content, images, serviceContext);
            }

            if (!tempOldUrlTitle.equals(article.getUrlTitle())) {
                oldUrlTitle = tempOldUrlTitle;
            }

        }

        // Recent articles

        // TODO
//        JournalUtil.addRecentArticle(actionRequest, article);

        // Journal content

        // TODO
        PortletPreferences portletPreferences = null;
        //
        // PortletPreferences portletPreferences = getStrictPortletSetup(
        // actionRequest);

        if (portletPreferences != null) {
            portletPreferences.setValue("groupId",
                    String.valueOf(article.getGroupId()));
            portletPreferences.setValue("articleId", article.getArticleId());

            portletPreferences.store();

            String portletResource = ParamUtil.getString(actionRequest,
                    "portletResource");

            // TODO
            // updateContentSearch(
            // actionRequest, portletResource, article.getArticleId());
        }

        return new Object[] { article, oldUrlTitle };
    }

    private static Log _log = LogFactoryUtil
            .getLog(CustomEditArticleAction.class);

}
