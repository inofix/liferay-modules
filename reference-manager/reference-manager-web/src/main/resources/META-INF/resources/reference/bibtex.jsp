<%--
    reference/bibtex.jsp: the bibtex tab of the reference editor.
    
    Created:    2016-12-25 19:22 by Christian Berndt
    Modified:   2016-12-25 20:28 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%
    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);
    boolean hasUpdatePermission = (Boolean) request.getAttribute("reference.hasUpdatePermission");
%>

<aui:fieldset>
    <aui:input bean="<%=reference%>" helpMessage="bibtex-help"
        disabled="<%=!hasUpdatePermission%>" label="bibtex"
        model="<%=Reference.class%>" name="bibTeX"
        type="textarea" />

</aui:fieldset>  
