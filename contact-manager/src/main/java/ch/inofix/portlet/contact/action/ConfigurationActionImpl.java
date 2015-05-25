package ch.inofix.portlet.contact.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-15 11:33
 * @modified 2015-05-15 11:33
 * @version 1.0.0
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

		log.info("Executing processAction().");

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String[] columns = ParamUtil.getParameterValues(actionRequest,
				"columns");

		log.info("columns.length = " + columns.length);
		
		setPreference(actionRequest, "columns", columns);
		
		super.processAction(portletConfig, actionRequest, actionResponse);

	}

}
