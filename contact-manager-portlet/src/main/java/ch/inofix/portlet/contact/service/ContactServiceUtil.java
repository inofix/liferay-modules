package ch.inofix.portlet.contact.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableService;

/**
 * Provides the remote service utility for Contact. This utility wraps
 * {@link ch.inofix.portlet.contact.service.impl.ContactServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see ContactService
 * @see ch.inofix.portlet.contact.service.base.ContactServiceBaseImpl
 * @see ch.inofix.portlet.contact.service.impl.ContactServiceImpl
 * @generated
 */
public class ContactServiceUtil {
    private static ContactService _service;

    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify this class directly. Add custom service methods to {@link ch.inofix.portlet.contact.service.impl.ContactServiceImpl} and rerun ServiceBuilder to regenerate this class.
     */

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    public static java.lang.String getBeanIdentifier() {
        return getService().getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    public static void setBeanIdentifier(java.lang.String beanIdentifier) {
        getService().setBeanIdentifier(beanIdentifier);
    }

    public static java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return getService().invokeMethod(name, parameterTypes, arguments);
    }

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
    public static ch.inofix.portlet.contact.model.Contact addContact(
        long userId, long groupId, java.lang.String card, java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return getService()
                   .addContact(userId, groupId, card, uid, serviceContext);
    }

    /**
    * @return
    * @since 1.0.2
    * @throws PortalException
    * @throws SystemException
    */
    public static ch.inofix.portlet.contact.model.Contact createContact()
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return getService().createContact();
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
    public static ch.inofix.portlet.contact.model.Contact deleteContact(
        long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return getService().deleteContact(contactId);
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
    public static ch.inofix.portlet.contact.model.Contact getContact(
        long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return getService().getContact(contactId);
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
    public static ch.inofix.portlet.contact.model.Contact updateContact(
        long userId, long groupId, long contactId, java.lang.String card,
        java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return getService()
                   .updateContact(userId, groupId, contactId, card, uid,
            serviceContext);
    }

    public static void clearService() {
        _service = null;
    }

    public static ContactService getService() {
        if (_service == null) {
            InvokableService invokableService = (InvokableService) PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
                    ContactService.class.getName());

            if (invokableService instanceof ContactService) {
                _service = (ContactService) invokableService;
            } else {
                _service = new ContactServiceClp(invokableService);
            }

            ReferenceRegistry.registerReference(ContactServiceUtil.class,
                "_service");
        }

        return _service;
    }

    /**
     * @deprecated As of 6.2.0
     */
    public void setService(ContactService service) {
    }
}
