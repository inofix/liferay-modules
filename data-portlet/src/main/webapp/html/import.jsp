<%--
    import.jsp: The import panel of the data-portlet
    
    Created:    2017-03-13 12:46 by Christian Berndt
    Modified:   2017-04-02 23:37 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<portlet:actionURL var="importMeasurementsURL" name="importMeasurements">
    <portlet:param name="mvcPath" value="/html/import_processes.jsp" />
    <portlet:param name="tabs1" value="import-export" />
</portlet:actionURL>

<portlet:renderURL var="browseURL" />

<aui:form action="<%=importMeasurementsURL%>" enctype="multipart/form-data"
    method="post" name="fm" cssClass="import-form">

    <%
        // TODO: Add error handling
    %>
    <%-- 
       <liferay-ui:error exception="<%= FileExtensionException.class %>">
   
       </liferay-ui:error>
       --%>

    <aui:fieldset label="import">
    
        <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />
    
    
        <aui:input name="file" type="file" inlineField="true" label="" />
    
        <aui:button name="import" type="submit" value="import"
            disabled="true" />
        <aui:button href="<%=browseURL%>" type="cancel" />

    </aui:fieldset>

</aui:form>

<div class="separator"></div>

<aui:form action="<%=importMeasurementsURL%>" name="fm1" cssClass="import-form">

    <%
        // TODO: Add error handling
    %>
    <%-- 
       <liferay-ui:error exception="<%= FileExtensionException.class %>">
   
       </liferay-ui:error>
       --%>


        <aui:input name="tabs1" value="<%=tabs1%>" type="hidden" />
    
        <aui:input name="dataURL" type="hidden" value="<%=dataURL%>" />
        <aui:input name="dataURL" disabled="<%=true%>" inlineField="true"
            value="<%=dataURL%>" />
    
        <%
            boolean isConfigured = Validator.isNotNull(dataURL);
        %>
    
        <aui:button name="import" type="submit" value="import"
            disabled="<%=!isConfigured%>" />
        <aui:button href="<%=browseURL%>" type="cancel" />
    
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

