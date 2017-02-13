<%--
    bibliography_action.jsp: The action menu of the bibliography manager's default view.
    
    Created:    2017-01-25 11:42 by Christian Berndt
    Modified:   2017-01-25 11:42 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Bibliography bibliography = (Bibliography) row.getObject();

    AssetRendererFactory<Bibliography> bibliographyAssetRendererFactory = AssetRendererFactoryRegistryUtil
            .getAssetRendererFactoryByClass(Bibliography.class);

    AssetRenderer<Bibliography> bibliographyAssetRenderer = bibliographyAssetRendererFactory
            .getAssetRenderer(bibliography.getBibliographyId());

    PortletURL editURL = bibliographyAssetRenderer.getURLEdit(liferayPortletRequest, liferayPortletResponse);
    editURL.setParameter("redirect", currentURL);

    String viewURL = bibliographyAssetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse,
            null);
    viewURL = HttpUtil.addParameter(viewURL, "redirect", currentURL);
%>


<liferay-ui:icon-menu icon="" message=""
    showWhenSingleIcon="true">

    <c:if test="<%=BibliographyPermission.contains(permissionChecker, bibliography,
                    BibliographyActionKeys.VIEW)%>">

        <liferay-ui:icon iconCssClass="icon-eye-open"
            message="view" url="<%=viewURL%>" />

    </c:if>

    <c:if test="<%=BibliographyPermission.contains(permissionChecker, bibliography,
                    BibliographyActionKeys.UPDATE)%>">

        <liferay-ui:icon iconCssClass="icon-edit"
            message="edit" url="<%=editURL.toString()%>" />

    </c:if>

    <c:if test="<%=BibliographyPermission.contains(permissionChecker, bibliography,
                    BibliographyActionKeys.DELETE)%>">

        <portlet:actionURL var="deleteURL"
            name="deleteBibliography">
            <portlet:param name="redirect"
                value="<%=currentURL%>" />
            <portlet:param name="bibliographyId"
                value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon-delete message="delete"
            url="<%=deleteURL%>" />

    </c:if>
    
    <c:if test="<%= BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.PERMISSIONS) %>">

        <liferay-security:permissionsURL
            modelResource="<%= Bibliography.class.getName() %>"
            modelResourceDescription="<%= bibliography.getTitle() %>"
            resourcePrimKey="<%= String.valueOf(bibliography.getBibliographyId()) %>"
            var="permissionsEntryURL"
            windowState="<%= LiferayWindowState.POP_UP.toString() %>" />

        <liferay-ui:icon iconCssClass="icon-cog" message="permissions" method="get"
            url="<%= permissionsEntryURL %>" useDialog="<%= true %>" />
    </c:if>   

</liferay-ui:icon-menu>
