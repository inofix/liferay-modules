<%--
    reference/configuration.jsp: configuration of the reference manager portlet.
    
    Created:    2017-02-13 21:58 by Christian Berndt
    Modified:   2017-02-13 21:58 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    String[] columns = new String[0];

    if (Validator.isNotNull(referenceManagerConfiguration)) {
        columns = portletPreferences.getValues("columns", referenceManagerConfiguration.columns());
    }

    String allColumns = SearchColumns.REFERENCE_SEARCH_COLUMNS;
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>"
    var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm"
    onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>

    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />

    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />
        
        <aui:input name="columns" type="hidden"/>

        <aui:fieldset collapsible="<%=true%>" label="show-columns">
            <%
                Set<String> availableColumns = SetUtil.fromArray(StringUtil.split(allColumns));

                // Left list

                List leftList = new ArrayList();

                for (String column : columns) {
                    leftList.add(new KeyValuePair(column, LanguageUtil.get(request, column)));
                }

                // Right list

                List rightList = new ArrayList();

                Arrays.sort(columns);

                for (String column : availableColumns) {
                    if (Arrays.binarySearch(columns, column) < 0) {
                        rightList.add(new KeyValuePair(column, LanguageUtil.get(request, column)));
                    }
                }

                rightList = ListUtil.sort(rightList, new KeyValuePairComparator(false, true));
            %>

            <liferay-ui:input-move-boxes
                leftBoxName="currentColumns"
                leftList="<%=leftList%>"
                leftReorder="<%=Boolean.TRUE.toString()%>"
                leftTitle="current"
                rightBoxName="availableColumns"
                rightList="<%=rightList%>" rightTitle="available" />
        </aui:fieldset>
                
    <aui:button-row>
        <aui:button type="submit"></aui:button>
    </aui:button-row>
</aui:form>

<aui:script>
    function <portlet:namespace />saveConfiguration() {
        var Util = Liferay.Util;

        var form = AUI.$(document.<portlet:namespace />fm);

        form.fm('columns').val(Util.listSelect(form.fm('currentColumns')));

        submitForm(form);
    }
</aui:script>
