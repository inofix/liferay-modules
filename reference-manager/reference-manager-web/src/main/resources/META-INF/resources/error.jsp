<%--
    error.jsp: error page of the reference manager portlet.

    Created:     2016-11-29 01:49 by Christian Berndt
    Modified:    2016-11-29 01:49 by Christian Berndt
    Version:     1.0.0
--%>


<%@ include file="/init.jsp" %>

<%@page import="ch.inofix.referencemanager.exception.NoSuchReferenceException"%>

<liferay-ui:error-header />

<%-- <liferay-ui:error exception="<%= NoSuchReferenceException %>" message="the-reference-could-not-be-found" /> --%>

<liferay-ui:error-principal />