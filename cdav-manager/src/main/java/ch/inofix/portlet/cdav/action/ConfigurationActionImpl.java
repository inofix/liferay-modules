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
 * @modified 2015-06-12 13:49
 * @version 1.0.3
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String calendar = ParamUtil.getString(actionRequest, "calendar");
		String calendarId = ParamUtil.getString(actionRequest, "calendarId");
		String domain = ParamUtil.getString(actionRequest, "domain");
		String password = ParamUtil.getString(actionRequest, "password");
		String restoreFromTrash = ParamUtil.getString(actionRequest, "restoreFromTrash");
		String servername = ParamUtil.getString(actionRequest, "servername");
		String syncOnlyUpcoming = ParamUtil.getString(actionRequest, "syncOnlyUpcoming");
		String username = ParamUtil.getString(actionRequest, "username");

		setPreference(actionRequest, "calendar", calendar);
		setPreference(actionRequest, "calendarId", calendarId);
		setPreference(actionRequest, "domain", domain);
		setPreference(actionRequest, "password", password);
		setPreference(actionRequest, "restoreFromTrash", restoreFromTrash);
		setPreference(actionRequest, "servername", servername);
		setPreference(actionRequest, "syncOnlyUpcoming", syncOnlyUpcoming);
		setPreference(actionRequest, "username", username);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
