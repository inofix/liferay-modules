<%--
    init.jsp: common setup code of the form sections.
    
    Created:    2015-05-13 17:55 by Christian Berndt
    Modified:   2015-05-13 17:55 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp" %>

<%
	Contact contact = (Contact) request.getAttribute("CONTACT");
%>