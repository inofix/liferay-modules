/**
 * main.js: Functions used by the map-portlet.
 * 
 * Created: 	2016-03-02 21:55 by Christian Berndt
 * Modified: 	2016-03-02 21:55 by Christian Berndt
 * Version: 	1.0.0
 */

/**
 * 
 * @param event
 * @since 1.0.0
 */
function restoreOriginalNames(event) {
	
    // liferay-auto-fields by default adds index numbers
    // to the cloned row's inputs which is here undone.
    var row = event.row;
    var guid = event.guid;

    var inputs = row.all('input, select, textarea');

    inputs.each(function(item) {
        var name = item.attr('name') || item.attr('id');
        var original = name.replace(guid, '');
        item.set('name', original);
        item.set('id', original);
    });
};

/**
 * jQuery plugins
 */
/**
 * jQuery plugins
 */
$(document).ready(function() {

    var wh = $(window).height();
    var ww = $(window).width();

    var bodyOffset = 40;
    var filterHeight = 90;
    var headHeight = 73;

    // setup the datatable
    var table = $("#table").DataTable({
        "ajax" : "/map-portlet/data/locations.txt",  // load data via ajax
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
    $("#filter, #table_info, tbody h3").css("padding-left",
            margin);
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

