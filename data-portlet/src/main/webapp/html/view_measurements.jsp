<%--
    view_measurements.jsp: the list view of the data-portlet
    
    Created:    2017-11-28 19:47 by Christian Berndt
    Modified:   2017-11-28 19:47 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");

    int delta = ParamUtil.getInteger(request, "delta", 20);
    
    int idx = ParamUtil.getInteger(request, "cur");
    String orderByCol = ParamUtil.getString(request, "orderByCol",
            "timestamp_sortable");
    
    PortletURL portletURL = renderResponse.createRenderURL();
    
    portletURL.setParameter("backURL", backURL);
    portletURL.setParameter("id", id);
    portletURL.setParameter("from", String.valueOf(from));
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("until", String.valueOf(until));
    
    if (idx > 0) {
        idx = idx - 1;
    }

    int start = delta * idx;
    int end = delta * idx + delta;
    
    boolean reverse = false;
    //     boolean reverse = "desc".equals(orderByType);
    
    Sort sort = new Sort(orderByCol, reverse);
    
    Hits hits = MeasurementLocalServiceUtil.search(
            themeDisplay.getCompanyId(), scopeGroupId, id, from, until,
            false, start, end, sort);
    
    List<Document> documents = hits.toList();

%>

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
    
    <liferay-ui:search-iterator type="<%= paginationType %>" />
                
</liferay-ui:search-container>
