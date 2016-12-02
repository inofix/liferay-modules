<%--
    edit_reference.jsp: edit a single reference.
    
    Created:    2016-11-18 18:46 by Christian Berndt
    Modified:   2016-12-01 23:19 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");
    String tabNames = "required-fields,optional-fields,general,abstract,review,bibtex";
    String tabs1 = ParamUtil.getString(request, "tabs1", "required-fields");

    long referenceId = ParamUtil.getLong(request, "referenceId");

    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);

    boolean hasUpdatePermission = ReferencePermission.contains(permissionChecker, reference,
            ReferenceActionKeys.UPDATE);  
    
    portletURL.setParameter("referenceId", String.valueOf(referenceId));
    portletURL.setParameter("mvcPath", "/edit_reference.jsp");

%>

<c:choose>
    <c:when test="<%=Validator.isNull(reference)%>">
        <div class="reference-head">
            <h2>
                <liferay-ui:message key="create-a-new-reference" />
            </h2>
            
            <c:choose>
                <c:when test="<%= themeDisplay.isSignedIn() %>">
                    <p>
                        <liferay-ui:message key="you-can-import-your-references-from-a-file-or-pick-references-already-available-on-bibshare" />
                    </p>                    
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        <liferay-ui:message key="you-must-sign-in-order-to-create-a-new-reference"/>
                        
                        <strong>
                            <aui:a href="<%= themeDisplay.getURLSignIn() %>" label="sign-in">
                                <liferay-ui:icon iconCssClass="icon-signin"/>
                            </aui:a>
                        </strong> 
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>
    <c:otherwise>
        <div class="reference-head">
            <h3><%=reference.getCitation()%></h3>
        </div>
    </c:otherwise>
</c:choose>

<liferay-ui:tabs names="<%= tabNames %>" param="tabs1"
    url="<%=portletURL.toString()%>" />
    
<portlet:actionURL name="updateReference" var="updateReferenceURL">
</portlet:actionURL>
   
<aui:form action="<%= updateReferenceURL %>" method="post" name="fm">

    <aui:input name="redirect" type="hidden" value="<%=currentURL%>" />
    <aui:input name="referenceId" type="hidden" value="<%=referenceId%>" />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />
    
    <c:choose>

        <c:when test='<%=tabs1.equals("abstract")%>'>

            <aui:fieldset>
                <aui:input name="name" type="hidden" value="abstract"/>
                <aui:input disabled="<%=!hasUpdatePermission%>"
                    label="abstract" name="value" type="textarea"
                    value="<%=reference.getField("abstract")%>" />

            </aui:fieldset>

        </c:when>

        <c:when test='<%= tabs1.equals("bibtex") %>'>

            <aui:fieldset>
                <aui:input bean="<%=reference%>"
                    disabled="<%=!hasUpdatePermission%>" label="bibtex"
                    model="<%=Reference.class%>" name="bibTeX"
                    type="textarea" />

            </aui:fieldset>

        </c:when>
        
        <c:when test='<%= tabs1.equals("general") %>'>
                
            <aui:fieldset cssClass="tags-and-categories">
        
                <c:choose>
                    <c:when test="<%=hasUpdatePermission%>">
                        <aui:input bean="<%=reference%>"
                            model="<%=Reference.class%>"
                            name="categories" type="assetCategories" />
                    </c:when>
                    <c:otherwise>
                        <aui:field-wrapper name="categories" inlineLabel="false">
                            <c:if test="<%= reference != null %>">                          
                                <liferay-ui:asset-categories-summary
                                    classPK="<%=reference.getReferenceId()%>"
                                    className="<%=Reference.class.getName()%>" />
                            </c:if>
                        </aui:field-wrapper>
                    </c:otherwise>
                </c:choose>
        
                <p class="help-message">
                    <liferay-ui:message
                        key="reference-categories-help" />
                </p>
                
                <c:choose>
                    <c:when test="<%=hasUpdatePermission%>">
                        <aui:input bean="<%=reference%>"
                            model="<%=Reference.class%>" name="tags"
                            type="assetTags" />
                    </c:when>
                    <c:otherwise>
                        <aui:field-wrapper name="tags">
                            <c:if test="<%= reference != null %>">
                                <liferay-ui:asset-tags-summary
                                    classPK="<%=reference.getReferenceId()%>"
                                    className="<%=Reference.class.getName()%>" />
                            </c:if>
                        </aui:field-wrapper>
                    </c:otherwise>
                </c:choose>
                            
                <p class="help-message">
                    <liferay-ui:message
                        key="reference-tags-help" />
                </p>
                    
            </aui:fieldset>
                
        </c:when>
        
        <c:when test='<%= tabs1.equals("optional-fields") %>'>
        
            optional-fields
                
        </c:when>
        
        <c:when test='<%= tabs1.equals("required-fields") %>'>
    
            <aui:fieldset cssClass="required-fields">
            <%
                for (String key : reference.getFields().keySet()) {
                    String value = reference.getFields().get(key); 
                    if (Validator.isNotNull(value)) {
            %>
                <aui:input name="name" type="hidden" value="<%= key %>"/>           
                <aui:input label="<%= key %>" name="value" value="<%= value %>"/>
            <%
                    }
                }
            %>
            </aui:fieldset>
                
        </c:when>
        
        <c:when test='<%= tabs1.equals("review") %>'>

            <aui:fieldset>
                <aui:input name="name" type="hidden" value="review"/>          
                <aui:input disabled="<%=!hasUpdatePermission%>"
                    label="review" name="value" type="textarea" />

            </aui:fieldset>
        </c:when>
            
    </c:choose>

    <aui:button-row>
        <aui:button type="submit" disabled="<%= !hasUpdatePermission %>" />    
        <aui:button href="<%=redirect%>" type="cancel" />
    </aui:button-row>
    
</aui:form>
