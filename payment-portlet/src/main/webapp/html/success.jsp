<%--
    success.jsp: Success view of the payment-portlet.
    
    Created:     2017-02-06 12:35 by Christian Berndt
    Modified:    2017-02-06 12:35 by Christian Berndt
    Version:     1.0.0
 --%>
 
<%@ include file="/html/init.jsp"%>

<%@ include file="/html/init.jsp"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String message = (String) request.getAttribute("message"); 
%>

<liferay-ui:header backURL="<%=backURL%>" title="payment-portlet" />

<div class="alert alert-success">
    <%= message %>
</div>