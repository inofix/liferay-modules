<%--
    edit_mailing.jsp: edit the mailing settings. 
    
    Created:    2016-10-10 18:34 by Christian Berndt
    Modified:   2015-10-13 17:41 by Christian Berndt
    Version:    1.0.5
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
            <aui:input name="windowId" type="hidden"
                value="<%=windowId%>" />

            <aui:input name="title" helpMessage="title-help"
                inlineField="true" value="<%=mailing.getTitle()%>" />

            <aui:select name="newsletterId" helpMessage="newsletter-help"
                label="newsletter" inlineField="true">
                <aui:option label="select-newsletter" value="0" />
                <%
                    for (Newsletter newsletter : newsletters) {
                %>
                <aui:option label="<%=newsletter.getTitle()%>"
                    value="<%=newsletter.getNewsletterId()%>"
                    selected="<%=mailing.getNewsletterId() == newsletter
                                        .getNewsletterId()%>" />
                <%
                    }
                %>
            </aui:select>

            <aui:select name="articleId" helpMessage="article-help"
                label="article" inlineField="true">
                <aui:option label="select-article" value="0" />
                <%
                    for (JournalArticle article : articles) {
                %>
                <aui:option label="<%=article.getTitle(locale)%>"
                    value="<%=article.getArticleId()%>"
                    selected="<%=article.getArticleId().equals(
                                        mailing.getArticleId())%>" />
                <%
                    }
                %>
            </aui:select>

            <aui:input name="sent" type="checkbox" inlineField="true"
                checked="<%=mailing.isSent()%>" />

            <aui:button type="submit" />

        </aui:form>
        
        <hr>

        <portlet:actionURL var="sendMailingURL" name="sendMailing">
        </portlet:actionURL>

        <aui:form action="<%=sendMailingURL%>" name="fm1">

            <aui:input name="backURL" type="hidden" value="<%=backURL%>" />
            <aui:input name="mailingId" type="hidden"
                value="<%=String.valueOf(mailing.getMailingId())%>" />
            <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
            <aui:input name="windowId" type="hidden"
                value="<%=windowId%>" />
                                
            <aui:input name="email" 
                label="send-test-mail-to"
                helpMessage="send-test-mail-to-help"
                inlineField="true" required="true" 
                value="berndt@kulturtechniker.de"/>

            <aui:button type="submit" value="send" />
        </aui:form>
        
                
        <%
            Newsletter newsletter = null; 
        
            if (mailing.getNewsletterId() > 0) {
                newsletter = NewsletterServiceUtil.getNewsletter(mailing.getNewsletterId());
            }
        %>
        
        <c:if test="<%= newsletter != null %>">
        
            <hr>
    
            <aui:form action="<%=sendMailingURL%>" name="fm2">
    
                <aui:input name="backURL" type="hidden" value="<%=backURL%>" />
                <aui:input name="mailingId" type="hidden"
                    value="<%=String.valueOf(mailing.getMailingId())%>" />
                <aui:input name="mvcPath" type="hidden" value="<%=mvcPath%>" />
                <aui:input name="windowId" type="hidden"
                    value="<%=windowId%>" />
    
                <aui:field-wrapper inlineField="true">
                    <liferay-ui:message
                        key="send-the-mailing-to-the-subscribers-of-newsletter-x"
                        arguments="<%=newsletter.getTitle() %>" />
                </aui:field-wrapper>
    
                <aui:button type="submit" value="send" />
            </aui:form>
        
        </c:if>

    </c:when>

    <c:otherwise>
        <div class="alert alert-info">
            <liferay-ui:message key="the-preview-does-not-include-settings-of-the-site-theme"/>
        </div>
        
        <%
            Map<String, String> contact_ = new HashMap<String, String>();
            contact_.put("firstname", "Christian");
            contact_.put("lastname", "Berndt");
            contact_.put("gender", "male");

            Map<String, Object> contextObjects = new HashMap<String, Object>();
            contextObjects.put("contact", contact_);        
        %>
        
        <%= MailingServiceUtil.prepareMailing(contextObjects, mailing.getMailingId()) %>
        
    </c:otherwise>
    
</c:choose>

<hr>

<ifx-util:build-info/>
