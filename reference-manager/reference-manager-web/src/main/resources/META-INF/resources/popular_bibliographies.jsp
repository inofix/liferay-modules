<%--
    popular_bibliographies.jsp: Default view of the popular-bibliographies-portlet.
    
    Created:    2016-12-15 13:21 by Christian Berndt
    Modified:   2016-12-15 13:21 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    SearchContainer<Bibliography> bibliographySearch = new BibliographySearch(renderRequest, "cur", portletURL);

    Sort sort = new Sort("viewCount_sortable", true);

    BibliographySearchTerms searchTerms = (BibliographySearchTerms) bibliographySearch.getSearchTerms();

    String keywords = null; 
    
    Hits hits = BibliographyServiceUtil.search(themeDisplay.getUserId(), 0, -1, keywords,
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
        <strong><liferay-ui:message
                key="popular-bibliographies" /></strong>
    </div>

    <c:if test="<%=hits.getLength() == 0%>">
        <div class="panel-body">
            <div class="alert alert-info">
                <liferay-ui:message
                    key="there-are-no-popular-bibliographies"
                    escape="false" />
            </div>
        </div>
    </c:if>

    <ul class="list-group">

        <%
            for (Bibliography bibliography : bibliographySearch.getResults()) {
        %>

        <li class="list-group-item">
            <%
                AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(Bibliography.class.getName(),
                            bibliography.getBibliographyId());

                AssetRenderer<Bibliography> assetRenderer = assetRendererFactory
                        .getAssetRenderer(bibliography.getBibliographyId());

                String viewURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse,
                        currentURL);               
            %>
            
            <a href="<%=viewURL%>"><strong><%=bibliography.getTitle()%></strong></a>
            <span class="pull-right">
                <%=assetEntry.getViewCount()%>
                <span class="icon-eye-open text-muted" title='<liferay-ui:message key="views"/>'></span>
            </span>
        </li>
        <%
            }
        %>
    </ul>

</div>
