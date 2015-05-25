<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:    2015-05-07 15:18 by Christian Berndt
    Modified:   2015-05-24 23:06 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%@page import="ch.inofix.portlet.contact.search.ContactChecker"%>
<%@page import="ch.inofix.portlet.contact.service.ContactLocalServiceUtil"%>
<%@page import="ch.inofix.portlet.contact.service.ContactServiceUtil"%>
<%@page import="ch.inofix.portlet.contact.service.permission.ContactPortletPermission"%>

<theme:defineObjects />

<%
	String backURL = ParamUtil.getString(request, "backURL");
    int delta = ParamUtil.getInteger(request, "delta", 20); 
    int idx = ParamUtil.getInteger(request, "cur");
    String keywords = ParamUtil.getString(request, "keywords"); 
    String orderByCol = ParamUtil.getString(request, "orderByCol", "modified"); 
    String orderByType = ParamUtil.getString(request, "orderByType", "desc"); 
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);  
%>

<%
	Log log = LogFactoryUtil.getLog("docroot.html.view.jsp");

	if (idx > 0) {
		idx = idx - 1;
	}
	int start = delta * idx;
	int end = delta * idx + delta;

	SearchContext searchContext = SearchContextFactory
			.getInstance(request);
	
	boolean reverse = "desc".equals(orderByType); 

	Sort sort = new Sort(orderByCol, reverse);

	searchContext.setKeywords(keywords);
	searchContext.setAttribute("paginationType", "more");
	searchContext.setStart(start);
	searchContext.setEnd(end);
	searchContext.setSorts(sort);

	Indexer indexer = IndexerRegistryUtil.getIndexer(Contact.class);

	Hits hits = indexer.search(searchContext);

	List<Contact> contacts = new ArrayList<Contact>();

	for (int i = 0; i < hits.getDocs().length; i++) {
		Document doc = hits.doc(i);

		long contactId = GetterUtil.getLong(doc
				.get(Field.ENTRY_CLASS_PK));

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
	
	<liferay-ui:error exception="<%= PrincipalException.class %>" 
	   message="you-dont-have-the-required-permissions"/>
	
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
                    scopeGroupId,ActionKeys.ADD_CONTACT)%>'>
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
            
            <%
               // TODO: implement set operations: deleteContacts, exportContacts, ....
            %>

			<liferay-ui:search-container 
			     searchContainer="<%= new ContactSearch(renderRequest, portletURL) %>"
			     var="contactSearchContainer">
            
                <liferay-ui:search-container-results
                    results="<%= contacts %>"
                    total="<%= hits.getLength() %>" />

				<liferay-ui:search-container-row
					className="ch.inofix.portlet.contact.model.Contact"
					modelVar="contact_" keyProperty="contactId">

                    <portlet:actionURL var="deleteURL" name="deleteContact">
                        <portlet:param name="contactId" value="<%= String.valueOf(contact_.getContactId()) %>"/>
                        <portlet:param name="backURL" value="<%= currentURL %>"/>
                        <portlet:param name="mvcPath" value="/html/view.jsp"/>
                    </portlet:actionURL> 
                                       
                    <portlet:actionURL var="editURL" name="editContact">
                        <portlet:param name="contactId" value="<%= String.valueOf(contact_.getContactId()) %>"/>
                        <portlet:param name="backURL" value="<%= currentURL %>"/>
                        <portlet:param name="mvcPath" value="/html/edit_contact.jsp"/>
                    </portlet:actionURL>
                    
                    <%
                        StringBuilder sb = new StringBuilder(); 
                    
                        sb.append(LanguageUtil.get(pageContext, "permissions-of-contact")); 
                        sb.append(" "); 
                        sb.append(contact_.getFullName(true)); 
                    
                        String modelResourceDescription = sb.toString(); 
                    %>
                    
                    <liferay-security:permissionsURL
                        modelResource="<%= Contact.class.getName() %>"
                        modelResourceDescription="<%= modelResourceDescription %>"
                        resourcePrimKey="<%= String.valueOf(contact_.getContactId()) %>"
                        var="permissionsURL" /> 
                    
                    <portlet:actionURL var="viewURL" name="viewContact">
                        <portlet:param name="contactId" value="<%= String.valueOf(contact_.getContactId()) %>"/>
                        <portlet:param name="backURL" value="<%= currentURL %>"/>
                        <portlet:param name="mvcPath" value="/html/view_contact.jsp"/>
                    </portlet:actionURL>

					<%
	                    boolean hasDeletePermission = ContactPermission.contains(permissionChecker,
	                            contact_.getContactId(), ActionKeys.DELETE);   
	                    boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker,
	                            contact_.getContactId(), ActionKeys.PERMISSIONS);  
	                    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
	                            contact_.getContactId(), ActionKeys.UPDATE);
	                    boolean hasViewPermission = ContactPermission.contains(permissionChecker,
	                            contact_.getContactId(), ActionKeys.VIEW);

						String detailURL = viewURL;

						if (hasUpdatePermission) {
							detailURL = editURL.toString();
						} else if (hasViewPermission) {
							detailURL = viewURL.toString(); 
						}
					%>

					<%@ include file="/html/search_columns.jspf" %>

					<liferay-ui:search-container-column-text align="right">

                        <liferay-ui:icon-menu>

                            <c:if test="<%= hasUpdatePermission %>">
	                            <liferay-ui:icon image="edit" url="<%=editURL%>" />
                            </c:if>
                            <c:if test="<%= hasPermissionsPermission %>">
	                            <liferay-ui:icon image="permissions" url="<%= permissionsURL %>" />
                            </c:if>
                            <c:if test="<%= hasViewPermission %>">
	                            <liferay-ui:icon image="view" url="<%=viewURL%>" />
                            </c:if>
                            <c:if test="<%= hasDeletePermission %>">
	                            <liferay-ui:icon-delete url="<%=deleteURL%>" />
                            </c:if>

                        </liferay-ui:icon-menu>
                    
                    </liferay-ui:search-container-column-text>


				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator />
            
            </liferay-ui:search-container>
             		    	    
	    </c:otherwise>
	    
	</c:choose>
	
</div>
