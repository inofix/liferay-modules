<%--
    your_bibliographies.jsp: Default view of the bibliography manager portlet.
    
    Created:    2016-11-29 22:52 by Christian Berndt
    Modified:   2016-12-01 13:24 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%
    boolean hasAddPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            BibliographyActionKeys.ADD_BIBLIOGRAPHY);
    String keywords = ParamUtil.getString(request, "keywords");

    SearchContainer<Bibliography> bibliographySearch = new BibliographySearch(renderRequest, "cur", portletURL);

    boolean reverse = false;
    if (bibliographySearch.getOrderByType().equals("desc")) {
        reverse = true;
    }

    Sort sort = new Sort(bibliographySearch.getOrderByCol(), reverse);

    BibliographySearchTerms searchTerms = (BibliographySearchTerms) bibliographySearch.getSearchTerms();

    Hits hits = BibliographyServiceUtil.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), keywords,
            bibliographySearch.getStart(), bibliographySearch.getEnd(), sort);

    List<Document> documents = ListUtil.toList(hits.getDocs());

    List<Bibliography> bibliographies = new ArrayList<Bibliography>();

    for (Document document : documents) {
        try {
            long bibliographyId = GetterUtil.getLong(document.get("entryClassPK"));
            Bibliography bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
            bibliographies.add(bibliography);
        } catch (Exception e) {
            // TODO: use logging
            System.out.println(e);
        }
    }

    bibliographySearch.setResults(bibliographies);
    bibliographySearch.setTotal(hits.getLength());
    
    AssetRendererFactory<Bibliography> assetRendererFactory = AssetRendererFactoryRegistryUtil
            .getAssetRendererFactoryByClass(Bibliography.class);
%>

<div class="panel panel-default">

    <div class="panel-heading">
        <strong><liferay-ui:message key="your-bibliographies"/></strong>     
    </div>
    
    <liferay-util:buffer var="addButton">
        <portlet:renderURL var="editBibliographyURL">
            <portlet:param name="mvcPath" value="/edit_bibliography.jsp" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
        </portlet:renderURL>

        <aui:button href="<%=editBibliographyURL%>"
            cssClass="btn-primary btn-sm" value="new-bibliography"
            disabled="<%=!hasAddPermission%>" />   
        
    </liferay-util:buffer> 
    
    <c:if test="<%= hits.getLength() == 0 %>">
        <div class="panel-body">
            <div class="alert alert-info">
                <liferay-ui:message key="you-havent-created-any-bibliographies-yet" escape="false"/> 
            </div>
            <%= addButton %>
        </div>
    </c:if>
    <%-- 
    <div class="panel-body">
        <div class="input-group">
            <input type="text" class="form-control"
                placeholder="Search for..."> <span
                class="input-group-btn">
                <button class="btn btn-default" type="button">Go!</button>
            </span>
        </div>
    </div>
    --%>

    <ul class="list-group">
    
        <%
            for (Bibliography bibliography : bibliographySearch.getResults()) {
        %>
    
            <li class="list-group-item">
                <%
                    AssetRenderer<Bibliography> assetRenderer = assetRendererFactory.getAssetRenderer(bibliography.getBibliographyId());
                    String viewURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse, currentURL); 
                %> 
                        
                <a href="<%= viewURL %>"><%= bibliography.getTitle() %></a> 
                 
                <liferay-ui:icon-menu icon="" message="">
                
                    <liferay-ui:icon iconCssClass="icon-eye-open"
                        message="view" url="<%=viewURL%>" />                       
                        
                    <liferay-ui:icon iconCssClass="icon-edit"
                        message="edit" url="<%=viewURL%>" />
                
                    <c:if test="<%= BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.DELETE) %>">
                
                        <portlet:actionURL var="deleteURL" name="deleteBibliography">
                            <portlet:param name="redirect"
                                value="<%= currentURL%>" />
                            <portlet:param name="bibliographyId"
                                value="<%= String.valueOf(bibliography.getBibliographyId()) %>" />
                        </portlet:actionURL>
                
                        <liferay-ui:icon-delete message="delete" url="<%=deleteURL%>" />
                    
                    </c:if>
                    
                </liferay-ui:icon-menu>

            </li>
        <%
            }
        %>
    </ul>

    <c:if test="<%= hits.getLength() > 0 %>">
        <div class="panel-body">
            <%= addButton %>
        </div>
    </c:if>
</div>
