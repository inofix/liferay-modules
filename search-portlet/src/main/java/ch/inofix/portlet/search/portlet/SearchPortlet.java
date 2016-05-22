
package ch.inofix.portlet.search.portlet;

import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.search.util.SearchUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2015-04-25 16:02
 * @modified 2015-05-22 13:14
 * @version 1.0.3
 */
public class SearchPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(SearchPortlet.class.getName());

    @Override
    public void serveResource(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws IOException, PortletException {

        PortletPreferences portletPreferences =
            resourceRequest.getPreferences();

        boolean useModel =
            GetterUtil.getBoolean(portletPreferences.getValue(
                "useModel", "false"));

        String className = ParamUtil.getString(resourceRequest, "className");

        Indexer indexer = IndexerRegistryUtil.getIndexer(className);

        HttpServletRequest request =
            PortalUtil.getHttpServletRequest(resourceRequest);

        SearchContext searchContext = SearchContextFactory.getInstance(request);
        
        String json = "[]";
        try {
            
            Hits hits = indexer.search(searchContext);   
            
            if (_log.isDebugEnabled()) {
                _log.debug("hits.getLength = " + hits.getLength());
            }
            
            json = SearchUtil.toJSON(hits, useModel);
            
        }
        catch (SearchException e) {
            _log.error(e);
        }

        PortletResponseUtil.write(resourceResponse, json);

    }
}
