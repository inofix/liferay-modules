package ch.inofix.portlet.timetracker.util.comparator;

import ch.inofix.portlet.timetracker.model.TaskRecord;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 *
 * @author         Christian Berndt
 * @created        2013-11-14 01:25
 * @modified    2013-11-14 01:25
 * @version        1.0
 *
 */
@SuppressWarnings("serial")
public class DurationComparator extends OrderByComparator {

    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil
            .getLog(DurationComparator.class.getName());

    // Private fields.
    private boolean _ascending;

    // Public constants.
    public static final String ORDER_BY_ASC = "duration ASC";
    public static final String ORDER_BY_DESC = "duration DESC";
    public static final String[] ORDER_BY_FIELDS = { "duration" };

    // Default no-args constructor
    public DurationComparator() {
        this(false);
    }

    // Constructor
    public DurationComparator(boolean ascending) {
        _log.info("Constructing new DurationComparator().");
        _ascending = ascending;
    }

    @Override
    public int compare(Object arg0, Object arg1) {

        TaskRecord taskRecord0 = (TaskRecord) arg0;
        TaskRecord taskRecord1 = (TaskRecord) arg1;

        // Compare two taskRecords by their Duration.
        int value = 0;
        if (taskRecord0.getDuration() > taskRecord1.getDuration()) {
            value = 1;
        }

        // Optional: add additional comparisons
        // if the Duration of two taskRecords are the same.

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
