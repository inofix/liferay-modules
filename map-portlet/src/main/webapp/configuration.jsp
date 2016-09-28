<%--
    configuration.jsp: Configure the map-portlet's preferences.
    
    Created:    2016-03-01 23:47 by Christian Berndt
    Modified:   2016-09-28 18:24 by Christian Berndt
    Version:    1.2.6
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
            
            <aui:input name="tilesURL" cssClass="full-width"
                helpMessage="tiles-url-help" label="tiles-url"
                value="<%= tilesURL %>"/>
                
            <aui:input name="tilesCopyright" cssClass="full-width" 
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
                                        cssClass="full-width"
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
                                        cssClass="full-width"
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
        
        <liferay-ui:panel id="mapportletFiltersPanel" title="filters" extended="true">
        
            <aui:fieldset id="filter" cssClass="filter">
            <%
                for (int i=0; i<filterColumns.length; i++) {
            %>
                <aui:container>
            
                    <aui:row>
                        <div class="lfr-form-row">

                            <legend class="fieldset-legend">
                            
                                <span class="sort-handle"></span>

                            </legend>           
                        
                            <div class="row-fields">
                            
                                <aui:col span="3">
                                
                                    <aui:input name="filterColumns"
                                        helpMessage="filter-column-help"
                                        label="filter-column"
                                        value="<%= filterColumns[i] %>" />
                                        
                                </aui:col>   
                                
                                                                
                                <aui:col span="3">
                                
                                    <aui:input name="filterDataURLs"
                                        helpMessage="filter-data-url-help"
                                        label="filter-data-url"
                                        value="<%= filterDataURLs[i] %>" />
                                        
                                </aui:col>
                                
                                <aui:col span="3">
                                
                                    <aui:input name="labelValueMappings"
                                        helpMessage="label-value-mapping-help"
                                        label="label-value-mapping"
                                        type="textarea"
                                        value="<%= labelValueMappings[i] %>" />
                                        
                                </aui:col>
                                                              
                                <aui:col span="3">
                                
                                    <aui:input name="filterPlaceholders"
                                        helpMessage="filter-placeholder-help"
                                        label="filter-placeholder"
                                        value="<%= filterPlaceholders[i] %>" />
                                        
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
        
        <liferay-ui:panel id="mapportletDataTablePanel" title="data-table" extended="true">                
                
            <aui:input name="showTable" type="checkbox" checked="<%= showTable %>" 
                helpMessage="show-table-help"/>  
                
            <aui:input name="locationsURL" cssClass="full-width" label="locations-url"
                helpMessage="locations-url-help" 
                value="<%= locationsURL %>"/>
                
            <%-- 
            <aui:input name="filter1DataURL" cssClass="full-width" label="filter-1-data-url"
                helpMessage="filter-1-data-url-help" 
                value="<%= filter1DataURL %>"/> 
                
            <aui:input name="filter1Values" type="textarea" 
                helpMessage="filter-1-values-help" 
                value="<%= filter1Values %>"/>                                  

            <aui:input name="labelValueMapping" type="textarea" 
                helpMessage="label-value-mapping-help" 
                value="<%= labelValueMapping %>"/> 
                
                --%>
                
            <aui:input name="customAddressAndLabel" type="textarea" 
                helpMessage="custom-address-and-label-help" 
                value="<%= customAddressAndLabel %>"/>                                                                     
 
            <aui:input name="customLatLon" type="textarea" 
                helpMessage="custom-lat-lon-help" 
                value="<%= customLatLon %>"/>                                                                     
 
            <aui:input name="dataTableColumns" type="textarea" 
                helpMessage="data-table-columns-help" 
                value="<%= dataTableColumns %>"/> 
                                      
            <aui:input name="dataTableColumnDefs" type="textarea" 
                helpMessage="data-table-column-defs-help" 
                value="<%= dataTableColumnDefs %>"/> 
                               
            <aui:input name="useAddressResolver" type="checkbox" 
                checked="<%= useAddressResolver %>" 
                helpMessage="use-address-resolver-help"/>                 
                
            <aui:input name="dataTablePaging" type="checkbox" value="<%= dataTablePaging %>"
                checked='<%= "true".equals(dataTablePaging) %>' 
                helpMessage="data-table-paging-help" />                 
                                      
        </liferay-ui:panel>        
        
        <liferay-ui:panel id="mapportletMiscellaneousPanel" title="miscellaneous" extended="true">
            
            <aui:field-wrapper name="claim" helpMessage="claim-help">
	            <liferay-ui:input-localized name="claim" xml="<%= claim %>"/>  
            </aui:field-wrapper>        
                
            <aui:input name="addressResolverURL" cssClass="full-width"
                helpMessage="address-resolver-url-help" label="address-resolver-url"
                value="<%= addressResolverURL %>"/>
                
            <aui:input name="markerIconConfig" type="textarea" value="<%= markerIconConfig %>"
                helpMessage="marker-icon-config-help" />                
                
            <aui:input name="useDivIcon" type="checkbox" checked="<%= useDivIcon %>" 
                helpMessage="use-div-icon-help"/>  
                
            <aui:input name="useGlobalJQuery" type="checkbox" checked="<%= useGlobalJQuery %>" 
                label="use-global-jquery"
                helpMessage="use-global-jquery-help"/>

            <aui:field-wrapper name="placeholderKeyword" helpMessage="placeholder-keyword-help">
                <liferay-ui:input-localized name="placeholderKeyword"
                    xml="<%=placeholderKeyword%>" />
            </aui:field-wrapper>

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
    
    var filterAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />filter',
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
