<%--
    toolbar.jsp: The toolbar of the data portlet
    
    Created:    2017-03-23 15:18 by Christian Berndt
    Modified:   2017-04-04 11:30 by Christian Berndt
    Version:    1.0.6
 --%>

<%@ include file="/html/init.jsp"%>

<%@page import="java.util.Collections"%>

<%@page import="com.liferay.portal.kernel.search.facet.collector.TermCollector"%>
<%@page import="com.liferay.portal.kernel.search.facet.collector.FacetCollector"%>
<%@page import="com.liferay.util.PropertyComparator"%>

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
%>

<aui:nav-bar>

    <aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons">
    
        <liferay-portlet:renderURL varImpl="searchURL" />
    
        <aui:form action="<%= searchURL %>" name="fm1">
        
            <aui:input name="tabs1" type="hidden" value="<%= tabs1 %>"/>
            
            <aui:select label="" name="channelName" inlineField="true" onChange='<%= renderResponse.getNamespace() + "select();" %>'>
                <aui:option value="" label="select-channel"/>
                <c:forEach items="<%=channelNameTermCollectors%>" var="termCollector">
                    <aui:option value="${termCollector.term}"
                        label="${termCollector.term} (${termCollector.frequency})" />
                </c:forEach>
            </aui:select>
            
            <aui:field-wrapper inlineField="<%= true %>" inlineLabel="true" name="from">
            
                <liferay-ui:input-date name="from"          
                    nullable="<%= fromDate == null %>" 
                    dayParam="fromDateDay"
                    dayValue="<%=fromDateDay%>"
                    monthParam="fromDateMonth"
                    monthValue="<%=fromDateMonth%>"
                    yearParam="fromDateYear"
                    yearValue="<%=fromDateYear%>" />

            </aui:field-wrapper>
            
            <aui:field-wrapper inlineField="<%= true %>" inlineLabel="true" name="until" >
            
                <liferay-ui:input-date name="until"                
                    disabled="<%= true %>"
                    nullable="<%= untilDate == null %>" 
                    dayParam="untilDateDay"
                    dayValue="<%=untilDateDay%>"
                    monthParam="untilDateMonth"
                    monthValue="<%=untilDateMonth%>"
                    yearParam="untilDateYear"
                    yearValue="<%=untilDateYear%>" />
                    
                <aui:input name="untilDateDay" type="hidden" value="<%= untilDateDay %>"/>
                <aui:input name="untilDateMonth" type="hidden" value="<%= untilDateMonth %>"/>
                <aui:input name="untilDateYear" type="hidden" value="<%= untilDateYear %>"/>

            </aui:field-wrapper>
                        
            <portlet:renderURL var="clearURL" />
            
            <aui:button-row cssClass="pull-right">
                <aui:button type="submit" value="search"/>
                <aui:button value="reset" href="<%=clearURL%>" cssClass="clear-btn" />
            </aui:button-row>
            
        </aui:form>
        
    </aui:nav>
    
</aui:nav-bar>

<aui:script>
    function <portlet:namespace />select() {
        submitForm(document.<portlet:namespace />fm1);
    }
</aui:script>

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
