<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-09-02 16:29 by Christian Berndt
    Version:    1.2.2
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
                <aui:button icon="icon-remove" onClick="clearForm();" />
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
        String filter = "filter-" + i; 
%>
                    <c:if test="<%= Validator.isNotNull(filterColumns[i]) %>">
                        <th><%= filter %></th>
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

    function clearForm() {
        $( "#keyword" ).val("");
        $( "#keyword" ).trigger( "keyup" );
<%
    for (int i=0; i<filterColumns.length; i++) {
%>           
         $( "#filter<%= i %>").val(""); 
<%
    }
%>
    }
    
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
