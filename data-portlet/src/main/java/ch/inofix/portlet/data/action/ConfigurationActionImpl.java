package ch.inofix.portlet.data.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-16 20:39
 * @modified 2017-11-20 18:08
 * @version 1.0.1
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String namespace = portletConfig.getDefaultNamespace();

        String[] dataURLs = actionRequest.getParameterValues(namespace + "dataURL");
        String[] idFields = actionRequest.getParameterValues(namespace + "idField");
        String[] nameFields = actionRequest.getParameterValues(namespace + "nameField");
        String[] passwords = actionRequest.getParameterValues(namespace + "password");
        String[] timestampFields = actionRequest.getParameterValues(namespace + "timestampField");
        String[] userIds = actionRequest.getParameterValues(namespace + "userId");
        String[] userNames = actionRequest.getParameterValues(namespace + "userName");

        setPreference(actionRequest, "dataURL", StringUtil.merge(dataURLs));
        setPreference(actionRequest, "idField", StringUtil.merge(idFields));
        setPreference(actionRequest, "nameField", StringUtil.merge(nameFields));
        setPreference(actionRequest, "password", StringUtil.merge(passwords));
        setPreference(actionRequest, "timestampField", StringUtil.merge(timestampFields));
        setPreference(actionRequest, "userId", StringUtil.merge(userIds));
        setPreference(actionRequest, "userName", StringUtil.merge(userNames));

        super.processAction(portletConfig, actionRequest, actionResponse);

    }
}
