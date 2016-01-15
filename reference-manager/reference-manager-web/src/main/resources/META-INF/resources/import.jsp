<%--
    export.jsp: The export-gui of the reference manager portlet.
    
    Created:    2016-01-10 23:33 by Christian Berndt
    Modified:   2016-01-16 00:37 by Christian Berndt
    Version:    0.0.2
--%>

<%@ include file="/init.jsp" %>

<%
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");
%>

<portlet:actionURL name="importBibtexFile" var="importBibtexFileURL" />

<aui:form action="<%= importBibtexFileURL %>" enctype="multipart/form-data" method="post" name="fm1">
<%--     <aui:input name="mvcPath" type="hidden" value="/install_local_app.jsp" /> --%>

    <%-- 
    <c:if test="<%= CompanyLocalServiceUtil.getCompaniesCount(false) > 1 %>">
        <div class="alert alert-info">
            <liferay-ui:message key="installed-apps-are-available-to-all-portal-instances.-go-to-plugins-configuration-within-each-portal-instance-to-enable-disable-each-app" />
        </div>
    </c:if>
    --%>

    <liferay-ui:error exception="<%= FileExtensionException.class %>" message="please-upload-a-file-with-a-valid-extension-jar-lpkg-or-war" />
    <liferay-ui:error exception="<%= UploadException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />

    <%-- 
    <liferay-ui:success key="pluginDownloaded" message="the-plugin-was-downloaded-successfully-and-is-now-being-installed" />
    <liferay-ui:success key="pluginUploaded" message="the-plugin-was-uploaded-successfully-and-is-now-being-installed" />
    --%>
    
    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="importBibtexFile" />
    
    <aui:fieldset label="import">
        <aui:input cssClass="file-input" label="" name="file" type="file" />
        <aui:button type="submit" value="import" />
    </aui:fieldset>
</aui:form>