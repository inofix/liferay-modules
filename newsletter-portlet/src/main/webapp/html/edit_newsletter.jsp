<%--
    edit_newsletter.jsp: edit the newsletter settings.. 
    
    Created:    2016-10-08 15:49 by Christian Berndt
    Modified:   2015-10-08 15:49 by Christian Berndt
    Version:    1.0.0
--%>

<%@include file="/html/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);
    
    Newsletter newsletter = (Newsletter)request.getAttribute("NEWSLETTER"); 
    
    String windowId = ""; 
    windowId = ParamUtil.getString(request, "windowId"); 
    
    // Close the popup, if we are in popup mode, a redirect was provided
    // and the windowId is "editNewsletter" (which means, viewByDefault 
    // is false.
    
    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp()
            && "editNewsletter".equals(windowId)) {
        
        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        portletURL.setParameter("redirect", redirect);
        portletURL.setParameter("windowId", windowId);
        backURL = portletURL.toString();
    }

    String mvcPath = ParamUtil.getString(request, "mvcPath");
%>


<liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

<portlet:actionURL var="saveNewsletterURL" name="saveNewsletter" />

<aui:form action="<%=saveNewsletterURL%>" method="post" name="fm">

    <aui:model-context bean="<%=newsletter%>"
        model="<%=Newsletter.class%>" />

    <aui:input name="backURL" type="hidden" value="<%=backURL%>" />
    <aui:input name="newsletterId" type="hidden"
        value="<%=String.valueOf(newsletter.getNewsletterId())%>" />
    <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
    <aui:input name="windowId" type="hidden" value="<%=windowId%>" />
    
    <aui:input name="title"/>
    <aui:input name="template"/>
    
    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
                
</aui:form>

<hr>

<ifx-util:build-info/>

