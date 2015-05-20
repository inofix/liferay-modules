<%--
    edit_miscellaneous.jsp: Edit the miscellaneous contact information. 
    
    Created:    2015-05-16 20:06 Christian Berndt
    Modified:   2015-05-20 18:11 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>
<%@page import="java.util.TimeZone"%>

<%@page import="ch.inofix.portlet.contact.dto.ExpertiseDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.HobbyDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.InterestDTO"%>

<%@page import="ezvcard.parameter.ExpertiseLevel"%>
<%@page import="ezvcard.parameter.HobbyLevel"%>
<%@page import="ezvcard.parameter.InterestLevel"%>

<%
	String[] expertiseLevels = new String[] {
			ExpertiseLevel.BEGINNER.getValue(),
			ExpertiseLevel.AVERAGE.getValue(),
			ExpertiseLevel.EXPERT.getValue() };

	String[] hobbyLevels = new String[] { HobbyLevel.HIGH.getValue(),
			HobbyLevel.MEDIUM.getValue(), HobbyLevel.LOW.getValue(), };

	String[] interestLevels = new String[] {
			InterestLevel.HIGH.getValue(),
			InterestLevel.MEDIUM.getValue(),
			InterestLevel.LOW.getValue(), };
%>

<aui:fieldset label="expertise">
	<aui:container>
		<%
			List<ExpertiseDTO> expertises = contact_.getExpertises();

					for (ExpertiseDTO expertise : expertises) {
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="expertise" inlineField="true" label=""
					value="<%=expertise.getValue()%>" />
				<aui:select name="expertise.level" inlineField="true" label="">
					<%
						for (String expertiseLevel : expertiseLevels) {
					%>
					<aui:option value="<%=expertiseLevel%>" label="<%=expertiseLevel%>"
						selected="<%=expertiseLevel
												.equalsIgnoreCase(expertise
														.getLevel())%>" />
					<%
						}
					%>
				</aui:select>
				<a class="remove-value" href="javascript:;">
                    <img src='<%= themeDisplay.getPathThemeImages() + "/common/close.png" %>' title="remove" />
                </a>
			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="expertise" inlineField="true" label="" />
				<aui:select name="expertise.level" inlineField="true" label="">
					<%
						for (String expertiseLevel : expertiseLevels) {
					%>
					<aui:option value="<%=expertiseLevel%>" label="<%=expertiseLevel%>" />
					<%
						}
					%>
				</aui:select>				
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="hobbies">
	<aui:container>
		<%
			List<HobbyDTO> hobbies = contact_.getHobbies();

					for (HobbyDTO hobby : hobbies) {
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="hobby" inlineField="true" label=""
					value="<%=hobby.getValue()%>" />
				<aui:select name="hobby.level" inlineField="true" label="">
					<%
						for (String hobbyLevel : hobbyLevels) {
					%>
					<aui:option value="<%=hobbyLevel%>" label="<%=hobbyLevel%>"
						selected="<%=hobbyLevel
												.equalsIgnoreCase(hobby
														.getLevel())%>" />
					<%
						}
					%>
				</aui:select>
                <a class="remove-value" href="javascript:;">
                    <img src='<%= themeDisplay.getPathThemeImages() + "/common/close.png" %>' title="remove" />
                </a>
			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="hobby" inlineField="true" label="" />
				<aui:select name="hobby.level" inlineField="true" label="">
					<%
						for (String hobbyLevel : hobbyLevels) {
					%>
					<aui:option value="<%=hobbyLevel%>" label="<%=hobbyLevel%>" />
					<%
						}
					%>
				</aui:select>
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>


<aui:fieldset label="interests">
	<aui:container>
		<%
			List<InterestDTO> interests = contact_.getInterests();

					for (InterestDTO interest : interests) {
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="interest" inlineField="true" label=""
					value="<%=interest.getValue()%>" />
				<aui:select name="interest.level" inlineField="true" label="">
					<%
						for (String interestLevel : interestLevels) {
					%>
					<aui:option value="<%=interestLevel%>" label="<%=interestLevel%>"
						selected="<%=interestLevel
												.equalsIgnoreCase(interest
														.getLevel())%>" />
					<%
						}
					%>
				</aui:select>
                <a class="remove-value" href="javascript:;">
                    <img src='<%= themeDisplay.getPathThemeImages() + "/common/close.png" %>' title="remove" />
                </a>
			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col width="100">
				<aui:input name="interest" inlineField="true" label="" />
				<aui:select name="interest.level" inlineField="true" label="">
					<%
						for (String interestLevel : interestLevels) {
					%>
					<aui:option value="<%=interestLevel%>" label="<%=interestLevel%>" />
					<%
						}
					%>
				</aui:select>
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="time-zone">
	<aui:container>
		<aui:row>
			<aui:col width="100">
				<aui:select name="timezone" label="">
					<%
						String[] timezones = TimeZone.getAvailableIDs();
						for (String timezone : timezones) {
					%>
					<aui:option value="<%=timezone%>" label="<%=timezone%>"
						selected="<%=timezone.equalsIgnoreCase(contact_.getTimezone())%>" />
					<%
						}
					%>
				</aui:select>
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="related-assets">
	<liferay-ui:input-asset-links
		className="<%= Contact.class.getName() %>"
		classPK="<%= contact_.getContactId() %>" />
</aui:fieldset>

<aui:model-context model="<%= Contact.class %>"/>

<aui:fieldset label="categorization">
	<aui:input classPK="<%=contact_.getContactId()%>" name="categories"
		type="assetCategories" />
	<aui:input classPK="<%=contact_.getContactId()%>" name="tags"
		type="assetTags" />
</aui:fieldset>
