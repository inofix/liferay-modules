<%--
    manage.jsp: The manage-gui of the reference manager portlet.
    
    Created:    2016-01-20 22:59 by Christian Berndt
    Modified:   2016-12-14 18:51 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp"%>

<%
    String tabs1 = ParamUtil.getString(request, "tabs1", "manage");

    boolean hasDeletePermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.DELETE_GROUP_REFERENCES);

    boolean hasImportPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.IMPORT_REFERENCES);
%>

<portlet:actionURL name="deleteGroupReferences"
    var="deleteGroupReferencesURL" />

<aui:form action="<%=deleteGroupReferencesURL%>" name="fm1">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <aui:button-row>
        <aui:button disabled="<%=!hasDeletePermission%>" type="submit"
            value="delete-group-references" />
    </aui:button-row>

</aui:form>

<portlet:actionURL name="deleteAllReferences"
    var="deleteAllReferencesURL" />

<aui:form action="<%=deleteAllReferencesURL%>" name="fm2">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <aui:button-row>
        <aui:button disabled="<%=!hasDeletePermission%>" type="submit"
            value="delete-all-references" />
    </aui:button-row>

</aui:form>


<portlet:actionURL name="importSampleData" var="importSampleDataURL" />

<aui:form action="<%=importSampleDataURL%>" name="fm3">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <aui:button-row>
        <aui:button disabled="<%=!hasImportPermission%>" type="submit"
            value="import-sample-data" />
    </aui:button-row>

</aui:form>
