<%--
    configuration.jsp: Configure the payment-portlet's preferences.
    
    Created:    2017-02-16 17:45 by Christian Berndt
    Modified:   2017-02-16 17:45 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%  
    List<KeyValuePair> selectedServices = new ArrayList<KeyValuePair>();
    for (String service : services) {
        selectedServices.add(new KeyValuePair(service, service));
    }
    
    Arrays.sort(services); 
    
    List<KeyValuePair> availableServices = new ArrayList<KeyValuePair>();
    for (String service : PaymentConstants.SERVICE_KEYS) {
        if (Arrays.binarySearch(services, service) < 0) {
            availableServices.add(new KeyValuePair(service,
                    service));
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

    <aui:input name="services" type="hidden" value="" />

    <liferay-ui:panel id="paymentportletApiKeyPanelContainer"
        title="api-key" extended="true">

        <aui:input name="apiKey" value="<%=apiKey%>"
            helpMessage="api-key-help" />

    </liferay-ui:panel>

    <liferay-ui:panel-container
        id="paymentportletSettingsPanelContainer"
        persistState="<%=true%>">

        <liferay-ui:panel id="paymentportletServicesPanel"
            title="services" extended="true">

            <liferay-ui:input-move-boxes
                rightBoxName="availableServices" 
                rightList="<%=availableServices%>" 
                rightTitle="available"
                leftBoxName="selectedServices"
                leftList="<%=selectedServices%>"
                leftTitle="current"
                leftReorder="true" />
        </liferay-ui:panel>
    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>

<aui:script>
    Liferay.provide(window, '<portlet:namespace />saveConfiguration',
        function() {
            document.<portlet:namespace />fm.<portlet:namespace />services.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedServices);
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
