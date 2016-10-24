package ch.inofix.portlet.newsletter.service.impl;

import java.util.Date;

import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.service.base.NewsletterLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLinkConstants;

/**
 * The implementation of the newsletter local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.NewsletterLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-08 16:41
 * @modified 2016-10-24 13:56
 * @version 1.0.4
 * @see ch.inofix.portlet.newsletter.service.base.NewsletterLocalServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.NewsletterLocalServiceUtil
 */
public class NewsletterLocalServiceImpl extends NewsletterLocalServiceBaseImpl {

    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.NewsletterLocalServiceUtil} to
     * access the newsletter local service.
     */

    @Override
    public Newsletter addNewsletter(long userId, long groupId, String title,
            String template, String fromAddress, String fromName,
            String vCardGroupId, ServiceContext serviceContext)
            throws PortalException, SystemException {

        Newsletter newsletter = saveNewsletter(userId, groupId, 0, title,
                template, fromAddress, fromName, vCardGroupId, serviceContext);

        // Asset

        resourceLocalService.addResources(newsletter.getCompanyId(), groupId,
                userId, Newsletter.class.getName(),
                newsletter.getNewsletterId(), false, true, true);

        updateAsset(userId, newsletter, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        return newsletter;
    }

    @Override
    public Newsletter deleteNewsletter(long newsletterId)
            throws PortalException, SystemException {

        Newsletter newsletter = newsletterPersistence.remove(newsletterId);

        resourceLocalService.deleteResource(newsletter.getCompanyId(),
                Newsletter.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
                newsletterId);

        // Asset

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Newsletter.class);
        indexer.delete(newsletter);

        AssetEntry assetEntry = assetEntryLocalService.fetchEntry(
                Newsletter.class.getName(), newsletterId);

        assetLinkLocalService.deleteLinks(assetEntry.getEntryId());

        assetEntryLocalService.deleteEntry(assetEntry);

        return newsletter;
    }

    public Newsletter getNewsletter(String vCardGroupId)
            throws PortalException, SystemException {

        return newsletterPersistence.fetchByvCardGroupId(vCardGroupId);

    }

    private Newsletter saveNewsletter(long userId, long groupId,
            long newsletterId, String title, String template,
            String fromAddress, String fromName, String vCardGroupId,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        User user = userPersistence.findByPrimaryKey(userId);
        Date now = new Date();
        Newsletter newsletter = null;

        if (newsletterId > 0) {
            newsletter = newsletterLocalService.getNewsletter(newsletterId);
        } else {
            newsletterId = counterLocalService.increment();
            newsletter = newsletterPersistence.create(newsletterId);
            newsletter.setCompanyId(user.getCompanyId());
            newsletter.setGroupId(groupId);
            newsletter.setUserId(user.getUserId());
            newsletter.setUserName(user.getFullName());
            newsletter.setCreateDate(now);

        }

        newsletter.setModifiedDate(now);

        // TODO: validate the template string
        newsletter.setTitle(title);
        newsletter.setTemplate(template);
        newsletter.setFromAddress(fromAddress);
        newsletter.setFromName(fromName);
        newsletter.setVCardGroupId(vCardGroupId);
        newsletter.setExpandoBridgeAttributes(serviceContext);

        newsletterPersistence.update(newsletter);

        return newsletter;

    }

    @Override
    public void updateAsset(long userId, Newsletter newsletter,
            long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds) throws PortalException, SystemException {

        boolean visible = true;

        // TODO: What's the classTypeId?
        long classTypeId = 0;
        Date startDate = null;
        Date endDate = null;
        Date expirationDate = null;
        String mimeType = "text/plain";
        String title = newsletter.getTitle();
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

        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId,
                newsletter.getGroupId(), newsletter.getCreateDate(),
                newsletter.getModifiedDate(), Newsletter.class.getName(),
                newsletter.getNewsletterId(), newsletter.getUuid(),
                classTypeId, assetCategoryIds, assetTagNames, visible,
                startDate, endDate, expirationDate, mimeType, title,
                description, summary, url, layoutUuid, height, width, priority,
                sync);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(),
                assetLinkEntryIds, AssetLinkConstants.TYPE_RELATED);

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Newsletter.class);
        indexer.reindex(newsletter);
    }

    @Override
    public Newsletter updateNewsletter(long userId, long groupId,
            long newsletterId, String title, String template,
            String fromAddress, String fromName, String vCardGroupId,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        Newsletter newsletter = saveNewsletter(userId, groupId, newsletterId,
                title, template, fromAddress, fromName, vCardGroupId,
                serviceContext);

        // Asset

        resourceLocalService.updateResources(serviceContext.getCompanyId(),
                serviceContext.getScopeGroupId(), newsletter.getTitle(),
                newsletterId, serviceContext.getGroupPermissions(),
                serviceContext.getGuestPermissions());

        updateAsset(userId, newsletter, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        return newsletter;
    }

    private static Log _log = LogFactoryUtil
            .getLog(NewsletterLocalServiceImpl.class.getName());
}
