<%--
    view.jsp: Default view of the newsletter-portlet.
    
    Created:     2016-10-05 15:54 by Christian Berndt
    Modified:    2016-10-06 18:01 by Christian Berndt
    Version:     1.0.1
 --%>

<%@page import="com.liferay.portal.security.auth.PrincipalException"%>
<%@page import="ch.inofix.portlet.newsletter.util.TemplateUtil"%>

<%@page import="com.liferay.portal.kernel.template.TemplateConstants"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@ include file="/html/init.jsp"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String tabs1 = ParamUtil.getString(request, "tabs1", "subscribers");  
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/view.jsp");
    portletURL.setParameter("backURL", backURL); 
%>

<div id="<portlet:namespace />newsletterContainer">


    <liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />
    
    <liferay-ui:error exception="<%= PrincipalException.class %>" 
       message="you-dont-have-the-required-permissions"/>
    
    <liferay-ui:tabs
        names="subscribers,newsletters,templates,mailings"
        param="tabs1" url="<%= portletURL.toString() %>" />
        
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
            <h1>Subscribers</h1>
        </c:otherwise>
        
    </c:choose>

    <hr>

    <ifx-util:build-info />

</div>