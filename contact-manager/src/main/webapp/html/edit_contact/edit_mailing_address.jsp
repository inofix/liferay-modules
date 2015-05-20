<%--
    edit_mailing_address.jsp: Edit the contact's mailing addresses. 
    
    Created:    2015-05-11 18:30 by Christian Berndt
    Modified:   2015-05-20 18:04 by Christian Berndt
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
	for (AddressDTO address : contact_.getAddresses()) {
%>
<aui:fieldset label="<%=address.getType()%>">
	<aui:container>

		<aui:row>
			<aui:col span="5">
				<aui:input name="address.streetAddress" type="textarea" label="street"
					cssClass="address" value="<%=address.getStreetAddress()%>" />
				<aui:input name="address.poBox" value="<%=address.getPoBox()%>" label="po-box"/>
				<aui:select name="address.type" label="type">
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
			<aui:col span="6">
				<aui:input name="address.locality" label="city"
					value="<%=address.getLocality()%>" />
				<aui:input name="address.postalCode" label="postal-code"
					value="<%=address.getPostalCode()%>" />
				<aui:input name="address.region" label="region"
					value="<%=address.getRegion()%>" />
				<aui:input name="address.country" label="country"
					value="<%=address.getCountry()%>" />
			</aui:col>
			<aui:col span="1">
	            <a class="remove-value pull-right" href="javascript:;">
                    <img src='<%= themeDisplay.getPathThemeImages() + "/common/close.png" %>' title="remove" />
                </a>
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
					cssClass="address" label="street" />
				<aui:input name="address.poBox" label="po-box" />
				<aui:select name="address.type" label="type">
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
				<aui:input name="address.locality" label="city" />
				<aui:input name="address.postalCode" label="postal-code" />
				<aui:input name="address.region" label="region" />
				<aui:input name="address.country" label="country"/>
			</aui:col>
		</aui:row>

	</aui:container>
</aui:fieldset>
