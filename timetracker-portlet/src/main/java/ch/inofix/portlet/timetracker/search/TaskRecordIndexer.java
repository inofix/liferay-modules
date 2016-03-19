
package ch.inofix.portlet.timetracker.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.portlet.PortletURL;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil;
import ch.inofix.portlet.timetracker.service.permission.TaskRecordPermission;
import ch.inofix.portlet.timetracker.service.persistence.TaskRecordActionableDynamicQuery;

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
 * @author Christian Berndt
 * @created 2016-03-19 21:55
 * @modified 2016-03-19 21:55
 * @version 1.0.0
 */
public class TaskRecordIndexer extends BaseIndexer {

    public static final String[] CLASS_NAMES = {
        TaskRecord.class.getName()
    };

    public static final String PORTLET_ID = "timetracker";

    public TaskRecordIndexer() {

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
    public boolean hasPermission(
        PermissionChecker permissionChecker, String entryClassName,
        long entryClassPK, String actionId)
        throws Exception {

        return TaskRecordPermission.contains(
            permissionChecker, entryClassPK, ActionKeys.VIEW);
    }

    @Override
    protected void doDelete(Object obj)
        throws Exception {

        TaskRecord taskRecord = (TaskRecord) obj;

        deleteDocument(taskRecord.getCompanyId(), taskRecord.getTaskRecordId());

    }

    @Override
    protected Document doGetDocument(Object obj)
        throws Exception {

        TaskRecord taskRecord = (TaskRecord) obj;

        Document document = getBaseModelDocument(PORTLET_ID, taskRecord);

        // Set document field values (in alphabetical order)
        document.addKeyword(
            TaskRecordSearchTerms.DESCRIPTION, taskRecord.getDescription());
        document.addDate(
            TaskRecordSearchTerms.END_DATE, taskRecord.getEndDate());
        document.addKeyword(
            Field.GROUP_ID, getSiteGroupId(taskRecord.getGroupId()));
        document.addKeyword(Field.SCOPE_GROUP_ID, taskRecord.getGroupId());
        document.addDate(
            TaskRecordSearchTerms.START_DATE, taskRecord.getStartDate());
        document.addKeyword(
            TaskRecordSearchTerms.TASK_RECORD_ID, taskRecord.getTaskRecordId());
        document.addDate(Field.MODIFIED_DATE, taskRecord.getModifiedDate());
        document.addKeyword(
            TaskRecordSearchTerms.USER_ID, taskRecord.getUserId());
        document.addKeyword(
            TaskRecordSearchTerms.WORK_PACKAGE, taskRecord.getWorkPackage());

        return document;

    }

    @Override
    protected Summary doGetSummary(
        Document document, Locale locale, String snippet, PortletURL portletURL)
        throws Exception {

        Summary summary = createSummary(document);

        summary.setMaxContentLength(200);

        return summary;
    }

    @Override
    protected void doReindex(Object obj)
        throws Exception {

        TaskRecord taskRecord = (TaskRecord) obj;

        Document document = getDocument(taskRecord);

        SearchEngineUtil.updateDocument(
            getSearchEngineId(), taskRecord.getCompanyId(), document);
    }

    @Override
    protected void doReindex(String[] ids)
        throws Exception {

        long companyId = GetterUtil.getLong(ids[0]);

        reindexEntries(companyId);
    }

    @Override
    protected void doReindex(String className, long classPK)
        throws Exception {

        TaskRecord taskRecord =
            TaskRecordLocalServiceUtil.getTaskRecord(classPK);

        doReindex(taskRecord);
    }

    @Override
    protected String getPortletId(SearchContext searchContext) {

        return PORTLET_ID;
    }

    protected void reindexEntries(long companyId)
        throws PortalException, SystemException {

        final Collection<Document> documents = new ArrayList<Document>();

        ActionableDynamicQuery actionableDynamicQuery =
            new TaskRecordActionableDynamicQuery() {

                @Override
                protected void addCriteria(DynamicQuery dynamicQuery) {

                }

                @Override
                protected void performAction(Object object)
                    throws PortalException {

                    TaskRecord taskRecord = (TaskRecord) object;

                    Document document = getDocument(taskRecord);

                    documents.add(document);

                }

            };

        // TODO: Check Liferay-Indexer for a more recent implementation
        actionableDynamicQuery.setCompanyId(companyId);

        actionableDynamicQuery.performActions();

        SearchEngineUtil.updateDocuments(
            getSearchEngineId(), companyId, documents);

    }

}
