<%--
    edit_mailing.jsp: edit the mailing settings. 
    
    Created:    2016-10-10 18:34 by Christian Berndt
    Modified:   2015-10-11 16:30 by Christian Berndt
    Version:    1.0.3
--%>

<%@include file="/html/init.jsp"%>

<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@page import="com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    Mailing mailing = (Mailing) request.getAttribute("MAILING");

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
    
    String tabs1 = ParamUtil.getString(request, "tabs1", "mailing");

    PortletURL portletURL = renderResponse.createActionURL();
    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/edit_mailing.jsp");
    portletURL.setParameter("backURL", backURL);
    portletURL.setParameter("mailingId", String.valueOf(mailing.getMailingId()));
    portletURL.setParameter("javax.portlet.action", "editMailing");

    String mvcPath = ParamUtil.getString(request, "mvcPath");

    List<JournalArticle> articles = JournalArticleLocalServiceUtil
            .getArticles(themeDisplay.getScopeGroupId(), 0, 20);

    List<Newsletter> newsletters = NewsletterServiceUtil
            .getGroupNewsletters(themeDisplay.getScopeGroupId(), 0,
                    Integer.MAX_VALUE);
%>


<liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

<liferay-ui:tabs names="mailing,preview"
    param="tabs1" url="<%=portletURL.toString()%>" />

<c:choose>

    <c:when test='<%=tabs1.equals("mailing")%>'>
    
        <portlet:actionURL var="saveMailingURL" name="saveMailing" />
        
        <aui:form action="<%=saveMailingURL%>" method="post" name="fm">
        
            <aui:input name="backURL" type="hidden" value="<%=backURL%>" />
            <aui:input name="mailingId" type="hidden"
                value="<%=String.valueOf(mailing.getMailingId())%>" />
            <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
            <aui:input name="windowId" type="hidden" value="<%=windowId%>" />
            
            <aui:input name="title"
                value="<%= mailing.getTitle() %>"/>
                
            <aui:select name="newsletterId" label="newsletter">
                <aui:option label="select-newsletter" value="0"/>
                <%  
                    for (Newsletter newsletter : newsletters) {           
                %>
                    <aui:option label="<%= newsletter.getTitle() %>"
                        value="<%= newsletter.getNewsletterId() %>"
                        selected="<%= mailing.getNewsletterId() == newsletter.getNewsletterId() %>"/>
                <%
                    }
                %>
            </aui:select> 
            
            <aui:select name="articleId" label="article">
                <aui:option label="select-article" value="0"/>
                <%  
                    for (JournalArticle article : articles) {           
                %>
                    <aui:option label="<%= article.getTitle(locale) %>"
                        value="<%= article.getArticleId() %>"
                        selected="<%= article.getArticleId().equals(mailing.getArticleId()) %>"/>
                <%
                    }
                %>
            </aui:select> 
                
            <aui:input name="sent" type="checkbox" checked="<%= mailing.isSent() %>"/>    
            
            <aui:button-row>
                <aui:button type="submit" />
            </aui:button-row>
                        
        </aui:form>            
    </c:when>
    
    <c:otherwise>
        <div class="alert alert-info">
            <liferay-ui:message key="the-preview-does-not-include-settings-of-the-site-theme"/>
        </div>
        
        <%= MailingServiceUtil.prepareMailing(themeDisplay, mailing.getMailingId()) %>
        
    </c:otherwise>
    
</c:choose>

<hr>

<ifx-util:build-info/>
