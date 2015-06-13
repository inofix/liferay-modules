<%--
    configuration.jsp: Configure the cdav-manager's preferences.
    
    Created:    2015-05-30 12:14 by Christian Berndt
    Modified:   2015-06-13 18:41 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="ch.inofix.portlet.cdav.util.CalendarResourceUtil"%>

<%@page import="com.liferay.calendar.model.CalendarResource"%>

<%@page import="zswi.protocols.caldav.ServerCalendar"%>
<%@page import="zswi.protocols.communication.core.HTTPSConnection"%>

<%
	long groupId = scopeGroupId;

	CalendarResource userResource = CalendarResourceUtil
			.getUserCalendarResource(renderRequest, user.getUserId());

	CalendarResource groupResource = CalendarResourceUtil
			.getGroupCalendarResource(renderRequest, groupId);

	List<Calendar> availableCalendars = new ArrayList<Calendar>();

	List<Calendar> userCalendars = CalendarServiceUtil
			.getCalendarResourceCalendars(groupId,
					userResource.getCalendarResourceId(), true);

	List<Calendar> groupCalendars = CalendarServiceUtil
			.getCalendarResourceCalendars(groupId,
					groupResource.getCalendarResourceId());

	availableCalendars.addAll(userCalendars);
	availableCalendars.addAll(groupCalendars);

	List<ServerCalendar> serverCalendars = new ArrayList<ServerCalendar>();

	try {
		// Do not create a keystore in the user's home directory
		// but use the truststore of the JRE installation. cdav-connect expects
		// it secured with the default keystore password "changeit".
		boolean installCert = false;

		HTTPSConnection conn = null;

		conn = new HTTPSConnection(servername, domain, username,
				password, 443, installCert);

		// Retrieve the calendars for the given connection parameters
		serverCalendars = conn.getCalendars();

		conn.shutdown();
	} catch (Exception e) {
		// TODO: log exception
		System.out.println(e);
	}

	boolean checkConnectionParameters = false;

	if (hasConnectionParameters && serverCalendars.size() == 0) {
		checkConnectionParameters = true;
	}
%>

<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
	var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm">

	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />
	<aui:input name="redirect" type="hidden"
		value="<%=configurationRenderURL%>" />

	<aui:fieldset label="target">

		<aui:select name="calendarId" inlineField="true" label="calendar"
			helpMessage="calendar-id-help" inlineLabel="true">
			<%
				for (Calendar availableCalendar : availableCalendars) {
					String availableCalendarId = String
							.valueOf(availableCalendar.getCalendarId());
			%>
			<aui:option value="<%=availableCalendarId%>"
				label="<%=availableCalendar.getName(locale)%>"
				selected="<%=availableCalendarId.equals(calendarId)%>" />
			<%
				}
			%>
		</aui:select>

		<aui:field-wrapper label="restore-from-trash"
			helpMessage="restore-from-trash-help" inlineField="true" inlineLabel="true">
			<aui:input name="restoreFromTrash" value="true"
				checked='<%=restoreFromTrash.equals("true")%>' label="yes"
				inlineLabel="true" inlineField="true" type="radio" />
			<aui:input name="restoreFromTrash" value="false"
				checked='<%=!restoreFromTrash.equals("true")%>' label="no"
				inlineLabel="true" inlineField="true" type="radio" />
		</aui:field-wrapper>

	</aui:fieldset>

	<aui:fieldset label="source">
	
       <c:if test="<%= checkConnectionParameters %>">
            <div class="alert alert-error">
                <liferay-ui:message key="please-check-your-connection-settings" />
            </div>
        </c:if>

		<aui:input name="servername" value="<%=servername%>" required="true"
			inlineField="true" helpMessage="servername-help" />

		<aui:input name="domain" value="<%=domain%>" required="true"
			inlineField="true" helpMessage="domain-help" />

		<aui:input name="username" value="<%=username%>" required="true"
			inlineField="true" helpMessage="username-help" />

		<aui:input name="password" value="<%=password%>" required="true"
			inlineField="true" helpMessage="password-help" type="password" />

		<c:if test="<%=serverCalendars.size() > 0%>">
			<aui:select name="calendar" value="<%=calendar%>" inlineField="true"
				helpMessage="calendar-help">

				<%
					for (ServerCalendar serverCalendar : serverCalendars) {
				%>
				<aui:option value="<%=serverCalendar.getDisplayName()%>"
					label="<%=serverCalendar.getDisplayName()%>"
					selected="<%=calendar.equals(serverCalendar
										.getDisplayName())%>" />
				<%
					}
				%>
			</aui:select>
		</c:if>

	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>

</aui:form>
