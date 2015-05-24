<%--
    view_contact.jsp: view a single contact. 
    
    Created:    2015-05-22 11:23 by Christian Berndt
    Modified:   2015-05-24 19:03 by Christian Berndt
    Version:    1.0.1
--%>

<%@include file="/html/init.jsp"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
	Contact contact_ = (Contact) request.getAttribute("CONTACT");
%>

<div class="portlet-contact-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />

	<liferay-util:include servletContext="<%=session.getServletContext()%>"
		page="/html/asset/full_content.jsp" />

	<liferay-ui:asset-links className="<%=Contact.class.getName()%>"
		classPK="<%=contact_.getContactId()%>" />
		
</div>
