<%--
    edit_mailing.jsp: edit the mailing settings. 
    
    Created:    2016-10-10 18:34 by Christian Berndt
    Modified:   2015-10-24 17:52 by Christian Berndt
    Version:    1.1.6
--%>

<%@include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.newsletter.model.impl.NewsletterModelImpl"%>

<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@page import="com.liferay.portlet.journal.service.JournalArticleServiceUtil"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    Mailing mailing = (Mailing) request.getAttribute("MAILING");

    boolean disabled = mailing.isSent();

    String articleId = mailing.getArticleId();

    Log log = LogFactoryUtil.getLog("docroot.html.edit_mailing_jsp");

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

    List<JournalArticle> articles = new ArrayList<JournalArticle>();

    if (Validator.isNotNull(newsletterStructureId)) {

        articles = JournalArticleServiceUtil.getArticlesByStructureId(
                themeDisplay.getScopeGroupId(), newsletterStructureId,
                0, 20, obc);

    } else {

        articles = JournalArticleServiceUtil.getGroupArticles(
                themeDisplay.getScopeGroupId(),
                themeDisplay.getUserId(), 0, 0, 20, obc);
    }

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

<c:if test="<%= disabled %>">
    <div class="alert alert-warn">
        <liferay-ui:message key="the-mailing-has-been-sent-already"/>
    </div>
</c:if>

<liferay-ui:tabs names="mailing,preview,send"
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
                    <aui:input name="title" disabled="<%= disabled%>"
                        helpMessage="title-help" inlineField="true"
                        required="true" value="<%=mailing.getTitle()%>" />

                    <aui:select name="newsletterId"
                        disabled="<%=disabled%>"
                        helpMessage="newsletter-help" label="newsletter"
                        inlineField="true">
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

                    <aui:select name="articleId"
                        disabled="<%=disabled%>"
                        helpMessage="article-help" label="article"
                        inlineField="true">
                        <aui:option label="select-article" value="" />
                        <%
                            for (JournalArticle article : articles) {
                        %>
                        <aui:option
                            label="<%=article.getTitle(locale)%>"
                            value="<%=article.getArticleId()%>"
                            selected="<%=article.getArticleId().equals(
                                                mailing.getArticleId())%>" />
                        <%
                            }
                        %>
                    </aui:select>

                    <aui:field-wrapper name="publishDate"
                        helpMessage="publish-date-help"
                        inlineField="true">
                        <liferay-ui:input-date name="publishDate"
                            dayParam="publishDate.day"
                            dayValue="<%=mailing.getPublishDateDay()%>"
                            monthParam="publishDate.month"
                            monthValue="<%=mailing.getPublishDateMonth()%>"
                            yearParam="publishDate.year"
                            yearValue="<%=mailing.getPublishDateYear()%>"
                            disabled="<%=disabled%>"
                            nullable="<%=mailing.getPublishDate() == null%>" />
                    </aui:field-wrapper>

                    <aui:field-wrapper name="sendDate"
                        helpMessage="send-date-help" inlineField="true">
                        <liferay-ui:input-date name="sendDate"
                            dayParam="sendDate.day"
                            dayValue="<%=mailing.getSendDateDay()%>"
                            monthParam="sendDate.month"
                            monthValue="<%=mailing.getSendDateMonth()%>"
                            yearParam="sendDate.year"
                            yearValue="<%=mailing.getSendDateYear()%>"
                            disabled="<%=disabled%>"
                            nullable="<%=mailing.getSendDate() == null%>" />
                    </aui:field-wrapper>

                    <div class="editor-wrapper">
                        <aui:input name="template"
                            disabled="<%=disabled%>"
                            helpMessage="mailing-template-help"
                            inlineField="true" type="textarea"
                            value="<%=mailing.getTemplate()%>" />
                    </div>                    
                    
                </aui:col>       
            </aui:row>

            <aui:button-row>
                <aui:button disabled="<%= disabled %>" type="submit" />
            </aui:button-row>

        </aui:form>
        
        <c:if test="<%= mailing.getMailingId() > 0%>">
            <hr>
        </c:if>
        
        <%@include file="/html/edit_mailing_send.jspf" %>
        
    </c:when>
    
    <c:when test='<%=tabs1.equals("send")%>'>
        <%@include file="/html/edit_mailing_send.jspf" %>    
    </c:when>

    <c:otherwise>
        
        <div class="alert alert-info">
            <liferay-ui:message key="the-preview-does-not-include-settings-of-the-site-theme"/>
        </div>
        
        <%
            Subscriber subscriber = SubscriberServiceUtil.createSubscriber();
            subscriber.setEmail("firstname.lastname@example.com"); 
            subscriber.setFirstname("Firstname"); 
            subscriber.setLastname("Lastname"); 
            subscriber.setMiddlename("Middlename"); 
            subscriber.setName("Firstname Lastname");

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
        
        <hr/>
        
        <%@include file="/html/edit_mailing_send.jspf" %>
        
    </c:otherwise>
    
</c:choose>

<hr>

<ifx-util:build-info/>
