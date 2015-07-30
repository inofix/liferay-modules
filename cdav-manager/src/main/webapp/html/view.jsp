<%--
    view.jsp: Default view of the cdav manager portlet.
    
    Created:    2015-05-30 13:11 by Christian Berndt
    Modified:   2015-07-30 19:11 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>
<%@page import="ch.inofix.portlet.cdav.NoCalendarSelectedException"%>
<%@page import="ch.inofix.portlet.cdav.SyncConnectionException"%>
<%@page import="ch.inofix.portlet.cdav.SyncWithCalDAVServerException"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
%>

<div class="portlet-cdav-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="cdav-manager" />
	
	<liferay-ui:error exception="<%= NoCalendarSelectedException.class %>" 
	   message="you-havent-selected-a-calendar-yet"/>
	<liferay-ui:error exception="<%= SyncConnectionException.class %>"
	   message="cant-establish-a-connection-to-the-cdav-server"/>
    <liferay-ui:error exception="<%= SyncWithCalDAVServerException.class %>"
       message="cant-sync-with-the-caldav-server"/>      
	   
	<portlet:actionURL var="syncResourcesURL" name="syncResources">
	   <portlet:param name="mvcPath" value="/html/view.jsp"/>
	</portlet:actionURL>

	<aui:form action="<%=syncResourcesURL%>" method="post" name="fm">
		<aui:row>
			<dl>
				<dt>
					<liferay-ui:message key="caldav-server" />:
				</dt>
				<c:choose>
					<c:when test="<%=hasConnectionParameters%>">
						<dd><%=servername%>/<%=domain%>/<%=username%>/<%=calendar%></dd>
					</c:when>
					<c:otherwise>
						<dd>
							<liferay-ui:message key="not-configured" />
						</dd>
					</c:otherwise>
				</c:choose>
				<dt>
					<liferay-ui:message key="target-calendar" />:
				</dt>
				<dd><%=targetCalendarName%></dd>
				<%--            <dt><liferay-ui:message key="last-sync"/>:</dt> --%>
				<%--            <dd><%= lastSync %></dd> --%>
			</dl>

			<aui:button type="submit" value="synchronize"
				disabled="<%=!hasConnectionParameters%>" />

			<c:if test="<%=!hasConnectionParameters%>">
				<div class="alert alert-warn pull-right">
					<liferay-ui:message key="please-configure-your-sync-settings" />
				</div>
			</c:if>
		</aui:row>
	</aui:form>

	<hr>
    
    <ifx-util:build-info/>

</div>