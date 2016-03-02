<%--
    configuration.jsp: Configure the map-portlet's preferences.
    
    Created:    2016-03-01 23:47 by Christian Berndt
    Modified:   2016-03-02 17:57 by Christian Berndt
    Version:    1.0.7
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

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
        
    <liferay-ui:panel-container id="mapportletSettingsPanelContainer" persistState="<%= true %>">

        <liferay-ui:panel id="mapportletMapSettingsPanel" title="map-settings" extended="true">
    
            <aui:input name="mapCenter" value="<%= mapCenter %>"/>    
            <aui:input name="mapHeight" value="<%= mapHeight %>"/>    
            <aui:select name="mapZoom">
            <% for (int i=0; i<18; i++) { %>
                <aui:option value="<%= i %>" selected="<%= i == GetterUtil.getInteger(mapZoom) %>"><%= i %></aui:option>
            <% } %>
            </aui:select>
            <aui:input name="tilesCopyright" value="<%= tilesCopyright %>"/>   
            <aui:input name="tilesURL" label="tiles-url" value="<%= tilesURL %>"/>    
                        
        </liferay-ui:panel>
        
        <%-- 
        <liferay-ui:panel id="mapportletMiscellaneousPanel" title="miscellaneous" extended="true">

        </liferay-ui:panel>
        --%>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>
