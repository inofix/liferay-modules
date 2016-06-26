<%--
    geotag.jsp: A custom section for the journal edit-form 
                which provides geo tagging functionality.
    
    Created:    2016-06-21 13:48 by Christian Berndt
    Modified:   2016-06-25 14:32 by Christian Berndt
    Version:    1.0.2
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
    var geoJSON = {};
    var zoom = 16;
    var map = L.map('<portlet:namespace />Map', {
        editable: true
    }).setView(center, zoom);
    
    L.tileLayer('<%= tilesURL %>', {
        attribution: '<%= tilesAttribution %>'
    }).addTo(map);
    
    function clearGeoJSON() {
        var geoJSONInputs = document.getElementsByName("<portlet:namespace />ExpandoAttribute--geoJSON--");
        
        if (geoJSONInputs.length > 0) {
            geoJSONInputs[0].value = "";
        }
    }
    
    function updateGeoJSON(e) {
                
        if (e.layer instanceof L.Marker) {
            geoJSON = e.layer.toGeoJSON();
        }
        if (e.layer instanceof L.Path) {
            geoJSON = e.layer.toGeoJSON();
        }
        
        var geoJSONInputs = document.getElementsByName("<portlet:namespace />ExpandoAttribute--geoJSON--");
        
        if (geoJSONInputs.length > 0) {
            geoJSONInputs[0].value = JSON.stringify(geoJSON);
        }
    }
 
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
