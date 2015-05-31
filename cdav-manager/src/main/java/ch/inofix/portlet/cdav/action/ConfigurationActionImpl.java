package ch.inofix.portlet.cdav.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-30 12:07
 * @modified 2015-05-30 12:07
 * @version 1.0.0
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String domain = ParamUtil.getString(actionRequest, "domain");
		String password = ParamUtil.getString(actionRequest, "password");
		String servername = ParamUtil.getString(actionRequest, "servername");
		String username = ParamUtil.getString(actionRequest, "username");

		setPreference(actionRequest, "domain", domain);
		setPreference(actionRequest, "password", password);
		setPreference(actionRequest, "servername", servername);
		setPreference(actionRequest, "username", username);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
