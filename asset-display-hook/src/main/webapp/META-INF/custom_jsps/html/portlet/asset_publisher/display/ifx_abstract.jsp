<%--
    ifx_abstract.jsp: A custom asset-publisher style where the asset's
    metadata is displayed BEFORE the title and summary. 
    
    Created:    2015-07-28 11:53 by Christian Berndt
    Modified:   2015-09-21 21:47 by Christian Berndt
    Version:    1.0.3
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

<%-- Import classes required by customization --%>
<%@page import="com.liferay.portal.kernel.xml.Document"%>
<%@page import="com.liferay.portal.kernel.xml.Node"%>
<%@page import="com.liferay.portal.kernel.xml.SAXReaderUtil"%>

<%@page import="com.liferay.portlet.asset.service.AssetCategoryPropertyServiceUtil"%>
<%@page import="com.liferay.portlet.asset.model.AssetCategoryProperty"%>
<%@page import="com.liferay.portlet.journal.asset.JournalArticleAssetRenderer"%>

<%
List results = (List)request.getAttribute("view.jsp-results");

int assetEntryIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRendererFactory assetRendererFactory = (AssetRendererFactory)request.getAttribute("view.jsp-assetRendererFactory");
AssetRenderer assetRenderer = (AssetRenderer)request.getAttribute("view.jsp-assetRenderer");

boolean show = ((Boolean)request.getAttribute("view.jsp-show")).booleanValue();

request.setAttribute("view.jsp-showIconLabel", true);

String title = (String)request.getAttribute("view.jsp-title");

if (Validator.isNull(title)) {
    title = assetRenderer.getTitle(locale);
}

String viewURL = AssetPublisherHelperImpl.getAssetViewURL(liferayPortletRequest, liferayPortletResponse, assetEntry, viewInContext);

String viewURLMessage = viewInContext ? assetRenderer.getViewInContextMessage() : "read-more-x-about-x";

String summary = StringUtil.shorten(assetRenderer.getSummary(locale), abstractLength);

List<AssetCategory> assetCategories = AssetCategoryServiceUtil.getCategories(assetEntry.getClassName(), assetEntry.getClassPK());

AssetCategory assetCategory = null; 

if (assetCategories.size() > 0) {
	assetCategory = assetCategories.get(0); 
}

boolean invert = false;
String backgroundColor = "white"; 

if (assetCategory != null) {
    
    List<AssetCategoryProperty> categoryProperties = AssetCategoryPropertyServiceUtil.getCategoryProperties(assetCategory.getCategoryId());
    
    for (AssetCategoryProperty categoryProperty : categoryProperties) {
        
        if ("background-color".equals(categoryProperty.getKey())) {
            
            String value = categoryProperty.getValue();
            
            if (Validator.isNotNull(value)) {
                backgroundColor = value; 
            }           
        }
        
        if ("invert".equals(categoryProperty.getKey())) {
            invert = GetterUtil.getBoolean(categoryProperty.getValue()); 
        }
    }
}
%>

<c:if test="<%= show %>">

<%-- 
    Customization: read the background-color and invert property from the current category 
--%> 
    <div class="asset-abstract <%= defaultAssetPublisher ? "default-asset-publisher" : StringPool.BLANK %> <%= invert ? "invert" : StringPool.BLANK %>"
         style="background-color: <%= backgroundColor %>">
         
        <liferay-util:include page="/html/portlet/asset_publisher/asset_actions.jsp" />
<%-- 
    Customization: wrap the asset's content sections. 
--%> 
     
    <div class="asset-wrapper">  
<%-- 
    Customization: display the asset's metadata as first element.
 --%>
        <div class="asset-metadata">

            <%
            request.setAttribute("asset_metadata.jspf-filterByMetadata", true);
            %>

            <%@ include file="/html/portlet/asset_publisher/asset_metadata.jspf" %>
        </div>

        <h3 class="asset-title">
