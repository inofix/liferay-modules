<%--
    init.jsp: Common imports and setup code of the contact-manager
    
    Created:    2015-05-07 15:16 by Christian Berndt
    Modified:   2015-06-29 16:11 by Christian Berndt
    Version:    1.0.8
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
			new String[] { "name", "modified-date" });

	String currentURL = PortalUtil.getCurrentURL(request);

	// Remove any actionParameters from the currentURL
	currentURL = HttpUtil.removeParameter(currentURL,
			renderResponse.getNamespace() + "javax.portlet.action");

	String portraitDataUri = "data:image/png;base64,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";

	String portraitClass = portletPreferences.getValue(
	         "portraitClass", "img-circle");
	String portrait = portletPreferences.getValue("portrait",
			portraitDataUri);
	String portraitFemale = portletPreferences.getValue(
			"portraitFemale", portraitDataUri);
	String portraitGroup = portletPreferences.getValue("portraitGroup",
			portraitDataUri);
	String portraitMale = portletPreferences.getValue("portraitMale",
			portraitDataUri);
	String portraitOrganization = portletPreferences.getValue(
			"portraitOrganization", portraitDataUri);
	String portraitStyle = portletPreferences.getValue(
            "portraitStyle", "max-height: 70px;");

	boolean showDeathdate = GetterUtil.getBoolean(portletPreferences
			.getValue("show-death-date", "false"));
%>
