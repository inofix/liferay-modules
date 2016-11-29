package ch.inofix.referencemanager.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyLocalServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-29 22:01
 * @modified 2016-11-29 22:01
 * @version 1.0.0
 *
 */
public class BibliographyPermission {
    
    public static void check(PermissionChecker permissionChecker, Bibliography bibliography, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, bibliography, actionId)) {
            throw new PrincipalException();
        }
    }

    public static void check(PermissionChecker permissionChecker, long bibliographyId, String actionId)
            throws PortalException {

        if (!contains(permissionChecker, bibliographyId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, Bibliography bibliography, String actionId) {

        if (permissionChecker.hasOwnerPermission(bibliography.getCompanyId(), Bibliography.class.getName(),
                bibliography.getBibliographyId(), bibliography.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(bibliography.getGroupId(), Bibliography.class.getName(),
                bibliography.getBibliographyId(), actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, long bibliographyId, String actionId)
            throws PortalException {

        Bibliography bibliography = BibliographyLocalServiceUtil.getBibliography(bibliographyId);

        return contains(permissionChecker, bibliography, actionId);
    }

}
