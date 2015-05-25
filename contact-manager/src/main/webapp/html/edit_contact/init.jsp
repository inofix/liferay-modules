<%--
    init.jsp: common setup code of the form sections.
    
    Created:    2015-05-13 17:55 by Christian Berndt
    Modified:   2015-05-25 20:25 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp"%>

<theme:defineObjects />

<%
	Contact contact_ = (Contact) request.getAttribute("CONTACT");

	boolean hasUpdatePermission = false;

	long contactId = contact_.getContactId();

	if (contactId > 0) {
		hasUpdatePermission = ContactPermission.contains(
				permissionChecker, contactId, ActionKeys.UPDATE);
	} else {
		// it's a new contact
		hasUpdatePermission = true;
	}
%>
