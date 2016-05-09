<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-05-09 23:18 by Christian Berndt
    Version:    1.0.6
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>


<c:if test="<%= showTable %>">
	<div class="row">
		<div class="span6">
			<form id="filter">
				<fieldset>
					<!-- <label>Keyword</label>-->
					<input class="keyword" type="text" placeholder="Search ...">
					<!-- <span class="help-block">Example block-level help text here.</span> -->
				</fieldset>
				<!--
		          <fieldset>
		            <button type="submit" class="btn">Search</button>
		          </fieldset>
		          -->
			</form>
			<table id="table" class="display">
				<thead>
					<tr>
						<th>Name</th>
						<th>Latitude</th>
						<th>Longitude</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="map-wrapper">
		    <div id="map" class="map"></div>
		</div>		
	</div>
</c:if>

<c:if test="<%=!showTable%>">

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
</c:if>
