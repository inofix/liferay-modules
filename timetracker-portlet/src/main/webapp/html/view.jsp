<%--
    view.jsp: Default view of the timetracker-portlet.
    
    Created:     2013-10-06 16:52 by Christian Berndt
    Modified:    2016-03-19 21:40 by Christian Berndt
    Version:     1.0.5
 --%>
 
<%@page import="ch.inofix.portlet.timetracker.search.TaskRecordChecker"%>
<%@ include file="/html/init.jsp"%>
 
<%-- Import required classes --%>

<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    int delta = ParamUtil.getInteger(request, "delta", 20); 
    String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
    String[] displayViews = StringUtil.split(PrefsParamUtil.getString(portletPreferences, liferayPortletRequest, "displayViews", "descriptive,icon,list"));
    int idx = ParamUtil.getInteger(request, "cur");
    String keywords = ParamUtil.getString(request, "keywords"); 
    String orderByCol = ParamUtil.getString(request, "orderByCol", "name"); 
    String orderByType = ParamUtil.getString(request, "orderByType", "asc"); 
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);  
%>

<%

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

    searchContext.setKeywords(keywords);
    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(start);
    searchContext.setEnd(end);
    searchContext.setSorts(sort);
    
    Indexer indexer = IndexerRegistryUtil.getIndexer(TaskRecord.class);

    Hits hits = indexer.search(searchContext);

    List<TaskRecord> taskRecords = new ArrayList<TaskRecord>();

    for (int i = 0; i < hits.getDocs().length; i++) {
        Document doc = hits.doc(i);

        long taskRecordId = GetterUtil.getLong(doc
                .get(Field.ENTRY_CLASS_PK));

        TaskRecord taskRecord = null;

        try {
            taskRecord = TaskRecordLocalServiceUtil.getTaskRecord(taskRecordId);
        } catch (PortalException pe) {
            log.error(pe.getLocalizedMessage());
        } catch (SystemException se) {
            log.error(se.getLocalizedMessage());
        }

        if (taskRecord != null) {
            taskRecords.add(taskRecord);
        }       
    }
    
    TaskRecordChecker rowChecker = new TaskRecordChecker(liferayPortletResponse); 
    rowChecker.setCssClass("entry-selector");
    
%>

<div id="<portlet:namespace />timetrackerContainer">

    <liferay-ui:header backURL="<%=backURL%>" title="timetracker" />
    
    <liferay-ui:error exception="<%= PrincipalException.class %>" 
       message="you-dont-have-the-required-permissions"/>
    
    <liferay-ui:tabs
        names="browse,import-export"
        param="tabs1" url="<%= portletURL.toString() %>" />

	<c:choose>

		<c:when test='<%= tabs1.equals("import-export") %>'>
			<%@include file="/html/import.jspf"%>
		</c:when>

		<c:otherwise>

            <liferay-ui:app-view-toolbar                 
                includeDisplayStyle="<%=true%>"
                includeSelectAll="<%=true%>">
                
                <liferay-util:include servletContext="<%= session.getServletContext() %>" page="/html/toolbar.jsp" />   
                            
            </liferay-ui:app-view-toolbar>
			
			
	        <portlet:actionURL name="editSet" var="editSetURL">
            </portlet:actionURL>

            <aui:form action="<%= editSetURL %>" name="fm" 
                onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "editSet();" %>'>
                
                <div id="<portlet:namespace />entriesContainer">
                    <liferay-ui:search-container
                        searchContainer="<%= new TaskRecordSearch(renderRequest, portletURL) %>"
                        var="taskRecordSearchContainer"
                        >    
                        
                        <liferay-ui:search-container-results results="<%= taskRecords %>"
                            total="<%= hits.getLength() %>" />
        
                        <liferay-ui:search-container-row
                            className="ch.inofix.portlet.timetracker.model.TaskRecord"
                            modelVar="taskRecord" keyProperty="taskRecordId">
        
                            <portlet:actionURL var="deleteURL" name="deleteTaskRecord">
                                <portlet:param name="taskRecordId"
                                    value="<%= String.valueOf(taskRecord.getTaskRecordId()) %>" />
                                <portlet:param name="backURL" value="<%= currentURL %>" />
                                <portlet:param name="mvcPath" value="/html/view.jsp" />
                            </portlet:actionURL>
        
                            <portlet:resourceURL var="downloadVCardURL" id="serveVCard">
                                <portlet:param name="taskRecordId"
                                    value="<%= String.valueOf(taskRecord.getTaskRecordId()) %>" />
                            </portlet:resourceURL>
        
                            <portlet:actionURL var="editURL" name="editTaskRecord"
                                windowState="<%= LiferayWindowState.POP_UP.toString() %>">
                                <portlet:param name="redirect" value="<%= currentURL %>" />
                                <portlet:param name="taskRecordId"
                                    value="<%= String.valueOf(taskRecord.getTaskRecordId()) %>" />
                                <portlet:param name="mvcPath" value="/html/edit_task_record.jsp" />
                                <portlet:param name="windowId" value="editTaskRecord" />
                            </portlet:actionURL>
        
                            <%
                                StringBuilder sb = new StringBuilder(); 
                            
                                sb.append(LanguageUtil.get(pageContext, "permissions-of-task-record")); 
                                sb.append(" "); 
                                sb.append(taskRecord.getTaskRecordId()); 
                            
                                String modelResourceDescription = sb.toString(); 
                            %>
        
                            <liferay-security:permissionsURL
                                modelResource="<%= TaskRecord.class.getName() %>"
                                modelResourceDescription="<%= modelResourceDescription %>"
                                resourcePrimKey="<%= String.valueOf(taskRecord.getTaskRecordId()) %>"
                                var="permissionsURL" />
        
                            <portlet:actionURL var="viewURL" name="viewTaskRecord"
                                windowState="<%= LiferayWindowState.POP_UP.toString() %>">
                                <portlet:param name="redirect" value="<%= currentURL %>" />
                                <portlet:param name="taskRecordId"
                                    value="<%= String.valueOf(taskRecord.getTaskRecordId()) %>" />
                                <portlet:param name="mvcPath" value="/html/view_task_record.jsp" />
                                <portlet:param name="windowId" value="viewTaskRecord" />
                            </portlet:actionURL>
        
                            <%
                            
