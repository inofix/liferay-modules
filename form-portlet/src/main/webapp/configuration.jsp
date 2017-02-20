<%--
    configuration.jsp: Configure the form-portlet's preferences.
    
    Created:    2017-02-20 14:43 by Christian Berndt
    Modified:   2017-02-20 14:43 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm">

    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />

    <liferay-ui:panel id="formPortletGeneralSettingsPanelContainer"
        title="general-settings" extended="true">

        <aui:input helpMessage="form-title-help" label="form-title"
            name="preferences--formTitle--" value="<%=formTitle%>" />
        <aui:input helpMessage="description-help" label="description"
            name="preferences--description--" type="textarea"
            value="<%=description%>" />
        <aui:input helpMessage="submit-label-help" label="submit-label"
            name="preferences--submitLabel--" value="<%=submitLabel%>" />
        <aui:input helpMessage="success-target-help"
            label="success-target" name="preferences--successTarget--"
            value="<%=successTarget%>" />

    </liferay-ui:panel>
    
    <liferay-ui:panel id="formPortletFieldSettingsPanelContainer"
        title="field-settings" extended="true">

        <aui:input helpMessage="order-id-help"
            label="order-id" name="preferences--orderId--"
            value="<%=orderId%>" />
            
        <aui:input helpMessage="currency-help"
            label="currency" name="preferences--currency--"
            value="<%=currency%>" />            
            
    </liferay-ui:panel>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>
