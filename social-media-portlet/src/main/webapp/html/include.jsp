<%--
    include.jsp: include shariff.js and shariff.css on every portal page
    
    Created:    2015-10-10 20:53 by Christian Berndt
    Modified:   2015-10-10 20:53 by Christian Berndt
    Version:    1.0.0
--%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%-- shariff.css and shariff.js are copied via a hook to the portal's --%>
<%-- default js and css directories.                                             --%>
<liferay-util:html-top>
    <link href="/html/css/shariff.complete.css" rel="stylesheet" type="text/css"/>
</liferay-util:html-top>

<liferay-util:html-bottom>
    <script src="/html/js/shariff.complete.js" type="text/javascript"></script>
</liferay-util:html-bottom>
