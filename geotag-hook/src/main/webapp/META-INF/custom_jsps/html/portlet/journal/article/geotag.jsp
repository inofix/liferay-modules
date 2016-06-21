<%--
    geotag.jsp: A custom section for the journal edit-form 
                which provides geo tagging functionality.
    
    Created:    2016-06-21 13:48 by Christian Berndt
    Modified:   2016-06-21 16:41 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/portlet/journal/init.jsp" %>

<h3><liferay-ui:message key="geotag" /></h3>

<%
    String leafletCSS = PropsUtil.get("inofix.hook.geotag.leaflet.css");
    String leafletJS = PropsUtil.get("inofix.hook.geotag.leaflet.js");
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

<script>
    var map = L.map('<portlet:namespace />Map').setView([51.505, -0.09], 13);
    
    L.tileLayer('<%= tilesURL %>', {
        attribution: '<%= tilesAttribution %>'
    }).addTo(map);
    
    setTimeout(function(){ 
        map.invalidateSize();  
    }, 1000); 
    
</script>   
