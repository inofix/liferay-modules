<%--
    init.jsp: Common imports and setup code of the my-sites-directory-portlet.
    
    Created:    2017-04-26 17:00 by Christian Berndt
    Modified:   2017-04-26 17:00 by Christian Berndt
    Version:    1.0.0
--%>

<%-- Required classes --%>

<%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>

<%-- Required taglibs --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    int maxGroups = PrefsParamUtil.getInteger(portletPreferences, renderRequest, "maxGroups", 30);
%>
