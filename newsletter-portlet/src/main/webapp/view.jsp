<%--
    view.jsp: Default view of the newsletter-portlet.
    
    Created:     2016-10-05 15:54 by Christian Berndt
    Modified:    2016-10-05 15:54 by Christian Berndt
    Version:     1.0.0
 --%>

<%@page import="ch.inofix.portlet.newsletter.util.TemplateUtil"%>

<%@page import="com.liferay.portal.kernel.template.TemplateConstants"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@ include file="/html/init.jsp"%>

This is the <b>newsletter-portlet</b> <br />

script = <strong><%= script %></strong> <br />

<%
    String langType = TemplateConstants.LANG_TYPE_FTL;
    String languageId = LanguageUtil.getLanguageId(locale);
    Map<String, Object> contextObjects = new HashMap<String, Object>();
    Map<String, String> map = new HashMap<String, String>();
    map.put("firstname", "Christian"); 
    map.put("lastname", "Berndt"); 
    map.put("salutation", "Hallo"); 
    contextObjects.put("contact", map);
    String viewMode = Constants.VIEW;
    String xml = "";

    String content = TemplateUtil.transform(
        themeDisplay, contextObjects, script, langType);
%>

content = <%= content %>

    <hr>
    
    <ifx-util:build-info/>
