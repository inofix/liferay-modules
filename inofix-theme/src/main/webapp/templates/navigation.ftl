<#--                                                        -->
<#-- navigation.vm: Navigation template of the inofix theme -->
<#--                                                        -->
<#-- Created:     2015-11-26 22:31 by Christian Berndt      -->
<#-- Modified:    2015-11-26 22:31 by Christian Berndt      -->
<#-- Version:     1.0.0                                     -->
<#--                                                        -->

<#assign brand = theme_display.getThemeSetting('brand') />

<nav class="${nav_css_class} navbar navbar-fixed-top" id="navigation" role="navigation">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-reorder"></span>
            </button>
            <a class="brand" href="${site_default_url}">${brand}</a>
            <div class="nav-collapse collapse pull-right">
                <ul aria-label="<@liferay.language key="site-pages" />" class="nav" role="menubar">
                    <#list nav_items as nav_item>
                        <#assign nav_item_attr_has_popup = "" />
                        <#assign nav_item_attr_selected = "" />
                        <#assign nav_item_css_class = "" />
            
                        <#if nav_item.isSelected()>
                            <#assign nav_item_attr_has_popup = "aria-haspopup='true'" />
                            <#assign nav_item_attr_selected = "aria-selected='true'" />
                            <#assign nav_item_css_class = "selected" />
                        </#if>
            
                        <li ${nav_item_attr_selected} class="${nav_item_css_class}" id="layout_${nav_item.getLayoutId()}" role="presentation">
                            <a aria-labelledby="layout_${nav_item.getLayoutId()}" ${nav_item_attr_has_popup} href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span>${nav_item.icon()} ${nav_item.getName()}</span></a>
                        </li>
                        
                    </#list>
                </ul>
            </div>
        </div>
    </div>
</nav>