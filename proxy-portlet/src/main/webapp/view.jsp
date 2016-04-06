<%--
    view.jsp: Default view of the proxy portlet.
    
    Created:    2016-04-06 15:41 by Christian Berndt
    Modified:   2016-04-06 15:41 by Christian Berndt
    Version:    1.0.0
--%>

<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>

<%
    String video = "https://youtu.be/7eNzc3QGgvg";
    // String video = "https://vimeo.com/147226235"; 

    String service = "http://www.youtube.com/oembed?url=";
    // String service = "https://vimeo.com/api/oembed.json?url=";

    String url =
        service + video + "&maxwidth=300&maxheight=400&format=json";

    String oembed = ParamUtil.getString(request, "oembed", url);

    String json = HttpUtil.URLtoString(oembed);

    JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

    String html = jsonObject.getString("html");
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

This is the <b>proxy-portlet</b>. <br/>

result = <form><textarea><%= json %></textarea></form> <br/>

<%-- html = <%= html %> --%>

<portlet:resourceURL var="oEmbedURL">
    <portlet:param name="url" value="<%= HtmlUtil.escapeURL(url) %>"/>
</portlet:resourceURL>

<%= oEmbedURL %>

<script type="text/javascript">
<!--

/**
 * jQuery plugins
 */

// $( document ).ready(function() {

//     /**
//      * oEmbed
//      */    
<%--     $.get( "<%= oEmbedURL %>", function( data ) { --%>
//           console.log(data);
//     });
    
// });
//-->
</script>    
