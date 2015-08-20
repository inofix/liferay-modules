<%--
    configuration.jsp: Configure the social-media portlet's preferences.
    
    Created:    2015-08-20 13:05 by Christian Berndt
    Modified:   2015-08-20 13:05 by Christian Berndt
    Version:    1.0.0
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

    <liferay-ui:panel-container>

        <liferay-ui:panel title="services" extended="true">

            <liferay-ui:input-move-boxes rightList="<%=available%>"
                rightTitle="available" leftBoxName="selected"
                leftList="<%=selected%>" rightBoxName="available"
                leftTitle="current" leftReorder="true" />
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
        