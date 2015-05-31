<%--
    configuration.jsp: Configure the cdav-manager's preferences.
    
    Created:    2015-05-30 12:14 by Christian Berndt
    Modified:   2015-05-30 12:14 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
	var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm">

	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />
	<aui:input name="redirect" type="hidden"
		value="<%=configurationRenderURL%>" />

	<aui:fieldset label="connection-settings">
		<aui:input name="servername" value="<%=servername%>"
			helpMessage="servername-help" />
		<aui:input name="domain" value="<%=domain%>"
			helpMessage="domain-help" />
		<aui:input name="username" value="<%=username%>"
			helpMessage="username-help" />
		<aui:input name="password" value="<%=password%>"
			helpMessage="password-help" type="password" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>

</aui:form>
