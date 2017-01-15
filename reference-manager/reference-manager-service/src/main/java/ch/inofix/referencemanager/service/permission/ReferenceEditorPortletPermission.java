package ch.inofix.referencemanager.service.permission;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.BaseResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.ResourcePermissionChecker;

import ch.inofix.referencemanager.constants.PortletKeys;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-14 20:42
 * @modified 2017-01-14 20:42
 * @version 1.0.0
 *
 */
@Component(
    immediate = true, 
    property = {"resource.name=" + ReferenceManagerPortletPermission.RESOURCE_NAME }, 
    service = ResourcePermissionChecker.class
)
public class ReferenceEditorPortletPermission extends BaseResourcePermissionChecker {

    public static final String RESOURCE_NAME = "ch.inofix.referencemanager.referenceeditor";

    public static void check(PermissionChecker permissionChecker, long groupId, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, groupId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, long groupId, String actionId) {
        return contains(permissionChecker, RESOURCE_NAME, PortletKeys.REFERENCE_EDITOR, groupId, actionId);
    }

    public Boolean checkResource(PermissionChecker permissionChecker, long classPK, String actionId) {

        return contains(permissionChecker, classPK, actionId);
    }

}
