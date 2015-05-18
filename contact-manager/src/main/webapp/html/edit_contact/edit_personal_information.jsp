<%--
    edit_personal_information.jsp: Edit the contact's personal information. 
    
    Created:    2015-05-11 17:34 by Christian Berndt
    Modified:   2015-05-11 17:34 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.contact.dto.UriDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.UrlDTO"%>

<%
	// TODO: make the list of url-types configurable
	String[] urlTypes = new String[] { "home-page", "blog", "video-chat" };
%>

<aui:fieldset label="web-addresses">
	<aui:container>
		<%
			List<UrlDTO> urls = contact.getUrls();

			for (UrlDTO url : urls) {
		%>
		<aui:row>
			<aui:col width="100">
				<aui:select name="url.type" inlineField="true" label="">
					<%
						for (String urlType : urlTypes) {
					%>
					<aui:option value="<%=urlType%>" label="<%=urlType%>"
						selected="<%=urlType.equalsIgnoreCase(url.getType())%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="url.address" inlineField="true" label=""
					cssClass="url-address" value="<%=url.getAddress()%>" />
			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col width="100">
                <aui:select name="url.type" inlineField="true" label="">
                    <%
                        for (String urlType : urlTypes) {
                    %>
                    <aui:option value="<%=urlType%>" label="<%=urlType%>" />
                    <%
                        }
                    %>
                </aui:select>
				<aui:input name="url.address" inlineField="true" label=""
					cssClass="url-address" />
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="calendar-requests">
    <aui:container>
        <%
            List<UriDTO> calendarRequestUris = contact.getCalendarRequestUris();

            for (UriDTO calendarRequestUri : calendarRequestUris) {
        %>
        <aui:row>
            <aui:col width="100">
                <aui:input name="calendarRequestUri" inlineField="true" inlineLabel="true"
                    cssClass="url-address" value="<%= calendarRequestUri.getUri() %>" />
            </aui:col>
        </aui:row>
        <%
            }
        %>
        <aui:row>
            <aui:col width="100">
                <aui:input name="calendarRequestUri" inlineField="true" inlineLabel="true"
                    cssClass="url-address" />
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="calendars">
    <aui:container>
        <%
            List<UriDTO> calendarUris = contact.getCalendarRequestUris();

            for (UriDTO calendarUri : calendarUris) {
        %>
        <aui:row>
            <aui:col width="100">
                <aui:input name="calendarUri" inlineField="true" inlineLabel="true"
                    cssClass="url-address" value="<%= calendarUri.getUri() %>" />
            </aui:col>
        </aui:row>
        <%
            }
        %>
        <aui:row>
            <aui:col width="100">
                <aui:input name="calendarUri" inlineField="true" inlineLabel="true"
                    cssClass="url-address" />
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="job">
	<aui:row>
        <aui:col width="50">
            <aui:input name="title" bean="<%=contact%>" />
        </aui:col>
		<aui:col width="50">
			<aui:input name="role" bean="<%=contact%>" />
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input name="organization" value="<%=contact.getCompany()%>"
				label="company" />
		</aui:col>
		<aui:col width="50">
			<aui:input name="organization" value="<%=contact.getDepartment()%>"
				label="department" />
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input name="organization" value="<%=contact.getOffice()%>"
				label="office" />
		</aui:col>
	</aui:row>
</aui:fieldset>

<aui:fieldset label="miscellaneous">
	<aui:row>
		<aui:col width="50">
			<label><liferay-ui:message key="birthday" /></label>
			<liferay-ui:input-date name="birthday" dayParam="birthday.day"
				dayValue="<%= contact.getBirthdayDay() %>"
				monthParam="birthday.month"
				monthValue="<%= contact.getBirthdayMonth() %>"
				yearParam="birthday.year"
				yearValue="<%= contact.getBirthdayYear() %>" />
		</aui:col>
		<aui:col width="50">
			<label><liferay-ui:message key="anniversary" /></label>
			<liferay-ui:input-date name="anniversary" dayParam="anniversary.day"
				dayValue="<%= contact.getAnniversaryDay() %>"
				monthParam="anniversary.month"
				monthValue="<%= contact.getAnniversaryMonth() %>"
				yearParam="anniversary.year"
				yearValue="<%= contact.getAnniversaryYear() %>" />
		</aui:col>
	</aui:row>
</aui:fieldset>
