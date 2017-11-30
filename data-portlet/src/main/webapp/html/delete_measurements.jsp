<%--
    delete_measurements.jsp: Delete all measurements of this group
    
    Created:    2017-04-05 12:16 by Christian Berndt
    Modified:   2017-11-30 23:14 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/init.jsp"%>

<%
    // TODO: add proper permission checks
    boolean hasDeletePermission = themeDisplay.isSignedIn();

    String namespace = liferayPortletResponse.getNamespace();
%>

<portlet:actionURL name="deleteMeasurementsByChannelName" var="deleteMeasurementsByChannelNameURL">
    <%-- We have to stay on the import-export tab     --%>
    <%-- since while deleting the records             --%>
    <%-- they are still found via the index, which    --%>
    <%-- results in npe-issues in the searchcontainer --%>
    <portlet:param name="tabs1" value="import-export"/>
</portlet:actionURL>

<c:if test="<%= hasDeletePermission %>">
    
    <aui:form action="<%= deleteMeasurementsByChannelNameURL %>" name="fm1" onSubmit='<%= "event.preventDefault();" + namespace + "deleteMeasurements();" %>'>
        <aui:fieldset label="delete-channel">
        
            <liferay-ui:error key="you-must-select-a-channel-name">
                <liferay-ui:message key="you-must-select-a-channel-name"/>
            </liferay-ui:error>
        
            <aui:select cssClass="pull-left" label="" name="id" inlineField="true">
                <aui:option value="" label="select-channel"/>
            <%            
                for (TermCollector termCollector : idTermCollectors) {
                    
                    Sort sort = new Sort(DataManagerFields.TIMESTAMP, true); 
                    
                    Hits hits = MeasurementLocalServiceUtil.search(
                            themeDisplay.getCompanyId(),
                            themeDisplay.getScopeGroupId(),
                            termCollector.getTerm(), 0,
                            new Date().getTime(), true, 0, 1, sort);
    
                    if (hits.getLength() > 0) {
                        
                        Document document = hits.toList().get(0);
        
                        id = termCollector.getTerm();
                        String label = id; 
                        
                        boolean hasNumericId = GetterUtil.getInteger(id) > 0; 
                        
                        StringBuilder sb = new StringBuilder(); 
                        
                        if (hasNumericId) {
                            
                            sb.append(id); 
                            sb.append(StringPool.SPACE);
                            sb.append(StringPool.OPEN_PARENTHESIS);
                            sb.append(document.get(DataManagerFields.NAME));
                            sb.append(StringPool.CLOSE_PARENTHESIS);
                                                        
                        } else {
                            sb.append(id);
                        }
                        
                        sb.append(StringPool.SPACE);
                        sb.append(StringPool.OPEN_BRACKET);
                        sb.append(termCollector.getFrequency());
                        sb.append(StringPool.CLOSE_BRACKET);
                        
                        label = sb.toString();

            %>
                    <aui:option value="<%= id %>" label="<%= label %>" />
            <%  
                    }
                }
            %>
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

<aui:script>
    Liferay.provide(
        window,
        '<portlet:namespace />deleteMeasurements',
        function() {
            if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-you-want-to-delete-this") %>')) {
                submitForm(document.<portlet:namespace />fm1);
            }
        }
    );
</aui:script>
