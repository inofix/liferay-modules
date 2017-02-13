<%--
    configuration.jsp: Configure the social-media portlet's preferences.
    
    Created:    2015-08-20 13:05 by Christian Berndt
    Modified:   2017-02-13 14:39 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/html/init.jsp"%>

<%
    PortletURL portletURL = renderResponse.createRenderURL();

    List<KeyValuePair> selected = new ArrayList<KeyValuePair>();
    for (String service : selectedServices) {
        selected.add(new KeyValuePair(service, service));
    }

    Arrays.sort(selectedServices); 
    
    List<KeyValuePair> available = new ArrayList<KeyValuePair>();
    for (String service : availableServices) {
        if (Arrays.binarySearch(selectedServices, service) < 0) {
            available.add(new KeyValuePair(service, service));
        }
    }
    
    // Add variables to pagecontext in order to access them in EL-Syntax
    pageContext.setAttribute("selectedOrientation", selectedOrientation); 
    pageContext.setAttribute("selectedTheme", selectedTheme); 
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
        
    <aui:input name="services" type="hidden" value="" />

    <liferay-ui:panel-container id="socialmediaSettingsPanelContainer" persistState="<%= true %>">

        <liferay-ui:panel id="socialmediaServicesPanel" title="services" extended="true">

            <liferay-ui:input-move-boxes rightList="<%=available%>"
                rightTitle="available" leftBoxName="selected"
                leftList="<%=selected%>" rightBoxName="available"
                leftTitle="current" leftReorder="true" />
                
        </liferay-ui:panel>
        
        <liferay-ui:panel id="socialmediaServiceSettingsPanel" title="service-settings" extended="true">

            <aui:row>
                <aui:col span="3">
                    <aui:fieldset label="url">
                        <aui:input name="shareUrl" value="<%=shareUrl%>"
                            helpMessage="share-url-help" />
                    </aui:fieldset>
                </aui:col>
                <aui:col span="3">
                    <aui:fieldset label="mail">
                        <aui:input name="mailSubject"
                            value="<%=mailSubject%>"
                            helpMessage="mail-subject-help" />
                        <aui:input name="mailBody" value="<%=mailBody%>"
                            helpMessage="mail-body-help" />
                        <aui:input name="mailUrl" value="<%=mailUrl%>"
                            helpMessage="mail-url-help" />

                    </aui:fieldset>
                </aui:col>

                <aui:col span="3">
                    <aui:fieldset label="facebook">
                        <aui:input helpMessage="fb-api-key-help"
                            name="fbApiKey" value="<%=fbApiKey%>" />
                        <aui:input helpMessage="fb-caption-help"
                            name="fbCaption" value="<%=fbCaption%>" />
                        <aui:input helpMessage="fb-description-help"
                            name="fbDescription"
                            value="<%=fbDescription%>" />
                        <aui:input helpMessage="fb-display-help"
                            name="fbDisplay" value="<%=fbDisplay%>" />
                        <aui:input helpMessage="fb-from-help"
                            name="fbFrom" value="<%=fbFrom%>" />
                        <aui:input helpMessage="fb-name-help"
                            name="fbName" value="<%=fbName%>" />
                        <aui:input helpMessage="fb-picture-help"
                            name="fbPicture" value="<%=fbPicture%>" />
                        <aui:input helpMessage="fb-redirect-uri-help"
                            name="fbRedirectUri"
                            value="<%=fbRedirectUri%>" />
                        <aui:input helpMessage="fb-ref-help"
                            name="fbRef" value="<%=fbRef%>" />
                        <aui:input helpMessage="fb-source-help"
                            name="fbSource" value="<%=fbSource%>" />
                        <aui:input helpMessage="fb-to-help" name="fbTo"
                            value="<%=fbTo%>" />
                    </aui:fieldset>
                </aui:col>
                <aui:col span="3">
                    <aui:fieldset label="other">

                        <%-- Not yet implemented in view.jsp
                        <aui:input name="githubUrl" value="<%= githubUrl%>"
                            helpMessage="github-url-help" />
                        --%>

                        <aui:input name="twitterVia"
                            value="<%=twitterVia%>"
                            helpMessage="twitter-via-help" />

                    </aui:fieldset>
                </aui:col>
            </aui:row>
        </liferay-ui:panel>

		<liferay-ui:panel id="socialmediaBackenPanel" title="backend" extended="true">

			<aui:input name="backendUrl" value="<%=backendUrl%>"
				helpMessage="backend-url-help" />

		</liferay-ui:panel>

		<liferay-ui:panel id="socialmediaDisplaySettingsPanel" title="display-settings" extended="true">
		
            <aui:field-wrapper label="orientation"
                helpMessage="orientation-help">
                
                <c:forEach items="<%= availableOrientations %>" var="orientation">

	                <aui:input name="orientation" type="radio" value="${orientation}"
	                    label="${orientation}" inlineLabel="true" inlineField="true"
	                    checked="${selectedOrientation==orientation}" />

				</c:forEach>
				
            </aui:field-wrapper>
            
            <aui:field-wrapper label="theme"
                helpMessage="theme-help">
                
                <c:forEach items="<%= availableThemes %>" var="theme">

                    <aui:input name="theme" type="radio" value="${theme}"
                        label="${theme}" inlineLabel="true" inlineField="true"
                        checked="${selectedTheme==theme}" />

                </c:forEach>
                
            </aui:field-wrapper>

			<aui:field-wrapper label="show-build-info"
				helpMessage="show-build-info-help">

				<aui:input name="showBuildInfo" type="radio" value="false"
					label="no" inlineLabel="true" inlineField="true"
					checked="<%=!showBuildInfo%>" />
					
				<aui:input name="showBuildInfo" type="radio" value="true"
					label="yes" inlineLabel="true" inlineField="true"
					checked="<%=showBuildInfo%>" />

			</aui:field-wrapper>
			
            <aui:field-wrapper label="use-container"
                helpMessage="use-container-help">

                <aui:input name="useContainer" type="radio" value="false"
                    label="no" inlineLabel="true" inlineField="true"
                    checked="<%=!useContainer%>" />
                    
                <aui:input name="useContainer" type="radio" value="true"
                    label="yes" inlineLabel="true" inlineField="true"
                    checked="<%=useContainer%>" />

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
            document.<portlet:namespace />fm.<portlet:namespace />services.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selected);
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
        