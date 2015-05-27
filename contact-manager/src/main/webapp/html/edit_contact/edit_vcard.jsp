<%-- 
    edit_contact/edit_vcard.jsp: Edit the vCard String of the contact.
    
    Created:    2015-05-08 15:42 by Christian Berndt
    Modified:   2015-05-27 19:01 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/html/edit_contact/init.jsp"%>


<aui:fieldset label="v-card">

	<aui:row>
		<aui:col span="10">
			<aui:input name="vCard" type="textarea" cssClass="v-card"
				value="<%=contact_.getCard()%>" disabled="true" label=""
				inlineField="true" />
			<liferay-ui:icon-help message="v-card-help" />
		</aui:col>
		<aui:col span="2">

			<portlet:resourceURL var="serveVCardURL" id="serveVCard">
			</portlet:resourceURL>

			<aui:button href="<%=serveVCardURL%>" value="download" />

		</aui:col>
	</aui:row>
</aui:fieldset>
