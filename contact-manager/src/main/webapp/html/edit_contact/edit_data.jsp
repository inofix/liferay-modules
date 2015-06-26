<%--
    edit_data.jsp: Edit the contact's data related properties, like
    - SOUND
    - KEY
    - etc.
    
    Created:    2015-06-25 18:50 by Christian Berndt
    Modified:   2015-06-26 17:40 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>
<%@page import="ch.inofix.portlet.contact.dto.FileDTO"%>

<liferay-ui:error key="the-image-file-format-is-not-supported" 
    message="the-image-file-format-is-not-supported" />
	   
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

<liferay-ui:error key="the-sound-file-format-is-not-supported" 
    message="the-sound-file-format-is-not-supported" />

<aui:fieldset label="logos" id="logos" helpMessage="logo.file-help">
    <aui:container>
        <%
        List<FileDTO> logos = contact_.getLogos(); 
        for (FileDTO logo : logos) {
       %>
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>
                <aui:row>
                    <aui:col span="5">
                        <aui:input name="logo.file" type="file" inlineField="true"
                            label="" disabled="<%=!hasUpdatePermission%>" />
                    </aui:col>
                    <aui:col span="5">
                        <c:if test="<%=Validator.isNotNull(logo.getData())%>">
                            <img src="<%=logo.getData()%>" />
                        </c:if>
                        <aui:input name="logo.data" type="hidden"
                            value="<%=logo.getData()%>" />
                    </aui:col>
                </aui:row>
            </div>
            </div>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="sounds" id="sounds" helpMessage="sound.file-help">
    <aui:container>
        <%
        List<FileDTO> sounds = contact_.getSounds();
        for (FileDTO sound : sounds) {
       %>
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>
                <aui:row>
                    <aui:col span="5">
                        <aui:input name="sound.file" type="file" inlineField="true"
                            label="" disabled="<%=!hasUpdatePermission%>" />
                    </aui:col>
                    <aui:col span="5">
                        <c:if test="<%=Validator.isNotNull(sound.getData())%>">
                            <a href="<%= sound.getData() %>" target="_blank">Play</a>                            
                        </c:if>
                        <aui:input name="sound.data" type="hidden"
                            value="<%=sound.getData()%>" />
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
	
    var logoAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />logos',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle : '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
    
    var soundAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />sounds',
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
