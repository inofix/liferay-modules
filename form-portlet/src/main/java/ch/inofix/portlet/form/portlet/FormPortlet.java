package ch.inofix.portlet.form.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import ch.inofix.portlet.form.MemberNumberException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Christian Berndt
 * @created 2016-02-20 14:24
 * @modified 2016-02-20 14:24
 * @version 1.0.0
 *
 */
public class FormPortlet extends MVCPortlet {

    public void processForm(ActionRequest actionRequest,
            ActionResponse actionResponse) throws PortalException,
            SystemException, IOException {

        PortletPreferences preferences = actionRequest.getPreferences();

        String address = ParamUtil.getString(actionRequest, "address");
        String address2 = ParamUtil.getString(actionRequest, "address2");
        String amount = ParamUtil.getString(actionRequest, "amount");
        boolean appendParameters = GetterUtil.getBoolean(preferences.getValue(
                "appendParameters", "true"));
        String city = ParamUtil.getString(actionRequest, "city");
        String country = ParamUtil.getString(actionRequest, "country");
        String currency = ParamUtil.getString(actionRequest, "currency");
        String email = ParamUtil.getString(actionRequest, "email");
        String firstName = ParamUtil.getString(actionRequest, "first_name");
        boolean isMember = ParamUtil.getBoolean(actionRequest, "isMember");
        String lastName = ParamUtil.getString(actionRequest, "last_name");
        String memberNumber = ParamUtil
                .getString(actionRequest, "memberNumber");
        String orderId = ParamUtil.getString(actionRequest, "orderId");
        String postalCode = ParamUtil.getString(actionRequest, "postal_code");
        String redirect = ParamUtil.getString(actionRequest, "redirect");
        String successTarget = preferences.getValue("successTarget", "");

        if (isMember && Validator.isNull(memberNumber)) {
            actionResponse.setRenderParameter("address", address);
            actionResponse.setRenderParameter("address2", address2);
            actionResponse.setRenderParameter("city", city);
            actionResponse.setRenderParameter("country", country);
            actionResponse.setRenderParameter("email", email);
            actionResponse.setRenderParameter("first_name", firstName);
            actionResponse.setRenderParameter("isMember", String.valueOf(isMember));
            actionResponse.setRenderParameter("last_name", lastName);
            actionResponse.setRenderParameter("postal_code", postalCode);
            throw new MemberNumberException();
        }

        if (Validator.isNotNull(successTarget)) {

            if (Validator.isNotNull(redirect)) {
                successTarget = HttpUtil.addParameter(successTarget,
                        "redirect", redirect);
            }

            if (appendParameters) {
                if (Validator.isNotNull(address)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "address", address);
                }
                if (Validator.isNotNull(address)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "address2", address2);
                }
                if (Validator.isNotNull(amount)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "amount", amount);
                }
                if (Validator.isNotNull(city)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "city", city);
                }
                if (Validator.isNotNull(city)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "country", country);
                }
                if (Validator.isNotNull(currency)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "currency", currency);
                }
                if (Validator.isNotNull(city)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "email", email);
                }
                if (Validator.isNotNull(firstName)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "first_name", firstName);
                }
                if (Validator.isNotNull(lastName)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "last_name", lastName);
                }
                if (Validator.isNotNull(lastName)) {
                    successTarget = HttpUtil.addParameter(successTarget,
                            "postal_code", postalCode);
                }
                if (Validator.isNotNull(orderId)) {

                    orderId = orderId + StringPool.NEW_LINE + firstName
                            + StringPool.BLANK + lastName;

                    successTarget = HttpUtil.addParameter(successTarget,
                            "order_id", orderId);
                }
            }

            _log.info("successTarget = " + successTarget);

            actionResponse.sendRedirect(successTarget);
        }

    }

    /**
     * Disable the get- / sendRedirect feature of LiferayPortlet.
     */
    @Override
    protected String getRedirect(ActionRequest actionRequest,
            ActionResponse actionResponse) {

        return null;
    }

    private static Log _log = LogFactoryUtil
            .getLog(FormPortlet.class.getName());
}
