<#--
    teaser.ftl: format the teaser-structure. 
    
    Created:    2015-11-30 18:17 by Christian Berndt
    Modified:   2015-11-30 18:17 by Christian Berndt
    Version:    1.0.0
    
    Please note: Although this template is stored in the
    site's context it's source is managed via git. Whenever you
    change the template online make sure that you commit your
    changes to the liferay-modules repo, too.
-->

<div class="teaser wc-template">
    <div class="container">
        <div class="teaser-body">
            <h1>${text.getData()}</h1>
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
