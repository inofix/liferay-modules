<%--
    init.jsp: Common setup code for inofix' activities portlet.
    
    Created:    2016-12-26 00:17 by Christian Berndt
    Modified:   2016-12-26 00:17 by Christian Berndt
    Version:    1.0.0
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@taglib uri="http://liferay.com/tld/security" prefix="liferay-security"%>
<%@taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="com.liferay.social.kernel.model.SocialActivity"%>
<%@page import="com.liferay.social.kernel.service.SocialActivityLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>

<liferay-theme:defineObjects />

<portlet:defineObjects />
