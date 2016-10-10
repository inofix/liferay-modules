package ch.inofix.portlet.newsletter.service.permission;

import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-10 17:37
 * @modified 2016-10-10 17:37
 * @version 1.0.0
 *
 */
public class MailingPermission {

    /**
     *
     * @param permissionChecker
     * @param mailingId
     * @param actionId
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public static void check(PermissionChecker permissionChecker,
            long mailingId, String actionId) throws PortalException,
            SystemException {

        if (!contains(permissionChecker, mailingId, actionId)) {
            throw new PrincipalException();
        }
    }

    /**
     *
     * @param permissionChecker
     * @param mailingId
     * @param actionId
     * @return
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    public static boolean contains(PermissionChecker permissionChecker,
            long mailingId, String actionId) throws PortalException,
            SystemException {

        Mailing mailing = MailingLocalServiceUtil.getMailing(mailingId);

        if (permissionChecker.hasOwnerPermission(mailing.getCompanyId(),
                Mailing.class.getName(), mailing.getMailingId(),
                mailing.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(mailing.getGroupId(),
                Mailing.class.getName(), mailing.getNewsletterId(), actionId);

    }
}
