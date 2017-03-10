<%--
    view.jsp: Default view of the data portlet.
    
    Created:    2017-03-09 19:59 by Christian Berndt
    Modified:   2017-03-09 19:59 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.security.auth.PrincipalException"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);
%>

<liferay-ui:header backURL="<%=backURL%>" title="data-manager" />

<liferay-ui:error exception="<%=PrincipalException.class%>"
    message="you-dont-have-the-required-permissions" />

<liferay-ui:tabs names="browse,import-export" param="tabs1"
    url="<%=portletURL.toString()%>" />

<c:choose>

    <c:when test='<%=tabs1.equals("import-export")%>'>
    
        <portlet:actionURL var="importMeasurementsURL" name="importMeasurements" />
        
        <portlet:renderURL var="browseURL" />

        <aui:form action="<%=importMeasurementsURL%>"
            enctype="multipart/form-data" method="post" name="fm"
            cssClass="import-form">

            <%
                // TODO: Add error handling
            %>
            <%-- 
               <liferay-ui:error exception="<%= FileExtensionException.class %>">
           
               </liferay-ui:error>
               --%>

            <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />

            <aui:fieldset label="import">

                <aui:input name="file" type="file" inlineField="true"
                    label="" />

                <aui:button name="import" type="submit" value="import"
                    disabled="true" />
                <aui:button href="<%=browseURL%>" type="cancel" />

            </aui:fieldset>

        </aui:form>

        <aui:script use="aui-base">
            var input = A.one('#<portlet:namespace />file');
            var button = A.one('#<portlet:namespace />import');
        
            input.on('change', function(e) {
        
                if (input.get('value')) {
                    button.removeClass('disabled');
                    button.removeAttribute('disabled');
                } else {
                    button.addClass('disabled');
                    button.setAttrs({
                        disabled : 'disabled'
                    });
                }
        
            });
        </aui:script>

    </c:when>

    <c:otherwise>
        Browse
    </c:otherwise>
</c:choose>

<hr>

<ifx-util:build-info />
