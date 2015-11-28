package ch.inofix.portlet.contact.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.security.ac.AccessControlled;
import com.liferay.portal.service.BaseService;
import com.liferay.portal.service.InvokableService;

/**
 * Provides the remote service interface for Contact. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see ContactServiceUtil
 * @see ch.inofix.portlet.contact.service.base.ContactServiceBaseImpl
 * @see ch.inofix.portlet.contact.service.impl.ContactServiceImpl
 * @generated
 */
@AccessControlled
@JSONWebService
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface ContactService extends BaseService, InvokableService {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this interface directly. Always use {@link ContactServiceUtil} to access the contact remote service. Add custom service methods to {@link ch.inofix.portlet.contact.service.impl.ContactServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
     */

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    public java.lang.String getBeanIdentifier();

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    public void setBeanIdentifier(java.lang.String beanIdentifier);

    @Override
    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable;

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
    public ch.inofix.portlet.contact.model.Contact addContact(long userId,
        long groupId, java.lang.String card, java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * @return
    * @since 1.0.2
    * @throws PortalException
    * @throws SystemException
    */
    public ch.inofix.portlet.contact.model.Contact createContact()
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Delete a specific contact version and return the deleted contact.
    *
    * @param contactId
    * @return the deleted contact
    * @since 1.0.0
    * @throws PortalException
    * @throws SystemException
    */
    public ch.inofix.portlet.contact.model.Contact deleteContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Return the contact.
    *
    * @param contactId
    * @return the latest version of a contact.
    * @since 1.0.0
    * @throws PortalException
    * @throws SystemException
    */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ch.inofix.portlet.contact.model.Contact getContact(long contactId)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException;

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
    public ch.inofix.portlet.contact.model.Contact updateContact(long userId,
        long groupId, long contactId, java.lang.String card,
        java.lang.String uid,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException;
}
