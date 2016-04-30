<%--
    configuration.jsp: Configuration page for the
    timetracker-portlet.

    Created:    2013-10-18 11:58 by Christian Berndt
    Modified:   2016-04-30 00:05 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp" %>
    
<%
	PortletURL portletURL = renderResponse.createRenderURL();

    TaskRecordSearch searchContainer =
        new TaskRecordSearch(renderRequest, portletURL);
    
    List<String> headerNames = searchContainer.getHeaderNames();

    List<KeyValuePair> selectedColumns = new ArrayList<KeyValuePair>();
    for (String column : columns) {
        selectedColumns.add(new KeyValuePair(column, column));
    }

    Arrays.sort(columns);

    List<KeyValuePair> availableColumns = new ArrayList<KeyValuePair>();
    for (String headerName : headerNames) {
        if (Arrays.binarySearch(columns, headerName) < 0) {
            availableColumns.add(new KeyValuePair(
                headerName, headerName));
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
        
    <liferay-ui:panel-container id="timetrackerSettingsPanelContainer" persistState="<%= true %>">
    
        <liferay-ui:panel id="timetrackerColumnsPanel" title="columns" extended="true">

            <liferay-ui:input-move-boxes rightList="<%=availableColumns%>"
                rightTitle="available" leftBoxName="selectedColumns"
                leftList="<%=selectedColumns%>" rightBoxName="availableColumns"
                leftTitle="current" leftReorder="true" />  
                
	    </liferay-ui:panel>
	    
        <liferay-ui:panel id="timetrackerMiscellaneousPanel" title="miscellaneous" extended="true">
            <aui:fieldset>
                <aui:field-wrapper label="time-format" helpMessage="time-format-help">
                    <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                        type="radio" value="<%=TimeFormat.DURATION%>"
                        checked="<%=Validator.equals(timeFormat, TimeFormat.DURATION)%>"
                        label="duration" inlineField="true"/>
        
                    <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                        type="radio" value="<%=TimeFormat.FROM_UNTIL%>"
                        checked="<%=Validator.equals(timeFormat, TimeFormat.FROM_UNTIL)%>"
                        label="from-until" inlineField="true" />
        
                </aui:field-wrapper>
            </aui:fieldset> 
            
            <aui:input name="max-length" value="<%= maxLength %>" helpMessage="max-length-help"/>
              
        </liferay-ui:panel>	    
    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>

<aui:script>
    Liferay.provide(window, '<portlet:namespace />saveConfiguration',
        function() {
            document.<portlet:namespace />fm.<portlet:namespace />columns.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedColumns);
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
