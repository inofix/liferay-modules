<%--
    edit_reference.jsp: edit a single reference.
    
    Created:    2016-11-18 18:46 by Christian Berndt
    Modified:   2016-12-28 18:33 by Christian Berndt
    Version:    1.1.7
--%>

<%@ include file="/init.jsp"%>

<%
    long bibliographyId = ParamUtil.getLong(request, "bibliographyId");
    String redirect = ParamUtil.getString(request, "redirect");
    
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
        type = ParamUtil.getString(request, "type", "article");
   
    }

    JSONObject entryFields = null;

    for (String entryType : BibTeXUtil.ENTRY_TYPES) {
        if (entryType.equals(type)) {
            entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + entryType));
        }
    }
    
    request.setAttribute("reference", reference); 
    request.setAttribute("reference.entryFields", entryFields); 
    request.setAttribute("reference.hasUpdatePermission", hasUpdatePermission); 

    portletURL.setParameter("bibliographyId", String.valueOf(bibliographyId));
    portletURL.setParameter("mvcPath", "/edit_reference.jsp");
    portletURL.setParameter("referenceId", String.valueOf(referenceId));
    portletURL.setParameter("type", type);
%>

<portlet:actionURL name="updateReference" var="updateReferenceURL">
    <portlet:param name="bibliographyId"
        value="<%=String.valueOf(bibliographyId)%>" />
    <portlet:param name="mvcPath" value="/edit_reference.jsp" />
</portlet:actionURL>


<liferay-util:buffer var="typeSelect">
    <div class="clearfix">
        <div class="pull-left">
            <aui:select disabled="<%= !hasUpdatePermission %>" name="type_select" label=""
                onChange="javascript: window.location.href = this.value; ">
                <%
                    PortletURL selectURL = liferayPortletResponse.createRenderURL();
            
                    selectURL.setParameter("bibliographyId", String.valueOf(bibliographyId));
                    selectURL.setParameter("mvcPath", "/edit_reference.jsp");
                    selectURL.setParameter("referenceId", String.valueOf(referenceId));
                    selectURL.setParameter("redirect", redirect);
                
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

    <%-- custom displayStyle "tabs2 is only available   --%>
    <%-- if the custom-form-navigator hook is deployed. --%>
    <liferay-ui:form-navigator
        backURL="<%= redirect %>"
        showButtons="<%= hasUpdatePermission %>"
        formModelBean="<%= reference %>"
        id="reference.form"
        displayStyle="tabs"        
    />

</aui:form>
