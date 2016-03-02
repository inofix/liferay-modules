<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-03-02 17:57 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>

<div id="map" class="map"></div>

<script>

	var map = L.map('map').setView(<%= mapCenter %>, <%= mapZoom %>);
	
	L.tileLayer('<%= tilesURL %>', {
	    attribution: '<%= tilesCopyright %>'
	}).addTo(map);
	
// 	L.marker([51.5, -0.09]).addTo(map)
// 	    .bindPopup('A pretty CSS3 popup.<br> Easily customizable.')
// 	    .openPopup();
</script>