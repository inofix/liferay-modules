<%--
    view.jsp: Default view of the reference manager portlet.
    
    Created:    2016-01-10 22:51 by Christian Berndt
    Modified:   2016-11-26 16:59 by Christian Berndt
    Version:    1.0.7
--%>

<%@ include file="/init.jsp" %>

<%@page import="com.liferay.portal.kernel.search.Sort"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    SearchContainer referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);
    
    boolean reverse = false; 
    if (referenceSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }
    
    Sort sort = new Sort(referenceSearch.getOrderByCol(), reverse);
    
    ReferenceSearchTerms searchTerms = (ReferenceSearchTerms) referenceSearch.getSearchTerms();

    Hits hits = referenceLocalService.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), keywords,
            referenceSearch.getStart(), referenceSearch.getEnd(), sort);
    
    List<Document> documents = ListUtil.toList(hits.getDocs());

    referenceSearch.setResults(documents); 
    referenceSearch.setTotal(hits.getLength());
%>

<liferay-ui:error exception="<%= PrincipalException.class %>"
	message="you-dont-have-the-required-permissions" />
 
<liferay-ui:tabs names="browse,import,export,manage" param="tabs1"
	url="<%=portletURL.toString()%>" />

<c:choose>

	<c:when test='<%= tabs1.equals("import") %>'>
        <liferay-util:include page="/import.jsp" servletContext="<%= application %>" />
	</c:when>

	<c:when test='<%= tabs1.equals("export") %>'>
           <liferay-util:include page="/export.jsp" servletContext="<%= application %>" />
	</c:when>
	
       <c:when test='<%= tabs1.equals("manage") %>'>
           <liferay-util:include page="/manage.jsp" servletContext="<%= application %>" />
       </c:when>		

	<c:otherwise>

		<aui:button-row>
			<portlet:renderURL var="editReferenceURL">
				<portlet:param name="mvcPath" value="/edit_reference.jsp" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:renderURL>

			<aui:button href="<%= editReferenceURL %>" value="add-reference" />
		</aui:button-row>
        
        <div class="search-results">
            <liferay-ui:search-speed hits="<%= hits %>" searchContainer="<%= referenceSearch %>" />
        </div>

        <liferay-ui:search-container
            cssClass="references-search-container"
            id="references"
            searchContainer="<%= referenceSearch %>"
            var="referenceSearchContainer">

        <%-- -
			<liferay-ui:search-container-results
				results="<%= referenceLocalService.getReferences(searchContainer.getStart(), searchContainer.getEnd()) %>" />
        --%>

        <liferay-ui:search-container-row
            className="com.liferay.portal.kernel.search.Document"
            keyProperty="entryClassPK"
            modelVar="document">
            
            <%-- 
			<liferay-ui:search-container-row
				className="ch.inofix.referencemanager.model.Reference"
				escapedModel="true" modelVar="reference">
--%>
                <liferay-ui:search-container-column-text name="id"
                    value="<%=document.get("entryClassPK")%>"
                    orderable="true" valign="top" />

                <liferay-ui:search-container-column-text name="author"
                    value="<%=document.get("author")%>"
                    orderable="true" orderableProperty="author_sortable" />
                <liferay-ui:search-container-column-text name="title"
                    value="<%=document.get("title")%>"
                    orderable="true" orderableProperty="title_sortable" />
                <liferay-ui:search-container-column-text name="year"
                    value="<%=document.get("year")%>" orderable="true"
                    orderableProperty="year_sortable" />

                <%-- 
				<liferay-ui:search-container-column-text name="bibtex" valign="top">
					<strong><%= reference.getBibTeX() %></strong>
        
					<br />

					<div class="lfr-asset-categories">
						<liferay-ui:asset-categories-summary
							className="<%= Reference.class.getName() %>"
							classPK="<%= reference.getReferenceId() %>" />
					</div>

					<div class="lfr-asset-tags">
						<liferay-ui:asset-tags-summary
							className="<%=Reference.class.getName()%>"
							classPK="<%=reference.getReferenceId()%>" message="tags" />
					</div>
				</liferay-ui:search-container-column-text>
                --%>

			
                <%-- 
				<liferay-ui:search-container-column-jsp cssClass="entry-action"
					path="/reference_action.jsp" valign="top" />
                --%>

			</liferay-ui:search-container-row>

            <liferay-ui:search-iterator/>
            			
		</liferay-ui:search-container>
	</c:otherwise>
</c:choose>
