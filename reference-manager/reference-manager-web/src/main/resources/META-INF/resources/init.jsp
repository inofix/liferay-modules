<%--
    init.jsp: Common setup code for the reference manager portlet.
    
    Created:    2016-01-10 22:51 by Christian Berndt
    Modified:   2016-01-10 22:51 by Christian Berndt
    Version:    1.0.0
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>

<%@ page import="com.liferay.portal.kernel.upload.UploadException"%>
<%@ page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ page import="com.liferay.portal.kernel.util.StringPool"%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@ page import="com.liferay.portal.security.auth.PrincipalException"%>

<%@ page import="com.liferay.portlet.documentlibrary.FileExtensionException"%>

<%@ page import="com.liferay.taglib.search.ResultRow"%>

<%@ page import="javax.portlet.PortletURL"%>

<%@ page import="ch.inofix.referencemanager.model.Reference"%>
<%@ page import="ch.inofix.referencemanager.service.ReferenceLocalService"%>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
	PortletURL portletURL = renderResponse.createRenderURL();

	String currentURL = portletURL.toString();

	//get service bean
	ReferenceLocalService referenceLocalService = (ReferenceLocalService) request
			.getAttribute("referenceLocalService");	
%>
