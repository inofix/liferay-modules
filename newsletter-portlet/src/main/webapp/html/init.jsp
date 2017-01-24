<%--
    init.jsp: Common imports and initialization code.

    Created:     2016-10-05 15:44 by Christian Berndt
    Modified:    2016-11-28 12:18 by Christian Berndt
    Version:     1.1.5
--%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.newsletter.EmailAddressException"%>
<%@page import="ch.inofix.portlet.newsletter.NoSuchNewsletterException"%>
<%@page import="ch.inofix.portlet.newsletter.model.Mailing"%>
<%@page import="ch.inofix.portlet.newsletter.model.Newsletter"%>
<%@page import="ch.inofix.portlet.newsletter.model.Subscriber"%>
<%@page import="ch.inofix.portlet.newsletter.search.MailingSearch"%>
<%@page import="ch.inofix.portlet.newsletter.search.NewsletterSearch"%>
<%@page import="ch.inofix.portlet.newsletter.security.permission.ActionKeys"%>
<%@page import="ch.inofix.portlet.newsletter.service.MailingServiceUtil"%>
<%@page import="ch.inofix.portlet.newsletter.service.NewsletterServiceUtil"%>
<%@page import="ch.inofix.portlet.newsletter.service.SubscriberServiceUtil"%>
<%@page import="ch.inofix.portlet.newsletter.service.permission.MailingPermission"%>
<%@page import="ch.inofix.portlet.newsletter.service.permission.NewsletterPermission"%>
<%@page import="ch.inofix.portlet.newsletter.service.permission.NewsletterPortletPermission"%>

<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletURL"%>

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
    long articleGroupId = GetterUtil.getLong(portletPreferences
            .getValue("articleGroupId", ""));

    String currentURL = PortalUtil.getCurrentURL(request);
    
    String newsletterStructureId = portletPreferences.getValue(
            "newsletterStructureId", "");
%>