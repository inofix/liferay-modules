package ch.inofix.portlet.contact.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-24 22:12
 * @modified 2015-05-24 22:17
 * @version 1.0.0
 *
 */
public class ContactDisplayTerms extends DisplayTerms {

	public static final String CREATE_DATE = "createDate";
	public static final String FULL_NAME = "fullName";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String USER_NAME = "userName";

	public ContactDisplayTerms(PortletRequest portletRequest) {
		super(portletRequest);

		createDate = ParamUtil.getString(portletRequest, CREATE_DATE);
		fullName = ParamUtil.getString(portletRequest, FULL_NAME);
		modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
		userName = ParamUtil.getString(portletRequest, USER_NAME);
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected String createDate;
	protected String fullName;
	protected String modifiedDate;
	protected String userName;

}
