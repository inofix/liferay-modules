<%--
    full_content.jsp: The full_content template for contact assets. 
    
    Created:    2015-05-19 17:41 by Christian Berndt
    Modified:   2015-05-20 21:45 by Christian Berndt
    Version:    1.0.1
--%>

<%@include file="/html/init.jsp" %>

<%
	Contact contact = (Contact)request.getAttribute("CONTACT");
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

<div>
    <%= contact.getVCardHTML() %>
</div>

<liferay-ui:custom-attributes-available className="<%= Contact.class.getName() %>">
    <liferay-ui:custom-attribute-list
        className="<%= Contact.class.getName() %>"
        classPK="<%= (contact != null) ? contact.getPrimaryKey() : 0 %>"
        editable="<%= false %>"
        label="<%= true %>"
    />
</liferay-ui:custom-attributes-available>
