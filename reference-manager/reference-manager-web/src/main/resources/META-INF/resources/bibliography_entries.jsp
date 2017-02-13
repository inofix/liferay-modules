<%--
    bibliography_entries.jsp: browse the bibliography's references.
    
    Created:    2016-12-03 15:50 by Christian Berndt
    Modified:   2017-02-13 23:21 by Christian Berndt
    Version:    1.1.9
--%>

<%@ include file="/init.jsp" %>

<%
    String bibliographyId = ParamUtil.getString(request, "bibliographyId");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    String [] columns = new String[] {"author", "title", "year"}; 

    if (Validator.isNotNull(bibliographyManagerConfiguration)) {
        columns = portletPreferences.getValues("columns", bibliographyManagerConfiguration.columns());
    }

    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);
    boolean hasUpdatePermission = BibliographyPermission.contains(permissionChecker, bibliography,
            BibliographyActionKeys.UPDATE);
    
    request.setAttribute("bibliography_entries.jsp-bibliography", bibliography);

    SearchContainer<Reference> referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);

    PortletURL iteratorURL = referenceSearch.getIteratorURL();
    iteratorURL.setParameter("bibliographyId", bibliographyId);
    iteratorURL.setParameter("mvcPath", "/edit_bibliography.jsp");

    boolean reverse = false;
    if (referenceSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }

    Sort sort = new Sort(referenceSearch.getOrderByCol(), reverse);

    Hits hits = ReferenceServiceUtil.search(themeDisplay.getUserId(), 0, keywords,
            bibliography.getBibliographyId(), referenceSearch.getStart(), referenceSearch.getEnd(), sort);

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

    PortletURL addReferenceURL = referenceAssetRendererFactory.getURLAdd(liferayPortletRequest,
            liferayPortletResponse);
    
    String href = null; 
    
    if (addReferenceURL != null) {
        addReferenceURL.setParameter("bibliographyId", String.valueOf(bibliographyId));
        addReferenceURL.setParameter("redirect", currentURL);
        href = addReferenceURL.toString(); 
    }
%>

<nav class="clearfix navbar">
    
    <portlet:renderURL var="searchURL">
        <portlet:param name="bibliographyId"
            value="<%=String.valueOf(bibliographyId)%>" />
        <portlet:param name="mvcPath" value="/edit_bibliography.jsp" />
        <portlet:param name="tabs1" value="browse" />
    </portlet:renderURL>

    <div class="pull-left">
        <aui:form action="<%=searchURL%>" name="fm1"
            cssClass="search-form">
            <aui:input inlineField="true" label="" name="keywords"
                value="<%=keywords%>" placeholder="search" />
            <aui:button type="submit" value="search" />
            <aui:button cssClass="btn-default" href="<%=searchURL%>"
                value="clear" />
        </aui:form>
    </div>

    <div class="pull-right">
        <aui:button cssClass="btn-success"
            disabled="<%=!hasUpdatePermission%>"
            href="<%= href %>"
            value="add-reference" />
    </div>
    
</nav>


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

<aui:fieldset cssClass="tags">
    <liferay-ui:asset-tags-summary
        className="<%=Bibliography.class.getName()%>"
        classPK="<%=bibliography.getBibliographyId()%>" />
</aui:fieldset>

<liferay-ui:ratings 
    className="<%=Bibliography.class.getName()%>"
    classPK="<%=bibliography.getBibliographyId()%>" 
    type="stars" />
