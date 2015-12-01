<#--
    projects.ftl: Display a list of projects with the flexslider widget.
    
    Created:    2015-12-01 14:52 by Christian Berndt
    Modified:   2015-12-01 14:52 by Christian Berndt
    Version:    1.0.0
    
    Please note: the flexsliders are configured in js/main.js.
-->

<#assign journalArticleService = serviceLocator.findService("com.liferay.portlet.journal.service.JournalArticleLocalService") />

<div class="projects flexslider slider">
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

<div class="container">
    <div class="projects flexslider carousel">
        <#if entries?has_content>
            <ul class="slides">
            <#list entries as curEntry>
                <#assign article = journalArticleService.getLatestArticle(curEntry.getClassPK()) />
                <#assign docXml = saxReaderUtil.read(curEntry.getAssetRenderer().getArticle().getContent()) />
                <#assign client = docXml.valueOf("//dynamic-element[@name='client']/dynamic-content/text()") />
                <#assign backgroundColor = "#fcfcfc" />
                <#assign fontColor = "#333" />
                <#assign clientBackgroundColor = docXml.valueOf("//dynamic-element[@name='clientBackgroundColor']/dynamic-content/text()") />
                <#if clientBackgroundColor?has_content>
                    <#assign backgroundColor= clientBackgroundColor />
                </#if>               
                <#assign clientFontColor = docXml.valueOf("//dynamic-element[@name='clientFontColor']/dynamic-content/text()") />
                <#if clientFontColor?has_content>
                    <#assign fontColor = clientFontColor />
                </#if>               
                <li style="background: ${backgroundColor}; color: ${fontColor}">
                    <div>${client}</div>
                </li>
            </#list>
            </ul>
        </#if>
    </div>
</div>