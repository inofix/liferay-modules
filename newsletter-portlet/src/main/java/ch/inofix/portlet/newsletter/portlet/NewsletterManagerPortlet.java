package ch.inofix.portlet.newsletter.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.portlet.util.PortletUtil;
import ch.inofix.portlet.newsletter.service.MailingServiceUtil;
import ch.inofix.portlet.newsletter.service.NewsletterServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
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
 * @modified 2016-10-11 15:16
 * @version 1.0.6
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
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String articleId = ParamUtil.getString(actionRequest, "articleId");
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
                    mailingId, title, newsletterId, articleId, sent, serviceContext);
            SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                    PortletUtil.translate("successfully-updated-the-mailing"));
        } else {
            mailing = MailingServiceUtil.addMailing(userId, groupId, title,
                    newsletterId, articleId, serviceContext);
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

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        String backURL = ParamUtil.getString(actionRequest, "backURL");
        long newsletterId = ParamUtil.getLong(actionRequest, "newsletterId");
        String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String windowId = ParamUtil.getString(actionRequest, "windowId");

        Newsletter newsletter = null;

        String title = ParamUtil.getString(actionRequest, "title");
        String template = ParamUtil.getString(actionRequest, "template");
        String vCardGroupId = ParamUtil
                .getString(actionRequest, "vCardGroupId");

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
            newsletter = NewsletterServiceUtil.updateNewsletter(userId,
                    groupId, newsletterId, title, template, vCardGroupId,
                    serviceContext);
            SessionMessages.add(actionRequest, REQUEST_PROCESSED, PortletUtil
                    .translate("successfully-updated-the-newsletter"));
        } else {
            newsletter = NewsletterServiceUtil.addNewsletter(userId, groupId,
                    title, template, vCardGroupId, serviceContext);
            SessionMessages.add(actionRequest, REQUEST_PROCESSED,
                    PortletUtil.translate("successfully-added-the-newsletter"));
        }

        actionRequest.setAttribute("NEWSLETTER", newsletter);

    }

    private static Log _log = LogFactoryUtil
            .getLog(NewsletterManagerPortlet.class.getName());
    private static String REQUEST_PROCESSED = "request_processed";
}
