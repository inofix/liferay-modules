package ch.inofix.portlet.timetracker.service.permission;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * @author Christian Berndt
 * @created 2016-03-19 22:03
 * @modified 2016-03-19 22:09
 * @version 1.0.0
 */
public class TaskRecordPermission {

    /**
     * @param permissionChecker
     * @param taskRecordId
     * @param actionId
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public static void check(
        PermissionChecker permissionChecker, long taskRecordId, String actionId)
        throws PortalException, SystemException {

        if (!contains(permissionChecker, taskRecordId, actionId)) {
            throw new PrincipalException();
        }
    }

    /**
     * @param permissionChecker
     * @param taskRecordId
     * @param actionId
     * @return
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public static boolean contains(
        PermissionChecker permissionChecker, long taskRecordId, String actionId)
        throws PortalException, SystemException {

        TaskRecord taskRecord =
            TaskRecordLocalServiceUtil.getTaskRecord(taskRecordId);

        if (permissionChecker.hasOwnerPermission(
            taskRecord.getCompanyId(), TaskRecord.class.getName(),
            taskRecord.getTaskRecordId(), taskRecord.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(
            taskRecord.getGroupId(), TaskRecord.class.getName(),
            taskRecord.getTaskRecordId(), actionId);

    }
}
