<%--
    view.jsp: Default view of the data portlet.
    
    Created:    2017-03-09 19:59 by Christian Berndt
    Modified:   2017-11-28 19:45 by Christian Berndt
    Version:    1.0.8
--%>

<%@ include file="/html/init.jsp"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("backURL", backURL);
    portletURL.setParameter("id", id);
    portletURL.setParameter("from", String.valueOf(from));
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("until", String.valueOf(until));
%>

<liferay-ui:header backURL="<%=backURL%>" title="data-manager" />

<liferay-ui:error exception="<%=PrincipalException.class%>"
    message="you-dont-have-the-required-permissions" />

<liferay-ui:tabs names="latest,chart,import-export" param="tabs1"
    url="<%=portletURL.toString()%>" />

<c:choose>

    <c:when test='<%=tabs1.equals("latest")%>'>
                
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/latest.jsp" />   
        
    </c:when>

    <c:when test='<%=tabs1.equals("list")%>'>
            
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/view_measurements.jsp" />   
        
    </c:when>

    <c:when test='<%=tabs1.equals("import-export")%>'>
            
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/import.jsp" />   
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/delete_measurements.jsp" />   
        
    </c:when>

    <c:otherwise>
    
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/chart.jsp" />    
    
    </c:otherwise>
</c:choose>

<%-- <ifx-util:build-info /> --%>