<%-- 
    Customization: for journal-article assets try to use the journal-article's
    articleTitle, which can be retrieved from an article's structure.
 --%>
<%
    String articleTitle = null; 
    String languageId = LanguageUtil.getLanguageId(request);
%>
			<%-- custom test --%>        
			<c:if test="<%= JournalArticle.class.getName().equals(assetEntry.getClassName()) %>">
<%
				JournalArticleAssetRenderer journalRenderer = (JournalArticleAssetRenderer) assetRenderer; 
				JournalArticle article = journalRenderer.getArticle(); 
                    
                if (article != null) {
                    try {
                    
                        Document document = SAXReaderUtil.read(article
                                .getContentByLocale(languageId));
                                             
                        Node headline = document
                                .selectSingleNode("/root/dynamic-element[@name='headline']/dynamic-content");
                        
                        if (headline.getText().length() > 0) {
                            articleTitle = headline.getText();
                        }
                    
                    } catch (Exception ignore) {
                        articleTitle = null;
                    }
                }
                
                if (articleTitle != null) {
                	title = articleTitle; 
                }

%>
			</c:if>             
                 
		    <%-- default behaviour --%>
	        <c:choose>
	            <c:when test="<%= Validator.isNotNull(viewURL) %>">
	                <a href="<%= viewURL %>"><img alt="" src="<%= assetRenderer.getIconPath(renderRequest) %>" /> <%= HtmlUtil.escape(title) %></a>
	            </c:when>
	            <c:otherwise>
	                <img alt="" src="<%= assetRenderer.getIconPath(renderRequest) %>" /> <%= HtmlUtil.escape(title) %>
	            </c:otherwise>
	        </c:choose>

        </h3>

        <div class="asset-content">
            <div class="asset-summary">

                <%
					// Disabled, since we need the assetRenderer earlier
					String path = assetRenderer.render(renderRequest, renderResponse, AssetRenderer.TEMPLATE_ABSTRACT);
					
					request.setAttribute(WebKeys.ASSET_RENDERER, assetRenderer);
					request.setAttribute(WebKeys.ASSET_PUBLISHER_ABSTRACT_LENGTH, abstractLength);
					request.setAttribute(WebKeys.ASSET_PUBLISHER_VIEW_URL, viewURL);
                 %> 
                
                <c:choose>
                    <c:when test="<%= path == null %>">
                        <%= HtmlUtil.escape(summary) %>
                    </c:when>
                    <c:otherwise>
                        <liferay-util:include page="<%= path %>" portletId="<%= assetRendererFactory.getPortletId() %>" />
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="<%= Validator.isNotNull(viewURL) %>">
                <div class="asset-more">
                    <a href="<%= viewURL %>"><liferay-ui:message arguments='<%= new Object[] {"hide-accessible", HtmlUtil.escape(assetRenderer.getTitle(locale))} %>' key="<%= viewURLMessage %>" /> &raquo; </a>
                </div>
            </c:if>
        </div>
        
        <div class="asset-social-media">
			<liferay-ui:message key="tell-others"/>
            <ul>
                <li class="shariff-button facebook">
                    <a aria-label="TODO" role="button" title="TODO" rel="popup" href="javascript:;">
                        <span class="fa fa-facebook"></span>
                        <span class="share_text">Facebook</span>
                    </a>
                </li>
                <li class="shariff-button twitter">
                    <a aria-label="TODO" role="button" title="TODO" rel="popup" href="javascript:;">
                        <span class="fa fa-twitter"></span>
                        <span class="share_text">Twitter</span>
                    </a>
                </li>
                <li class="shariff-button mail">
                    <a aria-label="TODO" role="button" title="TODO" rel="popup" href="javascript:;">
                        <span class="fa fa-mail"></span>
                        <span class="share_text">Email</span>
                    </a>
                </li>
            </ul>
        </div>

        </div>
    </div>
</c:if>