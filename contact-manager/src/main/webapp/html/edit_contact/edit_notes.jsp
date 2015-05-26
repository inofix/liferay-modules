<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2015-05-26 18:49 by Christian Berndt
    Version:    1.0.7
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.contact.dto.NoteDTO"%>

<aui:fieldset label="notes" id="note">

	<%
		List<NoteDTO> notes = contact_.getNotes();

		for (NoteDTO note : notes) {
	%>
	<aui:row>
		<aui:col span="12">
			<div class="lfr-form-row">
				<div class="row-fields">
				
                    <div class="sort-handle"></div>
				
					<aui:input name="note" type="textarea" cssClass="note"
						value="<%=note.getValue()%>" inlineField="true" label=""
						disabled="<%=!hasUpdatePermission%>" />
					<liferay-ui:icon-help message="note-help" />
				</div>
			</div>
		</aui:col>
	</aui:row>
	<%
		}
	%>
</aui:fieldset>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var noteAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />note',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();

    function restoreOriginalNames(event) {

        // liferay-auto-fields by default adds index numbers
        // to the cloned row's inputs which is here undone.
        var row = event.row;
        var guid = event.guid;

        var inputs = row.all('input, select, textarea');

        inputs.each(function(item) {
            var name = item.attr('name') || item.attr('id');
            var original = name.replace(guid, '');
            item.set('name', original);
            item.set('id', original);
        });

    }
</aui:script>