//                                 String taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "edit-x", taskRecord.getTaskRecordId())) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";
//                                 String taglibViewURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "viewTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "view-x", taskRecord.getTaskRecordId())) + "', uri:'" + HtmlUtil.escapeJS(viewURL) + "'});";
                            
                                boolean hasDeletePermission = TaskRecordPermission.contains(permissionChecker,
                                        taskRecord.getTaskRecordId(), ActionKeys.DELETE);   
                                boolean hasPermissionsPermission = TaskRecordPermission.contains(permissionChecker,
                                        taskRecord.getTaskRecordId(), ActionKeys.PERMISSIONS);  
                                boolean hasUpdatePermission = TaskRecordPermission.contains(permissionChecker,
                                        taskRecord.getTaskRecordId(), ActionKeys.UPDATE);
                                boolean hasViewPermission = TaskRecordPermission.contains(permissionChecker,
                                        taskRecord.getTaskRecordId(), ActionKeys.VIEW);
        
                                String detailURL = null;
        
                                if (hasUpdatePermission) {                                    
                                    detailURL = editURL;                                     
//                                     detailURL = taglibEditURL;                                     
                                } else if (hasViewPermission) {
                                    detailURL = viewURL;  
//                                     detailURL = taglibViewURL;  
                                }
                            %>
        
                            <%@ include file="/html/search_columns.jspf"%>
        
                            <liferay-ui:search-container-column-text align="right">
        
                                <liferay-ui:icon-menu>
        
                                    <c:if test="<%= hasUpdatePermission %>">
                                        <liferay-ui:icon image="edit" url="<%=editURL%>" />
<%--                                         <liferay-ui:icon image="edit" url="<%=taglibEditURL%>" /> --%>
                                    </c:if>
                                    <c:if test="<%= hasPermissionsPermission %>">
                                        <liferay-ui:icon image="permissions" url="<%= permissionsURL %>" />
                                    </c:if>
                                    <c:if test="<%= hasViewPermission %>">
                                        <liferay-ui:icon image="view" url="<%=viewURL%>" />
<%--                                         <liferay-ui:icon image="view" url="<%=taglibViewURL%>" /> --%>
                                    </c:if>
                                    <c:if test="<%= hasViewPermission %>">
                                        <liferay-ui:icon image="download" url="<%= downloadVCardURL %>" />
                                    </c:if>
                                    <c:if test="<%= hasDeletePermission %>">
                                        <liferay-ui:icon-delete url="<%=deleteURL%>" />
                                    </c:if>
        
                                </liferay-ui:icon-menu>
        
                            </liferay-ui:search-container-column-text>
                                    
                        </liferay-ui:search-container-row>
        
                        <liferay-ui:search-iterator />                         
                        
                    </liferay-ui:search-container>
	            </div>           
            </aui:form>
            
            <aui:script use="timetracker-navigation">
            
                new Liferay.Portlet.TimetrackerNavigation(
                    {
                        displayStyle: '<%= HtmlUtil.escapeJS(displayStyle) %>',
                        namespace: '<portlet:namespace />',
                        portletId: '<%= portletDisplay.getId() %>',
                        rowIds: '<%= RowChecker.ROW_IDS %>',
                        select: {
            
                           <%
                           String[] escapedDisplayViews = new String[displayViews.length];
            
                           for (int i = 0; i < displayViews.length; i++) {
                               escapedDisplayViews[i] = HtmlUtil.escapeJS(displayViews[i]);
                           }
                           %>
            
                           displayViews: ['<%= StringUtil.merge(escapedDisplayViews, "','") %>']
                       }
                    }); 
            </aui:script>            
            
            <aui:script>
                Liferay.provide(window, '<portlet:namespace />editSet', 
                    function(cmd) {
                    
                        document.<portlet:namespace />fm.<portlet:namespace />cmd.value = cmd; 
                                    
                        submitForm(document.<portlet:namespace />fm);
                
                    }, ['liferay-util-list-fields']
                );
            </aui:script>
            
		</c:otherwise>
		
	</c:choose>
	
    <hr>
    
    <ifx-util:build-info/>

</div>