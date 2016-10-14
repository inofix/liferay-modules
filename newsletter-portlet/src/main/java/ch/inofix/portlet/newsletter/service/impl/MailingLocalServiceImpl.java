package ch.inofix.portlet.newsletter.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.newsletter.backgroundtask.MailingBackgroundTaskExecutor;
import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.model.Subscriber;
import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;
import ch.inofix.portlet.newsletter.service.SubscriberLocalServiceUtil;
import ch.inofix.portlet.newsletter.service.base.MailingLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.model.AssetEntry;

/**
 * The implementation of the mailing local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.MailingLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-10 17:21
 * @modified 2016-10-14 17:08
 * @version 1.0.3
 * @see ch.inofix.portlet.newsletter.service.base.MailingLocalServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil
 */
public class MailingLocalServiceImpl extends MailingLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil} to access
     * the mailing local service.
     */

    @Override
    public Mailing addMailing(long userId, long groupId, String title,
            String template, long newsletterId, String articleId,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        Date sendDate = null;
        boolean sent = false;

        Mailing mailing = saveMailing(userId, groupId, 0, title, template,
                newsletterId, articleId, sendDate, sent, serviceContext);

        // Asset

        resourceLocalService.addResources(mailing.getCompanyId(), groupId,
                userId, Mailing.class.getName(), mailing.getMailingId(), false,
                true, true);

        updateAsset(userId, mailing, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        return mailing;
    }

    @Override
    public Mailing deleteMailing(long mailingId) throws PortalException,
            SystemException {

        Mailing mailing = mailingPersistence.remove(mailingId);

        resourceLocalService.deleteResource(mailing.getCompanyId(),
                Mailing.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
                mailingId);

        // Asset

        Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(Mailing.class);
        indexer.delete(mailing);

        AssetEntry assetEntry = assetEntryLocalService.fetchEntry(
                Mailing.class.getName(), mailingId);

        assetEntryLocalService.deleteEntry(assetEntry);

        return mailing;
    }

    private Mailing saveMailing(long userId, long groupId, long mailingId,
            String title, String template, long newsletterId, String articleId,
            Date sendDate, boolean sent, ServiceContext serviceContext) throws PortalException,
            SystemException {

        User user = userPersistence.findByPrimaryKey(userId);
        Date now = new Date();
        Mailing mailing = null;

        if (mailingId > 0) {
            mailing = mailingLocalService.getMailing(mailingId);
        } else {
            mailingId = counterLocalService.increment();
            mailing = mailingPersistence.create(mailingId);
            mailing.setCompanyId(user.getCompanyId());
            mailing.setGroupId(groupId);
            mailing.setUserId(user.getUserId());
            mailing.setUserName(user.getFullName());
            mailing.setCreateDate(now);
        }

        mailing.setModifiedDate(now);

        mailing.setTitle(title);
        mailing.setTemplate(template);
        mailing.setNewsletterId(newsletterId);
        mailing.setArticleId(articleId);
        mailing.setSendDate(sendDate);
        mailing.setSent(sent);
        mailing.setExpandoBridgeAttributes(serviceContext);

        mailingPersistence.update(mailing);

        return mailing;

    }

    @Override
    public void sendMailings(long userId, long groupId,
            Map<String, String[]> parameterMap) throws PortalException,
            SystemException {

        _log.info("sendMailings()");

        long mailingId = GetterUtil.getLong(ArrayUtil.getValue(
                parameterMap.get("mailingId"), 0));

        String email = GetterUtil.getString(ArrayUtil.getValue(
                parameterMap.get("email"), 0));

        Mailing mailing = null;

        if (mailingId > 0) {
            mailing = MailingLocalServiceUtil.getMailing(mailingId);
        }

        List<Subscriber> subscribers = new ArrayList<Subscriber>();

        if (Validator.isNotNull(email)) {

            // test mail

            Subscriber subscriber = SubscriberLocalServiceUtil
                    .createSubscriber(0);
            subscriber.setEmail(email);
            subscribers.add(subscriber);

        } else if (mailing != null) {

            Newsletter newsletter = null;

            if (mailing.getNewsletterId() > 0) {
                newsletter = newsletterLocalService.getNewsletter(mailing
                        .getNewsletterId());
            }

            if (newsletter != null) {
                subscribers = subscriberService.getSubscribersFromVCardGroup(
                        newsletter.getCompanyId(), groupId,
                        newsletter.getVCardGroupId(), 0, Integer.MAX_VALUE);
            }
        }

        _log.info("subscribers.size() = " + subscribers.size());

        for (Subscriber subscriber : subscribers) {
            Map<String, Object> contextObjects = new HashMap<String, Object>();
            contextObjects.put("subscriber", subscriber);
            String content = mailingService.prepareMailing(contextObjects,
                    mailingId);
            _log.info(content);
        }
    }

    @Override
    public long sendMailingsInBackground(long userId, String taskName,
            long groupId, Map<String, String[]> parameterMap)
            throws PortalException, SystemException {

        _log.info("sendMailingsInBackground()");

        Map<String, Serializable> taskContextMap = new HashMap<String, Serializable>();
        taskContextMap.put("userId", userId);
        taskContextMap.put("groupId", groupId);
        taskContextMap.put("parameterMap", (Serializable) parameterMap);

        String[] servletContextNames = parameterMap.get("servletContextNames");

        BackgroundTask backgroundTask = BackgroundTaskLocalServiceUtil
                .addBackgroundTask(userId, groupId, taskName,
                        servletContextNames,
                        MailingBackgroundTaskExecutor.class, taskContextMap,
                        new ServiceContext());

        return backgroundTask.getBackgroundTaskId();

    }

    @Override
    public void updateAsset(long userId, Mailing mailing,
            long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds) throws PortalException, SystemException {

        boolean visible = true;

        // TODO: What's the classTypeId?
        long classTypeId = 0;
        Date startDate = null;
        Date endDate = null;
        Date expirationDate = null;
        String mimeType = "text/plain";
        String title = mailing.getTitle();
        String description = "";
        String summary = "";
        // TODO: What does url mean in this context?
        String url = null;
        // TODO: What does layoutUuid mean in this context?
        String layoutUuid = null;
        int height = 0;
        int width = 0;
        Integer priority = null;
        boolean sync = false;

        assetEntryLocalService.updateEntry(userId, mailing.getGroupId(),
                mailing.getCreateDate(), mailing.getModifiedDate(),
                Mailing.class.getName(), mailing.getMailingId(),
                mailing.getUuid(), classTypeId, assetCategoryIds,
                assetTagNames, visible, startDate, endDate, expirationDate,
                mimeType, title, description, summary, url, layoutUuid, height,
                width, priority, sync);

        Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(Mailing.class);
        indexer.reindex(mailing);
    }

    @Override
    public Mailing updateMailing(long userId, long groupId, long mailingId,
            String title, String template, long newsletterId, String articleId,
            Date sendDate, boolean sent, ServiceContext serviceContext)
            throws PortalException, SystemException {

        Mailing mailing = saveMailing(userId, groupId, mailingId, title,
                template, newsletterId, articleId, sendDate, sent,
                serviceContext);

        // Asset

        resourceLocalService.updateResources(serviceContext.getCompanyId(),
                serviceContext.getScopeGroupId(), mailing.getTitle(),
                mailingId, serviceContext.getGroupPermissions(),
                serviceContext.getGuestPermissions());

        updateAsset(userId, mailing, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        return mailing;
    }

    private static Log _log = LogFactoryUtil
            .getLog(MailingLocalServiceImpl.class.getName());

}
