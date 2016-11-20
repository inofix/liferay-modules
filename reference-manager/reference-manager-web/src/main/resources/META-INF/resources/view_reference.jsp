<%--
    view_reference.jsp: Display a single reference.
    
    Created:    2016-11-19 23:08 by Christian Berndt
    Modified:   2016-11-19 23:08 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    long referenceId = ParamUtil.getLong(liferayPortletRequest, "referenceId");
%>

<h1>view_reference.jsp</h1>

referenceId = <%= referenceId %>
