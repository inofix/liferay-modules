<%--
    edit_mailing.jsp: edit the mailing settings. 
    
    Created:    2016-10-10 18:34 by Christian Berndt
    Modified:   2015-10-10 18:34 by Christian Berndt
    Version:    1.0.0
--%>

<%@include file="/html/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);
    
    Mailing mailing = (Mailing)request.getAttribute("MAILING"); 
    
    String windowId = ""; 
    windowId = ParamUtil.getString(request, "windowId"); 
    
    // Close the popup, if we are in popup mode, a redirect was provided
    // and the windowId is "editMailing" (which means, viewByDefault 
    // is false.
    
    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp()
            && "editMailing".equals(windowId)) {
        
        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        portletURL.setParameter("redirect", redirect);
        portletURL.setParameter("windowId", windowId);
        backURL = portletURL.toString();
    }

    String mvcPath = ParamUtil.getString(request, "mvcPath");
%>


<liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

<portlet:actionURL var="saveMailingURL" name="saveMailing" />

<aui:form action="<%=saveMailingURL%>" method="post" name="fm">

    <aui:input name="backURL" type="hidden" value="<%=backURL%>" />
    <aui:input name="mailingId" type="hidden"
        value="<%=String.valueOf(mailing.getMailingId())%>" />
    <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
    <aui:input name="windowId" type="hidden" value="<%=windowId%>" />
    
    <aui:input name="title"
        value="<%= mailing.getTitle() %>"/>
    
    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
                
</aui:form>

<hr>

<ifx-util:build-info/>
