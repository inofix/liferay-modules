<%--
    view.jsp: Default view of the 
    timetracker-portlet.
    
    Created:     2013-10-06 16:52 by Christian Berndt
    Modified:     2014-03-30 14:18 by Christian Berndt
    Version:     1.4
 --%>


<%@ include file="/init.jsp" %>

<%-- Enable aui view-port --%>
<script src="/html/js/aui/aui-min.js"></script>
<link href="/html/js/aui/aui-css/css/bootstrap.min.css" rel="stylesheet"></link>

<%
    // Get the portletURL.
    PortletURL portletURL = renderResponse.createRenderURL();

    // Create the SearchContainer 
    TaskRecordSearch taskRecordSearch = new TaskRecordSearch(
            renderRequest, portletURL);

    // Get the DisplayTerms 
    TaskRecordDisplayTerms displayTerms = (TaskRecordDisplayTerms) taskRecordSearch
            .getDisplayTerms();

    // Get the SearchTerms 
    TaskRecordSearchTerms searchTerms = (TaskRecordSearchTerms) taskRecordSearch
            .getSearchTerms();

    // Retrieve the portlet's currentURL.
    String currentURL = PortalUtil.getCurrentURL(renderRequest);

    // Retrieve the locale
    locale = renderRequest.getLocale();

    // Retrieve the portal instance ID
    long companyId = themeDisplay.getCompanyId();

    // Retrieve the group ID
    long groupId = scopeGroupId;
    
    String orderByCol = null; 
    String orderByType = null;
    
%>

<%-- Compose the searchURL. --%>
<liferay-portlet:renderURL varImpl="searchURL" />

<%-- Compose the addOrganizationURL --%>
<portlet:renderURL var="addTaskRecordURL">
    <portlet:param name="redirectURL" value="<%= currentURL %>" />
    <portlet:param name="mvcPath" value="/edit.jsp" />
</portlet:renderURL>

<portlet:renderURL var="importTaskRecordURL">
    <portlet:param name="redirectURL" value="<%= currentURL %>" />
    <portlet:param name="mvcPath" value="/import.jsp" />
</portlet:renderURL>

<%-- The portlet's header. Do not show the  --%>
<%-- backButton, because it's the portlet's --%>
<%-- main page.                             --%>
<liferay-ui:header title="timetracker-portlet-header" 
    showBackURL="<%= false %>"/>

<%-- Display errors. --%>    
<liferay-ui:error exception="<%=PrincipalException.class%>"
    message="you-dont-have-the-required-permissions" />
    
