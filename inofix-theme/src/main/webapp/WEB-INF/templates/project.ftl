<#--
    project.ftl: format the project-structure. 
    
    Created:    2015-12-01 14:47 by Christian Berndt
    Modified:   2015-12-01 14:47 by Christian Berndt
    Version:    1.0.0
    
    Please note: Although this template is stored in the
    site's context it's source is managed via git. Whenever you
    change the template online make sure that you commit your
    changes to the liferay-modules repo, too.
-->

<div class="project wc-template">
    <div class="container">
        <div class="project-body">
            <#if keyvisual??>
                <#if keyvisual.getData()?has_content>
                    <img src="${keyvisual.getData()}">
                </#if>
            </#if>
            <#if target??>
                <#if target.getFriendlyUrl()?has_content>
                    <a href="${target.getFriendlyUrl()}" class="btn">
                        <#if label??>
                            <#if label.getData()?has_content>
                                ${label.getData()}
                            <#else>
                                <@liferay.language key="read-more" />
                            </#if>
                        </#if>
                    </a>
                </#if>
            </#if>
        </div>
    </div>
</div>