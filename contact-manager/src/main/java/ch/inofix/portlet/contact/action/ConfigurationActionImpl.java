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
 * @modified 2015-05-25 17:57
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

		// String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String[] columns = ParamUtil.getParameterValues(actionRequest,
				"columns");
		String showDeathDate = ParamUtil.getString(actionRequest,
				"showDeathDate");
		
		setPreference(actionRequest, "columns", columns);
		setPreference(actionRequest, "show-death-date", showDeathDate);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
