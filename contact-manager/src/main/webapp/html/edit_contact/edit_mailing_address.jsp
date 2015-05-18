<%--
    edit_mailing_address.jsp: Edit the contact's mailing addresses. 
    
    Created:    2015-05-11 18:30 by Christian Berndt
    Modified:   2015-05-11 18:30 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/html/edit_contact/init.jsp"%>

<%-- Import required classes --%>

<%@page import="ch.inofix.portlet.contact.dto.AddressDTO"%>

<%@page import="ezvcard.parameter.AddressType"%>


<%
	// TODO: make the list of address-types configurable
	String[] addressTypes = new String[] { "other",
			AddressType.DOM.getValue(), AddressType.HOME.getValue(),
			AddressType.INTL.getValue(), AddressType.PARCEL.getValue(),
			AddressType.POSTAL.getValue(), AddressType.WORK.getValue() };
%>

<%
	for (AddressDTO address : contact.getAddresses()) {
%>
<aui:fieldset label="<%=address.getType()%>">
	<aui:container>

		<aui:row>
			<aui:col width="50">
				<aui:input name="address.streetAddress" type="textarea"
					cssClass="address" value="<%=address.getStreetAddress()%>" />
				<aui:input name="address.poBox" value="<%=address.getPoBox()%>" />
				<aui:select name="address.type">
					<%
						for (String type : addressTypes) {
					%>
					<aui:option value="<%=type%>" label="<%=type%>"
						selected="<%=type.equalsIgnoreCase(address.getType())%>" />
					<%
						}
					%>
				</aui:select>
			</aui:col>
			<aui:col width="50">
				<aui:input name="address.locality"
					value="<%=address.getLocality()%>" />
				<aui:input name="address.postalCode"
					value="<%=address.getPostalCode()%>" />
				<aui:input name="address.region" value="<%=address.getRegion()%>" />
				<aui:input name="address.country" value="<%=address.getCountry()%>" />
			</aui:col>
		</aui:row>

	</aui:container>
</aui:fieldset>
<%
	}
%>

<aui:fieldset label="new">
	<aui:container>

		<aui:row>
			<aui:col width="50">
				<aui:input name="address.streetAddress" type="textarea"
					cssClass="address" />
				<aui:input name="address.poBox" />
				<aui:select name="address.type">
					<%
						for (String type : addressTypes) {
					%>
					<aui:option value="<%=type%>" label="<%= type %>"/>
					<%
						}
					%>
				</aui:select>
			</aui:col>
			<aui:col width="50">
				<aui:input name="address.locality" />
				<aui:input name="address.postalCode" />
				<aui:input name="address.region" />
				<aui:input name="address.country" />
			</aui:col>
		</aui:row>

	</aui:container>
</aui:fieldset>
