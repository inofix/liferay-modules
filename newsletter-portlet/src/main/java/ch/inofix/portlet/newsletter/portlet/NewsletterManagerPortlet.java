package ch.inofix.portlet.newsletter.portlet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.newsletter.EmailAddressException;
import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.model.Subscriber;
import ch.inofix.portlet.newsletter.portlet.util.PortletUtil;
import ch.inofix.portlet.newsletter.service.MailingServiceUtil;
import ch.inofix.portlet.newsletter.service.NewsletterServiceUtil;
import ch.inofix.portlet.newsletter.service.SubscriberServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * NewsletterManagerPortlet: MVC-Controller of the newsletter-portlet.
 *
 * @author Christian Berndt
 * @created 2016-10-08 00:20
 * @modified 2016-10-20 18:32
 * @version 1.1.3
 */
public class NewsletterManagerPortlet extends MVCPortlet {

    /**
     *
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.4
     * @throws PortalException
     * @throws SystemException
     */
    public void deleteMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws PortalException,
            SystemException {

        // Get the parameters from the request.
        long mailingId = ParamUtil.getLong(actionRequest, "mailingId");

        // Delete the requested mailing.
        MailingServiceUtil.deleteMailing(mailingId);

        SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                PortletUtil.translate("successfully-deleted-the-mailing"));

        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("mailingId",
                String.valueOf(mailingId));
        actionResponse.setRenderParameter("tabs1", tabs1);

    }

    /**
     *
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.2
     * @throws PortalException
     * @throws SystemException
     */
    public void deleteNewsletter(ActionRequest actionRequest,
            ActionResponse actionResponse) throws PortalException,
            SystemException {

        // Get the parameters from the request.
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");

        // Delete the requested newsletter.
        NewsletterServiceUtil.deleteNewsletter(newsletterId);

        SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                PortletUtil.translate("successfully-deleted-the-newsletter"));

        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("newsletterId",
                String.valueOf(newsletterId));
        actionResponse.setRenderParameter("tabs1", tabs1);

    }

    /**
     *
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void editMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long mailingId = ParamUtil.getLong(actionRequest, "mailingId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        Mailing mailing = null;

        if (mailingId > 0) {
            mailing = MailingServiceUtil.getMailing(mailingId);
        } else {
            mailing = MailingServiceUtil.createMailing();
        }

        actionRequest.setAttribute("MAILING", mailing);

        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("mailingId",
                String.valueOf(mailingId));
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("tabs1", tabs1);
        actionResponse.setRenderParameter("windowId", windowId);

    }

    /**
     *
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void editNewsletter(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        Newsletter newsletter = null;

        if (newsletterId > 0) {
            newsletter = NewsletterServiceUtil.getNewsletter(newsletterId);
        } else {
            newsletter = NewsletterServiceUtil.createNewsletter();
        }

        actionRequest.setAttribute("NEWSLETTER", newsletter);

        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("newsletterId",
                String.valueOf(newsletterId));
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("windowId", windowId);

    }

    /**
     * Disable the get- / sendRedirect feature of LiferayPortlet.
     *
     * @since 1.0.0
     */
    @Override
    protected String getRedirect(ActionRequest actionRequest,
            ActionResponse actionResponse) {

        return null;
    }

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.1.3
     * @throws Exception
     */
    public void previewMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {
        
        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        SearchContext searchContext = SearchContextFactory.getInstance(request);

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long mailingId = ParamUtil.getLong(actionRequest, "mailingId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        long subscriberId = ParamUtil.getLong(actionRequest, "subscriberId");
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        String vCardGroupId = ParamUtil.getString(actionRequest, "vCardGroupId");
        String vCardUID = ParamUtil.getString(actionRequest, "vCardUID");
        String windowId = ParamUtil.getString(actionRequest, "windowId");
        
        Mailing mailing = null;

        if (mailingId > 0) {
            mailing = MailingServiceUtil.getMailing(mailingId);
        }

        actionRequest.setAttribute("MAILING", mailing);

        Subscriber subscriber = null;

        if (subscriberId > 0) {
            subscriber = SubscriberServiceUtil.getSubscriber(subscriberId);
        }
        if (Validator.isNotNull(vCardUID)) {
            subscriber = SubscriberServiceUtil.getSubscriber(
                    themeDisplay.getScopeGroupId(), searchContext, vCardUID);
        }

        actionRequest.setAttribute("SUBSCRIBER", subscriber);

        Newsletter newsletter = null;

        if (newsletterId > 0) {
            newsletter = NewsletterServiceUtil.getNewsletter(newsletterId);
        }

        if (newsletter == null && Validator.isNotNull(vCardGroupId)) {
            newsletter = NewsletterServiceUtil.getNewsletter(vCardGroupId);
        }

        actionRequest.setAttribute("NEWSLETTER", newsletter);

        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("mailingId", String.valueOf(mailingId));
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("newsletterId", String.valueOf(newsletterId));
        actionResponse.setRenderParameter("subscriberId", String.valueOf(subscriberId));
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("tabs1", tabs1);
        actionResponse.setRenderParameter("vCardGroupId", vCardGroupId);
        actionResponse.setRenderParameter("vCardUID", vCardUID);
        actionResponse.setRenderParameter("windowId", windowId);

    }

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.4
     * @throws Exception
     */
    public void saveMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long mailingId = ParamUtil.getLong(actionRequest, "mailingId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        Mailing mailing = null;

        String title = ParamUtil.getString(actionRequest, "title");
        String template = ParamUtil.getString(actionRequest, "template");
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String articleId = ParamUtil.getString(actionRequest, "articleId");
        // TODO:
        Date sendDate = null;
        boolean sent = ParamUtil.getBoolean(actionRequest, "sent");

        // Pass the required parameters to the render phase

        actionResponse.setRenderParameter("mailingId",
                String.valueOf(mailingId));
        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("windowId", windowId);

        // Save the mailing

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Mailing.class.getName(), actionRequest);

        if (mailingId > 0) {
            mailing = MailingServiceUtil.updateMailing(userId, groupId,
                    mailingId, title, template, newsletterId, articleId,
                    sendDate, sent, serviceContext);
            SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                    PortletUtil.translate("successfully-updated-the-mailing"));
        } else {
            mailing = MailingServiceUtil.addMailing(userId, groupId, title,
                    template, newsletterId, articleId, serviceContext);
            SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                    PortletUtil.translate("successfully-added-the-mailing"));
        }

        actionRequest.setAttribute("MAILING", mailing);

    }

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.1
     * @throws Exception
     */
    public void saveNewsletter(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        boolean hasErrors = false;

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        Newsletter newsletter = null;
        if (newsletterId > 0) {
            newsletter = NewsletterServiceUtil.getNewsletter(newsletterId);
        } else {
            newsletter = NewsletterServiceUtil.createNewsletter();
        }

        String title = ParamUtil.getString(actionRequest, "title");
        String template = ParamUtil.getString(actionRequest, "template");
        String fromAddress = ParamUtil.getString(actionRequest, "fromAddress");
        String fromName = ParamUtil.getString(actionRequest, "fromName");
        String vCardGroupId = ParamUtil
                .getString(actionRequest, "vCardGroupId");

        if (!Validator.isEmailAddress(fromAddress)) {
            SessionErrors.add(actionRequest, EmailAddressException.class);
            hasErrors = true;
        }

        // Pass the required parameters to the render phase

        actionResponse.setRenderParameter("newsletterId",
                String.valueOf(newsletterId));
        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("windowId", windowId);

        // Save the newsletter

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Newsletter.class.getName(), actionRequest);

        if (newsletterId > 0) {

            if (!hasErrors) {

                newsletter = NewsletterServiceUtil.updateNewsletter(userId,
                        groupId, newsletterId, title, template, fromAddress,
                        fromName, vCardGroupId, serviceContext);

                SessionMessages
                        .add(actionRequest,
                                REQUEST_PROCESSED,
                                PortletUtil
                                        .translate("successfully-updated-the-newsletter"));
            }
        } else {

            if (!hasErrors) {

                newsletter = NewsletterServiceUtil.addNewsletter(userId,
                        groupId, title, template, fromAddress, fromName,
                        vCardGroupId, serviceContext);

                SessionMessages
                        .add(actionRequest, REQUEST_PROCESSED, PortletUtil
                                .translate("successfully-added-the-newsletter"));
            }
        }

        actionRequest.setAttribute("NEWSLETTER", newsletter);
    }

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.7
     * @throws Exception
     */
    public void sendMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        boolean hasErrors = false;

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long mailingId = ParamUtil.getLong(actionRequest, "mailingId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        String email = ParamUtil.getString(actionRequest, "email");

        if (Validator.isNotNull(email)) {
            if (!Validator.isEmailAddress(email)) {
                SessionErrors.add(actionRequest, EmailAddressException.class);
                hasErrors = true;
            }
        }

        Mailing mailing = null;

        if (mailingId > 0) {
            mailing = MailingServiceUtil.getMailing(mailingId);
        } else {
            SessionErrors.add(actionRequest,
                    PortletUtil.translate("you-must-save-the-mailing-first"));
            hasErrors = true;
        }

        Newsletter newsletter = null;

        if (mailing != null) {

            long newsletterId = mailing.getNewsletterId();

            if (newsletterId > 0) {
                newsletter = NewsletterServiceUtil.getNewsletter(newsletterId);
            } else {
                // TODO: set error message
            }
        }

        // Process the mailing

        actionRequest.setAttribute("MAILING", mailing);

        long groupId = themeDisplay.getScopeGroupId();

        Map<String, String[]> parameterMap = new HashMap<String, String[]>(
                actionRequest.getParameterMap());

        String servletContextName = request.getSession().getServletContext()
                .getServletContextName();

        String[] servletContextNames = new String[] { servletContextName };

        parameterMap.put("servletContextNames", servletContextNames);

        String taskName = "sendMailings";

        long userId = themeDisplay.getUserId();

        if (!hasErrors) {

            MailingServiceUtil.sendMailingsInBackground(userId, taskName,
                    groupId, parameterMap);

            if (Validator.isNotNull(email)) {

                // test mail

                SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                        PortletUtil.translate("the-mailing-has-been-sent-to-x",
                                email));

                email = "";

            } else if (newsletter != null) {

                // mark mailing as sent

                ServiceContext serviceContext = ServiceContextFactory
                        .getInstance(Newsletter.class.getName(), actionRequest);

                MailingServiceUtil.updateMailing(mailing.getUserId(),
                        mailing.getGroupId(), mailing.getMailingId(),
                        mailing.getTitle(), mailing.getTemplate(),
                        mailing.getNewsletterId(), mailing.getArticleId(),
                        new Date(), true, serviceContext);

                SessionMessages
                        .add(actionRequest,
                                REQUEST_PROCESSED,
                                PortletUtil
                                        .translate(
                                                "the-mailing-has-been-sent-to-the-subscribers-of-x",
                                                newsletter.getTitle()));
            }
        } else {
            SessionErrors.add(actionRequest, "an-error-occurred");
        }

        // Pass the required parameters to the render phase

        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("mailingId",
                String.valueOf(mailingId));
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("tabs1", tabs1);
        actionResponse.setRenderParameter("windowId", windowId);
        actionResponse.setRenderParameter("email", email);
    }

    private static Log _log = LogFactoryUtil
            .getLog(NewsletterManagerPortlet.class.getName());
    private static String REQUEST_PROCESSED = "request_processed";
}
