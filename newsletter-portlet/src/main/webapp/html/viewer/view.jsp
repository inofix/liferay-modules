<%--
    /viewer/view.jsp: View of the newsletter-viewer-portlet
    
    Created:    2016-10-24 22:56 by Christian Berndt
    Modified:   2016-10-24 22:56 by Christian Berndt
    Version:    1.0.0
--%>

<%@include file="/html/init.jsp"%>

<%@page import="ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil"%>

<%
    HttpServletRequest original = PortalUtil
            .getOriginalServletRequest(request);

    long mailingId = ParamUtil.getLong(original, "mailingId");
    String uid = ParamUtil.getString(original, "uid");

    Log log = LogFactoryUtil.getLog("docroot.html.viewer_view_jsp");

    SearchContext searchContext = SearchContextFactory
            .getInstance(request);

    Mailing mailing = null;
    try {
        mailing = MailingLocalServiceUtil.getMailing(mailingId);
    } catch (Exception e) {
        log.error(e);
    }

    Subscriber subscriber = null;
    try {
        subscriber = SubscriberServiceUtil.getSubscriber(
                themeDisplay.getScopeGroupId(), searchContext, uid);
    } catch (Exception e) {
        log.error(e);
    }
%>

<c:choose>
    <c:when test="<%= Validator.isNotNull(subscriber) && Validator.isNotNull(mailing) %>">  
        <%
            Map<String, Object> contextObjects = new HashMap<String, Object>();
            contextObjects.put("subscriber", subscriber); 
            
            StringBuilder sb = new StringBuilder();
            if (mailing.getNewsletterId() > 0 ) {
                try {
                    sb.append(MailingServiceUtil.prepareMailing(contextObjects, mailing.getMailingId())); 
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    sb.append("<pre>");
                    sb.append(sw.toString());
                    sb.append("</pre>");
                }
            }
        %>
        
        <%= sb.toString() %>
        
    </c:when>
    <c:otherwise>
        <div class="alert alert-error">
            <liferay-ui:message key="the-requested-mailing-is-not-available"/>
        </div>
    </c:otherwise>
</c:choose>