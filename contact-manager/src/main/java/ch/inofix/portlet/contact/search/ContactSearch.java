package ch.inofix.portlet.contact.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.contact.model.Contact;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-24 22:01
 * @modified 2015-05-25 17:11
 * @version 1.0.2
 *
 */
public class ContactSearch extends SearchContainer<Contact> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();

	// The list of header names corresponds bean properties of 
	// ch.inofix.portlet.contact.model.ContactImpl
	static {
		headerNames.add("contact-id");
		headerNames.add("create-date");
		headerNames.add("email-home");
		headerNames.add("email-work");
		// TODO: implement fax-work, fax-home in ContactImpl, ContactIndexer
//		headerNames.add("fax");
		headerNames.add("full-name");
		headerNames.add("modified-date");
		headerNames.add("name");
		headerNames.add("phone-home");
		headerNames.add("phone-mobile");
		headerNames.add("phone-work");
		headerNames.add("user-name");

		orderableHeaders.put("contact-id", "contact-id");
		orderableHeaders.put("create-date", "create-date");
		orderableHeaders.put("email-home", "email-home");
		orderableHeaders.put("email-work", "email-work");
//		orderableHeaders.put("fax", "fax");
		orderableHeaders.put("full-name", "full-name");
		orderableHeaders.put("modified-date", "modified-date");
		orderableHeaders.put("name", "name");
		orderableHeaders.put("phone-home", "phone-home");
		orderableHeaders.put("phone-mobile", "phone-mobile");
		orderableHeaders.put("phone-work", "phone-work");
		orderableHeaders.put("user-name", "user-name");
	}

	public static final String EMPTY_RESULTS_MESSAGE = "no-contacts-were-found";

	public ContactSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
		this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
	}

	public ContactSearch(PortletRequest portletRequest, String curParam,
			PortletURL iteratorURL) {

		super(portletRequest, new ContactDisplayTerms(portletRequest),
				new ContactSearchTerms(portletRequest), curParam,
				DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		ContactDisplayTerms displayTerms = (ContactDisplayTerms) getDisplayTerms();

		iteratorURL.setParameter(ContactDisplayTerms.CONTACT_ID,
				String.valueOf(displayTerms.getContactId()));
		iteratorURL.setParameter(ContactDisplayTerms.CREATE_DATE,
				String.valueOf(displayTerms.getCreateDate()));
		iteratorURL.setParameter(ContactDisplayTerms.EMAIL_HOME,
				String.valueOf(displayTerms.getEmailHome()));
		iteratorURL.setParameter(ContactDisplayTerms.EMAIL_WORK,
				String.valueOf(displayTerms.getEmailWork()));
		iteratorURL.setParameter(ContactDisplayTerms.FAX,
				String.valueOf(displayTerms.getFax()));
		iteratorURL.setParameter(ContactDisplayTerms.FULL_NAME,
				String.valueOf(displayTerms.getFullName()));
		iteratorURL.setParameter(ContactDisplayTerms.MODIFIED_DATE,
				String.valueOf(displayTerms.getModifiedDate()));
		iteratorURL.setParameter(ContactDisplayTerms.NAME,
				String.valueOf(displayTerms.getName()));
		iteratorURL.setParameter(ContactDisplayTerms.PHONE_HOME,
				String.valueOf(displayTerms.getPhoneHome()));
		iteratorURL.setParameter(ContactDisplayTerms.PHONE_MOBILE,
				String.valueOf(displayTerms.getPhoneMobile()));
		iteratorURL.setParameter(ContactDisplayTerms.PHONE_WORK,
				String.valueOf(displayTerms.getPhoneWork()));
		iteratorURL.setParameter(ContactDisplayTerms.USER_NAME,
				String.valueOf(displayTerms.getUserName()));

		try {
			PortalPreferences preferences = PortletPreferencesFactoryUtil
					.getPortalPreferences(portletRequest);

			String orderByCol = ParamUtil.getString(portletRequest,
					"orderByCol");
			String orderByType = ParamUtil.getString(portletRequest,
					"orderByType");

			if (Validator.isNotNull(orderByCol)
					&& Validator.isNotNull(orderByType)) {

				preferences.setValue("contactmanager_WAR_contactmanager",
						"contacts-order-by-col", orderByCol);
				preferences.setValue("contactmanager_WAR_contactmanager",
						"contacts-order-by-type", orderByType);
			} else {
				orderByCol = preferences.getValue(
						"contactmanager_WAR_contactmanager",
						"contacts-order-by-col", "last-name");
				orderByType = preferences.getValue(
						"contactmanager_WAR_contactmanager",
						"contacts-order-by-type", "asc");
			}

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);

		} catch (Exception e) {
			log.error(e);
		}
	}

	private static Log log = LogFactoryUtil.getLog(ContactSearch.class);

}
