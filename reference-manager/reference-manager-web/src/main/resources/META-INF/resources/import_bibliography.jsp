<%--
    export.jsp: The import panel of bibliography manager.
    
    Created:    2016-12-01 02:50 by Christian Berndt
    Modified:   2016-12-01 02:50 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    String tabs1 = ParamUtil.getString(request, "tabs1", "import");

    boolean hasImportPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.IMPORT_REFERENCES);
%>

<portlet:actionURL name="importBibTeXFile" var="importBibTeXFileURL" />

<aui:form action="<%=importBibTeXFileURL%>"
    enctype="multipart/form-data" method="post" name="fm1">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <div class="upload">

        <aui:input label="" name="file" type="file" inlineField="true"
            disabled="<%= !hasImportPermission %>" />

        <aui:button type="submit" value="import"
            disabled="<%=!hasImportPermission %>" />
    </div>
</aui:form>
