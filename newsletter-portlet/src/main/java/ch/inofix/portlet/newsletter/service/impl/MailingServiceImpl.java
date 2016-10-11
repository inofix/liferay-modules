package ch.inofix.portlet.newsletter.service.impl;

import java.util.List;

import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.security.permission.ActionKeys;
import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;
import ch.inofix.portlet.newsletter.service.base.MailingServiceBaseImpl;
import ch.inofix.portlet.newsletter.service.permission.MailingPermission;
import ch.inofix.portlet.newsletter.service.permission.NewsletterPortletPermission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the mailing remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.MailingService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-10 17:19
 * @modified 2016-10-11 00:35
 * @version 1.0.1
 * @see ch.inofix.portlet.newsletter.service.base.MailingServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.MailingServiceUtil
 */
public class MailingServiceImpl extends MailingServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.MailingServiceUtil} to access the
     * mailing remote service.
     */

    @Override
    public Mailing addMailing(long userId, long groupId, String title,
            long newsletterId, String articleId, ServiceContext serviceContext)
            throws PortalException, SystemException {

        NewsletterPortletPermission.check(getPermissionChecker(), groupId,
                ActionKeys.ADD_NEWSLETTER);

        return MailingLocalServiceUtil.addMailing(userId, groupId, title,
                newsletterId, articleId, serviceContext);

    }

    @Override
    public Mailing createMailing() throws PortalException, SystemException {

        // Create an empty mailing - no permission check required
        return MailingLocalServiceUtil.createMailing(0);

    }

    @Override
    public Mailing deleteMailing(long mailingId) throws PortalException,
            SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.DELETE);

        Mailing mailing = MailingLocalServiceUtil.deleteMailing(mailingId);

        return mailing;

    }

    @Override
    public List<Mailing> getGroupMailings(long groupId, int start, int end)
            throws PortalException, SystemException {

        // TODO: check permissions
        // TODO: filter by group
        return mailingLocalService.getMailings(start, end);
    }

    @Override
    public Mailing getMailing(long mailingId) throws PortalException,
            SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.VIEW);

        return MailingLocalServiceUtil.getMailing(mailingId);

    }

    @Override
    public Mailing updateMailing(long userId, long groupId, long mailingId,
            String title, long newsletterId, String articleId, boolean sent,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.UPDATE);

        return MailingLocalServiceUtil.updateMailing(userId, groupId,
                mailingId, title, newsletterId, articleId, sent, serviceContext);

    }

    private static Log _log = LogFactoryUtil.getLog(MailingServiceImpl.class
            .getName());
}
