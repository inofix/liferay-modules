package ch.inofix.portlet.contact.service.impl;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;

import ch.inofix.portlet.contact.NoSuchContactException;
import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.base.ContactLocalServiceBaseImpl;

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
 * @modified 2015-05-07 18:36
 * @version 1.0.0
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
	private static Log _log = LogFactoryUtil
			.getLog(ContactLocalServiceImpl.class.getName());

	/**
	 * @since 1.0.0
	 */
	public Contact addContact(long userId, long groupId, String card,
			String uid) throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);
		Date now = new Date();

		long contactId = counterLocalService.increment();

		Contact contact = contactPersistence.create(contactId);

		contact.setCompanyId(user.getCompanyId());
		contact.setGroupId(groupId);
		contact.setUserId(user.getUserId());
		contact.setUserName(user.getFullName());
		contact.setCreateDate(now);
		contact.setModifiedDate(now);

		// TODO: validate the vCard string
		contact.setCard(card);
		contact.setUid(uid);

		contactPersistence.update(contact);

		return contact;
	}

	/**
	 * Return the contact's latest version.
	 * 
	 * @param groupId
	 * @param contactId
	 * @since 1.0.0
	 * @return the contact's latest version.
	 * @throws SystemException
	 * @throws NoSuchContactException
	 */
	public Contact getContact(String contactId) throws NoSuchContactException,
			SystemException {

		return contactPersistence.findByContactId_First(contactId, null);

	}

	/**
	 * 
	 * @since 1.0.0
	 */
	public Contact getContact(long groupId, String uid)
			throws NoSuchContactException, SystemException {

		return contactPersistence.findByG_U(groupId, uid);

	}

	/**
	 * Return all versions of a contact.
	 * 
	 * @param contactId
	 * @return all versions of a contact.
	 * @since 1.0.0
	 * @throws SystemException
	 */
	public List<Contact> getContacts(String contactId) throws SystemException {

		return contactPersistence.findByContactId(contactId);

	}

}
