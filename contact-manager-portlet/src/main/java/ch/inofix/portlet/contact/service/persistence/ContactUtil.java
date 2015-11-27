package ch.inofix.portlet.contact.service.persistence;

import ch.inofix.portlet.contact.model.Contact;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.ServiceContext;

import java.util.List;

/**
 * The persistence utility for the contact service. This utility wraps {@link ContactPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ContactPersistence
 * @see ContactPersistenceImpl
 * @generated
 */
public class ContactUtil {
    private static ContactPersistence _persistence;

    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
     */

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#clearCache()
     */
    public static void clearCache() {
        getPersistence().clearCache();
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#clearCache(com.liferay.portal.model.BaseModel)
     */
    public static void clearCache(Contact contact) {
        getPersistence().clearCache(contact);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
     */
    public static long countWithDynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return getPersistence().countWithDynamicQuery(dynamicQuery);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
     */
    public static List<Contact> findWithDynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
     */
    public static List<Contact> findWithDynamicQuery(
        DynamicQuery dynamicQuery, int start, int end)
        throws SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
     */
    public static List<Contact> findWithDynamicQuery(
        DynamicQuery dynamicQuery, int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        return getPersistence()
                   .findWithDynamicQuery(dynamicQuery, start, end,
            orderByComparator);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel)
     */
    public static Contact update(Contact contact) throws SystemException {
        return getPersistence().update(contact);
    }

    /**
     * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, ServiceContext)
     */
    public static Contact update(Contact contact, ServiceContext serviceContext)
        throws SystemException {
        return getPersistence().update(contact, serviceContext);
    }

    /**
    * Returns all the contacts where uuid = &#63;.
    *
    * @param uuid the uuid
    * @return the matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid(
        java.lang.String uuid)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid(uuid);
    }

    /**
    * Returns a range of all the contacts where uuid = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param uuid the uuid
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @return the range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid(
        java.lang.String uuid, int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid(uuid, start, end);
    }

    /**
    * Returns an ordered range of all the contacts where uuid = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param uuid the uuid
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid(
        java.lang.String uuid, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid(uuid, start, end, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where uuid = &#63;.
    *
    * @param uuid the uuid
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid_First(uuid, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where uuid = &#63;.
    *
    * @param uuid the uuid
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByUuid_First(uuid, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where uuid = &#63;.
    *
    * @param uuid the uuid
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid_Last(uuid, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where uuid = &#63;.
    *
    * @param uuid the uuid
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
    }

    /**
    * Returns the contacts before and after the current contact in the ordered set where uuid = &#63;.
    *
    * @param contactId the primary key of the current contact
    * @param uuid the uuid
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the previous, current, and next contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact[] findByUuid_PrevAndNext(
        long contactId, java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByUuid_PrevAndNext(contactId, uuid, orderByComparator);
    }

    /**
    * Removes all the contacts where uuid = &#63; from the database.
    *
    * @param uuid the uuid
    * @throws SystemException if a system exception occurred
    */
    public static void removeByUuid(java.lang.String uuid)
        throws com.liferay.portal.kernel.exception.SystemException {
        getPersistence().removeByUuid(uuid);
    }

    /**
    * Returns the number of contacts where uuid = &#63;.
    *
    * @param uuid the uuid
    * @return the number of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countByUuid(java.lang.String uuid)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countByUuid(uuid);
    }

    /**
    * Returns the contact where uuid = &#63; and groupId = &#63; or throws a {@link ch.inofix.portlet.contact.NoSuchContactException} if it could not be found.
    *
    * @param uuid the uuid
    * @param groupId the group ID
    * @return the matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByUUID_G(
        java.lang.String uuid, long groupId)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUUID_G(uuid, groupId);
    }

    /**
    * Returns the contact where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
    *
    * @param uuid the uuid
    * @param groupId the group ID
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUUID_G(
        java.lang.String uuid, long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId);
    }

    /**
    * Returns the contact where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
    *
    * @param uuid the uuid
    * @param groupId the group ID
    * @param retrieveFromCache whether to use the finder cache
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUUID_G(
        java.lang.String uuid, long groupId, boolean retrieveFromCache)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId, retrieveFromCache);
    }

    /**
    * Removes the contact where uuid = &#63; and groupId = &#63; from the database.
    *
    * @param uuid the uuid
    * @param groupId the group ID
    * @return the contact that was removed
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact removeByUUID_G(
        java.lang.String uuid, long groupId)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().removeByUUID_G(uuid, groupId);
    }

    /**
    * Returns the number of contacts where uuid = &#63; and groupId = &#63;.
    *
    * @param uuid the uuid
    * @param groupId the group ID
    * @return the number of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countByUUID_G(java.lang.String uuid, long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countByUUID_G(uuid, groupId);
    }

    /**
    * Returns all the contacts where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @return the matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid_C(
        java.lang.String uuid, long companyId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid_C(uuid, companyId);
    }

    /**
    * Returns a range of all the contacts where uuid = &#63; and companyId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @return the range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid_C(
        java.lang.String uuid, long companyId, int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByUuid_C(uuid, companyId, start, end);
    }

    /**
    * Returns an ordered range of all the contacts where uuid = &#63; and companyId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByUuid_C(
        java.lang.String uuid, long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByUuid_C(uuid, companyId, start, end, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByUuid_C_First(
        java.lang.String uuid, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByUuid_C_First(uuid, companyId, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUuid_C_First(
        java.lang.String uuid, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .fetchByUuid_C_First(uuid, companyId, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByUuid_C_Last(
        java.lang.String uuid, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByUuid_C_Last(uuid, companyId, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByUuid_C_Last(
        java.lang.String uuid, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .fetchByUuid_C_Last(uuid, companyId, orderByComparator);
    }

    /**
    * Returns the contacts before and after the current contact in the ordered set where uuid = &#63; and companyId = &#63;.
    *
    * @param contactId the primary key of the current contact
    * @param uuid the uuid
    * @param companyId the company ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the previous, current, and next contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact[] findByUuid_C_PrevAndNext(
        long contactId, java.lang.String uuid, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByUuid_C_PrevAndNext(contactId, uuid, companyId,
            orderByComparator);
    }

    /**
    * Removes all the contacts where uuid = &#63; and companyId = &#63; from the database.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @throws SystemException if a system exception occurred
    */
    public static void removeByUuid_C(java.lang.String uuid, long companyId)
        throws com.liferay.portal.kernel.exception.SystemException {
        getPersistence().removeByUuid_C(uuid, companyId);
    }

    /**
    * Returns the number of contacts where uuid = &#63; and companyId = &#63;.
    *
    * @param uuid the uuid
    * @param companyId the company ID
    * @return the number of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countByUuid_C(java.lang.String uuid, long companyId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countByUuid_C(uuid, companyId);
    }

    /**
    * Returns the contact where groupId = &#63; and uid = &#63; or throws a {@link ch.inofix.portlet.contact.NoSuchContactException} if it could not be found.
    *
    * @param groupId the group ID
    * @param uid the uid
    * @return the matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByG_U(
        long groupId, java.lang.String uid)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByG_U(groupId, uid);
    }

    /**
    * Returns the contact where groupId = &#63; and uid = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
    *
    * @param groupId the group ID
    * @param uid the uid
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByG_U(
        long groupId, java.lang.String uid)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByG_U(groupId, uid);
    }

    /**
    * Returns the contact where groupId = &#63; and uid = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
    *
    * @param groupId the group ID
    * @param uid the uid
    * @param retrieveFromCache whether to use the finder cache
    * @return the matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByG_U(
        long groupId, java.lang.String uid, boolean retrieveFromCache)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByG_U(groupId, uid, retrieveFromCache);
    }

    /**
    * Removes the contact where groupId = &#63; and uid = &#63; from the database.
    *
    * @param groupId the group ID
    * @param uid the uid
    * @return the contact that was removed
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact removeByG_U(
        long groupId, java.lang.String uid)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().removeByG_U(groupId, uid);
    }

    /**
    * Returns the number of contacts where groupId = &#63; and uid = &#63;.
    *
    * @param groupId the group ID
    * @param uid the uid
    * @return the number of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countByG_U(long groupId, java.lang.String uid)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countByG_U(groupId, uid);
    }

    /**
    * Returns all the contacts where groupId = &#63;.
    *
    * @param groupId the group ID
    * @return the matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByGroupId(
        long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    /**
    * Returns a range of all the contacts where groupId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param groupId the group ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @return the range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByGroupId(groupId, start, end);
    }

    /**
    * Returns an ordered range of all the contacts where groupId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param groupId the group ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByGroupId(groupId, start, end, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where groupId = &#63;.
    *
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByGroupId_First(
        long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByGroupId_First(groupId, orderByComparator);
    }

    /**
    * Returns the first contact in the ordered set where groupId = &#63;.
    *
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByGroupId_First(
        long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByGroupId_First(groupId, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where groupId = &#63;.
    *
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByGroupId_Last(
        long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByGroupId_Last(groupId, orderByComparator);
    }

    /**
    * Returns the last contact in the ordered set where groupId = &#63;.
    *
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching contact, or <code>null</code> if a matching contact could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByGroupId_Last(
        long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
    }

    /**
    * Returns the contacts before and after the current contact in the ordered set where groupId = &#63;.
    *
    * @param contactId the primary key of the current contact
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the previous, current, and next contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact[] findByGroupId_PrevAndNext(
        long contactId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .findByGroupId_PrevAndNext(contactId, groupId,
            orderByComparator);
    }

    /**
    * Returns all the contacts that the user has permission to view where groupId = &#63;.
    *
    * @param groupId the group ID
    * @return the matching contacts that the user has permission to view
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> filterFindByGroupId(
        long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().filterFindByGroupId(groupId);
    }

    /**
    * Returns a range of all the contacts that the user has permission to view where groupId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param groupId the group ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @return the range of matching contacts that the user has permission to view
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> filterFindByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().filterFindByGroupId(groupId, start, end);
    }

    /**
    * Returns an ordered range of all the contacts that the user has permissions to view where groupId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param groupId the group ID
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching contacts that the user has permission to view
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> filterFindByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .filterFindByGroupId(groupId, start, end, orderByComparator);
    }

    /**
    * Returns the contacts before and after the current contact in the ordered set of contacts that the user has permission to view where groupId = &#63;.
    *
    * @param contactId the primary key of the current contact
    * @param groupId the group ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the previous, current, and next contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact[] filterFindByGroupId_PrevAndNext(
        long contactId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence()
                   .filterFindByGroupId_PrevAndNext(contactId, groupId,
            orderByComparator);
    }

    /**
    * Removes all the contacts where groupId = &#63; from the database.
    *
    * @param groupId the group ID
    * @throws SystemException if a system exception occurred
    */
    public static void removeByGroupId(long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    /**
    * Returns the number of contacts where groupId = &#63;.
    *
    * @param groupId the group ID
    * @return the number of matching contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countByGroupId(long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    /**
    * Returns the number of contacts that the user has permission to view where groupId = &#63;.
    *
    * @param groupId the group ID
    * @return the number of matching contacts that the user has permission to view
    * @throws SystemException if a system exception occurred
    */
    public static int filterCountByGroupId(long groupId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().filterCountByGroupId(groupId);
    }

    /**
    * Caches the contact in the entity cache if it is enabled.
    *
    * @param contact the contact
    */
    public static void cacheResult(
        ch.inofix.portlet.contact.model.Contact contact) {
        getPersistence().cacheResult(contact);
    }

    /**
    * Caches the contacts in the entity cache if it is enabled.
    *
    * @param contacts the contacts
    */
    public static void cacheResult(
        java.util.List<ch.inofix.portlet.contact.model.Contact> contacts) {
        getPersistence().cacheResult(contacts);
    }

    /**
    * Creates a new contact with the primary key. Does not add the contact to the database.
    *
    * @param contactId the primary key for the new contact
    * @return the new contact
    */
    public static ch.inofix.portlet.contact.model.Contact create(long contactId) {
        return getPersistence().create(contactId);
    }

    /**
    * Removes the contact with the primary key from the database. Also notifies the appropriate model listeners.
    *
    * @param contactId the primary key of the contact
    * @return the contact that was removed
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact remove(long contactId)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().remove(contactId);
    }

    public static ch.inofix.portlet.contact.model.Contact updateImpl(
        ch.inofix.portlet.contact.model.Contact contact)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().updateImpl(contact);
    }

    /**
    * Returns the contact with the primary key or throws a {@link ch.inofix.portlet.contact.NoSuchContactException} if it could not be found.
    *
    * @param contactId the primary key of the contact
    * @return the contact
    * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact findByPrimaryKey(
        long contactId)
        throws ch.inofix.portlet.contact.NoSuchContactException,
            com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findByPrimaryKey(contactId);
    }

    /**
    * Returns the contact with the primary key or returns <code>null</code> if it could not be found.
    *
    * @param contactId the primary key of the contact
    * @return the contact, or <code>null</code> if a contact with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public static ch.inofix.portlet.contact.model.Contact fetchByPrimaryKey(
        long contactId)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().fetchByPrimaryKey(contactId);
    }

    /**
    * Returns all the contacts.
    *
    * @return the contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findAll()
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findAll();
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
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findAll(
        int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findAll(start, end);
    }

    /**
    * Returns an ordered range of all the contacts.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ch.inofix.portlet.contact.model.impl.ContactModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param start the lower bound of the range of contacts
    * @param end the upper bound of the range of contacts (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of contacts
    * @throws SystemException if a system exception occurred
    */
    public static java.util.List<ch.inofix.portlet.contact.model.Contact> findAll(
        int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().findAll(start, end, orderByComparator);
    }

    /**
    * Removes all the contacts from the database.
    *
    * @throws SystemException if a system exception occurred
    */
    public static void removeAll()
        throws com.liferay.portal.kernel.exception.SystemException {
        getPersistence().removeAll();
    }

    /**
    * Returns the number of contacts.
    *
    * @return the number of contacts
    * @throws SystemException if a system exception occurred
    */
    public static int countAll()
        throws com.liferay.portal.kernel.exception.SystemException {
        return getPersistence().countAll();
    }

    public static ContactPersistence getPersistence() {
        if (_persistence == null) {
            _persistence = (ContactPersistence) PortletBeanLocatorUtil.locate(ch.inofix.portlet.contact.service.ClpSerializer.getServletContextName(),
                    ContactPersistence.class.getName());

            ReferenceRegistry.registerReference(ContactUtil.class,
                "_persistence");
        }

        return _persistence;
    }

    /**
     * @deprecated As of 6.2.0
     */
    public void setPersistence(ContactPersistence persistence) {
    }
}
