<%--
    import_processes.jsp: View of the data portlet's import processes.
    
    Created:    2017-03-13 12:46 by Christian Berndt
    Modified:   2017-06-20 15:42 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.data.backgroundtask.MeasurementImportBackgroundTaskExecutor"%>

<%@page import="com.liferay.portal.service.BackgroundTaskLocalServiceUtil"%>

<%
    PortletURL backURL = renderResponse.createRenderURL();
    backURL.setParameter("mvcPath", "/html/view.jsp");
    backURL.setParameter("tabs1", "import-export");

    long groupId = scopeGroupId;
%>

<liferay-ui:header backURL="<%=backURL.toString()%>"
    title="data-manager" />

<aui:a href="<%=backURL.toString()%>" cssClass="btn btn-primary"
    label="back" />
