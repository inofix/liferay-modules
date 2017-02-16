package ch.inofix.portlet.payment.action;

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
 * @created 2017-02-16 17:43
 * @modified 2017-02-16 17:43
 * @version 1.0.0
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String apiKey = ParamUtil.getString(actionRequest, "apiKey");
        String[] services = ParamUtil.getParameterValues(actionRequest,
                "services");
        
        setPreference(actionRequest, "apiKey", apiKey);
        setPreference(actionRequest, "services", services);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }

    private static Log _log = LogFactoryUtil
            .getLog(ConfigurationActionImpl.class.getName());
}
