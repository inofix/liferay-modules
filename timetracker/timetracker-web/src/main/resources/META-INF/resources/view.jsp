<%--
    view.jsp: Default view of Inofix' timetracker.
    
    Created:     2013-10-06 16:52 by Christian Berndt
    Modified:    2016-11-12 19:23 by Christian Berndt
    Version:     1.5.0
 --%>

<%@ include file="/init.jsp" %>

<%-- <liferay-ui:header title="timetracker" showBackURL="<%= false %>" /> --%>

<aui:button-row>
    <portlet:renderURL var="editTaskRecordURL">
        <portlet:param name="mvcPath" value="/edit_task_record.jsp" />
        <portlet:param name="redirect" value="<%= currentURL %>" />
    </portlet:renderURL>

    <aui:button href="<%= editTaskRecordURL %>" value="add-task-record" />
</aui:button-row>

<liferay-ui:search-container
    total="<%=taskRecordLocalService.getTaskRecordsCount()%>">


    <liferay-ui:search-container-results
        results="<%= taskRecordLocalService.getTaskRecords(searchContainer.getStart(), searchContainer.getEnd()) %>" />

    <liferay-ui:search-container-row
        className="ch.inofix.timetracker.model.TaskRecord"
        escapedModel="true" modelVar="taskRecord">

        <liferay-ui:search-container-column-text name="id"
            property="taskRecordId" valign="top" />

    </liferay-ui:search-container-row>

</liferay-ui:search-container>
