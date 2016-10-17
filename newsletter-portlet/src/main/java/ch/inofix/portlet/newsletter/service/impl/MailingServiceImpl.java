package ch.inofix.portlet.newsletter.service.impl;

import java.util.Date;
import java.util.Locale;
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
 * @modified 2016-10-17 16:33
 * @version 1.0.7
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
            String template, long newsletterId, String articleId,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        NewsletterPortletPermission.check(getPermissionChecker(), groupId,
                ActionKeys.ADD_NEWSLETTER);

        return MailingLocalServiceUtil.addMailing(userId, groupId, title,
                template, newsletterId, articleId, serviceContext);

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
    public Mailing getMailing(long mailingId) throws PortalException,
            SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.VIEW);

        return MailingLocalServiceUtil.getMailing(mailingId);

    }

    @Override
    public String prepareMailing(Map<String, Object> contextObjects,
            long mailingId) throws PortalException, SystemException {

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

            // mailing.template overrides newsletter.template

            if (Validator.isNotNull(mailing.getTemplate())) {
                script = mailing.getTemplate();
            }

            String articleId = mailing.getArticleId();

            if (Validator.isNotNull(articleId)) {
                article = JournalArticleServiceUtil.getLatestArticle(groupId,
                        articleId, WorkflowConstants.STATUS_APPROVED);
            }
        }

        String introduction = null;

        if (Validator.isNotNull(script)) {

            try {
                introduction = TemplateUtil.transform(contextObjects, script,
                        TemplateConstants.LANG_TYPE_FTL);
            } catch (Exception e) {
                throw new PortalException(e);
            }
        }

        StringBuilder sb = new StringBuilder();

        if (Validator.isNotNull(introduction)) {
            sb.append("<div class=\"newsletter-introduction\">");
            sb.append(introduction);
            sb.append("</div>");
        }

        if (article != null) {

            String languageId = (String) contextObjects.get("languageId");
            if (Validator.isNull(languageId)) {
                languageId = LanguageUtil.getLanguageId(Locale.US);
            }

            JournalArticleDisplay articleDisplay = JournalArticleLocalServiceUtil
                    .getArticleDisplay(article, null, null, languageId, 1,
                            null, null);

            sb.append("<div class=\"newsletter-content\">");
            sb.append(articleDisplay.getContent());
            sb.append("</div>");

        }

        return sb.toString();

    }

    @Override
    public long sendMailingsInBackground(long userId, String taskName,
            long groupId, Map<String, String[]> parameterMap)
            throws PortalException, SystemException {

        // TODO: check permissions
        return mailingLocalService.sendMailingsInBackground(userId, taskName,
                groupId, parameterMap);
    }

    @Override
    public Mailing updateMailing(long userId, long groupId, long mailingId,
            String title, String template, long newsletterId, String articleId,
            Date sendDate, boolean sent, ServiceContext serviceContext)
            throws PortalException, SystemException {

        MailingPermission.check(getPermissionChecker(), mailingId,
                ActionKeys.UPDATE);

        return MailingLocalServiceUtil.updateMailing(userId, groupId,
                mailingId, title, template, newsletterId, articleId, sendDate,
                sent, serviceContext);

    }

    private static Log _log = LogFactoryUtil.getLog(MailingServiceImpl.class
            .getName());
}