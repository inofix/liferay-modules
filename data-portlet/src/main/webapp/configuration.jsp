<%--
    configuration.jsp: Configure the data-manager's preferences.
    
    Created:    2017-03-13 16:32 by Christian Berndt
    Modified:   2017-04-03 19:26 by Christian Berndt
    Version:    1.0.6
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
        
        <liferay-ui:panel id="datamanagerDataPanel"
            title="data" extended="true">

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
                    inlineField="true" value="<%=scopeGroupId%>" />

            </aui:fieldset>

        </liferay-ui:panel>        

        <liferay-ui:panel id="datamanagerColumnsPanel"
            title="columns" extended="true">

            <aui:fieldset>

                <%-- 
                <aui:input name="preferences--headerNames--"
                    helpMessage="header-names-help"
                    value="<%=StringUtil.merge(headerNames, StringPool.COMMA) %>" />
                --%>
            
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

                <aui:select name="preferences--interval--"
                    helpMessage="interval-help" inlineField="<%=true%>">

                    <aui:option value="1440" label="24 h"
                        selected='<%="1440".equals(String.valueOf(interval))%>' />
                        
                    <aui:option value="2880" label="48 h"
                        selected='<%="2880".equals(String.valueOf(interval))%>' />

<%--                      <aui:option value="4320" label="72 h" --%>
<%--                         selected='<%="4320".equals(String.valueOf(interval))%>' /> --%>

<%--                     <aui:option value="5760" label="96 h" --%>
<%--                         selected='<%="5760".equals(String.valueOf(interval))%>' /> --%>

<%--                      <aui:option value="7200" label="120 h" --%>
<%--                         selected='<%="7200".equals(String.valueOf(interval))%>' /> --%>

                 </aui:select>

                <aui:input name="preferences--frequency--"
                    helpMessage="frequency-help" inlineField="<%=true%>"
                    value="<%=frequency%>" />

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
