package ch.inofix.portlet.contact.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ContactService}.
 *
 * @author Brian Wing Shun Chan
 * @see ContactService
 * @generated
 */
public class ContactServiceWrapper implements ContactService,
    ServiceWrapper<ContactService> {
    private ContactService _contactService;

    public ContactServiceWrapper(ContactService contactService) {
        _contactService = contactService;
    }

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    @Override
    public java.lang.String getBeanIdentifier() {
        return _contactService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    @Override
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _contactService.setBeanIdentifier(beanIdentifier);
    }

    @Override
    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _contactService.invokeMethod(name, parameterTypes, arguments);
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
    @Override
    public ch.inofix.portlet.contact.model.Contact addContact(long userId,
        long groupId, java.lang.String card, java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactService.addContact(userId, groupId, card, uid,
            serviceContext);
    }

    /**
    * @return
    * @since 1.0.2
    * @throws PortalException
    * @throws SystemException
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact createContact()
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactService.createContact();
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
    @Override
    public ch.inofix.portlet.contact.model.Contact deleteContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactService.deleteContact(contactId);
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
    @Override
    public ch.inofix.portlet.contact.model.Contact getContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactService.getContact(contactId);
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
    @Override
    public ch.inofix.portlet.contact.model.Contact updateContact(long userId,
        long groupId, long contactId, java.lang.String card,
        java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactService.updateContact(userId, groupId, contactId, card,
            uid, serviceContext);
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
     */
    public ContactService getWrappedContactService() {
        return _contactService;
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
     */
    public void setWrappedContactService(ContactService contactService) {
        _contactService = contactService;
    }

    @Override
    public ContactService getWrappedService() {
        return _contactService;
    }

    @Override
    public void setWrappedService(ContactService contactService) {
        _contactService = contactService;
    }
}
