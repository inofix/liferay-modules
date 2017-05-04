<%--
    view.jsp: Default view of the my-sites-directory-portlet.
    
    Created:    2017-04-26 16:54 by Christian Berndt
    Modified:   2017-05-04 15:23 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp" %>

<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>

<%@page import="com.liferay.portal.kernel.util.PropsUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.model.Group"%>
<%@page import="com.liferay.portal.service.GroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="javax.portlet.WindowState"%>
<%@page import="javax.portlet.PortletURL"%>

<%
    String currentURL = PortalUtil.getCurrentURL(request);

    LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();
    groupParams.put("usersGroups", new Long(user.getUserId()));
    groupParams.put("active", Boolean.TRUE);

    int total = GroupLocalServiceUtil.searchCount(
            company.getCompanyId(), null, groupParams);

    List<Group> groups = GroupLocalServiceUtil.search(
            company.getCompanyId(), null, groupParams, 0,
            maxGroups, null);

    String privateServletMapping = PropsUtil
            .get("layout.friendly.url.private.group.servlet.mapping");
    String publicServletMapping = PropsUtil
            .get("layout.friendly.url.public.servlet.mapping");
%>

<div class="panel panel-default sites-directory-taglib">

    <div class="panel-heading">
        <liferay-ui:message key="my-sites"/>
    </div>
    
    <ul class="list-group">
        <% 
            for (Group group : groups) {
                
                String href = ""; 
                
                if (group.hasPublicLayouts()) {
                    href = publicServletMapping + group.getFriendlyURL(); 
                } 
                
                if (group.hasPrivateLayouts()) {
                    href = privateServletMapping + group.getFriendlyURL();                     
                }
                
        %>
        <c:if test="<%= Validator.isNotNull(href) %>">
            <li class="list-group-item">
                <a href="<%= href %>"><%= group.getDescriptiveName(locale) %></a>
            </li>
        </c:if>
        <% } %>
    </ul>

</div>
