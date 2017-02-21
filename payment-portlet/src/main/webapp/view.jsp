<%--
    view.jsp: Default view of the payment-portlet.
    
    Created:     2017-02-03 13:59 by Christian Berndt
    Modified:    2017-02-21 19:38 by Christian Berndt
    Version:     1.0.7
 --%>

<%@ include file="/html/init.jsp"%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    String backURL = ParamUtil.getString(request, "backURL");

    if (Validator.isNull(backURL)) {
        backURL = ParamUtil.getString(request, "redirect");
    }

    if (Validator.isNull(backURL)) {
        backURL = ParamUtil.getString(originalRequest, "redirect");
    }
    
    PortletURL portletURL = renderResponse.createRenderURL();
    
    for (String param : PaymentConstants.BILLING_ADDRESS_PARAMETERS) {
        String value = ParamUtil.getString(renderRequest, param);
        if (Validator.isNull(value)) {
            value = ParamUtil.getString(originalRequest, param);
        }
        if (Validator.isNotNull(value)) {
            portletURL.setParameter(param, value);
        }
    }
    
    for (String param : PaymentConstants.COMMON_PARAMETERS) {
        String value = ParamUtil.getString(renderRequest, param);
        if (Validator.isNull(value)) {
            value = ParamUtil.getString(originalRequest, param);
        }
        if (Validator.isNotNull(value)) {
            portletURL.setParameter(param, value);
        }
    }
    
    portletURL.setParameter("backURL", backURL); 

    List<String> serviceList = Arrays.asList(services);
    Iterator<String> iterator = serviceList.iterator();

    String tabs1 = ParamUtil.getString(renderRequest, "tabs1",
            "credit-card-redirect");

    StringBuilder sb = new StringBuilder();

    while (iterator.hasNext()) {
        sb.append(iterator.next());
        if (iterator.hasNext()) {
            sb.append(StringPool.COMMA);
        }
    }

    String tabNames = sb.toString();
    
    String cssClass = ""; 
    if ("left".equals(tabOrientation)) {
        cssClass = "tabbable tabs-left";       
    }
    if ("right".equals(tabOrientation)) {
        cssClass = "tabbable tabs-right";               
    }
%>

<portlet:actionURL name="submitPayment" var="actionURL">
</portlet:actionURL>

<liferay-ui:header backURL="<%=backURL%>" title="payment-portlet" />

<div class="<%= cssClass %>">

    <liferay-ui:tabs names="<%= tabNames %>" param="tabs1"
        portletURL="<%= portletURL %>" />


    <div class="tab-content">
        <aui:form action="<%= actionURL.toString() %>" method="post"
            name="fm1">
            <aui:input name="tabs1" type="hidden" value="<%= tabs1 %>" />

            <c:if test='<%= tabs1.equals("credit-card-redirect") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="cc" />

                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/redirect_urls.jspf"%>
                <c:if test="<%= showRecurring %>">
                    <%@ include file="/html/recurring.jspf"%>
                </c:if>
            </c:if>

            <c:if test='<%= tabs1.equals("credit-card-inline") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="cc" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <c:if test="<%= showRecurring %>">
                    <%@ include file="/html/recurring.jspf"%>
                </c:if>
            </c:if>

            <c:if test='<%= tabs1.equals("direct-debit") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="dd" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <c:if test="<%= showRecurring %>">
                    <%@ include file="/html/recurring.jspf"%>
                </c:if>
                <aui:fieldset label="direct-debit">
                    <aui:row>
                        <aui:col span="6">
                            <aui:input name="iban" required="true" />
                            <aui:input name="bic" required="true" />
                        </aui:col>
                        <aui:col span="6">
                            <aui:input name="account_holder"
                                required="true" />
                            <aui:input name="sepa_mandate" />
                        </aui:col>
                    </aui:row>
                </aui:fieldset>
            </c:if>

            <c:if test='<%= tabs1.equals("purchase-on-account-b2c") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="kar" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/shipping_address.jspf"%>
            </c:if>

            <c:if test='<%= tabs1.equals("purchase-on-account-b2b") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="kar_b2b" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/shipping_address.jspf"%>
            </c:if>

            <c:if test='<%= tabs1.equals("giro-pay") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="giro" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/redirect_urls.jspf"%>
            </c:if>

            <c:if test='<%= tabs1.equals("sofort-ueberweisung") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="sofort" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/redirect_urls.jspf"%>
            </c:if>

            <c:if test='<%= tabs1.equals("paypal") %>'>
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/redirect_urls.jspf"%>
                <c:if test="<%= showRecurring %>">
                    <%@ include file="/html/recurring.jspf"%>
                </c:if>
            </c:if>

            <c:if test='<%= tabs1.equals("pay-direkt") %>'>
                <aui:input name="payment_type" required="true"
                    type="hidden" value="paydirekt" />
                <%@ include file="/html/common_parameters.jspf"%>
                <%@ include file="/html/billing_address.jspf"%>
                <%@ include file="/html/redirect_urls.jspf"%>
            </c:if>

            <aui:button-row>
                <aui:button type="submit" />
                <c:if test="<%= Validator.isNotNull(backURL) %>">
                    <aui:a cssClass="btn" href="<%= backURL %>"
                        label="cancel" title="cancel" />
                </c:if>
            </aui:button-row>
            
        </aui:form>
    </div>
</div>

<hr>

<ifx-util:build-info />
