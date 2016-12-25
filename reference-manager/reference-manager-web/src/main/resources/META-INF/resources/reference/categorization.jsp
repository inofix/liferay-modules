<%--
    reference/categorization.jsp: the categories tab of the reference editor.
    
    Created:    2016-12-25 19:24 by Christian Berndt
    Modified:   2016-12-25 20:28 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%
    Reference reference = (Reference) request.getAttribute(ReferenceWebKeys.REFERENCE);
    boolean hasUpdatePermission = (Boolean) request.getAttribute("reference.hasUpdatePermission");
%>

<aui:model-context bean="<%=reference%>"
    model="<%=Reference.class%>" />

<liferay-ui:asset-categories-error />

<liferay-ui:asset-tags-error />

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
