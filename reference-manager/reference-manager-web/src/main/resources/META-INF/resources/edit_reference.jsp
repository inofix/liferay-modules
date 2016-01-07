<%@ include file="/init.jsp"%>

<%
	String redirect = ParamUtil.getString(request, "redirect");

	long referenceId = ParamUtil.getLong(request, "referenceId");

	Reference reference = null;

	if (referenceId > 0) {
		reference = referenceLocalService.getReference(referenceId);
	}
%>

<aui:form action="<%=renderResponse.createActionURL()%>" method="post"
	name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=reference == null ? Constants.ADD : Constants.UPDATE%>" />
	<aui:input name="redirect" type="hidden" value="<%=currentURL%>" />
	<aui:input name="referenceId" type="hidden" value="<%=referenceId%>" />

	<liferay-ui:header backURL="<%=redirect%>"
		title='<%=(reference != null) ? reference.getBibtex() : "new-reference"%>' />

	<liferay-ui:asset-categories-error />

	<liferay-ui:asset-tags-error />

	<aui:model-context bean="<%=reference%>" model="<%=Reference.class%>" />

	<aui:fieldset>
		<aui:input name="bibtex" type="textarea" />

		<liferay-ui:custom-attributes-available
			className="<%=Reference.class.getName()%>">
			<liferay-ui:custom-attribute-list
				className="<%=Reference.class.getName()%>"
				classPK="<%=(reference != null) ? reference.getReferenceId() : 0%>"
				editable="<%=true%>" label="<%=true%>" />
		</liferay-ui:custom-attributes-available>

		<aui:input name="categories" type="assetCategories" />

		<aui:input name="tags" type="assetTags" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />

		<aui:button href="<%=redirect%>" type="cancel" />
	</aui:button-row>
</aui:form>
