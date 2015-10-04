package ch.inofix.portlet.contact.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-19 21:08
 * @modified 2015-05-19 21:08
 * @version 1.0.0
 *
 */
public class ContactPortletPermission {

	public static final String RESOURCE_NAME = "ch.inofix.portlet.contact";

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
