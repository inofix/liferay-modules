<%--
    view.jsp: Default view of the newsletter-portlet.
    
    Created:     2016-10-05 15:54 by Christian Berndt
    Modified:    2016-10-10 19:08 by Christian Berndt
    Version:     1.0.9
 --%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String tabs1 = ParamUtil.getString(request, "tabs1", "subscribers");

    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/view.jsp");
    portletURL.setParameter("backURL", backURL);

    Log log = LogFactoryUtil.getLog("docroot.html.view.jsp");
%>

<div id="<portlet:namespace />newsletterContainer">

    <liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

    <liferay-ui:tabs names="newsletters,mailings,subscribers,templates"
        param="tabs1" url="<%=portletURL.toString()%>" />

    <c:choose>

        <c:when test='<%=tabs1.equals("newsletters")%>'>        
            <%@include file="/html/newsletters.jspf"%>
        </c:when>

        <c:when test='<%=tabs1.equals("templates")%>'>
            <%@include file="/html/templates.jspf"%>
        </c:when>

        <c:when test='<%=tabs1.equals("mailings")%>'>
            <%@include file="/html/mailings.jspf"%>
        </c:when>

        <c:otherwise>        
            <%@include file="/html/subscribers.jspf"%>
        </c:otherwise>

    </c:choose>

    <hr>

    <ifx-util:build-info />

</div>
