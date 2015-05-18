<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2015-05-11 18:48 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%@page import="ch.inofix.portlet.contact.dto.NoteDTO"%>

<%
	List<NoteDTO> notes = contact.getNotes();

	for (NoteDTO note : notes) {
%>
<aui:input name="note" type="textarea" cssClass="note"
	value="<%=note.getValue()%>" />
<%
	}
%>

<aui:input name="note" type="textarea" cssClass="note" label="new-note" />
