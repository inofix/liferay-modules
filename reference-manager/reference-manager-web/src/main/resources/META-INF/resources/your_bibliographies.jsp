<%--
    your_bibliographies.jsp: Default view of the your-bibliographies-portlet.
    
    Created:    2016-11-29 22:52 by Christian Berndt
    Modified:   2017-02-02 19:51 by Christian Berndt
    Version:    1.1.1
--%>

<%@ include file="/init.jsp"%>

<%
    Group group = user.getGroup();

    boolean hasAddPermission = false;
    boolean hasManagePermission = false;

    if (group != null) {
        hasAddPermission = BibliographyManagerPortletPermission.contains(permissionChecker, group.getGroupId(),
                BibliographyActionKeys.ADD_BIBLIOGRAPHY);
    }

    String keywords = ParamUtil.getString(request, "keywords");

    SearchContainer<Bibliography> bibliographySearch = new BibliographySearch(renderRequest, "cur", portletURL);

    Sort sort = new Sort("title_sortable", false);

    BibliographySearchTerms searchTerms = (BibliographySearchTerms) bibliographySearch.getSearchTerms();

    Hits hits = BibliographyServiceUtil.search(themeDisplay.getUserId(), 0, themeDisplay.getUserId(), keywords,
            bibliographySearch.getStart(), bibliographySearch.getEnd(), sort);

    List<Document> documents = ListUtil.toList(hits.getDocs());

    List<Bibliography> bibliographies = BibliographyUtil.documentsToBibliographies(documents);

    bibliographySearch.setResults(bibliographies);
    bibliographySearch.setTotal(hits.getLength());

    AssetRendererFactory<Bibliography> assetRendererFactory = AssetRendererFactoryRegistryUtil
            .getAssetRendererFactoryByClass(Bibliography.class);
%>

<div class="panel panel-default">

    <div class="panel-heading">
        <strong><liferay-ui:message key="your-bibliographies" /></strong>
    </div>

    <liferay-util:buffer var="addButton">

        <%
            liferayPortletRequest.setAttribute("redirect", currentURL);
            String editBibliographyURL = "";

            PortletURL addURL = assetRendererFactory.getURLAdd(liferayPortletRequest, liferayPortletResponse);

            if (addURL != null) {
                editBibliographyURL = addURL.toString();
            } else {
                // GUEST user
                hasAddPermission = false;
            }
            
            String manageURL = null; 
            
            if (!themeDisplay.getUser().isDefaultUser()) {
                hasManagePermission = true;
                manageURL = themeDisplay.getUser().getDisplayURL(themeDisplay);
            }
            
        %>

        <aui:button href="<%=editBibliographyURL%>"
            cssClass="btn-primary btn-success" value="new-bibliography"
            disabled="<%=!hasAddPermission%>" />

        <aui:button cssClass="btn-default pull-right" 
            disabled="<%= !hasManagePermission %>"
            href="<%=manageURL%>"
            value="manage" />

    </liferay-util:buffer>
    

    <c:if test="<%=hits.getLength() == 0%>">
        <div class="panel-body">
            <div class="alert alert-info">
                <liferay-ui:message
                    key="you-havent-created-any-bibliographies-yet"
                    escape="false" />
            </div>
            <%=addButton%>
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
                AssetRenderer<Bibliography> assetRenderer = assetRendererFactory
                            .getAssetRenderer(bibliography.getBibliographyId());
                    String viewURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse,
                            currentURL);
                    String editURL = assetRenderer.getURLEdit(liferayPortletRequest, liferayPortletResponse).toString();
            %> 
            
            <a href="<%=viewURL%>"><%=bibliography.getTitle()%></a>

        </li>
        <%
            }
        %>
    </ul>

    <c:if test="<%=hits.getLength() > 0%>">
        <div class="panel-body">
            <%=addButton%>
        </div>
    </c:if>

</div>
