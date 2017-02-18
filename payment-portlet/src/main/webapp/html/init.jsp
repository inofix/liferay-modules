<%--
    init.jsp: Common imports and initialization code.

    Created:     2017-02-03 14:00 by Christian Berndt
    Modified:    2017-02-18 13:49 by Christian Berndt
    Version:     1.0.3
--%>

<%-- Required classes --%>

<%@page import="java.util.Set"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.SortedMap"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>

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
    String apiKey = portletPreferences.getValue("apiKey", "");

    String[] countryCodes = Locale.getISOCountries();
    SortedMap<String, Locale> countryMap = new TreeMap<String, Locale>();
    for (String countryCode : countryCodes) {
        Locale countryLocale = new Locale("", countryCode);
        countryMap.put(countryLocale.getDisplayCountry(locale),
                countryLocale);
    }
    Set<String> countrySet = countryMap.keySet();
    Iterator<String> countryIterator = countrySet.iterator(); 

    String currency = portletPreferences.getValue("currency", "EUR");
    String defaultCountry = portletPreferences.getValue("defaultCountry", "DE");

    String merchantName = portletPreferences.getValue("merchantName",
            "");

    String[] services = portletPreferences.getValues("services",
            PaymentConstants.SERVICE_KEYS);

    String shippingCosts = portletPreferences.getValue("shippingCosts",
            "");

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

    String vat = portletPreferences.getValue("vat", "19%");
%>
