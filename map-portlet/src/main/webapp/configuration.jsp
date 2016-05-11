<%--
    configuration.jsp: Configure the map-portlet's preferences.
    
    Created:    2016-03-01 23:47 by Christian Berndt
    Modified:   2016-05-11 20:54 by Christian Berndt
    Version:    1.1.4
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

<aui:form action="<%=configurationActionURL%>" enctype="multipart/form-data" method="post" name="fm">
    
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />
        
    <liferay-ui:panel-container id="mapportletSettingsPanelContainer" persistState="<%= true %>">

        <liferay-ui:panel id="mapportletMapSettingsPanel" title="map-settings" extended="true">
    
            <aui:input name="mapCenter" value="<%= mapCenter %>" 
                helpMessage="map-center-help"/>
                
            <aui:input name="mapHeight" value="<%= mapHeight %>"
                helpMessage="map-height-help"/>
                 
            <aui:select name="mapZoom" helpMessage="map-zoom-help">
            <% for (int i=0; i<18; i++) { %>
                <aui:option value="<%= i %>" selected="<%= i == GetterUtil.getInteger(mapZoom) %>"><%= i %></aui:option>
            <% } %>
            </aui:select>
            
            <aui:input name="tilesURL" cssClass="tiles-url"
                helpMessage="tiles-url-help" label="tiles-url"
                value="<%= tilesURL %>"/>
                
            <aui:input name="tilesCopyright" cssClass="tiles-copyright" 
                helpMessage="tiles-copyright-help" value="<%= tilesCopyright %>"/>   
                        
        </liferay-ui:panel>
        
        <liferay-ui:panel id="mapportletMarkersPanel" title="markers" extended="true">
        
			<aui:fieldset>
                <aui:input name="file" type="file" inlineField="true"
                    label="import-markers-from-file" inlineLabel="true"
                    helpMessage="import-markers-from-file-help" />
            </aui:fieldset>
        
			<aui:fieldset id="marker" cssClass="marker">
			<%
			    for (int i=0; i<markerLabels.length; i++) {
			%>
			    <aui:container>
			
			        <aui:row>
			            <div class="lfr-form-row">

			                <legend class="fieldset-legend">
			                
			                    <span class="sort-handle"></span>

			                </legend>           
			            
			                <div class="row-fields">
			                                                    
			                    <aui:col span="6">
			                    
			                        <aui:input name="markerLocations"
			                            cssClass="marker-location"
			                            helpMessage="marker-location-help"
			                            label="marker-location"
			                            value="<%= markerLocations[i] %>" />
                                        
			                    </aui:col>
			                    <aui:col span="6">
			                    
			                        <%
			                             String label = ""; 
			                             if (markerLabels.length > i) {
			                                 label = markerLabels[i]; 
			                             }
			                        %>
			                    
                                    <aui:input name="markerLabels"
                                        cssClass="marker-label"
                                        helpMessage="marker-label-help"
                                        label="marker-label"
                                        value="<%= label %>" />
                                        
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
    
            <aui:input name="claim" cssClass="claim" helpMessage="claim-help" 
                value="<%= claim %>"/>
                
            <aui:input name="addressResolverURL" cssClass="address-resolver-url"
                helpMessage="address-resolver-url-help" label="address-resolver-url"
                value="<%= addressResolverURL %>"/>
                
            <aui:input name="markerIconConfig" type="textarea" value="<%= markerIconConfig %>"
                helpMessage="marker-icon-config-help" />
                
            <aui:input name="useDivIcon" type="checkbox" checked="<%= useDivIcon %>" 
                helpMessage="use-div-icon-help"/>  
                
            <aui:input name="showTable" type="checkbox" checked="<%= showTable %>" 
                helpMessage="show-table-help"/>                 
                       
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