<%-- Display a form for adding task records. --%>
<%-- (Only for users that have the "ADD"     --%>
<%-- privilege.                              --%>
<%
    // TODO: Add permission management
%>
<aui:form action="<%= addTaskRecordURL %>" method="POST" name="add">
    <aui:button-row>
        <aui:button type="submit" value="add-task-record" />
    </aui:button-row>
</aui:form>

<aui:form action="<%= importTaskRecordURL %>" method="POST" name="add">
    <aui:button-row>
        <aui:button type="submit" value="import-task-records" />
    </aui:button-row>
</aui:form>

<%
    int startDateDay = ParamUtil.getInteger(request, "startDateDay"); 
    int startDateMonth = ParamUtil.getInteger(request, "startDateMonth"); 
    int startDateYear = ParamUtil.getInteger(request, "startDateYear"); 

    int endDateDay = ParamUtil.getInteger(request, "endDateDay"); 
    int endDateMonth = ParamUtil.getInteger(request, "endDateMonth"); 
    int endDateYear = ParamUtil.getInteger(request, "endDateYear");
    
    Date startDateParam = PortalUtil.getDate(startDateMonth,
            startDateDay, startDateYear);
    
    Date endDateParam = PortalUtil.getDate(endDateMonth, endDateDay,
            endDateYear);

    double hours = 0;

    // Copy the request parameters to the portletURL
    // in order to preserve them when browsing or sorting  
    // the search container.
    portletURL.setParameters(request.getParameterMap());
%>

<%-- startDateDay = <%= startDateDay %><br/> --%>
<%-- startDateMonth = <%= startDateMonth %><br/> --%>
<%-- startDateYear = <%= startDateYear %><br/> --%>

<aui:form action="<%= searchURL %>" name="fm" method="POST">

    <%-- Include the portlets render parameters in the search --%>
    <liferay-portlet:renderURLParams varImpl="searchURL" />
    
    <aui:input name="<%= TimetrackerPortletKeys.REDIRECT %>" 
        type="hidden" value="<%= currentURL %>" />
    <aui:input name="<%= CommonFields.GROUP_ID %>" 
        type="hidden" value="<%= String.valueOf(scopeGroupId) %>" />
    
    <liferay-ui:search-container searchContainer="<%= taskRecordSearch %>">
    
        <%-- Include the search form.         --%>            
        <liferay-ui:search-form page="/search.jsp"
            servletContext="<%=config.getServletContext()%>"
            searchContainer="<%=searchContainer%>" showAddButton="<%= true %>" />
            
        <%-- Display the results. --%>
        <liferay-ui:search-container-results>
        <%
        
//             if (displayTerms.isAdvancedSearch()) {
                        
//                 total = TaskRecordLocalServiceUtil.searchCount(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getWorkPackage(),
//                         searchTerms.getDescription(), 
//                         startDateParam, 
//                         endDateParam,
//                         searchTerms.getStatus(),
//                         searchTerms.isAndOperator());

//                 results = TaskRecordLocalServiceUtil.search(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getWorkPackage(),
//                         searchTerms.getDescription(),
//                         startDateParam, 
//                         endDateParam,
//                         searchTerms.getStatus(),
//                         searchContainer.getStart(),
//                         searchContainer.getEnd(),
//                         searchTerms.isAndOperator(),
//                         searchContainer.getOrderByComparator());
                
//                 List<TaskRecord> all = TaskRecordLocalServiceUtil.search(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getWorkPackage(),
//                         searchTerms.getDescription(),
//                         startDateParam, 
//                         endDateParam,
//                         searchTerms.getStatus(),
//                         0,
//                         total,
//                         searchTerms.isAndOperator(),
//                         searchContainer.getOrderByComparator());
                
//                 hours = TimetrackerPortletUtil.getHours(all); 

//             } else {

//                 // TODO: Enable the proper remote service search methods.
//                 total = TaskRecordLocalServiceUtil.searchCount(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getKeywords(),
//                         searchTerms.getStatus());

//                 results = TaskRecordLocalServiceUtil.search(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getKeywords(),
//                         searchTerms.getStatus(),
//                         searchContainer.getStart(),
//                         searchContainer.getEnd(),
//                         searchContainer.getOrderByComparator());
                
//                 List<TaskRecord> all = TaskRecordLocalServiceUtil.search(
//                         companyId, groupId,
//                         searchTerms.getUserId(),
//                         searchTerms.getKeywords(),
//                         searchTerms.getStatus(),
//                         0,
//                         total,
//                         searchContainer.getOrderByComparator());                
                
//                 hours = TimetrackerPortletUtil.getHours(all); 
//             }
        
            orderByCol = searchContainer.getOrderByCol(); 
            orderByType = searchContainer.getOrderByType(); 
        
            
            // TODO: Why must we store the results in the page context?
            pageContext.setAttribute("results", results);
            pageContext.setAttribute("total", total);
            
        %>
        </liferay-ui:search-container-results>
        
        <%-- Define the result row. --%>
        <liferay-ui:search-container-row className="TaskRecord"
            escapedModel="<%=true%>" keyProperty="taskRecordId" 
            modelVar="taskRecordRow">

            <%-- Include the actual search columns definition --%>
            <%@ include file="/search_columns.jspf" %>

        </liferay-ui:search-container-row>

        <%-- Loop over the results. --%>
        <liferay-ui:search-iterator />
        
    </liferay-ui:search-container>    
    
    <h4>Hours = <%= hours %></h4>
    
</aui:form>

<%
    String companyIdString = String.valueOf(companyId); 
    String groupIdString = String.valueOf(groupId); 
    String userIdString = String.valueOf(searchTerms.getUserId());
    String workPackageString = searchTerms.getWorkPackage(); 
    String descriptionString = searchTerms.getDescription(); 
    String startDateString = searchTerms.getStartDate(); 
    String endDateString = searchTerms.getEndDate();
%>

<!-- Compose the exportTaskRecordsURL -->
<portlet:renderURL var="exportTaskRecordsURL">
    <portlet:param name="mvcPath" value="/export.jsp"/>
    <portlet:param name="redirectURL" value="<%= currentURL %>"/>
</portlet:renderURL>

<aui:form action="<%= exportTaskRecordsURL %>" name="export">

    <aui:input type="hidden" name="<%=CommonFields.COMPANY_ID%>"
        value="<%=companyIdString%>" />
    <aui:input type="hidden" name="<%=CommonFields.GROUP_ID%>"
        value="<%=groupIdString%>" />
    <aui:input type="hidden" name="<%=TaskRecordFields.USER_ID%>"
        value="<%=userIdString%>" />
    <aui:input type="hidden" name="<%=TaskRecordFields.WORK_PACKAGE%>"
        value="<%=workPackageString%>" />
    <aui:input type="hidden" name="<%=TaskRecordFields.DESCRIPTION%>"
        value="<%=descriptionString%>" />
    <aui:input type="hidden" name="startDateDay"
        value="<%=startDateDay %>" />
    <aui:input type="hidden" name="startDateMonth"
        value="<%=startDateMonth %>" />
    <aui:input type="hidden" name="startDateYear"
        value="<%=startDateYear %>" />
    <aui:input type="hidden" name="endDateDay"
        value="<%=endDateDay %>" />
    <aui:input type="hidden" name="endDateMonth"
        value="<%=endDateMonth %>" />
    <aui:input type="hidden" name="endDateYear"
        value="<%=endDateYear %>" />
    <aui:input type="hidden" name="<%= TimetrackerPortletKeys.ORDER_BY_COL %>"
        value="<%= orderByCol %>"/>
    <aui:input type="hidden" name="<%= TimetrackerPortletKeys.ORDER_BY_TYPE %>"
        value="<%= orderByType %>"/>
    
    <aui:button-row>    
        <aui:button type="submit" value="download-current-selection"/>
    </aui:button-row>
</aui:form>

<iu:revision-display />

<script type="text/javascript">
<!--
    YUI().use('aui-viewport');
//-->
</script>
