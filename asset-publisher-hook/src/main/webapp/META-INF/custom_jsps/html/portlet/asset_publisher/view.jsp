<%--
    view.jsp: Customized default view of Liferay's asset-publisher
    portlet which adds dynamic loading of pages to the asset-publisher.
        
    Created:    2017-01-24 17:42 by Christian Berndt
    Modified:   2017-04-16 14:04 by Christian Berndt
    Version:    1.0.2
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

<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
if (mergeUrlTags || mergeLayoutTags) {
    String[] compilerTagNames = new String[0];

    if (mergeUrlTags) {
        compilerTagNames = ParamUtil.getParameterValues(request, "tags");
    }

    if (mergeLayoutTags) {
        Set<String> layoutTagNames = AssetUtil.getLayoutTagNames(request);

        if (!layoutTagNames.isEmpty()) {
            compilerTagNames = ArrayUtil.append(compilerTagNames, layoutTagNames.toArray(new String[layoutTagNames.size()]));
        }
    }

    String titleEntry = null;

    if (ArrayUtil.isNotEmpty(compilerTagNames)) {
        String[] newAssetTagNames = ArrayUtil.append(allAssetTagNames, compilerTagNames);

        allAssetTagNames = ArrayUtil.distinct(newAssetTagNames, new StringComparator());

        long[] allAssetTagIds = AssetTagLocalServiceUtil.getTagIds(siteGroupIds, allAssetTagNames);

        assetEntryQuery.setAllTagIds(allAssetTagIds);

        titleEntry = compilerTagNames[compilerTagNames.length - 1];
    }

    String portletTitle = portletDisplay.getTitle();

    portletTitle = AssetUtil.substituteTagPropertyVariables(scopeGroupId, titleEntry, portletTitle);

    renderResponse.setTitle(portletTitle);
}
else {
    allAssetTagNames = ArrayUtil.distinct(allAssetTagNames, new StringComparator());
}

for (String curAssetTagName : allAssetTagNames) {
    try {
        AssetTag assetTag = AssetTagLocalServiceUtil.getTag(scopeGroupId, curAssetTagName);

        AssetTagProperty journalTemplateIdProperty = AssetTagPropertyLocalServiceUtil.getTagProperty(assetTag.getTagId(), "journal-template-id");

        String journalTemplateId = journalTemplateIdProperty.getValue();

        request.setAttribute(WebKeys.JOURNAL_TEMPLATE_ID, journalTemplateId);

        break;
    }
    catch (NoSuchTagException nste) {
    }
    catch (NoSuchTagPropertyException nstpe) {
    }
}

if (enableTagBasedNavigation && selectionStyle.equals("manual") && ((assetEntryQuery.getAllCategoryIds().length > 0) || (assetEntryQuery.getAllTagIds().length > 0))) {
    selectionStyle = "dynamic";
}

Group scopeGroup = themeDisplay.getScopeGroup();

boolean hasAddPortletURLs = false;
%>

<c:if test="<%= showAddContentButton && (scopeGroup != null) && (!scopeGroup.hasStagingGroup() || scopeGroup.isStagingGroup()) && !portletName.equals(PortletKeys.HIGHEST_RATED_ASSETS) && !portletName.equals(PortletKeys.MOST_VIEWED_ASSETS) && !portletName.equals(PortletKeys.RELATED_ASSETS) %>">

    <%
    for (long groupId : groupIds) {
        addPortletURLs = AssetUtil.getAddPortletURLs(liferayPortletRequest, liferayPortletResponse, groupId, classNameIds, classTypeIds, allAssetCategoryIds, allAssetTagNames, null);

        if ((addPortletURLs != null) && !addPortletURLs.isEmpty()) {
            hasAddPortletURLs = true;
        }
    %>

        <div class="lfr-meta-actions add-asset-selector">
            <%@ include file="/html/portlet/asset_publisher/add_asset.jspf" %>
        </div>

    <%
    }
    %>

</c:if>

