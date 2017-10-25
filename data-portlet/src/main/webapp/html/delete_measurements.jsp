<%--
    delete_measurements.jsp: Delete all measurements of this group
    
    Created:    2017-04-05 12:16 by Christian Berndt
    Modified:   2017-10-25 15:50 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<%
    // TODO: add proper permission checks
    boolean hasDeletePermission = themeDisplay.isSignedIn();
%>

<portlet:actionURL name="deleteMeasurementsByChannelName" var="deleteMeasurementsByChannelNameURL">
    <%-- We have to stay on the import-export tab     --%>
    <%-- since while deleting the records             --%>
    <%-- they are still found via the index, which    --%>
    <%-- results in npe-issues in the searchcontainer --%>
    <portlet:param name="tabs1" value="import-export"/>
</portlet:actionURL>

<c:if test="<%= hasDeletePermission %>">
    
        <aui:form action="<%= deleteMeasurementsByChannelNameURL %>">
            <aui:fieldset label="delete-channel">
            
                <liferay-ui:error key="you-must-select-a-channel-name">
                    <liferay-ui:message key="you-must-select-a-channel-name"/>
                </liferay-ui:error>
            
                <aui:select cssClass="pull-left" label="" name="channelName" inlineField="true">
                    <aui:option value="" label="select-channel"/>
                    <c:forEach items="<%=channelNameTermCollectors%>" var="termCollector">
                        <aui:option value="${termCollector.term}"
                            label="${termCollector.term} (${termCollector.frequency})" />
                    </c:forEach>
                </aui:select>
                <aui:button-row>
                    <aui:button cssClass="btn btn-danger delete" value="delete" name="delete" type="submit"/>
                </aui:button-row>
            </aui:fieldset>
        </aui:form>
        
        <aui:fieldset label="delete" cssClass="delete-section">
            <portlet:actionURL name="deleteGroupMeasurements" var="deleteGroupMeasurementsURL"> 
                <%-- We have to stay on the import-export tab     --%>
                <%-- since while deleting the records             --%>
                <%-- they are still found via the index, which    --%>
                <%-- results in npe-issues in the searchcontainer --%>
                <portlet:param name="tabs1" value="import-export"/>
            </portlet:actionURL>
    
            <aui:button-row>
                <liferay-ui:icon-delete
                    url="<%=deleteGroupMeasurementsURL.toString()%>"
                    cssClass="btn btn-danger delete" message="delete-group-measurements"
                    label="true" />
            </aui:button-row>
        </aui:fieldset>
</c:if>
