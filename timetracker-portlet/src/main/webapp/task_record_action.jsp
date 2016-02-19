<%--
    task_record_action.jsp: a menu with actions
    available for a task record.

    Created:     2013-10-12 15:23 by Christian Berndt
    Modified:    2014-02-01 15:49 by Christian Berndt
    Version:    1.1
--%>

<%@ include file="/init.jsp" %>

<%-- Include required classes. --%>
<%-- <%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%> --%>
<%-- <%@page import="com.liferay.portal.util.PortalUtil"%> --%>
<%-- <%@page import="com.liferay.portal.kernel.util.WebKeys"%> --%>

<%-- <%@page import="ch.inofix.portlet.timetracker.model.TaskRecord"%> --%>
<%-- <%@page import="ch.inofix.portlet.timetracker.util.TaskRecordFields"%> --%>
<%-- <%@page import="ch.inofix.portlet.timetracker.util.TimetrackerPortletKeys"%> --%>

<%-- Include required taglibs. --%>
<%-- <%@ taglib prefix="liferay-theme" uri="http://liferay.com/tld/theme"%> --%>
<%-- <%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui"%> --%>
<%-- <%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%> --%>

<%-- Enable the JSR 286 portlet variables          --%>
<%-- e.g. renderRequest, portletConfig etc.        --%>
<%-- For the full list of available variables see  --%>
<%-- the JSR 286 specification.                    --%>
<%-- <portlet:defineObjects /> --%>

<%-- Enable the liferay-theme variables, e.g.          --%>
<%-- locale, permissionChecker, scopeGroupId, required --%>
<%-- by the permissionChecker (see below).             --%>
<%-- <liferay-theme:defineObjects /> --%>

<%
    ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
    TaskRecord taskRecord = (TaskRecord)row.getObject();

    // Retrieve the portlet's currentURL.
    String currentURL = PortalUtil.getCurrentURL(renderRequest);

    String taskRecordId = String.valueOf(taskRecord.getTaskRecordId());
%>

<%-- Compose the deleteTaskRecordURL --%>
<portlet:actionURL var="deleteTaskRecordURL" name="deleteTaskRecord">
    <portlet:param  name="<%= TaskRecordFields.TASK_RECORD_ID %>"
        value="<%= taskRecordId %>"/>
    <portlet:param name="<%= TimetrackerPortletKeys.MVC_PATH %>"
        value="/view.jsp"/>
</portlet:actionURL>

<%-- Compose the editTaskRecordURL --%>
<portlet:actionURL var="editTaskRecordURL" name="viewTaskRecord">
    <portlet:param name="<%= TimetrackerPortletKeys.MVC_PATH %>"
        value="/edit.jsp"/>
    <portlet:param name="<%= TimetrackerPortletKeys.REDIRECT_URL %>"
        value="<%= currentURL %>"/>
    <portlet:param  name="<%= TaskRecordFields.TASK_RECORD_ID %>"
        value="<%= taskRecordId %>"/>
</portlet:actionURL>

<%-- The actual menu --%>
<liferay-ui:icon-menu>

    <liferay-ui:icon url="<%= editTaskRecordURL %>" image="edit"/>
    <liferay-ui:icon-delete url="<%= deleteTaskRecordURL %>" />

</liferay-ui:icon-menu>
