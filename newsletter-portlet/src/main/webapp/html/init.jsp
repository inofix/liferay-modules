<%--
    init.jsp: Common imports and initialization code.

    Created:     2016-10-05 15:44 by Christian Berndt
    Modified:    2016-10-07 14:30 by Christian Berndt
    Version:     1.0.1
--%>

<%-- Import required classes --%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme"%>


<%-- Common setup code --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    String className = portletPreferences.getValue("className", "ch.inofix.portlet.contact.model.Contact");
    String script = portletPreferences.getValue("script", "");
%>