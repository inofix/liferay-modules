<%-- 
    edit_contact/edit_vcard.jsp: Edit the vCard String of the contact.
    
    Created:    2015-05-08 15:42 by Christian Berndt
    Modified:   2015-05-08 15:42 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/edit_contact/init.jsp" %>

<%
    // TODO: Add error handling
%>

<aui:input name="vCard" type="textarea" cssClass="v-card"
    value="<%= contact.getCard() %>" />
    