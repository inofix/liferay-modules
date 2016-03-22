<%--
    view_task_record.jsp: view a single task-record.. 
    
    Created:    2016-03-22 09:16 by Christian Berndt
    Modified:   2016-03-23 00:27 by Christian Berndt
    Version:    1.0.1
--%>

<%@include file="/html/init.jsp"%>

<%-- Import required classes --%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>

<%@page import="com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil"%>
<%@page import="com.liferay.portlet.asset.model.AssetRenderer"%>
<%@page import="com.liferay.portlet.asset.model.AssetEntry"%>
<%@page import="com.liferay.portlet.asset.model.AssetRendererFactory"%>
<%@page import="com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");
    String backURL = ParamUtil.getString(request, "backURL", redirect);
    TaskRecord taskRecord = (TaskRecord) request.getAttribute("TASK_RECORD");
    String windowId = ParamUtil.getString(request, "windowId");

    // Close the popup, if we are in popup mode and a redirect was provided.

    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp()) {
        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        portletURL.setParameter("redirect", redirect);
        portletURL.setParameter("windowId", windowId);
        backURL = portletURL.toString();
    }

    String className = TaskRecord.class.getName();
    long classPK = taskRecord.getTaskRecordId();

//     AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
//             className, classPK);

//     AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil
//             .getAssetRendererFactoryByType("contact");

//     AssetRenderer assetRenderer = assetRendererFactory
//             .getAssetRenderer(classPK);

    // Create a fresh viewURL in order to get rid of any
    // redirect parameters. Pass it as a request attribute to the 
    // assetRenderer to be sure, that it does not interfere with
    // the default behaviour of the getURLEdit method.
    PortletURL viewURL = liferayPortletResponse.createActionURL();
    viewURL.setParameter("mvcPath", "/html/view_task_record.jsp");
    viewURL.setParameter("javax.portlet.action", "viewTaskRecord");
    viewURL.setParameter("contactId",
            String.valueOf(taskRecord.getTaskRecordId()));
    viewURL.setParameter("backURL", backURL); 
    
    liferayPortletRequest.setAttribute("backURL", viewURL.toString());

    PortletURL portletURL = liferayPortletResponse.createActionURL();

    WindowState windowState = LiferayWindowState.POP_UP;

//     String editURL = assetRenderer.getURLEdit(liferayPortletRequest,
//             liferayPortletResponse, windowState, portletURL).toString();
%>


<liferay-ui:header backURL="<%=backURL%>" title="timetracker" />

not yet implemented
<%-- 
<c:if test="<%=assetRenderer.hasEditPermission(permissionChecker)%>">
	<div class="lfr-meta-actions asset-actions">

		<%
           String taglibEditURL = null;

           // Do not open a new pop-up if we're already in popup-mode
           if (themeDisplay.isStatePopUp()) {
               taglibEditURL = editURL;
           } else {
               taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "edit-x", HtmlUtil.escape(assetRenderer.getTitle(locale)))) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";
           }
       %>

		<liferay-ui:icon image="edit" label="true" message='edit'
			url="<%=taglibEditURL%>" />

	</div>
</c:if>
--%>

<%--
<liferay-util:include servletContext="<%=session.getServletContext()%>"
	page="/html/asset/full_content.jsp" />
 --%>

<liferay-ui:asset-links className="<%=TaskRecord.class.getName()%>"
	classPK="<%=taskRecord.getTaskRecordId()%>" />
	