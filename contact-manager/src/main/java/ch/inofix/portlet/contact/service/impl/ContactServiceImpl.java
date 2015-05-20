package ch.inofix.portlet.contact.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ServiceContext;

import ch.inofix.portlet.contact.NoSuchContactException;
import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.base.ContactServiceBaseImpl;

/**
 * The implementation of the contact remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.contact.service.ContactService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @see ch.inofix.portlet.contact.service.base.ContactServiceBaseImpl
 * @see ch.inofix.portlet.contact.service.ContactServiceUtil
 * @created 2015-05-07 23:50
 * @modified 2015-05-19 16:10
 * @version 1.0.2
 */
public class ContactServiceImpl extends ContactServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * ch.inofix.portlet.contact.service.ContactServiceUtil} to access the
	 * contact remote service.
	 */

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(ContactServiceImpl.class
			.getName());

	/**
	 * 
	 * @param userId
	 * @param groupId
	 * @param vCard
	 *            the vCard string
	 * @param uid
	 *            the vCard's uid
	 * @return
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 */
	public Contact addContact(long userId, long groupId, String card,
			String uid, ServiceContext serviceContext) throws PortalException,
			SystemException {

		// TODO: Check ADD permission
		return ContactLocalServiceUtil.addContact(userId, groupId, card, uid,
				serviceContext);

	}

	/**
	 * 
	 * @return
	 * @since 1.0.2
	 * @throws PortalException
	 * @throws SystemException
	 */
	public Contact createContact() throws PortalException, SystemException {

		// No permission check required
		return ContactLocalServiceUtil.createContact(0);

	}

	/**
	 * Delete a specific contact version and return the deleted contact.
	 * 
	 * @param contactId
	 * @return the deleted contact
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 */
	public Contact deleteContact(long contactId)
			throws PortalException, SystemException {

		// TODO: Check DELETE permission
		Contact contact = ContactLocalServiceUtil.deleteContact(contactId);

		return contact;

	}

	/**
	 * Return the latest version of a contact.
	 * 
	 * @param contactId
	 * @return the latest version of a contact.
	 * @since 1.0.0
	 * @throws NoSuchContactException
	 * @throws SystemException
	 */
	public Contact getContact(long contactId) throws PortalException,
			SystemException {

		// TODO: Check VIEW permission
		return ContactLocalServiceUtil.getContact(contactId);

	}

	/**
	 * 
	 * @param userId
	 * @param groupId
	 * @param contactId
	 * @param card
	 * @param uid
	 * @param serviceContext
	 * @return
	 * @since 1.0.2
	 * @throws PortalException
	 * @throws SystemException
	 */
	public Contact updateContact(long userId, long groupId, long contactId,
			String card, String uid, ServiceContext serviceContext)
			throws PortalException, SystemException {

		// TODO: Check UPDATE permission
		return ContactLocalServiceUtil.updateContact(userId, groupId,
				contactId, card, uid, serviceContext);

	}

}
