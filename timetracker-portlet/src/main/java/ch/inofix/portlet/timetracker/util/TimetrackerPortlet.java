package ch.inofix.portlet.timetracker.util;

import java.util.List;

import ch.inofix.portlet.timetracker.model.TaskRecord;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * The interface of the Timetracker-portlet's main utility class.
 *
 * @author Christian Berndt
 * @author Michael Lustenberger
 * @created 2013-10-06 19:54
 * @modified 2014-10-14 14:46
 * @version 1.3
 */
public interface TimetrackerPortlet {

    /**
     * Return the sum in hours for a given list of taskRecords.
     *
     * @param taskRecords
     * @return the sum of hours for a given list of taskRecords.
     * @since 1.2
     */
    public double getHours(List<TaskRecord> taskRecords);

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
    public OrderByComparator getOrderByComparator(String orderByCol,
            String orderByType);

    /**
     * Parse a text for TaskRecords in the form of mic's vim list
     * =date=
     * work.package
     *   start end "comment" [links]
     *
     * @param the text string
     * @return the TaskRecords
     * @since 1.3
     */
    public List<String[]> parseMicVimport(String some);

    /**
     * Parse a subtext for work packages
     *
     * @param dateString for this date
     * @param payLoad content to parse
     * @return the list for the day
     * @since 1.3
     */
    public List<String[]> parseMicPackage(String dateString, String payLoad);

    /**
     * Parse a subtext for a job
     *
     * @param dateString for this date
     * @param payLoad content to parse
     * @return the list for the customer / work package for the day
     * @since 1.3
     */
    public List<String[]> parseMicWork(String dateString,
                                                String[] workPackage);
    /**
     * Parse a subtext for certain job
     *
     * @param dateString for this date
     * @param packageName of the work package
     * @param job content to parse
     * @return the TaskRecord
     * @since 1.3
     */
    public String[] parseMicTaskRecord(String dateString,
                                        String packageName, String job);
}
