<%--
    page.jsp: A customized implementation of Liferay's 
    asset-links tag which provides grouped and sorted asset-links.
        
    Created:    2016-03-14 15:27 by Christian Berndt
    Modified:   2016-03-15 13:38 by Christian Berndt
    Version:    1.0.1
--%>
<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/html/taglib/ui/asset_links/init.jsp" %>

<%@page import="com.liferay.util.PropertyComparator"%>
<%@page import="javax.portlet.WindowStateException"%>

<%
long assetEntryId = GetterUtil.getLong((String)request.getAttribute("liferay-ui:asset-links:assetEntryId"));

List<AssetLink> assetLinks = null;

if (assetEntryId > 0) {
    assetLinks = AssetLinkLocalServiceUtil.getDirectLinks(assetEntryId);
}

// Custom setup code
List<AssetRenderer> assetRenderers = new ArrayList<AssetRenderer>(); 

String[] assetLinkGroups = PropsUtil.getArray("inofix.asset.link.groups");

Map<String, List<AssetRenderer>> assetRendererMap = new LinkedHashMap<String, List<AssetRenderer>>();

// Initialize the map's lists in the order they have been configured
for (String assetLinkGroup : assetLinkGroups) {
    assetRendererMap.put(assetLinkGroup, new ArrayList<AssetRenderer>()); 
}

%>

<c:if test="<%= (assetLinks != null) && !assetLinks.isEmpty() %>">

<%
for (AssetLink assetLink : assetLinks) {
    AssetEntry assetLinkEntry = null;

    if (assetLink.getEntryId1() == assetEntryId) {
        assetLinkEntry = AssetEntryLocalServiceUtil.getEntry(assetLink.getEntryId2());
    }
    else {
        assetLinkEntry = AssetEntryLocalServiceUtil.getEntry(assetLink.getEntryId1());
    }

    if (!assetLinkEntry.isVisible()) {
        continue;
    }

    assetLinkEntry = assetLinkEntry.toEscapedModel();

    String className = PortalUtil.getClassName(assetLinkEntry.getClassNameId());

    AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);

    if (Validator.isNull(assetRendererFactory)) {
        if (_log.isWarnEnabled()) {
            _log.warn("No asset renderer factory found for class " + className);
        }

        continue;
    }

    if (!assetRendererFactory.isActive(company.getCompanyId())) {
        continue;
    }

    AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(assetLinkEntry.getClassPK());

    if (assetRenderer.hasViewPermission(permissionChecker)) {
        
        // Check, whether the linked object's class
        // is configured to be grouped
        if (Arrays.asList(assetLinkGroups).contains(className)) {
            
            List<AssetRenderer> groupedAssetRenderers = assetRendererMap.get(className); 

            groupedAssetRenderers.add(assetRenderer); 
            assetRendererMap.put(className, groupedAssetRenderers);
            
        } else {
            assetRenderers.add(assetRenderer);                        
        }
    }
}
%>

    <div class="taglib-asset-links">
    
    <%  
        for (String assetRendererGroup : assetRendererMap.keySet()) { 
            
            List<AssetRenderer> groupedAssetRenderers = assetRendererMap.get(assetRendererGroup);
            
            if (groupedAssetRenderers != null && !groupedAssetRenderers.isEmpty()) {
                
	            String[] orderByProperties = PropsUtil.getArray("inofix.asset.link." + assetRendererGroup + ".orderby.properties");
		            
	            if (orderByProperties.length > 0) {
	                PropertyComparator comparator = new PropertyComparator(orderByProperties, true, true);
	                Collections.sort(groupedAssetRenderers, comparator);                    
	            }    
                
    %>
	        <h2 class="asset-links-title"><liferay-ui:message key='<%= "model.resource." + assetRendererGroup %>' />:</h2>    
	        <ul class="asset-links-list">
	        <% 
	            for (AssetRenderer assetRenderer : groupedAssetRenderers) { 
	                
	                String assetLinkEntryTitle = assetRenderer.getTitle(locale);
	                
	                AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(assetRenderer.getClassName());
	                
	                AssetEntry assetLinkEntry = AssetEntryLocalServiceUtil.getEntry(assetRenderer.getClassName(), assetRenderer.getClassPK());
	                
	                LiferayPortletURL assetPublisherURL = getAssetPublisherURL(assetLinkEntry, 
	                    assetRenderer, assetRendererFactory, currentURL, plid, request, themeDisplay);
	               
	                String viewFullContentURLString = assetPublisherURL.toString();
	
	                viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect", currentURL);
	
	                String urlViewInContext = assetRenderer.getURLViewInContext((LiferayPortletRequest)portletRequest, (LiferayPortletResponse)portletResponse, viewFullContentURLString);
	
	                urlViewInContext = HttpUtil.setParameter(urlViewInContext, "inheritRedirect", true);                
	        %>
		        <li class="asset-links-list-item">
		            <liferay-ui:icon
		                label="<%= true %>"
		                message="<%= assetLinkEntryTitle %>"
		                src="<%= assetRenderer.getIconPath(portletRequest) %>"
		                url="<%= urlViewInContext %>"
		            />
		        </li>
	        <%  } %>
        </ul>
    <%      
            }
        }
    %>  
    
    <% if (assetRenderers.size() > 0) { %>
        <h2 class="asset-links-title"><liferay-ui:message key="related-assets" />:</h2>    
        <ul class="asset-links-list">
            <% for (AssetRenderer assetRenderer : assetRenderers) { %>
            <%
            String assetLinkEntryTitle = assetRenderer.getTitle(locale);
            
            AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(assetRenderer.getClassName());
            
            AssetEntry assetLinkEntry = AssetEntryLocalServiceUtil.getEntry(assetRenderer.getClassName(), assetRenderer.getClassPK());
            
            LiferayPortletURL assetPublisherURL = getAssetPublisherURL(assetLinkEntry, 
                assetRenderer, assetRendererFactory, currentURL, plid, request, themeDisplay);
            
            String viewFullContentURLString = assetPublisherURL.toString();

            viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect", currentURL);

            String urlViewInContext = assetRenderer.getURLViewInContext((LiferayPortletRequest)portletRequest, (LiferayPortletResponse)portletResponse, viewFullContentURLString);

            urlViewInContext = HttpUtil.setParameter(urlViewInContext, "inheritRedirect", true);            
            %>
	        <li class="asset-links-list-item">
	            <liferay-ui:icon
	                label="<%= true %>"
	                message="<%= assetLinkEntryTitle %>"
	                src="<%= assetRenderer.getIconPath(portletRequest) %>"
	                url="<%= urlViewInContext %>"
	            />
	        </li>
            <% } %>
        </ul>
    <% } %>
    
    </div>

