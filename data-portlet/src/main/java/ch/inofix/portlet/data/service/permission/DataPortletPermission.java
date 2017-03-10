package ch.inofix.portlet.data.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 18:29
 * @modified 2017-03-09 18:29
 * @version 1.0.0
 *
 */
public class DataPortletPermission {

    public static final String RESOURCE_NAME = "ch.inofix.portlet.data";

    public static void check(PermissionChecker permissionChecker, long groupId,
            String actionId) throws PortalException {

        if (!contains(permissionChecker, groupId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker,
            long groupId, String actionId) {

        return permissionChecker.hasPermission(groupId, RESOURCE_NAME, groupId,
                actionId);
    }

}
