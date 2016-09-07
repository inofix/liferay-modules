<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-09-07 18:48 by Christian Berndt
    Version:    1.2.3
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .table-wrapper {
        width: 50%; 
        float: left; 
    <c:if test="<%= !showTable %>">  
        display: none; 
    </c:if>           
    }
    
    .map {
        height: <%= mapHeight %>;
    }
    
    #table_info {
        opacity: 0;
    }
-->
</style>

<c:if test="<%= !useGlobalJQuery %>">
    <script type="text/javascript" src="/map-portlet/js/jquery-1.12.3.min.js" ></script>
</c:if>

<%
   String dataTablesTranslationURL = null; 
   String languageId = LanguageUtil.getLanguageId(request);

   if ("de_DE".equals(languageId)) {
       dataTablesTranslationURL = "/map-portlet/js/dataTables/German.json"; 
   }
%>

<div>
    <div class="table-wrapper">
        <form id="filter">
        
            <fieldset>
                <input id="keyword" class="keyword" type="text" placeholder="<liferay-ui:message key="search"/>">
        <%
           for (int i=0; i<filterColumns.length; i++) {
        %>          
                <input id="filter<%= i %>" type="text" data-value placeholder="<liferay-ui:message key="<%= filterPlaceholders[i] %>"/>">
        <%
           }
        %>
                <aui:button icon="icon-remove" cssClass="reset-button" />
            </fieldset>            
        </form>
        <table id="table" class="display">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Latitude</th>
                    <th>Longitude</th>
<%
    for (int i=0; i<filterColumns.length; i++) {
%>
                    <c:if test="<%= Validator.isNotNull(filterPlaceholders[i]) %>">
                        <th><%= filterPlaceholders[i] %></th>
                    </c:if>                 
<%
    }
%>
                </tr>
            </thead>
        </table>
    </div>
        
    <div class="map-wrapper">
        <c:if test="<%= Validator.isNotNull(claim) %>">
            <div class="jumbotron">
                <h1><%= claim %></h1>
            </div>
        </c:if>    
        <div id="map" class="map"></div>
    </div>      
</div>
    
