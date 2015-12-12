<#--
    projects_small.ftl: Display a list of projects with the flexslider widget.
    
    Created:    2015-12-12 13:32 by Christian Berndt
    Modified:   2015-12-12 13:32 by Christian Berndt
    Version:    1.0.0
    
    Please note: the flexslider is configured in js/main.js.
-->

<#assign journalArticleService = serviceLocator.findService("com.liferay.portlet.journal.service.JournalArticleLocalService") />

<div class="container">
    <div class="projects-min flexslider slider">
        <#if entries?has_content>
            <ul class="slides">
            <#list entries as curEntry>
                <li>
                    <#assign article = journalArticleService.getLatestArticle(curEntry.getClassPK()) />
                    <#assign content = journalContentUtil.getContent(themeDisplay.getScopeGroupId(), article.getArticleId(), article.getTemplateId(), themeDisplay.getLanguageId(), themeDisplay) />
                    ${content}
                </li>
            </#list>
            </ul>
        </#if>
    </div>
</div>
