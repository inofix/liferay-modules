<%--
    init.jsp: Common imports and setup code of the map-portlet
    
    Created:    2016-03-01 17:58 by Christian Berndt
    Modified:   2016-08-08 16:50 by Christian Berndt
    Version:    1.1.6
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

    String addressResolverURL = portletPreferences.getValue("addressResolverURL", "//nominatim.openstreetmap.org/?format=json&addressdetails=1&format=json&limit=1&q=");
    String claim = portletPreferences.getValue("claim", "");
    String customAddressAndLabel = portletPreferences.getValue("customAddressAndLabel", "");
    String customLatLon = portletPreferences.getValue("customLatLon", "");
    String dataTableColumnDefs = portletPreferences.getValue("dataTableColumnDefs", "");
    String dataTableColumns = portletPreferences.getValue("dataTableColumns", "");
    String dataTablePaging = portletPreferences.getValue("dataTablePaging", "false");
    String[] filterColumns = portletPreferences.getValues("filterColumns", new String[] {""});
    String[] filterDataURLs = portletPreferences.getValues("filterDataURLs", new String[] {"/map-portlet/data/countries.json"});
    String[] filterPlaceholders = portletPreferences.getValues("filterPlaceholders", new String[] {"search"});
    String[] labelValueMappings = portletPreferences.getValues("labelValueMappings", new String[] {""});
    String locationsURL = portletPreferences.getValue("locationsURL", "/map-portlet/data/cities.json");
    String mapCenter = portletPreferences.getValue("mapCenter", "[47.05207, 8.30585]");
    String mapHeight = portletPreferences.getValue("mapHeight", "400px");
    String mapZoom = portletPreferences.getValue("mapZoom", "13");
    String markerIconConfig = portletPreferences.getValue("markerIconConfig", "");
    String[] markerLabels = portletPreferences.getValues("markerLabels", new String[] {""});
    String[] markerLocations = portletPreferences.getValues("markerLocations", new String[] {""});
    boolean showTable = GetterUtil.getBoolean(portletPreferences.getValue("showTable", "false"));
    String tilesCopyright = portletPreferences.getValue("tilesCopyright", "&copy; <a href=\"http://osm.org/copyright\" target=\"_blank\">OpenStreetMap</a> contributors");
    String tilesURL = portletPreferences.getValue("tilesURL", "http://{s}.tile.osm.org/{z}/{x}/{y}.png");
    boolean useAddressResolver = GetterUtil.getBoolean(portletPreferences.getValue("useAddressResolver", "false"));
    boolean useDivIcon = GetterUtil.getBoolean(portletPreferences.getValue("useDivIcon", "false"));
    boolean useGlobalJQuery = GetterUtil.getBoolean(portletPreferences.getValue("useGlobalJQuery", "false"));

%>
