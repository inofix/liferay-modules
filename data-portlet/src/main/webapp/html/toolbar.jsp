<%--
    toolbar.jsp: The toolbar of the data portlet
    
    Created:    2017-03-23 15:18 by Christian Berndt
    Modified:   2017-04-11 23:26 by Christian Berndt
    Version:    1.0.7
 --%>

<%@ include file="/html/init.jsp"%>

<%
    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    Facet channelIdFacet = new MultiValueFacet(searchContext);
    channelIdFacet.setFieldName("channelId");

    Facet channelNameFacet = new MultiValueFacet(searchContext);
    channelNameFacet.setFieldName("channelName");

    searchContext.addFacet(channelIdFacet);
    searchContext.addFacet(channelNameFacet);
    
    // remove facet attributes from context, since we need the field's index here
    searchContext.setAttribute("channelId", null); 
    searchContext.setAttribute("channelName", null);
    searchContext.setAttribute("from", 0);
    searchContext.setAttribute("until", 0);

    Indexer indexer = IndexerRegistryUtil.getIndexer(Measurement.class);
    indexer.search(searchContext);

    FacetCollector channelIdFacetCollector = channelIdFacet
            .getFacetCollector();
    List<TermCollector> channelIdTermCollectors = channelIdFacetCollector
            .getTermCollectors();
    
    FacetCollector channelNameFacetCollector = channelNameFacet
            .getFacetCollector();
    List<TermCollector> channelNameTermCollectors = channelNameFacetCollector
            .getTermCollectors();
    
    PropertyComparator termComparator = new PropertyComparator("term");
    Collections.sort(channelNameTermCollectors, termComparator);
    
    Calendar cal = Calendar.getInstance();
    
    cal.setTime(new Date(from)); 

    int fromDay = cal.get(Calendar.DAY_OF_MONTH); 
    int fromMonth = cal.get(Calendar.MONTH); 
    int fromYear = cal.get(Calendar.YEAR);   
    
    cal.setTime(new Date(until)); 
    
    int untilDay = cal.get(Calendar.DAY_OF_MONTH); 
    int untilMonth = cal.get(Calendar.MONTH); 
    int untilYear = cal.get(Calendar.YEAR); 
%>

<portlet:renderURL var="nextDayURL">
    <portlet:param name="channelName" value="<%= channelName %>"/>
    <portlet:param name="tabs1" value="<%= tabs1 %>"/>
    <portlet:param name="from" value="<%= String.valueOf(until) %>"/>
    <portlet:param name="until" value="<%= String.valueOf(until + oneDay) %>"/>    
</portlet:renderURL>

<portlet:renderURL var="previousDayURL">
    <portlet:param name="channelName" value="<%= channelName %>"/>
    <portlet:param name="tabs1" value="<%= tabs1 %>"/>
    <portlet:param name="from" value="<%= String.valueOf(from - oneDay) %>"/>
    <portlet:param name="until" value="<%= String.valueOf(from) %>"/>
</portlet:renderURL>

<liferay-portlet:renderURL varImpl="searchURL" />

<aui:form action="<%= searchURL %>" name="fm1">

    <aui:nav-bar cssClass="toolbar">
    
        <aui:nav cssClass="pull-left">
       
            <aui:input name="tabs1" type="hidden" value="<%= tabs1 %>"/>
            
            <aui:select cssClass="pull-left" label="" name="channelName" inlineField="true" onChange='<%= renderResponse.getNamespace() + "select();" %>'>
                <aui:option value="" label="select-channel"/>
                <c:forEach items="<%=channelNameTermCollectors%>" var="termCollector">
                    <aui:option value="${termCollector.term}"
                        label="${termCollector.term} (${termCollector.frequency})" />
                </c:forEach>
            </aui:select>
            
        </aui:nav>
        
        <aui:nav>
            
            <aui:button-row cssClass="prev-next">
                <aui:a href="<%= previousDayURL %>" cssClass="btn btn-default" label="previous"/>
                <liferay-ui:input-date 
                    disabled="<%= true %>"         
                    dayParam="fromDay"
                    dayValue="<%=fromDay%>"
                    monthParam="fromMonth"
                    monthValue="<%=fromMonth%>"
                    yearParam="fromYear"
                    yearValue="<%=fromYear%>" />
                    
                <liferay-ui:input-date               
                    disabled="<%= true %>"
                    dayParam="untilDay"
                    dayValue="<%=untilDay%>"
                    monthParam="untilMonth"
                    monthValue="<%=untilMonth%>"
                    yearParam="untilYear"
                    yearValue="<%=untilYear%>" />
                <aui:a href="<%= nextDayURL %>" cssClass="btn btn-default" label="next"/>
            </aui:button-row> 
            
            <aui:input name="from" type="hidden" value="<%= from %>"/>
            <aui:input name="until" type="hidden" value="<%= until %>"/>
                        
        </aui:nav>
        
        <%-- 
        <aui:nav cssClass="pull-right">  
         
            <portlet:renderURL var="clearURL" />
            
            <aui:button-row >
                <aui:button type="submit" value="search"/>
                <aui:button value="reset" href="<%=clearURL%>" cssClass="clear-btn" />
            </aui:button-row>
            
        </aui:nav>
        --%>
        
    </aui:nav-bar>

</aui:form>


<aui:script>
    function <portlet:namespace />select() {
        submitForm(document.<portlet:namespace />fm1);
    }
</aui:script>

<%-- 
<aui:script use="aui-base">

    // Because hidden fields don't fire a change event, we have to 
    // observe it's value manually.
    
    var fromDateDayInput = A.one('#<portlet:namespace />fromDateDay');
    var fromDateDay = fromDateDayInput.get('value');
    
    var fromDatePicker = A.one('#<portlet:namespace />from');
    
    fromDatePicker.after('click', function(e) {
        
        datepickerPopover = A.one('.datepicker-popover');
        
        datepickerPopover.after('click', function(e) {
            
            if (fromDateDay != fromDateDayInput.get('value')) {
                submitForm(document.<portlet:namespace />fm1);                
            }
            
        }); 
    });
    
</aui:script>
--%>
