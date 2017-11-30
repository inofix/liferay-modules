<%--
    init.jsp: Common imports and setup code of the data manager.
    
    Created:    2017-03-09 20:00 by Christian Berndt
    Modified:   2017-12-01 00:10 by Christian Berndt
    Version:    1.3.7
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.Format"%>

<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="ch.inofix.portlet.data.FileFormatException"%>
<%@page import="ch.inofix.portlet.data.MeasurementXMLException"%>
<%@page import="ch.inofix.portlet.data.model.Measurement"%>
<%@page import="ch.inofix.portlet.data.service.MeasurementLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.data.util.DataManagerFields"%>

<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
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
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>
<%@page import="com.liferay.portal.service.UserServiceUtil"%>
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

<portlet:defineObjects />
<theme:defineObjects />

<%
    String id = ParamUtil.getString(request, "id");

    Calendar cal = Calendar.getInstance();

    String[] columns = portletPreferences.getValue("columns",
            "id,value,unit,timestamp").split(StringPool.COMMA);

    String dataURL = portletPreferences.getValue("dataURL", "");
    String[] dataURLs = null;

    if (dataURL.endsWith(StringPool.COMMA)) {
        dataURLs = (dataURL + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        dataURLs = dataURL.split(StringPool.COMMA);
    }

    String[] headerNames = portletPreferences.getValue("headerNames",
            "id,value,unit,createDate,modifiedDate").split(
            StringPool.COMMA);

    String idField = portletPreferences.getValue("idField", "id");
    String[] idFields = null;

    if (idField.endsWith(StringPool.COMMA)) {
        idFields = (idField + StringPool.SPACE).split(StringPool.COMMA);
    } else {
        idFields = idField.split(StringPool.COMMA);
    }

    String idFieldLabel = portletPreferences.getValue("idFieldLabel",
            "id");

    int limit = GetterUtil.getInteger(portletPreferences.getValue(
            "limit", "1000"));
    if (limit > 10000)
        limit = 10000; // maximum number of hits returned by indexer

    long interval = GetterUtil.getLong(
            portletPreferences.getValue("interval", "0"),
            1000 * 60 * 60 * 24); // 24 h

    Date now = new Date();
    long oneDay = 1000 * 60 * 60 * 24;
    long oneWeek = oneDay * 7;

    cal.setTimeInMillis(now.getTime() - oneWeek);

    int fromDay = ParamUtil.getInteger(request, "fromDay",
            cal.get(Calendar.DAY_OF_MONTH));
    int fromMonth = ParamUtil.getInteger(request, "fromMonth",
            cal.get(Calendar.MONTH));
    int fromYear = ParamUtil.getInteger(request, "fromYear",
            cal.get(Calendar.YEAR));

    long from = ParamUtil.getLong(request, "from", 0);

    if (from == 0) {
        from = PortalUtil.getDate(fromMonth, fromDay, fromYear)
                .getTime();
    } else {
        cal.setTimeInMillis(from);
        fromDay = cal.get(Calendar.DAY_OF_MONTH);
        fromMonth = cal.get(Calendar.MONTH);
        fromYear = cal.get(Calendar.YEAR);
    }

    String paginationType = portletPreferences.getValue(
            "paginationType", "regular");

    String nameField = portletPreferences.getValue("nameField", "");
    String[] nameFields = null;

    if (nameField.endsWith(StringPool.COMMA)) {
        nameFields = (nameField + StringPool.SPACE)
                .split(StringPool.COMMA);
    } else {
        nameFields = nameField.split(StringPool.COMMA);
    }

    String password = portletPreferences.getValue("password", "");
    String[] passwords = null;

    if (password.endsWith(StringPool.COMMA)) {
        passwords = (password + StringPool.SPACE)
                .split(StringPool.COMMA);
    } else {
        passwords = password.split(StringPool.COMMA);
    }

    String tabs1 = ParamUtil.getString(request, "tabs1", "latest");

    String timestampField = portletPreferences.getValue(
            "timestampField", "");
    String[] timestampFields = null;

    if (timestampField.endsWith(StringPool.COMMA)) {
        timestampFields = (timestampField + StringPool.SPACE)
                .split(StringPool.COMMA);
    } else {
        timestampFields = timestampField.split(StringPool.COMMA);
    }

    cal.setTime(new Date(now.getTime() + (oneDay - 1)));

    int untilDay = ParamUtil.getInteger(request, "untilDay",
            cal.get(Calendar.DAY_OF_MONTH));
    int untilMonth = ParamUtil.getInteger(request, "untilMonth",
            cal.get(Calendar.MONTH));
    int untilYear = ParamUtil.getInteger(request, "untilYear",
            cal.get(Calendar.YEAR));

    long until = ParamUtil.getLong(request, "until", 0);

    if (until == 0) {
        until = PortalUtil.getDate(untilMonth, untilDay, untilYear)
                .getTime();
    } else {
        cal.setTimeInMillis(until);
        untilDay = cal.get(Calendar.DAY_OF_MONTH);
        untilMonth = cal.get(Calendar.MONTH);
        untilYear = cal.get(Calendar.YEAR);
    }

    String userId = portletPreferences.getValue("userId", "0");
    long[] userIds = null;

    if (userId.endsWith(StringPool.COMMA)) {
        userIds = GetterUtil.getLongValues((userId + "0")
                .split(StringPool.COMMA));
    } else {
        userIds = GetterUtil.getLongValues(userId
                .split(StringPool.COMMA));
    }

    String userName = portletPreferences.getValue("userName", "");
    String[] userNames = null;

    if (userName.endsWith(StringPool.COMMA)) {
        userNames = (userName + StringPool.SPACE)
                .split(StringPool.COMMA);
    } else {
        userNames = userName.split(StringPool.COMMA);
    }

    // The list of availabe channels

    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    Facet idFacet = new MultiValueFacet(searchContext);
    idFacet.setFieldName("id");

    searchContext.addFacet(idFacet);

    // remove facet attributes from context, since we need the field's index here
    searchContext.setAttribute("id", null);
    searchContext.setAttribute("from", 0);
    searchContext.setAttribute("until", 0);

    Indexer indexer = IndexerRegistryUtil.getIndexer(Measurement.class);
    indexer.search(searchContext);

    FacetCollector idFacetCollector = idFacet.getFacetCollector();
    List<TermCollector> idTermCollectors = idFacetCollector
            .getTermCollectors();

    PropertyComparator termComparator = new PropertyComparator("term");
    Collections.sort(idTermCollectors, termComparator);
%>
