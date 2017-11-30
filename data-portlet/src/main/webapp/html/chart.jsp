<%--
    chart.jsp: a d3 driven chart panel for the data-portlet
    
    Created:    2017-04-01 23:15 by Christian Berndt
    Modified:   2017-11-30 13:05 by Christian Berndt
    Version:    1.0.7
--%>

<%@ include file="/html/init.jsp"%>

<%
    Sort sort = new Sort(DataManagerFields.TIMESTAMP, true); 

    Hits hits = MeasurementLocalServiceUtil
            .search(themeDisplay.getCompanyId(), scopeGroupId,
                    id, null, from, until, false, 0,
                    Integer.MAX_VALUE, sort);
%>

<liferay-ui:app-view-toolbar               
    includeDisplayStyle="<%=true%>">
    
    <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/toolbar.jsp" />   
                
</liferay-ui:app-view-toolbar> 

<c:if test="<%= hits.getLength() == 0 %>">
    <div class="alert alert-info">
        <liferay-ui:message key="no-data-found"/>
    </div>
</c:if>

<c:if test="<%= hits.getLength() > 0 %>">
    <style>
        
        .axis--x path {
            display: none;
        }
        
        .bar {
            fill: steelblue;
        }
        
        .bar:hover {
            fill: brown;
        }

    </style>

    <portlet:resourceURL id="getJSON" var="getJSONURL">
        <portlet:param name="id" value="<%=id%>" />
        <portlet:param name="from" value="<%=String.valueOf(from)%>" />
        <portlet:param name="interval" value="<%=String.valueOf(interval)%>" />
        <portlet:param name="limit" value="<%=String.valueOf(limit)%>" />
        <portlet:param name="until" value="<%=String.valueOf(until)%>" />
    </portlet:resourceURL>
        
    <svg width="960" height="500"></svg>
    
    <a href="<%= getJSONURL %>" target="_blank" class="pull-right">Download JSON</a>
    
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
                
        var unit = ""; 
        if (data && data.length > 0) {
            unit = data[0].channelUnit; 
        }
            
        // format the date
        data.forEach(function(d) {
            d.timestamp = parseTime(d.timestamp);
        });
        
        var minDate = d3.min(data, function(d) {return d.timestamp}); 
        var maxDate = d3.max(data, function(d) {return d.timestamp});
            
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
                .attr("width", (width / data.length))
                .attr("height", function(d) { return height - y(d.value); })
        ;
    });
    </script>
</c:if>