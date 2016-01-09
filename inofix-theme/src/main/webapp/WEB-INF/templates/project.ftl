<#--
    project.ftl: format the project-structure. 
    
    Created:    2015-12-01 14:47 by Christian Berndt
    Modified:   2016-01-09 14:37 by Christian Berndt
    Version:    1.0.4
    
    Please note: Although this template is stored in the
    site's context it's source is managed via git. Whenever you
    change the template online make sure that you commit your
    changes to the liferay-modules repo, too.
-->

<div class="project template">
    <div class="container">
        <div class="template-body">
            <#if keyvisual??>
                <#if keyvisual.getData()?has_content>
                    <img src="${keyvisual.getData()}">
                </#if>
            </#if>
        </div>
        <#if href??>
            <#if href.getData()?has_content>
            	<#assign label = "read-more"/>
            	<#if href.hrefLabel??>
            		<#if href.hrefLabel?has_content>
            			<#assign label = href.hrefLabel.getData() />
            		</#if>
            	</#if>
                <a href="${href.getData()}" class="client-link muted" rel="external">${label}</a>                
            </#if>
        </#if>        
        <#if headline??>
            <#if headline.getData()?has_content>
                <h3>${client.getData()}:
                    <span class="muted">${headline.getData()}</span>
                </h3>
            </#if>
        </#if>
    </div>
</div>