<script type="text/javascript">
    
    /**
     * jQuery plugins
     */
    $(document).ready(function () {
        
        var wh = $(window).height();
        var ww = $(window).width(); 
        
        var margin = 0; 
        if (ww >= 1200) {
            margin = (ww - 1170)/2; 
        }
        
        if (ww < 1200 && ww > 979) {
            margin = (ww - 940)/2;            
        }
      
        var locations = [];
        
        var bodyOffset = $('#site-navigation').height();
        var filterHeight = $('#filter').height();
        var headHeight = $('#site-navigation').height();

        var resolverURL = "<%= addressResolverURL %>";
        var tableHeight = wh - (bodyOffset + filterHeight + headHeight);
        
        if (wh > ww) {
            tableHeight = (wh - (bodyOffset + headHeight)) / 2;
        }
        
        var useAddressResolver = false; 
        
        <c:if test="<%= useAddressResolver %>">  
            useAddressResolver = true; 
        </c:if>
        
        // setup the datatable
        var table = $("#table").DataTable({
            
            <c:if test="<%= Validator.isNotNull(dataTablesTranslationURL) %>">
                "language": {
                    "url": "<%= dataTablesTranslationURL %>"
                },
            </c:if> 
                
            "dom" : 'ipt',  // hide default filter and length config
            
            <c:if test="<%= Validator.isNotNull(dataTableColumns) %>">
                <%= dataTableColumns %>,
            </c:if> 
                
            <c:if test="<%= Validator.isNotNull(dataTableColumnDefs) %>">
                <%= dataTableColumnDefs %>,
            </c:if>
                
            scrollY : tableHeight,
            paging : <%= dataTablePaging %>
                
        });
        
        $( "#filter .reset-button" ).click(function() {
            $( "#keyword" ).val("");
            $( "#keyword" ).trigger( "keyup" );
    <%
        for (int i=0; i<filterColumns.length; i++) {
    %>           
             $( "#filter<%= i %>").val(""); 
    <%
        }
    %>
            table.search( "" );
            table.columns().search( "" ).draw();

        });
             
        
        <c:if test="<%= Validator.isNotNull(locationsURL) %>">
        
        loadData();

        function loadData(){
                        
            var namesRequest = $.getJSON( "<%= locationsURL %>", function( data ) { /* debug messages */ });
            var labels = []; 
            var latLons = [];
            
            namesRequest.done(function() { // if "data/cities.json" were successfully loaded ...
                            
                var data = namesRequest.responseJSON; 
                

                if (useAddressResolver) {
                    console.log('TODO: useAddressResolver');                    
                } else {
                    
                    for ( var i = 0; i < data.length; i++ ){ 
                        
                        var address = data[i].name; 
                        var label = data[i].name;
                        var latLon = data[i].latLon;
                        
                        <c:if test="<%= Validator.isNotNull(customAddressAndLabel) %>">
                            <%= customAddressAndLabel %>
                        </c:if>
                        
                        <c:if test="<%= Validator.isNotNull(customLatLon) %>">
                            <%= customLatLon %>
                        </c:if>
                                                
                        <%
                            for (int i=0; i<filterColumns.length; i++) {
                                if (Validator.isNotNull(filterColumns[i])) {
                        %>
                            var filter<%= i %> = <%= filterColumns[i] %>;
                        <%
                                }
                            }
                        %>

                        var row = {
                            "0": label
                            , "1": latLon[0]
                            , "2": latLon[1] 
                        <%
                            for (int i=0; i<filterColumns.length; i++) {
                                if (Validator.isNotNull(filterColumns[i])) {
                        %>
                            , "<%= i + 3 %>": filter<%= i %>
                        <%
                                }
                            }
                        %>                            
                        };
                       
                        table.row.add(row);
                    }
                    
                    table.draw();
                    
                }
            }); 
        }
            
        </c:if>
        
        var considerBounds = false;

        var minLat = parseFloat(-90);
        var maxLat = parseFloat(90);
        var minLong = parseFloat(-180);
        var maxLong = parseFloat(180);

        // Disable the filter form's default 
        // submit behaviour.
        $("#filter").submit(function(e){
            return false;
        });
        
        
        // Filter the table by keyword
        $("#filter .keyword").bind("keyup", function() {
        
            // Ignore and reset the map bounds 
            // when the keyword field is used.
            considerBounds = false;

            minLat = parseFloat(-90);
            maxLat = parseFloat(90);
            minLong = parseFloat(-180);
            maxLong = parseFloat(180);

            table.search( this.value ).draw();

        });
        
        // Load data for the select filters and filter the table
        <%
            for (int i=0; i<filterColumns.length; i++) {
                if (Validator.isNotNull(filterColumns[i])) {
        %>        
                $.ajax({
                    url: "<%= filterDataURLs[i] %>"
                }).done(function (data) {
                   
                    var values = [];
                                        
//                     for (i=0; i<data.length; i++) {
//                         values.push({"label": data[i].name, "value": data[i].id});
//                     }
                      
                    <%= labelValueMappings[i] %>
                    
                    values.sort(function(a, b) {
                        if (a.label > b.label) {
                            return 1;
                        } else if (a.label < b.label) {
                            return -1; 
                        } else {
                            return 0;
                        }
                    });
                                        
                    $( "#filter<%= i %>" ).autocomplete({
                        minLength: 0,
                        source: values,
                        select: function( event, ui ) {
                          
                            event.preventDefault();
                          
                            $("#filter<%= i %>").val(ui.item.label);
                            $("#filter<%= i %>").attr("data-value", ui.item.value);
                                                        
                            table.columns(<%= i + 3 %>).search(ui.item.label).draw();                            
                        } 
                        , delay: 500
                        , open: function() { $(this).attr('state', 'open'); }
                        , close: function () { $(this).attr('state', 'closed'); }                          
                    }).focus(function () {
                        if ($(this).attr('state') != 'open') {
                            $(this).autocomplete("search");
                        }
                    });                    
                }); 
        
        <%
                }
            }
        %>
        
        // Redraw the map whenever the table is searched.
        table.on("search", function () {

            locations = table.rows({search: "applied"}).data();
          
            updateMarkers(locations);

            if (!considerBounds) {

                // when the search is initiated from the 
                // filter form, fit the map to found locations.
                map.fitBounds(locationLayer.getBounds());

            }

        });
        
        table.on("draw.dt", function() {
            $("#table_info").css("padding-left", margin); 
            $("#table_info").css("opacity", "1"); 
            $("td").css("padding-left", margin); 
        });
      
        // Custom filter method which filters the 
        // locations by the map's boundaries.
        $.fn.dataTable.ext.search.push(

            function( settings, data, dataIndex ) {

                // only consider the bounds, if the search was triggered by the map
                if (considerBounds) {

                    minLat = parseFloat(map.getBounds().getSouth());
                    maxLat = parseFloat(map.getBounds().getNorth());
                    minLong = parseFloat(map.getBounds().getWest());
                    maxLong = parseFloat(map.getBounds().getEast());

                }

                var latitude = parseFloat( data[1] ) || 0;  // use data for the lat column
                var longitude = parseFloat( data[2] ) || 0; // use data for the long column

                if (( minLat <= latitude && latitude <= maxLat ) &&       // north-south
                    ( minLong <= longitude && longitude <= maxLong )) {   // east-west
                    return true;
                  }
                return false;
            }
        );
        
        
        // Map setup
        var locations = [];
        var locationLayer = L.featureGroup([]);
        
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
      
        <% if (showTable) { %> 
        
            // fit the map into the right half of the window
            $("#map").css("height", wh - bodyOffset);           
            
            if (wh > ww) {
                $("#map").css("height", tableHeight); 
                margin = 15;
            }

            $("#filter, #locations_info, tbody h3").css("padding-left", margin); 
          
            map.invalidateSize(true);
          
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
        <% } %>
      
        /** update the location markers */
        function updateMarkers(locations) {

            var markers = [];

            for (var i = 0; i < locations.length; i++) {

                var lat =  parseFloat(locations[i][1]);
                var lon =  parseFloat(locations[i][2]);


                var marker = new L.marker([lat,lon], {icon: markerIcon})
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
