<%--
    toolbar.jsp: The toolbar of the timetracker portlet
    
    Created:    2016-03-20 16:58 by Christian Berndt
    Modified:   2016-03-20 16:58 by Christian Berndt
    Version:    1.0.0
 --%>

<%@ include file="/html/init.jsp"%>

<aui:nav-bar>

    <aui:nav id="toolbarContainer">

        <aui:nav-item cssClass="hide" dropdown="<%=true%>"
            id="actionsButtonContainer" label="actions">

            <%
                String downloadTaskRecordsURL = "javascript:" + renderResponse.getNamespace() + "downloadTaskRecords();";
            %>

            <aui:nav-item href="<%=downloadTaskRecordsURL%>" iconCssClass="icon-download"
                label="download-selected-task-records" />
            
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

<%
   // TODO: Fix missing "ADD_TASK_RECORD" resource issue.
%>
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

        <%
            // TODO: Move to search.jsp
        %>
        <portlet:renderURL var="clearURL" />

        <liferay-portlet:renderURL varImpl="searchURL">
            <portlet:param name="mvcPath" value="/html/view.jsp" />
        </liferay-portlet:renderURL>

        <div class="form-search">
            <aui:form action="<%=searchURL%>" method="get" name="fm1"
                cssClass="pull-right">
                <liferay-portlet:renderURLParams varImpl="searchURL" />

                <div class="search-form task-record-search">
                    <span class="aui-search-bar"> <aui:input
                            inlineField="<%=true%>" label="" name="keywords" size="30"
                            title="search-task-records" type="text" /> <aui:button type="submit"
                            value="search" /> <aui:button value="clear"
                            href="<%=clearURL%>" />
                    </span>
                </div>
            </aui:form>
        </div>

    </aui:nav-bar-search>

</aui:nav-bar>
