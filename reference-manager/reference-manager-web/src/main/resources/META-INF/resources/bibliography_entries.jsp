<%--
    bibliography_entries.jsp: browse the bibliography's references.
    
    Created:    2016-12-03 15:50 by Christian Berndt
    Modified:   2016-12-24 16:25 by Christian Berndt
    Version:    1.0.4
--%>

<%@ include file="/init.jsp" %>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String bibliographyId = ParamUtil.getString(request, "bibliographyId");
    String keywords = ParamUtil.getString(request, "keywords");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);
    boolean hasUpdatePermission = BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.UPDATE);

    SearchContainer<Reference> referenceSearch = new ReferenceSearch(renderRequest, "cur", portletURL);
    
    PortletURL iteratorURL = referenceSearch.getIteratorURL(); 
    iteratorURL.setParameter("bibliographyId", bibliographyId); 
    iteratorURL.setParameter("mvcPath", "/edit_bibliography.jsp"); 
    
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

<div class="clearfix">

    <div class="pull-left">
        <aui:form name="fm1" cssClass="search-form">
            <aui:input inlineField="true" label="" name="keywords"
                placeholder="search" />
            <aui:button type="submit" value="search" />
        </aui:form>
    </div>

    <div class="pull-right">
        <portlet:renderURL var="editReferenceURL">
            <portlet:param name="bibliographyId"
                value="<%=bibliographyId%>" />
            <portlet:param name="mvcPath" value="/edit_reference.jsp" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
        </portlet:renderURL>

        <aui:button cssClass="btn-success"
            disabled="<%=!hasUpdatePermission%>"
            href="<%=editReferenceURL.toString()%>"
            value="add-reference" />
    </div>
</div>

<liferay-ui:search-container
    cssClass="references-search-container"            
    id="references"
    searchContainer="<%= referenceSearch %>"
    var="referenceSearchContainer">
    
    <liferay-ui:search-container-row
        className="ch.inofix.referencemanager.model.Reference"
        escapedModel="true" modelVar="reference">
        
        <%
            String detailURL = null; 
        %>
        
        
        <portlet:renderURL var="viewURL">
            <portlet:param name="bibliographyId"
                value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
            <portlet:param name="mvcPath"
                value="/edit_reference.jsp" />
            <portlet:param name="redirect"
                value="<%=currentURL%>" />
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:renderURL>
        
        <%
            if (ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.VIEW)) {
                detailURL = viewURL; 
            }
        %>
    
    
    
        <portlet:renderURL var="editURL">
            <portlet:param name="bibliographyId"
                value="<%=String.valueOf(bibliography.getBibliographyId())%>" />
            <portlet:param name="mvcPath"
                value="/edit_reference.jsp" />
            <portlet:param name="redirect"
                value="<%=currentURL%>" />
            <portlet:param name="referenceId"
                value="<%=String.valueOf(reference.getReferenceId())%>" />
        </portlet:renderURL>
        
        <%
            if (ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.UPDATE)) {
                detailURL = editURL; 
            }
        %>            
        
        
        <%
            row.setParameter("detailURL", detailURL);            
        %>

        <%@ include file="/search_columns.jspf" %>
        
        
        <liferay-ui:search-container-column-text>

            <liferay-ui:icon-menu icon="<%=StringPool.BLANK%>"
                message="<%=StringPool.BLANK%>"
                showExpanded="<%=row == null%>" showWhenSingleIcon="true">
    
                <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.VIEW)%>">
    
                    <liferay-ui:icon iconCssClass="icon-eye-open"
                        message="view" url="<%=viewURL%>" />
    
                </c:if>
    
                <c:if test="<%=ReferencePermission.contains(permissionChecker, reference, ReferenceActionKeys.UPDATE)%>">
    
                    <liferay-ui:icon iconCssClass="icon-edit" message="edit"
                        url="<%=editURL%>" />
    
                </c:if>
    
            </liferay-ui:icon-menu>
        
        </liferay-ui:search-container-column-text>

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator/>
                
</liferay-ui:search-container>
