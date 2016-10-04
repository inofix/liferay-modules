<%--
    configuration.jsp: Configure the social-media portlet's preferences.
    
    Created:    2016-10-04 21:39 by Christian Berndt
    Modified:   2016-10-04 21:39 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%
    PortletURL portletURL = renderResponse.createRenderURL();
%>

<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm">

    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />

    <liferay-ui:panel-container id="sliderSettingsPanelContainer"
        persistState="<%=true%>">

        <liferay-ui:panel id="sliderServicesPanel" title="services"
            extended="true">

            <aui:input name="structureId" value="<%=structureId%>"
                helpMessage="structure-id-help" />

        </liferay-ui:panel>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>