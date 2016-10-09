<%--
    view.jsp: Default view of the newsletter-portlet.
    
    Created:     2016-10-05 15:54 by Christian Berndt
    Modified:    2016-10-09 16:26 by Christian Berndt
    Version:     1.0.5
 --%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.search.BooleanClause"%>
<%@page import="com.liferay.portal.kernel.search.BooleanClauseFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.search.BooleanQuery"%>
<%@page import="com.liferay.portal.kernel.search.BooleanQueryFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.FacetedSearcher"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.search.facet.Facet"%>
<%@page import="com.liferay.portal.kernel.search.facet.AssetEntriesFacet"%>
<%@page import="com.liferay.portal.kernel.search.facet.RangeFacet"%>
<%@page import="com.liferay.portal.kernel.search.facet.ScopeFacet"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String[] classNames = new String[] { className };
    int delta = ParamUtil.getInteger(request, "delta", 20);
    int idx = ParamUtil.getInteger(request, "cur");
    String keywords = ParamUtil.getString(request, "keywords");
    String orderByCol = ParamUtil.getString(request, "orderByCol","name");
    String orderByType = ParamUtil.getString(request, "orderByType", "asc");
    String tabs1 = ParamUtil.getString(request, "tabs1", "subscribers");

    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/view.jsp");
    portletURL.setParameter("backURL", backURL);

    Log log = LogFactoryUtil.getLog("docroot.html.view.jsp");

    if (idx > 0) {
        idx = idx - 1;
    }
    int start = delta * idx;
    int end = delta * idx + delta;

    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    boolean reverse = "desc".equals(orderByType);

    Sort sort = new Sort(orderByCol, reverse);

    searchContext.setEntryClassNames(classNames);
    searchContext.setKeywords(keywords);
    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(start);
    searchContext.setEnd(end);
    searchContext.setSorts(sort);
        
    // exclude empty emails
    BooleanQuery query = BooleanQueryFactoryUtil.create(searchContext);
    query.addRangeTerm("email", "a", "z");
    BooleanClause booleanClause = BooleanClauseFactoryUtil.create(searchContext, query, "MUST");
    searchContext.setBooleanClauses(new BooleanClause[] {booleanClause});
    
    Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);
    assetEntriesFacet.setStatic(true);
    searchContext.addFacet(assetEntriesFacet);

    Facet scopeFacet = new ScopeFacet(searchContext);
    scopeFacet.setStatic(true);
    searchContext.addFacet(scopeFacet); 

    Indexer indexer = FacetedSearcher.getInstance();

    Hits hits = indexer.search(searchContext);
%>

<div id="<portlet:namespace />newsletterContainer">

    <liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

    <liferay-ui:tabs names="subscribers,newsletters,templates,mailings"
        param="tabs1" url="<%=portletURL.toString()%>" />

    <c:choose>

        <c:when test='<%=tabs1.equals("newsletters")%>'>
            <%@include file="/html/newsletters.jspf"%>
        </c:when>

        <c:when test='<%=tabs1.equals("templates")%>'>
            <%@include file="/html/templates.jspf"%>
        </c:when>

        <c:when test='<%=tabs1.equals("mailings")%>'>
            <%@include file="/html/mailings.jspf"%>
        </c:when>

        <c:otherwise>
            <%@include file="/html/subscribers.jspf"%>
        </c:otherwise>

    </c:choose>

    <hr>

    <ifx-util:build-info />

</div>