<%--
    init.jsp: Common imports and initialization code.

    Created:     2014-02-01 15:31 by Christian Berndt
    Modified:    2016-03-22 10:43 by Christian Berndt
    Version:     1.0.5
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.search.SortFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.DateUtil"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.workflow.WorkflowConstants"%>

<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>

<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="com.liferay.portlet.PortletPreferencesFactoryUtil"%>


<%@page import="ch.inofix.portlet.timetracker.model.TaskRecord"%>
<%@page import="ch.inofix.portlet.timetracker.search.TaskRecordSearchTerms"%>
<%@page import="ch.inofix.portlet.timetracker.search.TaskRecordDisplayTerms"%>
<%@page import="ch.inofix.portlet.timetracker.search.TaskRecordSearch"%>
<%@page import="ch.inofix.portlet.timetracker.security.permission.ActionKeys"%>
<%@page import="ch.inofix.portlet.timetracker.service.permission.TaskRecordPermission"%>
<%@page import="ch.inofix.portlet.timetracker.service.permission.TimetrackerPortletPermission"%>
<%@page import="ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.timetracker.util.CommonFields"%>
<%@page import="ch.inofix.portlet.timetracker.util.TaskRecordFields"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimeFormat"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimetrackerPortletKeys"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimetrackerPortletUtil"%>

<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/inofix-util.tld" prefix="iu"%>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Enable the JSR 286 portlet variables available, --%>
<%-- e.g. renderRequest, portletConfig etc.          --%>
<%-- For the full list of available variables see    --%>
<%-- the JSR 286 specification.                      --%>
<portlet:defineObjects />

<%-- Enable the liferay-theme variables, e.g.        --%>
<%-- permissionChecker, scopeGroupId, required       --%>
<%-- by the permissionChecker (see below.)           --%>
<theme:defineObjects />

<%
	String[] columns = portletPreferences.getValues("columns",
	    new String[] { "task-record-id", "work-package", "start-date", "duration", "create-date", "modified-date", "user-name" });

    String currentURL = PortalUtil.getCurrentURL(request);

    // Remove any actionParameters from the currentURL
    currentURL = HttpUtil.removeParameter(currentURL,
            renderResponse.getNamespace() + "javax.portlet.action");
    
    boolean viewByDefault = GetterUtil.getBoolean(portletPreferences
        .getValue("view-by-default", "false"));
%>
