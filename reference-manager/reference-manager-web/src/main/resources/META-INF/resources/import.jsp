<%--
    export.jsp: The import-gui of the reference manager portlet.
    
    Created:    2016-01-10 23:33 by Christian Berndt
    Modified:   2016-11-28 23:09 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%
    String tabs1 = ParamUtil.getString(request, "tabs1", "import");
%>

<portlet:actionURL name="importBibTeXFile" var="importBibTeXFileURL" />

<aui:form action="<%=importBibTeXFileURL%>"
    enctype="multipart/form-data" method="post" name="fm1">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="importBibTeXFile" />

    <div class="upload">

        <aui:input label="" name="file" type="file" inlineField="true"
            disabled="<%=!ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
                        ReferenceActionKeys.IMPORT_REFERENCES)%>" />

        <aui:button type="submit" value="import"
            disabled="<%=!ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
                        ReferenceActionKeys.IMPORT_REFERENCES)%>" />
    </div>
</aui:form>
