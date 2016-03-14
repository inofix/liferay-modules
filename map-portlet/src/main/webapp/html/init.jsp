<%--
    init.jsp: Common imports and setup code of the contact-manager
    
    Created:    2016-03-01 17:58 by Christian Berndt
    Modified:   2016-03-14 21:14 by Christian Berndt
    Version:    1.0.3
--%>

<%-- Import required classes --%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%

    String currentURL = PortalUtil.getCurrentURL(request);

    // Remove any actionParameters from the currentURL
    currentURL = HttpUtil.removeParameter(currentURL,
            renderResponse.getNamespace() + "javax.portlet.action");

    String claim = portletPreferences.getValue("claim", "");
    String mapCenter = portletPreferences.getValue("mapCenter", "[47.05207, 8.30585]");
    String mapHeight = portletPreferences.getValue("mapHeight", "400px");
    String mapZoom = portletPreferences.getValue("mapZoom", "10");
    String[] markerLabels = portletPreferences.getValues("markerLabels", new String[] {""});
    String[] markerLatLongs = portletPreferences.getValues("markerLatLongs", new String[] {""});
    String tilesCopyright = portletPreferences.getValue("tilesCopyright", "&copy; <a href=\"http://osm.org/copyright\" target=\"_blank\">OpenStreetMap</a> contributors");
    String tilesURL = portletPreferences.getValue("tilesURL", "http://{s}.tile.osm.org/{z}/{x}/{y}.png");

%>
