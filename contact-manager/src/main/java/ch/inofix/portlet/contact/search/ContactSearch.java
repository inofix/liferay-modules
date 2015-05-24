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
 * @modified 2015-05-24 22:01
 * @version 1.0.0
 *
 */
public class ContactSearch extends SearchContainer<Contact> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();

	static {
		headerNames.add("create-date");
		headerNames.add("full-name");
		headerNames.add("modified-date");
		headerNames.add("user-name");

		orderableHeaders.put("create-date", "create-date");
		orderableHeaders.put("full-name", "full-name");
		orderableHeaders.put("modified-date", "modified-date");
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

		iteratorURL.setParameter(ContactDisplayTerms.CREATE_DATE,
				String.valueOf(displayTerms.getCreateDate()));
		iteratorURL.setParameter(ContactDisplayTerms.FULL_NAME,
				String.valueOf(displayTerms.getFullName()));
		iteratorURL.setParameter(ContactDisplayTerms.MODIFIED_DATE,
				String.valueOf(displayTerms.getModifiedDate()));
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

			// OrderByComparator orderByComparator =
			// UsersAdminUtil.getUserOrderByComparator(
			// orderByCol, orderByType);

			// setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			// setOrderByComparator(orderByComparator);

		} catch (Exception e) {
			log.error(e);
		}
	}

	private static Log log = LogFactoryUtil.getLog(ContactSearch.class);

}
