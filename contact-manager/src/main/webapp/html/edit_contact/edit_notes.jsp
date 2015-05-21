<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2015-05-21 12:41 by Christian Berndt
    Version:    1.0.2
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
				value="<%=note.getValue()%>" inlineField="true" label="" />
	    </aui:col>
	    <aui:col span="1">
		    <liferay-ui:icon-delete url="javascript:;" cssClass="btn" />
	    </aui:col>
	</aui:row>
	<%
		}
	%>
    <aui:row>
        <aui:col span="11">
            <aui:input name="note" type="textarea" cssClass="note" label="new" inlineField="true"/>
        </aui:col>
        <aui:col span="1">
		    <liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />
		</aui:col>
    </aui:row>	
</aui:fieldset>