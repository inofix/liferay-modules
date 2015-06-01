<%--
    init.jsp: Common imports and setup code of the cdav-manager portlet.
    
    Created:    2015-05-30 12:19 by Christian Berndt
    Modified:   2015-05-31 23:28 by Christian Berndt
    Version:    1.0.1
--%>

<%-- Import required classes --%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	String password = portletPreferences.getValue("password", "");
	String servername = portletPreferences.getValue("servername", "");
	String username = portletPreferences.getValue("username", "");
%>
