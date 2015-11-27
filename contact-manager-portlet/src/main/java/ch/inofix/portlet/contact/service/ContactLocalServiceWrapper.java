package ch.inofix.portlet.contact.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ContactLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ContactLocalService
 * @generated
 */
public class ContactLocalServiceWrapper implements ContactLocalService,
    ServiceWrapper<ContactLocalService> {
    private ContactLocalService _contactLocalService;

    public ContactLocalServiceWrapper(ContactLocalService contactLocalService) {
        _contactLocalService = contactLocalService;
    }

    /**
    * Adds the contact to the database. Also notifies the appropriate model listeners.
    *
    * @param contact the contact
    * @return the contact that was added
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact addContact(
        ch.inofix.portlet.contact.model.Contact contact)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.addContact(contact);
    }

    /**
    * Creates a new contact with the primary key. Does not add the contact to the database.
    *
    * @param contactId the primary key for the new contact
    * @return the new contact
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact createContact(long contactId) {
        return _contactLocalService.createContact(contactId);
    }

    /**
    * Deletes the contact with the primary key from the database. Also notifies the appropriate model listeners.
    *
    * @param contactId the primary key of the contact
    * @return the contact that was removed
    * @throws PortalException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact deleteContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.deleteContact(contactId);
    }

    /**
    * Deletes the contact from the database. Also notifies the appropriate model listeners.
    *
    * @param contact the contact
    * @return the contact that was removed
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact deleteContact(
        ch.inofix.portlet.contact.model.Contact contact)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.deleteContact(contact);
    }

    @Override
    public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
        return _contactLocalService.dynamicQuery();
    }

    /**
    * Performs a dynamic query on the database and returns the matching rows.
    *
    * @param dynamicQuery the dynamic query
    * @return the matching rows
    * @throws SystemException if a system exception occurred
    */
    @Override
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.dynamicQuery(dynamicQuery);
    }

    /**
    * Performs a dynamic query on the database and returns a range of the matching rows.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param dynamicQuery the dynamic query
    * @param start the lower bound of the range of model instances
    * @param end the upper bound of the range of model instances (not inclusive)
    * @return the range of matching rows
    * @throws SystemException if a system exception occurred
    */
    @Override
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.dynamicQuery(dynamicQuery, start, end);
    }

    /**
    * Performs a dynamic query on the database and returns an ordered range of the matching rows.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param dynamicQuery the dynamic query
    * @param start the lower bound of the range of model instances
    * @param end the upper bound of the range of model instances (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching rows
    * @throws SystemException if a system exception occurred
    */
    @Override
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.dynamicQuery(dynamicQuery, start, end,
            orderByComparator);
    }

    /**
    * Returns the number of rows that match the dynamic query.
    *
    * @param dynamicQuery the dynamic query
    * @return the number of rows that match the dynamic query
    * @throws SystemException if a system exception occurred
    */
    @Override
    public long dynamicQueryCount(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.dynamicQueryCount(dynamicQuery);
    }

    /**
    * Returns the number of rows that match the dynamic query.
    *
    * @param dynamicQuery the dynamic query
    * @param projection the projection to apply to the query
    * @return the number of rows that match the dynamic query
    * @throws SystemException if a system exception occurred
    */
    @Override
    public long dynamicQueryCount(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
        com.liferay.portal.kernel.dao.orm.Projection projection)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.dynamicQueryCount(dynamicQuery, projection);
    }

    @Override
    public ch.inofix.portlet.contact.model.Contact fetchContact(long contactId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.fetchContact(contactId);
    }

    /**
    * Returns the contact with the matching UUID and company.
    *
    * @param uuid the contact's UUID
    * @param companyId the primary key of the company
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact fetchContactByUuidAndCompanyId(
        java.lang.String uuid, long companyId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.fetchContactByUuidAndCompanyId(uuid,
            companyId);
    }

    /**
    * Returns the contact matching the UUID and group.
    *
    * @param uuid the contact's UUID
    * @param groupId the primary key of the group
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact fetchContactByUuidAndGroupId(
        java.lang.String uuid, long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.fetchContactByUuidAndGroupId(uuid, groupId);
    }

    /**
    * Returns the contact with the primary key.
    *
    * @param contactId the primary key of the contact
    * @return the contact
    * @throws PortalException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact getContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContact(contactId);
    }

    @Override
    public com.liferay.portal.model.PersistedModel getPersistedModel(
        java.io.Serializable primaryKeyObj)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getPersistedModel(primaryKeyObj);
    }

    /**
    * Returns the contact with the matching UUID and company.
    *
    * @param uuid the contact's UUID
    * @param companyId the primary key of the company
    * @return the matching contact
    * @throws PortalException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact getContactByUuidAndCompanyId(
        java.lang.String uuid, long companyId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContactByUuidAndCompanyId(uuid, companyId);
    }

    /**
    * Returns the contact matching the UUID and group.
    *
    * @param uuid the contact's UUID
    * @param groupId the primary key of the group
    * @return the matching contact
    * @throws PortalException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact getContactByUuidAndGroupId(
        java.lang.String uuid, long groupId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContactByUuidAndGroupId(uuid, groupId);
    }

    /**
    * Returns a range of all the contacts.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @return the range of contacts
    * @throws SystemException if a system exception occurred
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.model.Contact> getContacts(
        int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContacts(start, end);
    }

    /**
    * Returns the number of contacts.
    *
    * @return the number of contacts
    * @throws SystemException if a system exception occurred
    */
    @Override
    public int getContactsCount()
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContactsCount();
    }

    /**
    * Updates the contact in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
    *
    * @param contact the contact
    * @return the contact that was updated
    * @throws SystemException if a system exception occurred
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact updateContact(
        ch.inofix.portlet.contact.model.Contact contact)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.updateContact(contact);
    }

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    @Override
    public java.lang.String getBeanIdentifier() {
        return _contactLocalService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    @Override
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _contactLocalService.setBeanIdentifier(beanIdentifier);
    }

    @Override
    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _contactLocalService.invokeMethod(name, parameterTypes, arguments);
    }

    /**
    * @since 1.0.0
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact addContact(long userId,
        long groupId, java.lang.String card, java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.addContact(userId, groupId, card, uid,
            serviceContext);
    }

    /**
    * @param groupId
    * @param uid
    * @since 1.0.0
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact getContact(long groupId,
        java.lang.String uid)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContact(groupId, uid);
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
    @Override
    public java.util.List<ch.inofix.portlet.contact.model.Contact> getContacts(
        long groupId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.getContacts(groupId);
    }

    /**
    * @since 1.0.3
    */
    @Override
    public void updateAsset(long userId,
        ch.inofix.portlet.contact.model.Contact contact,
        long[] assetCategoryIds, java.lang.String[] assetTagNames,
        long[] assetLinkEntryIds)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        _contactLocalService.updateAsset(userId, contact, assetCategoryIds,
            assetTagNames, assetLinkEntryIds);
    }

    /**
    * @since 1.0.0
    */
    @Override
    public ch.inofix.portlet.contact.model.Contact updateContact(long userId,
        long groupId, long contactId, java.lang.String card,
        java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _contactLocalService.updateContact(userId, groupId, contactId,
            card, uid, serviceContext);
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
     */
    public ContactLocalService getWrappedContactLocalService() {
        return _contactLocalService;
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
     */
    public void setWrappedContactLocalService(
        ContactLocalService contactLocalService) {
        _contactLocalService = contactLocalService;
    }

    @Override
    public ContactLocalService getWrappedService() {
        return _contactLocalService;
    }

    @Override
    public void setWrappedService(ContactLocalService contactLocalService) {
        _contactLocalService = contactLocalService;
    }
}
