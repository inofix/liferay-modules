<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-05-04 21:25 by Christian Berndt
    Version:    1.0.5
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

<script type="text/javascript">

	var map = L.map('map').setView(<%= mapCenter %>, <%= mapZoom %>);
	
	L.tileLayer('<%= tilesURL %>', {
	    attribution: '<%= tilesCopyright %>'
	}).addTo(map);
	
	var markerIcon = new L.Icon.Default();
	
	<% if (Validator.isNotNull(markerIconConfig)) { %>
	    markerIcon = L.icon(<%= markerIconConfig %>); 
	<% } %>
	
    <% if (useDivIcon) { %>
       markerIcon = L.divIcon(); 
    <% } %>
	
	<% 
	   for (int i=0; i<markerLatLongs.length; i++) { 
	       if (Validator.isNotNull(markerLatLongs[i])) {
	%>
		L.marker(<%= markerLatLongs[i] %>, {icon: markerIcon}).addTo(map)
            .bindPopup('<%= markerLabels[i] %>');	
	<%     
	       }
	   } 
	%>
</script>
