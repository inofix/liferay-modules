<%--
    bibliography_entries.jsp: browse the bibliography's references.
    
    Created:    2016-12-03 15:50 by Christian Berndt
    Modified:   2016-12-03 16:54 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String bibliographyId = ParamUtil.getString(request, "bibliographyId");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    SearchContainer<Reference> referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);
    
    PortletURL iteratorURL = referenceSearch.getIteratorURL(); 
    iteratorURL.setParameter("bibliographyId", bibliographyId); 
    
    boolean reverse = false; 
    if (referenceSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }
    
    Sort sort = new Sort(referenceSearch.getOrderByCol(), reverse);
    
    Hits hits = ReferenceServiceUtil.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), keywords,
            referenceSearch.getStart(), referenceSearch.getEnd(), sort);
    
    List<Document> documents = ListUtil.toList(hits.getDocs());
    
    List<Reference> references = new ArrayList<Reference>();
    
    for (Document document : documents) {
        try {
            long referenceId = GetterUtil.getLong(document.get("entryClassPK"));
            Reference reference = ReferenceServiceUtil.getReference(referenceId);
            references.add(reference); 
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    referenceSearch.setResults(references); 
    referenceSearch.setTotal(hits.getLength());
%>

<liferay-ui:search-container
    cssClass="references-search-container"            
    id="references"
    searchContainer="<%= referenceSearch %>"
    var="referenceSearchContainer">
    
    <liferay-ui:search-container-row
        className="ch.inofix.referencemanager.model.Reference"
        escapedModel="true" modelVar="reference">

        <%@ include file="/search_columns.jspf" %>

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator/>
                
</liferay-ui:search-container>
