<%--
    reference/optional_fields.jsp: the optional-fields tab of the reference editor.
    
    Created:    2016-12-25 19:24 by Christian Berndt
    Modified:   2016-12-28 23:56 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/init.jsp"%>

<%
    Reference reference = (Reference) request.getAttribute("reference");
    JSONObject entryFields = (JSONObject) request.getAttribute("reference.entryFields");
    boolean hasUpdatePermission = (Boolean) request.getAttribute("reference.hasUpdatePermission");
    String namespace = liferayPortletResponse.getNamespace();
    JSONArray optionalFields = entryFields.getJSONArray("optional");  
%>
    
<aui:fieldset cssClass="optional-fields">
<%
    for (int i=0; i<optionalFields.length(); i++) {
        JSONObject field = optionalFields.getJSONObject(i);
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
%>
</aui:fieldset>
