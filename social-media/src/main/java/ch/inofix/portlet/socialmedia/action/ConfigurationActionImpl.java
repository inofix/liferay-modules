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
 * @modified 2015-08-20 16:51
 * @version 1.0.1
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
		String orientation = ParamUtil.getString(actionRequest, "orientation");
		String[] services = ParamUtil.getParameterValues(actionRequest,
				"services");
		String showBuildInfo = ParamUtil.getString(actionRequest,
				"showBuildInfo");
		String theme = ParamUtil.getString(actionRequest, "theme");

		setPreference(actionRequest, "backend-url", backendUrl);
		setPreference(actionRequest, "orientation", orientation);
		setPreference(actionRequest, "services", services);
		setPreference(actionRequest, "show-build-info", showBuildInfo);
		setPreference(actionRequest, "theme", theme);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}
}
