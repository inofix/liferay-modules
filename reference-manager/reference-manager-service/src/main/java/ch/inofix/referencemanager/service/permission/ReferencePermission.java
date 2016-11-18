package ch.inofix.referencemanager.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-18 16:20
 * @modified 2016-11-18 16:20
 * @version 1.0.0
 *
 */
public class ReferencePermission {

    public static void check(PermissionChecker permissionChecker, Reference reference, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, reference, actionId)) {
            throw new PrincipalException();
        }
    }

    public static void check(PermissionChecker permissionChecker, long referenceId, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, referenceId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, Reference reference, String actionId) {

        if (permissionChecker.hasOwnerPermission(reference.getCompanyId(), Reference.class.getName(),
                reference.getReferenceId(), reference.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(reference.getGroupId(), Reference.class.getName(),
                reference.getReferenceId(), actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, long referenceId, String actionId)
            throws PortalException {

        Reference reference = ReferenceLocalServiceUtil.getReference(referenceId);

        return contains(permissionChecker, reference, actionId);
    }

}
