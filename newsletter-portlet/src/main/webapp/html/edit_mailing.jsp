<%--
    edit_mailing.jsp: edit the mailing settings. 
    
    Created:    2016-10-10 18:34 by Christian Berndt
    Modified:   2015-10-17 22:22 by Christian Berndt
    Version:    1.0.8
--%>

<%@include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.newsletter.model.impl.NewsletterModelImpl"%>
<%@page import="ch.inofix.portlet.newsletter.service.SubscriberLocalServiceUtil"%>

<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@page import="com.liferay.portlet.journal.service.JournalArticleServiceUtil"%>

<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    Mailing mailing = (Mailing) request.getAttribute("MAILING");

    String articleId = mailing.getArticleId();

    Log log = LogFactoryUtil.getLog("docroot.html.edit_mailing.jsp");

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
    portletURL.setParameter("mailingId",
            String.valueOf(mailing.getMailingId()));
    portletURL.setParameter("javax.portlet.action", "editMailing");

    String mvcPath = ParamUtil.getString(request, "mvcPath");

    OrderByComparator obc = OrderByComparatorFactoryUtil.create(
            NewsletterModelImpl.TABLE_NAME, "modifiedDate", false);

    List<JournalArticle> articles = JournalArticleServiceUtil
            .getGroupArticles(themeDisplay.getScopeGroupId(),
                    themeDisplay.getUserId(), 0, 0, 20, obc);

    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    boolean reverse = false;

    Sort sort = new Sort("title_sortable", reverse);

    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(0);
    searchContext.setEnd(20);
    searchContext.setSorts(sort);

    Indexer indexer = IndexerRegistryUtil.getIndexer(Newsletter.class);

    Hits hits = indexer.search(searchContext);

    List<Newsletter> newsletters = new ArrayList<Newsletter>();

    for (int i = 0; i < hits.getDocs().length; i++) {

        Document doc = hits.doc(i);

        long newsletterId = GetterUtil.getLong(doc
                .get(Field.ENTRY_CLASS_PK));

        Newsletter newsletter = null;

        try {
            newsletter = NewsletterServiceUtil
                    .getNewsletter(newsletterId);
        } catch (PortalException pe) {
            log.error(pe.getLocalizedMessage());
        } catch (SystemException se) {
            log.error(se.getLocalizedMessage());
        }

        if (newsletter != null) {
            newsletters.add(newsletter);
        }
    }
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
                
            <aui:row>
            
                <aui:col span="6">       
                    <aui:input name="title" helpMessage="title-help"
                        inlineField="true" required="true" 
                        value="<%=mailing.getTitle()%>" />                   

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
                        <aui:option label="select-article" value="" />
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
                </aui:col>

                <aui:col span="4">
                    <div class="editor-wrapper">
                        <aui:input name="template" type="textarea"
                            helpMessage="mailing-template-help" inlineField="true"
                            value="<%=mailing.getTemplate()%>" />
                    </div>
                </aui:col>

                <%
                    // TODO: sendDate
                %>
            </aui:row>

            <aui:button-row>
                <aui:button type="submit" />
            </aui:button-row>

        </aui:form>
                
        <%
            Newsletter newsletter = null; 
        
            if (mailing.getNewsletterId() > 0) {
                newsletter = NewsletterServiceUtil.getNewsletter(mailing.getNewsletterId());
            }
        %>
        
        <c:if test="<%= newsletter != null %>">        
                
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
            Subscriber subscriber = SubscriberLocalServiceUtil.createSubscriber(0);
            subscriber.setEmail("firstname.lastname@example.com"); 
            subscriber.setFirstname("Firstname"); 
            subscriber.setLastname("Lastname"); 
            subscriber.setMiddlename("Middlename"); 
            subscriber.setName("Firstname Lastname");
            subscriber.setSalutation("Mr. / Ms.");           

            Map<String, Object> contextObjects = new HashMap<String, Object>();
            contextObjects.put("subscriber", subscriber); 
            
            StringBuilder preview = new StringBuilder();
            if (mailing.getNewsletterId() > 0 ) {
                try {
                    preview.append(MailingServiceUtil.prepareMailing(contextObjects, mailing.getMailingId())); 
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    preview.append("<pre>");
                    preview.append(sw.toString());
                    preview.append("</pre>");
                }
            }
        %>
        
        <%= preview.toString() %>
        
        <c:if test="<%= mailing.getNewsletterId() <= 0 %>">
            <div class="alert alert-warn">
                <liferay-ui:message key="no-newsletter-selected"/>
            </div>
        </c:if>        
        
        <c:if test="<%= Validator.isNull(articleId)  %>">
            <div class="alert alert-warn">
                <liferay-ui:message key="no-article-selected"/>
            </div>
        </c:if>        
        
        
    </c:otherwise>
    
</c:choose>

<hr>

<ifx-util:build-info/>