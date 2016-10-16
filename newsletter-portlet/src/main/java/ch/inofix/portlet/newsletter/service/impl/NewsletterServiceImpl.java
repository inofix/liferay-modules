package ch.inofix.portlet.newsletter.service.impl;

import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.security.permission.ActionKeys;
import ch.inofix.portlet.newsletter.service.NewsletterLocalServiceUtil;
import ch.inofix.portlet.newsletter.service.base.NewsletterServiceBaseImpl;
import ch.inofix.portlet.newsletter.service.permission.NewsletterPermission;
import ch.inofix.portlet.newsletter.service.permission.NewsletterPortletPermission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the newsletter remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.NewsletterService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-08 01:25
 * @modified 2016-10-16 23:46
 * @version 1.0.4
 * @see ch.inofix.portlet.newsletter.service.base.NewsletterServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.NewsletterServiceUtil
 */
public class NewsletterServiceImpl extends NewsletterServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.NewsletterServiceUtil} to access the
     * newsletter remote service.
     */

    /**
     *
     * @param userId
     * @param groupId
     * @param title
     * @param template
     * @param vCardGroupId
     * @param serviceContext
     * @return
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public Newsletter addNewsletter(long userId, long groupId, String title,
            String template, String fromAddress, String fromName,
            String vCardGroupId, ServiceContext serviceContext)
            throws PortalException, SystemException {

        NewsletterPortletPermission.check(getPermissionChecker(), groupId,
                ActionKeys.ADD_NEWSLETTER);

        return NewsletterLocalServiceUtil.addNewsletter(userId, groupId, title,
                template, fromAddress, fromName, vCardGroupId, serviceContext);

    }

    /**
     *
     * @return
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public Newsletter createNewsletter() throws PortalException,
            SystemException {

        // Create an empty newsletter - no permission check required
        return NewsletterLocalServiceUtil.createNewsletter(0);

    }

    /**
     * Delete a specific newsletter and return the deleted newsletter.
     *
     * @param newsletterId
     * @return the deleted newsletter
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public Newsletter deleteNewsletter(long newsletterId)
            throws PortalException, SystemException {

        NewsletterPermission.check(getPermissionChecker(), newsletterId,
                ActionKeys.DELETE);

        Newsletter newsletter = NewsletterLocalServiceUtil
                .deleteNewsletter(newsletterId);

        return newsletter;

    }

    /**
     * Return the newsletter.
     *
     * @param newsletterId
     * @return the latest version of a newsletter.
     * @since 1.0.0
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public Newsletter getNewsletter(long newsletterId) throws PortalException,
            SystemException {

        NewsletterPermission.check(getPermissionChecker(), newsletterId,
                ActionKeys.VIEW);

        return NewsletterLocalServiceUtil.getNewsletter(newsletterId);

    }

    /**
     *
     */
    @Override
    public Newsletter updateNewsletter(long userId, long groupId,
            long newsletterId, String title, String template,
            String fromAddress, String fromName, String vCardGroupId,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        NewsletterPermission.check(getPermissionChecker(), newsletterId,
                ActionKeys.UPDATE);

        return NewsletterLocalServiceUtil.updateNewsletter(userId, groupId,
                newsletterId, title, template, fromName, fromAddress,
                vCardGroupId, serviceContext);

    }

    private static Log _log = LogFactoryUtil.getLog(NewsletterServiceImpl.class
            .getName());
}
