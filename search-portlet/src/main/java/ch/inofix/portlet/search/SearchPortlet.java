
package ch.inofix.portlet.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2015-04-25 16:02
 * @modified 2015-04-26 13:45
 * @version 1.0.1
 */
public class SearchPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(SearchPortlet.class.getName());

    @Override
    public void serveResource(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws IOException, PortletException {

        _log.info("serveResource");

        _log.info(resourceRequest.getResourceID());

        String className = ParamUtil.getString(resourceRequest, "className");

        _log.info("className = " + className);
                
        Indexer indexer = IndexerRegistryUtil.getIndexer(className);

        HttpServletRequest request =
            PortalUtil.getHttpServletRequest(resourceRequest);

        SearchContext searchContext = SearchContextFactory.getInstance(request);

        List<Document> documents = new ArrayList<Document>();

        StringBuilder sb = new StringBuilder();

        try {

            Hits hits = indexer.search(searchContext);

            _log.info("hits.getLength() = " + hits.getLength());

            documents = hits.toList();
            
            ObjectMapper objectMapper = new ObjectMapper(); 

            for (Document document : documents) {
                
                _log.info(objectMapper.writeValueAsString(document));
                _log.info(document.toString());
                sb.append(document); 
            }

        }
        catch (SearchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PortletResponseUtil.write(resourceResponse, sb.toString());

    }

}
