<%--
    latest.jsp: Display the latest state of the configured channels
    
    Created:    2017-04-11 17:45 by Christian Berndt
    Modified:   2017-10-25 15:51 by Christian Berndt
    Version:    1.0.4
 --%>
 
<%@ include file="/html/init.jsp"%>

<c:choose>
    <c:when test="<%= channelNameTermCollectors.size() == 0 %>">
        <div class="alert alert-info">
            <liferay-ui:message key="no-channel-data-found"/>
        </div>
    </c:when>
    <c:otherwise>
        <aui:row>
            <%
                int i = 0;
                for (TermCollector termCollector : channelNameTermCollectors) {
                    
                    Hits hits = MeasurementLocalServiceUtil.search(
                            themeDisplay.getCompanyId(), scopeGroupId, null,
                            termCollector.getTerm(), 0, new Date().getTime(),
                            true, 0, 1, null);
                    
                    Document document = hits.toList().get(0); 
                    
                    channelName = termCollector.getTerm();
                    String timestamp = document.get("timestamp"); 
                    until = GetterUtil.getLong(document.get("date_sortable"));
                    from = until - 1000 * 60 * 60 * 24; 
                    
                    
                    PortletURL graphURL = renderResponse.createRenderURL(); 
                    graphURL.setParameter("tabs1", "chart"); 
                    graphURL.setParameter("channelName", channelName);
                    graphURL.setParameter("from", String.valueOf(from));
                    graphURL.setParameter("until", String.valueOf(until)); 
            %>
                <aui:col span="3">
                    <div class="display">
                        <div class="name">
                            <%= channelName %>
                        </div> 
                        <a href="<%= graphURL.toString() %>">          
                            <span class="face">
                                <span class="value-wrapper">
                                    <span class="value"><%= document.get("value") %></span><br/>
                                    <span class="unit"><%= document.get("channelUnit") %></span> 
                                </span>
                            </span>
                        </a> 
                        <div class="caption">
                            <%= timestamp %> 
                        </div>
                    </div>
                </aui:col>
                
                <c:if test="<%= i > 0 && (i+1)%4 == 0 %>">
                    <%= "</div><div class='row-fluid'>" %>
                </c:if>    
            <%
                    i++;
                }
            %>
        </aui:row>
    </c:otherwise>
</c:choose>
