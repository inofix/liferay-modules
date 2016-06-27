<%--
    geotag.jsp: A custom section for the journal edit-form 
                which provides geo tagging functionality.
    
    Created:    2016-06-21 13:48 by Christian Berndt
    Modified:   2016-06-27 13:13 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/portlet/journal/init.jsp" %>

<h3><liferay-ui:message key="geotag" /></h3>

<%
    String geotagJS = PropsUtil.get("inofix.hook.geotag.geotag.js");
    String leafletCSS = PropsUtil.get("inofix.hook.geotag.leaflet.css");
    String leafletJS = PropsUtil.get("inofix.hook.geotag.leaflet.js");
    String leafletEditableJS = PropsUtil.get("inofix.hook.geotag.leaflet.editable.js");
    String tilesAttribution = PropsUtil.get("inofix.hook.geotag.tiles.attribution");
    String tilesURL = PropsUtil.get("inofix.hook.geotag.tiles.url");
%>

<link rel="stylesheet" href="<%= leafletCSS %>" />

<style>
    .map {
        height: 70vh;
    }
</style>

<div id="<portlet:namespace />Map" class="map"></div>

<script src="<%= leafletJS %>"></script>
<script src="<%= leafletEditableJS %>"></script>

<script>

    var center = [52.51733, 13.38886];
    var geoJSON = null;
    var zoom = 16;
    var map = L.map('<portlet:namespace />Map', {
        editable: true
    }).setView(center, zoom);
    var mapState = null;
    
    L.tileLayer('<%= tilesURL %>', {
        attribution: '<%= tilesAttribution %>'
    }).addTo(map);
    
    var geoJSONInputs = document.getElementsByName("<portlet:namespace />ExpandoAttribute--geoJSON--");
    var mapStateInputs = document.getElementsByName("<portlet:namespace />ExpandoAttribute--mapState--");
    
    function clearCustomFields() {
        
        if (geoJSONInputs.length > 0) {
            geoJSONInputs[0].value = "";
        }
        
        if (mapStateInputs.length > 0) {
            mapStateInputs[0].value = "";
        }
    }
    
    function restore() {
                
        if (geoJSONInputs.length > 0) {
            geoJSON = JSON.parse(geoJSONInputs[0].value);
        }
        
        if (mapStateInputs.length > 0) {
            mapState = JSON.parse(mapStateInputs[0].value);
        }
        
        if (geoJSON) {

            var coordinates = [];
            var type = "";

            if (geoJSON.geometry) {
                coordinates = geoJSON.geometry.coordinates;
                type = geoJSON.geometry.type;
            }

            if (type == 'LineString') {

                var latLngs = [];

                // coords in geojson are stored as [longitude,latitude]
                // as opposed to leaflet, which expects points in as
                // [latitude, longitude] array.
                for (i = 0; i < coordinates.length; i++) {
                    var latLng = [];
                    latLng[0] = coordinates[i][1];
                    latLng[1] = coordinates[i][0];
                    latLngs.push(latLng);
                }

                var polyline = L.polyline(latLngs).addTo(map);
                polyline.enableEdit();

            }

            if (type == 'Point') {

                // coords in geojson are stored as [longitude,latitude]
                // as opposed to leaflet, which expects points in as
                // [latitude, longitude] array.
                var latLng = [];

                latLng[0] = coordinates[1];
                latLng[1] = coordinates[0];

                var marker = L.marker(latLng).addTo(map);
                marker.enableEdit();
            }
            if (type == 'Polygon') {

                var latLngs = [];

                // coords in geojson are stored as [longitude,latitude]
                // as opposed to leaflet, which expects points in as
                // [latitude, longitude] array.
                for (i = 0; i < coordinates[0].length; i++) {
                    var latLng = [];
                    latLng[0] = coordinates[0][i][1];
                    latLng[1] = coordinates[0][i][0];
                    latLngs.push(latLng);
                }

                var polygon = L.polygon(latLngs).addTo(map);
                polygon.enableEdit();

            }
        }
                
        if (mapState) {
                        
            center = mapState.center;
            zoom = mapState.zoom;
            
            map.setView(center, zoom);          
        }
    }    
    
    function updateCustomFields(e) {
                
        if (e.layer instanceof L.Marker) {
            geoJSON = e.layer.toGeoJSON();
        }
        if (e.layer instanceof L.Path) {
            geoJSON = e.layer.toGeoJSON();
        }
                
        if (geoJSONInputs.length > 0) {
            geoJSONInputs[0].value = JSON.stringify(geoJSON);
        }
        
        mapState = {"center": map.getCenter(), "zoom": map.getZoom()}
        
        mapStateInputs[0].value = JSON.stringify(mapState);
    }
    
    restore(); 
 
</script>

<aui:script>
    // Reload the map when the user navigates the form-navigator
    AUI().ready('aui-event', function (A) {
    
        // Wait until the tabView has been created.
        setTimeout(function () {
            var geoTagTabs = Liferay.component('<portlet:namespace />fm1Tabview');
            
            if (geoTagTabs) {
                geoTagTabs.after('selectionChange',function(event) {
                     map.invalidateSize();
                });   
            }    
        }, 1000);
    });
</aui:script>

<script src="<%= geotagJS %>"></script>
