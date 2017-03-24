<%--
    view.jsp: Default view of the data portlet.
    
    Created:    2017-03-09 19:59 by Christian Berndt
    Modified:   2017-03-23 18:34 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.CamelCaseUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String channelId = ParamUtil.getString(request, "channelId"); 
    int delta = ParamUtil.getInteger(request, "delta", 20);
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");

    int idx = ParamUtil.getInteger(request, "cur");
    String orderByCol = ParamUtil.getString(request, "orderByCol", "timestamp_sortable");
    String orderByType = ParamUtil.getString(request, "orderByType", "desc");

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("backURL", backURL);
    portletURL.setParameter("channelId", channelId);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("tabs1", tabs1);

    if (idx > 0) {
        idx = idx - 1;
    }
    int start = delta * idx;
    int end = delta * idx + delta;

    SearchContext searchContext =
        SearchContextFactory.getInstance(request);

    boolean reverse = "desc".equals(orderByType);

    Sort sort = new Sort(orderByCol, reverse);

    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(start);
    searchContext.setEnd(end);
    searchContext.setSorts(sort);
    
    Facet channelIdFacet = new MultiValueFacet(searchContext);
    channelIdFacet.setFieldName("channelId");

    searchContext.addFacet(channelIdFacet);
    
    Indexer indexer = IndexerRegistryUtil.getIndexer(Measurement.class);

    Hits hits = indexer.search(searchContext);
    List<Document> documents = hits.toList();   
%>

<liferay-ui:header backURL="<%=backURL%>" title="data-manager" />

<liferay-ui:error exception="<%=PrincipalException.class%>"
    message="you-dont-have-the-required-permissions" />

<liferay-ui:tabs names="browse,import-export" param="tabs1"
    url="<%=portletURL.toString()%>" />

<c:choose>

    <c:when test='<%=tabs1.equals("import-export")%>'>
    
        <%@include file="/html/import.jsp" %>

    </c:when>

    <c:otherwise>

        <liferay-ui:app-view-toolbar               
            includeDisplayStyle="<%=true%>">
            
            <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/toolbar.jsp" />   
                        
        </liferay-ui:app-view-toolbar>        
    
        <liferay-ui:search-container emptyResultsMessage="no-data-found" iteratorURL="<%= portletURL %>">
            <liferay-ui:search-container-results
                results="<%=documents%>"
                total="<%= hits.getLength() %>" />
                
            <liferay-ui:search-container-row className="com.liferay.portal.kernel.search.Document" 
                modelVar="document">
            
            <%
                JSONObject jsonObj = JSONFactoryUtil.createJSONObject(document.get(Field.CONTENT));
                
                for (String column : columns) {
            %>
                <liferay-ui:search-container-column-text 
                    name="<%= CamelCaseUtil.fromCamelCase(column)  %>"
                    orderable="<%= false %>"
                    value='<%= jsonObj.getString(column) %>'/>               
            <%
                }
            %>
            
            </liferay-ui:search-container-row>
            
            <liferay-ui:search-iterator/>
            
        </liferay-ui:search-container>
    
    </c:otherwise>
</c:choose>

<hr>

<!-- <ifx-util:build-info /> -->
