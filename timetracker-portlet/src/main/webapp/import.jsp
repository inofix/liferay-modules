<%--
    import.jsp: select the export parameter.

    Created:     2014-10-14 21:51 by Michael Lustenberger
    Modified:    2014-05-23 23:08 by Michael Lustenberger
    Version:     1.0
--%>

<%@ include file="/init.jsp" %>

<%
    // Retrieve the "redirectURL" parameter from the request.
    String redirectURL = ParamUtil.getString(request, "redirectURL");

//    TaskRecordSearch searchContainer = (TaskRecordSearch) request
//                        .getAttribute("liferay-ui:search:searchContainer");

//    TaskRecordDisplayTerms displayTerms = (TaskRecordDisplayTerms)
//                                        searchContainer.getDisplayTerms();

//    displayTerms.setAdvancedSearch(true);
    String vimText = (String)request.getAttribute(TaskRecordFields.VIM_TEXT);
    List<String[]> taskRecords = (List<String[]>)request.getAttribute(
                                                TaskRecordFields.VIM_LIST);
%>

<portlet:actionURL var="parseMicSVimListURL" name="parseVimTaskRecords">
    <portlet:param name="mvcPath" value="/import.jsp"/>
</portlet:actionURL>

<portlet:actionURL var="importMicSVimListURL" name="importVimTaskRecords">
    <portlet:param name="mvcPath" value="/view.jsp"/>
</portlet:actionURL>

<div class="timetracker-portlet">
    <liferay-ui:header title="timetracker-portlet-header"
                       backURL="<%= redirectURL %>"
                       showBackURL="<%= true %>"/>

    <aui:form method="post" action="<%=parseMicSVimListURL%>" name="fm">

        <aui:input name="<%=TaskRecordFields.VIM_TEXT%>"
                    value="<%=vimText%>"
                    type="textarea" />
        <aui:button type="submit" value="parse" />
    </aui:form>

    <aui:form method="post" action="<%=importMicSVimListURL%>" name="fm">
<%
    int i = 0;
    int j = 0;
    String inputBaseName = TaskRecordFields.VIM_TEXT;
    String inputRows = inputBaseName + "_rows";
    String inputCols = inputBaseName + "_cols";

    if ( taskRecords != null ) {
%>
        <table>
<%
        for ( String[] taskRecord : taskRecords ) {
%>
            <tr>
<%
            String inputRowName = inputBaseName + "_" + i + "_";
            j = 0;

            for ( String taskRecordField : taskRecord ) {

                String inputName = inputRowName + j;
                String styleText = "width:0em;";

                if (taskRecordField != null) {
// TODO why is em too big for the font?
//      and why can't we have 100% rel. to table
//      .. but first do it, right?
                    int styleLength = taskRecordField.length() / 2 + 1;
                    styleText = "width:" + styleLength + "em;";
                }
%>
                <td>
                    <aui:input
                        name="<%=inputName%>"
                        label=""
                        value="<%=taskRecordField%>"
                        style="<%=styleText%>" />
                </td>
<%
                j++;
            }
%>
            </tr>
<%
            i++;
        }
%>
        </table>
<%
    }
%>
        <aui:input type="hidden" name="<%=inputRows%>" value="<%=i%>" />
        <aui:input type="hidden" name="<%=inputCols%>" value="<%=j%>" />
        <aui:button type="submit" value="import" />
    </aui:form>

</div>

