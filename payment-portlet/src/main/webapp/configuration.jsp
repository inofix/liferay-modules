<%--
    configuration.jsp: Configure the payment-portlet's preferences.
    
    Created:    2017-02-16 17:45 by Christian Berndt
    Modified:   2017-02-21 19:38 by Christian Berndt
    Version:    1.0.3
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

    <liferay-ui:panel-container
        id="paymentportletSettingsPanelContainer"
        persistState="<%=true%>">
        
        <liferay-ui:panel id="paymentportletCommonParametersPanelContainer"
            title="common-parameters" extended="true">
    
            <aui:row>
                <aui:col span="4">
                    <aui:input name="apiKey" value="<%=apiKey%>"
                        helpMessage="api-key-help" />                    
                    <aui:input name="merchantName" value="<%=merchantName%>"
                        helpMessage="merchant-name-help" />
                    <aui:select helpMessage="default-country-help"
                        name="defaultCountry">
                        <%
                            while (countryIterator.hasNext()) {
                                String countryName = countryIterator.next();
                                String countryCode = countryMap.get(
                                        countryName).getCountry();
                        %>
                        <aui:option label="<%=countryName%>"
                            selected="<%=defaultCountry.equals(countryCode)%>"
                            value="<%=countryCode%>" />
                        <%
                            }
                        %>
                    </aui:select>
                </aui:col>
                <aui:col span="4">
                    <aui:input name="currency" value="<%=currency%>"
                        helpMessage="currency-help" />
    
                    <aui:input name="vat" value="<%=vat%>"
                        helpMessage="vat-help" />
                </aui:col>
                
                <aui:col span="4">
                    <aui:input helpMessage="show-duration-help"
                        name="showDuration" type="checkbox"
                        value="<%=showDuration%>" />
                    <aui:input helpMessage="show-locale-help"
                        name="showLocale" type="checkbox"
                        value="<%=showLocale%>" />
                    <aui:input
                        helpMessage="show-original-transaction-id-help"
                        name="showOriginalTransactionId" type="checkbox"
                        value="<%=showOriginalTransactionId%>" />
                    <aui:input helpMessage="show-recurring-help" name="showRecurring"
                        type="checkbox" value="<%=showRecurring%>" />
                    <aui:input helpMessage="show-shipping-costs-help"
                        name="showShippingCosts" type="checkbox"
                        value="<%=showShippingCosts%>" />
                </aui:col>
            </aui:row>
        </liferay-ui:panel>

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

        <liferay-ui:panel id="paymentportletDisplaySettingsPanel"
            title="display-settings" extended="true">
            <aui:field-wrapper name="tabsOrientation">
                <aui:input label="default" name="tabOrientation" type="radio"
                    value="default"
                    checked='<%="default".equals(tabOrientation)%>' />
                <aui:input label="left" name="tabOrientation" type="radio"
                    value="left"
                    checked='<%="left".equals(tabOrientation)%>' />
                <aui:input label="right" name="tabOrientation" type="radio"
                    value="right"
                    checked='<%= "right".equals(tabOrientation) %>' />
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
            document.<portlet:namespace />fm.<portlet:namespace />services.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedServices);
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
