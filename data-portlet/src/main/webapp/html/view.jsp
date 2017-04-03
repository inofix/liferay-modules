<%--
    view.jsp: Default view of the data portlet.
    
    Created:    2017-03-09 19:59 by Christian Berndt
    Modified:   2017-04-03 19:32 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.CamelCaseUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String channelId = ParamUtil.getString(request, "channelId");
    String channelName = ParamUtil.getString(request, "channelName");
    int delta = ParamUtil.getInteger(request, "delta", 20);

    int idx = ParamUtil.getInteger(request, "cur");
    String orderByCol = ParamUtil.getString(request, "orderByCol","date_sortable");
    //     String orderByType = ParamUtil.getString(request, "orderByType", "desc");

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("backURL", backURL);
    portletURL.setParameter("channelId", channelId);
    portletURL.setParameter("channelName", channelName);
    portletURL.setParameter("fromDateDay", String.valueOf(fromDateDay));
    portletURL.setParameter("fromDateMonth",
            String.valueOf(fromDateMonth));
    portletURL.setParameter("fromDateYear",
            String.valueOf(fromDateYear));
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("untilDateDay",
            String.valueOf(untilDateDay));
    portletURL.setParameter("untilDateMonth",
            String.valueOf(untilDateMonth));
    portletURL.setParameter("untilDateYear",
            String.valueOf(untilDateYear));

    long from = 0;
    if (fromDate != null) {
        from = fromDate.getTime();
    }

    long until = 0;
    if (untilDate != null) {
        until = untilDate.getTime();
    }

    if (idx > 0) {
        idx = idx - 1;
    }

    int start = delta * idx;
    int end = delta * idx + delta;

    boolean reverse = false;
    //     boolean reverse = "desc".equals(orderByType);

    Sort sort = new Sort(orderByCol, reverse);

    Hits hits = MeasurementLocalServiceUtil.search(
            themeDisplay.getCompanyId(), scopeGroupId, channelId,
            channelName, from, until, false, start, end, sort);

    List<Document> documents = hits.toList();
%>

<liferay-ui:header backURL="<%=backURL%>" title="data-manager" />

<liferay-ui:error exception="<%=PrincipalException.class%>"
    message="you-dont-have-the-required-permissions" />

<liferay-ui:tabs names="chart,list,import-export" param="tabs1"
    url="<%=portletURL.toString()%>" />

<c:choose>

    <c:when test='<%=tabs1.equals("chart")%>'>
            
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/chart.jsp" />   
        
    </c:when>

    <c:when test='<%=tabs1.equals("import-export")%>'>
            
        <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/import.jsp" />   
        
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
            
            <liferay-ui:search-iterator type="<%= paginationType %>" />
                        
        </liferay-ui:search-container>
    
    </c:otherwise>
</c:choose>

<hr>

<!-- <ifx-util:build-info /> -->
