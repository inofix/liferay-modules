package ch.inofix.portlet.newsletter.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import ch.inofix.portlet.newsletter.model.Newsletter;
import ch.inofix.portlet.newsletter.service.NewsletterServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * NewsletterManagerPortlet: MVC-Controller of the newsletter-portlet.
 *
 * @author Christian Berndt
 * @created 2016-10-08 00:20
 * @modified 2016-10-08 00:20
 * @version 1.0.0
 */
public class NewsletterManagerPortlet extends MVCPortlet {

    public void editMailing(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        _log.info("editMailing()");

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

        _log.info("editNewsletter()");

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

    private static Log _log = LogFactoryUtil
            .getLog(NewsletterManagerPortlet.class.getName());
}
