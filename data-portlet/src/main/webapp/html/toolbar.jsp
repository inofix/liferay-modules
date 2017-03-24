<%--
    toolbar.jsp: The toolbar of the data portlet
    
    Created:    2017-03-23 15:18 by Christian Berndt
    Modified:   2017-03-23 15:18 by Christian Berndt
    Version:    1.0.0
 --%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.kernel.search.facet.collector.TermCollector"%>
<%@page import="com.liferay.portal.kernel.search.facet.collector.FacetCollector"%>

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
%>

<aui:nav-bar>

    <aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons">
    
        <liferay-portlet:renderURL varImpl="searchURL" />
    
        <aui:form action="<%= searchURL %>" name="fm1">
         
            <aui:select label="" name="channelId" inlineField="true" onChange='<%= renderResponse.getNamespace() + "selectChannelId();" %>'>
                <aui:option value="" label="any-channel"/>
                <c:forEach items="<%=channelIdTermCollectors%>" var="termCollector">
                    <aui:option value="${termCollector.term}"
                        label="${termCollector.term} (${termCollector.frequency})" />
                </c:forEach>
            </aui:select>
            
            <%-- TODO: fix indexing of channel names
            <aui:select label="" name="channelName" inlineField="true">
                <aui:option value="" label="any-name"/>
                <c:forEach items="<%=channelNameTermCollectors%>" var="termCollector">
                    <aui:option value="${termCollector.term}"
                        label="${termCollector.term} (${termCollector.frequency})" />
                </c:forEach>
            </aui:select>
            
            <aui:field-wrapper inlineField="true">
                <liferay-ui:input-date name="startDate" nullable="<%= true %>" />
                <liferay-ui:input-time name="startTime" minuteParam=""
                    amPmParam="" hourParam="" />
            </aui:field-wrapper>
            
            <aui:field-wrapper inlineField="true">
                <liferay-ui:input-date name="endDate" nullable="<%= true %>"/>
                <liferay-ui:input-time name="endTime" minuteParam="" amPmParam="" hourParam=""/> 
            </aui:field-wrapper>
            
            --%>
            
            
            <portlet:renderURL var="clearURL" />
            
            <aui:button-row cssClass="pull-right">
                <aui:button type="submit" value="search"/>
                <aui:button value="reset" href="<%=clearURL%>" cssClass="clear-btn" />
            </aui:button-row>
            
        </aui:form>
        
    </aui:nav>
    
</aui:nav-bar>

<aui:script>
    function <portlet:namespace />selectChannelId() {
        submitForm(document.<portlet:namespace />fm1);
    }
</aui:script>
