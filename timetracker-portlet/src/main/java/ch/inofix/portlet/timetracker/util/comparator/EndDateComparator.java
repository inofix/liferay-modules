package ch.inofix.portlet.timetracker.util.comparator;

import ch.inofix.portlet.timetracker.model.TaskRecord;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 *
 * @author         Christian Berndt
 * @created        2013-11-14 10:31
 * @modified    2013-11-14 10:31
 * @version        1.0
 *
 */
@SuppressWarnings("serial")
public class EndDateComparator extends OrderByComparator {

    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil
            .getLog(EndDateComparator.class.getName());

    // Private fields.
    private boolean _ascending;

    // Public constants.
    public static final String ORDER_BY_ASC = "endDate ASC";
    public static final String ORDER_BY_DESC = "endDate DESC";
    public static final String[] ORDER_BY_FIELDS = { "endDate" };

    // Default no-args constructor
    public EndDateComparator() {
        this(false);
    }

    // Constructor
    public EndDateComparator(boolean ascending) {
        _log.info("Constructing new EndDateComparator().");
        _ascending = ascending;
    }

    @Override
    public int compare(Object arg0, Object arg1) {

        TaskRecord taskRecord0 = (TaskRecord) arg0;
        TaskRecord taskRecord1 = (TaskRecord) arg1;

        // Compare two taskRecords by their EndDate.
        // Compare two taskRecords by their createDate.
        int value = taskRecord0.getEndDate().compareTo(
                taskRecord1.getEndDate());

        // Optional: add additional comparisons
        // if the EndDate of two taskRecords are the same.

        // Return the result of the comparison depending
        // on the requested sort direction.
        if (_ascending) {
            return value;
        } else {
            return -value;
        }

    }

    @Override
    public String getOrderBy() {
        if (_ascending) {
            return ORDER_BY_ASC;
        } else {
            return ORDER_BY_DESC;
        }
    }

    @Override
    public String[] getOrderByFields() {
        return ORDER_BY_FIELDS;
    }

    @Override
    public boolean isAscending() {
        return _ascending;
    }

}
