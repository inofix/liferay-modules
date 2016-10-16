package ch.inofix.portlet.newsletter.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.newsletter.model.Mailing;
import ch.inofix.portlet.newsletter.util.PortletKey;

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
 * @created 2016-10-16 23:28
 * @modified 2016-10-16 23:28
 * @version 1.0.0
 *
 */
public class MailingSearch extends SearchContainer<Mailing> {

    static List<String> headerNames = new ArrayList<String>();
    static Map<String, String> orderableHeaders = new HashMap<String, String>();

    // The list of header names corresponds bean properties of
    // ch.inofix.portlet.contact.model.ContactImpl
    static {
        headerNames.add("create-date");
        headerNames.add("modified-date");
        headerNames.add("title");
        headerNames.add("user-name");

        orderableHeaders.put("create-date", "create-date");
        orderableHeaders.put("modified-date", "modified-date");
        orderableHeaders.put("title", "title");
        orderableHeaders.put("user-name", "user-name");
    }

    public static final String EMPTY_RESULTS_MESSAGE = "there-are-no-mailings";

    public MailingSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    public MailingSearch(PortletRequest portletRequest, String curParam,
            PortletURL iteratorURL) {

        super(portletRequest, new MailingDisplayTerms(portletRequest),
                new MailingSearchTerms(portletRequest), curParam,
                DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        MailingDisplayTerms displayTerms = (MailingDisplayTerms) getDisplayTerms();

        iteratorURL.setParameter(MailingDisplayTerms.CREATE_DATE,
                String.valueOf(displayTerms.getCreateDate()));
        iteratorURL.setParameter(MailingDisplayTerms.MODIFIED_DATE,
                String.valueOf(displayTerms.getModifiedDate()));
        iteratorURL.setParameter(MailingDisplayTerms.TITLE,
                String.valueOf(displayTerms.getTitle()));
        iteratorURL.setParameter(MailingDisplayTerms.USER_NAME,
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

                preferences.setValue(PortletKey.NEWSLETTER,
                        "mailings-order-by-col", orderByCol);
                preferences.setValue(PortletKey.NEWSLETTER,
                        "mailings-order-by-type", orderByType);
            } else {
                orderByCol = preferences.getValue(PortletKey.NEWSLETTER,
                        "mailings-order-by-col", "modified-date");
                orderByType = preferences.getValue(PortletKey.NEWSLETTER,
                        "mailings-order-by-type", "desc");
            }

            setOrderableHeaders(orderableHeaders);
            setOrderByCol(orderByCol);
            setOrderByType(orderByType);

        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static Log _log = LogFactoryUtil.getLog(MailingSearch.class);

}
