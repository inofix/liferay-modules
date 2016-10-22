<%--
    preview_mailing.jsp: preview a mailing personalized for a subscriber.
    
    Created:    2016-10-13 17:02 by Christian Berndt
    Modified:   2015-10-20 18:21 by Christian Berndt
    Version:    1.0.1
--%>

<%@include file="/html/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    Mailing mailing = (Mailing) request.getAttribute("MAILING");
    Newsletter newsletter = (Newsletter) request.getAttribute("NEWSLETTER");
    Subscriber subscriber = (Subscriber) request.getAttribute("SUBSCRIBER");
    
    long mailingId = 0;
    
    if (mailing != null) {
        mailingId = mailing.getMailingId();
    }
    
    String vCardGroupId = null; 
    if (newsletter != null) {
        vCardGroupId = newsletter.getVCardGroupId(); 
    }
    
    String vCardUID = null; 
    if (subscriber != null) {
        vCardUID = subscriber.getVCardUID();
    }
    
    Log log = LogFactoryUtil.getLog("docroot.html.edit_mailing.jsp");

    String windowId = "";
    windowId = ParamUtil.getString(request, "windowId");

    // Close the popup, if we are in popup mode, a redirect was provided
    // and the windowId is "editMailing" (which means, viewByDefault 
    // is false.

    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp()
            && "previewMailing".equals(windowId)) {

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        portletURL.setParameter("redirect", redirect);
        portletURL.setParameter("windowId", windowId);
        backURL = portletURL.toString();
    }

    String orderByCol = ParamUtil.getString(request, "orderByCol", "modified"); 
    String orderByType = ParamUtil.getString(request, "orderByType", "desc"); 

    int start = 0;
    int end = 20;

    SearchContext searchContext =
        SearchContextFactory.getInstance(request);

    boolean reverse = "desc".equals(orderByType);

    Sort sort = new Sort(orderByCol, reverse);

    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(start);
    searchContext.setEnd(end);
    searchContext.setSorts(sort);

    Indexer indexer = IndexerRegistryUtil.getIndexer(Mailing.class);

    Hits hits = indexer.search(searchContext);

    List<Mailing> mailings = new ArrayList<Mailing>(); 
    
    for (int i = 0; i < hits.getDocs().length; i++) {
        
        Document doc = hits.doc(i);

        long classPK =
            GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));
        
        Mailing mailing_ = null;

        try {
            mailing_ = MailingServiceUtil.getMailing(classPK);
        }
        catch (PortalException pe) {
            log.error(pe.getLocalizedMessage());
        }
        catch (SystemException se) {
            log.error(se.getLocalizedMessage());
        }

        if (mailing_ != null) {
            mailings.add(mailing_);
        }        
    }    
%>

<liferay-ui:header backURL="<%=backURL%>" title="newsletter-manager" />

<div class="alert alert-info">
    <liferay-ui:message key="the-preview-does-not-include-settings-of-the-site-theme"/>
</div>

<aui:form name="fm">
    <aui:select name="mailingId" label=""  onChange=" window.location.href = this.value; ">
        <aui:option value="" label="select-mailing"/>
        <%  for (Mailing mailing_ : mailings) { 
            
                PortletURL selectURL = renderResponse.createActionURL();
                selectURL.setWindowState(LiferayWindowState.POP_UP);
                selectURL.setParameter("javax.portlet.action", "previewMailing");
                selectURL.setParameter("mailingId", String.valueOf(mailing_.getMailingId()));
                selectURL.setParameter("mvcPath", "/html/preview_mailing.jsp");
                selectURL.setParameter("redirect", backURL);
                if (Validator.isNotNull(vCardGroupId)) {
                    selectURL.setParameter("vCardGroupId", vCardGroupId );
                }                
                if (Validator.isNotNull(vCardUID)) {
                    selectURL.setParameter("vCardUID", vCardUID );
                }
                
                boolean selected = mailing_.getMailingId() == mailingId;
        %>       
            <option <%= selected ? "selected" : StringPool.BLANK %> value="<%= selectURL.toString() %>" ><%= mailing_.getTitle() %></option>
        <%  } %>
    </aui:select>
</aui:form>

<c:if test="<%= Validator.isNull(mailing) %>">
    <div class="alert alert-warn">
        <liferay-ui:message key="no-mailing-selected"/>
    </div>
</c:if>

<c:if test="<%= Validator.isNull(newsletter) %>">
    <div class="alert alert-warn">
        <liferay-ui:message key="no-newsletter-selected"/>
    </div>
</c:if>

<c:if test="<%= Validator.isNull(subscriber) %>">
    <div class="alert alert-warn">
        <liferay-ui:message key="no-subscriber-selected"/>
    </div>
</c:if>

<c:if test="<%= Validator.isNotNull(subscriber) && Validator.isNotNull(mailing) %>">

    <%
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
    
</c:if>

<hr>

<ifx-util:build-info/>
