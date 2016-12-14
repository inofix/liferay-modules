<%--
    view.jsp: Default view of the reference manager portlet.
    
    Created:    2016-01-10 22:51 by Christian Berndt
    Modified:   2016-12-15 00:13 by Christian Berndt
    Version:    1.1.5
--%>

<%@ include file="/init.jsp" %>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    boolean hasAddPermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.ADD_REFERENCE);
    
    SearchContainer<Reference> referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);
    
    boolean reverse = false; 
    if (referenceSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }
    
    Sort sort = new Sort(referenceSearch.getOrderByCol(), reverse);
    
    ReferenceSearchTerms searchTerms = (ReferenceSearchTerms) referenceSearch.getSearchTerms();

    Hits hits = ReferenceServiceUtil.search(themeDisplay.getUserId(), 0, keywords,
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

        <aui:button-row>
            <portlet:renderURL var="addReferenceURL">
                <portlet:param name="mvcPath"
                    value="/edit_reference.jsp" />
                <portlet:param name="redirect" value="<%=currentURL%>" />
            </portlet:renderURL>

            <aui:button href="<%=addReferenceURL%>"
                cssClass="btn-primary"
                value="add-reference"
                disabled="<%= !hasAddPermission %>" />
        </aui:button-row>

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

			</liferay-ui:search-container-row>

            <liferay-ui:search-iterator/>
            			
		</liferay-ui:search-container>
	</c:otherwise>
</c:choose>
