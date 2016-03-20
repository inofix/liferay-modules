
package ch.inofix.portlet.timetracker.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * @author Christian Berndt
 * @created 2016-03-20 17:07
 * @modified 2015-03-20 17:07
 * @version 1.0.0
 */
public class TimetrackerPortletPermission {

    public static final String RESOURCE_NAME = "ch.inofix.portlet.timetracker";

    public static void check(
        PermissionChecker permissionChecker, long groupId, String actionId)
        throws PortalException {

        if (!contains(permissionChecker, groupId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(
        PermissionChecker permissionChecker, long groupId, String actionId) {

        return permissionChecker.hasPermission(
            groupId, RESOURCE_NAME, groupId, actionId);
    }

}
