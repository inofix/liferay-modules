<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2015-05-24 21:24 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%@page import="ch.inofix.portlet.contact.dto.NoteDTO"%>

<aui:fieldset label="notes">

	<%
		List<NoteDTO> notes = contact_.getNotes();

		for (NoteDTO note : notes) {
	%>
    <aui:row>
		<aui:col span="11">
			<aui:input name="note" type="textarea" cssClass="note"
				value="<%=note.getValue()%>" inlineField="true" label=""
				disabled="<%=!hasUpdatePermission%>" />
			<liferay-ui:icon-help message="note-help" />
		</aui:col>
		<aui:col span="1">
            <c:if test="<%= hasUpdatePermission %>">
				<liferay-ui:icon-delete url="javascript:;" cssClass="btn" />
			</c:if>
		</aui:col>
	</aui:row>
	<%
		}
	%>
	<aui:row>
		<aui:col span="11">
			<aui:input name="note" type="textarea" cssClass="note" label="new"
				inlineField="true" helpMessage="note-help"
				disabled="<%= !hasUpdatePermission %>" />
		</aui:col>
		<aui:col span="1">
			<c:if test="<%= hasUpdatePermission %>">
				<liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />
			</c:if>
		</aui:col>
	</aui:row>
</aui:fieldset>