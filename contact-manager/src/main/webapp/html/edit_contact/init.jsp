<%--
    init.jsp: common setup code of the form sections.
    
    Created:    2015-05-13 17:55 by Christian Berndt
    Modified:   2015-05-25 17:48 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<theme:defineObjects />

<%
	Contact contact_ = (Contact) request.getAttribute("CONTACT");

	boolean hasUpdatePermission = ContactPermission.contains(
			permissionChecker, contact_.getContactId(),
			ActionKeys.UPDATE);
%>
