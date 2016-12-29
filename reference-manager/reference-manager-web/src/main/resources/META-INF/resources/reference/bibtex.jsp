<%--
    reference/bibtex.jsp: the bibtex tab of the reference editor.
    
    Created:    2016-12-25 19:22 by Christian Berndt
    Modified:   2016-12-29 14:48 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp"%>

<%
    String bibTeX = ""; 
    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);
    boolean hasUpdatePermission = (Boolean) request.getAttribute("reference.hasUpdatePermission");
    String namespace = liferayPortletResponse.getNamespace();

    if (reference != null) {
        bibTeX = reference.getBibTeX(); 
    }
%>

<aui:fieldset>
    <aui:field-wrapper helpMessage="bibtex-help" label="bibtex">
        <textarea class="field form-control"
            <%=hasUpdatePermission ? "" : "disabled=\"disabled\""%>
            name="<%=namespace + "bibTeX"%>"><%=bibTeX%></textarea>
    </aui:field-wrapper>
    
    <%-- Disabled because of an unresolved issue with render after update  
    <aui:input bean="<%=reference%>" helpMessage="bibtex-help"
        disabled="<%=!hasUpdatePermission%>" label="bibtex"
        model="<%=Reference.class%>" name="bibTeX"
        type="textarea" value="<%= bibTeX %>"/>
    --%>
</aui:fieldset>  
