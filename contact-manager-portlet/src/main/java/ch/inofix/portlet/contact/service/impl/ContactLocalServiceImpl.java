package ch.inofix.portlet.contact.service.impl;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLinkConstants;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.base.ContactLocalServiceBaseImpl;
import ch.inofix.portlet.contact.social.ContactActivityKeys;

/**
 * The implementation of the contact local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.contact.service.ContactLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @see ch.inofix.portlet.contact.service.base.ContactLocalServiceBaseImpl
 * @see ch.inofix.portlet.contact.service.ContactLocalServiceUtil
 * @created 2015-05-07 18:36
 * @modified 2015-06-04 21:36
 * @version 1.0.6
 */
public class ContactLocalServiceImpl extends ContactLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * ch.inofix.portlet.contact.service.ContactLocalServiceUtil} to access the
	 * contact local service.
	 */

	// Enable logging for this class
	private static Log log = LogFactoryUtil
			.getLog(ContactLocalServiceImpl.class.getName());

	/**
	 * @since 1.0.0
	 */
	public Contact addContact(long userId, long groupId, String card,
			String uid, ServiceContext serviceContext) throws PortalException,
			SystemException {

		Contact contact = saveContact(userId, groupId, 0, card, uid,
				serviceContext);

		// Asset

		resourceLocalService.addResources(contact.getCompanyId(), groupId,
				userId, Contact.class.getName(), contact.getContactId(), false,
				true, true);

		updateAsset(userId, contact, serviceContext.getAssetCategoryIds(),
				serviceContext.getAssetTagNames(),
				serviceContext.getAssetLinkEntryIds());

		// Social

		JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

		extraDataJSONObject.put("title", contact.getFullName(true));

		socialActivityLocalService.addActivity(userId, groupId,
				Contact.class.getName(), contact.getContactId(),
				ContactActivityKeys.ADD_CONTACT,
				extraDataJSONObject.toString(), 0);

		return contact;
	}

	/**
	 * @since 1.0.3
	 */
	public Contact deleteContact(long contactId) throws PortalException,
			SystemException {

		Contact contact = contactPersistence.remove(contactId);

		resourceLocalService.deleteResource(contact.getCompanyId(),
				Contact.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
				contactId);

		// Asset

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(Contact.class);
		indexer.delete(contact);

		AssetEntry assetEntry = assetEntryLocalService.fetchEntry(
				Contact.class.getName(), contactId);

		assetLinkLocalService.deleteLinks(assetEntry.getEntryId());

		assetEntryLocalService.deleteEntry(assetEntry);

		return contact;
	}

	/**
	 * @param groupId
	 * @param uid
	 * @since 1.0.0
	 */
	public Contact getContact(long groupId, String uid) throws PortalException,
			SystemException {

		return contactPersistence.findByG_U(groupId, uid);

	}

	/**
	 * Return all contacts which belong to the current group.
	 * 
	 * @param groupId
	 * @return all contacts which belong to the current group.
	 * @since 1.0.6
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<Contact> getContacts(long groupId) throws PortalException,
			SystemException {

		return contactPersistence.findByGroupId(groupId);
	}

	/**
	 * 
	 * @param userId
	 * @param groupId
	 * @param contactId
	 * @param card
	 * @param uid
	 * @return
	 * @see 1.0.1
	 * @throws PortalException
	 * @throws SystemException
	 */
	private Contact saveContact(long userId, long groupId, long contactId,
			String card, String uid, ServiceContext serviceContext)
			throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);
		Date now = new Date();
		Contact contact = null;

		if (contactId > 0) {
			contact = contactLocalService.getContact(contactId);
		} else {
			contactId = counterLocalService.increment();
			contact = contactPersistence.create(contactId);
			contact.setCompanyId(user.getCompanyId());
			contact.setGroupId(groupId);
			contact.setUserId(user.getUserId());
			contact.setUserName(user.getFullName());
			contact.setCreateDate(now);

		}

		contact.setModifiedDate(now);

		// TODO: validate the vCard string
		contact.setCard(card);
		contact.setUid(uid);
		contact.setExpandoBridgeAttributes(serviceContext);

		contactPersistence.update(contact);

		return contact;

	}

	/**
	 * @since 1.0.3
	 */
	public void updateAsset(long userId, Contact contact,
			long[] assetCategoryIds, String[] assetTagNames,
			long[] assetLinkEntryIds) throws PortalException, SystemException {

		boolean visible = true;
		// boolean visible = false;

		// if (contact.isApproved()) {
		// visible = true;
		// }

		// TODO: What's the classTypeId?
		long classTypeId = 0;
		Date startDate = null;
		Date endDate = null;
		Date expirationDate = null;
		// TODO: Is vcard the correct mime-type?
		String mimeType = "text/vcard";
		String title = contact.getFullName(true);
		String description = contact.getFormattedName();
		String summary = HtmlUtil.extractText(StringUtil.shorten(
				contact.getFormattedName(), 500));
		// TODO: What does url mean in this context?
		String url = null;
		// TODO: What does layoutUuid mean in this context?
		String layoutUuid = null;
		int height = 0;
		int width = 0;
		Integer priority = null;
		boolean sync = false;

		AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId,
				contact.getGroupId(), contact.getCreateDate(),
				contact.getModifiedDate(), Contact.class.getName(),
				contact.getContactId(), contact.getUuid(), classTypeId,
				assetCategoryIds, assetTagNames, visible, startDate, endDate,
				expirationDate, mimeType, title, description, summary, url,
				layoutUuid, height, width, priority, sync);

		assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(),
				assetLinkEntryIds, AssetLinkConstants.TYPE_RELATED);

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(Contact.class);
		indexer.reindex(contact);
	}

	/**
	 * @since 1.0.0
	 */
	public Contact updateContact(long userId, long groupId, long contactId,
			String card, String uid, ServiceContext serviceContext)
			throws PortalException, SystemException {

		Contact contact = saveContact(userId, groupId, contactId, card, uid,
				serviceContext);

		// Asset

		resourceLocalService.updateResources(serviceContext.getCompanyId(),
				serviceContext.getScopeGroupId(), contact.getFullName(),
				contactId, serviceContext.getGroupPermissions(),
				serviceContext.getGuestPermissions());

		updateAsset(userId, contact, serviceContext.getAssetCategoryIds(),
				serviceContext.getAssetTagNames(),
				serviceContext.getAssetLinkEntryIds());

		// Social

		JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

		extraDataJSONObject.put("title", contact.getFullName(true));

		socialActivityLocalService.addActivity(userId, groupId,
				Contact.class.getName(), contact.getContactId(),
				ContactActivityKeys.UPDATE_CONTACT,
				extraDataJSONObject.toString(), 0);

		return contact;
	}
}
