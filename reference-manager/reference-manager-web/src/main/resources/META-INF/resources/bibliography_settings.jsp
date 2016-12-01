<%--
    bibliography_settings.jsp: edit the bibliography's settings.
    
    Created:    2016-12-01 02:33 by Christian Berndt
    Modified:   2016-12-01 02:33 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    long bibliographyId = ParamUtil.getLong(request, "bibliographyId");
    String redirect = ParamUtil.getString(request, "redirect");

    String submitLabel = "create"; 
    
    Bibliography bibliography = (Bibliography) request.getAttribute(BibliographyWebKeys.BIBLIOGRAPHY);
    
    if (bibliography != null) {
        submitLabel = "save"; 
    }

    boolean hasUpdatePermission = true;
    
    if (bibliography != null) {
        BibliographyPermission.contains(permissionChecker, bibliography, BibliographyActionKeys.UPDATE);
    }
%>

<portlet:actionURL name="updateBibliography" var="updateBibliographyURL">
</portlet:actionURL>

<aui:form action="<%= updateBibliographyURL %>" method="post"
    name="fm">

    <aui:input name="redirect" type="hidden"
        value="<%=currentURL%>" />
    <aui:input name="bibliographyId" type="hidden"
        value="<%=bibliographyId%>" />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />

    <aui:model-context bean="<%=bibliography%>"
        model="<%=Bibliography.class%>" />

    <aui:fieldset>

        <div class="form-group form-group-inline input-text-wrapper">
            <label class="control-label" for="account">
                <liferay-ui:message key="account" />
            </label> <br/>
                    
            <input class="field form-control" id="account" readonly="readonly"
                value="<%="/user/" + themeDisplay.getUser().getScreenName()%>" />
        </div>
           
        <div class="form-group form-group-inline input-text-wrapper dash">                    
            / 
        </div>
            
        <aui:input disabled="<%=!hasUpdatePermission%>" inlineField="true"
            name="urlTitle" /> 
            
        <p class="help-message">
            <liferay-ui:message
                key="bibliography-url-title-help" />
        </p>                 

        <aui:input disabled="<%=!hasUpdatePermission%>"
            name="title" />

        <p class="help-message">
            <liferay-ui:message
                key="bibliography-title-help" />
        </p>

        <aui:input disabled="<%=!hasUpdatePermission%>"
            name="description" type="textarea" />

        <p class="help-message">
            <liferay-ui:message
                key="bibliography-description-help" />
        </p>

    </aui:fieldset>

    <aui:fieldset cssClass="private-settings">
        <%
            boolean isPrivate = false;
        %>

        <aui:field-wrapper>
            <aui:input checked="<%=!isPrivate%>" name="private" label=""
                type="radio" value="false" />
                
            <span class="help-message">
                <liferay-ui:icon cssClass="icon icon-eye-open"/>
                <liferay-ui:message key="bibliography-private-false"/>
            </span>
        </aui:field-wrapper>
            
        <aui:field-wrapper>
            <aui:input checked="<%=isPrivate%>" name="private" label=""
                type="radio" value="true" disabled="true" />
                
            <span class="help-message text-muted">
                <liferay-ui:icon cssClass="icon icon-lock"/>
                <liferay-ui:message key="bibliography-private-true"/>
            </span>
        </aui:field-wrapper>


    </aui:fieldset>

    <aui:fieldset cssClass="tags-and-categories">

        <aui:input name="categories" type="assetCategories"
            disabled="<%=!hasUpdatePermission%>" />
            
        <p class="help-message">
            <liferay-ui:message
                key="bibliography-categories-help" />
        </p>

        <aui:input name="tags" type="assetTags"
            disabled="<%=!hasUpdatePermission%>" />
            
        <p class="help-message">
            <liferay-ui:message
                key="bibliography-tags-help" />
        </p>
            
    </aui:fieldset>
    
    <aui:button-row>
        <aui:button
            disabled="<%=!hasUpdatePermission%>" type="submit" value="<%= submitLabel %>" />

        <aui:button 
            disabled="<%=!hasUpdatePermission%>"
            href="<%=redirect%>" type="cancel" />
    </aui:button-row>

</aui:form>
