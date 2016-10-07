package ch.inofix.portlet.contact.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.portlet.PortletURL;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.permission.ContactPermission;
import ch.inofix.portlet.contact.service.persistence.ContactActionableDynamicQuery;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-20 13:28
 * @modified 2015-05-27 11:45
 * @version 1.0.3
 *
 */
public class ContactIndexer extends BaseIndexer {

    public static final String[] CLASS_NAMES = { Contact.class.getName() };

    public static final String PORTLET_ID = "contact-manager";

    public ContactIndexer() {
        setPermissionAware(true);
    }

    @Override
    public String[] getClassNames() {
        return CLASS_NAMES;
    }

    @Override
    public String getPortletId() {
        return PORTLET_ID;
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker,
            String entryClassName, long entryClassPK, String actionId)
            throws Exception {

        return ContactPermission.contains(permissionChecker, entryClassPK,
                ActionKeys.VIEW);
    }

    @Override
    protected void doDelete(Object obj) throws Exception {
        Contact contact = (Contact) obj;

        deleteDocument(contact.getCompanyId(), contact.getContactId());
    }

    @Override
    protected Document doGetDocument(Object obj) throws Exception {
        Contact contact = (Contact) obj;

        Document document = getBaseModelDocument(PORTLET_ID, contact);

        // Set document field values (in alphabetical order)

        document.addKeyword(ContactSearchTerms.COMPANY, contact.getCompany());
        document.addKeyword(ContactSearchTerms.CONTACT_ID,
                contact.getContactId());
        document.addText(Field.CONTENT, contact.getCard());
        document.addKeyword(ContactSearchTerms.EMAIL, contact.getEmail()
                .getAddress());
        // TODO: add default fax
        document.addKeyword(ContactSearchTerms.FULL_NAME,
                contact.getFullName(false));
        document.addKeyword(Field.GROUP_ID,
                getSiteGroupId(contact.getGroupId()));
        // TODO add default impp
        document.addDate(Field.MODIFIED_DATE, contact.getModifiedDate());
        document.addKeyword(ContactSearchTerms.NAME, contact.getName());
        document.addKeyword(ContactSearchTerms.PHONE, contact.getPhone()
                .getNumber());
        document.addKeyword(Field.SCOPE_GROUP_ID, contact.getGroupId());
        document.addText(Field.TITLE, contact.getFullName(true));

        return document;
    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale,
            String snippet, PortletURL portletURL) throws Exception {

        Summary summary = createSummary(document);

        summary.setMaxContentLength(200);

        return summary;
    }

    @Override
    protected void doReindex(Object obj) throws Exception {

        Contact contact = (Contact) obj;

        Document document = getDocument(contact);

        SearchEngineUtil.updateDocument(getSearchEngineId(),
                contact.getCompanyId(), document);
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {

        long companyId = GetterUtil.getLong(ids[0]);

        reindexEntries(companyId);
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {

        Contact contact = ContactLocalServiceUtil.getContact(classPK);

        doReindex(contact);
    }

    @Override
    protected String getPortletId(SearchContext searchContext) {
        return PORTLET_ID;
    }

    protected void reindexEntries(long companyId) throws PortalException,
            SystemException {

        final Collection<Document> documents = new ArrayList<Document>();

        ActionableDynamicQuery actionableDynamicQuery = new ContactActionableDynamicQuery() {

            @Override
            protected void addCriteria(DynamicQuery dynamicQuery) {
            }

            @Override
            protected void performAction(Object object) throws PortalException {
                Contact contact = (Contact) object;

                Document document = getDocument(contact);

                documents.add(document);

            }

        };

        // TODO: Check Liferay-Indexer for a more recent implementation
        actionableDynamicQuery.setCompanyId(companyId);

        actionableDynamicQuery.performActions();

        SearchEngineUtil.updateDocuments(getSearchEngineId(), companyId,
                documents);

    }

}
