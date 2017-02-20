<%--
    init.jsp: Common imports and initialization code.

    Created:     2017-02-20 14:45 by Christian Berndt
    Modified:    2017-02-20 14:45 by Christian Berndt
    Version:     1.0.0
--%>

<%-- Required classes --%>

<%@page import="java.util.Set"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.SortedMap"%>
<%-- <%@page import="com.liferay.portal.kernel.util.GetterUtil"%> --%>
<%-- <%@page import="java.util.ArrayList"%> --%>
<%-- <%@page import="java.util.Arrays"%> --%>
<%@page import="java.util.Iterator"%>
<%-- <%@page import="java.util.List"%> --%>
<%@page import="java.util.Locale"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="ch.inofix.portlet.form.MemberNumberException"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%-- <%@page import="com.liferay.portal.kernel.util.KeyValuePair"%> --%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%-- <%@page import="com.liferay.portal.kernel.util.StringPool"%> --%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%-- Required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%-- <%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%> --%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme"%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    String currency = portletPreferences.getValue("currency", "EUR");
    String description = portletPreferences.getValue("description", "");
    String formTitle = portletPreferences.getValue("formTitle", "Form Portlet (edit form title in configuration)");
    String orderId = portletPreferences.getValue("orderId", "");
    String submitLabel = portletPreferences.getValue("submitLabel", "save");
    String successTarget = portletPreferences.getValue("successTarget", "");
    
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
%>