</c:if>


<%!
    private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.taglib.ui.asset_links.page_jsp");

    private static LiferayPortletURL getAssetPublisherURL(
        AssetEntry assetLinkEntry, AssetRenderer assetRenderer,
        AssetRendererFactory assetRendererFactory, String currentURL,
        long plid, HttpServletRequest request, ThemeDisplay themeDisplay) throws WindowStateException {

        LiferayPortletURL assetPublisherURL =
            new PortletURLImpl(
                request, PortletKeys.ASSET_PUBLISHER, plid,
                PortletRequest.RENDER_PHASE);

        assetPublisherURL.setParameter(
            "struts_action", "/asset_publisher/view_content");
        assetPublisherURL.setParameter("redirect", currentURL);
        assetPublisherURL.setParameter(
            "assetEntryId", String.valueOf(assetLinkEntry.getEntryId()));
        assetPublisherURL.setParameter("type", assetRendererFactory.getType());

        if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
            if (assetRenderer.getGroupId() != themeDisplay.getSiteGroupId()) {
                assetPublisherURL.setParameter(
                    "groupId", String.valueOf(assetRenderer.getGroupId()));
            }

            assetPublisherURL.setParameter(
                "urlTitle", assetRenderer.getUrlTitle());
        }

        if (themeDisplay.isStatePopUp()) {
            assetPublisherURL.setWindowState(LiferayWindowState.POP_UP);
        }
        else {
            assetPublisherURL.setWindowState(WindowState.MAXIMIZED);
        }

        return assetPublisherURL;
    }
%>

