package ch.inofix.taglib.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.DirectRequestDispatcherFactoryUtil;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * A configurable revision display tag. Tries to
 * read various parameters from the webapp's 
 * MANIFEST.MF, Liferay's service.properties. 
 * 
 * @author Christian Berndt
 * @created 2013-11-02 16:50
 * @modified 2013-11-02 16:50
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class RevisionDisplayTag extends TagSupport {
	
	// Enable logging for this class.
	private static final Log _log = LogFactoryUtil.getLog(RevisionDisplayTag.class
			.getName());
	
	// Configurable parameter
	private boolean displayBuildDate = true; 
	private boolean displayBuildNumber = true; 
	private boolean displayServiceBuildNumber = true; 
	private boolean displaySVNRevision = true; 
	private boolean displayVersion = true; 
	
	private static final String _PAGE = "/taglib/util/revision_display/page.jsp";
	
	
	@Override
	public int doEndTag() throws JspException {

		_log.info("Executing doEndTag().");

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		 setAttributes(request);

		try {
			include(_PAGE);
		} catch (Exception e) {
			_log.error(e);
		}

		return EVAL_PAGE;

	};

	/**
	 * Include and execute the jsp-page.
	 * 
	 * @param page
	 *            the path to the jsp page.
	 * @since 1.0
	 * @throws Exception
	 */
	// From com.liferay.taglib.util.IncludeTag
	protected void include(String page) throws Exception {

		_log.info("Executing include().");

		ServletContext servletContext = pageContext.getServletContext();

		RequestDispatcher requestDispatcher = DirectRequestDispatcherFactoryUtil
				.getRequestDispatcher(servletContext, page);

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		HttpServletResponse response = new PipingServletResponse(pageContext);

		requestDispatcher.include(request, response);

		request.removeAttribute(WebKeys.SERVLET_CONTEXT_INCLUDE_FILTER_STRICT);
	}
	
	/**
	 * Store the tag configuration parameters as request attributes.
	 * 
	 * @param request
	 * @since 1.0
	 */
	protected void setAttributes(HttpServletRequest request) {

		request.setAttribute("inofix-util:revision-display:displayBuildDate",
				displayBuildDate);
		request.setAttribute("inofix-util:revision-display:displayBuildNumber",
				displayBuildNumber);
		request.setAttribute("inofix-util:revision-display:displayServiceBuildNumber",
				displayServiceBuildNumber);
		request.setAttribute("inofix-util:revision-display:displaySVNRevision",
				displaySVNRevision);		
		request.setAttribute("inofix-util:revision-display:displayVersion",
				displayVersion);

	}

	// Getter and setter.
	public boolean isDisplayBuildDate() {
		return displayBuildDate;
	}

	public void setDisplayBuildDate(boolean displayBuildDate) {
		this.displayBuildDate = displayBuildDate;
	}

	public boolean isDisplayBuildNumber() {
		return displayBuildNumber;
	}

	public void setDisplayBuildNumber(boolean displayBuildNumber) {
		this.displayBuildNumber = displayBuildNumber;
	}

	public boolean isDisplayServiceBuildNumber() {
		return displayServiceBuildNumber;
	}

	public void setDisplayServiceBuildNumber(boolean displayServiceBuildNumber) {
		this.displayServiceBuildNumber = displayServiceBuildNumber;
	}

	public boolean isDisplaySVNRevision() {
		return displaySVNRevision;
	}

	public void setDisplaySVNRevision(boolean displaySVNRevision) {
		this.displaySVNRevision = displaySVNRevision;
	}
	
	public boolean isDisplayVersion() {
		return displayVersion;
	}

	public void setDisplayVersion(boolean displayVersion) {
		this.displayVersion = displayVersion;
	}	

}
