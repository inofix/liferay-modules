package ch.inofix.portlet.contact.action;

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
 * @created 2015-05-25 11:33
 * @modified 2015-06-29 15:55
 * @version 1.0.2
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

		// String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String[] columns = ParamUtil.getParameterValues(actionRequest,
				"columns");
		String portrait = ParamUtil.getString(actionRequest,
				"portrait");
		String portraitFemale = ParamUtil.getString(actionRequest,
				"portraitFemale");
		String portraitClass = ParamUtil.getString(actionRequest,
				"portraitClass");
		String portraitGroup = ParamUtil.getString(actionRequest,
				"portraitGroup");
		String portraitMale = ParamUtil.getString(actionRequest,
				"portraitMale");
		String portraitOrganization = ParamUtil.getString(actionRequest,
				"portraitOrganization");
		String portraitStyle = ParamUtil.getString(actionRequest,
				"portraitStyle");
		String showDeathDate = ParamUtil.getString(actionRequest,
				"showDeathDate");

		setPreference(actionRequest, "columns", columns);
		setPreference(actionRequest, "portrait", portrait);
		setPreference(actionRequest, "portraitClass", portraitClass);
		setPreference(actionRequest, "portraitFemale", portraitFemale);
		setPreference(actionRequest, "portraitGroup", portraitGroup);
		setPreference(actionRequest, "portraitMale", portraitMale);
		setPreference(actionRequest, "portraitOrganization", portraitOrganization);
		setPreference(actionRequest, "portraitStyle", portraitStyle);
		setPreference(actionRequest, "show-death-date", showDeathDate);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
