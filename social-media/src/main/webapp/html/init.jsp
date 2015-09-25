<%--
    init.jsp: Common imports and setup code of the social-media portlet.
    
    Created:    2015-08-20 13:12 by Christian Berndt
    Modified:   2015-09-25 14:46 by Christian Berndt
    Version:    1.0.3
--%>

<%-- Import required classes --%>

<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>


<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
	// backend-url

	String backendUrl = portletPreferences
			.getValue("backend-url", null);

	// github-url

	String githubUrl = portletPreferences.getValue("github-url", null);

	// mail

	String mailBody = portletPreferences.getValue("mail-body", null);

	String mailSubject = portletPreferences.getValue("mail-subject",
			null);

	String mailUrl = portletPreferences.getValue("mail-url", null);

	// orientation

	String[] availableOrientations = new String[] { "horizontal",
			"vertical" };

	String selectedOrientation = portletPreferences.getValue(
			"orientation", "horizontal");

	// services

	String[] availableServices = new String[] { "addthis", "facebook",
			"googleplus", "info", "linkedin", "mail", "pinterest",
			"twitter", "tumblr", "whatsapp", "xing" };

	String[] selectedServices = portletPreferences.getValues(
			"services", new String[] { "facebook", "googleplus",
					"twitter" });

	// show build info

	boolean showBuildInfo = GetterUtil.getBoolean(portletPreferences
			.getValue("show-build-info", "false"));

	// theme

	String[] availableThemes = new String[] { "standard", "grey",
			"white" };

	String selectedTheme = portletPreferences.getValue("theme",
			"standard");

	// twitter

	String twitterVia = portletPreferences
			.getValue("twitter-via", null);

	// use container

	boolean useContainer = GetterUtil.getBoolean(portletPreferences
			.getValue("use-container", "false"));
%>
