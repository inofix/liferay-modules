<%--
    configuration.jsp: Configure the cdav-manager's preferences.
    
    Created:    2015-05-30 12:14 by Christian Berndt
    Modified:   2015-05-31 23:25 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="ch.inofix.portlet.cdav.util.CalendarResourceUtil"%>

<%@page import="com.liferay.calendar.model.CalendarResource"%>
<%@page import="com.liferay.calendar.service.CalendarServiceUtil"%>
<%@page import="com.liferay.calendar.model.Calendar"%>

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

	// Do not create a keystore in the user's home directory
	// but use the truststore of the JRE installation. cdav-connect expects
	// it secured with the default keystore password "changeit".
	boolean installCert = false;

	HTTPSConnection conn = null;

	conn = new HTTPSConnection(servername, domain, username, password,
			443, installCert);

	// Retrieve the calendars for the given connection parameters
	List<ServerCalendar> serverCalendars = conn.getCalendars();

	conn.shutdown();
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

	<aui:fieldset label="connection-settings">

		<aui:select name="calendarId" inlineField="true"
			helpMessage="calendar-id-help">
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

		<aui:input name="servername" value="<%=servername%>"
			inlineField="true" helpMessage="servername-help" />

		<aui:input name="domain" value="<%=domain%>" inlineField="true"
			helpMessage="domain-help" />

		<aui:input name="username" value="<%=username%>" inlineField="true"
			helpMessage="username-help" />

		<aui:input name="password" value="<%=password%>" inlineField="true"
			helpMessage="password-help" type="password" />

		<aui:select name="calendar" value="<%=calendar%>" inlineField="true"
			helpMessage="calendar-help">

			<%
				for (ServerCalendar serverCalendar : serverCalendars) {
			%>
			<aui:option value="<%=serverCalendar.getDisplayName()%>"
				label="<%=serverCalendar.getDisplayName()%>"
				selected="<%= calendar.equals(serverCalendar.getDisplayName()) %>" />
			<%
				}
			%>

		</aui:select>

	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>

</aui:form>
