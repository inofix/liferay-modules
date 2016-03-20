
package ch.inofix.portlet.timetracker.service.impl;

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
 * @modified 2016-03-20 18:24
 * @version 1.0.0
 */
public class TaskRecordServiceImpl extends TaskRecordServiceBaseImpl {

    /*
     * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
     * {@link ch.inofix.portlet.timetracker.service.TaskRecordServiceUtil} to
     * access the task record remote service.
     */

    // Enable logging for this class
    private static Log log =
        LogFactoryUtil.getLog(TaskRecordServiceImpl.class.getName());

    /**
     * Return the added taskRecord.
     * 
     * @param userId
     * @param groupId
     * @param card
     *            the vCard string
     * @param uid
     *            the vCard's uid
     * @return the added taskRecord
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord addTaskRecord(
        long userId, long groupId, String card, String uid,
        ServiceContext serviceContext)
        throws PortalException, SystemException {

        TimetrackerPortletPermission.check(
            getPermissionChecker(), groupId, ActionKeys.ADD_TASK_RECORD);

        // TODO
        return null;
        // return TaskRecordLocalServiceUtil.addTaskRecord(
        // userId, groupId, card, uid, serviceContext);

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
     * @param userId
     * @param groupId
     * @param taskRecordId
     * @param card
     * @param uid
     * @param serviceContext
     * @return
     * @since 1.0.2
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord updateTaskRecord(
        long userId, long groupId, long taskRecordId, String card, String uid,
        ServiceContext serviceContext)
        throws PortalException, SystemException {

        TaskRecordPermission.check(
            getPermissionChecker(), taskRecordId, ActionKeys.UPDATE);

        // TODO
        // return TaskRecordLocalServiceUtil.updateTaskRecord(
        // userId, groupId, taskRecordId, card, uid, serviceContext);

        return null;

    }
}
