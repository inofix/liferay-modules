<%--
    import_bibliography.jsp: The import panel of bibliography manager.
    
    Created:    2016-12-01 02:50 by Christian Berndt
    Modified:   2017-01-25 11:35 by Christian Berndt
    Version:    1.0.5
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


    <aui:fieldset>
        <div class="upload">
            <aui:input name="fileName" type="hidden" />
            <aui:input label="" name="file" type="file"
                inlineField="true" disabled="<%=!hasImportPermission%>" />

            <aui:button disabled="true" id="import" type="submit"
                value="import" />

            <p class="help-block">
                <liferay-ui:message
                    key="select-the-bibliography-you-want-to-import-into-bibliography-x"
                    arguments="<%=new String[] { bibliography.getTitle() }%>" />
            </p>
        </div>
    </aui:fieldset>

    <aui:fieldset>

        <aui:field-wrapper label="update-existing" inlineLabel="false">
            <aui:input checked="true"
                disabled="<%=!hasImportPermission%>" label="yes"
                name="updateExisting" type="radio" inlineField="yes"
                inlineLabel="yes" value="true" />
            <aui:input disabled="<%=!hasImportPermission%>" label="no"
                name="updateExisting" type="radio" inlineField="no"
                inlineLabel="yes" value="false" />
        </aui:field-wrapper>

        <p class="help-block">
            <liferay-ui:message
                key="whether-to-update-existing-references-or-not" />
        </p>
    </aui:fieldset>

</aui:form>

<aui:script use="aui-base">

    var input = A.one('#<portlet:namespace />file');
    var fileName = A.one('#<portlet:namespace />fileName');
    var button = A.one('#<portlet:namespace />import');

    input.on('change', function(e) {
    	
    	var value = input.get('value'); 

        if (value) {
            button.removeClass('disabled');
            button.removeAttribute('disabled');
            fileName.val(value);
        } else {
            button.addClass('disabled');
            button.attr('disabled','disabled');
        }

    });
</aui:script>

