
package ch.inofix.portlet.search.portlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2015-04-25 16:02
 * @modified 2015-05-12 20:47
 * @version 1.0.2
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

        boolean useDocument =
            GetterUtil.getBoolean(portletPreferences.getValue(
                "useDocument", "false"));

        _log.info("useDocument = " + useDocument);

        String className = ParamUtil.getString(resourceRequest, "className");

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
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            for (Document document : documents) {

                String entryClassName = document.get(Field.ENTRY_CLASS_NAME);

                AssetRendererFactory assetRendererFactory =
                    AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(entryClassName);

                AssetRenderer assetRenderer = null;

                if (assetRendererFactory != null) {

                    long entryClassPK =
                        GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

                    long resourcePrimKey =
                        GetterUtil.getLong(document.get(Field.ROOT_ENTRY_CLASS_PK));

                    if (resourcePrimKey > 0) {
                        entryClassPK = resourcePrimKey;
                    }

                    assetRenderer =
                        assetRendererFactory.getAssetRenderer(entryClassPK);
                }

                long classPK = assetRenderer.getClassPK();

                String serviceClassName = className.replace("model", "service");
                serviceClassName = serviceClassName + "LocalServiceUtil";

                Class<?> modelClass = Class.forName(className);
                Class<?> serviceClass = Class.forName(serviceClassName);

                String simpleName = modelClass.getSimpleName();

                Object obj = null;

                if (JournalArticle.class.getName().equals(className)) {
                                        
                    String articleId = document.get("articleId"); 
                    long groupId = GetterUtil.getLong(document.get("groupId")); 

                    obj = JournalArticleLocalServiceUtil.getArticle(groupId, articleId);

                }
                else {

                    // Generic method invocation for any other model. (Works
                    // only with ServiceBuilder generated classes).
                    Method get =
                        serviceClass.getMethod("get" + simpleName, long.class);

                    obj = get.invoke(null, classPK);

                }
                _log.info(obj);

                String json = null;

                if (useDocument) {
                    json = objectMapper.writeValueAsString(document);
                }
                else {
                    json = objectMapper.writeValueAsString(obj);
                }

                _log.info(json);

                sb.append(json);
            }
        }

        catch (SearchException se) {
            _log.error(se);
        }
        catch (ClassNotFoundException cnfe) {
            _log.error(cnfe);
        }
        catch (SecurityException se) {
            _log.error(se);
        }
        catch (NoSuchMethodException nsme) {
            _log.error(nsme);
        }
        catch (IllegalArgumentException e) {
            _log.error(e);
        }
        catch (IllegalAccessException e) {
            _log.error(e);
        }
        catch (InvocationTargetException e) {
            _log.error(e);
        }
        catch (PortalException e) {
            _log.error(e);
        }
        catch (SystemException e) {
            _log.error(e);
        }

        PortletResponseUtil.write(resourceResponse, sb.toString());

    }
}
