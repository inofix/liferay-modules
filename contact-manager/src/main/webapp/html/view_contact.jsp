<%--
    view_contact.jsp: view a single contact. 
    
    Created:    2015-05-22 11:23 by Christian Berndt
    Modified:   2015-05-22 11:23 by Christian Berndt
    Version:    1.0.0
--%>

<%@include file="/html/init.jsp"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
%>

<div class="portlet-contact-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />

	<liferay-util:include
		servletContext="<%=session.getServletContext()%>"
		page="/html/asset/full_content.jsp" />
		
	<%--     <%@include file="/html/asset/full_content.jsp" %> --%>

</div>