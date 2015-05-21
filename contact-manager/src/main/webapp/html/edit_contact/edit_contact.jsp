<%--
    edit_contact.jsp: Edit the contact's basic contact information. 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2015-05-21 12:39 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.contact.dto.EmailDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.ImppDTO"%>
<%@page import="ch.inofix.portlet.contact.dto.PhoneDTO"%>

<%@page import="ezvcard.parameter.ImppType"%>
<%@page import="ezvcard.parameter.TelephoneType"%>
<%@page import="ezvcard.property.Kind"%>

<%
	// TODO: Make the emailTypes configurable
	String[] emailTypes = new String[] { "home", "work", "other" };

	String[] imppTypes = new String[] { "other",
			ImppType.BUSINESS.getValue(), ImppType.HOME.getValue(),
			ImppType.MOBILE.getValue(), ImppType.PERSONAL.getValue(),
			ImppType.WORK.getValue() };

	// TODO: Make imppProtocols configurable
	String[] imppProtocols = new String[] { "other", "aim", "jabber",
			"yahoo", "gadu-gadu", "msn", "icq", "groupwise", "skype",
			"twitter" };

	String[] kinds = new String[] { Kind.APPLICATION, Kind.DEVICE,
			Kind.GROUP, Kind.INDIVIDUAL, Kind.LOCATION, Kind.ORG };

	// TODO: Make the phoneTypes configurable
	String[] phoneTypes = new String[] { "other",
			TelephoneType.BBS.getValue(), TelephoneType.CAR.getValue(),
			TelephoneType.CELL.getValue(),
			TelephoneType.FAX.getValue(),
			TelephoneType.HOME.getValue(),
			TelephoneType.ISDN.getValue(),
			TelephoneType.MODEM.getValue(),
			TelephoneType.MSG.getValue(),
			TelephoneType.PAGER.getValue(),
			TelephoneType.PCS.getValue(),
			TelephoneType.TEXT.getValue(),
			TelephoneType.TEXTPHONE.getValue(),
			TelephoneType.VIDEO.getValue(),
			TelephoneType.VOICE.getValue(),
			TelephoneType.WORK.getValue() };

	String[] snFields = new String[] { "structuredName.prefix",
			"structuredName.given", "structuredName.additional",
			"structuredName.family", "structuredName.suffix" };
%>

<%-- The values of the hidden snFields are managed --%>
<%-- by the structured-name popover below.         --%>
<%
	for (String snField : snFields) {
%>
<aui:input name="<%=snField%>" bean="<%=contact_%>"
	cssClass='<%=snField.replace(".", "-") + "-twin"%>' type="hidden" />
<%
	}
%>

