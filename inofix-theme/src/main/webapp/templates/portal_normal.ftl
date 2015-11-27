<#--                                                        -->
<#-- portal_normal.ftl: Main template of the inofix theme   -->
<#--                                                        -->
<#-- Created:     2015-11-26 22:31 by Christian Berndt      -->
<#-- Modified:    2015-11-26 22:31 by Christian Berndt      -->
<#-- Version:     1.0.0                                     -->
<#--                                                        -->

<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
    <title>${the_title} - ${company_name}</title>

    <meta content="initial-scale=1.0, width=device-width" name="viewport" />

    ${theme.include(top_head_include)}
</head>

<body class="${css_class}">

<a href="#main-content" id="skip-to-content"><@liferay.language key="skip-to-content" /></a>

${theme.include(body_top_include)}

<#if is_signed_in>
    <@liferay.dockbar />
</#if>

<div class="container-fluid" id="wrapper">
    <header id="banner" role="banner">
        <#if !is_signed_in>
            <a href="${sign_in_url}" data-redirect="${is_login_redirect_required?string}" id="sign-in" rel="nofollow">${sign_in_text}</a>
        </#if>

        <#if has_navigation || is_signed_in>
           <#include "${full_templates_path}/navigation.ftl" />
        </#if>
    </header>

    <div id="content">
    
        <#-- <nav id="breadcrumbs"><@liferay.breadcrumbs /></nav> -->

        <#if selectable>
            ${theme.include(content_include)}
        <#else>
            ${portletDisplay.recycle()}

            ${portletDisplay.setTitle(the_title)}

            ${theme.wrapPortlet("portlet.ftl", content_include)}
        </#if>
    </div>

    <footer id="footer" role="contentinfo">
        <div class="container">
            <div class="copyright pull-left">
                &copy; 2015&dash;2016 Inofix GmbH, Luzern <a href="http://www.inofix.ch" rel="external" target="_blank">www.inofix.ch</a>
            </div>
            <div class="pull-right">
                <#if !is_signed_in>
                    <a href="${sign_in_url}" data-redirect="${is_login_redirect_required?string}" id="sign-in" rel="nofollow">${sign_in_text}</a>
                <#else>
                    <a href="${sign_out_url}" id="sign-out" rel="nofollow">${sign_out_text}</a>
                </#if>
            </div>
        </div>
    </footer>
</div>

${theme.include(body_bottom_include)}

${theme.include(bottom_include)}

</body>

</html>