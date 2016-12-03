<%--
    import_bibliography.jsp: The import panel of bibliography manager.
    
    Created:    2016-12-01 02:50 by Christian Berndt
    Modified:   2016-12-02 18:25 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%
    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);

    String bibliographyId = String.valueOf(bibliography.getBibliographyId()); 
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "import");
   
    boolean hasImportPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.IMPORT_REFERENCES);
%>

<portlet:actionURL name="importBibliography" var="importBibliographyURL"/>

<aui:form action="<%=importBibliographyURL%>"
    enctype="multipart/form-data" method="post" name="fm1">
    
    <aui:input name="bibliographyId" type="hidden" value="<%= bibliographyId %>"/>
    <aui:input name="tabs1" type="hidden" value="<%= tabs1 %>"/>

    <div class="upload">

        <aui:input label="" name="file" type="file" inlineField="true"
            disabled="<%= !hasImportPermission %>" />

        <aui:button type="submit" value="import"
            disabled="<%=!hasImportPermission %>" />
            
        <p class="help-message">
            <liferay-ui:message
                key="select-the-bibliography-you-want-to-import-into-bibliography-x" arguments="<%= new String[] {bibliography.getTitle()} %>" />
        </p>
    </div>
</aui:form>
