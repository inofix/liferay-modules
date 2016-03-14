<%--
    configuration.jsp: Configure the map-portlet's preferences.
    
    Created:    2016-03-01 23:47 by Christian Berndt
    Modified:   2016-03-14 21:04 by Christian Berndt
    Version:    1.1.0
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
            <aui:input name="tilesCopyright" cssClass="tiles-copyright" value="<%= tilesCopyright %>"/>   
            <aui:input name="tilesURL" cssClass="tiles-url" label="tiles-url" value="<%= tilesURL %>"/>    
                        
        </liferay-ui:panel>
        
        <liferay-ui:panel id="mapportletMarkersPanel" title="markers" extended="true">
        
			<aui:fieldset id="marker" cssClass="marker">
			<%
			    for (int i=0; i<markerLatLongs.length; i++) {
			%>
			    <aui:container>
			
			        <aui:row>
			            <div class="lfr-form-row">

			                <legend class="fieldset-legend">
			                
			                    <span class="sort-handle"></span>

			                </legend>           
			            
			                <div class="row-fields">
			                                                    
			                    <aui:col span="6">
			                    
			                        <aui:input name="markerLatLongs"
			                            cssClass="marker-lat-long"
			                            value="<%= markerLatLongs[i] %>" />

			                    </aui:col>
			                    <aui:col span="6">
			                    
                                    <aui:input name="markerLabels"
                                        cssClass="marker-label"
                                        value="<%= markerLabels[i] %>" />
                                        
                              </aui:col>
			                </div>
			            </div>
			        </aui:row>
			
			    </aui:container>
			<%
			    }
			%>
			</aui:fieldset>
        </liferay-ui:panel>
        
        <liferay-ui:panel id="mapportletMiscelaneousPanel" title="miscellaneous" extended="true">
    
            <aui:input name="claim" cssClass="claim" value="<%= claim %>"/>  
                        
        </liferay-ui:panel>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var markerAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />marker',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
    
</aui:script>
