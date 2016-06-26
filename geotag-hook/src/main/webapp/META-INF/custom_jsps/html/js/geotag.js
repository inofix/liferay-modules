/**
 * geotag.js: Functions required by the geotag-hook.
 *
 * Created:     2016-06-25 14:48 by Christian Berndt
 * Modified:    2016-06-25 14:48 by Christian Berndt
 * Version:     1.0.0
 */
restore();

L.NewLineControl = L.Control.extend({

    options : {
        position : 'topleft'
    },

    onAdd : function(map) {
        var container = L.DomUtil.create('div',
                'leaflet-control leaflet-bar'), link = L.DomUtil
                .create('a', '', container);

        link.href = '#';
        link.title = 'Create a new line';
        link.innerHTML = '/\\/';
        L.DomEvent.on(link, 'click', L.DomEvent.stop).on(link, 'click',
                function() {
                    map.editTools.startPolyline();
                });

        return container;
    }
});

L.NewPolygonControl = L.Control.extend({

    options : {
        position : 'topleft'
    },

    onAdd : function(map) {
        var container = L.DomUtil.create('div',
                'leaflet-control leaflet-bar'), link = L.DomUtil
                .create('a', '', container);

        link.href = '#';
        link.title = 'Create a new polygon';
        link.innerHTML = '▱';
        L.DomEvent.on(link, 'click', L.DomEvent.stop).on(link, 'click',
                function() {
                    map.editTools.startPolygon();
                });

        return container;
    }
});

L.NewMarkerControl = L.Control.extend({

    options : {
        position : 'topleft'
    },

    onAdd : function(map) {
        var container = L.DomUtil.create('div',
                'leaflet-control leaflet-bar'), link = L.DomUtil
                .create('a', '', container);

        link.href = '#';
        link.title = 'Add a new marker';
        link.innerHTML = '⚫';
        L.DomEvent.on(link, 'click', L.DomEvent.stop).on(link, 'click',
                function() {
                    map.editTools.startMarker({});
                });

        return container;
    }
});

map.addControl(new L.NewMarkerControl());
// map.addControl(new L.NewLineControl());
map.addControl(new L.NewPolygonControl());


function clearLayers() {
    map.eachLayer(function(layer) {
        if (layer instanceof L.Marker) {
            map.removeLayer(layer);
        }
        if (layer instanceof L.Path) {
            map.removeLayer(layer);
        }
    })
};

function deleteMarker(e) {
    if ((e.originalEvent.ctrlKey || e.originalEvent.metaKey)) {
        map.removeLayer(this);
    }
};

function deleteShape(e) {
    if ((e.originalEvent.ctrlKey || e.originalEvent.metaKey)
            && this.editEnabled()) {
        this.editor.deleteShapeAt(e.latlng);
    }
};

function getCookie(cname) {

    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function restore() {

    if (getCookie('center') != "") {
        center = JSON.parse(getCookie('center'));
    }

    if (getCookie('geojson') != "") {

        geoJSON = JSON.parse(getCookie('geojson'));
        // document.getElementById('geoJSON').value = JSON.stringify(geoJSON);

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

        if (getCookie('zoom') != "") {
            zoom = getCookie('zoom');
        }

        map.setView(center, zoom);
    }
}

function setCookie(cname, cvalue, exdays) {

    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;

}

map.on('editable:drawing:end', updateGeoJSON);
map.on('editable:drawing:move', updateGeoJSON);
map.on('editable:drawing:start', clearGeoJSON);
map.on('editable:drawing:start', clearLayers);
map.on('editable:vertex:dragend', updateGeoJSON);

map.on('layeradd', function(e) {
    if (e.layer instanceof L.Marker) {
        e.layer.on('click', L.DomEvent.stop).on('click', deleteMarker,
                e.layer);
    }
    if (e.layer instanceof L.Path) {
        e.layer.on('click', L.DomEvent.stop).on('click', deleteShape,
                e.layer);
    }
    if (e.layer instanceof L.Path)
        e.layer.on('dblclick', L.DomEvent.stop).on('dblclick',
                e.layer.toggleEdit);
});

map.on('zoomend', function(e) {
    setCookie('zoom', map.getZoom(), 365);
});

map.on('dragend', function(e) {
    setCookie('center', JSON.stringify(map.getCenter()), 365);
})
