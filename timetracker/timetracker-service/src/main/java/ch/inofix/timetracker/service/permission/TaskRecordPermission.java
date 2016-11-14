package ch.inofix.timetracker.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.timetracker.model.TaskRecord;
import ch.inofix.timetracker.service.TaskRecordLocalServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-13 17:55
 * @modified 2016-11-13 17:55
 * @version 1.0.0
 *
 */
public class TaskRecordPermission {

    public static void check(PermissionChecker permissionChecker, TaskRecord calendarResource, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, calendarResource, actionId)) {
            throw new PrincipalException();
        }
    }

    public static void check(PermissionChecker permissionChecker, long calendarResourceId, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, calendarResourceId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, TaskRecord calendarResource, String actionId) {

        if (permissionChecker.hasOwnerPermission(calendarResource.getCompanyId(), TaskRecord.class.getName(),
                calendarResource.getTaskRecordId(), calendarResource.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(calendarResource.getGroupId(), TaskRecord.class.getName(),
                calendarResource.getTaskRecordId(), actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, long calendarResourceId, String actionId)
            throws PortalException {

        TaskRecord calendarResource = TaskRecordLocalServiceUtil.getTaskRecord(calendarResourceId);

        return contains(permissionChecker, calendarResource, actionId);
    }
}
