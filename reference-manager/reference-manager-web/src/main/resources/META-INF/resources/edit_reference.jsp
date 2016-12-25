<%--
    edit_reference.jsp: edit a single reference.
    
    Created:    2016-11-18 18:46 by Christian Berndt
    Modified:   2016-12-25 14:57 by Christian Berndt
    Version:    1.1.4
--%>

<%@ include file="/init.jsp"%>

<%
    long bibliographyId = ParamUtil.getLong(request, "bibliographyId");
    String redirect = ParamUtil.getString(request, "redirect");
    String tabNames = "bibtex,general,usage";
//     String tabNames = "required-fields,optional-fields,general,bibtex,usage";
    String tabs1 = ParamUtil.getString(request, "tabs1", "bibtex");
//     String tabs1 = ParamUtil.getString(request, "tabs1", "required-fields");

    long referenceId = ParamUtil.getLong(request, "referenceId");

    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);
    String type = ParamUtil.getString(request, "type");

    boolean hasUpdatePermission = false;

    if (reference != null) {

        hasUpdatePermission = ReferencePermission.contains(permissionChecker, reference,
                ReferenceActionKeys.UPDATE);

        AssetEntryServiceUtil.incrementViewCounter(Reference.class.getName(), reference.getReferenceId());

        if (Validator.isNull(type)) {
            type = reference.getType().toLowerCase();
        }
        
    } else {

        // new reference
        
        hasUpdatePermission = true;
        type = "article";
        
    }

    JSONObject entryFields = null;

    for (String entryType : BibTeXUtil.ENTRY_TYPES) {
        if (entryType.equals(type)) {
            entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + entryType));
        }
    }

    portletURL.setParameter("bibliographyId", String.valueOf(bibliographyId));
    portletURL.setParameter("mvcPath", "/edit_reference.jsp");
    portletURL.setParameter("referenceId", String.valueOf(referenceId));
    portletURL.setParameter("type", type);
%>

<portlet:actionURL name="updateReference" var="updateReferenceURL">
    <portlet:param name="bibliographyId"
        value="<%=String.valueOf(bibliographyId)%>" />
    <portlet:param name="mvcPath" value="/edit_reference.jsp" />
    <portlet:param name="tabs1" value="<%= tabs1 %>" />
</portlet:actionURL>


<liferay-util:buffer var="typeSelect">
    <div class="clearfix">
        <div class="pull-left">
            <aui:select name="type_select" label=""
                onChange="javascript: window.location.href = this.value; ">
                <%
                    PortletURL selectURL = liferayPortletResponse.createRenderURL();
            
                    selectURL.setParameter("bibliographyId", String.valueOf(bibliographyId));
                    selectURL.setParameter("mvcPath", "/edit_reference.jsp");
                    selectURL.setParameter("referenceId", String.valueOf(referenceId));
                    selectURL.setParameter("tabs1", tabs1);
                
                    for (String entryType : BibTeXUtil.ENTRY_TYPES) {
                        selectURL.setParameter("type", entryType);
                        boolean selected = entryType.equals(type);
                %>
                <option value="<%=selectURL.toString()%>"
                    <%=(selected) ? "selected" : ""%>>
                    <liferay-ui:message key="<%= "entry-type-" + entryType %>"/>
                </option>
                <%
                    }
                %>
            </aui:select>
        </div>
        <div class="pull-right">
            <a class="btn btn-default" href="<%= redirect %>"><liferay-ui:message key="back"/></a>
        </div>
    </div>

</liferay-util:buffer>

