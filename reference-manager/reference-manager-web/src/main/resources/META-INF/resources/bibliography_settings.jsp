<%--
    bibliography_settings.jsp: edit the bibliography's settings.
    
    Created:    2016-12-01 02:33 by Christian Berndt
    Modified:   2017-02-06 22:34 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.referencemanager.exception.DuplicateUrlTitleException"%>

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
        hasUpdatePermission = BibliographyPermission.contains(permissionChecker, bibliography,
                BibliographyActionKeys.UPDATE);
    } else if (!themeDisplay.isSignedIn()) {
        hasUpdatePermission = false; 
    }
%>

<portlet:actionURL name="updateBibliography" var="updateBibliographyURL">
</portlet:actionURL>

<aui:form action="<%= updateBibliographyURL %>" method="post"
    name="fm">
    
    <aui:input name="bibliographyId" type="hidden"
        value="<%=bibliographyId%>" />

    <aui:input name="mvcPath" type="hidden"
        value="/edit_bibliography.jsp" />
        
    <aui:input name="redirect" type="hidden"
        value="<%=currentURL%>" />
        
    <aui:input name="tabs1" type="hidden"
        value="settings" />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />

    <liferay-ui:error
        exception="<%= DuplicateUrlTitleException.class %>"
        message="please-enter-a-unique-url-title" />

    <aui:model-context bean="<%=bibliography%>"
        model="<%=Bibliography.class%>" />

    <aui:fieldset>

        <div class="form-group form-group-inline input-text-wrapper">
            <label class="control-label" for="account"> <liferay-ui:message
                    key="account" />
            </label> <br /> <input class="field form-control" id="account"
                readonly="readonly"
                value="<%="/user/" + themeDisplay.getUser().getScreenName()%>" />
        </div>

        <div  class="form-group form-group-inline input-text-wrapper dash">
            /</div>

        <aui:input disabled="<%=!hasUpdatePermission%>"
            inlineField="true" name="urlTitle" />

        <p class="help-block">
            <liferay-ui:message key="bibliography-url-title-help" />
        </p>

        <aui:input disabled="<%=!hasUpdatePermission%>" name="title" />

        <p class="help-block">
            <liferay-ui:message key="bibliography-title-help" />
        </p>

        <aui:input cssClass="description"
            disabled="<%=!hasUpdatePermission%>" name="description"
            type="textarea" />

        <p class="help-block">
            <liferay-ui:message key="bibliography-description-help" />
        </p>

    </aui:fieldset>

    <aui:fieldset cssClass="private-settings">
        <%
            boolean isPrivate = false;
        %>

        <aui:field-wrapper>
            <aui:input checked="<%=!isPrivate%>" name="private" label=""
                type="radio" value="false" />
                
            <span class="help-block">
                <liferay-ui:icon cssClass="icon icon-eye-open"/>
                <liferay-ui:message key="bibliography-private-false"/>
            </span>
        </aui:field-wrapper>
            
        <aui:field-wrapper>
            <aui:input checked="<%=isPrivate%>" name="private" label=""
                type="radio" value="true" disabled="true" />
                
            <span class="help-block text-muted">
                <liferay-ui:icon cssClass="icon icon-lock"/>
                <liferay-ui:message key="bibliography-private-true"/>
            </span>
        </aui:field-wrapper>


    </aui:fieldset>

    <aui:fieldset cssClass="tags-and-categories">

        <c:choose>
            <c:when test="<%=hasUpdatePermission%>">
                <aui:input name="categories" type="assetCategories" />
            </c:when>
            <c:otherwise>
                <aui:field-wrapper name="categories" inlineLabel="false">
                    <c:if test="<%= bibliography != null %>">                          
                        <liferay-ui:asset-categories-summary
                            classPK="<%=bibliography.getBibliographyId()%>"
                            className="<%=Bibliography.class.getName()%>" />
                    </c:if>
                </aui:field-wrapper>
            </c:otherwise>
        </c:choose>

        <p class="help-block">
            <liferay-ui:message
                key="bibliography-categories-help" />
        </p>
        
        <c:choose>
            <c:when test="<%=hasUpdatePermission%>">
                <aui:input name="tags" type="assetTags" />
            </c:when>
            <c:otherwise>
                <aui:field-wrapper name="tags">
                    <c:if test="<%= bibliography != null %>">
                        <liferay-ui:asset-tags-summary
                            classPK="<%=bibliography.getBibliographyId()%>"
                            className="<%=Bibliography.class.getName()%>" />
                    </c:if>
                </aui:field-wrapper>
            </c:otherwise>
        </c:choose>
                    
        <p class="help-block">
            <liferay-ui:message
                key="bibliography-tags-help" />
        </p>
            
    </aui:fieldset>

    <aui:fieldset>
        <aui:input type="hidden" name="comments" />
        <aui:input disabled="true" name="comments" type="textarea" />

        <p class="help-block">
            <liferay-ui:message key="comments-help" />
        </p>

        <aui:input type="hidden" name="preamble" />
        <aui:input disabled="true" name="preamble" type="textarea" />

        <p class="help-block">
            <liferay-ui:message key="preamble-help" />
        </p>

        <aui:input type="hidden" name="strings" />
        <aui:input disabled="true" name="strings" type="textarea" />

        <p class="help-block">
            <liferay-ui:message key="strings-help" />
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
