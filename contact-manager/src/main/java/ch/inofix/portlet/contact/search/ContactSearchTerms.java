package ch.inofix.portlet.contact.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-24 22:14
 * @modified 2015-05-24 22:14
 * @version 1.0.0
 *
 */
public class ContactSearchTerms extends ContactDisplayTerms {

	public ContactSearchTerms(PortletRequest portletRequest) {
		super(portletRequest);

		createDate = DAOParamUtil.getString(portletRequest, CREATE_DATE);
		fullName = DAOParamUtil.getString(portletRequest, FULL_NAME);
		modifiedDate = DAOParamUtil.getString(portletRequest, MODIFIED_DATE);
		userName = DAOParamUtil.getString(portletRequest, USER_NAME);
	}

}
