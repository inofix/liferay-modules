
package ch.inofix.portlet.timetracker.util;

import java.util.List;

import ch.inofix.portlet.timetracker.model.TaskRecord;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * Main utility class for the timetracker portlet.
 *
 * @author Christian Berndt
 * @author Michael Lustenberger
 * @created 2013-10-06 20:56
 * @modified 2014-10-14 14:48
 * @version 1.3
 */
public class TimetrackerPortletUtil {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(TimetrackerPortletUtil.class.getName());

    private static TimetrackerPortlet _timetrackerPortlet;

    /**
     * Return the sum in hours for a given list of taskRecords.
     *
     * @param taskRecords
     * @return the sum of hours for a given list of taskRecords.
     * @since 1.2
     */
    public static double getHours(List<TaskRecord> taskRecords) {

        return getTimetrackerPortlet().getHours(taskRecords);
    }

    /**
     * Return an OrderByComparator for a given column and type (desc / asc).
     *
     * @param orderByCol
     *            the column to be ordered.
     * @param orderByType
     *            desc(ending) or asc(ending)
     * @return an OrderByComparator for a given column with a given type (asc /
     *         desc).
     * @since 1.0
     */
    public static OrderByComparator getOrderByComparator(
        String orderByCol, String orderByType) {

        return getTimetrackerPortlet().getOrderByComparator(
            orderByCol, orderByType);
    }

    /**
     * @return
     * @since 1.0
     */
    public static TimetrackerPortlet getTimetrackerPortlet() {

        return _timetrackerPortlet;
    }

    /**
     * Parse a text for TaskRecords in the form of mic's vim list =date=
     * work.package start end "comment" [links]
     *
     * @param the
     *            text string
     * @return the TaskRecords
     * @since 1.3
     */
    public static List<String[]> parseMicVimport(String some) {

        return getTimetrackerPortlet().parseMicVimport(some);
    }

    /**
     * Parse a subtext for work packages
     *
     * @param dateString
     *            for this date
     * @param payLoad
     *            content to parse
     * @return the list for the day
     * @since 1.3
     */
    public static List<String[]> parseMicPackage(
        String dateString, String payLoad) {

        return getTimetrackerPortlet().parseMicPackage(dateString, payLoad);
    }

    /**
     * Parse a subtext for a job
     *
     * @param dateString
     *            for this date
     * @param payLoad
     *            content to parse
     * @return the list for the customer / work package for the day
     * @since 1.3
     */
    public static List<String[]> parseMicWork(
        String dateString, String[] workPackage) {

        return getTimetrackerPortlet().parseMicWork(dateString, workPackage);
    }

    /**
     * Parse a subtext for certain job
     *
     * @param dateString
     *            for this date
     * @param packageName
     *            of the work package
     * @param job
     *            content to parse
     * @return the TaskRecord
     * @since 1.3
     */
    public static String[] parseMicTaskRecord(
        String dateString, String packageName, String job) {

        return getTimetrackerPortlet().parseMicTaskRecord(
            dateString, packageName, job);
    }

    /**
     * @param timetrackerPortlet
     * @since 1.0
     */
    public void setTimetrackerPortlet(TimetrackerPortlet timetrackerPortlet) {

        _timetrackerPortlet = timetrackerPortlet;
    }
}
