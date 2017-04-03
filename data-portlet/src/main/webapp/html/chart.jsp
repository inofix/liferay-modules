<%--
    chart.jsp: a d3 driven chart panel for the data-portlet
    
    Created:    2017-04-01 23:15 by Christian Berndt
    Modified:   2017-04-03 19:26 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%

    String channelId = ParamUtil.getString(request, "channelId"); 
    String channelName = ParamUtil.getString(request, "channelName");
    
    long from = 0; 
    if (fromDate != null) {
        from = fromDate.getTime();
    }
    
    Sort sort = new Sort("date_sortable", true);

    long until = 0; 
    if (untilDate != null) {
        until = untilDate.getTime();
    } 
    
    Hits hits = MeasurementLocalServiceUtil.search(
            themeDisplay.getCompanyId(), scopeGroupId, channelId,
            channelName, from, until, false, 0, Integer.MAX_VALUE,
            sort);
%>

<liferay-ui:app-view-toolbar               
    includeDisplayStyle="<%=true%>">
    
    <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/toolbar.jsp" />   
                
</liferay-ui:app-view-toolbar> 

<style>

.bar {
  fill: steelblue;
}

.bar:hover {
  fill: brown;
}

.axis--x path {
  display: none;
}

</style>

<svg width="960" height="500"></svg>

<portlet:resourceURL id="getJSON" var="getJSONURL">
    <portlet:param name="channelName" value="<%=channelName%>" />
    <portlet:param name="frequency" value="<%=String.valueOf(frequency)%>" />
    <portlet:param name="from" value="<%=String.valueOf(from)%>" />
    <portlet:param name="interval" value="<%=String.valueOf(interval)%>" />
    <portlet:param name="until" value="<%=String.valueOf(until)%>" />
</portlet:resourceURL>


<script>
var svg = d3.select("svg"),
    margin = {top: 20, right: 20, bottom: 30, left: 40},
    width = +svg.attr("width") - margin.left - margin.right,
    height = +svg.attr("height") - margin.top - margin.bottom;

var x = d3.scaleTime().range([0, width]);
var y = d3.scaleLinear().rangeRound([height, 0]);

var g = svg.append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    
var parseTime = d3.timeParse("%Y-%m-%dT%H:%M:%S");

var formatTime = d3.timeFormat("%H:%M");

d3.json("<%= getJSONURL %>", function(error, data) {
    if (error) throw error;
    
//     console.log(data);
    
    var unit = ""; 
    if (data && data.length > 0) {
        unit = data[0].channelUnit; 
    }
    
//    console.log("unit = " + unit); 

    // format the date
    data.forEach(function(d) {
        d.timestamp = parseTime(d.timestamp);
    });
    
    var minDate = d3.min(data, function(d) {return d.timestamp}); 
    var maxDate = d3.max(data, function(d) {return d.timestamp});
    
    console.log("minDate = " + minDate); 
    console.log("maxDate = " + maxDate); 
        
    x.domain([minDate, maxDate]);
    y.domain([0, d3.max(data, function(d) { return d.value; })]);
    
    g.append("g")
        .attr("class", "axis axis--x")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x))
    ;

    g.append("g")
        .attr("class", "axis axis--y")
        .call(d3.axisLeft(y).ticks(10, ""))
    ;

    g.selectAll(".bar")
        .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function(d) { return x(d.timestamp); })
            .attr("y", function(d) { return y(d.value); })
            .attr("width", (width / data.length) - 1)
            .attr("height", function(d) { return height - y(d.value); })
    ;
});

</script>
