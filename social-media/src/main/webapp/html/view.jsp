<%--
    view.jsp: Default view of the social-media-portlet
    
    Created:    2015-08-19 22:17 by Christian Berndt
    Modified:   2015-08-20 16:07 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%-- Import required classes --%>

<%@page import="java.util.Iterator"%>

<%
	List<String> services = new ArrayList<String>(
			Arrays.asList(selectedServices));
 
    Iterator<String> iter = services.iterator();
    
    StringBuilder servicesConfig = new StringBuilder(); 
    
    servicesConfig.append("["); 
    
    while (iter.hasNext()) {	
    	
        servicesConfig.append("&quot;"); 
        servicesConfig.append(iter.next()); 
        servicesConfig.append("&quot;"); 
        
        if (iter.hasNext()) {
        	servicesConfig.append(","); 
        }  	
    }
    
    servicesConfig.append("]"); 

%>

<div class="shariff" data-backend-url="<%= backendUrl %>"
	data-url="http://www.example.com/my-article.html" data-theme="<%= selectedTheme %>"
	data-orientation="<%= selectedOrientation %>" data-services="<%= servicesConfig %>"></div>

<c:if test="<%= showBuildInfo %>">
	<ifx-util:build-info />
</c:if>
