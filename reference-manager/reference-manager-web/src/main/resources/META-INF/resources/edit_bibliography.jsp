<%--
    edit_bibliography.jsp: edit a bibliography.
    
    Created:    2016-11-30 00:18 by Christian Berndt
    Modified:   2016-11-30 00:18 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    long bibliographyId = ParamUtil.getLong(request, "bibliographyId");

    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);

    boolean hasUpdatePermission = true;

    if (bibliography != null) {
        BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.UPDATE);
    }
%>

<portlet:actionURL name="updateBibliography" var="updateBibliographyURL">
</portlet:actionURL>

<div class="panel panel-default">
    <div class="panel-heading">
        <strong><liferay-ui:message key="your-bibliographies" /></strong>
    </div>
    <div class="panel-body">
        <aui:form action="<%= updateBibliographyURL %>" method="post"
            name="fm">

            <aui:input name="redirect" type="hidden"
                value="<%=currentURL%>" />
            <aui:input name="bibliographyId" type="hidden"
                value="<%=bibliographyId%>" />

            <liferay-ui:asset-categories-error />

            <liferay-ui:asset-tags-error />

            <aui:model-context bean="<%=bibliography%>"
                model="<%=Bibliography.class%>" />

            <aui:fieldset>

                <aui:input disabled="<%=!hasUpdatePermission%>" label=""
                    name="title" placeholder="title" />

                <aui:input disabled="<%=!hasUpdatePermission%>"
                    name="description" placeholder="description"
                    label="" type="textarea" />

                <%-- 
                <aui:input name="categories" type="assetCategories"
                    disabled="<%=!hasUpdatePermission%>" />

                <aui:input name="tags" type="assetTags"
                    disabled="<%=!hasUpdatePermission%>" />
                    
                --%>

            </aui:fieldset>

            <aui:button-row>
                <aui:button cssClass="btn-sm"
                    disabled="<%=!hasUpdatePermission%>" type="submit" />

                <aui:button cssClass="btn-sm"
                    disabled="<%=!hasUpdatePermission%>"
                    href="<%=redirect%>" type="cancel" />
            </aui:button-row>

        </aui:form>

    </div>
</div>