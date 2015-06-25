<%--
    init.jsp: Common imports and setup code of the contact-manager
    
    Created:    2015-05-07 15:16 by Christian Berndt
    Modified:   2015-06-25 16:47 by Christian Berndt
    Version:    1.0.7
--%>

<%-- Import required classes --%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="ch.inofix.portlet.contact.model.Contact"%>
<%@page import="ch.inofix.portlet.contact.search.ContactSearch"%>
<%@page import="ch.inofix.portlet.contact.security.permission.ActionKeys"%>
<%@page import="ch.inofix.portlet.contact.service.permission.ContactPermission"%>

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="ezvcard.VCard"%>

<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
	String[] columns = portletPreferences.getValues("columns",
			new String[] {"name", "modified-date"});

	String currentURL = PortalUtil.getCurrentURL(request);

	// Remove any actionParameters from the currentURL
	currentURL = HttpUtil.removeParameter(currentURL,
			renderResponse.getNamespace() + "javax.portlet.action");

	boolean showDeathdate = GetterUtil.getBoolean(portletPreferences
			.getValue("show-death-date", "false"));
%>
