<%--
    delete_measurements.jsp: Delete all measurements of this group
    
    Created:    2017-04-05 12:16 by Christian Berndt
    Modified:   2017-04-11 00:33 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/init.jsp"%>

<%
    // TODO: add proper permission checks
    boolean hasDeletePermission = themeDisplay.isSignedIn();
%>

<portlet:actionURL name="deleteGroupMeasurements" var="deleteGroupMeasurementsURL"> 
    <%-- We have to stay on the import-export tab     --%>
    <%-- since while deleting the records             --%>
    <%-- they are still found via the index, which    --%>
    <%-- results in npe-issues in the searchcontainer --%>
    <portlet:param name="tabs1" value="import-export"/>
</portlet:actionURL>

<c:if test="<%= hasDeletePermission %>">
    <aui:fieldset label="delete" cssClass="delete-section">
        <aui:button-row>
            <liferay-ui:icon-delete
                url="<%=deleteGroupMeasurementsURL.toString()%>"
                cssClass="btn btn-danger delete" message="delete-group-measurements"
                label="true" />
        </aui:button-row>
    </aui:fieldset>
</c:if>