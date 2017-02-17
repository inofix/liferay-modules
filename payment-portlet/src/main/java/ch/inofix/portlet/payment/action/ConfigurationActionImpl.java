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
 * @modified 2017-02-17 12:42
 * @version 1.0.1
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String apiKey = ParamUtil.getString(actionRequest, "apiKey");
        String defaultCountry = ParamUtil.getString(actionRequest, "defaultCountry");
        String merchantName = ParamUtil
                .getString(actionRequest, "merchantName");
        String[] services = ParamUtil.getParameterValues(actionRequest,
                "services");
        String showCurrency = ParamUtil
                .getString(actionRequest, "showCurrency");
        String showDuration = ParamUtil
                .getString(actionRequest, "showDuration");
        String showLocale = ParamUtil.getString(actionRequest, "showLocale");
        String showOriginalTransactionId = ParamUtil.getString(actionRequest,
                "showOriginalTransactionId");
        String showRecurring = ParamUtil.getString(actionRequest,
                "showRecurring");
        String showShippingCosts = ParamUtil.getString(actionRequest,
                "showShippingCosts");
        String showVat = ParamUtil.getString(actionRequest, "showVat");
        String vat = ParamUtil.getString(actionRequest, "vat");

        setPreference(actionRequest, "apiKey", apiKey);
        setPreference(actionRequest, "defaultCountry", defaultCountry);
        setPreference(actionRequest, "merchantName", merchantName);
        setPreference(actionRequest, "services", services);
        setPreference(actionRequest, "showCurrency", showCurrency);
        setPreference(actionRequest, "showDuration", showDuration);
        setPreference(actionRequest, "showLocale", showLocale);
        setPreference(actionRequest, "showOriginalTransactionId",
                showOriginalTransactionId);
        setPreference(actionRequest, "showRecurring", showRecurring);
        setPreference(actionRequest, "showShippingCosts", showShippingCosts);
        setPreference(actionRequest, "showVat", showVat);
        setPreference(actionRequest, "vat", vat);

        super.processAction(portletConfig, actionRequest, actionResponse);

    }

    private static Log _log = LogFactoryUtil
            .getLog(ConfigurationActionImpl.class.getName());
}
