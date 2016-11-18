<%--
    export.jsp: The import-gui of the reference manager portlet.
    
    Created:    2016-01-10 23:33 by Christian Berndt
    Modified:   2016-11-18 18:39 by Christian Berndt
    Version:    0.1.0
--%>

<%@ include file="/init.jsp" %>

<%
    // TODO: Check upload permission
    String tabs1 = ParamUtil.getString(request, "tabs1", "import");
%>

<portlet:actionURL name="importBibTeXFile" var="importBibTeXFileURL" />

<aui:form action="<%= importBibTeXFileURL %>" enctype="multipart/form-data" method="post" name="fm1">
    
    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="importBibTeXFile" />
    
    <aui:fieldset label="import">
        <aui:input cssClass="file-input" label="" name="file" type="file" />
        <aui:button type="submit" value="import" />
    </aui:fieldset>
</aui:form>