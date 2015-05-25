<%--
    configuration.jsp: Configure the contact-manager's preferences.
    
    Created:    2015-05-25 11:36 by Christian Berndt
    Modified:   2015-05-25 17:51 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.Arrays"%>

<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>

<%
	PortletURL portletURL = renderResponse.createRenderURL();
	ContactSearch searchContainer = new ContactSearch(renderRequest,
			portletURL);
	List<String> headerNames = searchContainer.getHeaderNames();

	List<KeyValuePair> selectedColumns = new ArrayList<KeyValuePair>();
	for (String column : columns) {
		selectedColumns.add(new KeyValuePair(column, column));
	}

	Arrays.sort(columns); 
	
	List<KeyValuePair> availableColumns = new ArrayList<KeyValuePair>();
	for (String headerName : headerNames) {
        if (Arrays.binarySearch(columns, headerName) < 0) {
			availableColumns.add(new KeyValuePair(headerName,
					headerName));
		}
	}
%>

<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
	var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm"
	onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
	
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />
	<aui:input name="redirect" type="hidden"
		value="<%=configurationRenderURL%>" />
		
    <aui:input name="columns" type="hidden" value="" />


	<aui:fieldset label="columns" helpMessage="columns-help">
		<liferay-ui:input-move-boxes rightList="<%=availableColumns%>"
			rightTitle="available" leftBoxName="selectedColumns"
			leftList="<%=selectedColumns%>" rightBoxName="availableColumns"
			leftTitle="current" leftReorder="true" />
	</aui:fieldset>

	<aui:fieldset label="miscellaneous">
		<aui:field-wrapper inlineLabel="true" label="show-death-date"
			helpMessage="show-death-date-help">
			<aui:input name="showDeathDate" type="radio"
				value="true" checked="<%= showDeathdate %>" label="yes"/>
			<aui:input name="showDeathDate" type="radio"
				value="false" checked="<%= !showDeathdate %>" label="no" />
		</aui:field-wrapper>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>

</aui:form>

<aui:script>
    Liferay.provide(
        window,
        '<portlet:namespace />saveConfiguration',
        function() {
            document.<portlet:namespace />fm.<portlet:namespace />columns.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedColumns);
            submitForm(document.<portlet:namespace />fm);
        },
        ['liferay-util-list-fields']
    );
</aui:script>
