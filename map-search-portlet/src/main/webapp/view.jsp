<%--
    view.jsp: Default view of the map-search-portlet.
    
    Created:    2016-07-21 23:10 by Christian Berndt
    Modified:   2016-08-18 15:24 by Christian Berndt
    Version:    1.1.1
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portlet.asset.service.AssetCategoryServiceUtil"%>
<%@page import="com.liferay.portlet.asset.model.AssetCategory"%>

<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>

<%
    List<AssetCategory> categories = AssetCategoryServiceUtil
            .getVocabularyRootCategories(scopeGroupId, vocabularyId, 0,
                    Integer.MAX_VALUE, null);
%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>

<c:if test="<%= !useGlobalJQuery %>">
    <script type="text/javascript" src="/map-search-portlet/js/jquery.min.js" ></script>
</c:if>

<div class="row-fluid">
    <div class="span6">
    
        <aui:form name="fm" onSubmit="event.preventDefault();">
        
            <aui:input name="keywords" label="" placeholder="search" inlineField="true"/>
        
            <c:if test="<%= vocabularyId > 0 %>">
                <aui:select name="assetCategoryId" label="" inlineField="true">
                    <aui:option label="select-category" value="0"/>
                    <% for (AssetCategory category : categories) { %>
                        <aui:option label="<%= category.getTitle(locale) %>" 
                            value="<%= category.getCategoryId() %>"/>
                    <% } %>
                </aui:select>
            </c:if>
            
            <aui:button-row cssClass="pull-right">
<%--                 <aui:button icon="icon-search" name="search"/> --%>
                <aui:button icon="icon-remove" name="clear"/>
            </aui:button-row> 
            
        </aui:form>
        
        <table id="<portlet:namespace/>table" class="display">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Latitude</th>
                    <th>Longitude</th>                
                    <th>CategoryIDs</th>                
                    <th>AssetTagNames</th>                
                </tr>
            </thead>
        </table>
    </div>
    <div class="span6">
        <div id="<portlet:namespace/>map" class="map"></div>
    </div>
</div>


<portlet:resourceURL var="resourceURL" id="search">
</portlet:resourceURL>

<%-- <a href="<%= resourceURL %>" target="_blank">Display results</a> --%>

<hr>

<ifx-util:build-info/>

<script type="text/javascript">
    
    $(document).ready(function () {
    	
        var considerBounds = false;
        var locations = [];
        
        var markers = L.markerClusterGroup();
        
        var maxLat = parseFloat(90);
        var maxLong = parseFloat(180);
        var minLat = parseFloat(-90);
        var minLong = parseFloat(-180);

        function clearForm() {
            $( "#<portlet:namespace/>keywords" ).val("");
            $( "#<portlet:namespace/>assetCategoryId" ).val("0");
            searchTable(); 
        }
        
//         $("#<portlet:namespace/>fm").submit(function(){
//             console.log('form submitted');
//         });       
        
        // Reset the search form
        $("#<portlet:namespace/>clear").bind("click", function() {
            clearForm();  
        });
        // Filter the table by keyword
        $("#<portlet:namespace/>keywords").bind("keyup", function() {
            searchTable();  
        });
        
        // Filter the table by assetCategoryId
        $("#<portlet:namespace/>assetCategoryId").bind("change", function() {
            searchTable();   
        });
        
        // Search the table by column
        function searchTable() {
        	        	
            table.columns().search('');

            // Ignore and reset the map bounds 
            // when the keyword field is used.
            considerBounds = false;

            minLat = parseFloat(-90);
            maxLat = parseFloat(90);
            minLong = parseFloat(-180);
            maxLong = parseFloat(180);
            
            var keywords = $("#<portlet:namespace/>keywords").val(); 
            var categoryId = $("#<portlet:namespace/>assetCategoryId").val(); 
            
            table.column(0).search(keywords);
            
            if (categoryId > 0) {
                table.column(3).search(categoryId); 
            }
            
            table.draw(); 
        
        }
        
        // Datatable Setup
        var table = $("#<portlet:namespace/>table")
            .on('xhr.dt', function ( e, settings, json, xhr ) {
                // console.log("resourceURL loaded");
            })
            .DataTable({
                <c:if test="<%= Validator.isNotNull(dataTableColumnDefs) %>">
                    <%= dataTableColumnDefs %>,
                </c:if> 
                <c:if test="<%= Validator.isNotNull(dataTableDom) %>">
                    "dom": '<%= dataTableDom %>',
                </c:if>                 
                "ajax": "<%= resourceURL %>"
        });
        
        // Redraw the map whenever the table is searched.
        table.on("search", function () {
                      
            // Update the locations array
            locations = table.rows({
                search: "applied"
            }).data();
                        
            updateMarkers(locations);
            
            if (!considerBounds) {
            
                // when the search is initiated from the filter form,
                // fit the map to the bounds of the clustered markers.
                if (locations.length > 0) {
                    map.fitBounds(markers.getBounds());
                }           
            }  
        });
        
        // Custom filter method which filters the 
        // locations by the map's boundaries.
        $.fn.dataTable.ext.search.push(
        		                
            function (settings, data, dataIndex) {
            	            
            // only consider the bounds, if the search was triggered by the map
            if (considerBounds) {
            
                minLat = parseFloat(map.getBounds().getSouth());
                maxLat = parseFloat(map.getBounds().getNorth());
                minLong = parseFloat(map.getBounds().getWest());
                maxLong = parseFloat(map.getBounds().getEast());
            
            }
                                    
            var latitude = parseFloat(data[2]) || 0; // use data for the lat column
            var longitude = parseFloat(data[1]) || 0; // use data for the long column
            
            if ((minLat <= latitude && latitude <= maxLat) && // north-south
                (minLong <= longitude && longitude <= maxLong)) { // east-west
                return true;
            }
                return false;
            }
            
        );
        
        // Map Setup
        var map = L.map('<portlet:namespace/>map').setView(<%= mapCenter %>, <%= mapZoom %>);
        
        L.tileLayer('<%= tilesURL %>', {
            attribution: '<%= tilesCopyright %>'
        }).addTo(map);
    
        map.on("drag", function () {
            filterByMap();
         });
    
         map.on("zoomend", function () {
            filterByMap();
         });
    
         /** redraw the table and filter by map bounds */
        function filterByMap() {
            considerBounds = true;
            table.draw();
        }    
    
        function updateMarkers(locations) {
            
            // console.log("updateMarkers");
        
            markers.clearLayers(); 
                    
            for (var i = 0; i < locations.length; i++) {
                var lat = parseFloat(locations[i][2]);
                var lon = parseFloat(locations[i][1]);
                var title = locations[i][0];
                var marker = L.marker(new L.LatLng(lat, lon), { title: title });
                marker.bindPopup(title);
                markers.addLayer(marker);
            }
            
            map.addLayer(markers);
        
        }
        
    }); 

</script>
