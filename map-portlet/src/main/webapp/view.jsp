<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-03-03 10:43 by Christian Berndt
    Version:    1.0.2
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
	
	<% for (int i=0; i<markerLatLongs.length; i++) { %>
	
		L.marker(<%= markerLatLongs[i] %>).addTo(map)
            .bindPopup('<%= markerLabels[i] %>');
		    // .bindPopup('<%= markerLabels[i] %>').openPopup();
	
	<% } %>
</script>