package ch.inofix.portlet.newsletter.service.impl;

import java.util.List;
import java.util.Map;

import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.security.permission.ActionKeys;
import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;
import ch.inofix.portlet.newsletter.service.base.MailingServiceBaseImpl;
import ch.inofix.portlet.newsletter.service.permission.MailingPermission;
import ch.inofix.portlet.newsletter.service.permission.NewsletterPortletPermission;
import ch.inofix.portlet.newsletter.util.TemplateUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;

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
 * @modified 2016-10-12 14:44
 * @version 1.0.3
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
    public String prepareMailing(ThemeDisplay themeDisplay,
            Map<String, Object> contextObjects, long mailingId)
            throws PortalException, SystemException {

        _log.info("prepareMailing");

        Newsletter newsletter = null;
        JournalArticle article = null;

        String script = null;

        if (mailingId > 0) {

            Mailing mailing = getMailing(mailingId);

            long groupId = mailing.getGroupId();

            long newsletterId = mailing.getNewsletterId();

            if (newsletterId > 0) {

                newsletter = newsletterService.getNewsletter(newsletterId);
                script = newsletter.getTemplate();
            }

            String articleId = mailing.getArticleId();

            _log.info("articleId = " + articleId);

            if (Validator.isNotNull(articleId)) {
                article = JournalArticleServiceUtil.getLatestArticle(groupId,
                        articleId, WorkflowConstants.STATUS_APPROVED);
            }

        }

        String introduction = null;

        if (Validator.isNotNull(script)) {

            try {
                introduction = TemplateUtil
                        .transform(themeDisplay, contextObjects, script,
                                TemplateConstants.LANG_TYPE_FTL);
            } catch (Exception e) {
                _log.error(e);
                throw new SystemException(e);
            }
        }

        StringBuilder sb = new StringBuilder();

        if (Validator.isNotNull(introduction)) {
            sb.append("<div class=\"newsletter-introduction\">");
            sb.append(introduction);
            sb.append("</div>");
        }

        if (article != null) {

            String languageId = LanguageUtil.getLanguageId(themeDisplay
                    .getLocale());

            JournalArticleDisplay articleDisplay = JournalArticleLocalServiceUtil
                    .getArticleDisplay(article, null, null, languageId, 1,
                            null, themeDisplay);

            sb.append("<div class=\"newsletter-content\">");
            sb.append(articleDisplay.getContent());
            sb.append("</div>");

        }

        return sb.toString();

    }

    @Override
    public Mailing updateMailing(long userId, long groupId, long mailingId,
            String title, long newsletterId, String articleId, boolean sent,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.UPDATE);

        return MailingLocalServiceUtil
                .updateMailing(userId, groupId, mailingId, title, newsletterId,
                        articleId, sent, serviceContext);

    }

    private static Log _log = LogFactoryUtil.getLog(MailingServiceImpl.class
            .getName());
}
