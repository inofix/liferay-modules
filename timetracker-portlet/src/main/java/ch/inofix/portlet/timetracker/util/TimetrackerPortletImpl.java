package ch.inofix.portlet.timetracker.util;

import java.util.ArrayList;
import java.util.List;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.util.comparator.DurationComparator;
import ch.inofix.portlet.timetracker.util.comparator.EndDateComparator;
import ch.inofix.portlet.timetracker.util.comparator.StartDateComparator;
import ch.inofix.portlet.timetracker.util.comparator.UserNameComparator;
import ch.inofix.portlet.timetracker.util.comparator.WorkPackageComparator;
import ch.inofix.portlet.timetracker.util.comparator.CreateDateComparator;
import ch.inofix.portlet.timetracker.util.comparator.ModifiedDateComparator;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * The implementation of the Timetracker portlet's utility class.
 *
 * @author Christian Berndt
 * @author Michael Lustenberger
 * @created 2013-10-06 20:14
 * @modified 2014-10-14 14:38
 * @version 1.3
 */
public class TimetrackerPortletImpl implements TimetrackerPortlet {

    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil
            .getLog(TimetrackerPortletImpl.class.getName());

    /**
     * Return the sum in hours for a given list of taskRecords.
     *
     * @param taskRecords
     * @return the sum of hours for a given list of taskRecords.
     * @since 1.2
     */
    public double getHours(List<TaskRecord> taskRecords) {

        _log.info("Executing getHours().");

        long minutes = 0;

        for (TaskRecord taskRecord : taskRecords) {
            minutes = minutes + taskRecord.getDurationInMinutes();
        }

        double hours = 0;

        if (minutes > 0) {
            hours = ((double) minutes) / 60;
        }

        return hours;
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
    public OrderByComparator getOrderByComparator(String orderByCol,
            String orderByType) {

        _log.info("Executing getOrderByComparator().");
        _log.info("Returning an OrderByComparator for orderbyCol = "
                + orderByCol + " and orderByType = " + orderByType + ".");

        boolean orderByAsc = false;

        if (StringPool.ASC.equalsIgnoreCase(orderByType)) {
            orderByAsc = true;
        }

        OrderByComparator obc = null;

        // Look up the appropriate comparator.
        if (CommonFields.CREATE_DATE.equalsIgnoreCase(orderByCol)) {

            obc = new CreateDateComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else if (TaskRecordFields.DURATION.equalsIgnoreCase(orderByCol)) {

            obc = new DurationComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else if (CommonFields.END_DATE.equalsIgnoreCase(orderByCol)) {

            obc = new EndDateComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else if (TaskRecordFields.START_DATE.equalsIgnoreCase(orderByCol)) {

            obc = new StartDateComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else if (CommonFields.USER_NAME.equalsIgnoreCase(orderByCol)) {

            obc = new UserNameComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else if (TaskRecordFields.WORK_PACKAGE.equalsIgnoreCase(orderByCol)) {

            obc = new WorkPackageComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        } else {

            // By default sort projects descending
            // by their last modification.
            obc = new ModifiedDateComparator(orderByAsc);
            _log.debug("orderByAsc = " + orderByAsc);
            _log.debug(obc.getClass().getName());

        }

        _log.debug("orderByAsc = " + orderByAsc);
        _log.debug(obc.getClass().getName());

        return obc;
    }

    /**
     * Parse a text for TaskRecords in the form of mic's vim list
     * =date=
     * work.package
     *   start end "comment" [links]
     *
     * @param the text string
     * @return a list of String arrays representing the TaskRecords
     * @since 1.3
     */
    public List<String[]> parseMicVimport(String some) {

        List<String[]> records = new ArrayList<String[]>();

        // look for the start of every day, e.g.: '=2014-10-14='
        String[] workDays = some.split("(?m)^=");

        // skip the first empty string
        for (int i=1; i < workDays.length ; i++) {

            String[] aWorkDay = workDays[i].split("(?m)=$");
            records.addAll(parseMicPackage(aWorkDay[0], aWorkDay[1]));
        }
        return records;
    }

    /**
     * Parse a subtext for work packages
     *
     * @param dateString for this date
     * @param payLoad content to parse
     * @return a list of String arrays representing the TaskRecords of the day
     * @since 1.3
     */
    public List<String[]> parseMicPackage(String dateString, String payLoad) {

        List<String[]> records = new ArrayList<String[]>();

        if (dateString == null || "".equals(dateString) || payLoad == null ||
                "".equals(payLoad)) {

            System.out.println("ERROR");
            //throw some exception
            return null;
        }

        String[] workPackages = payLoad.split("(?m)^(?=(\\w+\\.*))");

        // skipt first empty content
        for (int i=1; i < workPackages.length; i++) {

            String[] workPackage = workPackages[i].split("(?m)^ +");
            records.addAll(parseMicWork(dateString, workPackage));
        }

        return records;
    }

    /**
     * Parse a subtext for a job
     *
     * @param dateString for this date
     * @param payLoad content to parse
     * @return a list of String arrays, the TaskRecords of the day per customer
     * @since 1.3
     */
    public List<String[]> parseMicWork(String dateString,
                                                String[] workPackage) {

        List<String[]> records = new ArrayList<String[]>();

        if (dateString == null || "".equals(dateString) || workPackage == null
                || workPackage.length <= 0 || workPackage[0] == null ) {
//                || "".equals(workPackage[0])) {

            System.out.println("ERROR parseWork(s,s)");
            //throw some exception
            return null;
        }

        // again skip first, this time it was the work package name..
        for (int i=1; i < workPackage.length; i++) {

            records.add(parseMicTaskRecord(dateString, workPackage[0],
                                                            workPackage[i]));
        }

        return records;
    }

    /**
     * Parse a subtext for certain job
     *
     * @param dateString for this date
     * @param packageName of the work package
     * @param job content to parse
     * @return the TaskRecord represented as String array
     * @since 1.3
     */
    public String[] parseMicTaskRecord(String dateString,
                                        String packageName, String job) {

        String[] someWork = job.split("(?m) +(?=(\".*\"))");
        // length must be the common 13 plus the optional links..
        String[] taskRecord = new String[11 + someWork.length];

        // startYear
        taskRecord[0] = dateString.substring(0, 4);
        // startMonth
        taskRecord[1] = dateString.substring(5, 7);
        // startDay
        taskRecord[2] = dateString.substring(8, 10);
        // startHour
        taskRecord[3] = "0";
        // startMinute
        taskRecord[4] = "0";
        // endYear
        taskRecord[5] = taskRecord[0];
        // endMonth
        taskRecord[6] = taskRecord[1];
        // endDay
        taskRecord[7] = taskRecord[2];
        // endHour
        taskRecord[8] = "0";
        // endMinute
        taskRecord[9] = "0";

        // if there is only one number found here, we are in minutes mode
        int spaceIndex = someWork[0].indexOf(" ");
        if (spaceIndex < 0) {

            taskRecord[8] = "-1";
            taskRecord[9] = someWork[0];

        // otherwise we have the clock mode
        } else {

            taskRecord[3] = someWork[0].substring(0, 2);
            taskRecord[4] = someWork[0].substring(2, 4);
            taskRecord[8] = someWork[0].substring(5, 7);
            taskRecord[9] = someWork[0].substring(7, 9);
        }

// TODO here we might allow midnight labour..
//        int startHour = Integer.parseInt(taskRecord[3]);
//        int startMinute = Integer.parseInt(taskRecord[4]);
//        int endHour = Integer.parseInt(taskRecord[8]);
//        int endMinute = Integer.parseInt(taskRecord[9]);

        // now see if we have to switch the date, for jobs over midnight
//        if (startHour < endHour ||
//                    (startHour == endHour && startMinute < endMinute)) {

            // if start time is bigger then end time, add a day..
//            taskRecord[7] = String.valueOf(endMinute + 1);
//        }

        // workPackage
        taskRecord[10] = packageName;
        // description
        taskRecord[11] = someWork[1].replace("\"", "");

        for (int i=2; i < someWork.length; i++) {

            taskRecord[11+i] = someWork[i];
        }

        return taskRecord;
    }
}

