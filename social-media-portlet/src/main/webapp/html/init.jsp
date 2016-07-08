<%--
    init.jsp: Common imports and setup code of the social-media portlet.
    
    Created:    2015-08-20 13:12 by Christian Berndt
    Modified:   2016-07-08 23:39 by Christian Berndt
    Version:    1.0.6
--%>

<%-- Import required classes --%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme"%>


<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    // backend-url

    String backendUrl = portletPreferences.getValue("backend-url", "");

    // github-url

    String githubUrl = portletPreferences.getValue("github-url", "");

    // mail

    String mailBody = portletPreferences.getValue("mail-body", "");

    String mailSubject =
        portletPreferences.getValue("mail-subject", "");

    String mailUrl = portletPreferences.getValue("mail-url", "");

    // orientation

    String[] availableOrientations = new String[] {
        "horizontal", "vertical"
    };

    String selectedOrientation =
        portletPreferences.getValue("orientation", "horizontal");

    // services

    String[] availableServices =
        new String[] {
            "addthis", "diaspora", "facebook", "flattr", "googleplus",
            "info", "linkedin", "mail", "pinterest", "qzone", "reddit",
            "stumbleupon", "tencent-weibo", "threema", "tumblr",
            "twitter", "weibo", "whatsapp", "xing"
        };

    String[] selectedServices =
        portletPreferences.getValues("services", new String[] {
            "facebook", "googleplus", "twitter"
        });

    // share-url

    String shareUrl = portletPreferences.getValue("share-url", null);

    // show build info

    boolean showBuildInfo =
        GetterUtil.getBoolean(portletPreferences.getValue(
            "show-build-info", "false"));

    // theme

    String[] availableThemes = new String[] {
        "standard", "grey", "white"
    };

    String selectedTheme =
        portletPreferences.getValue("theme", "standard");

    // twitter

    String twitterVia =
        portletPreferences.getValue("twitter-via", null);

    // use container

    boolean useContainer =
        GetterUtil.getBoolean(portletPreferences.getValue(
            "use-container", "false"));
%>
