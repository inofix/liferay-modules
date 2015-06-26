<%--
    edit_data.jsp: Edit the contact's data related properties, like
    - PHOTO
    - LOGO
    - SOUND
    - KEY
    - etc.
    
    Created:    2015-06-25 18:50 by Christian Berndt
    Modified:   2015-06-25 18:50 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>
<%@page import="ch.inofix.portlet.contact.dto.FileDTO"%>

<liferay-ui:error key="the-photo-file-format-is-not-supported" 
    message="the-photo-file-format-is-not-supported" />
	   
<aui:fieldset label="photos" id="photos" helpMessage="photo.file-help">
	<aui:container>
		<%
	    List<FileDTO> photos = contact_.getPhotos();
	    for (FileDTO photo : photos) {
	   %>
			<div class="lfr-form-row">
				<div class="row-fields">
					<div class="sort-handle"></div>
				<aui:row>
					<aui:col span="5">
						<aui:input name="photo.file" type="file" inlineField="true"
							label="" disabled="<%=!hasUpdatePermission%>" />
					</aui:col>
					<aui:col span="5">
						<c:if test="<%=Validator.isNotNull(photo.getData())%>">
							<img src="<%=photo.getData()%>" />
						</c:if>
                        <aui:input name="photo.data" type="hidden"
	                        value="<%=photo.getData()%>" />
					</aui:col>
				</aui:row>
			</div>
			</div>
		<%
			}
		%>
	</aui:container>
</aui:fieldset>

<%-- Configure autofields --%>
<aui:script use="liferay-auto-fields">
	var photoAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />photos',
		namespace : '<portlet:namespace />',
		sortable : true,
		sortableHandle : '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();
</aui:script>
