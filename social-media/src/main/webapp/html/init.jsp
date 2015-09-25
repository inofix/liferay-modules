<%--
    init.jsp: Common imports and setup code of the social-media portlet.
    
    Created:    2015-08-20 13:12 by Christian Berndt
    Modified:   2015-09-25 15:15 by Christian Berndt
    Version:    1.0.4
--%>

<%-- Import required classes --%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portlet.PortletPreferencesFactoryUtil"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletPreferences"%>
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
	PortletPreferences preferences = renderRequest.getPreferences();

	String portletResource = ParamUtil.getString(request,
			"portletResource");

	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(
				request, portletResource);
	}

	// backend-url

	String backendUrl = preferences.getValue("backend-url", null);

	// github-url

	String githubUrl = preferences.getValue("github-url", null);

	// mail

	String mailBody = preferences.getValue("mail-body", null);

	String mailSubject = preferences.getValue("mail-subject", null);

	String mailUrl = preferences.getValue("mail-url", null);

	// orientation

	String[] availableOrientations = new String[] { "horizontal",
			"vertical" };

	String selectedOrientation = preferences.getValue("orientation",
			"horizontal");

	// services

	String[] availableServices = new String[] { "addthis", "facebook",
			"googleplus", "info", "linkedin", "mail", "pinterest",
			"twitter", "tumblr", "whatsapp", "xing" };

	String[] selectedServices = preferences.getValues("services",
			new String[] { "facebook", "googleplus", "twitter" });

	// show build info

	boolean showBuildInfo = GetterUtil.getBoolean(preferences.getValue(
			"show-build-info", "false"));

	// theme

	String[] availableThemes = new String[] { "standard", "grey",
			"white" };

	String selectedTheme = preferences.getValue("theme", "standard");

	// twitter

	String twitterVia = preferences.getValue("twitter-via", null);

	// use container

	boolean useContainer = GetterUtil.getBoolean(preferences.getValue(
			"use-container", "false"));
%>
