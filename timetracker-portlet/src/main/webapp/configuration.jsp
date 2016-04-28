<%--
    configuration.jsp: Configuration page for the
    timetracker-portlet.

    Created:    2013-10-18 11:58 by Christian Berndt
    Modified:   2016-04-28 18:26 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp" %>

<%-- Compose the renderURL
<liferay-portlet:renderURL portletConfiguration="true"
    var="portletURL" />
--%>

<%-- Compose the configurationURL --%>
<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationURL" />

<aui:form action="<%=configurationURL%>">

    <%-- cmd is required when using --%>
    <%-- DefaultConfigurationAction --%>
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />

    <liferay-ui:panel-container id="timetrackerSettingsPanelContainer" persistState="<%= true %>">
    
        <%-- TODO: make columns configurable 
        <liferay-ui:panel id="timetrackerColumnsPanel" title="columns" extended="true">
  
	    </liferay-ui:panel>
	    --%>
        <liferay-ui:panel id="timetrackerMiscellaneousPanel" title="miscellaneous" extended="true">
            <aui:fieldset>
                <aui:field-wrapper label="time-format" helpMessage="time-format-help">
                    <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                        type="radio" value="<%=TimeFormat.DURATION%>"
                        checked="<%=Validator.equals(timeFormat, TimeFormat.DURATION)%>"
                        label="duration" inlineField="true"/>
        
                    <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                        type="radio" value="<%=TimeFormat.FROM_UNTIL%>"
                        checked="<%=Validator.equals(timeFormat, TimeFormat.FROM_UNTIL)%>"
                        label="from-until" inlineField="true" />
        
                </aui:field-wrapper>
            </aui:fieldset> 
            
            <aui:input name="max-length" value="<%= maxLength %>" helpMessage="max-length-help"/>
              
        </liferay-ui:panel>	    
    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>
