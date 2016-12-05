package com.liferay.portlet.journal.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletPreferences;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.journal.ArticleDisplayDateException;
import com.liferay.portlet.journal.ArticleExpirationDateException;
import com.liferay.portlet.journal.ArticleReviewDateException;
import com.liferay.portlet.journal.ArticleVersionException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;

/**
 *
 * @author Christian Berndt
 * @created 2016-12-05 18:20
 * @modified 2016-12-05 18:20
 * @version 1.0.0
 *
 */
public class CustomJournalArticleLocalServiceImpl extends
        JournalArticleLocalServiceWrapper {

    public CustomJournalArticleLocalServiceImpl(
            JournalArticleLocalService journalArticleLocalService) {
        super(journalArticleLocalService);
    }

    @Indexable(type = IndexableType.REINDEX)
    @Override
    public JournalArticle updateArticle(long userId, long groupId,
            long folderId, String articleId, double version,
            Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
            String content, String type, String ddmStructureKey,
            String ddmTemplateKey, String layoutUuid, int displayDateMonth,
            int displayDateDay, int displayDateYear, int displayDateHour,
            int displayDateMinute, int expirationDateMonth,
            int expirationDateDay, int expirationDateYear,
            int expirationDateHour, int expirationDateMinute,
            boolean neverExpire, int reviewDateMonth, int reviewDateDay,
            int reviewDateYear, int reviewDateHour, int reviewDateMinute,
            boolean neverReview, boolean indexable, boolean smallImage,
            String smallImageURL, File smallImageFile,
            Map<String, byte[]> images, String articleURL,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        _log.info("updateArticle()");

        // Article

        User user = UserLocalServiceUtil.getUser(userId);
        // User user = userPersistence.findByPrimaryKey(userId);
        articleId = StringUtil.toUpperCase(articleId.trim());

        byte[] smallImageBytes = null;

        try {
            smallImageBytes = FileUtil.getBytes(smallImageFile);
        } catch (IOException ioe) {
        }

        JournalArticle latestArticle = getLatestArticle(groupId, articleId,
                WorkflowConstants.STATUS_ANY);

        JournalArticle article = latestArticle;

        boolean imported = ExportImportThreadLocal.isImportInProcess();

        double latestVersion = latestArticle.getVersion();

        boolean addNewVersion = false;

        if (imported) {
            if (latestVersion > version) {

                JournalArticle existingArticle = JournalArticleLocalServiceUtil
                        .getArticle(groupId, articleId, latestVersion);

                // JournalArticle existingArticle =
                // journalArticlePersistence.fetchByG_A_V(
                // groupId, articleId, version);

                if (existingArticle != null) {
                    article = existingArticle;
                } else {
                    addNewVersion = true;
                }
            } else if (latestVersion < version) {
                addNewVersion = true;
            }
        } else {
            if ((version > 0) && (version != latestVersion)) {
                throw new ArticleVersionException();
            }

            serviceContext.validateModifiedDate(latestArticle,
                    ArticleVersionException.class);

            if (latestArticle.isApproved() || latestArticle.isExpired()
                    || latestArticle.isScheduled()) {

                addNewVersion = true;

                version = MathUtil.format(latestVersion + 0.1, 1, 1);
            }
        }

        Date displayDate = null;
        Date expirationDate = null;
        Date reviewDate = null;

        if (article.getClassNameId() == JournalArticleConstants.CLASSNAME_ID_DEFAULT) {

            displayDate = PortalUtil.getDate(displayDateMonth, displayDateDay,
                    displayDateYear, displayDateHour, displayDateMinute,
                    user.getTimeZone(), ArticleDisplayDateException.class);

            if (!neverExpire) {
                expirationDate = PortalUtil.getDate(expirationDateMonth,
                        expirationDateDay, expirationDateYear,
                        expirationDateHour, expirationDateMinute,
                        user.getTimeZone(),
                        ArticleExpirationDateException.class);
            }

            if (!neverReview) {
                reviewDate = PortalUtil.getDate(reviewDateMonth, reviewDateDay,
                        reviewDateYear, reviewDateHour, reviewDateMinute,
                        user.getTimeZone(), ArticleReviewDateException.class);
            }
        }

        Date now = new Date();

        boolean expired = false;

        if ((expirationDate != null) && expirationDate.before(now)) {
            expired = true;
        }

        // TODO
        // validate(user.getCompanyId(), groupId,
        // latestArticle.getClassNameId(),
        // titleMap, content, type, ddmStructureKey, ddmTemplateKey,
        // expirationDate, smallImage, smallImageURL, smallImageFile,
        // smallImageBytes, serviceContext);

        if (addNewVersion) {

            long id = CounterLocalServiceUtil.increment();
            // long id = counterLocalService.increment();

            article = JournalArticleLocalServiceUtil.createJournalArticle(id);
            // article = journalArticlePersistence.create(id);

            article.setResourcePrimKey(latestArticle.getResourcePrimKey());
            article.setGroupId(latestArticle.getGroupId());
            article.setCompanyId(latestArticle.getCompanyId());
            article.setUserId(user.getUserId());
            article.setUserName(user.getFullName());
            article.setCreateDate(latestArticle.getCreateDate());
            article.setClassNameId(latestArticle.getClassNameId());
            article.setClassPK(latestArticle.getClassPK());
            article.setArticleId(articleId);
            article.setVersion(version);
            article.setSmallImageId(latestArticle.getSmallImageId());
        }

        // TODO:
        Locale locale = Locale.ENGLISH;
        // Locale locale = getArticleDefaultLocale(content, serviceContext);

        String title = titleMap.get(locale);

        // TODO
        // content = format(user, groupId, articleId, article.getVersion(),
        // addNewVersion, content, ddmStructureKey, images);

        article.setModifiedDate(serviceContext.getModifiedDate(now));
        article.setFolderId(folderId);
        article.setTreePath(article.buildTreePath());
        article.setTitleMap(titleMap, locale);

        article.setUrlTitle(getUniqueUrlTitle(article.getId(),
                article.getArticleId(), title, latestArticle.getUrlTitle(),
                serviceContext));
        article.setDescriptionMap(descriptionMap, locale);
        article.setContent(content);
        article.setType(type);
        article.setStructureId(ddmStructureKey);
        article.setTemplateId(ddmTemplateKey);
        article.setLayoutUuid(layoutUuid);
        article.setDisplayDate(displayDate);
        article.setExpirationDate(expirationDate);
        article.setReviewDate(reviewDate);
        article.setIndexable(indexable);
        article.setSmallImage(smallImage);

        if (smallImage) {
            if ((smallImageFile != null) && (smallImageBytes != null)) {
                article.setSmallImageId(CounterLocalServiceUtil.increment());
                // article.setSmallImageId(counterLocalService.increment());
            }
        } else {
            article.setSmallImageId(0);
        }

        article.setSmallImageURL(smallImageURL);

        if (latestArticle.isPending()) {
            article.setStatus(latestArticle.getStatus());
        } else if (!expired) {
            article.setStatus(WorkflowConstants.STATUS_DRAFT);
        } else {
            article.setStatus(WorkflowConstants.STATUS_EXPIRED);
        }

        article.setExpandoBridgeAttributes(serviceContext);

        // TODO
        // journalArticlePersistence.update(article);

        // Asset

        updateAsset(userId, article, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        // Dynamic data mapping

        if (PortalUtil.getClassNameId(DDMStructure.class) == article
                .getClassNameId()) {

            // TODO
            // updateDDMStructureXSD(article.getClassPK(), content,
            // serviceContext);
        }

        // Small image

        // TODO
        // saveImages(smallImage, article.getSmallImageId(), smallImageFile,
        // smallImageBytes);

        // Email

        PortletPreferences preferences = ServiceContextUtil
                .getPortletPreferences(serviceContext);

        // Workflow

        if (expired && imported) {
            updateStatus(userId, article, article.getStatus(), articleURL,
                    new HashMap<String, Serializable>(), serviceContext);
        }

        if (serviceContext.getWorkflowAction() == WorkflowConstants.ACTION_PUBLISH) {

            // TODO
            // articleURL = buildArticleURL(articleURL, groupId, folderId,
            // articleId);

            serviceContext.setAttribute("articleURL", articleURL);

            // TODO
            // sendEmail(article, articleURL, preferences, "requested",
            // serviceContext);

            WorkflowHandlerRegistryUtil.startWorkflowInstance(
                    user.getCompanyId(), groupId, userId,
                    JournalArticle.class.getName(), article.getId(), article,
                    serviceContext);
        }

        return JournalArticleLocalServiceUtil.getArticle(article.getId());
        // return journalArticlePersistence.findByPrimaryKey(article.getId());
    }

    protected String getUniqueUrlTitle(long id, long groupId, String articleId,
            String title) throws PortalException, SystemException {

        _log.info("getUniqueUrlTitle()");

        // TODO
        String urlTitle = "TODO: uniqueUrlTtile";
//        String urlTitle = JournalUtil.getUrlTitle(id, title);

        return getUniqueUrlTitle(groupId, articleId, urlTitle);
    }

    protected String getUniqueUrlTitle(long id, String articleId, String title,
            String oldUrlTitle, ServiceContext serviceContext)
            throws PortalException, SystemException {

        _log.info("getUniqueUrlTitle()");

        String serviceContextUrlTitle = ParamUtil.getString(serviceContext,
                "urlTitle");

        String urlTitle = null;

        if (Validator.isNotNull(serviceContextUrlTitle)) {

            // TODO
//            urlTitle = JournalUtil.getUrlTitle(id, serviceContextUrlTitle);


        } else if (Validator.isNotNull(oldUrlTitle)) {
            return oldUrlTitle;
        } else {
            urlTitle = getUniqueUrlTitle(id, serviceContext.getScopeGroupId(),
                    articleId, title);
        }

        JournalArticle urlTitleArticle = fetchArticleByUrlTitle(
                serviceContext.getScopeGroupId(), urlTitle);

        if ((urlTitleArticle != null)
                && !Validator.equals(urlTitleArticle.getArticleId(), articleId)) {

            urlTitle = getUniqueUrlTitle(id, serviceContext.getScopeGroupId(),
                    articleId, urlTitle);
        }

        return urlTitle;
    }

    private static Log _log = LogFactoryUtil
            .getLog(CustomJournalArticleLocalServiceImpl.class);
}
