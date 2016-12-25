<%--
    view.jsp: Default view of inofix' activities portlet.
    
    Created:    2016-12-26 00:17 by Christian Berndt
    Modified:   2016-12-26 00:17 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    List<SocialActivity> bibliographyActivities = SocialActivityLocalServiceUtil
            .getActivities("ch.inofix.referencemanager.model.Bibliography", 0, 20);

    List<SocialActivity> referenceActivities = SocialActivityLocalServiceUtil
            .getActivities("ch.inofix.referencemanager.model.Reference", 0, 20);

    List<SocialActivity> socialActivities = new ArrayList<SocialActivity>(bibliographyActivities);
    socialActivities.addAll(referenceActivities);         
%>

<liferay-ui:social-activities   
    activities="<%= socialActivities %>"
/>