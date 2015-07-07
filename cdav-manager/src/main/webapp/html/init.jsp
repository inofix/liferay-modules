<%--
    init.jsp: Common imports and setup code of the cdav-manager portlet.
    
    Created:    2015-05-30 12:19 by Christian Berndt
    Modified:   2015-07-07 20:54 by Christian Berndt
    Version:    1.0.6
--%>

<%-- Import required classes --%>

<%@page import="java.util.Date"%>

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
	String calendar = portletPreferences.getValue("calendar", "");
	String calendarId = portletPreferences.getValue("calendarId", "");
	String domain = portletPreferences.getValue("domain", "");
	// TODO: Retrieve the lastSync from the portlet / application scope
	Date lastSync = new Date();
	String password = portletPreferences.getValue("password", "");
	String restoreFromTrash = portletPreferences.getValue("restoreFromTrash", "false");
	String servername = portletPreferences.getValue("servername", "");
	String username = portletPreferences.getValue("username", "");

	long targetCalendarId = GetterUtil.getLong(calendarId);

	Calendar targetCalendar = null;
	String targetCalendarName = LanguageUtil.get(pageContext,
			"not-selected");
	if (targetCalendarId > 0) {
		targetCalendar = CalendarServiceUtil
				.getCalendar(targetCalendarId);
		targetCalendarName = targetCalendar.getName(locale);
	}

	boolean hasConnectionParameters = false;

	if (Validator.isNotNull(servername) && Validator.isNotNull(domain)
			&& Validator.isNotNull(username)
			&& Validator.isNotNull(password)) {
		hasConnectionParameters = true;
	}
%>
