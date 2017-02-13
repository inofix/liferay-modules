<%--
    reference_action.jsp: The action menu of the reference manager's default view.
    
    Created:    2016-11-29 18:51 by Christian Berndt
    Modified:   2017-02-04 17:26 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/init.jsp"%>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Bibliography bibliography = (Bibliography) request.getAttribute("bibliography_entries.jsp-bibliography");

    Reference reference = null;

    if (row != null) {
        reference = (Reference) row.getObject();
    } else {
        reference = (Reference) request.getAttribute("edit_reference.jsp-reference");
    }

    AssetRendererFactory<Reference> referenceAssetRendererFactory = AssetRendererFactoryRegistryUtil
            .getAssetRendererFactoryByClass(Reference.class);

    AssetRenderer<Reference> referenceAssetRenderer = referenceAssetRendererFactory
            .getAssetRenderer(reference.getReferenceId());

    PortletURL editURL = referenceAssetRenderer.getURLEdit(liferayPortletRequest, liferayPortletResponse);
    editURL.setParameter("redirect", currentURL);

    String viewURL = referenceAssetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse,
            null);
    viewURL = HttpUtil.addParameter(viewURL, "redirect", currentURL);
%>

<liferay-ui:icon-menu icon="<%=StringPool.BLANK%>"
    message="<%=StringPool.BLANK%>" showExpanded="<%=row == null%>"
    showWhenSingleIcon="true">

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.VIEW)%>">

        <liferay-ui:icon iconCssClass="icon-eye-open" message="view"
            url="<%=viewURL%>" />

    </c:if>

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.UPDATE)%>">

        <liferay-ui:icon iconCssClass="icon-edit" message="edit"
            url="<%=editURL.toString()%>" />

    </c:if>
    
    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.DELETE)%>">

        <portlet:actionURL var="deleteURL" name="deleteReference">
            <c:if test="<%= bibliography != null %>">
                <portlet:param name="bibliographyId"
                    value="<%=String.valueOf(bibliography.getBibliographyId())%>" />                
            </c:if>  
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon-delete label="true" url="<%=deleteURL%>" />

    </c:if>

    <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.DELETE)%>">

        <portlet:actionURL var="deleteBibRefRelationURL"
            name="deleteBibRefRelation">
            <c:if test="<%=bibliography != null%>">
                <portlet:param name="bibliographyId"
                    value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
            </c:if>
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon iconCssClass="icon-remove-sign"
            message="remove-from-bibliography"
            url="<%=deleteBibRefRelationURL%>" />

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
