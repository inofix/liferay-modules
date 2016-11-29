<%--
    manage.jsp: The manage-gui of the reference manager portlet.
    
    Created:    2016-01-20 22:59 by Christian Berndt
    Modified:   2016-11-29 21:13 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    String tabs1 = ParamUtil.getString(request, "tabs1", "manage");

    boolean hasDeletePermission = ReferenceManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ReferenceActionKeys.DELETE_GROUP_REFERENCES);
%>

<portlet:actionURL name="deleteAllReferences"
    var="deleteAllReferencesURL" />

<aui:form action="<%=deleteAllReferencesURL%>" name="fm1">

    <aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

    <aui:button disabled="<%=!hasDeletePermission%>" type="submit"
        value="delete-all-references" />

</aui:form>
