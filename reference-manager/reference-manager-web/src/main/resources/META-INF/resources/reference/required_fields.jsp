<%--
    reference/required_fields.jsp: the required-fields tab of the reference editor.
    
    Created:    2016-12-25 19:24 by Christian Berndt
    Modified:   2016-12-25 19:24 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    Reference reference = (Reference) request.getAttribute("reference");
    JSONObject entryFields = (JSONObject) request.getAttribute("reference.entryFields");
%>
    
<aui:fieldset cssClass="required-fields">
<%
    JSONArray requiredFields = entryFields.getJSONArray("required");  

    for (int i=0; i<requiredFields.length(); i++) {
        JSONObject field = requiredFields.getJSONObject(i);
        String name = field.getString("name");
        String helpKey = field.getString("help");
        if (Validator.isNull(helpKey)) {
            helpKey = name + "-help"; 
        }                    
        String value = reference.getFields().get(name); 
%>
    <aui:input name="name" type="hidden" value="<%= name %>"/>           
    <aui:input helpMessage="<%= helpKey %>" label="<%= name %>" name="value" value="<%= value %>"/>
<%
    }
%>
</aui:fieldset>