<aui:form action="<%= updateReferenceURL %>" method="post" name="fm">

    <div class="reference-head">
        <c:choose>
            <c:when test="<%=Validator.isNull(reference)%>">
                <h2>
                    <liferay-ui:message key="add-reference" />
                </h2>
                <c:choose>
                    <c:when test="<%=themeDisplay.isSignedIn()%>">
                        <p>
                            <liferay-ui:message
                                key="you-can-import-your-references-from-a-file-or-pick-references-already-available-on-bibshare" />
                        </p>
                        
                        <%= typeSelect %>
                        
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            <liferay-ui:message
                                key="you-must-sign-in-order-to-create-a-new-reference" />

                            <strong> <aui:a
                                    href="<%=themeDisplay.getURLSignIn()%>"
                                    label="sign-in">
                                    <liferay-ui:icon
                                        iconCssClass="icon-signin" />
                                </aui:a>
                            </strong>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            
            <c:otherwise>
            
                <c:if test="<%= Validator.isNotNull(reference.getCitation()) %>">
                    <h3><%=reference.getCitation()%></h3>
                </c:if>
                
                <%= typeSelect %>
                
            </c:otherwise>
        </c:choose>
    </div>
        
    <liferay-ui:tabs names="<%= tabNames %>" param="tabs1"
        url="<%=portletURL.toString()%>" />

    <aui:input name="redirect" type="hidden" value="<%=redirect%>" />
    <aui:input name="referenceId" type="hidden" value="<%=referenceId%>" />

    <liferay-ui:asset-categories-error />

    <liferay-ui:asset-tags-error />
    
    <c:choose>
    
        <c:when test='<%= tabs1.equals("required-fields") %>'>
             
            <aui:fieldset cssClass="required-fields">
            
            <%
                JSONArray requiredFields = entryFields.getJSONArray("required");  
            
                if (requiredFields != null && requiredFields.length() > 0) {
            
                    for (int i=0; i<requiredFields.length(); i++) {
                        JSONObject field = requiredFields.getJSONObject(i);
                        String name = field.getString("name"); 
                        String helpKey = field.getString("help");
                        if (Validator.isNull(helpKey)) {
                            helpKey = name + "-help"; 
                        }
    
                        String value = reference.getFields().get(name); 
                        
            %>
                <aui:input name="name" type="hidden" value="<%= name %>"/>           
                <aui:input helpMessage="<%= helpKey %>" label="<%= name %>" name="value" value="<%= value %>"/>
            <%
                    }
                } else {
                    
            %>
                <div class="alert alert-info"><liferay-ui:message key="this-entry-type-has-no-required-fields"/></div>
            <%
                }
            %>
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
        
            <aui:fieldset cssClass="optional-fields">
            <%
                JSONArray optionalFields = entryFields.getJSONArray("optional");  
            
                for (int i=0; i<optionalFields.length(); i++) {
                    JSONObject field = optionalFields.getJSONObject(i);
                    String name = field.getString("name");
                    String helpKey = field.getString("help");
                    if (Validator.isNull(helpKey)) {
                        helpKey = name + "-help"; 
                    }                    

                    String value = reference.getFields().get(name); 
            %>
                <aui:input name="name" type="hidden" value="<%= name %>"/>           
                <aui:input helpMessage="<%= helpKey %>" label="<%= name %>" name="value" value="<%= value %>"/>
            <%
                }
            %>
            </aui:fieldset>
                            
        </c:when>
        
        <c:when test='<%= tabs1.equals("usage") %>'>
            <p class="help-message"><strong><liferay-ui:message key="your-bibliographies"/></strong></p>
            <p>
                <a href="#" class="btn btn-default">Ancient Astronomy</a>
                <a href="#" class="btn btn-default">Collaborative Action</a>            
            </p>
            <p><strong><liferay-ui:message key="other-bibliographies"/></strong><p>
            <div>
                <a href="#" class="btn btn-default">Collaborative Action</a>            
                <a href="#" class="btn btn-default">Ancient Astronomy</a>
            </div>
        </c:when>  
        
        <c:otherwise>
        
            <aui:fieldset>
                <aui:input bean="<%=reference%>" helpMessage="bibtex-help"
                    disabled="<%=!hasUpdatePermission%>" label="bibtex"
                    model="<%=Reference.class%>" name="bibTeX"
                    type="textarea" />

            </aui:fieldset>  
                         
        </c:otherwise>
            
    </c:choose>

    <aui:button-row>
        <aui:button type="submit" disabled="<%= !hasUpdatePermission %>" />    
        <aui:button href="<%=redirect%>" type="cancel" />
    </aui:button-row>

</aui:form>
