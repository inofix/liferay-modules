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
 * @modified 2016-03-16 20:51
 * @version 1.0.5
 *
 */
public class ContactSearch extends SearchContainer<Contact> {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(ContactSearch.class);

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();

	// The list of header names corresponds bean properties of
	// ch.inofix.portlet.contact.model.ContactImpl
	static {
		headerNames.add("company");
		headerNames.add("contact-id");
		headerNames.add("create-date");
		headerNames.add("email");
		// TODO: enable default fax
		// headerNames.add("fax");
		headerNames.add("full-name");
		// TODO: enable default impp
		// headerNames.add("impp");
		headerNames.add("modified-date");
		headerNames.add("name");
		headerNames.add("phone");
		headerNames.add("portrait");
		headerNames.add("status"); 
		headerNames.add("user-name");

		orderableHeaders.put("company", "company");
		orderableHeaders.put("contact-id", "contact-id");
		orderableHeaders.put("create-date", "create-date");
		orderableHeaders.put("email", "email");
		// TODO: enable default fax
		// orderableHeaders.put("fax", "fax");
		orderableHeaders.put("full-name", "full-name");
		// TODO: enable default impp
		// orderableHeaders.put("impp", "impp");
		orderableHeaders.put("modified-date", "modified-date");
		orderableHeaders.put("name", "name");
		orderableHeaders.put("phone", "phone");
		orderableHeaders.put("status", "status");
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

		iteratorURL.setParameter(ContactDisplayTerms.COMPANY,
				String.valueOf(displayTerms.getCompany()));
		iteratorURL.setParameter(ContactDisplayTerms.CONTACT_ID,
				String.valueOf(displayTerms.getContactId()));
		iteratorURL.setParameter(ContactDisplayTerms.CREATE_DATE,
				String.valueOf(displayTerms.getCreateDate()));
		iteratorURL.setParameter(ContactDisplayTerms.EMAIL,
				String.valueOf(displayTerms.getEmail()));
		iteratorURL.setParameter(ContactDisplayTerms.FAX,
				String.valueOf(displayTerms.getFax()));
		iteratorURL.setParameter(ContactDisplayTerms.FULL_NAME,
				String.valueOf(displayTerms.getFullName()));
		// TODO: add default impp
		iteratorURL.setParameter(ContactDisplayTerms.MODIFIED_DATE,
				String.valueOf(displayTerms.getModifiedDate()));
		iteratorURL.setParameter(ContactDisplayTerms.NAME,
				String.valueOf(displayTerms.getName()));
		iteratorURL.setParameter(ContactDisplayTerms.PHONE,
				String.valueOf(displayTerms.getPhone()));
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

				preferences.setValue("contactmanagerportlet_WAR_contactmanagerportlet",
						"contacts-order-by-col", orderByCol);
				preferences.setValue("contactmanagerportlet_WAR_contactmanagerportlet",
						"contacts-order-by-type", orderByType);
			} else {
				orderByCol = preferences.getValue(
						"contactmanagerportlet_WAR_contactmanagerportlet",
						"contacts-order-by-col", "last-name");
				orderByType = preferences.getValue(
						"contactmanagerportlet_WAR_contactmanagerportlet",
						"contacts-order-by-type", "asc");
			}

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);

		} catch (Exception e) {
			log.error(e);
		}
	}

}
