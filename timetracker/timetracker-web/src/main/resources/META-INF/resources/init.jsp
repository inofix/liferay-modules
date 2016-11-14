<%--
    init.jsp: Common imports and initialization code.

    Created:     2014-02-01 15:31 by Christian Berndt
    Modified:    2016-11-13 23:08 by Christian Berndt
    Version:     1.0.2
--%>

<%@page import="ch.inofix.timetracker.model.TaskRecord"%>
<%@page import="ch.inofix.timetracker.service.TaskRecordService"%>
<%@page import="ch.inofix.timetracker.service.TaskRecordLocalService"%>

<%@ page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ page import="com.liferay.portal.kernel.util.StringPool"%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@ page import="com.liferay.taglib.search.ResultRow"%>

<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List"%>

<%@ page import="javax.portlet.PortletPreferences"%>
<%@ page import="javax.portlet.PortletURL"%>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    PortletURL portletURL = renderResponse.createRenderURL();
    String currentURL = portletURL.toString();
    // TODO: remove local service
    TaskRecordLocalService taskRecordLocalService = (TaskRecordLocalService) request
            .getAttribute("taskRecordLocaleService");
    TaskRecordService taskRecordService = (TaskRecordService) request.getAttribute("taskRecordService");
%>

<liferay-ui:search-container
    total="<%=taskRecordLocalService.getTaskRecordsCount()%>">


    <liferay-ui:search-container-results
        results="<%= taskRecordLocalService.getTaskRecords(searchContainer.getStart(), searchContainer.getEnd()) %>" />

    <liferay-ui:search-container-row
        className="ch.inofix.timetracker.model.TaskRecord"
        escapedModel="true" modelVar="taskRecord">

        <liferay-ui:search-container-column-text name="id"
            property="taskRecordId" valign="top" />
            
        <liferay-ui:search-container-column-text name="workPackage"
            property="workPackage" valign="top" />            

    </liferay-ui:search-container-row>
    
    <liferay-ui:search-iterator/>

</liferay-ui:search-container>
