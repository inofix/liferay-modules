<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:    2015-05-07 15:18 by Christian Berndt
    Modified:   2015-05-07 15:18 by Christian Berndt
    Version:    1.0.0
--%>

<%-- Import required classes --%>
<%@page import="ch.inofix.portlet.contact.search.ContactChecker"%>
<%@page import="ch.inofix.portlet.contact.service.ContactLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.contact.service.ContactServiceUtil"%>

<%@ include file="/html/init.jsp"%>

<%
	String backURL = ParamUtil.getString(request, "backURL");
	
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);	
%>

<portlet:renderURL var="browseURL"/>

<div class="portlet-contact-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />
	
	<liferay-ui:tabs
	    names="browse,import-export"
	    param="tabs1" url="<%= portletURL.toString() %>" />	
	    
	<c:choose>
	
	    <c:when test='<%= tabs1.equals("import-export") %>'>
	        <%@include file="/html/import_vcards.jspf"%>
	    </c:when>	    

	    <c:otherwise>
		    <liferay-ui:search-container emptyResultsMessage="no-contacts-found"
		      rowChecker="<%= new ContactChecker(renderResponse) %>">
		    
		        <liferay-ui:search-container-results
		            results="<%=ContactLocalServiceUtil.getContacts(
		                            searchContainer.getStart(), 
		                            searchContainer.getEnd())%>"
		            total="<%=ContactLocalServiceUtil.getContactsCount()%>" />
		    
		        <liferay-ui:search-container-row
		            className="ch.inofix.portlet.contact.model.Contact" modelVar="contact">
		            
		            <portlet:actionURL var="editURL" name="editContact">
		                <%
		                   // TODO: use contactId instead of id
		                %>
		                <portlet:param name="id" value="<%= String.valueOf(contact.getId()) %>"/>
		                <portlet:param name="contactId" value="<%= contact.getContactId() %>"/>
		                <portlet:param name="backURL" value="<%= currentURL %>"/>
		                <portlet:param name="mvcPath" value="/html/edit_contact.jsp"/>
		            </portlet:actionURL>
		            
                    <portlet:actionURL var="deleteURL" name="deleteContact">
                        <%
                           // TODO: use contactId instead of id
                        %>
                        <portlet:param name="id" value="<%= String.valueOf(contact.getId()) %>"/>
                        <portlet:param name="contactId" value="<%= contact.getContactId() %>"/>
                        <portlet:param name="backURL" value="<%= currentURL %>"/>
                        <portlet:param name="mvcPath" value="/html/view.jsp"/>
                    </portlet:actionURL> 
                    
                    <%
                         String detailURL = null; 
                    
                         // TODO: Check permission
                         detailURL = editURL.toString();      
                    %>
                                                                                		            
		            <%@ include file="/html/search_columns.jspf" %>
		                
		            <liferay-ui:search-container-column-text align="right">

						<liferay-ui:icon-menu>

							<%
								// TODO: Check permissions
							%>

                            <liferay-ui:icon image="edit" url="<%=editURL%>" />
                            <liferay-ui:icon image="view" url="<%=editURL%>" />
                            <liferay-ui:icon image="delete" url="<%=deleteURL%>" />

						</liferay-ui:icon-menu>
		            
		            </liferay-ui:search-container-column-text>
		            
		        </liferay-ui:search-container-row>
		    
		        <liferay-ui:search-iterator />
		    
		    </liferay-ui:search-container>
		    	    
	    </c:otherwise>
	</c:choose>
	
</div>