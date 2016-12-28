<%--
    reference/required_fields.jsp: the required-fields tab of the reference editor.
    
    Created:    2016-12-25 19:24 by Christian Berndt
    Modified:   2016-12-28 23:56 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/init.jsp"%>

<%
    Reference reference = (Reference) request.getAttribute("reference");
    JSONObject entryFields = (JSONObject) request.getAttribute("reference.entryFields");
    boolean hasUpdatePermission = (Boolean) request.getAttribute("reference.hasUpdatePermission");
    String label = ""; 
    String namespace = liferayPortletResponse.getNamespace();
    JSONArray requiredFields = entryFields.getJSONArray("required");
    
    if (reference != null) {
        label = reference.getLabel();
    }
%>
    
<aui:fieldset cssClass="required-fields">

    <aui:input disabled="<%=!hasUpdatePermission%>"
        helpMessage="label-help" name="label" value="<%=label%>" />

    <%
    if (requiredFields != null) {
        for (int i = 0; i < requiredFields.length(); i++) {
            JSONObject field = requiredFields.getJSONObject(i);
            String name = field.getString("name");
            String helpKey = field.getString("help");
            if (Validator.isNull(helpKey)) {
                helpKey = name + "-help";
            }
            String value = "";
            if (reference != null) {
                value = reference.getField(name);
            }
%>
    <aui:input name="name" type="hidden" value="<%= name %>"/>
    <aui:field-wrapper name="<%= name %>" helpMessage="<%= helpKey %>"> 
        <input class="field form-control" name="<%= namespace + "value" %>" type="text" value="<%= value %>" />
    </aui:field-wrapper>
    
<%-- Disabled because of an unresolved issue with render after update  
    <aui:input disabled="<%=!hasUpdatePermission%>"
        helpMessage="<%=helpKey%>" label="<%=name%>" name="value"
        value="<%= value %>" />
--%>
<%
        }
    }
%>
</aui:fieldset>
