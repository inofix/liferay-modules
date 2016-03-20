<%--
    edit_taks_record.jsp: edit a single task-record.

    Created:     2013-10-07 10:41 by Christian Berndt
    Modified:    2016-03-20 18:05 by Christian Berndt
    Version:     1.0.3

--%>

<%@ include file="/html/init.jsp" %>

<%
    // Retrieve the display settings.
    PortletPreferences preferences = renderRequest.getPreferences();

    String portletResource = ParamUtil.getString(request, "portletResource");

    if (Validator.isNotNull(portletResource)) {

        preferences = PortletPreferencesFactoryUtil.getPortletSetup(
        request, portletResource);
    }

    String timeFormat = PrefsParamUtil.getString(preferences,
    renderRequest, TimetrackerPortletKeys.TIME_FORMAT,
    TimeFormat.FROM_UNTIL);

    // Retrieve the "redirectURL" parameter from the request.
    String redirectURL = ParamUtil.getString(request, "redirectURL");

    // Retrieve the task record from the request
    // (Stored there by the MVC Controller portlet).
    TaskRecord taskRecord = (TaskRecord) request
            .getAttribute(TimetrackerPortletKeys.TASK_RECORD);

    String durationInMinutes = null;

    String namespace = portletDisplay.getNamespace();

    int startDateHour = -1;
    int startDateMinute = -1;
    String startDatePrefix = namespace + "startDate";

    int endDateHour = -1;
    int endDateMinute = -1;
    String endDatePrefix = namespace + "endDate";

    if (taskRecord != null) {

        durationInMinutes = String.valueOf(taskRecord
                .getDurationInMinutes());

        Date startDate = taskRecord.getStartDate();
        Date endDate = taskRecord.getEndDate();

        if (startDate != null) {
            startDateHour = startDate.getHours();
            startDateMinute = startDate.getMinutes();
        }

        if (endDate != null) {
            endDateHour = endDate.getHours();
            endDateMinute = endDate.getMinutes();
        }

    }
%>


<%-- Compose the saveTaskRecordURL --%>
<portlet:actionURL var="saveTaskRecordURL" name="addOrUpdateTaskRecord">
    <portlet:param name="mvcPath" value="/view.jsp"/>
</portlet:actionURL>

<div class="timetracker-portlet">

    <liferay-ui:header title="timetracker-portlet-header"
                       backURL="<%= redirectURL %>"
                       showBackURL="<%= true %>"/>

    <aui:form method="post" action="<%= saveTaskRecordURL %>" name="fm">

        <aui:input name="<%= CommonFields.USER_ID %>"
            value="<%= String.valueOf(themeDisplay.getUserId()) %>" type="hidden"/>

        <aui:fieldset>

            <%-- The model for this fieldset. --%>
            <aui:model-context bean="<%=taskRecord%>"
                model="<%=TaskRecord.class%>" />

            <aui:input name="<%= TaskRecordFields.END_DATE %>" type="hidden"/>
            <aui:input name="<%= TaskRecordFields.TASK_RECORD_ID %>" type="hidden"/>

            <aui:input name="<%= TaskRecordFields.WORK_PACKAGE %>"
                       helpMessage="work-package-help"
                       cssClass="timetracker-input"/>
            <aui:input name="<%= TaskRecordFields.TICKET_URL %>"
                       helpMessage="ticket-u-r-l-help"
                       cssClass="timetracker-input"/>
            <aui:input name="<%= TaskRecordFields.DESCRIPTION %>"/>
            <aui:input name="<%= TaskRecordFields.START_DATE %>"
                label="date"/>

            <c:if test="<%= Validator.equals(TimeFormat.FROM_UNTIL, timeFormat) %>">
                <aui:field-wrapper name="from-until">
                    <iu:time-picker
                        deltaMinutes="<%= 15 %>"
                        firstHour="<%= 0 %>"
                        hour="<%= startDateHour %>"
                        minute="<%= startDateMinute %>"
                        nullable="<%= false %>"
                        prefix="<%= startDatePrefix %>"/>
                    --
                    <iu:time-picker
                        deltaMinutes="<%= 15 %>"
                        firstHour="<%= 0 %>"
                        hour="<%= endDateHour %>"
                        minute="<%= endDateMinute %>"
                        nullable="<%= false %>"
                        prefix="<%=  endDatePrefix %>"/>

                </aui:field-wrapper>
            </c:if>
            <c:if test="<%= !Validator.equals(TimeFormat.FROM_UNTIL, timeFormat) %>">
                <aui:field-wrapper label="<%= TaskRecordFields.DURATION %>"
                                   helpMessage="duration-help">
                    <input name="<portlet:namespace/>duration"
                           value="<%= durationInMinutes %>"
                           class="aui-field-input aui-field-input-text lfr-input-text duration-in-minutes"/>
                </aui:field-wrapper>
            </c:if>
        </aui:fieldset>

        <aui:button-row>
            <aui:button type="submit"/>
        </aui:button-row>

    </aui:form>
</div>

<iu:revision-display />
