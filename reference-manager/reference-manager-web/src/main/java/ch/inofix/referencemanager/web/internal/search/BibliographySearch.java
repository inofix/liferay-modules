package ch.inofix.referencemanager.web.internal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import ch.inofix.referencemanager.model.Bibliography;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-30 18:23
 * @modified 2016-11-30 18:23
 * @version 1.0.0
 *
 */
public class BibliographySearch extends SearchContainer<Bibliography> {
    
    public static final String EMPTY_RESULTS_MESSAGE = "no-bibliographies-were-found";

    public static List<String> headerNames = new ArrayList<>();
    public static Map<String, String> orderableHeaders = new HashMap<>();

    static {
        headerNames.add("author");
        headerNames.add("title");
        headerNames.add("year");

        orderableHeaders.put("author", "author");
        orderableHeaders.put("title", "title");
        orderableHeaders.put("year", "year");

    }

    public BibliographySearch(PortletRequest portletRequest, PortletURL iteratorURL) {
        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    public BibliographySearch(PortletRequest portletRequest, String curParam, PortletURL iteratorURL) {

        super(portletRequest, new BibliographyDisplayTerms(portletRequest), new BibliographySearchTerms(portletRequest),
                curParam, DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);

        BibliographyDisplayTerms displayTerms = (BibliographyDisplayTerms) getDisplayTerms();
        BibliographySearchTerms searchTerms = (BibliographySearchTerms) getSearchTerms();

        String portletId = PortletProviderUtil.getPortletId(User.class.getName(), PortletProvider.Action.VIEW);
        String portletName = portletConfig.getPortletName();

        if (!portletId.equals(portletName)) {
            displayTerms.setStatus(WorkflowConstants.STATUS_APPROVED);
            searchTerms.setStatus(WorkflowConstants.STATUS_APPROVED);
        }

        iteratorURL.setParameter(BibliographyDisplayTerms.DESCRIPTION, displayTerms.getDescription());
        iteratorURL.setParameter(BibliographyDisplayTerms.STATUS, String.valueOf(displayTerms.getStatus()));
        iteratorURL.setParameter(BibliographyDisplayTerms.TITLE, displayTerms.getTitle());

        try {
            PortalPreferences preferences = PortletPreferencesFactoryUtil.getPortalPreferences(portletRequest);

            String orderByCol = ParamUtil.getString(portletRequest, "orderByCol");
            String orderByType = ParamUtil.getString(portletRequest, "orderByType");

            if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {

                preferences.setValue(portletId, "bibliographies-order-by-col", orderByCol);
                preferences.setValue(portletId, "bibliographies-order-by-type", orderByType);
            } else {
                orderByCol = preferences.getValue(portletId, "bibliographies-order-by-col", "last-name");
                orderByType = preferences.getValue(portletId, "bibliographies-order-by-type", "asc");
            }

            setOrderableHeaders(orderableHeaders);
            setOrderByCol(orderByCol);
            setOrderByType(orderByType);
            
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographySearch.class);

}
