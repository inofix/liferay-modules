
package ch.inofix.portlet.proxy.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2016-04-14 11:40
 * @modified 2016-04-14 11:40
 * @version 1.0.0
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    // Enable logging for this class
    private static Log log =
        LogFactoryUtil.getLog(ConfigurationActionImpl.class.getName());

    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws Exception {

        String[] hosts = ParamUtil.getParameterValues(actionRequest, "host");
        // String portrait = ParamUtil.getString(actionRequest, "portrait");

        setPreference(actionRequest, "hosts", hosts);
        // setPreference(actionRequest, "portrait", portrait);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
