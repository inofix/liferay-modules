<%--
    init.jsp: Common imports and setup code of the social-media portlet.
    
    Created:    2015-08-20 13:12 by Christian Berndt
    Modified:   2015-08-20 13:12 by Christian Berndt
    Version:    1.0.0
--%>

<%-- Import required classes --%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>


<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%

    // orientation
    
    String[] availableOrientations = new String[] {"horizontal", "vertical"}; 
    
    String selectedOrientation = portletPreferences.getValue("orientation", "horizontal"); 


    // services
    
	String[] availableServices = new String[] { "facebook",
			"googleplus", "info", "linkedin", "mail", "pinterest",
			"twitter", "whatsapp", "xing" };

	String[] selectedServices = portletPreferences.getValues("services",
			new String[] { "facebook", "googleplus", "twitter" });
	

	// theme
	
    String[] availableThemes = new String[] {"standard", "grey", "white"};
	
    String selectedTheme = portletPreferences.getValue("theme", "standard"); 
%>
