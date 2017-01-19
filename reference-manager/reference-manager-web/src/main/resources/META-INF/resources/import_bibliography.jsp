<%--
    import_bibliography.jsp: The import panel of bibliography manager.
    
    Created:    2016-12-01 02:50 by Christian Berndt
    Modified:   2017-01-19 21:00 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/init.jsp" %>

<%
    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);

    String bibliographyId = String.valueOf(bibliography.getBibliographyId()); 
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "import");
   
    boolean hasImportPermission = BibliographyManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            BibliographyActionKeys.IMPORT_BIBLIOGRAPHY);
%>

<portlet:actionURL name="importBibliography" var="importBibliographyURL"/>

<aui:form action="<%=importBibliographyURL%>"
    enctype="multipart/form-data" method="post" name="fm1">

    <aui:input name="bibliographyId" type="hidden"
        value="<%=bibliographyId%>" />
    <aui:input name="mvcPath" type="hidden"
        value="/edit_bibliography.jsp" />
    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <div class="upload">

        <aui:input label="" name="file" type="file" inlineField="true"
            disabled="<%=!hasImportPermission%>" />

        <aui:button type="submit" value="import"
            disabled="<%=!hasImportPermission%>" />

        <p class="help-block">
            <liferay-ui:message
                key="select-the-bibliography-you-want-to-import-into-bibliography-x"
                arguments="<%=new String[] { bibliography.getTitle() }%>" />
        </p>
    </div>
</aui:form>

<portlet:resourceURL id="exportBibliography" var="exportBibliographyURL">
    <portlet:param name="bibliographyId"
        value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
</portlet:resourceURL>

<aui:button href="<%=exportBibliographyURL%>"
    icon="download" primary="true" value="export-bibliography"/>
