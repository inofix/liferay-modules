<#--                                                        -->
<#-- portal_normal.ftl: Main template of the inofix theme   -->
<#--                                                        -->
<#-- Created:     2015-11-26 22:31 by Christian Berndt      -->
<#-- Modified:    2016-04-27 12:28 by Christian Berndt      -->
<#-- Version:     1.0.8                                     -->
<#--                                                        -->

<!DOCTYPE html>

<#include init />

<#assign piwik_opt_out_url = theme_display.getThemeSetting('piwik-opt-out-url') />
<#assign services_layout_uuid = theme_display.getThemeSetting('services-layout-uuid') />
<#assign virtualhost = layout.getLayoutSet().getVirtualHostname() />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
    <#if virtualhost?has_content >
        <title>${the_title} - ${virtualhost}</title>
    <#else>
        <title>${the_title} - ${company_name}</title>
    </#if>
    
    <meta content="initial-scale=1.0, width=device-width" name="viewport" />

    ${theme.include(top_head_include)}
    
    <link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,200,200italic,300,300italic,400italic,600,600italic,700,700italic&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    
    <script src='${javascript_folder}/jquery-1.11.3.min.js' type='text/javascript'></script>
    
    <#-- bootstrap -->
    <script src='${javascript_folder}/affix.js' type='text/javascript'></script>
    <script src='${javascript_folder}/collapse.js' type='text/javascript'></script>
    <script src='${javascript_folder}/scrollspy.js' type='text/javascript'></script>
    
    <#-- flexslider -->
    <script src='${javascript_folder}/jquery.flexslider-min.js' type='text/javascript'></script>
    <link href='${css_folder}/flexslider.css' rel='stylesheet' type='text/css' media='screen' />
       
</head>

<body class="${css_class}">

<a href="#main-content" id="skip-to-content"><@liferay.language key="skip-to-content" /></a>

${theme.include(body_top_include)}

<#if is_signed_in>
    <@liferay.dockbar />
</#if>

<div class="container-fluid" id="wrapper">
    <header id="banner" role="banner">
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
            <div class="footer-content">
                <div class="copyright pull-left">
                    &copy; 2015&ndash;2016 <a href="http://www.inofix.ch" rel="external" target="_blank">www.inofix.ch</a>
                </div>

                <div class="pull-right">
                
                    <#if (validator.isNotNull(services_layout_uuid))>  
                    
                        <#--                                        -->
                        <#-- Embed a configurable services-site-map -->
                        <#--                                        -->       
            
                        <#-- Hide borders  -->
                        ${freeMarkerPortletPreferences.setValue("portletSetupShowBorders", "false")}
                        
                        <#-- Set the displayDepth -->
                        ${freeMarkerPortletPreferences.setValue("displayDepth", "1")}
                                    
                        <#-- Set the root-layout-uuid  -->
                        ${freeMarkerPortletPreferences.setValue("rootLayoutUuid", services_layout_uuid)}                     
                                  
                        <#-- Include a site-map-portlet instance  -->  
                        <#-- ${theme.runtime("145")} -->       
                        ${theme.runtime("85_INSTANCE_N6bz", "", freeMarkerPortletPreferences.toString())}         
                        
                        <#-- Reset the preferences settings -->
                        ${freeMarkerPortletPreferences.reset()}                     
                                             
                    </#if>
                
                    <#if !is_signed_in>
                        <a href="${sign_in_url}" data-redirect="${is_login_redirect_required?string}" id="sign-in" rel="nofollow">${sign_in_text}</a>
                    <#else>
                        <a href="${sign_out_url}" id="sign-out" rel="nofollow">${sign_out_text}</a>
                    </#if>
                    <a href="https://github.com/inofix" target="_blank" title="<@liferay.language key="meet-us-on-github" />"><span class="icon icon-github"></span></a>
                    <a href="mailto:contact@inofix.ch?subject=<@liferay.language key="contact-message" />" title="<@liferay.language key="send-us-a-message" />"><span class="icon icon-envelope-alt"></span></a>
               </div>
           </div>
        </div>
    </footer>
</div>

${theme.include(body_bottom_include)}

${theme.include(bottom_include)}

<#if piwik_opt_out_url?has_content>
    <iframe style="border: 0; width: 100%;"  src="${piwik_opt_out_url}"></iframe>
</#if>

</body>

</html>