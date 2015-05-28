<%--
    close_popup.jsp: Close the popup window 
    
    Created:    2015-05-28 13:12 by Christian Berndt
    Modified:   2015-05-28 16:14 by Christian Berndt
    Version:    1.0.1
--%>


<%@include file="/html/init.jsp" %>

<%@page import="com.liferay.portal.service.PortletLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Portlet"%>

<%
	String redirect = request.getParameter("redirect");
	String windowId = ParamUtil.getString(request, "windowId", "editAsset");
	
	if (Validator.isNull(windowId)) {
		windowId = "editAsset"; 
	}

	redirect = PortalUtil.escapeRedirect(redirect);

	Portlet selPortlet = PortletLocalServiceUtil.getPortletById(
			company.getCompanyId(), portletDisplay.getId());
%>

<aui:script use="aui-base">
    Liferay.fire(
        'closeWindow',
        {
            id: '<portlet:namespace /><%= windowId %>',
            portletAjaxable: <%= selPortlet.isAjaxable() %>,

            <c:choose>
                <c:when test="<%= redirect != null %>">
                    redirect: '<%= HtmlUtil.escapeJS(redirect) %>'
                </c:when>
                <c:otherwise>
                    refresh: '<%= portletDisplay.getId() %>'
                </c:otherwise>
            </c:choose>
        }
    );
</aui:script>
