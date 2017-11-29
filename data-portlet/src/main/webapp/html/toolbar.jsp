<%--
    toolbar.jsp: The toolbar of the data portlet
    
    Created:    2017-03-23 15:18 by Christian Berndt
    Modified:   2017-11-29 20:59 by Christian Berndt
    Version:    1.1.0 
 --%>

<%@ include file="/html/init.jsp"%>

<liferay-portlet:renderURL varImpl="searchURL" />

<aui:form action="<%= searchURL %>" name="fm1">

    <aui:nav-bar cssClass="toolbar">
    
        <aui:nav cssClass="pull-left">
       
            <aui:input name="tabs1" type="hidden" value="<%= tabs1 %>"/>
            
            <aui:select cssClass="pull-left" label="" name="id" inlineField="true" onChange='<%= renderResponse.getNamespace() + "select();" %>'>
                <aui:option value="" label="select-channel"/>
                <c:forEach items="<%=channelIdTermCollectors%>" var="termCollector">
                    <aui:option value="${termCollector.term}"
                        label="${termCollector.term} (${termCollector.frequency})" />
                </c:forEach>
            </aui:select>
            
        </aui:nav>
        
            
        <aui:button-row cssClass="prev-next">
        
            <liferay-ui:input-date 
                dayParam="fromDay"
                dayValue="<%=fromDay%>"
                monthParam="fromMonth"
                monthValue="<%=fromMonth%>"
                name="fromDate"
                yearParam="fromYear"
                yearValue="<%=fromYear%>" />
                
            <liferay-ui:input-date               
                dayParam="untilDay"
                dayValue="<%=untilDay%>"   
                monthParam="untilMonth"
                monthValue="<%=untilMonth%>"
                name="untilDate"                   
                yearParam="untilYear"
                yearValue="<%=untilYear%>" />
                
        </aui:button-row> 
        
        <aui:input name="from" type="hidden" value="<%= from %>"/>
        <aui:input name="until" type="hidden" value="<%= until %>"/>                    

        <aui:nav cssClass="pull-right" collapsible="<%= false %>">  
         
            <portlet:renderURL var="clearURL" />
            
            <aui:button-row >
                <aui:button type="submit" value="search"/>
                <aui:button value="reset" href="<%=clearURL%>" cssClass="clear-btn" />
            </aui:button-row>
            
        </aui:nav>
        
    </aui:nav-bar>

</aui:form>


<aui:script>
    function <portlet:namespace />select() {
        submitForm(document.<portlet:namespace />fm1);
    }
</aui:script>

<%-- 
<aui:script use="aui-base">

    // Because hidden fields don't fire a change event, we have to 
    // observe it's value manually.
    
    var fromDayInput = A.one('#<portlet:namespace />fromDay');
    var fromDay = fromDayInput.get('value');
    
    var fromDatePicker = A.one('#<portlet:namespace />fromDate');
    
    fromDatePicker.after('click', function(e) {
    	        
        var datepickerPopover = A.one('.datepicker-popover');
        
        datepickerPopover.after('click', function(e) {
            
            if (fromDay != fromDayInput.get('value')) {
                submitForm(document.<portlet:namespace />fm1);                
            }
            
        }); 
    });
    
    var untilDayInput = A.one('#<portlet:namespace />untilDay');
    var untilDay = untilDayInput.get('value');
    
    var untilDatePicker = A.one('#<portlet:namespace />untilDate');
    
    untilDatePicker.after('click', function(e) {
                
        var datepickerPopover = A.one('.datepicker-popover');
        
        datepickerPopover.after('click', function(e) {
            
            if (untilDay != untilDayInput.get('value')) {
                submitForm(document.<portlet:namespace />fm1);                
            }
            
        }); 
    });
    
</aui:script>
--%>