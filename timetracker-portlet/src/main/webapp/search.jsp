<%--
    search.jsp: The search form of the timetracker portlet

    Created:     2013-10-15 11:41 by Christian Berndt
    Modified:    2014-03-30 15:00 by Christian Berndt
    Version:     1.3

--%>

<%@page import="com.liferay.portal.kernel.util.CalendarFactoryUtil"%>
<%@page import="java.util.Calendar"%>


<%@ include file="/init.jsp" %>

<%
    TaskRecordSearch searchContainer = (TaskRecordSearch) request
            .getAttribute("liferay-ui:search:searchContainer");

    TaskRecordDisplayTerms displayTerms = (TaskRecordDisplayTerms) searchContainer
            .getDisplayTerms();

    displayTerms.setAdvancedSearch(true);

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

<div class="aui-search-bar lfr-display-terms-search">

    <div class="taglib-search-toggle">
<%--
    <liferay-ui:search-toggle
        displayTerms="<%=displayTerms%>" id="project_search_toggle_id"
        buttonLabel="search">
 --%>
        <aui:fieldset>

            <aui:input name="<%=TaskRecordFields.WORK_PACKAGE%>"
                value="<%=displayTerms.getWorkPackage()%>"
                inlineField="true"
                first="true" />

            <aui:input name="<%=TaskRecordFields.DESCRIPTION%>"
                value="<%=displayTerms.getDescription()%>"
                inlineField="true" />

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
                    <liferay-ui:message key="select-user" />
                </aui:option>
                <c:forEach items="<%=documents%>" var="document">
                    <aui:option value="${document.get('userId')}">${document.get('fullName')}</aui:option>
                </c:forEach>
            </aui:select>

            <aui:button type="submit" value="search"/>

        </aui:fieldset>

<%--     </liferay-ui:search-toggle> --%>

    </div>
</div>
