<#--
    teaser.ftl: format the teaser-structure. 
    
    Created:    2015-11-30 18:17 by Christian Berndt
    Modified:   2016-01-08 22:04 by Christian Berndt
    Version:    1.0.2
-->

<div class="teaser template">
    <div class="container">
        <div class="template-body">
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
	        <#if assertion??>
	            <#if assertion.getSiblings()?has_content>
	                <div class="row">
	                    <#list assertion.getSiblings() as cur_assertion>
	                        <div class="span4">
	                            <h2>${cur_assertion.getData()}</h2>
	                            <p class="lead">${cur_assertion.explanation.getData()}</p>
	                        </div>
	                    </#list>
	                </div>
	            </#if>
	        </#if>
	    </div> <#-- / .template-body -->
    </div> <#-- / .container -->
</div> <#-- / .article -->