<div class="subscribe-action">
    <c:if test="<%= !portletName.equals(PortletKeys.HIGHEST_RATED_ASSETS) && !portletName.equals(PortletKeys.MOST_VIEWED_ASSETS) && !portletName.equals(PortletKeys.RECENT_CONTENT) && !portletName.equals(PortletKeys.RELATED_ASSETS) && PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.SUBSCRIBE) && AssetPublisherUtil.getEmailAssetEntryAddedEnabled(portletPreferences) %>">
        <c:choose>
            <c:when test="<%= AssetPublisherUtil.isSubscribed(themeDisplay.getCompanyId(), user.getUserId(), themeDisplay.getPlid(), portletDisplay.getId()) %>">
                <portlet:actionURL var="unsubscribeURL">
                    <portlet:param name="struts_action" value="/asset_publisher/edit_subscription" />
                    <portlet:param name="<%= Constants.CMD %>" value="<%= Constants.UNSUBSCRIBE %>" />
                    <portlet:param name="redirect" value="<%= currentURL %>" />
                </portlet:actionURL>

                <liferay-ui:icon
                    image="unsubscribe"
                    label="<%= true %>"
                    url="<%= unsubscribeURL %>"
                />
            </c:when>
            <c:otherwise>
                <portlet:actionURL var="subscribeURL">
                    <portlet:param name="struts_action" value="/asset_publisher/edit_subscription" />
                    <portlet:param name="<%= Constants.CMD %>" value="<%= Constants.SUBSCRIBE %>" />
                    <portlet:param name="redirect" value="<%= currentURL %>" />
                </portlet:actionURL>

                <liferay-ui:icon
                    image="subscribe"
                    label="<%= true %>"
                    url="<%= subscribeURL %>"
                />
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="<%= enableRSS %>">
        <liferay-portlet:resourceURL varImpl="rssURL">
            <portlet:param name="struts_action" value="/asset_publisher/rss" />
        </liferay-portlet:resourceURL>

        <liferay-ui:rss resourceURL="<%= rssURL %>" />
    </c:if>
</div>

<%
PortletURL portletURL = renderResponse.createRenderURL();

SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, pageDelta, portletURL, null, null);

if (!paginationType.equals("none")) {
    searchContainer.setDelta(pageDelta);
    searchContainer.setDeltaConfigurable(false);
}
%>

<c:if test="<%= showMetadataDescriptions %>">
    <liferay-ui:categorization-filter
        assetType="content"
        portletURL="<%= portletURL %>"
    />
</c:if>

<%
long portletDisplayDDMTemplateId = PortletDisplayTemplateUtil.getPortletDisplayTemplateDDMTemplateId(displayStyleGroupId, displayStyle);

Map<String, Object> contextObjects = new HashMap<String, Object>();

contextObjects.put(PortletDisplayTemplateConstants.ASSET_PUBLISHER_HELPER, AssetPublisherHelperUtil.getAssetPublisherHelper());
%>

<c:choose>
    <c:when test='<%= selectionStyle.equals("dynamic") %>'>
        <%@ include file="/html/portlet/asset_publisher/view_dynamic_list.jspf" %>
    </c:when>
    <c:when test='<%= selectionStyle.equals("manual") %>'>
        <%@ include file="/html/portlet/asset_publisher/view_manual.jspf" %>
    </c:when>
</c:choose>

