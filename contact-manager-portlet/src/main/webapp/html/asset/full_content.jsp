<%--
    full_content.jsp: The full_content template for contact assets. 
    
    Created:    2015-05-19 17:41 by Christian Berndt
    Modified:   2015-07-06 14:36 by Christian Berndt
    Version:    1.0.3
--%>

<%@include file="/html/init.jsp" %>

<%
	Contact contact_ = (Contact)request.getAttribute("CONTACT");
%>

<style>
<!-- 
	/**
	 * We do the default styling of the vCard here
	 * because the portlet's main.css is not read
	 * by the AssetRenderer.
	 */
    .vcard table:nth-of-type(2) {
        margin-top: 20px;
        margin-bottom: 20px;
    }
    
	.vcard table:nth-of-type(2) td {
		width: 33%;
	}
	
	.vcard .indent {
	   border-left: none; 
	   margin-left: 0; 
	   padding-left: 0; 
	}
	
	.vcard .l {
	   font-variant: normal;
	}
	

-->
</style>

<portlet:resourceURL var="serveVCardURL" id="serveVCard">
    <portlet:param name="contactId"
        value="<%= String.valueOf(contact_.getContactId()) %>" />
</portlet:resourceURL>

<div> 
    <%= contact_.getVCardHTML() %>
    <aui:button-row>
	    <aui:button href="<%=serveVCardURL%>" value="download" />
    </aui:button-row>
</div>

<liferay-ui:custom-attributes-available className="<%= Contact.class.getName() %>">
    <liferay-ui:custom-attribute-list
        className="<%= Contact.class.getName() %>"
        classPK="<%= (contact_ != null) ? contact_.getPrimaryKey() : 0 %>"
        editable="<%= false %>"
        label="<%= true %>"
    />
</liferay-ui:custom-attributes-available>
