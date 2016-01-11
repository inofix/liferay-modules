<%--
    configuration.jsp: Configuration page for the
    timetracker-portlet.

    Created:    2013-10-18 11:58 by Christian Berndt
    Modified:   2014-02-01 16:17 by Christian Berndt
    Version:    1.1
--%>

<%@ include file="/init.jsp" %>

<%
    PortletPreferences preferences = renderRequest.getPreferences();

    String portletResource = ParamUtil.getString(request,
            "portletResource");

    if (Validator.isNotNull(portletResource)) {

        preferences = PortletPreferencesFactoryUtil.getPortletSetup(
                request, portletResource);
    }

    String timeFormat = PrefsParamUtil.getString(preferences,
            renderRequest, TimetrackerPortletKeys.TIME_FORMAT,
            TimeFormat.FROM_UNTIL);
%>

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

    <aui:fieldset>
        <aui:field-wrapper label="time-format" helpMessage="configuration-time-format-help">
            <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                type="radio" value="<%=TimeFormat.DURATION%>"
                checked="<%=Validator.equals(timeFormat,
                                TimeFormat.DURATION)%>"
                label="duration" />

            <aui:input name="<%=TimetrackerPortletKeys.TIME_FORMAT%>"
                type="radio" value="<%=TimeFormat.FROM_UNTIL%>"
                checked="<%=Validator.equals(timeFormat,
                                TimeFormat.FROM_UNTIL)%>"
                label="from-until" />

        </aui:field-wrapper>
    </aui:fieldset>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>
