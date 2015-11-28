package ch.inofix.portlet.contact.service.persistence;

import ch.inofix.portlet.contact.NoSuchContactException;
import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.model.impl.ContactImpl;
import ch.inofix.portlet.contact.model.impl.ContactModelImpl;
import ch.inofix.portlet.contact.service.persistence.ContactPersistence;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The persistence implementation for the contact service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ContactPersistence
 * @see ContactUtil
 * @generated
 */
public class ContactPersistenceImpl extends BasePersistenceImpl<Contact>
    implements ContactPersistence {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this class directly. Always use {@link ContactUtil} to access the contact persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
     */
    public static final String FINDER_CLASS_NAME_ENTITY = ContactImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
        ".List1";
    public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
        ".List2";
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
            new String[] {
                String.class.getName(),
                
            Integer.class.getName(), Integer.class.getName(),
                OrderByComparator.class.getName()
            });
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
            new String[] { String.class.getName() },
            ContactModelImpl.UUID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
            new String[] { String.class.getName() });
    private static final String _FINDER_COLUMN_UUID_UUID_1 = "contact.uuid IS NULL";
    private static final String _FINDER_COLUMN_UUID_UUID_2 = "contact.uuid = ?";
    private static final String _FINDER_COLUMN_UUID_UUID_3 = "(contact.uuid IS NULL OR contact.uuid = '')";
    public static final FinderPath FINDER_PATH_FETCH_BY_UUID_G = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
            new String[] { String.class.getName(), Long.class.getName() },
            ContactModelImpl.UUID_COLUMN_BITMASK |
            ContactModelImpl.GROUPID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_UUID_G = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
            new String[] { String.class.getName(), Long.class.getName() });
    private static final String _FINDER_COLUMN_UUID_G_UUID_1 = "contact.uuid IS NULL AND ";
    private static final String _FINDER_COLUMN_UUID_G_UUID_2 = "contact.uuid = ? AND ";
    private static final String _FINDER_COLUMN_UUID_G_UUID_3 = "(contact.uuid IS NULL OR contact.uuid = '') AND ";
    private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 = "contact.groupId = ?";
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
            new String[] {
                String.class.getName(), Long.class.getName(),
                
            Integer.class.getName(), Integer.class.getName(),
                OrderByComparator.class.getName()
            });
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C =
        new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
            new String[] { String.class.getName(), Long.class.getName() },
            ContactModelImpl.UUID_COLUMN_BITMASK |
            ContactModelImpl.COMPANYID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_UUID_C = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
            new String[] { String.class.getName(), Long.class.getName() });
    private static final String _FINDER_COLUMN_UUID_C_UUID_1 = "contact.uuid IS NULL AND ";
    private static final String _FINDER_COLUMN_UUID_C_UUID_2 = "contact.uuid = ? AND ";
    private static final String _FINDER_COLUMN_UUID_C_UUID_3 = "(contact.uuid IS NULL OR contact.uuid = '') AND ";
    private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 = "contact.companyId = ?";
    public static final FinderPath FINDER_PATH_FETCH_BY_G_U = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_ENTITY, "fetchByG_U",
            new String[] { Long.class.getName(), String.class.getName() },
            ContactModelImpl.GROUPID_COLUMN_BITMASK |
            ContactModelImpl.UID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_G_U = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_U",
            new String[] { Long.class.getName(), String.class.getName() });
    private static final String _FINDER_COLUMN_G_U_GROUPID_2 = "contact.groupId = ? AND ";
    private static final String _FINDER_COLUMN_G_U_UID_1 = "contact.uid IS NULL";
    private static final String _FINDER_COLUMN_G_U_UID_2 = "contact.uid = ?";
    private static final String _FINDER_COLUMN_G_U_UID_3 = "(contact.uid IS NULL OR contact.uid = '')";
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
            new String[] {
                Long.class.getName(),
                
            Integer.class.getName(), Integer.class.getName(),
                OrderByComparator.class.getName()
            });
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
        new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, ContactImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
            new String[] { Long.class.getName() },
            ContactModelImpl.GROUPID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
            new String[] { Long.class.getName() });
    private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "contact.groupId = ?";
    private static final String _SQL_SELECT_CONTACT = "SELECT contact FROM Contact contact";
    private static final String _SQL_SELECT_CONTACT_WHERE = "SELECT contact FROM Contact contact WHERE ";
    private static final String _SQL_COUNT_CONTACT = "SELECT COUNT(contact) FROM Contact contact";
    private static final String _SQL_COUNT_CONTACT_WHERE = "SELECT COUNT(contact) FROM Contact contact WHERE ";
    private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN = "contact.contactId";
    private static final String _FILTER_SQL_SELECT_CONTACT_WHERE = "SELECT DISTINCT {contact.*} FROM Inofix_Contact contact WHERE ";
    private static final String _FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_1 =
        "SELECT {Inofix_Contact.*} FROM (SELECT DISTINCT contact.contactId FROM Inofix_Contact contact WHERE ";
    private static final String _FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_2 =
        ") TEMP_TABLE INNER JOIN Inofix_Contact ON TEMP_TABLE.contactId = Inofix_Contact.contactId";
    private static final String _FILTER_SQL_COUNT_CONTACT_WHERE = "SELECT COUNT(DISTINCT contact.contactId) AS COUNT_VALUE FROM Inofix_Contact contact WHERE ";
    private static final String _FILTER_ENTITY_ALIAS = "contact";
    private static final String _FILTER_ENTITY_TABLE = "Inofix_Contact";
    private static final String _ORDER_BY_ENTITY_ALIAS = "contact.";
    private static final String _ORDER_BY_ENTITY_TABLE = "Inofix_Contact.";
    private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Contact exists with the primary key ";
    private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Contact exists with the key {";
    private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
                PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
    private static Log _log = LogFactoryUtil.getLog(ContactPersistenceImpl.class);
    private static Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
                "uuid"
            });
    private static Contact _nullContact = new ContactImpl() {
            @Override
            public Object clone() {
                return this;
            }

            @Override
            public CacheModel<Contact> toCacheModel() {
                return _nullContactCacheModel;
            }
        };

    private static CacheModel<Contact> _nullContactCacheModel = new CacheModel<Contact>() {
            @Override
            public Contact toEntityModel() {
                return _nullContact;
            }
        };

    public ContactPersistenceImpl() {
        setModelClass(Contact.class);
    }

    /**
     * Returns all the contacts where uuid = &#63;.
     *
     * @param uuid the uuid
     * @return the matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public List<Contact> findByUuid(String uuid) throws SystemException {
        return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
    @Override
    public List<Contact> findByUuid(String uuid, int start, int end)
        throws SystemException {
        return findByUuid(uuid, start, end, null);
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
    @Override
    public List<Contact> findByUuid(String uuid, int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        boolean pagination = true;
        FinderPath finderPath = null;
        Object[] finderArgs = null;

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            pagination = false;
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID;
            finderArgs = new Object[] { uuid };
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID;
            finderArgs = new Object[] { uuid, start, end, orderByComparator };
        }

        List<Contact> list = (List<Contact>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if ((list != null) && !list.isEmpty()) {
            for (Contact contact : list) {
                if (!Validator.equals(uuid, contact.getUuid())) {
                    list = null;

                    break;
                }
            }
        }

        if (list == null) {
            StringBundler query = null;

            if (orderByComparator != null) {
                query = new StringBundler(3 +
                        (orderByComparator.getOrderByFields().length * 3));
            } else {
                query = new StringBundler(3);
            }

            query.append(_SQL_SELECT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_UUID_2);
            }

            if (orderByComparator != null) {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);
            } else
             if (pagination) {
                query.append(ContactModelImpl.ORDER_BY_JPQL);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                if (!pagination) {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);

                    list = new UnmodifiableList<Contact>(list);
                } else {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end);
                }

                cacheResult(list);

                FinderCacheUtil.putResult(finderPath, finderArgs, list);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return list;
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
    @Override
    public Contact findByUuid_First(String uuid,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByUuid_First(uuid, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("uuid=");
        msg.append(uuid);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
    }

    /**
     * Returns the first contact in the ordered set where uuid = &#63;.
     *
     * @param uuid the uuid
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the first matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByUuid_First(String uuid,
        OrderByComparator orderByComparator) throws SystemException {
        List<Contact> list = findByUuid(uuid, 0, 1, orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact findByUuid_Last(String uuid,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByUuid_Last(uuid, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("uuid=");
        msg.append(uuid);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
    }

    /**
     * Returns the last contact in the ordered set where uuid = &#63;.
     *
     * @param uuid the uuid
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the last matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByUuid_Last(String uuid,
        OrderByComparator orderByComparator) throws SystemException {
        int count = countByUuid(uuid);

        if (count == 0) {
            return null;
        }

        List<Contact> list = findByUuid(uuid, count - 1, count,
                orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact[] findByUuid_PrevAndNext(long contactId, String uuid,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = findByPrimaryKey(contactId);

        Session session = null;

        try {
            session = openSession();

            Contact[] array = new ContactImpl[3];

            array[0] = getByUuid_PrevAndNext(session, contact, uuid,
                    orderByComparator, true);

            array[1] = contact;

            array[2] = getByUuid_PrevAndNext(session, contact, uuid,
                    orderByComparator, false);

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    protected Contact getByUuid_PrevAndNext(Session session, Contact contact,
        String uuid, OrderByComparator orderByComparator, boolean previous) {
        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(6 +
                    (orderByComparator.getOrderByFields().length * 6));
        } else {
            query = new StringBundler(3);
        }

        query.append(_SQL_SELECT_CONTACT_WHERE);

        boolean bindUuid = false;

        if (uuid == null) {
            query.append(_FINDER_COLUMN_UUID_UUID_1);
        } else if (uuid.equals(StringPool.BLANK)) {
            query.append(_FINDER_COLUMN_UUID_UUID_3);
        } else {
            bindUuid = true;

            query.append(_FINDER_COLUMN_UUID_UUID_2);
        }

        if (orderByComparator != null) {
            String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

            if (orderByConditionFields.length > 0) {
                query.append(WHERE_AND);
            }

            for (int i = 0; i < orderByConditionFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByConditionFields[i]);

                if ((i + 1) < orderByConditionFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN_HAS_NEXT);
                    } else {
                        query.append(WHERE_LESSER_THAN_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN);
                    } else {
                        query.append(WHERE_LESSER_THAN);
                    }
                }
            }

            query.append(ORDER_BY_CLAUSE);

            String[] orderByFields = orderByComparator.getOrderByFields();

            for (int i = 0; i < orderByFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByFields[i]);

                if ((i + 1) < orderByFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC_HAS_NEXT);
                    } else {
                        query.append(ORDER_BY_DESC_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC);
                    } else {
                        query.append(ORDER_BY_DESC);
                    }
                }
            }
        } else {
            query.append(ContactModelImpl.ORDER_BY_JPQL);
        }

        String sql = query.toString();

        Query q = session.createQuery(sql);

        q.setFirstResult(0);
        q.setMaxResults(2);

        QueryPos qPos = QueryPos.getInstance(q);

        if (bindUuid) {
            qPos.add(uuid);
        }

        if (orderByComparator != null) {
            Object[] values = orderByComparator.getOrderByConditionValues(contact);

            for (Object value : values) {
                qPos.add(value);
            }
        }

        List<Contact> list = q.list();

        if (list.size() == 2) {
            return list.get(1);
        } else {
            return null;
        }
    }

    /**
     * Removes all the contacts where uuid = &#63; from the database.
     *
     * @param uuid the uuid
     * @throws SystemException if a system exception occurred
     */
    @Override
    public void removeByUuid(String uuid) throws SystemException {
        for (Contact contact : findByUuid(uuid, QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, null)) {
            remove(contact);
        }
    }

    /**
     * Returns the number of contacts where uuid = &#63;.
     *
     * @param uuid the uuid
     * @return the number of matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countByUuid(String uuid) throws SystemException {
        FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID;

        Object[] finderArgs = new Object[] { uuid };

        Long count = (Long) FinderCacheUtil.getResult(finderPath, finderArgs,
                this);

        if (count == null) {
            StringBundler query = new StringBundler(2);

            query.append(_SQL_COUNT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_UUID_2);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(finderPath, finderArgs, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
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
    @Override
    public Contact findByUUID_G(String uuid, long groupId)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByUUID_G(uuid, groupId);

        if (contact == null) {
            StringBundler msg = new StringBundler(6);

            msg.append(_NO_SUCH_ENTITY_WITH_KEY);

            msg.append("uuid=");
            msg.append(uuid);

            msg.append(", groupId=");
            msg.append(groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchContactException(msg.toString());
        }

        return contact;
    }

    /**
     * Returns the contact where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
     *
     * @param uuid the uuid
     * @param groupId the group ID
     * @return the matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByUUID_G(String uuid, long groupId)
        throws SystemException {
        return fetchByUUID_G(uuid, groupId, true);
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
    @Override
    public Contact fetchByUUID_G(String uuid, long groupId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { uuid, groupId };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_UUID_G,
                    finderArgs, this);
        }

        if (result instanceof Contact) {
            Contact contact = (Contact) result;

            if (!Validator.equals(uuid, contact.getUuid()) ||
                    (groupId != contact.getGroupId())) {
                result = null;
            }
        }

        if (result == null) {
            StringBundler query = new StringBundler(4);

            query.append(_SQL_SELECT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_G_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_G_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_G_UUID_2);
            }

            query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                qPos.add(groupId);

                List<Contact> list = q.list();

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                        finderArgs, list);
                } else {
                    Contact contact = list.get(0);

                    result = contact;

                    cacheResult(contact);

                    if ((contact.getUuid() == null) ||
                            !contact.getUuid().equals(uuid) ||
                            (contact.getGroupId() != groupId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                            finderArgs, contact);
                    }
                }
            } catch (Exception e) {
                FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
                    finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        if (result instanceof List<?>) {
            return null;
        } else {
            return (Contact) result;
        }
    }

    /**
     * Removes the contact where uuid = &#63; and groupId = &#63; from the database.
     *
     * @param uuid the uuid
     * @param groupId the group ID
     * @return the contact that was removed
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact removeByUUID_G(String uuid, long groupId)
        throws NoSuchContactException, SystemException {
        Contact contact = findByUUID_G(uuid, groupId);

        return remove(contact);
    }

    /**
     * Returns the number of contacts where uuid = &#63; and groupId = &#63;.
     *
     * @param uuid the uuid
     * @param groupId the group ID
     * @return the number of matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countByUUID_G(String uuid, long groupId)
        throws SystemException {
        FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID_G;

        Object[] finderArgs = new Object[] { uuid, groupId };

        Long count = (Long) FinderCacheUtil.getResult(finderPath, finderArgs,
                this);

        if (count == null) {
            StringBundler query = new StringBundler(3);

            query.append(_SQL_COUNT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_G_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_G_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_G_UUID_2);
            }

            query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                qPos.add(groupId);

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(finderPath, finderArgs, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Returns all the contacts where uuid = &#63; and companyId = &#63;.
     *
     * @param uuid the uuid
     * @param companyId the company ID
     * @return the matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public List<Contact> findByUuid_C(String uuid, long companyId)
        throws SystemException {
        return findByUuid_C(uuid, companyId, QueryUtil.ALL_POS,
            QueryUtil.ALL_POS, null);
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
    @Override
    public List<Contact> findByUuid_C(String uuid, long companyId, int start,
        int end) throws SystemException {
        return findByUuid_C(uuid, companyId, start, end, null);
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
    @Override
    public List<Contact> findByUuid_C(String uuid, long companyId, int start,
        int end, OrderByComparator orderByComparator) throws SystemException {
        boolean pagination = true;
        FinderPath finderPath = null;
        Object[] finderArgs = null;

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            pagination = false;
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C;
            finderArgs = new Object[] { uuid, companyId };
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C;
            finderArgs = new Object[] {
                    uuid, companyId,
                    
                    start, end, orderByComparator
                };
        }

        List<Contact> list = (List<Contact>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if ((list != null) && !list.isEmpty()) {
            for (Contact contact : list) {
                if (!Validator.equals(uuid, contact.getUuid()) ||
                        (companyId != contact.getCompanyId())) {
                    list = null;

                    break;
                }
            }
        }

        if (list == null) {
            StringBundler query = null;

            if (orderByComparator != null) {
                query = new StringBundler(4 +
                        (orderByComparator.getOrderByFields().length * 3));
            } else {
                query = new StringBundler(4);
            }

            query.append(_SQL_SELECT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_C_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_C_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_C_UUID_2);
            }

            query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

            if (orderByComparator != null) {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);
            } else
             if (pagination) {
                query.append(ContactModelImpl.ORDER_BY_JPQL);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                qPos.add(companyId);

                if (!pagination) {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);

                    list = new UnmodifiableList<Contact>(list);
                } else {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end);
                }

                cacheResult(list);

                FinderCacheUtil.putResult(finderPath, finderArgs, list);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return list;
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
    @Override
    public Contact findByUuid_C_First(String uuid, long companyId,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByUuid_C_First(uuid, companyId, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(6);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("uuid=");
        msg.append(uuid);

        msg.append(", companyId=");
        msg.append(companyId);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
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
    @Override
    public Contact fetchByUuid_C_First(String uuid, long companyId,
        OrderByComparator orderByComparator) throws SystemException {
        List<Contact> list = findByUuid_C(uuid, companyId, 0, 1,
                orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact findByUuid_C_Last(String uuid, long companyId,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByUuid_C_Last(uuid, companyId, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(6);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("uuid=");
        msg.append(uuid);

        msg.append(", companyId=");
        msg.append(companyId);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
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
    @Override
    public Contact fetchByUuid_C_Last(String uuid, long companyId,
        OrderByComparator orderByComparator) throws SystemException {
        int count = countByUuid_C(uuid, companyId);

        if (count == 0) {
            return null;
        }

        List<Contact> list = findByUuid_C(uuid, companyId, count - 1, count,
                orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact[] findByUuid_C_PrevAndNext(long contactId, String uuid,
        long companyId, OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = findByPrimaryKey(contactId);

        Session session = null;

        try {
            session = openSession();

            Contact[] array = new ContactImpl[3];

            array[0] = getByUuid_C_PrevAndNext(session, contact, uuid,
                    companyId, orderByComparator, true);

            array[1] = contact;

            array[2] = getByUuid_C_PrevAndNext(session, contact, uuid,
                    companyId, orderByComparator, false);

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    protected Contact getByUuid_C_PrevAndNext(Session session, Contact contact,
        String uuid, long companyId, OrderByComparator orderByComparator,
        boolean previous) {
        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(6 +
                    (orderByComparator.getOrderByFields().length * 6));
        } else {
            query = new StringBundler(3);
        }

        query.append(_SQL_SELECT_CONTACT_WHERE);

        boolean bindUuid = false;

        if (uuid == null) {
            query.append(_FINDER_COLUMN_UUID_C_UUID_1);
        } else if (uuid.equals(StringPool.BLANK)) {
            query.append(_FINDER_COLUMN_UUID_C_UUID_3);
        } else {
            bindUuid = true;

            query.append(_FINDER_COLUMN_UUID_C_UUID_2);
        }

        query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

        if (orderByComparator != null) {
            String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

            if (orderByConditionFields.length > 0) {
                query.append(WHERE_AND);
            }

            for (int i = 0; i < orderByConditionFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByConditionFields[i]);

                if ((i + 1) < orderByConditionFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN_HAS_NEXT);
                    } else {
                        query.append(WHERE_LESSER_THAN_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN);
                    } else {
                        query.append(WHERE_LESSER_THAN);
                    }
                }
            }

            query.append(ORDER_BY_CLAUSE);

            String[] orderByFields = orderByComparator.getOrderByFields();

            for (int i = 0; i < orderByFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByFields[i]);

                if ((i + 1) < orderByFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC_HAS_NEXT);
                    } else {
                        query.append(ORDER_BY_DESC_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC);
                    } else {
                        query.append(ORDER_BY_DESC);
                    }
                }
            }
        } else {
            query.append(ContactModelImpl.ORDER_BY_JPQL);
        }

        String sql = query.toString();

        Query q = session.createQuery(sql);

        q.setFirstResult(0);
        q.setMaxResults(2);

        QueryPos qPos = QueryPos.getInstance(q);

        if (bindUuid) {
            qPos.add(uuid);
        }

        qPos.add(companyId);

        if (orderByComparator != null) {
            Object[] values = orderByComparator.getOrderByConditionValues(contact);

            for (Object value : values) {
                qPos.add(value);
            }
        }

        List<Contact> list = q.list();

        if (list.size() == 2) {
            return list.get(1);
        } else {
            return null;
        }
    }

    /**
     * Removes all the contacts where uuid = &#63; and companyId = &#63; from the database.
     *
     * @param uuid the uuid
     * @param companyId the company ID
     * @throws SystemException if a system exception occurred
     */
    @Override
    public void removeByUuid_C(String uuid, long companyId)
        throws SystemException {
        for (Contact contact : findByUuid_C(uuid, companyId, QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, null)) {
            remove(contact);
        }
    }

    /**
     * Returns the number of contacts where uuid = &#63; and companyId = &#63;.
     *
     * @param uuid the uuid
     * @param companyId the company ID
     * @return the number of matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countByUuid_C(String uuid, long companyId)
        throws SystemException {
        FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID_C;

        Object[] finderArgs = new Object[] { uuid, companyId };

        Long count = (Long) FinderCacheUtil.getResult(finderPath, finderArgs,
                this);

        if (count == null) {
            StringBundler query = new StringBundler(3);

            query.append(_SQL_COUNT_CONTACT_WHERE);

            boolean bindUuid = false;

            if (uuid == null) {
                query.append(_FINDER_COLUMN_UUID_C_UUID_1);
            } else if (uuid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_UUID_C_UUID_3);
            } else {
                bindUuid = true;

                query.append(_FINDER_COLUMN_UUID_C_UUID_2);
            }

            query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (bindUuid) {
                    qPos.add(uuid);
                }

                qPos.add(companyId);

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(finderPath, finderArgs, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
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
    @Override
    public Contact findByG_U(long groupId, String uid)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByG_U(groupId, uid);

        if (contact == null) {
            StringBundler msg = new StringBundler(6);

            msg.append(_NO_SUCH_ENTITY_WITH_KEY);

            msg.append("groupId=");
            msg.append(groupId);

            msg.append(", uid=");
            msg.append(uid);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchContactException(msg.toString());
        }

        return contact;
    }

    /**
     * Returns the contact where groupId = &#63; and uid = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
     *
     * @param groupId the group ID
     * @param uid the uid
     * @return the matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByG_U(long groupId, String uid)
        throws SystemException {
        return fetchByG_U(groupId, uid, true);
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
    @Override
    public Contact fetchByG_U(long groupId, String uid,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { groupId, uid };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_U,
                    finderArgs, this);
        }

        if (result instanceof Contact) {
            Contact contact = (Contact) result;

            if ((groupId != contact.getGroupId()) ||
                    !Validator.equals(uid, contact.getUid())) {
                result = null;
            }
        }

        if (result == null) {
            StringBundler query = new StringBundler(4);

            query.append(_SQL_SELECT_CONTACT_WHERE);

            query.append(_FINDER_COLUMN_G_U_GROUPID_2);

            boolean bindUid = false;

            if (uid == null) {
                query.append(_FINDER_COLUMN_G_U_UID_1);
            } else if (uid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_G_U_UID_3);
            } else {
                bindUid = true;

                query.append(_FINDER_COLUMN_G_U_UID_2);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                if (bindUid) {
                    qPos.add(uid);
                }

                List<Contact> list = q.list();

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_U,
                        finderArgs, list);
                } else {
                    Contact contact = list.get(0);

                    result = contact;

                    cacheResult(contact);

                    if ((contact.getGroupId() != groupId) ||
                            (contact.getUid() == null) ||
                            !contact.getUid().equals(uid)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_U,
                            finderArgs, contact);
                    }
                }
            } catch (Exception e) {
                FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_U,
                    finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        if (result instanceof List<?>) {
            return null;
        } else {
            return (Contact) result;
        }
    }

    /**
     * Removes the contact where groupId = &#63; and uid = &#63; from the database.
     *
     * @param groupId the group ID
     * @param uid the uid
     * @return the contact that was removed
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact removeByG_U(long groupId, String uid)
        throws NoSuchContactException, SystemException {
        Contact contact = findByG_U(groupId, uid);

        return remove(contact);
    }

    /**
     * Returns the number of contacts where groupId = &#63; and uid = &#63;.
     *
     * @param groupId the group ID
     * @param uid the uid
     * @return the number of matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countByG_U(long groupId, String uid) throws SystemException {
        FinderPath finderPath = FINDER_PATH_COUNT_BY_G_U;

        Object[] finderArgs = new Object[] { groupId, uid };

        Long count = (Long) FinderCacheUtil.getResult(finderPath, finderArgs,
                this);

        if (count == null) {
            StringBundler query = new StringBundler(3);

            query.append(_SQL_COUNT_CONTACT_WHERE);

            query.append(_FINDER_COLUMN_G_U_GROUPID_2);

            boolean bindUid = false;

            if (uid == null) {
                query.append(_FINDER_COLUMN_G_U_UID_1);
            } else if (uid.equals(StringPool.BLANK)) {
                query.append(_FINDER_COLUMN_G_U_UID_3);
            } else {
                bindUid = true;

                query.append(_FINDER_COLUMN_G_U_UID_2);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                if (bindUid) {
                    qPos.add(uid);
                }

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(finderPath, finderArgs, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Returns all the contacts where groupId = &#63;.
     *
     * @param groupId the group ID
     * @return the matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public List<Contact> findByGroupId(long groupId) throws SystemException {
        return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
    @Override
    public List<Contact> findByGroupId(long groupId, int start, int end)
        throws SystemException {
        return findByGroupId(groupId, start, end, null);
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
    @Override
    public List<Contact> findByGroupId(long groupId, int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        boolean pagination = true;
        FinderPath finderPath = null;
        Object[] finderArgs = null;

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            pagination = false;
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
            finderArgs = new Object[] { groupId };
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
            finderArgs = new Object[] { groupId, start, end, orderByComparator };
        }

        List<Contact> list = (List<Contact>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if ((list != null) && !list.isEmpty()) {
            for (Contact contact : list) {
                if ((groupId != contact.getGroupId())) {
                    list = null;

                    break;
                }
            }
        }

        if (list == null) {
            StringBundler query = null;

            if (orderByComparator != null) {
                query = new StringBundler(3 +
                        (orderByComparator.getOrderByFields().length * 3));
            } else {
                query = new StringBundler(3);
            }

            query.append(_SQL_SELECT_CONTACT_WHERE);

            query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

            if (orderByComparator != null) {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);
            } else
             if (pagination) {
                query.append(ContactModelImpl.ORDER_BY_JPQL);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                if (!pagination) {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);

                    list = new UnmodifiableList<Contact>(list);
                } else {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end);
                }

                cacheResult(list);

                FinderCacheUtil.putResult(finderPath, finderArgs, list);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return list;
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
    @Override
    public Contact findByGroupId_First(long groupId,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByGroupId_First(groupId, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("groupId=");
        msg.append(groupId);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
    }

    /**
     * Returns the first contact in the ordered set where groupId = &#63;.
     *
     * @param groupId the group ID
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the first matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByGroupId_First(long groupId,
        OrderByComparator orderByComparator) throws SystemException {
        List<Contact> list = findByGroupId(groupId, 0, 1, orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact findByGroupId_Last(long groupId,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByGroupId_Last(groupId, orderByComparator);

        if (contact != null) {
            return contact;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("groupId=");
        msg.append(groupId);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchContactException(msg.toString());
    }

    /**
     * Returns the last contact in the ordered set where groupId = &#63;.
     *
     * @param groupId the group ID
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the last matching contact, or <code>null</code> if a matching contact could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByGroupId_Last(long groupId,
        OrderByComparator orderByComparator) throws SystemException {
        int count = countByGroupId(groupId);

        if (count == 0) {
            return null;
        }

        List<Contact> list = findByGroupId(groupId, count - 1, count,
                orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
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
    @Override
    public Contact[] findByGroupId_PrevAndNext(long contactId, long groupId,
        OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        Contact contact = findByPrimaryKey(contactId);

        Session session = null;

        try {
            session = openSession();

            Contact[] array = new ContactImpl[3];

            array[0] = getByGroupId_PrevAndNext(session, contact, groupId,
                    orderByComparator, true);

            array[1] = contact;

            array[2] = getByGroupId_PrevAndNext(session, contact, groupId,
                    orderByComparator, false);

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    protected Contact getByGroupId_PrevAndNext(Session session,
        Contact contact, long groupId, OrderByComparator orderByComparator,
        boolean previous) {
        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(6 +
                    (orderByComparator.getOrderByFields().length * 6));
        } else {
            query = new StringBundler(3);
        }

        query.append(_SQL_SELECT_CONTACT_WHERE);

        query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

        if (orderByComparator != null) {
            String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

            if (orderByConditionFields.length > 0) {
                query.append(WHERE_AND);
            }

            for (int i = 0; i < orderByConditionFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByConditionFields[i]);

                if ((i + 1) < orderByConditionFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN_HAS_NEXT);
                    } else {
                        query.append(WHERE_LESSER_THAN_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN);
                    } else {
                        query.append(WHERE_LESSER_THAN);
                    }
                }
            }

            query.append(ORDER_BY_CLAUSE);

            String[] orderByFields = orderByComparator.getOrderByFields();

            for (int i = 0; i < orderByFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByFields[i]);

                if ((i + 1) < orderByFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC_HAS_NEXT);
                    } else {
                        query.append(ORDER_BY_DESC_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC);
                    } else {
                        query.append(ORDER_BY_DESC);
                    }
                }
            }
        } else {
            query.append(ContactModelImpl.ORDER_BY_JPQL);
        }

        String sql = query.toString();

        Query q = session.createQuery(sql);

        q.setFirstResult(0);
        q.setMaxResults(2);

        QueryPos qPos = QueryPos.getInstance(q);

        qPos.add(groupId);

        if (orderByComparator != null) {
            Object[] values = orderByComparator.getOrderByConditionValues(contact);

            for (Object value : values) {
                qPos.add(value);
            }
        }

        List<Contact> list = q.list();

        if (list.size() == 2) {
            return list.get(1);
        } else {
            return null;
        }
    }

    /**
     * Returns all the contacts that the user has permission to view where groupId = &#63;.
     *
     * @param groupId the group ID
     * @return the matching contacts that the user has permission to view
     * @throws SystemException if a system exception occurred
     */
    @Override
    public List<Contact> filterFindByGroupId(long groupId)
        throws SystemException {
        return filterFindByGroupId(groupId, QueryUtil.ALL_POS,
            QueryUtil.ALL_POS, null);
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
    @Override
    public List<Contact> filterFindByGroupId(long groupId, int start, int end)
        throws SystemException {
        return filterFindByGroupId(groupId, start, end, null);
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
    @Override
    public List<Contact> filterFindByGroupId(long groupId, int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        if (!InlineSQLHelperUtil.isEnabled(groupId)) {
            return findByGroupId(groupId, start, end, orderByComparator);
        }

        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(3 +
                    (orderByComparator.getOrderByFields().length * 3));
        } else {
            query = new StringBundler(3);
        }

        if (getDB().isSupportsInlineDistinct()) {
            query.append(_FILTER_SQL_SELECT_CONTACT_WHERE);
        } else {
            query.append(_FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_1);
        }

        query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

        if (!getDB().isSupportsInlineDistinct()) {
            query.append(_FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_2);
        }

        if (orderByComparator != null) {
            if (getDB().isSupportsInlineDistinct()) {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator, true);
            } else {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_TABLE,
                    orderByComparator, true);
            }
        } else {
            if (getDB().isSupportsInlineDistinct()) {
                query.append(ContactModelImpl.ORDER_BY_JPQL);
            } else {
                query.append(ContactModelImpl.ORDER_BY_SQL);
            }
        }

        String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
                Contact.class.getName(), _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN,
                groupId);

        Session session = null;

        try {
            session = openSession();

            SQLQuery q = session.createSQLQuery(sql);

            if (getDB().isSupportsInlineDistinct()) {
                q.addEntity(_FILTER_ENTITY_ALIAS, ContactImpl.class);
            } else {
                q.addEntity(_FILTER_ENTITY_TABLE, ContactImpl.class);
            }

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            return (List<Contact>) QueryUtil.list(q, getDialect(), start, end);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
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
    @Override
    public Contact[] filterFindByGroupId_PrevAndNext(long contactId,
        long groupId, OrderByComparator orderByComparator)
        throws NoSuchContactException, SystemException {
        if (!InlineSQLHelperUtil.isEnabled(groupId)) {
            return findByGroupId_PrevAndNext(contactId, groupId,
                orderByComparator);
        }

        Contact contact = findByPrimaryKey(contactId);

        Session session = null;

        try {
            session = openSession();

            Contact[] array = new ContactImpl[3];

            array[0] = filterGetByGroupId_PrevAndNext(session, contact,
                    groupId, orderByComparator, true);

            array[1] = contact;

            array[2] = filterGetByGroupId_PrevAndNext(session, contact,
                    groupId, orderByComparator, false);

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    protected Contact filterGetByGroupId_PrevAndNext(Session session,
        Contact contact, long groupId, OrderByComparator orderByComparator,
        boolean previous) {
        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(6 +
                    (orderByComparator.getOrderByFields().length * 6));
        } else {
            query = new StringBundler(3);
        }

        if (getDB().isSupportsInlineDistinct()) {
            query.append(_FILTER_SQL_SELECT_CONTACT_WHERE);
        } else {
            query.append(_FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_1);
        }

        query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

        if (!getDB().isSupportsInlineDistinct()) {
            query.append(_FILTER_SQL_SELECT_CONTACT_NO_INLINE_DISTINCT_WHERE_2);
        }

        if (orderByComparator != null) {
            String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

            if (orderByConditionFields.length > 0) {
                query.append(WHERE_AND);
            }

            for (int i = 0; i < orderByConditionFields.length; i++) {
                if (getDB().isSupportsInlineDistinct()) {
                    query.append(_ORDER_BY_ENTITY_ALIAS);
                } else {
                    query.append(_ORDER_BY_ENTITY_TABLE);
                }

                query.append(orderByConditionFields[i]);

                if ((i + 1) < orderByConditionFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN_HAS_NEXT);
                    } else {
                        query.append(WHERE_LESSER_THAN_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN);
                    } else {
                        query.append(WHERE_LESSER_THAN);
                    }
                }
            }

            query.append(ORDER_BY_CLAUSE);

            String[] orderByFields = orderByComparator.getOrderByFields();

            for (int i = 0; i < orderByFields.length; i++) {
                if (getDB().isSupportsInlineDistinct()) {
                    query.append(_ORDER_BY_ENTITY_ALIAS);
                } else {
                    query.append(_ORDER_BY_ENTITY_TABLE);
                }

                query.append(orderByFields[i]);

                if ((i + 1) < orderByFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC_HAS_NEXT);
                    } else {
                        query.append(ORDER_BY_DESC_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC);
                    } else {
                        query.append(ORDER_BY_DESC);
                    }
                }
            }
        } else {
            if (getDB().isSupportsInlineDistinct()) {
                query.append(ContactModelImpl.ORDER_BY_JPQL);
            } else {
                query.append(ContactModelImpl.ORDER_BY_SQL);
            }
        }

        String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
                Contact.class.getName(), _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN,
                groupId);

        SQLQuery q = session.createSQLQuery(sql);

        q.setFirstResult(0);
        q.setMaxResults(2);

        if (getDB().isSupportsInlineDistinct()) {
            q.addEntity(_FILTER_ENTITY_ALIAS, ContactImpl.class);
        } else {
            q.addEntity(_FILTER_ENTITY_TABLE, ContactImpl.class);
        }

        QueryPos qPos = QueryPos.getInstance(q);

        qPos.add(groupId);

        if (orderByComparator != null) {
            Object[] values = orderByComparator.getOrderByConditionValues(contact);

            for (Object value : values) {
                qPos.add(value);
            }
        }

        List<Contact> list = q.list();

        if (list.size() == 2) {
            return list.get(1);
        } else {
            return null;
        }
    }

    /**
     * Removes all the contacts where groupId = &#63; from the database.
     *
     * @param groupId the group ID
     * @throws SystemException if a system exception occurred
     */
    @Override
    public void removeByGroupId(long groupId) throws SystemException {
        for (Contact contact : findByGroupId(groupId, QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, null)) {
            remove(contact);
        }
    }

    /**
     * Returns the number of contacts where groupId = &#63;.
     *
     * @param groupId the group ID
     * @return the number of matching contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countByGroupId(long groupId) throws SystemException {
        FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

        Object[] finderArgs = new Object[] { groupId };

        Long count = (Long) FinderCacheUtil.getResult(finderPath, finderArgs,
                this);

        if (count == null) {
            StringBundler query = new StringBundler(2);

            query.append(_SQL_COUNT_CONTACT_WHERE);

            query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(finderPath, finderArgs, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Returns the number of contacts that the user has permission to view where groupId = &#63;.
     *
     * @param groupId the group ID
     * @return the number of matching contacts that the user has permission to view
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int filterCountByGroupId(long groupId) throws SystemException {
        if (!InlineSQLHelperUtil.isEnabled(groupId)) {
            return countByGroupId(groupId);
        }

        StringBundler query = new StringBundler(2);

        query.append(_FILTER_SQL_COUNT_CONTACT_WHERE);

        query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

        String sql = InlineSQLHelperUtil.replacePermissionCheck(query.toString(),
                Contact.class.getName(), _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN,
                groupId);

        Session session = null;

        try {
            session = openSession();

            SQLQuery q = session.createSQLQuery(sql);

            q.addScalar(COUNT_COLUMN_NAME,
                com.liferay.portal.kernel.dao.orm.Type.LONG);

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            Long count = (Long) q.uniqueResult();

            return count.intValue();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    /**
     * Caches the contact in the entity cache if it is enabled.
     *
     * @param contact the contact
     */
    @Override
    public void cacheResult(Contact contact) {
        EntityCacheUtil.putResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactImpl.class, contact.getPrimaryKey(), contact);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
            new Object[] { contact.getUuid(), contact.getGroupId() }, contact);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_U,
            new Object[] { contact.getGroupId(), contact.getUid() }, contact);

        contact.resetOriginalValues();
    }

    /**
     * Caches the contacts in the entity cache if it is enabled.
     *
     * @param contacts the contacts
     */
    @Override
    public void cacheResult(List<Contact> contacts) {
        for (Contact contact : contacts) {
            if (EntityCacheUtil.getResult(
                        ContactModelImpl.ENTITY_CACHE_ENABLED,
                        ContactImpl.class, contact.getPrimaryKey()) == null) {
                cacheResult(contact);
            } else {
                contact.resetOriginalValues();
            }
        }
    }

    /**
     * Clears the cache for all contacts.
     *
     * <p>
     * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
     * </p>
     */
    @Override
    public void clearCache() {
        if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
            CacheRegistryUtil.clear(ContactImpl.class.getName());
        }

        EntityCacheUtil.clearCache(ContactImpl.class.getName());

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
    }

    /**
     * Clears the cache for the contact.
     *
     * <p>
     * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
     * </p>
     */
    @Override
    public void clearCache(Contact contact) {
        EntityCacheUtil.removeResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactImpl.class, contact.getPrimaryKey());

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

        clearUniqueFindersCache(contact);
    }

    @Override
    public void clearCache(List<Contact> contacts) {
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

        for (Contact contact : contacts) {
            EntityCacheUtil.removeResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
                ContactImpl.class, contact.getPrimaryKey());

            clearUniqueFindersCache(contact);
        }
    }

    protected void cacheUniqueFindersCache(Contact contact) {
        if (contact.isNew()) {
            Object[] args = new Object[] { contact.getUuid(), contact.getGroupId() };

            FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G, args,
                Long.valueOf(1));
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G, args, contact);

            args = new Object[] { contact.getGroupId(), contact.getUid() };

            FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_U, args,
                Long.valueOf(1));
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_U, args, contact);
        } else {
            ContactModelImpl contactModelImpl = (ContactModelImpl) contact;

            if ((contactModelImpl.getColumnBitmask() &
                    FINDER_PATH_FETCH_BY_UUID_G.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        contact.getUuid(), contact.getGroupId()
                    };

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G, args,
                    Long.valueOf(1));
                FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G, args,
                    contact);
            }

            if ((contactModelImpl.getColumnBitmask() &
                    FINDER_PATH_FETCH_BY_G_U.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        contact.getGroupId(), contact.getUid()
                    };

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_U, args,
                    Long.valueOf(1));
                FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_U, args,
                    contact);
            }
        }
    }

    protected void clearUniqueFindersCache(Contact contact) {
        ContactModelImpl contactModelImpl = (ContactModelImpl) contact;

        Object[] args = new Object[] { contact.getUuid(), contact.getGroupId() };

        FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_G, args);
        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G, args);

        if ((contactModelImpl.getColumnBitmask() &
                FINDER_PATH_FETCH_BY_UUID_G.getColumnBitmask()) != 0) {
            args = new Object[] {
                    contactModelImpl.getOriginalUuid(),
                    contactModelImpl.getOriginalGroupId()
                };

            FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_G, args);
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G, args);
        }

        args = new Object[] { contact.getGroupId(), contact.getUid() };

        FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_U, args);
        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_U, args);

        if ((contactModelImpl.getColumnBitmask() &
                FINDER_PATH_FETCH_BY_G_U.getColumnBitmask()) != 0) {
            args = new Object[] {
                    contactModelImpl.getOriginalGroupId(),
                    contactModelImpl.getOriginalUid()
                };

            FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_U, args);
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_U, args);
        }
    }

    /**
     * Creates a new contact with the primary key. Does not add the contact to the database.
     *
     * @param contactId the primary key for the new contact
     * @return the new contact
     */
    @Override
    public Contact create(long contactId) {
        Contact contact = new ContactImpl();

        contact.setNew(true);
        contact.setPrimaryKey(contactId);

        String uuid = PortalUUIDUtil.generate();

        contact.setUuid(uuid);

        return contact;
    }

    /**
     * Removes the contact with the primary key from the database. Also notifies the appropriate model listeners.
     *
     * @param contactId the primary key of the contact
     * @return the contact that was removed
     * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact remove(long contactId)
        throws NoSuchContactException, SystemException {
        return remove((Serializable) contactId);
    }

    /**
     * Removes the contact with the primary key from the database. Also notifies the appropriate model listeners.
     *
     * @param primaryKey the primary key of the contact
     * @return the contact that was removed
     * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact remove(Serializable primaryKey)
        throws NoSuchContactException, SystemException {
        Session session = null;

        try {
            session = openSession();

            Contact contact = (Contact) session.get(ContactImpl.class,
                    primaryKey);

            if (contact == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
                }

                throw new NoSuchContactException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
                    primaryKey);
            }

            return remove(contact);
        } catch (NoSuchContactException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    @Override
    protected Contact removeImpl(Contact contact) throws SystemException {
        contact = toUnwrappedModel(contact);

        Session session = null;

        try {
            session = openSession();

            if (!session.contains(contact)) {
                contact = (Contact) session.get(ContactImpl.class,
                        contact.getPrimaryKeyObj());
            }

            if (contact != null) {
                session.delete(contact);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        if (contact != null) {
            clearCache(contact);
        }

        return contact;
    }

    @Override
    public Contact updateImpl(ch.inofix.portlet.contact.model.Contact contact)
        throws SystemException {
        contact = toUnwrappedModel(contact);

        boolean isNew = contact.isNew();

        ContactModelImpl contactModelImpl = (ContactModelImpl) contact;

        if (Validator.isNull(contact.getUuid())) {
            String uuid = PortalUUIDUtil.generate();

            contact.setUuid(uuid);
        }

        Session session = null;

        try {
            session = openSession();

            if (contact.isNew()) {
                session.save(contact);

                contact.setNew(false);
            } else {
                session.merge(contact);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

        if (isNew || !ContactModelImpl.COLUMN_BITMASK_ENABLED) {
            FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
        }
        else {
            if ((contactModelImpl.getColumnBitmask() &
                    FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
                Object[] args = new Object[] { contactModelImpl.getOriginalUuid() };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
                    args);

                args = new Object[] { contactModelImpl.getUuid() };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
                    args);
            }

            if ((contactModelImpl.getColumnBitmask() &
                    FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        contactModelImpl.getOriginalUuid(),
                        contactModelImpl.getOriginalCompanyId()
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
                    args);

                args = new Object[] {
                        contactModelImpl.getUuid(),
                        contactModelImpl.getCompanyId()
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
                    args);
            }

            if ((contactModelImpl.getColumnBitmask() &
                    FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        contactModelImpl.getOriginalGroupId()
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
                    args);

                args = new Object[] { contactModelImpl.getGroupId() };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
                    args);
            }
        }

        EntityCacheUtil.putResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
            ContactImpl.class, contact.getPrimaryKey(), contact);

        clearUniqueFindersCache(contact);
        cacheUniqueFindersCache(contact);

        return contact;
    }

    protected Contact toUnwrappedModel(Contact contact) {
        if (contact instanceof ContactImpl) {
            return contact;
        }

        ContactImpl contactImpl = new ContactImpl();

        contactImpl.setNew(contact.isNew());
        contactImpl.setPrimaryKey(contact.getPrimaryKey());

        contactImpl.setUuid(contact.getUuid());
        contactImpl.setContactId(contact.getContactId());
        contactImpl.setGroupId(contact.getGroupId());
        contactImpl.setCompanyId(contact.getCompanyId());
        contactImpl.setUserId(contact.getUserId());
        contactImpl.setUserName(contact.getUserName());
        contactImpl.setCreateDate(contact.getCreateDate());
        contactImpl.setModifiedDate(contact.getModifiedDate());
        contactImpl.setParentContactId(contact.getParentContactId());
        contactImpl.setCard(contact.getCard());
        contactImpl.setUid(contact.getUid());
        contactImpl.setStatus(contact.getStatus());

        return contactImpl;
    }

    /**
     * Returns the contact with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
     *
     * @param primaryKey the primary key of the contact
     * @return the contact
     * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact findByPrimaryKey(Serializable primaryKey)
        throws NoSuchContactException, SystemException {
        Contact contact = fetchByPrimaryKey(primaryKey);

        if (contact == null) {
            if (_log.isWarnEnabled()) {
                _log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
            }

            throw new NoSuchContactException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
                primaryKey);
        }

        return contact;
    }

    /**
     * Returns the contact with the primary key or throws a {@link ch.inofix.portlet.contact.NoSuchContactException} if it could not be found.
     *
     * @param contactId the primary key of the contact
     * @return the contact
     * @throws ch.inofix.portlet.contact.NoSuchContactException if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact findByPrimaryKey(long contactId)
        throws NoSuchContactException, SystemException {
        return findByPrimaryKey((Serializable) contactId);
    }

    /**
     * Returns the contact with the primary key or returns <code>null</code> if it could not be found.
     *
     * @param primaryKey the primary key of the contact
     * @return the contact, or <code>null</code> if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByPrimaryKey(Serializable primaryKey)
        throws SystemException {
        Contact contact = (Contact) EntityCacheUtil.getResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
                ContactImpl.class, primaryKey);

        if (contact == _nullContact) {
            return null;
        }

        if (contact == null) {
            Session session = null;

            try {
                session = openSession();

                contact = (Contact) session.get(ContactImpl.class, primaryKey);

                if (contact != null) {
                    cacheResult(contact);
                } else {
                    EntityCacheUtil.putResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
                        ContactImpl.class, primaryKey, _nullContact);
                }
            } catch (Exception e) {
                EntityCacheUtil.removeResult(ContactModelImpl.ENTITY_CACHE_ENABLED,
                    ContactImpl.class, primaryKey);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return contact;
    }

    /**
     * Returns the contact with the primary key or returns <code>null</code> if it could not be found.
     *
     * @param contactId the primary key of the contact
     * @return the contact, or <code>null</code> if a contact with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public Contact fetchByPrimaryKey(long contactId) throws SystemException {
        return fetchByPrimaryKey((Serializable) contactId);
    }

    /**
     * Returns all the contacts.
     *
     * @return the contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public List<Contact> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
    public List<Contact> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
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
    @Override
    public List<Contact> findAll(int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        boolean pagination = true;
        FinderPath finderPath = null;
        Object[] finderArgs = null;

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            pagination = false;
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
            finderArgs = FINDER_ARGS_EMPTY;
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
            finderArgs = new Object[] { start, end, orderByComparator };
        }

        List<Contact> list = (List<Contact>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if (list == null) {
            StringBundler query = null;
            String sql = null;

            if (orderByComparator != null) {
                query = new StringBundler(2 +
                        (orderByComparator.getOrderByFields().length * 3));

                query.append(_SQL_SELECT_CONTACT);

                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);

                sql = query.toString();
            } else {
                sql = _SQL_SELECT_CONTACT;

                if (pagination) {
                    sql = sql.concat(ContactModelImpl.ORDER_BY_JPQL);
                }
            }

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                if (!pagination) {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);

                    list = new UnmodifiableList<Contact>(list);
                } else {
                    list = (List<Contact>) QueryUtil.list(q, getDialect(),
                            start, end);
                }

                cacheResult(list);

                FinderCacheUtil.putResult(finderPath, finderArgs, list);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(finderPath, finderArgs);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return list;
    }

    /**
     * Removes all the contacts from the database.
     *
     * @throws SystemException if a system exception occurred
     */
    @Override
    public void removeAll() throws SystemException {
        for (Contact contact : findAll()) {
            remove(contact);
        }
    }

    /**
     * Returns the number of contacts.
     *
     * @return the number of contacts
     * @throws SystemException if a system exception occurred
     */
    @Override
    public int countAll() throws SystemException {
        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
                FINDER_ARGS_EMPTY, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(_SQL_COUNT_CONTACT);

                count = (Long) q.uniqueResult();

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
                    FINDER_ARGS_EMPTY, count);
            } catch (Exception e) {
                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
                    FINDER_ARGS_EMPTY);

                throw processException(e);
            } finally {
                closeSession(session);
            }
        }

        return count.intValue();
    }

    @Override
    protected Set<String> getBadColumnNames() {
        return _badColumnNames;
    }

    /**
     * Initializes the contact persistence.
     */
    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
                    com.liferay.util.service.ServiceProps.get(
                        "value.object.listener.ch.inofix.portlet.contact.model.Contact")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<Contact>> listenersList = new ArrayList<ModelListener<Contact>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<Contact>) InstanceFactory.newInstance(
                            getClassLoader(), listenerClassName));
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }

    public void destroy() {
        EntityCacheUtil.removeCache(ContactImpl.class.getName());
        FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
    }
}
