<%--
    reference_action.jsp: The action menu of the reference manager's default view.
    
    Created:    2016-11-29 18:51 by Christian Berndt
    Modified:   2016-11-29 18:51 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Reference reference = null;

    if (row != null) {
        reference = (Reference) row.getObject();
    } else {
        reference = (Reference) request.getAttribute("edit_reference.jsp-reference");
    }
%>

<liferay-ui:icon-menu icon="<%=StringPool.BLANK%>"
    message="<%=StringPool.BLANK%>" showExpanded="<%=row == null%>"
    showWhenSingleIcon="<%=row == null%>">

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.VIEW)%>">

        <portlet:renderURL var="viewURL">
            <portlet:param name="mvcPath" value="/edit_reference.jsp" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:renderURL>

        <liferay-ui:icon iconCssClass="icon-eye-open" message="view"
            url="<%=viewURL%>" />

    </c:if>

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.UPDATE)%>">

        <portlet:renderURL var="editURL">
            <portlet:param name="mvcPath" value="/edit_reference.jsp" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:renderURL>

        <liferay-ui:icon iconCssClass="icon-edit" message="edit"
            url="<%=editURL%>" />

    </c:if>

    <portlet:renderURL var="redirectURL">
        <portlet:param name="mvcPath" value="/view.jsp" />
    </portlet:renderURL>

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.DELETE)%>">

        <portlet:actionURL var="deleteURL" name="deleteReference">
            <portlet:param name="redirect"
                value="<%=(row == null) ? redirectURL : currentURL%>" />
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon-delete url="<%=deleteURL%>" />

    </c:if>

    <c:if test="<%= ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.PERMISSIONS) %>">

        <liferay-security:permissionsURL
            modelResource="<%= Reference.class.getName() %>"
            modelResourceDescription="<%= reference.getCitation() %>"
            resourcePrimKey="<%= String.valueOf(reference.getReferenceId()) %>"
            var="permissionsEntryURL"
            windowState="<%= LiferayWindowState.POP_UP.toString() %>" />

        <liferay-ui:icon iconCssClass="icon-cog" message="permissions" method="get"
            url="<%= permissionsEntryURL %>" useDialog="<%= true %>" />
    </c:if>

</liferay-ui:icon-menu>
