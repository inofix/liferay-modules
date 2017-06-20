<%--
    import.jsp: The import panel of the data-portlet
    
    Created:    2017-03-13 12:46 by Christian Berndt
    Modified:   2017-06-20 12:53 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.data.FileFormatException"%>
<%@page import="ch.inofix.portlet.data.MeasurementXMLException"%>

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

<aui:form action="<%=importMeasurementsURL%>" enctype="multipart/form-data"
    method="post" name="fm" cssClass="import-form">

    <aui:fieldset label="import">
    
        <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />
    
        <aui:input name="file" disabled="<%= !hasImportPermission %>" type="file" inlineField="true" label="" />
    
        <aui:button name="import" type="submit" value="import"
            disabled="true" />
        <aui:button href="<%=browseURL%>" type="cancel" />

    </aui:fieldset>

</aui:form>

<div class="separator"></div>

<aui:form action="<%=importMeasurementsURL%>" name="fm1"
    cssClass="import-form">

    <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />

    <aui:input name="dataURL" type="hidden" value="<%=dataURL%>" />
    <aui:input name="dataURL" disabled="<%=true%>" inlineField="true"
        value="<%=dataURL%>" />

    <%
        boolean isConfigured = Validator.isNotNull(dataURL);
    %>

    <aui:button name="import" type="submit" value="import"
        disabled="<%=!isConfigured || !hasImportPermission%>" />
    <aui:button href="<%=browseURL%>" type="cancel" />

</aui:form>

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

