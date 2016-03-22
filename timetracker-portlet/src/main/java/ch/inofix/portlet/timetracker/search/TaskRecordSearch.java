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
 * @modified 2013-10-06 18:26
 * @version 1.0
 */
public class TaskRecordSearch extends SearchContainer<TaskRecord> {

    // Enable logging for this class.
    public static final Log _log =
        LogFactoryUtil.getLog(TaskRecordSearch.class.getName());

    // Default configuration for column headers
    // and orderable column headers
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

    /**
     * Call the constructor with the default parameter for the current page
     * value ("cur").
     * 
     * @param portletRequest
     *            the portletRequest.
     * @param iteratorURL
     *            the iteratorURL.
     */
    public TaskRecordSearch(
        PortletRequest portletRequest, PortletURL iteratorURL) {

        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    /**
     * Constructor.
     * 
     * @param portletRequest
     *            the request.
     * @param curParam
     *            parameter for the current page value ("cur").
     * @param iteratorURL
     *            the URL of the portlet.
     */
    public TaskRecordSearch(
        PortletRequest portletRequest, String curParam, PortletURL iteratorURL) {

        // Pass the constructor parameters to the superclass.
        super(portletRequest, new TaskRecordDisplayTerms(portletRequest), new TaskRecordSearchTerms(
            portletRequest), curParam, DEFAULT_DELTA, iteratorURL, headerNames, "there-are-no-results");

        TaskRecordDisplayTerms displayTerms =
            (TaskRecordDisplayTerms) getDisplayTerms();

        iteratorURL.setParameter(
            displayTerms.CREATE_DATE,
            String.valueOf(displayTerms.getCreateDate()));
        iteratorURL.setParameter(
            displayTerms.DESCRIPTION,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.DURATION,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.END_DATE,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.MODIFIED_DATE,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.START_DATE,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.TASK_RECORD_ID,
            String.valueOf(displayTerms.getDescription()));
        iteratorURL.setParameter(
            displayTerms.USER_ID, String.valueOf(displayTerms.getUserId()));
        iteratorURL.setParameter(
            displayTerms.USER_NAME, String.valueOf(displayTerms.getUserName()));
        iteratorURL.setParameter(
            displayTerms.WORK_PACKAGE,
            String.valueOf(displayTerms.getWorkPackage()));

        try {
            PortalPreferences preferences =
                PortletPreferencesFactoryUtil.getPortalPreferences(portletRequest);

            String orderByCol =
                ParamUtil.getString(portletRequest, "orderByCol");
            String orderByType =
                ParamUtil.getString(portletRequest, "orderByType");

            if (Validator.isNotNull(orderByCol) &&
                Validator.isNotNull(orderByType)) {

                preferences.setValue(
                    "timetrackerportlerportlet_WAR_timetrackerportlerportlet",
                    "timetracker-order-by-col", orderByCol);
                preferences.setValue(
                    "timetrackerportlerportlet_WAR_timetrackerportlerportlet",
                    "timetracker-order-by-type", orderByType);
            }
            else {
                orderByCol =
                    preferences.getValue(
                        "timetrackerportlerportlet_WAR_timetrackerportlerportlet",
                        "timetracker-order-by-col", "create-date");
                orderByType =
                    preferences.getValue(
                        "timetrackerportlerportlet_WAR_timetrackerportlerportlet",
                        "timetracker-order-by-type", "desc");
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
