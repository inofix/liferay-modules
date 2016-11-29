<%--
    edit_reference.jsp: edit a single reference.
    
    Created:    2016-11-18 18:46 by Christian Berndt
    Modified:   2016-11-29 19:15 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    long referenceId = ParamUtil.getLong(request, "referenceId");

    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);

    boolean hasUpdatePermission = ReferencePermission.contains(permissionChecker, reference,
            ReferenceActionKeys.UPDATE);
%>

<portlet:actionURL name="updateReference" var="updateReferenceURL">
</portlet:actionURL>

<aui:form action="<%= updateReferenceURL %>" method="post" name="fm">

    <aui:input name="redirect" type="hidden" value="<%=currentURL%>" />
    <aui:input name="referenceId" type="hidden" value="<%=referenceId%>" />

    <liferay-ui:header backURL="<%=redirect%>"
        title='<%=(reference != null) ? reference.getBibTeX() : "new-reference"%>' />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />

    <aui:model-context bean="<%=reference%>"
        model="<%=Reference.class%>" />

    <aui:fieldset>
        <aui:input label="bibtex" name="bibTeX" type="textarea"
            disabled="<%= !hasUpdatePermission %>" />

        <liferay-ui:custom-attributes-available
            className="<%=Reference.class.getName()%>">

            <liferay-ui:custom-attribute-list
                className="<%=Reference.class.getName()%>"
                classPK="<%=(reference != null) ? reference.getReferenceId() : 0%>"
                editable="<%=true%>" label="<%=true%>" />

        </liferay-ui:custom-attributes-available>

        <aui:input name="categories" type="assetCategories"
            disabled="<%= !hasUpdatePermission %>" />

        <aui:input name="tags" type="assetTags"
            disabled="<%= !hasUpdatePermission %>" />

    </aui:fieldset>

    <aui:button-row>
        <aui:button type="submit" disabled="<%= !hasUpdatePermission %>" />

        <aui:button href="<%=redirect%>" type="cancel" />
    </aui:button-row>

</aui:form>
