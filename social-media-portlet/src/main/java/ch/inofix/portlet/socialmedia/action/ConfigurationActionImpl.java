package ch.inofix.portlet.socialmedia.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-08-20 13:54
 * @modified 2017-02-13 13:10
 * @version 1.0.4
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	// Enable logging for this class
	private static Log log = LogFactoryUtil
			.getLog(ConfigurationActionImpl.class.getName());

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String backendUrl = ParamUtil.getString(actionRequest, "backendUrl");
        String fbApiKey = ParamUtil.getString(actionRequest, "fbApiKey");
        String fbCaption = ParamUtil.getString(actionRequest, "fbCaption");
        String fbDescription = ParamUtil.getString(actionRequest,
                "fbDescription");
        String fbDisplay = ParamUtil.getString(actionRequest, "fbDisplay");
        String fbFrom = ParamUtil.getString(actionRequest, "fbFrom");
        String fbName = ParamUtil.getString(actionRequest, "fbName");
        String fbPicture = ParamUtil.getString(actionRequest, "fbPicture");
        String fbRedirectUri = ParamUtil.getString(actionRequest,
                "fbRedirectUri");
        String fbRef = ParamUtil.getString(actionRequest, "fbRef");
        String fbSource = ParamUtil.getString(actionRequest, "fbSource");
        String fbTo = ParamUtil.getString(actionRequest, "fbTo");
        String githubUrl = ParamUtil.getString(actionRequest, "githubUrl");
        String mailBody = ParamUtil.getString(actionRequest, "mailBody");
        String mailSubject = ParamUtil.getString(actionRequest, "mailSubject");
        String mailUrl = ParamUtil.getString(actionRequest, "mailUrl");
        String orientation = ParamUtil.getString(actionRequest, "orientation");
        String[] services = ParamUtil.getParameterValues(actionRequest,
                "services");
        String shareUrl = ParamUtil.getString(actionRequest, "shareUrl");
        String showBuildInfo = ParamUtil.getString(actionRequest,
                "showBuildInfo");
        String theme = ParamUtil.getString(actionRequest, "theme");
        String twitterVia = ParamUtil.getString(actionRequest, "twitterVia");
        String useContainer = ParamUtil
                .getString(actionRequest, "useContainer");

        setPreference(actionRequest, "backend-url", backendUrl);
        setPreference(actionRequest, "fb-api-key", fbApiKey);
        setPreference(actionRequest, "fb-caption", fbCaption);
        setPreference(actionRequest, "fb-description", fbDescription);
        setPreference(actionRequest, "fb-display", fbDisplay);
        setPreference(actionRequest, "fb-from", fbFrom);
        setPreference(actionRequest, "fb-name", fbName);
        setPreference(actionRequest, "fb-picture", fbPicture);
        setPreference(actionRequest, "fb-redirect-uri", fbRedirectUri);
        setPreference(actionRequest, "fb-ref", fbRef);
        setPreference(actionRequest, "fb-source", fbSource);
        setPreference(actionRequest, "fb-to", fbTo);
        setPreference(actionRequest, "github-url", githubUrl);
        setPreference(actionRequest, "mail-body", mailBody);
        setPreference(actionRequest, "mail-subject", mailSubject);
        setPreference(actionRequest, "mail-url", mailUrl);
        setPreference(actionRequest, "orientation", orientation);
        setPreference(actionRequest, "services", services);
        setPreference(actionRequest, "share-url", shareUrl);
        setPreference(actionRequest, "show-build-info", showBuildInfo);
        setPreference(actionRequest, "theme", theme);
        setPreference(actionRequest, "twitter-via", twitterVia);
        setPreference(actionRequest, "use-container", useContainer);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
