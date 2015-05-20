<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:    2015-05-07 15:18 by Christian Berndt
    Modified:   2015-05-20 16:16 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.ArrayList"%>

<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>

<%@page import="ch.inofix.portlet.contact.search.ContactChecker"%>
<%@page import="ch.inofix.portlet.contact.service.ContactLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.contact.service.ContactServiceUtil"%>
<%@page import="ch.inofix.portlet.contact.service.permission.ContactPortletPermission"%>

<theme:defineObjects />

<%
	String backURL = ParamUtil.getString(request, "backURL");
    String keywords = ParamUtil.getString(request, "keywords"); 
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);
    
%>

<%
    Log log = LogFactoryUtil.getLog("docroot.html.view.jsp"); 

    int delta = 20; 
    int idx = ParamUtil.getInteger(request, "cur");
    if (idx > 0) idx = idx -1;
    int start = delta * idx; 
    int end = delta * idx + delta;
    
	SearchContext searchContext = SearchContextFactory
			.getInstance(request);

	searchContext.setKeywords(keywords);
	searchContext.setAttribute("paginationType", "more");
	searchContext.setStart(start);
	searchContext.setEnd(end);

	Indexer indexer = IndexerRegistryUtil.getIndexer(Contact.class);

	Hits hits = indexer.search(searchContext);

	List<Contact> contacts = new ArrayList<Contact>();

	for (int i = 0; i < hits.getDocs().length; i++) {
		Document doc = hits.doc(i);

		long contactId = GetterUtil
				.getLong(doc.get(Field.ENTRY_CLASS_PK));

		Contact contact_ = null;

		try {
			contact_ = ContactLocalServiceUtil.getContact(contactId);
		} catch (PortalException pe) {
			log.error(pe.getLocalizedMessage());
		} catch (SystemException se) {
			log.error(se.getLocalizedMessage());
		}

		contacts.add(contact_);
	}

%>

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
	    
            <portlet:actionURL var="addURL" name="editContact">
                <portlet:param name="mvcPath" value="/html/edit_contact.jsp"/>
                <portlet:param name="backURL" value="<%= currentURL %>"/>
            </portlet:actionURL>
            
            <portlet:renderURL var="clearURL">
            </portlet:renderURL>

            <aui:row>
                <c:if test='<%=ContactPortletPermission.contains(permissionChecker, 
                    scopeGroupId,"ADD_CONTACT")%>'>
                    <aui:button type="submit" value="add-contact" href="<%=addURL%>" cssClass="pull-left" />
                </c:if>

                <liferay-portlet:renderURL varImpl="searchURL">
                    <portlet:param name="mvcPath" value="/html/view.jsp" />
                </liferay-portlet:renderURL>

                <aui:form action="<%=searchURL%>" method="get" name="fm" cssClass="pull-right">
                    <liferay-portlet:renderURLParams varImpl="searchURL" />
    
                    <div class="search-form contact-search">
                        <span class="aui-search-bar"> 
                            <aui:input inlineField="<%=true%>" label=""  name="keywords" 
                               size="30" title="search-contacts" type="text" /> 
                            <aui:button type="submit" value="search" /> 
                            <aui:button value="clear" href="<%= clearURL %>" /> 
                                                     
                        </span>
                    </div>
                </aui:form>
            </aui:row>

            <liferay-ui:search-container emptyResultsMessage="no-contacts-found"
                rowChecker="<%= new ContactChecker(renderResponse) %>">
            
                <liferay-ui:search-container-results
                    results="<%= contacts %>"
                    total="<%= hits.getLength() %>" />
            
                <liferay-ui:search-container-row
                    className="ch.inofix.portlet.contact.model.Contact" modelVar="contact_">
                    
                    <portlet:actionURL var="editURL" name="editContact">
                        <portlet:param name="contactId" value="<%= String.valueOf(contact_.getContactId()) %>"/>
                        <portlet:param name="backURL" value="<%= currentURL %>"/>
                        <portlet:param name="mvcPath" value="/html/edit_contact.jsp"/>
                    </portlet:actionURL>
                    
                    <portlet:actionURL var="deleteURL" name="deleteContact">
                        <portlet:param name="contactId" value="<%= String.valueOf(contact_.getContactId()) %>"/>
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
                            <liferay-ui:icon-delete url="<%=deleteURL%>" />

                        </liferay-ui:icon-menu>
                    
                    </liferay-ui:search-container-column-text>
                    
                </liferay-ui:search-container-row>
            
                <liferay-ui:search-iterator />
            
            </liferay-ui:search-container>
 		    	    
	    </c:otherwise>
	    
	</c:choose>
	
</div>
