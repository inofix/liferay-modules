package ch.inofix.portlet.newsletter.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.newsletter.model.Newsletter;
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
 * @created 2016-10-16 22:22
 * @modified 2016-10-16 22:22
 * @version 1.0.0
 *
 */
public class NewsletterSearch extends SearchContainer<Newsletter> {

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

    public static final String EMPTY_RESULTS_MESSAGE = "there-are-no-newsletters";

    public NewsletterSearch(PortletRequest portletRequest,
            PortletURL iteratorURL) {
        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    public NewsletterSearch(PortletRequest portletRequest, String curParam,
            PortletURL iteratorURL) {

        super(portletRequest, new NewsletterDisplayTerms(portletRequest),
                new NewsletterSearchTerms(portletRequest), curParam,
                DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        NewsletterDisplayTerms displayTerms = (NewsletterDisplayTerms) getDisplayTerms();

        iteratorURL.setParameter(NewsletterDisplayTerms.CREATE_DATE,
                String.valueOf(displayTerms.getCreateDate()));
        iteratorURL.setParameter(NewsletterDisplayTerms.MODIFIED_DATE,
                String.valueOf(displayTerms.getModifiedDate()));
        iteratorURL.setParameter(NewsletterDisplayTerms.TITLE,
                String.valueOf(displayTerms.getTitle()));
        iteratorURL.setParameter(NewsletterDisplayTerms.USER_NAME,
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
                        "newsletters-order-by-col", orderByCol);
                preferences.setValue(PortletKey.NEWSLETTER,
                        "newsletters-order-by-type", orderByType);
            } else {
                orderByCol = preferences.getValue(PortletKey.NEWSLETTER,
                        "newsletters-order-by-col", "modified-date");
                orderByType = preferences.getValue(PortletKey.NEWSLETTER,
                        "newsletters-order-by-type", "desc");
            }

            setOrderableHeaders(orderableHeaders);
            setOrderByCol(orderByCol);
            setOrderByType(orderByType);

        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static Log _log = LogFactoryUtil.getLog(NewsletterSearch.class);

}
