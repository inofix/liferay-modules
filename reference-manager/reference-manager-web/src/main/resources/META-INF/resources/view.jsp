<%--
    view.jsp: Default view of the reference manager portlet.
    
    Created:    2016-01-10 22:51 by Christian Berndt
    Modified:   2016-01-10 22:51 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
	String backURL = ParamUtil.getString(request, "backURL");
	String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
%>

<div class="reference-manager-portlet">

	<liferay-ui:header backURL="<%=backURL%>" title="reference-manager" />

	<liferay-ui:error exception="<%= PrincipalException.class %>"
		message="you-dont-have-the-required-permissions" />

	<liferay-ui:tabs names="browse,import,export" param="tabs1"
		url="<%=portletURL.toString()%>" />

	<c:choose>

		<c:when test='<%= tabs1.equals("import") %>'>
	        <liferay-util:include page="/import.jsp" servletContext="<%= application %>" />
		</c:when>

		<c:when test='<%= tabs1.equals("export") %>'>
            <liferay-util:include page="/export.jsp" servletContext="<%= application %>" />
		</c:when>

		<c:otherwise>

			<aui:button-row>
				<portlet:renderURL var="editReferenceURL">
					<portlet:param name="mvcPath" value="/edit_reference.jsp" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
				</portlet:renderURL>

				<aui:button href="<%= editReferenceURL %>" value="add-reference" />
			</aui:button-row>

			<liferay-ui:search-container
				total="<%= referenceLocalService.getReferencesCount() %>">

				<liferay-ui:search-container-results
					results="<%= referenceLocalService.getReferences(searchContainer.getStart(), searchContainer.getEnd()) %>" />

				<liferay-ui:search-container-row
					className="ch.inofix.referencemanager.model.Reference"
					escapedModel="true" modelVar="reference">

					<liferay-ui:search-container-column-text name="id"
						property="referenceId" valign="top" />

					<liferay-ui:search-container-column-text name="bibtex" valign="top">
						<strong><%= reference.getBibtex() %></strong>

						<br />

						<div class="lfr-asset-categories">
							<liferay-ui:asset-categories-summary
								className="<%= Reference.class.getName() %>"
								classPK="<%= reference.getReferenceId() %>" />
						</div>

						<div class="lfr-asset-tags">
							<liferay-ui:asset-tags-summary
								className="<%=Reference.class.getName()%>"
								classPK="<%=reference.getReferenceId()%>" message="tags" />
						</div>
					</liferay-ui:search-container-column-text>

					<%--
				
						<liferay-ui:search-container-column-text
							property="field2"
							valign="top"
						/>
				
						--%>

					<liferay-ui:search-container-column-jsp cssClass="entry-action"
						path="/reference_action.jsp" valign="top" />

				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator />
				
			</liferay-ui:search-container>
		</c:otherwise>
	</c:choose>
</div>
