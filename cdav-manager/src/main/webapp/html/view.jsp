<%--
    view.jsp: Default view of the cdav manager portlet.
    
    Created:    2015-05-30 13:11 by Christian Berndt
    Modified:   2015-06-05 21:41 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
%>

<div class="portlet-cdav-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="cdav-manager" />

	<portlet:actionURL var="syncResourcesURL" name="syncResources">
	   <portlet:param name="mvcPath" value="/html/view.jsp"/>
	</portlet:actionURL>

	<aui:form action="<%=syncResourcesURL%>" method="post" name="fm">
	
	   <dl>
           <dt><liferay-ui:message key="source"/>:</dt>
           <dd><%= servername %>/<%= domain %>/<%= calendar %></dd>
           <dt><liferay-ui:message key="target"/>:</dt>
           <dd><%= calendarId %></dd>
           <dt><liferay-ui:message key="last-sync"/>:</dt>
           <dd><%= lastSync %></dd>
	   </dl>

		<aui:button type="submit" value="synchronize" />

	</aui:form>
	
	<hr>
    
    <ifx-util:build-info/>

</div>