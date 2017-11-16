<%--
    configuration.jsp: Configure the data-manager's preferences.
    
    Created:    2017-03-13 16:32 by Christian Berndt
    Modified:   2017-11-15 16:00 by Christian Berndt
    Version:    1.0.8
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="java.util.Arrays"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserServiceUtil"%>

<%
    PortletURL portletURL = renderResponse.createRenderURL();

    List<KeyValuePair> selectedColumns = new ArrayList<KeyValuePair>();
    for (String column : columns) {
        selectedColumns.add(new KeyValuePair(column, column));
    }

    Arrays.sort(columns); 
    
    List<KeyValuePair> availableColumns = new ArrayList<KeyValuePair>();
    for (String headerName : headerNames) {
        if (Arrays.binarySearch(columns, headerName) < 0) {
            availableColumns.add(new KeyValuePair(headerName,
                    headerName));
        }
    }
    
    List<User> users = UserServiceUtil.getGroupUsers(scopeGroupId);

    String h24 = String.valueOf(1000 * 60 * 60 * 24); 
    String h48 = String.valueOf(1000 * 60 * 60 * 48); 
%>

<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm"
    onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />

    <liferay-ui:panel-container
        id="datamanagerSettingsPanelContainer"
        persistState="<%=true%>">
        
        <liferay-ui:panel id="datamanagerDataSourcesPanel"
            title="data-sources" extended="true">

            <aui:fieldset>

                <aui:input name="preferences--dataURL--"
                    helpMessage="data-url-help" inlineField="<%=true%>"
                    value="<%=dataURL%>" />
                    
                <aui:input name="preferences--userName--"
                    helpMessage="user-name-help" inlineField="<%=true%>"
                    value="<%=userName%>" />
                    
                <aui:input name="preferences--password--"
                    helpMessage="password-help" inlineField="<%=true%>"
                    type="password"
                    value="<%=password%>" />

                <aui:select name="preferences--userId--"
                    helpMessage="user-id-help" inlineField="<%=true%>">
                    <%
                        for (User user1 : users) {
                    %>
                    <aui:option value="<%=user1.getUserId()%>"
                        label="<%=user1.getFullName()%>"
                        selected="<%=user1.getUserId() == userId%>" />
                    <%
                        }
                    %>
                </aui:select>

                <aui:input name="preferences--groupId--" type="hidden"
                    value="<%=scopeGroupId%>" />

                <aui:input name="preferences--groupId--"
                    disabled="<%=true%>" helpMessage="group-id-help"
                    inlineField="<%= true %>" value="<%=scopeGroupId%>" />

            </aui:fieldset>

            <aui:fieldset>

                <aui:input name="preferences--idField--"
                    helpMessage="id-field-help"
                    inlineField="<%=true%>" value="<%=idField%>" />

                <aui:input name="preferences--timestampField--"
                    helpMessage="timestamp-field-help"
                    inlineField="<%=true%>"
                    value="<%=timestampField%>" />

            </aui:fieldset>

        </liferay-ui:panel>
        
        <liferay-ui:panel id="datamanagerChartPanel"
            title="chart" extended="true">

            <aui:fieldset>

                <aui:select name="preferences--interval--"
                    helpMessage="interval-help" inlineField="<%=true%>">

                    <aui:option value="<%= h24 %>" label="24 h"
                        selected='<%=h24.equals(String.valueOf(interval))%>' />
                        
                    <aui:option value="<%= h48 %>" label="48 h"
                        selected='<%=h48.equals(String.valueOf(interval))%>' />

                 </aui:select>
                
                <aui:input name="preferences--limit--"
                    helpMessage="limit-help" inlineField="<%=true%>"
                    value="<%=limit%>" />
                    
            </aui:fieldset>
        </liferay-ui:panel>       

        <liferay-ui:panel id="datamanagerColumnsPanel"
            title="columns" extended="true">

            <aui:fieldset>
            
                <aui:input name="preferences--columns--" type="hidden" />
                
                <liferay-ui:input-move-boxes
                    rightList="<%=availableColumns%>"
                    rightTitle="available" 
                    leftBoxName="selectedColumns"
                    leftList="<%=selectedColumns%>"
                    rightBoxName="availableColumns" 
                    leftTitle="current"
                    leftReorder="true" />

            </aui:fieldset>

        </liferay-ui:panel>
        
        <liferay-ui:panel id="datamanagerMiscellaneousPanel"
            title="miscellaneous" extended="true">

            <aui:fieldset>
                
                <aui:select name="preferences--paginationType--"
                    inlineField="<%=true%>">
                    
                    <aui:option value="approximate" label="approximate"
                        selected='<%="approximate".equals(paginationType)%>' />
                    <aui:option value="article" label="article"
                        selected='<%="article".equals(paginationType)%>' />
                    <aui:option value="more" label="more"
                        selected='<%="more".equals(paginationType)%>' />
                    <aui:option value="regular" label="regular"
                        selected='<%="regular".equals(paginationType)%>' />
                        
                </aui:select>

            </aui:fieldset>

        </liferay-ui:panel>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
</aui:form>

<aui:script>
    Liferay.provide(window, '<portlet:namespace />saveConfiguration',
        function() {
            if (document.<portlet:namespace />fm.<portlet:namespace />columns) {        
                document.<portlet:namespace />fm.<portlet:namespace />columns.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedColumns);
            }
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
