<%-- 
    edit_contact/edit_vcard.jsp: Edit the vCard String of the contact.
    
    Created:    2015-05-08 15:42 by Christian Berndt
    Modified:   2015-05-20 18:18 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/html/edit_contact/init.jsp" %>


<aui:fieldset label="v-card">

    <aui:row>
        <aui:col span="10">
			<aui:input name="vCard" type="textarea" cssClass="v-card"
			    value="<%= contact_.getCard() %>" disabled="true" label="" />
	    </aui:col>
	    <aui:col span="2">
	       <aui:button value="copy"/>
	    </aui:col>
    </aui:row>
</aui:fieldset>
    