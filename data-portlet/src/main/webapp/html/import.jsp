<%--
    import.jsp: The import panel of the data-portlet
    
    Created:    2017-03-13 12:46 by Christian Berndt
    Modified:   2017-11-28 12:04 by Christian Berndt
    Version:    1.0.7
--%>

<%@ include file="/html/init.jsp"%>

<%
    // TODO: add proper permission checks
    boolean hasImportPermission = themeDisplay.isSignedIn();

    String currentURL = PortalUtil.getCurrentURL(request);
%>

<liferay-ui:error exception="<%=FileFormatException.class %>" message="the-format-of-the-uploaded-file-is-invalid"/>
<liferay-ui:error exception="<%=MeasurementXMLException.class %>" message="cant-read-xml-document"/>

<portlet:actionURL var="importMeasurementsURL" name="importMeasurements">
    <portlet:param name="mvcPath" value="/html/import_processes.jsp" />
    <portlet:param name="redirect" value="<%= currentURL %>" />
    <portlet:param name="tabs1" value="import-export" />
</portlet:actionURL>

<portlet:renderURL var="browseURL" />

<aui:form action="<%=importMeasurementsURL%>"
    enctype="multipart/form-data" method="post" name="fm"
    cssClass="import-form">

    <aui:fieldset label="import">

        <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />

        <aui:input name="file" disabled="<%=!hasImportPermission%>"
            type="file" inlineField="true" label="" />
        <br />

        <aui:input inlineField="<%=true%>" name="idField" />
        <aui:input inlineField="<%=true%>" name="nameField" />
        <aui:input inlineField="<%=true%>" name="timestampField" />

        <aui:button name="import" type="submit" value="import"
            disabled="true" />
        <aui:button href="<%=browseURL%>" type="cancel" />

    </aui:fieldset>

</aui:form>

<div class="separator"></div>

<% for (int i=0; i<dataURLs.length; i++) { %>

    <aui:form action="<%=importMeasurementsURL%>" name='<%= "fm" + (i+2) %>'
        cssClass="import-form">
    
        <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />
    
        <aui:input name="dataURL" type="hidden" value="<%=dataURLs[i]%>" />
        <aui:input name="dataURL" disabled="<%=true%>" inlineField="true"
            value="<%=dataURLs[i]%>" />
 
        <aui:input name="idField" type="hidden" value="<%=idFields[i]%>" />
        <aui:input name="idField" disabled="<%=true%>" inlineField="true"
            value="<%=idFields[i]%>" />
    
        <aui:input name="nameField" type="hidden" value="<%=nameFields[i]%>" />
        <aui:input name="nameField" disabled="<%=true%>" inlineField="true"
            value="<%=nameFields[i]%>" />
    
        <aui:input name="timestampField" type="hidden" value="<%=timestampFields[i]%>" />
        <aui:input name="timestampField" disabled="<%=true%>" inlineField="true"
            value="<%=timestampFields[i]%>" />
    
    
        <%
            boolean isConfigured = Validator.isNotNull(dataURLs[i]);
        %>
    
        <aui:button name="import" type="submit" value="import"
            disabled="<%=!isConfigured || !hasImportPermission%>" />
        <aui:button href="<%=browseURL%>" type="cancel" />
    
    </aui:form>

<% } %>

<aui:script use="aui-base">
	var input = A.one('#<portlet:namespace />file');
	var button = A.one('#<portlet:namespace />import');

	input.on('change', function(e) {

		if (input.get('value')) {
			button.removeClass('disabled');
			button.removeAttribute('disabled');
		} else {
			button.addClass('disabled');
			button.setAttrs({
				disabled : 'disabled'
			});
		}

	});
</aui:script>

