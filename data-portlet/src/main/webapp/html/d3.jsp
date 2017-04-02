<%--
    d3.jsp: a d3 enabled panel for the data-portlet
    
    Created:    2017-04-01 23:15 by Christian Berndt
    Modified:   2017-04-01 23:15 by Christian Berndt
    Version:    1.0.0
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

<h3>svg</h3>

<style>
svg.chart rect {
    fill: steelblue;
}

svg.chart text {
    fill: white;
    font: 10px sans-serif;
    text-anchor: end;
}
</style>

<svg class="chart"></svg>

<portlet:resourceURL id="getJSON" var="getJSONURL">
    <portlet:param name="channelName" value="<%=channelName%>" />
    <portlet:param name="from" value="<%=String.valueOf(from)%>" />
    <portlet:param name="until" value="<%=String.valueOf(until)%>" />
</portlet:resourceURL>

<script>

    //1. Code here runs first, before the download starts.
    var width = 420,
        barHeight = 20;
    
    var x = d3.scaleLinear()
        .range([0, width]);
    
    var chart = d3.select(".chart")
        .attr("width", width);
    
    d3.json("<%= getJSONURL %>", function(error, data) {
        
        //  console.log(data);
        
        // 3. Code here runs last, after the download finishes.
        x.domain([0, d3.max(data, function(d) { return d.value; })]);
        
        chart.attr("height", barHeight * data.length);
        
        var bar = chart.selectAll("g")
            .data(data)
          .enter().append("g")
            .attr("transform", function(d, i) { return "translate(0," + i * barHeight + ")"; });
        
        bar.append("rect")
            .attr("width", function(d) { return x(d.value); })
            .attr("height", barHeight - 1);
        
        bar.append("text")
            .attr("x", function(d) { return x(d.value) - 3; })
            .attr("y", barHeight / 2)
            .attr("dy", ".35em")
            .text(function(d) { return d.value; });     
    });
    
    // 2. Code here runs second, while the file is downloading.
    function type(d) {
        d.value = +d.value; // coerce to number
        return d;
    }

</script>
