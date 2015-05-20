<%--
    init.jsp: common setup code of the form sections.
    
    Created:    2015-05-13 17:55 by Christian Berndt
    Modified:   2015-05-20 18:04 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp" %>

<%
	Contact contact_ = (Contact) request.getAttribute("CONTACT");
%>

<theme:defineObjects/>
