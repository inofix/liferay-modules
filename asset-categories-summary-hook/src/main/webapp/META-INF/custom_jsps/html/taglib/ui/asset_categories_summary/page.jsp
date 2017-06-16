<%--
    page.jsp: An improved page.jsp for Liferay's asset-categories-summary-tag.
    We should propose this improvement to Liferay proper, since it does not hurt
    and makes css customization more easy.
            
    Created:    2015-09-14 13:55 by Christian Berndt
    Modified:   2017-06-18 10:46 by Christian Berndt
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

<%@ include file="/html/taglib/init.jsp" %>

<%-- Import classes required by customization --%>

<%@page import="com.liferay.portlet.asset.service.AssetCategoryPropertyServiceUtil"%>
<%@page import="com.liferay.portlet.asset.model.AssetCategoryProperty"%>

<%
String className = (String)request.getAttribute("liferay-ui:asset-categories-summary:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui:asset-categories-summary:classPK"));
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui:asset-categories-summary:portletURL");

AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(className, classPK);

List<AssetVocabulary> vocabularies = AssetVocabularyServiceUtil.getGroupsVocabularies(new long[] {((assetEntry != null) ? assetEntry.getGroupId() : themeDisplay.getSiteGroupId()), themeDisplay.getCompanyGroupId()});
List<AssetCategory> categories = AssetCategoryServiceUtil.getCategories(className, classPK);

for (AssetVocabulary vocabulary : vocabularies) {
    vocabulary = vocabulary.toEscapedModel();

    String vocabularyTitle = vocabulary.getTitle(themeDisplay.getLocale());

    List<AssetCategory> curCategories = _filterCategories(categories, vocabulary);
%>

    <c:if test="<%= !curCategories.isEmpty() %>">
        <span class="taglib-asset-categories-summary">
        
<%-- Customization: wrap the vocabulary-title into a span. --%>

            <span class="vocabulary-title"><%= vocabularyTitle %>:</span>

            <c:choose>
                <c:when test="<%= portletURL != null %>">

                    <%
                    for (AssetCategory category : curCategories) {
                        category = category.toEscapedModel();
                        
// Customization: check whether the category has the show-in-logbook property

					    String cssClass = StringPool.BLANK; 
					    
					    if (category != null) {
					        
					        List<AssetCategoryProperty> categoryProperties = AssetCategoryPropertyServiceUtil.getCategoryProperties(category.getCategoryId());
					        
					        for (AssetCategoryProperty categoryProperty : categoryProperties) {
					            
					            if ("show-in-logbook".equals(categoryProperty.getKey())) {
					                
					                cssClass="show-in-logbook"; 
					            }
					        }
					    }

                        portletURL.setParameter("categoryId", String.valueOf(category.getCategoryId()));
                        
                        // required for ajaxed asset-publisher
                        portletURL.setWindowState(WindowState.NORMAL);                       

                    %>

                        <a class="asset-category <%= cssClass %>" href="<%= HtmlUtil.escape(portletURL.toString()) %>"><%= _buildCategoryPath(category, themeDisplay) %></a>

                    <%
                    }
                    %>

                </c:when>
                <c:otherwise>

                    <%
                    for (AssetCategory category : curCategories) {
                        category = category.toEscapedModel();
                    %>

                        <span class="asset-category">
                            <%= _buildCategoryPath(category, themeDisplay) %>
                        </span>

                    <%
                    }
                    %>

                </c:otherwise>
            </c:choose>
        </span>
    </c:if>

<%
}
%>

<%!
private String _buildCategoryPath(AssetCategory category, ThemeDisplay themeDisplay) throws PortalException, SystemException {
    List<AssetCategory> ancestorCategories = category.getAncestors();

    if (ancestorCategories.isEmpty()) {
        return category.getTitle(themeDisplay.getLocale());
    }

    Collections.reverse(ancestorCategories);

    StringBundler sb = new StringBundler(ancestorCategories.size() * 2 + 1);

    for (AssetCategory ancestorCategory : ancestorCategories) {
        ancestorCategory = ancestorCategory.toEscapedModel();

        sb.append(ancestorCategory.getTitle(themeDisplay.getLocale()));
        sb.append(" &raquo; ");
    }

    sb.append(category.getTitle(themeDisplay.getLocale()));

    return sb.toString();
}

private List<AssetCategory> _filterCategories(List<AssetCategory> categories, AssetVocabulary vocabulary) {
    List<AssetCategory> filteredCategories = new ArrayList<AssetCategory>();

    for (AssetCategory category : categories) {
        if (category.getVocabularyId() == vocabulary.getVocabularyId()) {
            filteredCategories.add(category);
        }
    }

    return filteredCategories;
}
%>