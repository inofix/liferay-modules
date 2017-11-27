<%--
    init.jsp: Common imports and setup code of the data manager.
    
    Created:    2017-03-09 20:00 by Christian Berndt
    Modified:   2017-11-20 15:45 by Christian Berndt
    Version:    1.3.0
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="ch.inofix.portlet.data.model.Measurement"%>
<%@page import="ch.inofix.portlet.data.service.MeasurementLocalServiceUtil"%>

<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.search.facet.collector.TermCollector"%>
<%@page import="com.liferay.portal.kernel.search.facet.collector.FacetCollector"%>
<%@page import="com.liferay.portal.kernel.search.facet.Facet"%>
<%@page import="com.liferay.portal.kernel.search.facet.MultiValueFacet"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.CamelCaseUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.liferay.util.PropertyComparator"%>

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
    String id = ParamUtil.getString(request, "id"); 
    
    String[] columns = portletPreferences.getValue("columns",
            "id,value,unit,timestamp").split(
            StringPool.COMMA);

    String dataURL = portletPreferences.getValue("dataURL", "");       
    String[] dataURLs = null; 
    
    if (dataURL.endsWith(StringPool.COMMA)) {
        dataURLs = (dataURL + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        dataURLs = dataURL.split(StringPool.COMMA);        
    }
     
    String idField = portletPreferences.getValue("idField", "id");
    String[] idFields = null; 
    
    if (idField.endsWith(StringPool.COMMA)) {
        idFields = (idField + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        idFields = idField.split(StringPool.COMMA);        
    }
    
    int limit = GetterUtil.getInteger(portletPreferences.getValue("limit", "1000"));
    if (limit > 10000) limit = 10000; // maximum number of hits returned by indexer
    
    long interval = GetterUtil.getLong(portletPreferences.getValue("interval", "0"), 1000 * 60 * 60 * 24); // 24 h
        
    Date now = new Date(); 
    long oneDay = 1000 * 60 * 60 * 24;  
    
    long from = ParamUtil.getLong(request, "from", now.getTime() - oneDay);
    
    String[] headerNames = portletPreferences.getValue("headerNames",
                    "id,value,unit,createDate,modifiedDate")
            .split(StringPool.COMMA);
        
    String paginationType = portletPreferences.getValue("paginationType", "regular");
    
    String nameField = portletPreferences.getValue("nameField", "");
    String[] nameFields = null; 
    
    if (nameField.endsWith(StringPool.COMMA)) {
        nameFields = (nameField + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        nameFields = nameField.split(StringPool.COMMA);        
    }
    
    String password = portletPreferences.getValue("password", "");
    String[] passwords = null; 
    
    if (password.endsWith(StringPool.COMMA)) {
        passwords = (password + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        passwords = password.split(StringPool.COMMA);        
    }
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "latest");
    
    String timestampField = portletPreferences.getValue("timestampField", "");
    String[] timestampFields = null; 
    
    if (timestampField.endsWith(StringPool.COMMA)) {
        timestampFields = (timestampField + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        timestampFields = timestampField.split(StringPool.COMMA);        
    }
    
    long until = ParamUtil.getLong(request, "until", now.getTime());
    
    String userId = portletPreferences.getValue("userId", "0");    
    long[] userIds = null; 
    
    if (userId.endsWith(StringPool.COMMA)) {
        userIds = GetterUtil.getLongValues((userId + "0").split(StringPool.COMMA));
    } else {
        userIds = GetterUtil.getLongValues(userId.split(StringPool.COMMA));        
    }
    
    String userName = portletPreferences.getValue("userName", "");
    String[] userNames = null; 
    
    if (userName.endsWith(StringPool.COMMA)) {
        userNames = (userName + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        userNames = userName.split(StringPool.COMMA);        
    }
    
    // The list of availabe channels
    
    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    Facet channelIdFacet = new MultiValueFacet(searchContext);
    channelIdFacet.setFieldName("id");

    searchContext.addFacet(channelIdFacet);
    
    // remove facet attributes from context, since we need the field's index here
    searchContext.setAttribute("id", null); 
    searchContext.setAttribute("from", 0);
    searchContext.setAttribute("until", 0);

    Indexer indexer = IndexerRegistryUtil.getIndexer(Measurement.class);
    indexer.search(searchContext);

    FacetCollector channelIdFacetCollector = channelIdFacet
            .getFacetCollector();
    List<TermCollector> channelIdTermCollectors = channelIdFacetCollector
            .getTermCollectors();
    
    PropertyComparator termComparator = new PropertyComparator("term");
    Collections.sort(channelIdTermCollectors, termComparator);
%>
