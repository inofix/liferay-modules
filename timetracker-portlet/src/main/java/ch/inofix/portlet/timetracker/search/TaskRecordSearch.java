
package ch.inofix.portlet.timetracker.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.timetracker.model.TaskRecord;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 * @author Christian Berndt
 * @created 2013-10-06 18:26
 * @modified 2016-05-08 12:44
 * @version 1.0.5
 */
public class TaskRecordSearch extends SearchContainer<TaskRecord> {

    public static final String EMPTY_RESULTS_MESSAGE = "there-are-no-results";

    // Enable logging for this class.
    public static final Log _log =
        LogFactoryUtil.getLog(TaskRecordSearch.class.getName());

    static List<String> headerNames = new ArrayList<String>();
    static Map<String, String> orderableHeaders = new HashMap<String, String>();

    static {
        headerNames.add("create-date");
        headerNames.add("duration");
        headerNames.add("description");
        headerNames.add("end-date");
        headerNames.add("modified-date");
        headerNames.add("start-date");
        headerNames.add("task-record-id");
        headerNames.add("ticket-url");
        headerNames.add("work-package");

        orderableHeaders.put("create-date", "create-date");
        orderableHeaders.put("duration", "duration");
        orderableHeaders.put("description", "description");
        orderableHeaders.put("end-date", "end-date");
        orderableHeaders.put("modified-date", "modified-date");
        orderableHeaders.put("start-date", "start-date");
        orderableHeaders.put("task-record-id", "task-record-id");
        orderableHeaders.put("ticket-url", "ticket-url");
        orderableHeaders.put("work-package", "work-package");
    }

    public TaskRecordSearch(
        PortletRequest portletRequest, PortletURL iteratorURL) {

        super(portletRequest, new TaskRecordDisplayTerms(portletRequest), new TaskRecordSearchTerms(
            portletRequest), DEFAULT_CUR_PARAM, DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        TaskRecordDisplayTerms displayTerms =
            (TaskRecordDisplayTerms) getDisplayTerms();

        iteratorURL.setParameter(
            TaskRecordDisplayTerms.CREATE_DATE, displayTerms.getCreateDate());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.DESCRIPTION, displayTerms.getDescription());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.DURATION, displayTerms.getDuration());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.END_DATE, displayTerms.getEndDate());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.END_DATE_DAY, displayTerms.getEndDateDay());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.END_DATE_MONTH,
            displayTerms.getEndDateMonth());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.END_DATE_YEAR, displayTerms.getEndDateYear());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.IGNORE_END_DATE,
            String.valueOf(displayTerms.ignoreEndDate));
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.IGNORE_START_DATE,
            String.valueOf(displayTerms.ignoreStartDate));
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.MODIFIED_DATE,
            displayTerms.getModifiedDate());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.START_DATE, displayTerms.getStartDate());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.START_DATE_DAY,
            displayTerms.getStartDateDay());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.START_DATE_MONTH,
            displayTerms.getStartDateMonth());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.START_DATE_YEAR,
            displayTerms.getStartDateYear());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.STATUS,
            String.valueOf(displayTerms.getStatus()));        
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.TASK_RECORD_ID,
            String.valueOf(displayTerms.getTaskRecordId()));
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.USER_ID,
            String.valueOf(displayTerms.getUserId()));
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.USER_NAME, displayTerms.getUserName());
        iteratorURL.setParameter(
            TaskRecordDisplayTerms.WORK_PACKAGE, displayTerms.getWorkPackage());

        try {
            PortalPreferences preferences =
                PortletPreferencesFactoryUtil.getPortalPreferences(portletRequest);

            String portletId = "timetrackerportlet_WAR_timetrackerportlet";

            String orderByCol =
                ParamUtil.getString(portletRequest, "orderByCol");
            String orderByType =
                ParamUtil.getString(portletRequest, "orderByType");

            if (Validator.isNotNull(orderByCol) &&
                Validator.isNotNull(orderByType)) {

                preferences.setValue(
                    portletId, "task-records-order-by-col", orderByCol);
                preferences.setValue(
                    portletId, "task-records-order-by-type", orderByType);
            }
            else {
                orderByCol =
                    preferences.getValue(
                        portletId, "task-records-order-by-col", "modifiedDate");
                orderByType =
                    preferences.getValue(
                        portletId, "task-records-order-by-type", "desc");
            }

            setOrderableHeaders(orderableHeaders);
            setOrderByCol(orderByCol);
            setOrderByType(orderByType);
        }
        catch (Exception e) {
            _log.error(e);
        }
    }
}
