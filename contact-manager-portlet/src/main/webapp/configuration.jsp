<%--
    configuration.jsp: Configure the contact-manager's preferences.
    
    Created:    2015-05-25 11:36 by Christian Berndt
    Modified:   2015-10-03 14:52 by Christian Berndt
    Version:    1.0.6
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.Arrays"%>

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

    <liferay-ui:panel-container id="contactmanagerSettingsPanelContainer" persistState="<%= true %>">

		<liferay-ui:panel id="contactmanagerColumnsPanel" title="columns" extended="true">

			<liferay-ui:input-move-boxes rightList="<%=availableColumns%>"
				rightTitle="available" leftBoxName="selectedColumns"
				leftList="<%=selectedColumns%>" rightBoxName="availableColumns"
				leftTitle="current" leftReorder="true" />
		</liferay-ui:panel>

        <liferay-ui:panel id="contactmanagerAppearancePanel" title="appearance" extended="true">
        
            <aui:field-wrapper label="view-by-default" helpMessage="view-by-default-help">
                <aui:input name="viewByDefault" type="radio" value="true"
                    checked="<%= viewByDefault%>" label="yes" inlineField="true" />
                <aui:input name="viewByDefault" type="radio" value="false"
                    checked="<%=!viewByDefault%>" label="no" inlineField="true" />
			</aui:field-wrapper>
			
            <aui:input name="portraitStyle" value="<%= portraitStyle %>" helpMessage="portrait-style-help"/>
            
            <aui:select name="portraitClass" helpMessage="portrait-class-help">
                <aui:option value="img-circle" label="circle" selected='<%= "img-circle".equals(portraitClass) %>' />
                <aui:option value="img-rounded" label="rounded" selected='<%= "img-rounded".equals(portraitClass) %>' />
                <aui:option value="img-polaroid" label="polaroid" selected='<%= "img-polaroid".equals(portraitClass) %>' />
            </aui:select>
            			
        </liferay-ui:panel>
        
        <liferay-ui:panel id="contactmanagerMiscellaneousPanel" title="miscellaneous" extended="true">

            <%-- 
            <aui:row>
	            <div class="pull-left portrait-config">
					<c:if test="<%=Validator.isNotNull(portrait)%>">
	                    <aui:field-wrapper>
	                        <img src="<%=portrait%>" class="<%= portraitClass %>" style="<%= portraitStyle %>" />
	                    </aui:field-wrapper>				
	                </c:if>
	                <aui:input name="portrait" helpMessage="portrait-help"
	                    value="<%=portrait%>"/>
	   			</div>
	   			
                <div class="pull-left portrait-config">
                    <c:if test="<%=Validator.isNotNull(portraitFemale)%>">
                        <aui:field-wrapper>
                            <img src="<%=portraitFemale%>" class="<%= portraitClass %>" style="<%= portraitStyle %>" />
                        </aui:field-wrapper>
                    </c:if>
                    <aui:input name="portraitFemale" helpMessage="portrait-female-help"
                        value="<%=portraitFemale%>" />
                </div>
                
                <div class="pull-left portrait-config">
                    <c:if test="<%=Validator.isNotNull(portraitGroup)%>">
                        <aui:field-wrapper>
                            <img src="<%=portraitGroup%>" class="<%= portraitClass %>" style="<%= portraitStyle %>" />
                        </aui:field-wrapper>
                    </c:if>
                    <aui:input name="portraitGroup" helpMessage="portrait-group-help"
                        value="<%=portraitGroup%>" />
                </div>
                
                <div class="pull-left portrait-config">
                    <c:if test="<%=Validator.isNotNull(portraitMale)%>">
                        <aui:field-wrapper>
                            <img src="<%=portraitMale%>" class="<%= portraitClass %>" style="<%= portraitStyle %>" />
                        </aui:field-wrapper>
                    </c:if>
                    <aui:input name="portraitMale" helpMessage="portrait-male-help"
                        value="<%=portraitMale%>" />
                </div>
                
                <div class="pull-left portrait-config">
                    <c:if test="<%=Validator.isNotNull(portraitOrganization)%>">
                        <aui:field-wrapper>
                            <img src="<%=portraitOrganization%>" class="<%= portraitClass %>" style="<%= portraitStyle %>" />
                        </aui:field-wrapper>
                    </c:if>
                    <aui:input name="portraitOrganization" helpMessage="portrait-organization-help"
                        value="<%=portraitOrganization%>" />
                </div>
                
			</aui:row>
			--%>

			<aui:field-wrapper label="show-death-date"
				helpMessage="show-death-date-help">
				<aui:input name="showDeathDate" type="radio" value="true"
					checked="<%=showDeathdate%>" label="yes" inlineField="true" />
				<aui:input name="showDeathDate" type="radio" value="false"
					checked="<%=!showDeathdate%>" label="no" inlineField="true" />
			</aui:field-wrapper>

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
