<%--
    init.jsp: Common imports and initialization code.

    Created:     2014-02-01 15:31 by Christian Berndt
    Modified:     2014-02-01 15:31 by Christian Berndt
    Version:     1.0
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletURL"%>


<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.search.SortFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.DateUtil"%>
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
<%@page import="ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.timetracker.util.CommonFields"%>
<%@page import="ch.inofix.portlet.timetracker.util.TaskRecordFields"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimeFormat"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimetrackerPortletKeys"%>
<%@page import="ch.inofix.portlet.timetracker.util.TimetrackerPortletUtil"%>

<%-- Import required taglibs --%>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="iu" uri="/WEB-INF/tld/inofix-util.tld"%>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet"%>
<%@ taglib prefix="liferay-theme" uri="http://liferay.com/tld/theme"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>

<%-- Enable the JSR 286 portlet variables available, --%>
<%-- e.g. renderRequest, portletConfig etc.          --%>
<%-- For the full list of available variables see    --%>
<%-- the JSR 286 specification.                      --%>
<portlet:defineObjects />

<%-- Enable the liferay-theme variables, e.g.        --%>
<%-- permissionChecker, scopeGroupId, required       --%>
<%-- by the permissionChecker (see below.)           --%>
<liferay-theme:defineObjects />
