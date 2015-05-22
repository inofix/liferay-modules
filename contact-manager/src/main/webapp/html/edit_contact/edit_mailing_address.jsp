<%--
    edit_mailing_address.jsp: Edit the contact's mailing addresses. 
    
    Created:    2015-05-11 18:30 by Christian Berndt
    Modified:   2015-05-22 15:38 by Christian Berndt
    Version:    1.0.2
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
					cssClass="address" value="<%=address.getStreetAddress()%>" helpMessage="address.street-address-help" />
				<aui:input name="address.poBox" value="<%=address.getPoBox()%>" label="po-box" helpMessage="address.po-box-help"/>
				<aui:select name="address.type" label="type" helpMessage="address.type-help">
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
					value="<%=address.getLocality()%>" helpMessage="address.locality-help" />
				<aui:input name="address.postalCode" label="postal-code"
					value="<%=address.getPostalCode()%>" helpMessage="address.postal-code-help"/>
				<aui:input name="address.region" label="region"
					value="<%=address.getRegion()%>" helpMessage="address.region-help" />
				<aui:input name="address.country" label="country"
					value="<%=address.getCountry()%>" helpMessage="address.country-help" />
			</aui:col>
			<aui:col span="1">
                <liferay-ui:icon-delete url="javascript:;" cssClass="btn" />
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
			<aui:col span="5">
				<aui:input name="address.streetAddress" type="textarea"
					cssClass="address" label="street" helpMessage="address.street-address-help" />
				<aui:input name="address.poBox" label="po-box" helpMessage="address.po-box-help" />
				<aui:select name="address.type" label="type" helpMessage="address.type-help">
					<%
						for (String type : addressTypes) {
					%>
					<aui:option value="<%=type%>" label="<%= type %>"/>
					<%
						}
					%>
				</aui:select>
			</aui:col>
			<aui:col span="6">
				<aui:input name="address.locality" label="city" helpMessage="address.locality-help" />
				<aui:input name="address.postalCode" label="postal-code" helpMessage="address.postal-code-help" />
				<aui:input name="address.region" label="region" helpMessage="address.region-help" />
				<aui:input name="address.country" label="country" helpMessage="address.country-help"/>
			</aui:col>
			<aui:col span="1">
                <liferay-ui:icon iconCssClass="icon-plus" url="javascript:;" cssClass="btn btn-add" />              
			</aui:col>
		</aui:row>

	</aui:container>
</aui:fieldset>
