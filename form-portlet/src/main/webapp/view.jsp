<%--
    view.jsp: Default view of the form-portlet.
    
    Created:     2017-02-20 14:30 by Christian Berndt
    Modified:    2017-02-20 14:30 by Christian Berndt
    Version:     1.0.0
--%>

<%@ include file="/html/init.jsp"%>

<%
    String backURL = ParamUtil.getString(request, "backURL");
    String currentURL = PortalUtil.getCurrentURL(request);
    boolean isMember = ParamUtil.getBoolean(request, "isMember", false);
%>

<liferay-ui:header backURL="<%=backURL%>" title="<%=formTitle%>" />

<liferay-ui:error exception="<%=MemberNumberException.class%>"
    focusField="memberNumber" message="member-number-is-required" />

<c:if test="<%=Validator.isNotNull(description)%>">
    <p class="description lead"><%=description%></p>
</c:if>

<portlet:actionURL name="processForm" var="actionURL">
</portlet:actionURL>

<aui:form action="<%= actionURL %>">

    <aui:input name="redirect" type="hidden"
        value="<%=currentURL%>" />

    <aui:fieldset>
        <aui:input name="orderId" type="hidden" value="<%=orderId%>" />
        <aui:input cssClass="width-max" disabled="true" label="subject"
            name="orderId" value="<%=orderId%>" />
    </aui:fieldset>

    <aui:fieldset>
        <aui:input label="member-of-flussbad-berlin" name="isMember"
            type="checkbox" value="<%=isMember%>" />
    </aui:fieldset>

    <aui:fieldset id="noMemberInfo" label="">
        <aui:row>
            <aui:col span="6">
                <aui:input name="amount" type="hidden" value="15"/>
                <aui:input cssClass="width-1col" disabled="true" inlineField="<%= true %>" name="amount" value="15"/>
                <aui:input name="currency" type="hidden" value="<%= currency %>"/>                
                <aui:input cssClass="width-1col" disabled="true" inlineField="<%= true %>" label="" name="currency" value="<%= currency %>"/>                
            </aui:col>
        </aui:row>
    </aui:fieldset>

    <aui:fieldset id="memberInfo" label="">
        <aui:row>
            <aui:col span="6">    
                <aui:input name="amount" type="hidden" value="5"/>
                <aui:input cssClass="width-1col" disabled="true" inlineField="<%= true %>" name="amount" value="5"/> 
                <aui:input name="currency" type="hidden" value="<%= currency %>"/>                
                <aui:input cssClass="width-1col" disabled="true" inlineField="<%= true %>" label="" name="currency" value="<%= currency %>"/>                
            </aui:col>
            <aui:col span="6">
                <aui:input name="memberNumber"/>            
            </aui:col>
        </aui:row>       
    </aui:fieldset>

    <aui:fieldset label="">
        <aui:row>
            <aui:col span="6">
                <aui:input label="first-name" name="first_name"
                    required="true" />
            </aui:col>
            <aui:col span="6">
                <aui:input label="last-name" name="last_name"
                    required="true" />
            </aui:col>
        </aui:row>
    </aui:fieldset>

    <aui:fieldset label="">
        <aui:row>
            <aui:col span="6">
                <aui:input label="address" name="address"
                    required="true" />
            </aui:col>
            <aui:col span="6">
                <aui:input label="address-2" name="address2" />
            </aui:col>
        </aui:row>
    </aui:fieldset>
    
    <aui:fieldset label="">
        <aui:row>
            <aui:col span="6">
                <aui:input label="postal-code" name="postal_code"
                    required="true" />
            </aui:col>
            <aui:col span="6">
                <aui:input label="city" name="city"
                    required="true" />
            </aui:col>
        </aui:row>
    </aui:fieldset>
            
    <aui:fieldset label="">
        <aui:row>
            <aui:col span="6">
                <aui:field-wrapper>
                    <aui:select name="country" required="true">
                        <%
                            countryIterator = countrySet.iterator();                    
                            while (countryIterator.hasNext()) {
                                String countryName = countryIterator.next();
                                String countryCode = countryMap.get(
                                        countryName).getCountry();
                        %>
                        <aui:option label="<%=countryName%>"
                            selected="<%=defaultCountry.equals(countryCode)%>"
                            value="<%=countryCode%>" />
                        <%
                            }
                        %>
                    </aui:select>
                </aui:field-wrapper> 
            </aui:col>
            <aui:col span="6">
                <aui:input label="email" name="email"
                    required="true" />
            </aui:col>
        </aui:row>
    </aui:fieldset>      

    <aui:button-row>
        <aui:button value="<%= submitLabel %>" type="submit" />
        <c:if test="<%= Validator.isNotNull(backURL) %>">
            <aui:a cssClass="btn" href="<%= backURL %>"
                label="cancel" title="cancel" />
        </c:if>
    </aui:button-row>
</aui:form>

<hr>

<ifx-util:build-info />

<aui:script>
    Liferay.Util.toggleBoxes('<portlet:namespace />isMemberCheckbox','<portlet:namespace />memberInfo', false);
    Liferay.Util.toggleBoxes('<portlet:namespace />isMemberCheckbox','<portlet:namespace />noMemberInfo', true);
</aui:script>
