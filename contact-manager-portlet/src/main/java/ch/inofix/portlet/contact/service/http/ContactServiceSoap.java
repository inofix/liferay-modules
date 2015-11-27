package ch.inofix.portlet.contact.service.http;

import ch.inofix.portlet.contact.service.ContactServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link ch.inofix.portlet.contact.service.ContactServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link ch.inofix.portlet.contact.model.ContactSoap}.
 * If the method in the service utility returns a
 * {@link ch.inofix.portlet.contact.model.Contact}, that is translated to a
 * {@link ch.inofix.portlet.contact.model.ContactSoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ContactServiceHttp
 * @see ch.inofix.portlet.contact.model.ContactSoap
 * @see ch.inofix.portlet.contact.service.ContactServiceUtil
 * @generated
 */
public class ContactServiceSoap {
    private static Log _log = LogFactoryUtil.getLog(ContactServiceSoap.class);

    /**
    * Return the added contact.
    *
    * @param userId
    * @param groupId
    * @param card
    the vCard string
    * @param uid
    the vCard's uid
    * @return the added contact
    * @since 1.0.0
    * @throws PortalException
    * @throws SystemException
    */
    public static ch.inofix.portlet.contact.model.ContactSoap addContact(
        long userId, long groupId, java.lang.String card, java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws RemoteException {
        try {
            ch.inofix.portlet.contact.model.Contact returnValue = ContactServiceUtil.addContact(userId,
                    groupId, card, uid, serviceContext);

            return ch.inofix.portlet.contact.model.ContactSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);

            throw new RemoteException(e.getMessage());
        }
    }

    /**
    * @return
    * @since 1.0.2
    * @throws PortalException
    * @throws SystemException
    */
    public static ch.inofix.portlet.contact.model.ContactSoap createContact()
        throws RemoteException {
        try {
            ch.inofix.portlet.contact.model.Contact returnValue = ContactServiceUtil.createContact();

            return ch.inofix.portlet.contact.model.ContactSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);

            throw new RemoteException(e.getMessage());
        }
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
    public static ch.inofix.portlet.contact.model.ContactSoap deleteContact(
        long contactId) throws RemoteException {
        try {
            ch.inofix.portlet.contact.model.Contact returnValue = ContactServiceUtil.deleteContact(contactId);

            return ch.inofix.portlet.contact.model.ContactSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);

            throw new RemoteException(e.getMessage());
        }
    }

    /**
    * Return the contact.
    *
    * @param contactId
    * @return the latest version of a contact.
    * @since 1.0.0
    * @throws PortalException
    * @throws SystemException
    */
    public static ch.inofix.portlet.contact.model.ContactSoap getContact(
        long contactId) throws RemoteException {
        try {
            ch.inofix.portlet.contact.model.Contact returnValue = ContactServiceUtil.getContact(contactId);

            return ch.inofix.portlet.contact.model.ContactSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);

            throw new RemoteException(e.getMessage());
        }
    }

    /**
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
    public static ch.inofix.portlet.contact.model.ContactSoap updateContact(
        long userId, long groupId, long contactId, java.lang.String card,
        java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws RemoteException {
        try {
            ch.inofix.portlet.contact.model.Contact returnValue = ContactServiceUtil.updateContact(userId,
                    groupId, contactId, card, uid, serviceContext);

            return ch.inofix.portlet.contact.model.ContactSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);

            throw new RemoteException(e.getMessage());
        }
    }
}
