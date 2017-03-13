<%--
    import.jsp: View of the data portlet's import processes.
    
    Created:    2017-03-13 12:46 by Christian Berndt
    Modified:   2017-03-13 12:46 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.data.backgroundtask.MeasurementImportBackgroundTaskExecutor"%>

<%@page import="com.liferay.portal.service.BackgroundTaskLocalServiceUtil"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    long groupId = scopeGroupId; 
%>

<liferay-ui:header backURL="<%=backURL%>" title="data-manager" />

<%-- <liferay-ui:section> --%>
    <div id="<portlet:namespace />exportImportOptions">
        <%
            int incompleteBackgroundTaskCount = 0; 
            incompleteBackgroundTaskCount = BackgroundTaskLocalServiceUtil.getBackgroundTasksCount(groupId, MeasurementImportBackgroundTaskExecutor.class.getName(), false);
        %>
        incompleteBackgroundTaskCount = <%= incompleteBackgroundTaskCount %>
    </div>
<%-- </liferay-ui:section> --%>
