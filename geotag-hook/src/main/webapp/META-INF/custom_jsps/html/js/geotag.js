/**
 * geotag.js: Functions required by the geotag-hook.
 *
 * Created:     2016-06-25 14:48 by Christian Berndt
 * Modified:    2016-06-27 13:14 by Christian Berndt
 * Version:     1.0.1
 */
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

function setCookie(cname, cvalue, exdays) {

    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;

}

map.on('editable:drawing:end', updateCustomFields);
map.on('editable:drawing:move', updateCustomFields);
map.on('editable:drawing:start', clearCustomFields);
map.on('editable:drawing:start', clearLayers);
map.on('editable:vertex:dragend', updateCustomFields);

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
    updateCustomFields(e);
//     setCookie('zoom', map.getZoom(), 365);
});

map.on('dragend', function(e) {
    updateCustomFields(e);
//    setCookie('center', JSON.stringify(map.getCenter()), 365);
})
