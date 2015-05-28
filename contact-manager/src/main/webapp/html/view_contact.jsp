<%--
    view_contact.jsp: view a single contact. 
    
    Created:    2015-05-22 11:23 by Christian Berndt
    Modified:   2015-05-28 10:52 by Christian Berndt
    Version:    1.0.1
--%>

<%@include file="/html/init.jsp"%>

<%-- Import required classes --%>
<%@page import="javax.portlet.WindowState"%>

<%@page import="com.liferay.portal.kernel.util.StringPool"%>

<%@page import="com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil"%>
<%@page import="com.liferay.portlet.asset.model.AssetRenderer"%>
<%@page import="com.liferay.portlet.asset.model.AssetEntry"%>
<%@page import="com.liferay.portlet.asset.model.AssetRendererFactory"%>
<%@page import="com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil"%>

<%
    String redirect = ParamUtil.getString(request, "redirect"); 
	String backURL = ParamUtil.getString(request, "backURL", redirect);
	Contact contact_ = (Contact) request.getAttribute("CONTACT");
	String windowId = ParamUtil.getString(request, "windowId");

   // Close the popup, if we are in popup mode and a redirect was provided.
    
    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp()) {
        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        portletURL.setParameter("windowId", windowId);
        backURL = portletURL.toString();
    }

	String className = Contact.class.getName();
	long classPK = contact_.getContactId();

	AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
			className, classPK);

	AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil
			.getAssetRendererFactoryByType("contact");

	AssetRenderer assetRenderer = assetRendererFactory
			.getAssetRenderer(classPK);

	PortletURL portletURL = liferayPortletResponse.createActionURL();
	portletURL.setParameter("redirect", currentURL);
	portletURL.setParameter("windowId", "editContact"); 
		
	WindowState windowState = LiferayWindowState.POP_UP;
	
	String editURL = assetRenderer.getURLEdit(liferayPortletRequest,
			liferayPortletResponse, windowState,
			portletURL).toString();
%>

<div class="portlet-contact-manager">

	<liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />

	<c:if test="<%=assetRenderer.hasEditPermission(permissionChecker)%>">
		<div class="lfr-meta-actions asset-actions">

			<%
				String taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editContact', title: '"	+ HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "edit-x", HtmlUtil.escape(assetRenderer.getTitle(locale)))) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";
			%>

			<liferay-ui:icon image="edit" label="true" message='edit'
				url="<%=taglibEditURL%>"/>				
			
		</div>
	</c:if>

	<liferay-util:include servletContext="<%=session.getServletContext()%>"
		page="/html/asset/full_content.jsp" />

	<liferay-ui:asset-links className="<%=Contact.class.getName()%>"
		classPK="<%=contact_.getContactId()%>" />

</div>
