package ch.inofix.portlet.cdav.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-30 12:07
 * @modified 2015-07-31 16:19
 * @version 1.0.4
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String[] calendars = actionRequest.getParameterValues("calendar"); 
		String[] calendarIds = actionRequest.getParameterValues("calendarId");
		String[] domains = actionRequest.getParameterValues("domain");
		String[] passwords = actionRequest.getParameterValues("password");
		String[] restoreFromTrash = actionRequest.getParameterValues("restoreFromTrash");
		String[] servernames = actionRequest.getParameterValues("servername");
		String[] syncOnlyUpcoming = actionRequest.getParameterValues("syncOnlyUpcoming");
		String[] usernames = actionRequest.getParameterValues("username");

		setPreference(actionRequest, "calendar", calendars);
		setPreference(actionRequest, "calendarId", calendarIds);
		setPreference(actionRequest, "domain", domains);
		setPreference(actionRequest, "password", passwords);
		setPreference(actionRequest, "restoreFromTrash", restoreFromTrash);
		setPreference(actionRequest, "servername", servernames);
		setPreference(actionRequest, "syncOnlyUpcoming", syncOnlyUpcoming);
		setPreference(actionRequest, "username", usernames);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
