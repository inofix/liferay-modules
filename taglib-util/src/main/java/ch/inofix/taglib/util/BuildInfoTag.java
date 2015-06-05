package ch.inofix.taglib.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 
 * @author Christian Berndt
 * @created 2015-06-04 11:56
 * @modified 2015-06-04 11:56
 * @version 1.0.0
 *
 */
public class BuildInfoTag extends SimpleTagSupport {

	public void doTag() throws JspException, IOException {

		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		
		// TODO: Read maxLength from tag-configuration
		int maxLength = 50; 

		// Read the build properties from
		// the MANIFEST.MF
		ServletContext ctxt = request.getSession().getServletContext();
		InputStream in = ctxt.getResourceAsStream("/META-INF/MANIFEST.MF");
		Properties props = new Properties();

		if (in != null) {
			props.load(in);
		}

		String buildNumber = props.getProperty("Implementation-Build");
		String version = props.getProperty("Implementation-Version");
		String timestamp = props.getProperty("Timestamp");
		String issuesURL = props.getProperty("Issue-Management");

		Date buildDate = new Date();
		if (timestamp != null) {
			long time = Long.parseLong(timestamp);
			buildDate.setTime(time);
		}

		// Load the properties stored service.properties
		// (Especially build.number - the service build number)
		in = ctxt.getResourceAsStream("/WEB-INF/classes/service.properties");

		Properties serviceProperties = new Properties();

		if (in != null) {
			serviceProperties.load(in);
		}

		String serviceBuild = serviceProperties.getProperty("build.number");

		StringBuilder sb = new StringBuilder();

		sb.append("<dl class=\"build-info\">");

		if (version != null) {
			sb.append("<dt>");
			// TODO: Add i18n
			sb.append("Version:");
			sb.append("</dt>");
			sb.append("<dfn>");
			sb.append(version);
			sb.append("</dfn>");
		}
		if (buildNumber != null) {
			sb.append("<dt>");
			// TODO: Add i18n
			sb.append("Build:");
			sb.append("</dt>");
			sb.append("<dfn>");
			sb.append(buildNumber);
			sb.append("</dfn>");
		}
		if (timestamp != null) {
			sb.append("<dt>");
			// TODO: Add i18n
			sb.append("Date: ");
			sb.append("</dt>");
			sb.append("<dfn>");
			sb.append(buildDate);
			sb.append("</dfn>");
		}
		if (serviceBuild != null) {
			sb.append("<dt>");
			// TODO: Add i18n
			sb.append("Service Build:");
			sb.append("</dt>");
			sb.append("<dfn>");
			sb.append(serviceBuild);
			sb.append("</dfn>");
		}
		if (issuesURL != null) {
			sb.append("<dt>");
			// TODO: Add i18n
			sb.append("Issue Management:");
			sb.append("</dt>");
			sb.append("<dfn>");
			sb.append("<a href=\"");
			sb.append(issuesURL);
			sb.append("\" target=\"_blank\">");
			if (issuesURL.length() > maxLength) {
				sb.append(issuesURL.substring(0, maxLength-1));
				sb.append(" ..."); 
			} else {
				sb.append(issuesURL);
			}
			sb.append("</a>");
			sb.append("</dfn>");
		}

		sb.append("</dl>");

		pageContext.getOut().write(sb.toString());

	}

}
