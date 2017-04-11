<%--
    latest.jsp: Display the latest state of the configured channels
    
    Created:    2017-04-11 17:45 by Christian Berndt
    Modified:   2017-04-11 23:52 by Christian Berndt
    Version:    1.0.1
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
%>

<aui:row>
    <%
        int i = 0;
        for (TermCollector termCollector : channelNameTermCollectors) {
            
            Hits hits = MeasurementLocalServiceUtil.search(
                    themeDisplay.getCompanyId(), scopeGroupId, null,
                    termCollector.getTerm(), 0, new Date().getTime(),
                    true, 0, 1, null);
            
            Document document = hits.toList().get(0); 
            
            channelName = termCollector.getTerm();
            String timestamp = document.get("timestamp"); 
            until = GetterUtil.getLong(document.get("date_sortable"));
            from = until - 1000 * 60 * 60 * 24; 
            
            
            PortletURL graphURL = renderResponse.createRenderURL(); 
            graphURL.setParameter("tabs1", "chart"); 
            graphURL.setParameter("channelName", channelName);
            graphURL.setParameter("from", String.valueOf(from));
            graphURL.setParameter("until", String.valueOf(until)); 
    %>
        <aui:col span="3">
            <div class="display">
                <div class="name">
                    <%= channelName %>
                </div> 
                <a href="<%= graphURL.toString() %>">          
                    <span class="face">
                        <span class="value"><%= document.get("value") %></span>
                        <span class="unit"><%= document.get("unit") %></span> 
                    </span>
                </a> 
                <div class="caption">
                    <%= timestamp %> 
                </div>
            </div>
        </aui:col>
        
        <c:if test="<%= i > 0 && (i+1)%4 == 0 %>">
            <%= "</div><div class='row-fluid'>" %>
        </c:if>    
    <%
            i++;
        }
    %>
</aui:row>