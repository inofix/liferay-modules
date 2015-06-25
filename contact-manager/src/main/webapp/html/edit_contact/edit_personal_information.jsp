<%--
    edit_personal_information.jsp: Edit the contact's personal information. 
    
    Created:    2015-05-11 17:34 by Christian Berndt
    Modified:   2015-05-26 18:50 by Christian Berndt
    Version:    1.0.9
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.contact.dto.UriDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.UrlDTO"%>

<%
	// TODO: make the list of url-types configurable
	String[] urlTypes = new String[] { "home-page", "blog", "video-chat" };
%>

<aui:fieldset label="job">
    <aui:row>
        <aui:col width="50">
            <aui:input name="title" bean="<%=contact_%>" helpMessage="title-help"
                disabled="<%= !hasUpdatePermission %>" />
        </aui:col>
        <aui:col width="50">
            <aui:input name="role" bean="<%=contact_%>" helpMessage="role-help"
                disabled="<%= !hasUpdatePermission %>" />
        </aui:col>
    </aui:row>
    <aui:row>
        <aui:col width="50">
            <aui:input name="organization" value="<%=contact_.getCompany()%>"
                label="company" helpMessage="company-help"
                disabled="<%= !hasUpdatePermission %>" />
        </aui:col>
        <aui:col width="50">
            <aui:input name="organization" value="<%=contact_.getDepartment()%>"
                label="department" helpMessage="department-help"
                disabled="<%= !hasUpdatePermission %>"/>
        </aui:col>
    </aui:row>
    <aui:row>
        <aui:col width="50">
			<aui:input name="organization" value="<%=contact_.getOffice()%>"
				label="office" helpMessage="office-help"
				disabled="<%= !hasUpdatePermission %>"/>
		</aui:col>
    </aui:row>
</aui:fieldset>

<aui:fieldset label="web-addresses" id="url">
	<aui:container>
		<%
			List<UrlDTO> urls = contact_.getUrls();

			for (UrlDTO url : urls) {
		%>
		<aui:row>
			<aui:col span="12">
				<div class="lfr-form-row">
					<div class="row-fields">
					
	                    <div class="sort-handle"></div>
	                    
						<aui:select name="url.type" inlineField="true" label=""
							disabled="<%=!hasUpdatePermission%>">
							<%
								for (String urlType : urlTypes) {
							%>
							<aui:option value="<%=urlType%>" label="<%=urlType%>"
								selected="<%=urlType.equalsIgnoreCase(url
												.getType())%>" />
							<%
								}
							%>
						</aui:select>

						<aui:input name="url.address" inlineField="true" label=""
							cssClass="url-address" value="<%=url.getAddress()%>"
							disabled="<%=!hasUpdatePermission%>" />

						<liferay-ui:icon-help message="url.address-help" />

					</div>
				</div>
			</aui:col>
		</aui:row>
		<%
			}
		%>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="calendar-requests" id="calendar-request">
	<aui:container>
		<%
			List<UriDTO> calendarRequestUris = contact_
							.getCalendarRequestUris();

			for (UriDTO calendarRequestUri : calendarRequestUris) {
		%>
		<aui:row>
			<aui:col span="12">
				<div class="lfr-form-row">
					<div class="row-fields">
					
                        <div class="sort-handle"></div>
                        
						<aui:input name="calendarRequestUri" inlineField="true"
							inlineLabel="true" cssClass="url-address"
							value="<%=calendarRequestUri.getUri()%>"
							helpMessage="calendar-request-uri-help"
							disabled="<%=!hasUpdatePermission%>" />
					</div>
				</div>
			</aui:col>
		</aui:row>
		<%
			}
		%>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="free-busy-url" id="free-busy-url">
	<aui:container>
		<%
			List<UrlDTO> freeBusyUrls = contact_.getFreeBusyUrls();

			for (UrlDTO freeBusyUrl : freeBusyUrls) {
		%>
		<aui:row>
			<aui:col span="12">
				<div class="lfr-form-row">
					<div class="row-fields">
					
                        <div class="sort-handle"></div>
                        
						<aui:input name="freeBusyUrl" inlineField="true"
							inlineLabel="true" cssClass="url-address"
							value="<%=freeBusyUrl.getAddress()%>"
							helpMessage="free-busy-url-help"
							disabled="<%=!hasUpdatePermission%>" />
					</div>
				</div>
			</aui:col>
		</aui:row>

		<%
			}
		%>

	</aui:container>
</aui:fieldset>

<aui:fieldset label="calendars" id="calendar">
	<aui:container>
		<%
			List<UriDTO> calendarUris = contact_
							.getCalendarUris();

			for (UriDTO calendarUri : calendarUris) {
		%>
		<aui:row>
			<aui:col span="12">
				<div class="lfr-form-row">
					<div class="row-fields">
					
                        <div class="sort-handle"></div>
                        
						<aui:input name="calendarUri" inlineField="true"
							inlineLabel="true" cssClass="url-address"
							value="<%=calendarUri.getUri()%>"
							helpMessage="calendar-uri-help"
							disabled="<%=!hasUpdatePermission%>" />

					</div>
				</div>
			</aui:col>
		</aui:row>

		<%
			}
		%>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="miscellaneous">
	<aui:row>
		<aui:col width="50">
			<aui:field-wrapper name="birthday" helpMessage="birthday-help">
				<liferay-ui:input-date name="birthday" dayParam="birthday.day"
					dayValue="<%=contact_.getBirthdayDay()%>"
					monthParam="birthday.month"
					monthValue="<%=contact_.getBirthdayMonth()%>"
					yearParam="birthday.year"
					yearValue="<%=contact_.getBirthdayYear()%>"
					disabled="<%=!hasUpdatePermission%>" />
			</aui:field-wrapper>
			<aui:input name="birthplace" helpMessage="birthplace-help"
				value="<%=contact_.getBirthplace()%>" />
		</aui:col>
		<aui:col width="50">
            <aui:field-wrapper name="anniversary" helpMessage="anniversary-help">		
				<liferay-ui:input-date name="anniversary" dayParam="anniversary.day"
					dayValue="<%= contact_.getAnniversaryDay() %>"
					monthParam="anniversary.month"
					monthValue="<%= contact_.getAnniversaryMonth() %>"
					yearParam="anniversary.year"
					yearValue="<%= contact_.getAnniversaryYear() %>"
					disabled="<%= !hasUpdatePermission %>" />
			</aui:field-wrapper>
		</aui:col>
	</aui:row>
	<c:if test="<%= showDeathdate %>">
	    <aui:row>
	        <aui:col width="50">
	            <aui:field-wrapper name="deathdate" helpMessage="deathdate-help">
	                <liferay-ui:input-date name="deathdate" dayParam="deathdate.day"
	                    dayValue="<%=contact_.getDeathdateDay()%>"
	                    monthParam="deathdate.month"
	                    monthValue="<%=contact_.getDeathdateMonth()%>"
	                    yearParam="deathdate.year"
	                    yearValue="<%=contact_.getDeathdateYear()%>"
	                    disabled="<%=!hasUpdatePermission%>" />
	            </aui:field-wrapper>
	            <aui:input name="deathplace" helpMessage="deathplace-help"
	                value="<%=contact_.getDeathplace()%>" />
	        </aui:col>
	    </aui:row>
    </c:if>
</aui:fieldset>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

	var urlAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />url',
		namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var calendarRequestAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />calendar-request',
		namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var freeBusyUrlAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />free-busy-url',
		namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var calendarAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />calendar',
		namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

</aui:script>
