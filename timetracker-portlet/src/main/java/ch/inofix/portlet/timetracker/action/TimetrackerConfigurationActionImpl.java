package ch.inofix.portlet.timetracker.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import ch.inofix.portlet.timetracker.util.TimeFormat;
import ch.inofix.portlet.timetracker.util.TimetrackerPortletKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * TimeTrackerConfigurationActionImpl handles the portlet configuration of the
 * timetracker portlet.
 * 
 * @author Christian Berndt
 * @created 2013-10-18 11:37
 * @modified 2013-10-18 11:37
 * @version 1.0
 * 
 */
public class TimetrackerConfigurationActionImpl extends
		DefaultConfigurationAction {

	// Enable logging for this class.
	private static final Log _log = LogFactoryUtil
			.getLog(TimetrackerConfigurationActionImpl.class.getName());

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		_log.info("Executing processAction().");

		String timeFormat = ParamUtil.getString(actionRequest,
				TimetrackerPortletKeys.TIME_FORMAT, TimeFormat.FROM_UNTIL);

		_log.debug(TimetrackerPortletKeys.TIME_FORMAT + " = " + timeFormat);

		setPreference(actionRequest, TimetrackerPortletKeys.TIME_FORMAT,
				timeFormat);

		super.processAction(portletConfig, actionRequest, actionResponse);

	}
	
	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		_log.info("Executing render().");

		return TimetrackerPortletKeys.CONFIG_JSP;

	}

}
