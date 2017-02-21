<%--
    init.jsp: Common imports and initialization code.

    Created:     2017-02-03 14:00 by Christian Berndt
    Modified:    2017-02-21 19:14 by Christian Berndt
    Version:     1.0.6
--%>

<%-- Required classes --%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="ch.inofix.portlet.payment.util.PaymentConstants"%>

<%-- Required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme"%>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    HttpServletRequest originalRequest = PortalUtil
            .getOriginalServletRequest(request);

    // common_parameters from request
    String orderId = ParamUtil.getString(request, "order_id", ParamUtil.getString(originalRequest, "order_id", ""));
    String merchantReference = ParamUtil.getString(request, "merchant_reference", ParamUtil.getString(originalRequest, "merchant_reference", ""));
    String address = ParamUtil.getString(request, "address", ParamUtil.getString(originalRequest, "address", ""));
    String address2 = ParamUtil.getString(request, "address2", ParamUtil.getString(originalRequest, "address2", ""));
    String amount = ParamUtil.getString(request, "amount", ParamUtil.getString(originalRequest, "amount", ""));
    String apiKey = portletPreferences.getValue("apiKey", "");
    String city = ParamUtil.getString(request, "city", ParamUtil.getString(originalRequest, "city", ""));
    String country = ParamUtil.getString(request, "country", ParamUtil.getString(originalRequest, "country", ""));
    String currency = ParamUtil.getString(request, "currency", ParamUtil.getString(originalRequest, "currency", ""));
    if (Validator.isNull(currency)) {
        currency = portletPreferences.getValue("currency", "EUR");
    }

    String[] countryCodes = Locale.getISOCountries();
    SortedMap<String, Locale> countryMap = new TreeMap<String, Locale>();
    for (String countryCode : countryCodes) {
        Locale countryLocale = new Locale("", countryCode);
        countryMap.put(countryLocale.getDisplayCountry(locale),
                countryLocale);
    }
    Set<String> countrySet = countryMap.keySet();
    Iterator<String> countryIterator = countrySet.iterator();

    String defaultCountry = portletPreferences.getValue(
            "defaultCountry", "DE");
    if (Validator.isNotNull(country)) {
        defaultCountry = country; 
    }
    String email = ParamUtil.getString(request, "email", ParamUtil.getString(originalRequest, "email", ""));    
    String firstName = ParamUtil.getString(request, "first_name", ParamUtil.getString(originalRequest, "first_name", ""));
    String lastName = ParamUtil.getString(request, "last_name", ParamUtil.getString(originalRequest, "last_name", ""));

    String merchantName = portletPreferences.getValue("merchantName","");

    if (Validator.isNull(merchantReference)) {
        merchantReference = merchantName + StringPool.NEW_LINE + orderId;
    }
    String postalCode = ParamUtil.getString(request, "postal_code", ParamUtil.getString(originalRequest, "postal_code", ""));

    String[] services = portletPreferences.getValues("services",PaymentConstants.SERVICE_KEYS);

    String shippingCosts = portletPreferences.getValue("shippingCosts", "");

    boolean showDuration = GetterUtil.getBoolean(portletPreferences
            .getValue("showDuration", "false"));
    boolean showLocale = GetterUtil.getBoolean(portletPreferences
            .getValue("showLocale", "false"));
    boolean showOriginalTransactionId = GetterUtil
            .getBoolean(portletPreferences.getValue(
                    "showOriginalTransactionId", "false"));
    boolean showRecurring = GetterUtil.getBoolean(portletPreferences
            .getValue("showRecurring", "false"));
    boolean showShippingCosts = GetterUtil
            .getBoolean(portletPreferences.getValue(
                    "showShippingCosts", "false"));
    
    String tabOrientation = portletPreferences.getValue("tabOrientation", "default");

    String vat = portletPreferences.getValue("vat", "19%");
%>
