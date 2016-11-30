<%--
    view_bibliographies.jsp: Default view of the bibliography manager portlet.
    
    Created:    2016-11-29 22:52 by Christian Berndt
    Modified:   2016-11-29 22:52 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    boolean hasAddPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            BibliographyActionKeys.ADD_BIBLIOGRAPHY);
%>

<div class="panel panel-default">
    <div class="panel-heading">
        <strong><liferay-ui:message key="your-bibliographies"/></strong>     
    </div>
    
    <%-- 
    <div class="panel-body">
        <div class="input-group">
            <input type="text" class="form-control"
                placeholder="Search for..."> <span
                class="input-group-btn">
                <button class="btn btn-default" type="button">Go!</button>
            </span>
        </div>
    </div>
    --%>
    
    <ul class="list-group">
        <li class="list-group-item">Cras justo odio</li>
        <li class="list-group-item">Dapibus ac facilisis in</li>
        <li class="list-group-item">Morbi leo risus</li>
        <li class="list-group-item">Porta ac consectetur ac</li>
        <li class="list-group-item">Vestibulum at eros</li>
        <li class="list-group-item">    
            <portlet:renderURL var="editBibliographyURL">
                <portlet:param name="mvcPath"
                    value="/edit_bibliography.jsp" />
                <portlet:param name="redirect" value="<%=currentURL%>" />
            </portlet:renderURL>
        
            <aui:button href="<%=editBibliographyURL%>"
                cssClass="btn-primary btn-sm"
                value="new-bibliography"
                disabled="<%= !hasAddPermission %>" />
        </li>
    </ul>
</div>
