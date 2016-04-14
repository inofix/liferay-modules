<%--
    configuration.jsp: Configure the proxy-portlet's preferences.
    
    Created:    2016-04-14 11:44 by Christian Berndt
    Modified:   2016-04-14 11:44 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%-- Import required classes --%>
<%
    PortletURL portletURL = renderResponse.createRenderURL();
%>

<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" enctype="multipart/form-data" method="post" name="fm">
    
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />
        
    <liferay-ui:panel-container id="proxyportletSettingsPanelContainer" persistState="<%= true %>">
        
        <liferay-ui:panel id="proxyportletHostsPanel" title="allowed-hosts" extended="true">
        
            <aui:fieldset id="host" cssClass="host">
            <%
                for (int i=0; i<hosts.length; i++) {
            %>
                <aui:container>
            
                    <aui:row>
                        <div class="lfr-form-row">

                            <legend class="fieldset-legend">
                            
                                <span class="sort-handle"></span>

                            </legend>           
                        
                            <div class="row-fields">
                                                                
                                <aui:col span="6">
                                
                                    <aui:input name="host"
                                        helpMessage="allowed-host-help"
                                        label="allowed-host"
                                        value="<%= hosts[i] %>" />

                                </aui:col>

                            </div>
                        </div>
                    </aui:row>
            
                </aui:container>
            <%
                }
            %>
            </aui:fieldset>

        </liferay-ui:panel>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var markerAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />host',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
    
</aui:script>
