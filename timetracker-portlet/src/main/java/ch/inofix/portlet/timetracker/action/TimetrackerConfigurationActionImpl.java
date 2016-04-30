
package ch.inofix.portlet.timetracker.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

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
 * @modified 2016-04-30 00:02
 * @version 1.0.2
 */
public class TimetrackerConfigurationActionImpl
    extends DefaultConfigurationAction {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(TimetrackerConfigurationActionImpl.class.getName());

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String[] columns =
            ParamUtil.getParameterValues(actionRequest, "columns");
        String maxLength = ParamUtil.getString(actionRequest, "max-length");
        String timeFormat =
            ParamUtil.getString(actionRequest, "time-format", "from-until");

        setPreference(actionRequest, "columns", columns);
        setPreference(actionRequest, "max-length", maxLength);
        setPreference(actionRequest, "time-format", timeFormat);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
