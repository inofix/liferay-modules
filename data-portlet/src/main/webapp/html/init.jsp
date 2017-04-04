<%--
    init.jsp: Common imports and setup code of the data manager.
    
    Created:    2017-03-09 20:00 by Christian Berndt
    Modified:   2017-04-04 11:30 by Christian Berndt
    Version:    1.2.2
--%>

<%@page import="java.util.Calendar"%>
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
    
    int frequency = GetterUtil.getInteger(portletPreferences.getValue("frequency", "15"));
        
    Date now = new Date(); 
    Calendar cal = Calendar.getInstance();
    cal.setTime(now); 
    
    int nowDay = cal.get(Calendar.DAY_OF_MONTH); 
    int nowMonth = cal.get(Calendar.MONTH); 
    int nowYear = cal.get(Calendar.YEAR); 
    
    long oneDay = 1000 * 60 * 60 * 24; 
    
    Date yesterday = new Date(now.getTime() - oneDay); 
    cal.setTime(yesterday); 
    int yesterdayDay = cal.get(Calendar.DAY_OF_MONTH); 
    int yesterdayMonth = cal.get(Calendar.MONTH); 
    int yesterdayYear = cal.get(Calendar.YEAR); 
    
    long from = 0;
    int fromDateDay = ParamUtil.getInteger(request, "fromDateDay", yesterdayDay);  
    int fromDateMonth = ParamUtil.getInteger(request, "fromDateMonth", yesterdayMonth); 
    int fromDateYear = ParamUtil.getInteger(request, "fromDateYear", yesterdayYear);
    Date fromDate = PortalUtil.getDate(fromDateMonth, fromDateDay, fromDateYear);
    
    if (fromDate != null) {
        from = fromDate.getTime();
    } else {
        fromDate = yesterday;
        from = fromDate.getTime();
    }

    String[] headerNames = portletPreferences.getValue("headerNames",
                    "channelId,channelName,value,channelUnit,createDate,modifiedDate")
            .split(StringPool.COMMA);
    
    int interval = GetterUtil.getInteger(portletPreferences.getValue("interval", "1440"));
    
    String paginationType = portletPreferences.getValue("paginationType", "regular");
    
    String password = portletPreferences.getValue("password", "");
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "chart");
    
    long until = 0;
    int untilDateDay = ParamUtil.getInteger(request, "untilDateDay", nowDay);  
    int untilDateMonth = ParamUtil.getInteger(request, "untilDateMonth", nowMonth); 
    int untilDateYear = ParamUtil.getInteger(request, "untilDateYear", nowYear); 
    Date untilDate = PortalUtil.getDate(untilDateMonth, untilDateDay, untilDateYear);
    
    if (untilDate != null) {
        until = untilDate.getTime();
    } else {
        untilDate = now; 
        until = now.getTime();
    }
    
    long userId = GetterUtil.getLong(portletPreferences.getValue("userId", "0"));
    
    String userName = portletPreferences.getValue("userName", "");

%>
