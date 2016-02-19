<%--
    export.jsp: select the export parameter.

    Created:     2014-02-01 21:51 by Christian Berndt
    Modified:     2014-05-23 23:08 by Michael Lustenberger
    Version:     1.2
--%>

<%@ include file="/init.jsp" %>

<%
    // Retrieve the "redirectURL" parameter from the request.
    String redirectURL = ParamUtil.getString(request, "redirectURL");
%>

<div class="timetracker-portlet">
    <liferay-ui:header title="timetracker-portlet-header"
                       backURL="<%= redirectURL %>"
                       showBackURL="<%= true %>"/>

    <!-- Compose the exportTaskRecordsURL -->
    <portlet:resourceURL var="exportTaskRecordsURL" id="exportTaskRecords"/>

    <aui:form action="<%= exportTaskRecordsURL %>">
        <aui:fieldset>
            <aui:input name="format" type="radio" value="csv" label="List (CSV)" inlineField="true" checked="true"/>
            <aui:input name="format" type="radio" value="xml" label="List (XML)" inlineField="true"/>
            <aui:input name="format" type="radio" value="latex" label="List (LaTeX)" inlineField="true"/>
            <aui:input name="format" type="radio" value="fulllatex" label="Report (LaTeX)" inlineField="true"/>
        </aui:fieldset>

<%-- TODO hide this if not in fulllatex mode.. --%>
        <liferay-ui:panel-container id='<portlet:namespace />fullLaTeXConfiguration' persistState="false" extended="true">
            <liferay-ui:panel title="Report (LaTeX) specific stuff" collapsible="true">
                <aui:input type="text" name="carryOver" />
            </liferay-ui:panel>
        </liferay-ui:panel-container>

        <aui:input type="hidden" name="<%=CommonFields.COMPANY_ID%>" />
        <aui:input type="hidden" name="<%=CommonFields.GROUP_ID%>" />
        <aui:input type="hidden" name="<%=TaskRecordFields.USER_ID%>" />
        <aui:input type="hidden" name="<%=TaskRecordFields.WORK_PACKAGE%>" />
        <aui:input type="hidden" name="<%=TaskRecordFields.DESCRIPTION%>" />
        <aui:input type="hidden" name="startDateDay"/>
        <aui:input type="hidden" name="startDateMonth"/>
        <aui:input type="hidden" name="startDateYear"/>
        <aui:input type="hidden" name="endDateDay"/>
        <aui:input type="hidden" name="endDateMonth"/>
        <aui:input type="hidden" name="endDateYear"/>
        <aui:input type="hidden" name="<%= TimetrackerPortletKeys.ORDER_BY_COL %>"/>
        <aui:input type="hidden" name="<%= TimetrackerPortletKeys.ORDER_BY_TYPE %>"/>
        <aui:button-row>
            <aui:button type="submit" value="download-current-selection"/>
        </aui:button-row>
    </aui:form>
</div>

