<%--
	time_picker/page.jsp: The jsp-part of the
	time-picker tag.
	
	Created: 	2013-10-13 11:26 by Christian Berndt
	Modified:	2013-11-09 20:02 by Christian Berndt
	Version: 	1.2
--%>

<%
	// Read the configuration parameter from the request.
	int deltaMinutes = (Integer)request.
		getAttribute("inofix-util:time-picker:deltaMinutes"); 
	int firstHour = (Integer)request.
		getAttribute("inofix-util:time-picker:firstHour"); 
	int hour = (Integer) request.
		getAttribute("inofix-util:time-picker:hour"); 
	int minute = (Integer) request.
		getAttribute("inofix-util:time-picker:minute"); 
	boolean nullable = (Boolean) request.
		getAttribute("inofix-util:time-picker:nullable"); 
	String prefix = (String) request.
		getAttribute("inofix-util:time-picker:prefix"); 

%>

<select name='<%=prefix%>Hour' class="time-picker-select">
	<% if (nullable) { %>
	<option value="-1"></option>
	<% } %>
	<%
		for (int i = firstHour; i < 24; i++) {
	%>

	<%
		String label = "0";
		if (i < 10) {
			label = label + String.valueOf(i);
		} else {
			label = String.valueOf(i);
		}

		boolean isSelected = false;
		if (hour == i) {
			isSelected = true;
		}
	%>
	<option value="<%=i%>" <%if (isSelected) {%> 
			selected="selected"
		<%}%>><%=label%></option>
	<%
		}
	%>
</select>
:
<select name='<%=prefix%>Minute' class="time-picker-select">
	<% if (nullable) { %>
	<option value="-1"></option>
	<% } %>
	<%
		for (int i = 0; i < 60; i++) {
			if (i==0 || (i%deltaMinutes == 0)) {
	%>

	<%
			String label = "0";
			if (i < 10) {
				label = label + String.valueOf(i);
			} else {
				label = String.valueOf(i);
			}
	
			boolean isSelected = false;
			if (minute == i) {
				isSelected = true;
			}
	%>
	<option value="<%=i%>" <%if (isSelected) {%> 
			selected="selected"
		<%}%>><%=label%>
	</option>
	<%
			}
		}
	%>
</select>