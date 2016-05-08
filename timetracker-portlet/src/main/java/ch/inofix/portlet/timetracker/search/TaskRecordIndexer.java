
package ch.inofix.portlet.timetracker.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Christian Berndt
 * @created 2016-03-19 21:55
 * @modified 2016-05-08 12:30
 * @version 1.0.6
 */
public class TaskRecordIndexer extends BaseIndexer {

    // Enable logging for this class.

    private static final Log _log =
        LogFactoryUtil.getLog(TaskRecordIndexer.class.getName());

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
    public void postProcessSearchQuery(
        BooleanQuery searchQuery, SearchContext searchContext)
        throws Exception {

        // Set advanced search parameters
        addSearchTerm(searchQuery, searchContext, "description", false);
        addSearchTerm(searchQuery, searchContext, "status", false);
        addSearchTerm(searchQuery, searchContext, "userId", false);
        addSearchTerm(searchQuery, searchContext, "workPackage", false);

        // Set expando parameters
        LinkedHashMap<String, Object> params =
            (LinkedHashMap<String, Object>) searchContext.getAttribute("params");

        if (params != null) {
            String expandoAttributes = (String) params.get("expandoAttributes");

            if (Validator.isNotNull(expandoAttributes)) {
                addSearchExpando(searchQuery, searchContext, expandoAttributes);
            }
        }
    }

    @Override
    protected void postProcessFullQuery(
        BooleanQuery fullQuery, SearchContext searchContext)
        throws Exception {

        // end- and start-date
        boolean ignoreEndDate =
            GetterUtil.getBoolean(
                searchContext.getAttribute("ignoreEndDate"), true);
        int endDateDay =
            GetterUtil.getInteger(searchContext.getAttribute("endDateDay"));
        int endDateMonth =
            GetterUtil.getInteger(searchContext.getAttribute("endDateMonth"));
        int endDateYear =
            GetterUtil.getInteger(searchContext.getAttribute("endDateYear"));

        boolean ignoreStartDate =
            GetterUtil.getBoolean(
                searchContext.getAttribute("ignoreStartDate"), true);
        int startDateDay =
            GetterUtil.getInteger(searchContext.getAttribute("startDateDay"));
        int startDateMonth =
            GetterUtil.getInteger(searchContext.getAttribute("startDateMonth"));
        int startDateYear =
            GetterUtil.getInteger(searchContext.getAttribute("startDateYear"));

        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;

        if (!ignoreStartDate && startDateDay > 0 && startDateMonth >= 0 &&
            startDateYear > 0) {

            Date startDate =
                PortalUtil.getDate(startDateMonth, startDateDay, startDateYear);

            min = startDate.getTime();

        }

        if (!ignoreEndDate && endDateDay > 0 && endDateMonth >= 0 &&
            endDateYear > 0) {

            Date endDate =
                PortalUtil.getDate(endDateMonth, endDateDay, endDateYear);

            max = endDate.getTime();
        }

        BooleanQuery booleanQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        booleanQuery.addNumericRangeTerm("startDate_sortable", min, max);
        fullQuery.add(booleanQuery, BooleanClauseOccur.MUST);

    };

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
        document.addDate(
            TaskRecordSearchTerms.CREATE_DATE, taskRecord.getCreateDate());
        document.addText(
            TaskRecordSearchTerms.DESCRIPTION, taskRecord.getDescription());
        document.addNumber(
            TaskRecordSearchTerms.DURATION, taskRecord.getDuration());
        document.addDate(
            TaskRecordSearchTerms.END_DATE, taskRecord.getEndDate());
        document.addKeyword(
            Field.GROUP_ID, getSiteGroupId(taskRecord.getGroupId()));
        document.addDate(
            TaskRecordSearchTerms.MODIFIED_DATE, taskRecord.getModifiedDate());
        document.addKeyword(Field.SCOPE_GROUP_ID, taskRecord.getGroupId());
        document.addDate(
            TaskRecordSearchTerms.START_DATE, taskRecord.getStartDate());
        document.addNumber(TaskRecordSearchTerms.STATUS, taskRecord.getStatus());
        document.addKeyword(
            TaskRecordSearchTerms.TASK_RECORD_ID, taskRecord.getTaskRecordId());
        document.addText(Field.TITLE, taskRecord.getWorkPackage());
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
