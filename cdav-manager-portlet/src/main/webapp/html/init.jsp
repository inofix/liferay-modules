<%--
    init.jsp: Common imports and setup code of the cdav-manager portlet.
    
    Created:    2015-05-30 12:19 by Christian Berndt
    Modified:   2015-07-31 16:22 by Christian Berndt
    Version:    1.0.7
--%>

<%-- Import required classes --%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>


<%@page import="com.liferay.calendar.service.CalendarServiceUtil"%>
<%@page import="com.liferay.calendar.model.Calendar"%>

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>


<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects/>

<%
	String[] calendars = portletPreferences.getValues("calendar",
			new String[] {""});
	String[] calendarIds = portletPreferences.getValues("calendarId",
			new String[] {""});
	String[] domains = portletPreferences.getValues("domain",
			new String[]{""});
	// TODO: Retrieve the lastSync from the portlet / application scope
	Date lastSync = new Date();
	String[] passwords = portletPreferences.getValues("password",
			new String[]{""});
	String[] restoreFromTrash = portletPreferences.getValues(
			"restoreFromTrash", new String[] {"false"});
	String[] servernames = portletPreferences.getValues("servername",
			new String[]{""});
	String[] usernames = portletPreferences.getValues("username",
			new String[]{""});

	long[] targetCalendarIds = GetterUtil.getLongValues(calendarIds);

	List<Calendar> targetCalendars = new ArrayList<Calendar>();

	for (long targetCalendarId : targetCalendarIds) {

		if (targetCalendarId > 0) {
			
			Calendar targetCalendar = CalendarServiceUtil
					.getCalendar(targetCalendarId);
			
			targetCalendars.add(targetCalendar);
			
		} else {
			targetCalendars.add(null);
		}
	}
%>
