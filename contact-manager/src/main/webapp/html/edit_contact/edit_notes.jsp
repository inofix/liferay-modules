<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2015-05-20 18:12 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%@page import="ch.inofix.portlet.contact.dto.NoteDTO"%>

<aui:fieldset label="notes">

	<%
		List<NoteDTO> notes = contact_.getNotes();

		for (NoteDTO note : notes) {
	%>
	<aui:input name="note" type="textarea" cssClass="note"
		value="<%=note.getValue()%>" inlineField="true" label="" />

	<a class="remove-value" href="javascript:;"> <img
		src='<%=themeDisplay.getPathThemeImages()
							+ "/common/close.png"%>'
		title="remove" />
	</a>
	<%
		}
	%>

	<aui:input name="note" type="textarea" cssClass="note" label="new" />
	
</aui:fieldset>