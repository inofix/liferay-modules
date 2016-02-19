<%--
	revision_display/page.jsp: The jsp-part of the
	RevisionDisplayTag.
	
	Created:	2013-11-02 16:30 by Christian Berndt
	Modified:	2013-11-02 16:30 by Christian Berndt
	Version: 	1.0
	
--%>

<%-- Import required classes. --%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Properties"%>

<%-- Import required taglibs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	final String NOT_AVAILABLE = "Not available"; 

	// Read the configuration parameter from the request.
	boolean displayBuildDate = (Boolean) request
			.getAttribute("inofix-util:revision-display:displayBuildDate");
	boolean displayBuildNumber = (Boolean) request
			.getAttribute("inofix-util:revision-display:displayBuildNumber");
	boolean displayServiceBuildNumber = (Boolean) request
			.getAttribute("inofix-util:revision-display:displayServiceBuildNumber");
	boolean displaySVNRevision = (Boolean) request
			.getAttribute("inofix-util:revision-display:displaySVNRevision");
	boolean displayVersion = (Boolean) request
			.getAttribute("inofix-util:revision-display:displayVersion");


	// Read the build properties from
	// the MANIFEST.MF
	ServletContext ctxt = request.getSession().getServletContext();
	InputStream in = ctxt.getResourceAsStream("/META-INF/MANIFEST.MF");
	Properties props = new Properties();

	if (in != null) {
		props.load(in);
	}

	String buildNumber = props.getProperty("Implementation-Build");
	if (buildNumber == null) buildNumber = NOT_AVAILABLE;
	
	String buildVersion = props.getProperty("Implementation-Version");
	if (buildVersion == null) buildVersion = NOT_AVAILABLE; 
	
	String svnRevision = props.getProperty("SVN-Revision");
	if (svnRevision == null) svnRevision = NOT_AVAILABLE; 
	
// 	String dateInMillis = props.getProperty("Build-Date");

// 	Date date = new Date();
// 	if (dateInMillis != null) {
// 		date.setTime(Long.parseLong(dateInMillis));
// 	}

// 	SimpleDateFormat sdf = new SimpleDateFormat(
// 			"EEE, yyyy-MM-dd HH:mm:ss");
// 	String buildDate = sdf.format(date);

	String buildDate = props.getProperty("Build-Time"); 

	// Load the properties stored service.properties
	// (Especially build.number - the service build number)
	in = ctxt.getResourceAsStream("/WEB-INF/classes/service.properties");

	Properties serviceProperties = new Properties();

	if (in != null) {
		serviceProperties.load(in);
	}

	String serviceBuildNumber = serviceProperties
			.getProperty("build.number");
	if (serviceBuildNumber == null) serviceBuildNumber = NOT_AVAILABLE; 

	// Store the service props information
	// in the page scope.
	pageContext.setAttribute("serviceBuildNumber", serviceBuildNumber);
%>

<div class="revision-display-tag" style="clear:both; padding-top: 20px; padding-bottom: 20px;">
	<c:if test="<%= displayVersion %>">
	    Version: <%= buildVersion %>&nbsp;&nbsp;&nbsp;
    </c:if>
	<c:if test="<%= displayServiceBuildNumber %>">
	    Service Build: <%= serviceBuildNumber %>&nbsp;&nbsp;&nbsp;
    </c:if>
    <c:if test="<%= displayBuildNumber %>">
	    Build: <%= buildNumber %>
    </c:if>
    <c:if test="<%= displayBuildDate %>">
	    (<%= buildDate %>)&nbsp;&nbsp;&nbsp;
    </c:if>
    <c:if test="<%= displaySVNRevision %>">
	    SVN Revision: <%= svnRevision %>&nbsp;&nbsp;&nbsp;
    </c:if>
    <!-- Issue Tracking: TODO  --> 
</div>