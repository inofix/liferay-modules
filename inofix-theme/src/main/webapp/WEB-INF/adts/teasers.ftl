<#--
    teasers.ftl: Display a list of teasers with the flexslider widget.
    
    Created:    2015-12-01 11:58 by Christian Berndt
    Modified:   2015-12-01 11:58 by Christian Berndt
    Version:    1.0.0
-->

<#assign journalArticleService = serviceLocator.findService("com.liferay.portlet.journal.service.JournalArticleLocalService") />

<div class="teasers flexslider">
    <#if entries?has_content>
        <ul class="slides">
        <#list entries as curEntry>
            <li class="item">

                <#assign article = journalArticleService.getLatestArticle(curEntry.getClassPK()) />
                <#assign content = journalContentUtil.getContent(themeDisplay.getScopeGroupId(), article.getArticleId(), article.getTemplateId(), themeDisplay.getLanguageId(), themeDisplay) />
                ${content}

            </li>
        </#list>
        </ul>
    </#if>
</div>

<script>
    $( document ).ready(function() {
      $('.teasers.flexslider').flexslider({
        pauseOnHover: true,
        prevText:"",      
        nextText:""      
      });
    });
</script>
