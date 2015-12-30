<#--
    article.ftl: Format the article structure

    Created:    2015-12-29 17:36 by Christian Berndt
    Modified:   2015-12-30 20:27 by Christian Berndt
    Version:    0.9.2

    Please note: Although this template is stored in the
    site's context it's source is managed via git. Whenever you
    change the template online make sure to commit your
    changes to the liferay-modules repo, too.
-->

<#assign displayToc = false />

<#if showToc?? >
    <#if showToc.getData()?has_content>
        <#if getterUtil.getBoolean(showToc.getData())>
            <#assign displayToc = getterUtil.getBoolean(showToc.getData()) />
        </#if>
    </#if>
</#if>

<div class="article wc-template">
    <div class="container">
    
        <#assign cssClass = "content span8 offset2" />
        
        <#if displayToc >
            <#assign cssClass = "content span8" />       
        </#if>    

        <div class="${cssClass}">
            
            <#if headline??>
                <#if headline.getData()?has_content>
                    <h1 id="section-0">${headline.getData()}</h1>
                </#if>
            </#if>
            
            <#if summary.getData()??>
                <#if summary.getData()?has_content>
                    <p class="lead">${summary.getData()}</p>
                </#if>
            </#if>
            
            <#if section?? >
                <#if section.getSiblings()?has_content>
                    <#assign i = 1 />
                    <#list section.getSiblings() as cur_section>
                        <div class="section" id="section-${i}">                       
                            <#if cur_section.getData()?has_content>
                                <h2>${cur_section.getData()}</h2>
                            </#if>
                            <#if cur_section.content.getData()?has_content>
                                <div class="section-content">${cur_section.content.getData()}</div>
                            </#if>
                        </div>
                        <#assign i = i+1 />
                    </#list>
                </#if>
            </#if>             
        </div> <#-- / .span8 -->
        
        <#if displayToc>        
            <div class="span4">
                <div class="toc">
                    <ul class="nav nav-list">                        
                        <#if section.getSiblings()?has_content>
                            <#assign i = 1 />
                            <#list section.getSiblings() as cur_section >
                                <#if cur_section.getData()?has_content >
                                    <li class="">
                                        <a href="#section-${i}">${cur_section.getData()}</a>
                                    </li>
                                </#if>
                                <#assign i = i+1 />
                            </#list>
                        </#if>
                    </ul>
                </div> <#-- / .toc -->
            </div> <#-- / .span4 -->
        </#if>
    </div> <#-- / .container -->
</div> <#-- / .article -->
