
package ch.inofix.portlet.timetracker.service.impl;

import java.util.Date;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ServiceContext;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.security.permission.ActionKeys;
import ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil;
import ch.inofix.portlet.timetracker.service.base.TaskRecordServiceBaseImpl;
import ch.inofix.portlet.timetracker.service.permission.TaskRecordPermission;
import ch.inofix.portlet.timetracker.service.permission.TimetrackerPortletPermission;

/**
 * The implementation of the task record remote service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.timetracker.service.TaskRecordService} interface.
 * <p> This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely. </p>
 *
 * @author Christian Berndt
 * @see ch.inofix.portlet.timetracker.service.base.TaskRecordServiceBaseImpl
 * @see ch.inofix.portlet.timetracker.service.TaskRecordServiceUtil
 * @created 2015-05-07 23:50
 * @modified 2016-04-24 22:14
 * @version 1.0.2
 */
public class TaskRecordServiceImpl extends TaskRecordServiceBaseImpl {

    /*
     * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
     * {@link ch.inofix.portlet.timetracker.service.TaskRecordServiceUtil} to
     * access the task record remote service.
     */

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(TaskRecordServiceImpl.class.getName());

    /**
     * Return the added taskRecord.
     * 
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord addTaskRecord(
        long userId, long groupId, String workPackage, String description,
        String ticketURL, int endDateDay, int endDateMonth, int endDateYear,
        int endDateHour, int endDateMinute, int startDateDay,
        int startDateMonth, int startDateYear, int startDateHour,
        int startDateMinute, int status, long duration,
        ServiceContext serviceContext)
        throws PortalException, SystemException {

        TimetrackerPortletPermission.check(
            getPermissionChecker(), groupId, ActionKeys.ADD_TASK_RECORD);

        return taskRecordLocalService.addTaskRecord(
            userId, groupId, workPackage, description, ticketURL, endDateDay,
            endDateMonth, endDateYear, endDateHour, endDateMinute,
            startDateDay, startDateMonth, startDateYear, startDateHour,
            startDateMinute, status, duration, serviceContext);

    }

    /**
     * 
     * @param userId
     * @param groupId
     * @param workPackage
     * @param description
     * @param ticketURL
     * @param endDate
     * @param startDate
     * @param status
     * @param duration
     * @param serviceContext
     * @return the added record
     * @since 1.0.8
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord addTaskRecord(
        long userId, long groupId, String workPackage, String description,
        String ticketURL, Date endDate, Date startDate, int status,
        long duration, ServiceContext serviceContext)
        throws PortalException, SystemException {

        TimetrackerPortletPermission.check(
            getPermissionChecker(), groupId, ActionKeys.ADD_TASK_RECORD);

        return taskRecordLocalService.addTaskRecord(
            userId, groupId, workPackage, description, ticketURL, endDate,
            startDate, status, duration, serviceContext);

    }

    /**
     * @return
     * @since 1.0.2
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord createTaskRecord()
        throws PortalException, SystemException {

        // Create an empty taskRecord - no permission check required
        return TaskRecordLocalServiceUtil.createTaskRecord(0);

    }

    /**
     * Delete a specific taskRecord version and return the deleted taskRecord.
     * 
     * @param taskRecordId
     * @return the deleted taskRecord
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord deleteTaskRecord(long taskRecordId)
        throws PortalException, SystemException {

        TaskRecordPermission.check(
            getPermissionChecker(), taskRecordId, ActionKeys.DELETE);

        TaskRecord taskRecord =
            TaskRecordLocalServiceUtil.deleteTaskRecord(taskRecordId);

        return taskRecord;

    }

    /**
     * Return the taskRecord.
     * 
     * @param taskRecordId
     * @return the latest version of a taskRecord.
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord getTaskRecord(long taskRecordId)
        throws PortalException, SystemException {

        TaskRecordPermission.check(
            getPermissionChecker(), taskRecordId, ActionKeys.VIEW);

        return TaskRecordLocalServiceUtil.getTaskRecord(taskRecordId);

    }

    /**
     * @return
     * @since 1.0.2
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord updateTaskRecord(
        long userId, long groupId, long taskRecordId, String workPackage,
        String description, String ticketURL, int endDateDay, int endDateMonth,
        int endDateYear, int endDateHour, int endDateMinute, int startDateDay,
        int startDateMonth, int startDateYear, int startDateHour,
        int startDateMinute, int status, long duration,
        ServiceContext serviceContext)
        throws PortalException, SystemException {

        TaskRecordPermission.check(
            getPermissionChecker(), taskRecordId, ActionKeys.UPDATE);

        return taskRecordLocalService.updateTaskRecord(
            userId, groupId, taskRecordId, workPackage, description, ticketURL,
            endDateDay, endDateMonth, endDateYear, endDateHour, endDateMinute,
            startDateDay, startDateMonth, startDateYear, startDateHour,
            startDateMinute, status, duration, serviceContext);

    }
}
