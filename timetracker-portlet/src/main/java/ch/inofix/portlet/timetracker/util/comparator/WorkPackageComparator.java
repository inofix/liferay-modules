
package ch.inofix.portlet.timetracker.util.comparator;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * Compare two TaskRecords bei their bundle field value.
 *
 * @author Christian Berndt
 * @created 2013-10-06 19:39
 * @modified 2013-10-06 19:39
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WorkPackageComparator extends OrderByComparator {

    // Enable logging for this class.
    public static final Log _log =
        LogFactoryUtil.getLog(WorkPackageComparator.class.getName());

    public static final String ORDER_BY_ASC = "workPackage ASC";

    public static final String ORDER_BY_DESC = "workPackage DESC";

    public static final String[] ORDER_BY_FIELDS = {
        "workPackage"
    };

    // Default no-args constructor.
    public WorkPackageComparator() {

        this(false);
    }

    // Constructor.
    public WorkPackageComparator(boolean ascending) {

        _ascending = ascending;
    }

    @Override
    public int compare(Object obj1, Object obj2) {

        TaskRecord tastRecord1 = (TaskRecord) obj1;
        TaskRecord tastRecord2 = (TaskRecord) obj2;

        int value =
            tastRecord1.getWorkPackage().compareTo(tastRecord2.getWorkPackage());

        if (_ascending) {
            return value;
        }
        else {
            return -value;
        }
    }

    @Override
    public String getOrderBy() {

        if (_ascending) {
            return ORDER_BY_ASC;
        }
        else {
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

    private boolean _ascending;

}
