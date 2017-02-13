<%--
    user_bibliographies: display a user's bibliographies.
    
    Created:    2016-12-16 00:12 by Christian Berndt
    Modified:   2017-02-13 22:56 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/init.jsp"%>

<%
   boolean hasAddPermission = false;

   long userGroupId = 0; 
   Group userGroup = user.getGroup(); 
   String userName = themeDisplay.getScopeGroupName();

   if (userGroup != null) {
       userGroupId = userGroup.getGroupId(); 
       hasAddPermission = BibliographyManagerPortletPermission.contains(permissionChecker, userGroupId,
               BibliographyActionKeys.ADD_BIBLIOGRAPHY); 
   } 
   boolean isUserGroup = themeDisplay.getScopeGroupId() == userGroupId;

   String keywords = null; 

   SearchContainer<Bibliography> bibliographySearch = new BibliographySearch(renderRequest, "cur", portletURL);

   boolean reverse = false; 
   if (bibliographySearch.getOrderByType().equals("desc")) {
       reverse = true;
   }
   
   Sort sort = new Sort(bibliographySearch.getOrderByCol(), reverse);
   
   BibliographySearchTerms searchTerms = (BibliographySearchTerms) bibliographySearch.getSearchTerms();

   Hits hits = BibliographyServiceUtil.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), -1, keywords,
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

<div class="bibliography-head">
    <c:choose>
        <c:when test="<%= isUserGroup %>">
            <h2><liferay-ui:message key="your-bibliographies"/></h2>
        </c:when>
        <c:otherwise>
            <h2><liferay-ui:message key="bibliographies-of-x" arguments="<%= new String[] {userName} %>"/></h2>
        </c:otherwise>
    </c:choose>
</div>

<liferay-ui:search-container
    cssClass="bibliographies-search-container"  
    emptyResultsMessage="the-user-hasnt-created-any-bibliographies-yet"          
    id="references"
    searchContainer="<%= bibliographySearch %>"
    var="bibliographySearchContainer">
    
    <liferay-ui:search-container-row
        className="ch.inofix.referencemanager.model.Bibliography"
        escapedModel="true" modelVar="bibliography">
        
        <portlet:renderURL var="viewURL">
            <portlet:param name="mvcPath" value="/edit_bibliography.jsp" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="bibliographyId"
                value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
        </portlet:renderURL>
    
        <liferay-ui:search-container-column-text href="<%=viewURL%>"
            orderable="true"
            orderableProperty="title_sortable"
            property="title" valign="middle" />
            
        <liferay-ui:search-container-column-jsp cssClass="entry-action"
              path="/bibliography/bibliography_action.jsp" valign="top" />

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator/>
                
</liferay-ui:search-container>

<%
    liferayPortletRequest.setAttribute("redirect", currentURL);
    String editBibliographyURL = ""; 
    
    PortletURL addURL = assetRendererFactory
            .getURLAdd(liferayPortletRequest, liferayPortletResponse);
    
    if (addURL != null) {
        editBibliographyURL = addURL.toString(); 
    } else {
        // GUEST user
        hasAddPermission = false; 
    }
%>

<aui:button href="<%=editBibliographyURL%>"
    cssClass="btn-primary btn-success" value="new-bibliography"
    disabled="<%=!hasAddPermission%>" />
