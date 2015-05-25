package ch.inofix.portlet.contact.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-24 22:12
 * @modified 2015-05-26 17:09
 * @version 1.0.2
 *
 */
public class ContactDisplayTerms extends DisplayTerms {

	public static final String CONTACT_ID = "contactId";
	public static final String CREATE_DATE = "createDate";
	public static final String EMAIL_HOME = "emailHome";
	public static final String EMAIL_WORK = "emailWork";
	public static final String FAX = "fax";
	public static final String FULL_NAME = "fullName";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String NAME = "name";
	public static final String PHONE_HOME = "phoneHome";
	public static final String PHONE_MOBILE = "phoneMobile";
	public static final String PHONE_WORK = "phoneWork";
	public static final String USER_NAME = "userName";

	public ContactDisplayTerms(PortletRequest portletRequest) {
		super(portletRequest);

		contactId = ParamUtil.getString(portletRequest, CONTACT_ID);
		createDate = ParamUtil.getString(portletRequest, CREATE_DATE);
		emailHome = ParamUtil.getString(portletRequest, EMAIL_HOME);
		emailWork = ParamUtil.getString(portletRequest, EMAIL_WORK);
		fax = ParamUtil.getString(portletRequest, FAX);
		fullName = ParamUtil.getString(portletRequest, FULL_NAME);
		name = ParamUtil.getString(portletRequest, NAME);
		modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
		phoneHome = ParamUtil.getString(portletRequest, PHONE_HOME);
		phoneMobile = ParamUtil.getString(portletRequest, PHONE_MOBILE);
		phoneWork = ParamUtil.getString(portletRequest, PHONE_WORK);
		userName = ParamUtil.getString(portletRequest, USER_NAME);
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEmailHome() {
		return emailHome;
	}

	public void setEmailHome(String emailHome) {
		this.emailHome = emailHome;
	}

	public String getEmailWork() {
		return emailWork;
	}

	public void setEmailWork(String emailWork) {
		this.emailWork = emailWork;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getPhoneWork() {
		return phoneWork;
	}

	public void setPhoneWork(String phoneWork) {
		this.phoneWork = phoneWork;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected String contactId;
	protected String createDate;
	protected String emailHome;
	protected String emailWork;
	protected String fax;
	protected String fullName;
	protected String modifiedDate;
	protected String name;
	protected String phoneHome;
	protected String phoneMobile;
	protected String phoneWork;
	protected String userName;

}
