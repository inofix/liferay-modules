package ch.inofix.portlet.contact.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-24 22:14
 * @modified 2015-05-25 17:10
 * @version 1.0.2
 *
 */
public class ContactSearchTerms extends ContactDisplayTerms {

	public ContactSearchTerms(PortletRequest portletRequest) {
		super(portletRequest);

		contactId = DAOParamUtil.getString(portletRequest, CONTACT_ID);
		createDate = DAOParamUtil.getString(portletRequest, CREATE_DATE);
		emailHome = DAOParamUtil.getString(portletRequest, EMAIL_HOME);
		emailWork = DAOParamUtil.getString(portletRequest, EMAIL_WORK);
		fax = DAOParamUtil.getString(portletRequest, FAX);
		fullName = DAOParamUtil.getString(portletRequest, FULL_NAME);
		modifiedDate = DAOParamUtil.getString(portletRequest, MODIFIED_DATE);
		name = DAOParamUtil.getString(portletRequest, NAME);
		phoneHome = DAOParamUtil.getString(portletRequest, PHONE_HOME);
		phoneMobile = DAOParamUtil.getString(portletRequest, PHONE_MOBILE);
		phoneWork = DAOParamUtil.getString(portletRequest, PHONE_WORK);
		userName = DAOParamUtil.getString(portletRequest, USER_NAME);
	}

}
