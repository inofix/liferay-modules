<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-05-13 12:28 by Christian Berndt
    Version:    1.0.9
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>

<c:if test="<%= !useGlobalJQuery %>">
    <script type="text/javascript" src="/map-portlet/js/jquery-1.11.3.min.js" ></script>
</c:if>

<c:if test="<%= showTable %>">
	<div>
		<div style="width: 50%; float: left;">
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
	
    <script type="text/javascript">
    
    /**
     * jQuery plugins
     */
    $(document).ready(function() {

        var wh = $(window).height();
        var ww = $(window).width();

        var bodyOffset = $('#site-navigation').height();
        var filterHeight = $('#filter').height();
        var headHeight = $('#site-navigation').height();
        
        // setup the datatable
        var table = $("#table").DataTable({
            
            <c:if test="<%= Validator.isNotNull(dataURL) %>">
                "ajax" : "<%= dataURL %>",  // load data via ajax    
            </c:if>
                
            "dom" : 'ipt',                               // hide default filter and length config
            "columnDefs" : [                 
            {
                "targets" : [ 1 ],  // hide the second
                "visible" : false
            }, {    
                "targets" : [ 2 ],  // and third column (lat / long)
                "visible" : false
            } ],
            scrollY : wh - (bodyOffset + filterHeight + headHeight),
            paging : false
        });   
        
        var considerBounds = false;

        var minLat = parseFloat(-90);
        var maxLat = parseFloat(90);
        var minLong = parseFloat(-180);
        var maxLong = parseFloat(180);

        // Disable the filter form's default
        // submit behaviour.
        $("#filter").submit(function(e) {
            return false;
        });

        $("#filter .keyword").bind("keyup", function() {

            // Ignore and reset the map bounds
            // when the keyword field is used.
            considerBounds = false;

            minLat = parseFloat(-90);
            maxLat = parseFloat(90);
            minLong = parseFloat(-180);
            maxLong = parseFloat(180);

            table.search(this.value).draw();

        });

        // Redraw the map whenever the table is searched.
        table.on("search", function() {

            locations = table.rows({
                search : "applied"
            }).data();

            updateMarkers(locations);

            if (!considerBounds) {

                // when the search is initiated from the
                // filter form, fit the map to found locations.
                map.fitBounds(locationLayer.getBounds());

            }

        });

        table.on("draw.dt", function() {
            $("td").css("padding-left", margin);
        });

        // Custom filter method which filters the
        // locations by the map's boundaries.
        $.fn.dataTable.ext.search.push(

        function(settings, data, dataIndex) {

            // only consider the bounds, if the search was triggered
            // by the map
            if (considerBounds) {

                minLat = parseFloat(map.getBounds().getSouth());
                maxLat = parseFloat(map.getBounds().getNorth());
                minLong = parseFloat(map.getBounds().getWest());
                maxLong = parseFloat(map.getBounds().getEast());

            }

            var latitude = parseFloat(data[1]) || 0;        // use data for the lat column
            var longitude = parseFloat(data[2]) || 0;       // use data for the long column

            if ((minLat <= latitude && latitude <= maxLat) && // north-south
                (minLong <= longitude && longitude <= maxLong)) { // east-west
                return true;
            }
            return false;
        });

        // Map setup
        var locations = [];
        var locationLayer = L.featureGroup([]);
        var map = L.map("map", {
            // maxZoom: 15, 
            center : [ 0, 0 ],
            // maxZoom: 13,
            zoom : 8
        });

        // fit the map into the right half of the window
        $("#map").css("height", wh - bodyOffset);
        var margin = 0;
        if (ww >= 1200) {
             margin = (ww - 1170) / 2;
        }
        $("#filter, #table_info, tbody h3").css("padding-left", margin);
        // $(".map-wrapper").css("margin-right", mapMargin);

        map.invalidateSize(true);

        L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
            attribution : '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors' 
        }).addTo(map);

        map.on("drag", function() {
            filterByMap();
        });

        map.on("zoomend", function() {
            filterByMap();
        });

        /** redraw the table and filter by map bounds */
        function filterByMap() {
            considerBounds = true;
            table.draw();
        }

        /** update the location markers */
        function updateMarkers(locations) {

            var markers = [];

            for (var i = 0; i < locations.length; i++) {
                var marker = new L.marker([ locations[i][1],
                        locations[i][2] ])
                        .bindPopup(locations[i][0]);
                markers.push(marker);
            }

            if (map.hasLayer(locationLayer)) {
                map.removeLayer(locationLayer);
            }

            if (markers.length > 0) {
                locationLayer = L.featureGroup(markers);
                locationLayer.addTo(map);
            }
        }
    });        
        
    </script>	
    
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
	       for (int i=0; i<markerLabels.length; i++) { 
	           
	           String markerLocation = markerLocations[i]; 
	           	           
	           if (Validator.isNotNull(markerLocation)) {
	               
	               if (markerLocation.startsWith("[")
                       && markerLocation.endsWith("]")) {
	                   
	                   // Location is configured in array syntax
	               
	    %>
	        L.marker(<%= markerLocation %>, {icon: markerIcon}).addTo(map)
	            .bindPopup('<%= markerLabels[i] %>');   
	    <%     
		           } else {
		               
	                   // Location is given via the address

		               if (Validator.isNotNull(addressResolverURL)) {
		                  
		                   String resolverURL = addressResolverURL + markerLocation;
		                   
		               %>
			               $.getJSON('<%= resolverURL %>', function( data ) {
			                   
			                   if (data[0]) {
			                   
			                       var lat = data[0].lat; 
			                       var lon = data[0].lon;
			                       
			                       L.marker([lat, lon], {icon: markerIcon}).addTo(map)
			                           .bindPopup('<%= markerLabels[i] %>');
			                       
			                   }
			               });		               
		               
		               <%
		               
		               }
		           }
	           }
           }
	    %>
         
	</script>    
</c:if>
