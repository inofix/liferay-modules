<%--
    view.jsp: Default view of the reference manager portlet.
    
    Created:    2016-01-10 22:51 by Christian Berndt
    Modified:   2017-02-13 22:14 by Christian Berndt
    Version:    1.1.8
--%>

<%@ include file="/init.jsp" %>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    String [] columns = new String[] {"author", "title", "year"}; 

    if (Validator.isNotNull(referenceManagerConfiguration)) {
        columns = portletPreferences.getValues("columns", referenceManagerConfiguration.columns());
    }

    SearchContainer<Reference> referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);
    
    boolean reverse = false; 
    if (referenceSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }
    
    Sort sort = new Sort(referenceSearch.getOrderByCol(), reverse);
    
    ReferenceSearchTerms searchTerms = (ReferenceSearchTerms) referenceSearch.getSearchTerms();

    Hits hits = ReferenceServiceUtil.search(themeDisplay.getUserId(), 0, keywords, 0,
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
    
    AssetRendererFactory<Reference> referenceAssetRendererFactory = AssetRendererFactoryRegistryUtil
            .getAssetRendererFactoryByClass(Reference.class);    
%>

<liferay-ui:error exception="<%= PrincipalException.class %>"
	message="you-dont-have-the-required-permissions" />
 
<liferay-ui:tabs names="browse,import,manage" param="tabs1"
	url="<%=portletURL.toString()%>" />

<c:choose>

	<c:when test='<%= tabs1.equals("import") %>'>
        <liferay-util:include page="/import_references.jsp" servletContext="<%= application %>" />
	</c:when>
	
    <c:when test='<%= tabs1.equals("manage") %>'>
        <liferay-util:include page="/manage.jsp" servletContext="<%= application %>" />
    </c:when>		

	<c:otherwise>

        <div class="search-results">
            <liferay-ui:search-speed hits="<%= hits %>" searchContainer="<%= referenceSearch %>" />
        </div>

        <liferay-ui:search-container
            cssClass="references-search-container"            
            id="references"
            searchContainer="<%= referenceSearch %>"
            var="referenceSearchContainer">
            
			<liferay-ui:search-container-row
				className="ch.inofix.referencemanager.model.Reference"
				escapedModel="true" modelVar="reference">
                
                <%@ include file="/search_columns.jspf" %>
 
                <liferay-ui:search-container-column-jsp cssClass="entry-action"
                     path="/reference/reference_action.jsp" valign="top" />

			</liferay-ui:search-container-row>

            <liferay-ui:search-iterator/>
            			
		</liferay-ui:search-container>
	</c:otherwise>
</c:choose>
