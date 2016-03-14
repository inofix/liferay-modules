<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-03-14 22:47 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>


<c:if test="<%= Validator.isNotNull(claim) %>">
	<div class="jumbotron">
	    <h1><%= claim %></h1>
	</div>
</c:if>

<div id="map" class="map"></div>

<script>

	var map = L.map('map').setView(<%= mapCenter %>, <%= mapZoom %>);
	
	L.tileLayer('<%= tilesURL %>', {
	    attribution: '<%= tilesCopyright %>'
	}).addTo(map);
	
	<% 
	   for (int i=0; i<markerLatLongs.length; i++) { 
	       if (Validator.isNotNull(markerLatLongs[i])) {
	%>
		L.marker(<%= markerLatLongs[i] %>).addTo(map)
            .bindPopup('<%= markerLabels[i] %>');	
	<%     
	       }
	   } 
	%>
</script>