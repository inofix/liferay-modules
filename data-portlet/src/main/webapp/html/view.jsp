<%--
    view.jsp: Default view of the data portlet.
    
    Created:    2017-03-09 19:59 by Christian Berndt
    Modified:   2017-03-23 18:26 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.data.model.Measurement"%>

<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    int delta = ParamUtil.getInteger(request, "delta", 20);
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");

    int idx = ParamUtil.getInteger(request, "cur");
    String orderByCol = ParamUtil.getString(request, "orderByCol", "modifiedDate");
    String orderByType = ParamUtil.getString(request, "orderByType", "desc");
    
    System.out.println("orderByType = " + orderByType);

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);
%>

<%

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
    
        <portlet:actionURL var="importMeasurementsURL" name="importMeasurements" >
            <portlet:param name="mvcPath" value="/html/import.jsp"/>
        </portlet:actionURL>
        
        <portlet:renderURL var="browseURL" />

        <aui:form action="<%=importMeasurementsURL%>"
            enctype="multipart/form-data" method="post" name="fm"
            cssClass="import-form">

            <%
                // TODO: Add error handling
            %>
            <%-- 
               <liferay-ui:error exception="<%= FileExtensionException.class %>">
           
               </liferay-ui:error>
               --%>

            <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />

            <aui:fieldset label="import">

                <aui:input name="file" type="file" inlineField="true"
                    label="" />

                <aui:button name="import" type="submit" value="import"
                    disabled="true" />
                <aui:button href="<%=browseURL%>" type="cancel" />

            </aui:fieldset>

        </aui:form>

        <aui:script use="aui-base">
            var input = A.one('#<portlet:namespace />file');
            var button = A.one('#<portlet:namespace />import');
        
            input.on('change', function(e) {
        
                if (input.get('value')) {
                    button.removeClass('disabled');
                    button.removeAttribute('disabled');
                } else {
                    button.addClass('disabled');
                    button.setAttrs({
                        disabled : 'disabled'
                    });
                }
        
            });
        </aui:script>

    </c:when>

    <c:otherwise>
    
        <liferay-ui:search-container emptyResultsMessage="no-data-found">
            <liferay-ui:search-container-results
                results="<%=documents%>"
                total="<%= hits.getLength() %>" />
                
            <liferay-ui:search-container-row className="com.liferay.portal.kernel.search.Document" 
                modelVar="document">
            
            <%
                JSONObject data = JSONFactoryUtil.createJSONObject(document.get(Field.CONTENT));
                
                for (String column : columns) {
            %>
                <liferay-ui:search-container-column-text 
                    name="<%= column %>"
                    orderableProperty='<%= column + "_sortable" %>'
                    orderable="<%= true %>"
                    value='<%= data.getString(column) %>'/>               
            <%
                }
            %>
            
            </liferay-ui:search-container-row>
            
            <liferay-ui:search-iterator/>
            
        </liferay-ui:search-container>
    
    </c:otherwise>
</c:choose>

<hr>

<ifx-util:build-info />
