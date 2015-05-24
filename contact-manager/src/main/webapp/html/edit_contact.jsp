<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2015-05-24 19:13 by Christian Berndt
    Version:    1.0.6
--%>

<%@include file="/html/edit_contact/init.jsp"%>

<%
	String redirect = ParamUtil.getString(request, "redirect");

	String backURL = ParamUtil.getString(request, "backURL", redirect);
	
	String mvcPath = ParamUtil.getString(request, "mvcPath");

	String tabs1 = ParamUtil.getString(request, "tabs1", "contact");

	String[] categoryNames = new String[] { "" };
	String[][] categorySections = new String[][] { { "edit_contact",
			"edit_personal_information", "edit_mailing_address",
			"edit_notes", "edit_miscellaneous", "edit_vcard" } };

	// TODO: configurable display styles?
	String displayStyle = "default"; // default, panel, steps
%>

<div class="portlet-contact-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />

	<portlet:actionURL var="saveContactURL" name="saveContact" />

	<aui:form action="<%=saveContactURL%>" method="post" name="fm">

		<aui:input name="backURL" type="hidden" value="<%=backURL%>" />
        <aui:input name="contactId" type="hidden"
            value="<%=String.valueOf(contact_.getContactId())%>" />
        <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
        <aui:input name="redirect" type="hidden" value="<%=redirect%>" />
		<aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />
        <aui:input name="uid" type="hidden" value="<%=contact_.getUid() %>" />

		<liferay-ui:form-navigator categorySections="<%=categorySections%>"
			categoryNames="<%=categoryNames%>" jspPath="/html/edit_contact/" 
			showButtons="<%= hasUpdatePermission %>" />

	</aui:form>
	
</div>
