<%--
    view_bibliography.jsp: default view of the bibliography manaager portlet.
    
    Created:    2016-11-30 00:18 by Christian Berndt
    Modified:   2016-12-01 18:23 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");
    String tabs1 = ParamUtil.getString(request, "tabs1", "settings");
    String availableTabs = "settings"; 
    
    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);

    boolean hasUpdatePermission = true;

    if (bibliography != null) {
        BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.UPDATE);
        availableTabs = "browse,import-export,settings";
        portletURL.setParameter("bibliographyId", String.valueOf(bibliography.getBibliographyId()));
        tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    }
%>

<c:choose>
    <c:when test="<%=Validator.isNull(bibliography)%>">
        <div class="bibliography-head">
            <h2>
                <liferay-ui:message key="create-a-new-bibliography" />
            </h2>
            <p>
                <liferay-ui:message key="you-can-import-your-references-from-a-file-or-pick-references-already-available-on-bibshare" />
            </p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="bibliography-head">
            <h2><%=bibliography.getTitle()%></h2>
            <p><%= bibliography.getDescription() %></p>
        </div>
    </c:otherwise>
</c:choose>

<liferay-ui:tabs names="<%= availableTabs %>" param="tabs1"
    url="<%=portletURL.toString()%>" />
    
<c:choose>

    <c:when test='<%= tabs1.equals("import-export") %>'>
        <liferay-util:include page="/import_bibliography.jsp" servletContext="<%= application %>" />
    </c:when>
    
   <c:when test='<%= tabs1.equals("settings") %>'>
       <liferay-util:include page="/bibliography_settings.jsp" servletContext="<%= application %>" />
   </c:when>        

    <c:otherwise>
       
       browse entries 
        
    </c:otherwise>
</c:choose>
