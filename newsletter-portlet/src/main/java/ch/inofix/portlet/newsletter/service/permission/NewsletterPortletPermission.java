package ch.inofix.portlet.newsletter.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-08 00:34
 * @modified 2016-10-08 00:34
 * @version 1.0.0
 *
 */
public class NewsletterPortletPermission {

    public static final String RESOURCE_NAME = "ch.inofix.portlet.newsletter";

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
