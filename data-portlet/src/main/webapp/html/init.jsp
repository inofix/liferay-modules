<%--
    init.jsp: Common imports and setup code of the data manager.
    
    Created:    2017-03-09 20:00 by Christian Berndt
    Modified:   2017-04-03 12:42 by Christian Berndt
    Version:    1.2.0
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="ch.inofix.portlet.data.model.Measurement"%>
<%@page import="ch.inofix.portlet.data.service.MeasurementLocalServiceUtil"%>

<%@page import="com.liferay.portal.kernel.search.facet.MultiValueFacet"%>
<%@page import="com.liferay.portal.kernel.search.facet.Facet"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <%@ taglib uri="/inofix-util" prefix="ifx-util" %> --%>
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
    String[] columns = portletPreferences.getValue("columns",
            "channelName,value,channelUnit,timestamp").split(
            StringPool.COMMA);

    String dataURL = portletPreferences.getValue("dataURL", "");
    
    int frequency = ParamUtil.getInteger(request, "frequency", 15);
    
    int fromDateDay = ParamUtil.getInteger(request, "fromDateDay");  
    int fromDateMonth = ParamUtil.getInteger(request, "fromDateMonth"); 
    int fromDateYear = ParamUtil.getInteger(request, "fromDateYear");
    Date fromDate = PortalUtil.getDate(fromDateMonth, fromDateDay, fromDateYear);

    String[] headerNames = portletPreferences.getValue("headerNames",
                    "channelId,channelName,value,channelUnit,createDate,modifiedDate")
            .split(StringPool.COMMA);
    
    int interval = ParamUtil.getInteger(request, "interval", 24 * 60);
    
    String paginationType = portletPreferences.getValue("paginationType", "regular");
    
    String password = portletPreferences.getValue("password", "");
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    int untilDateDay = ParamUtil.getInteger(request, "untilDateDay");  
    int untilDateMonth = ParamUtil.getInteger(request, "untilDateMonth"); 
    int untilDateYear = ParamUtil.getInteger(request, "untilDateYear"); 
    Date untilDate = PortalUtil.getDate(untilDateMonth, untilDateDay, untilDateYear);
    
    long userId = GetterUtil.getLong(portletPreferences.getValue("userId", "0"));
    
    String userName = portletPreferences.getValue("userName", "");

%>
