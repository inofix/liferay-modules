package ch.inofix.portlet.contact.service.persistence;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.BaseActionableDynamicQuery;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public abstract class ContactActionableDynamicQuery
    extends BaseActionableDynamicQuery {
    public ContactActionableDynamicQuery() throws SystemException {
        setBaseLocalService(ContactLocalServiceUtil.getService());
        setClass(Contact.class);

        setClassLoader(ch.inofix.portlet.contact.service.ClpSerializer.class.getClassLoader());

        setPrimaryKeyPropertyName("contactId");
    }
}
