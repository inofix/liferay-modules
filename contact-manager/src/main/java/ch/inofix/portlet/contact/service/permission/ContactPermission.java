package ch.inofix.portlet.contact.service.permission;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-19 19:19
 * @modified 2015-05-23 17:50
 * @version 1.0.1
 *
 */
public class ContactPermission {

	/**
	 * 
	 * @param permissionChecker
	 * @param contactId
	 * @param actionId
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void check(PermissionChecker permissionChecker,
			long contactId, String actionId) throws PortalException,
			SystemException {

		if (!contains(permissionChecker, contactId, actionId)) {
			throw new PrincipalException();
		}
	}

	/**
	 * 
	 * @param permissionChecker
	 * @param contactId
	 * @param actionId
	 * @return
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static boolean contains(PermissionChecker permissionChecker,
			long contactId, String actionId) throws PortalException,
			SystemException {

		Contact contact = ContactLocalServiceUtil.getContact(contactId);

		if (permissionChecker.hasOwnerPermission(contact.getCompanyId(),
				Contact.class.getName(), contact.getContactId(),
				contact.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(contact.getGroupId(),
				Contact.class.getName(), contact.getContactId(), actionId);

	}

}
