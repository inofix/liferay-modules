<%--
    view.jsp: Default view of the map-portlet.
    
    Created:    2016-03-02 00:07 by Christian Berndt
    Modified:   2016-09-02 15:25 by Christian Berndt
    Version:    1.2.1
--%>

<%@ include file="/html/init.jsp"%>

<style>
<!--
    .table-wrapper {
        width: 50%; 
        float: left; 
    <c:if test="<%= !showTable %>">  
        display: none; 
    </c:if>           
    }
    
    .map {
        height: <%= mapHeight %>;
    }
    
    #table_info {
        opacity: 0;
    }
-->
</style>

<c:if test="<%= !useGlobalJQuery %>">
    <script type="text/javascript" src="/map-portlet/js/jquery-1.12.3.min.js" ></script>
</c:if>

<%
   String dataTablesTranslationURL = null; 
   String languageId = LanguageUtil.getLanguageId(request);

   if ("de_DE".equals(languageId)) {
       dataTablesTranslationURL = "/map-portlet/js/dataTables/German.json"; 
   }
%>

<div>
modified 3 times
    <div class="table-wrapper">
        <form id="filter">
        
            <fieldset>
                <input id="keyword" class="keyword" type="text" placeholder="<liferay-ui:message key="search"/>">
        <%
           for (int i=0; i<filterColumns.length; i++) {
        %>          
                <input id="filter<%= i %>" type="text" data-value placeholder="<liferay-ui:message key="<%= filterPlaceholders[i] %>"/>">
        <%
           }
        %>
                <aui:button icon="icon-remove" onClick="clearForm();" />
            </fieldset>
            
        </form>
        <table id="table" class="display">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Latitude</th>
                    <th>Longitude</th>
<%
    for (int i=0; i<filterColumns.length; i++) {
        String filter = "filter-" + i; 
%>
                    <c:if test="<%= Validator.isNotNull(filterColumns[i]) %>">
<%--                     <c:if test="<%= Validator.isNotNull(filter1Values) %>"> --%>
                        <th><%= filter %></th>
                    </c:if>                 
<%
    }
%>
                </tr>
            </thead>
        </table>
    </div>
        
    <div class="map-wrapper">
        <c:if test="<%= Validator.isNotNull(claim) %>">
            <div class="jumbotron">
                <h1><%= claim %></h1>
            </div>
        </c:if>    
        <div id="map" class="map"></div>
    </div>      
</div>
    
<script type="text/javascript">

    function clearForm() {
        $( "#keyword" ).val("");
        $( "#keyword" ).trigger( "keyup" );
<%
    for (int i=0; i<filterColumns.length; i++) {
%>           
         $( "#filter<%= i %>").val(""); 
<%
    }
%>
    }
         
</script> 
