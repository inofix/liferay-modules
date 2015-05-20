<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2015-05-19 17:01 by Christian Berndt
    Version:    1.0.2
--%>

<%@include file="/html/init.jsp"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
	String mvcPath = ParamUtil.getString(request, "mvcPath");

	Contact contact = (Contact) request.getAttribute("CONTACT");

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
		<aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
		<aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />
		<aui:input name="contactId" type="hidden"
			value="<%=String.valueOf(contact.getContactId())%>" />
        <aui:input name="uid" type="hidden" value="<%=contact.getUid() %>" />

		<liferay-ui:form-navigator categorySections="<%=categorySections%>"
			categoryNames="<%=categoryNames%>" jspPath="/html/edit_contact/" />

	</aui:form>
	
</div>
