<%--
    toolbar.jsp: The toolbar of the timetracker portlet
    
    Created:    2016-03-20 16:58 by Christian Berndt
    Modified:   2016-03-23 09:53 by Christian Berndt
    Version:    1.0.3
 --%>

<%@ include file="/html/init.jsp"%>

<%
    PortletURL portletURL = liferayPortletResponse.createRenderURL();

    TaskRecordSearch searchContainer =
        new TaskRecordSearch(liferayPortletRequest, portletURL);

    TaskRecordDisplayTerms displayTerms =
        (TaskRecordDisplayTerms) searchContainer.getDisplayTerms();
    
    long companyId = themeDisplay.getCompanyId();

    Sort sort = SortFactoryUtil.getSort(User.class, "lastName", "asc");

    int numUsers = UserLocalServiceUtil.searchCount(companyId, null,
            WorkflowConstants.STATUS_APPROVED, null);

    Hits hits = UserLocalServiceUtil.search(companyId, null,
            WorkflowConstants.STATUS_APPROVED, null, 0, numUsers, sort);

    List<Document> documents = hits.toList();    
    
    boolean ignoreEndDate = ParamUtil.getBoolean(request, "ignoreEndDate", true);
    boolean ignoreStartDate = ParamUtil.getBoolean(request, "ignoreStartDate", true);
%>

<aui:nav-bar>

    <aui:nav id="toolbarContainer">

        <aui:nav-item cssClass="hide" dropdown="<%=true%>"
            id="actionsButtonContainer" label="actions">

            <%
                String downloadTaskRecordsURL = "javascript:" + renderResponse.getNamespace() + "downloadTaskRecords();";
            %>

            <%-- <aui:nav-item href="<%=downloadTaskRecordsURL%>" iconCssClass="icon-download"
                label="download-selected-task-records" /> --%>
            
            <%
                String deleteTaskRecordsURL = "javascript:" + renderResponse.getNamespace() + "editSet('delete');";
            %>

            <aui:nav-item>
                <liferay-ui:icon-delete url="<%= deleteTaskRecordsURL %>" label="true"
                    message="delete-selected-task-records" />
            </aui:nav-item>
        </aui:nav-item>
        

        <portlet:actionURL var="addURL" name="editTaskRecord"
            windowState="<%= LiferayWindowState.POP_UP.toString() %>">
            <portlet:param name="mvcPath" value="/html/edit_task_record.jsp" />
            <portlet:param name="redirect" value="<%= currentURL %>" />
            <portlet:param name="windowId" value="editTaskRecord" />
        </portlet:actionURL>

    <c:if test='<%=TimetrackerPortletPermission.contains(
                            permissionChecker, scopeGroupId,
                            ActionKeys.ADD_TASK_RECORD)%>'>
        <%
            String taglibAddURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "edit-x", "new")) + "', uri:'" + HtmlUtil.escapeJS(addURL)+ "'});";
        %>

        <aui:button type="submit" value="add-task-record"
            href="<%=taglibAddURL%>" cssClass="pull-left add-task-record" />

    </c:if>

    </aui:nav>

    <aui:nav-bar-search cssClass="pull-right">

		<liferay-portlet:renderURL varImpl="searchURL"/>
		
		<aui:form action="<%= searchURL %>" method="get" name="fm">
		
		    <liferay-portlet:renderURLParams varImpl="searchURL" />
			
			<liferay-ui:search-toggle 
			    buttonLabel="search"
				displayTerms="<%=displayTerms%>"
				id="toggle_id_timetracker_search"
				>
				
				<aui:fieldset>
                    <aui:input name="<%= TaskRecordSearchTerms.WORK_PACKAGE %>" value="<%= displayTerms.getWorkPackage() %>"/>
                    <aui:input name="<%= TaskRecordSearchTerms.DESCRIPTION %>" value="<%= displayTerms.getDescription() %>"/>
		            <aui:input dateTogglerCheckboxLabel="ignore-start-date"
		                disabled="<%= ignoreStartDate %>" formName="fm"
		                name="startDate" model="<%= TaskRecord.class %>" inlineField="true"/>
		
		            <aui:input dateTogglerCheckboxLabel="ignore-end-date"
		                disabled="<%= ignoreEndDate %>" formName="fm"
		                name="endDate" model="<%= TaskRecord.class %>" inlineField="true"/>
		                
		            <aui:select name="userId"
		                inlineField="true"
		                last="true">
		                <aui:option value="-1">
		                    <liferay-ui:message key="any-user" />
		                </aui:option>
		                <c:forEach items="<%=documents%>" var="document">
		                    <aui:option value="${document.get('userId')}">${document.get('fullName')}</aui:option>
		                </c:forEach>
		            </aui:select>		                

				</aui:fieldset>
				
			</liferay-ui:search-toggle>
			
	        <portlet:renderURL var="clearURL" />
			
            <aui:button value="reset" href="<%=clearURL%>" cssClass="clear-btn" />
            
		</aui:form>

	</aui:nav-bar-search>

</aui:nav-bar>
