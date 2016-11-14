<%--
    edit_task_record.jsp: edit a single task-record.

    Created:    2013-10-07 10:41 by Christian Berndt
    Modified:   2016-11-14 00:28 by Christian Berndt
    Version:    1.5.0
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

long taskRecordId = ParamUtil.getLong(request, "taskRecordId");

TaskRecord taskRecord = null;

if (taskRecordId > 0) {
    taskRecord = taskRecordLocalService.getTaskRecord(taskRecordId);
}
%>

<portlet:actionURL name="editTaskRecord" var="editTaskRecordURL">
</portlet:actionURL>

<aui:form action="<%= editTaskRecordURL %>" method="post" name="fm">
<%--     <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= taskRecord == null ? Constants.ADD : Constants.UPDATE %>" /> --%>
    <aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
    <aui:input name="taskRecordId" type="hidden" value="<%= taskRecordId %>" />

    <liferay-ui:header
        backURL="<%= redirect %>"
        title='<%= (taskRecord != null) ? String.valueOf(taskRecord.getTaskRecordId()) : "new-task-record" %>'
    />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />

    <aui:model-context bean="<%= taskRecord %>" model="<%= TaskRecord.class %>" />

    <aui:fieldset>
        <aui:input name="workPackage" />
        <aui:input name="description" />
        <aui:input name="duration" />
    </aui:fieldset>

    <aui:button-row>
        <aui:button type="submit" />

        <aui:button href="<%= redirect %>" type="cancel" />
    </aui:button-row>
</aui:form>