<aui:fieldset label="name">
	<aui:container>
		<aui:row>
			<aui:col width="50">
				<aui:input name="formattedName" bean="<%=contact_%>"
					inlineField="true" />
				<aui:button name="structuredNameBtn" value="structured-name"
					cssClass="btn" />
			</aui:col>
			<aui:col width="50">
				<aui:input name="nickname" bean="<%=contact_%>" />
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="email">
	<aui:container>
		<%
			List<EmailDTO> emails = contact_.getEmails();

			for (EmailDTO email : emails) {
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="email.type" label="" inlineField="true">
					<%
						for (String emailType : emailTypes) {
					%>
					<aui:option value="<%=emailType%>" label="<%=emailType%>"
						selected="<%=emailType.equalsIgnoreCase(email.getType())%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="email.address" inlineField="true"
					value="<%=email.getAddress()%>" label="" />
					
                <liferay-ui:icon-delete url="javascript:;" cssClass="btn" />

			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="email.type" label="" inlineField="true">
					<%
						for (String emailType : emailTypes) {
					%>
					<aui:option value="<%=emailType%>" label="<%=emailType%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="email.address" inlineField="true" label="" />
				
				<liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />
				
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="phone">
	<aui:container>
		<%
			List<PhoneDTO> phones = contact_.getPhones(); 
				        
            for (PhoneDTO phone : phones) {
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="phone.type" label="" inlineField="true">
					<%
						for (String phoneType : phoneTypes) {
					%>
					<aui:option value="<%=phoneType%>" label="<%=phoneType%>"
						selected="<%=phoneType.equalsIgnoreCase(phone.getType())%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="phone.number" inlineField="true"
					value="<%=phone.getNumber()%>" label="" />
					
                <liferay-ui:icon-delete url="javascript:;" cssClass="btn" />
                
			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="phoneType" label="" inlineField="true">
					<%
						for (String phoneType : phoneTypes) {
					%>
					<aui:option value="<%=phoneType%>" label="<%=phoneType%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="phoneNumber" inlineField="true" label="" />
				<liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />				
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="instant-messaging">
	<aui:container>
		<%
			List<ImppDTO> impps = contact_.getImpps();
				                        
            for (ImppDTO impp : impps) {
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="impp.type" label="" inlineField="true">
					<%
						for (String imppType : imppTypes) {
					%>
					<aui:option value="<%=imppType%>" label="<%=imppType%>"
						selected="<%=imppType.equalsIgnoreCase(impp.getType())%>" />
					<%
						}
					%>
				</aui:select>
				<aui:select name="impp.protocol" label="" inlineField="true">
					<%
						for (String imppProtocol : imppProtocols) {
					%>
					<aui:option value="<%=imppProtocol%>" label="<%=imppProtocol%>"
						selected="<%=imppProtocol.equalsIgnoreCase(impp.getProtocol())%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="impp.uri" inlineField="true"
					value="<%=impp.getUri()%>" label="" />
					
                <liferay-ui:icon-delete url="javascript:;" cssClass="btn" />

			</aui:col>
		</aui:row>
		<%
			}
		%>
		<aui:row>
			<aui:col span="12">
				<aui:select name="impp.type" label="" inlineField="true">
					<%
						for (String imppType : imppTypes) {
					%>
					<aui:option value="<%=imppType%>" label="<%=imppType%>" />
					<%
						}
					%>
				</aui:select>
				<aui:select name="impp.protocol" label="" inlineField="true">
					<%
						for (String imppProtocol : imppProtocols) {
					%>
					<aui:option value="<%=imppProtocol%>" label="<%=imppProtocol%>" />
					<%
						}
					%>
				</aui:select>
				<aui:input name="impp.uri" inlineField="true" label="" />
                <liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />              
			</aui:col>
		</aui:row>
	</aui:container>
</aui:fieldset>

<aui:fieldset label="kind">
    <aui:select name="kind" title="kind-help" label="" >
        <%
            for (String kind : kinds) {
        %>
        <aui:option value="<%=kind%>" label="<%=kind%>"
            selected="<%=kind.equalsIgnoreCase(contact_.getKind())%>" />
        <%
            }
        %>
    </aui:select>
</aui:fieldset>

<div id='<portlet:namespace/>structuredNamePopover'
	class="structured-name">

	<%
		for (String snField : snFields) {
	%>
	<aui:input name="<%=snField%>" bean="<%=contact_%>"
		cssClass='<%=snField.replace(".", "-")%>' useNamespace="false" />
	<%
		}
	%>

</div>

<script>
	YUI().use('aui-popover', 'widget-anim', function(A) {
		var triggerAnim = A.one('#<portlet:namespace/>structuredNameBtn');

		var popoverAnim = new A.Popover({
			align : {
				node : triggerAnim,
				points : [ A.WidgetPositionAlign.RC, A.WidgetPositionAlign.LC ]
			},
			headerContent : '<liferay-ui:message key="structured-name"/>',
			plugins : [ A.Plugin.WidgetAnim ],
			position : 'left',
			visible : false,
			zIndex : Liferay.zIndex.TOOLTIP

		}).render();

		var sn = A.one('#<portlet:namespace/>structuredNamePopover');

		popoverAnim.set("bodyContent", sn);

		triggerAnim.on('click', function() {
			popoverAnim.set('visible', !popoverAnim.get('visible'));
		});
	});
</script>

<%-- Synchronize structured-name inputs with --%>
<%-- the hidden form fields above.           --%>
<script>
	YUI().use('event',function(Y) {
		var inputs = Y.all('#<portlet:namespace/>structuredNamePopover input');
		inputs.each(function(input) {
			input.on('change', function(e) {
				var selector = 'input.'
						+ input.get('name').replace('.', '-')
						+ '-twin';
				var twin = Y.one(selector);
				twin.set('value', input.get('value'));
			});
		});
	});
</script>