<c:if test='<%= !paginationType.equals("none") && (searchContainer.getTotal() > searchContainer.getResults().size()) %>'>
    <c:choose>
        <c:when test='<%= paginationType.equals("more") && (searchContainer.getTotal() > searchContainer.getResults().size()) %>'>
                      
            <%
                boolean includeScript = ParamUtil.getBoolean(request, "includeScript", true); 
            %>
            <div class="more-assets"></div>
            
            <c:if test="<%= searchContainer.getTotal() == 0 %>">
                <div class="no-assets">no assets</div>
            </c:if>
                                                            
            <div class="loading-bar"></div> 
            
            <script>
                <portlet:namespace />cur = <%= searchContainer.getCur() %>;
                <portlet:namespace />delta = <%= searchContainer.getDelta() %>;
                <portlet:namespace />total = <%= searchContainer.getTotal() %>;            
            </script>
                    
            <c:if test="<%= includeScript %>">
                
                <script src='/html/portlet/asset_publisher/js/main.js' type='text/javascript'></script>          
                
                <aui:script use="aui-base,aui-io-request-deprecated,aui-parse-content,liferay-so-scroll">
                
                    var assetPublisher = A.one('#p_p_id<portlet:namespace />');
                    var body = A.getBody();
                    var footer = A.one('#footer');   
                                    
                    var loadingBar = assetPublisher.one('.loading-bar');
                    var moreAssets = assetPublisher.one('.more-assets');
                
                    moreAssets.plug(A.Plugin.ParseContent);
                
                    var win = A.getWin();
                                        
                    win.plug(
                        
                        Liferay.SO.Scroll,
                        {
                            edgeProximity: footer.height() 
                        }
                    ); 
                
                    var loading = false;
                    
                    function loadNewContent(page) {
                    
                        loadingBar.addClass('loading-animation');
                
                        loading = true;
                                                        
                        setTimeout(
                            function() {
                            
                                <portlet:renderURL var="viewAssetsURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
                                    <portlet:param name="mvcPath" value="/html/portlet/asset_publisher/view.jsp" />
                                    <portlet:param name="includeScript" value="false"/>
                                </portlet:renderURL>
                
                                var uri = '<%= viewAssetsURL %>';
                
                                uri = Liferay.Util.addParams('<portlet:namespace />cur=' + page, uri) || uri;
                                            
                                A.io.request(
                                    uri,
                                    {
                                        after: {
                                            success: function(event, id, obj) {
                                            
                                                var responseData = this.get('responseData');
                
                                                moreAssets.append(responseData);
                
                                                loadingBar.removeClass('loading-animation');
                
                                                loading = false;
                                                                                                                                            
                                                if (!assetPublisher.one('.no-assets')) {
                                                    if (body.height() < win.height()) {
                                                        loadNewContent();
                                                    }
                                                }
                                                
                                                
                                                // initialize .shariff elements
                                                if (typeof jQuery != 'undefined') {  
                                                    $('.shariff').each(function() {
                                                        if (!this.hasOwnProperty('shariff')) {
                                                            this.shariff = new Shariff(this);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                );
                            },
                            1000
                        );                   
                    }
                  
                moreAssets.delegate(
                    'click',
                    function(event) {
                    
                        var moreButton = moreAssets.one('.btn-more');
                        var moreButtonWrapper = moreAssets.one('.btn-more-wrapper');
            
                        moreButton.remove(true);
                        moreButtonWrapper.remove(true);
            
                        loadNewContent();
                    },
                    '.btn-more'
                )
                
    
                win.scroll.on(
                    'bottom-edge',
                    function(event) {
                                                        
                        if (assetPublisher.one('.no-assets')) {
                            loading = true;
                        }
            
                        if (!loading) {
                                                
                            if (<portlet:namespace />total / <portlet:namespace />delta >=  <portlet:namespace />cur + 1) {
                                loading = true;                            
                                loadNewContent(<portlet:namespace />cur + 1);    
                            }               
                        }
                    }
                );
                                                 
                </aui:script>  
            </c:if>          
            
        </c:when>
        <c:otherwise>
            <liferay-ui:search-paginator searchContainer="<%= searchContainer %>" type="<%= paginationType %>" />
        </c:otherwise>    
    </c:choose>
</c:if>

<%-- 
<c:if test='<%= !paginationType.equals("none") && (searchContainer.getTotal() > searchContainer.getResults().size()) %>'>
    <liferay-ui:search-paginator searchContainer="<%= searchContainer %>" type="<%= paginationType %>" />
</c:if>
--%>
<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.asset_publisher.view_jsp");
%>