package ch.inofix.portlet.timetracker.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.util.TaskRecordFields;
import ch.inofix.portlet.timetracker.util.TimetrackerPortletKeys;
import ch.inofix.portlet.timetracker.util.TimetrackerPortletUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

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
        headerNames.add(TaskRecordFields.TASK_RECORD_ID);
        headerNames.add(TaskRecordFields.WORK_PACKAGE);
        headerNames.add(TaskRecordFields.DESCRIPTION);
        headerNames.add(TaskRecordFields.START_DATE);
        headerNames.add(TaskRecordFields.END_DATE);
        headerNames.add(TaskRecordFields.DURATION);

        orderableHeaders.put(
            TaskRecordFields.TASK_RECORD_ID, TaskRecordFields.TASK_RECORD_ID);
        orderableHeaders.put(
            TaskRecordFields.WORK_PACKAGE, TaskRecordFields.WORK_PACKAGE);
        orderableHeaders.put(
            TaskRecordFields.DESCRIPTION, TaskRecordFields.DESCRIPTION);
        orderableHeaders.put(
            TaskRecordFields.START_DATE, TaskRecordFields.START_DATE);
        orderableHeaders.put(
            TaskRecordFields.END_DATE, TaskRecordFields.END_DATE);
        orderableHeaders.put(
            TaskRecordFields.DURATION, TaskRecordFields.DURATION);
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

        // Get the orderByCol and orderByType either from
        // the request or from the portlet's preferences.
        try {

            String orderByCol =
                ParamUtil.getString(
                    portletRequest, TimetrackerPortletKeys.ORDER_BY_COL);
            String orderByType =
                ParamUtil.getString(
                    portletRequest, TimetrackerPortletKeys.ORDER_BY_TYPE);

            OrderByComparator obc =
                TimetrackerPortletUtil.getOrderByComparator(
                    orderByCol, orderByType);

            // Pass the orderBy parameter to the super class.
            super.setOrderByCol(orderByCol);
            super.setOrderByType(orderByType);
            super.setOrderByComparator(obc);

        }
        catch (Exception e) {
            _log.error(e);
        }
    }

}
