
package ch.inofix.portlet.mapsearch.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2016-07-23 16:05
 * @modified 2016-07-23 16:05
 * @version 1.0.0
 */
public class MapSearchPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(MapSearchPortlet.class.getName());

    @Override
    public void serveResource(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws IOException, PortletException {

        PortletPreferences portletPreferences =
            resourceRequest.getPreferences();

        _log.info(portletPreferences.getValue(
            "classNames", JournalArticle.class.getName()));

        String className = ParamUtil.getString(resourceRequest, "className");

        Indexer indexer = IndexerRegistryUtil.getIndexer(className);

        HttpServletRequest request =
            PortalUtil.getHttpServletRequest(resourceRequest);

        SearchContext searchContext = SearchContextFactory.getInstance(request);

        String json = "[-37.8210922667, 175.2209316333, \"2\"],[-37.8210819833, 175.2213903167, \"3\"]";
//        String json = "[]";
        try {

            Hits hits = indexer.search(searchContext);

            _log.info("hits.getLength = " + hits.getLength());

            if (_log.isDebugEnabled()) {
                _log.debug("hits.getLength = " + hits.getLength());
            }

        }
        catch (SearchException e) {
            _log.error(e);
        }

        PortletResponseUtil.write(resourceResponse, json);

    }
}
