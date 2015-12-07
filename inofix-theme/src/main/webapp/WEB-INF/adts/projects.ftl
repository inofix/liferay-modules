<#--
    projects.ftl: Display a list of projects with the flexslider widget.
    
    Created:    2015-12-01 14:52 by Christian Berndt
    Modified:   2015-12-06 23:48 by Christian Berndt
    Version:    1.0.2
    
    Please note: the flexsliders are configured in js/main.js.
-->

<#assign journalArticleService = serviceLocator.findService("com.liferay.portlet.journal.service.JournalArticleLocalService") />

<div class="container">
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
</div>

<div class="container">
    <div class="projects flexslider carousel">
        <#if entries?has_content>
            <ul class="slides">
            <#list entries as curEntry>
                <#assign article = journalArticleService.getLatestArticle(curEntry.getClassPK()) />
                <#assign docXml = saxReaderUtil.read(curEntry.getAssetRenderer().getArticle().getContent()) />
                <#assign client = docXml.valueOf("//dynamic-element[@name='client']/dynamic-content/text()") />
                <#assign headline = docXml.valueOf("//dynamic-element[@name='headline']/dynamic-content/text()") />
                <#assign description = docXml.valueOf("//dynamic-element[@name='description']/dynamic-content/text()") />
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
                <li>
                    <div class="client-name" style="background: ${backgroundColor}; color: ${fontColor}">
                        <div>${client}</div>
                    </div>
                    <div class="client-body">
                        <#if headline?has_content>
                            <h3>${headline}</h3>
                        </#if>
                        <#if description?has_content >
                            <p class="lead">${description}</p>
                        </#if>
                    </div>
                </li>
            </#list>
            </ul>
        </#if>
    </div>
</div>