<%--
    view.jsp: Default view of the map-search-portlet.
    
    Created:    2016-07-21 23:10 by Christian Berndt
    Modified:   2016-07-23 16:34 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>

<style>
<!--
    .map {
        height: <%= mapHeight %>;
    }
-->
</style>

<c:if test="<%= !useGlobalJQuery %>">
    <script type="text/javascript" src="/map-search-portlet/js/jquery.min.js" ></script>
</c:if>

<div class="row-fluid">
    <div class="span6">
        <table id="<portlet:namespace/>table" class="display">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Latitude</th>
                    <th>Longitude</th>                
                </tr>
            </thead>
        </table>
    </div>
    <div class="span6">
        <div id="<portlet:namespace/>map" class="map"></div>
    </div>
</div>


<portlet:resourceURL var="resourceURL" id="search">
    <portlet:param name="className" value="<%= JournalArticle.class.getName() %>"/>
</portlet:resourceURL>

<a href="<%= resourceURL %>" target="_blank">Display results</a>

<script type="text/javascript">

$(document).ready(function () {
    
    // Datatable Setup
    var table = $("#<portlet:namespace/>table").DataTable({});
    
    // Map Setup
    var map = L.map('<portlet:namespace/>map').setView(<%= mapCenter %>, <%= mapZoom %>);
    
    L.tileLayer('<%= tilesURL %>', {
        attribution: '<%= tilesCopyright %>'
    }).addTo(map);

    var markers = L.markerClusterGroup();
    
    for (var i = 0; i < addressPoints.length; i++) {
        var a = addressPoints[i];
        var title = a[2];
        var marker = L.marker(new L.LatLng(a[0], a[1]), { title: title });
        marker.bindPopup(title);
        markers.addLayer(marker);
    }

    map.addLayer(markers);
    
}); 

</script>
