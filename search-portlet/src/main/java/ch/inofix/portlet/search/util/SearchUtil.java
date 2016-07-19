package ch.inofix.portlet.search.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

/**
 * @author Christian Berndt
 * @created 2016-05-20 16:34
 * @modified 2016-07-19 21:46
 * @version 1.0.1
 */
public class SearchUtil {

    // Enable logging for this class
    private static final Log _log = LogFactoryUtil.getLog(SearchUtil.class
            .getName());

    /**
     * Return the hits' documents as an array of json-objects.
     * 
     * @param hits
     * @return the hits' documents as an array of json-objects.
     * @throws ClassNotFoundException
     * @since 1.0.0
     */
    public static final String toJSON(Hits hits) {

        return toJSON(hits, false);
    }

    /**
     * Return the hits' documents as an array of json-objects.
     * 
     * @param hits
     * @return the hits' documents as an array of json-objects.
     * @since 1.0.0
     */
    public static final String toJSON(Hits hits, boolean useModel) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        // Disable JSON serializer on methods configured with the IgnoreMixin
        // class.
        objectMapper.addMixIn(PersistedModel.class, IgnoreMixin.class);

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        if (_log.isDebugEnabled()) {
            _log.debug("Start toJson.");
        }

        List<Document> documents = hits.toList();
        Iterator<Document> iterator = documents.iterator();

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        while (iterator.hasNext()) {

            Object obj = null;

            Document document = iterator.next();

            if (useModel) {

                String entryClassName = document.get(Field.ENTRY_CLASS_NAME);
                long entryClassPK = GetterUtil.getLong(document
                        .get(Field.ENTRY_CLASS_PK));

                if (JournalArticle.class.getName().equals(entryClassName)) {

                    String articleId = document.get("articleId");
                    long groupId = GetterUtil.getLong(document.get("groupId"));

                    try {
                        obj = JournalArticleLocalServiceUtil.getArticle(
                                groupId, articleId);

                    } catch (PortalException e) {
                        _log.error(e);
                    } catch (SystemException e) {
                        _log.error(e);
                    }
                } else if (DLFileEntry.class.getName().equals(entryClassName)) {

                    try {

                        obj = DLFileEntryLocalServiceUtil
                                .getFileEntry(entryClassPK);
                    } catch (PortalException e) {
                        _log.error(e);
                    } catch (SystemException e) {
                        _log.error(e);
                    }
                }

                // TODO: check, whether we need more custom Object loader
                // methods for liferay default model classes.

                else {

                    // Generic method invocation for any other (ServiceBuilder
                    // generated) model.

                    String serviceClassName = entryClassName.replace("model",
                            "service");
                    serviceClassName = serviceClassName + "LocalServiceUtil";

                    try {
                        Class<?> modelClass = Class.forName(entryClassName);
                        Class<?> serviceClass = Class.forName(serviceClassName);

                        String simpleName = modelClass.getSimpleName();
                        Method method = serviceClass.getMethod("get"
                                + simpleName, long.class);
                        obj = method.invoke(null, entryClassPK);

                    } catch (SecurityException e) {
                        _log.error(e);
                    } catch (NoSuchMethodException e) {
                        _log.error(e);
                    } catch (IllegalArgumentException e) {
                        _log.error(e);
                    } catch (IllegalAccessException e) {
                        _log.error(e);
                    } catch (InvocationTargetException e) {
                        _log.error(e);
                    } catch (ClassNotFoundException e) {
                        _log.error(e);
                    }

                    // Use the document, if invocation of the default get-method
                    // fails.
                    obj = document;
                }
            }

            else {
                obj = document;
            }

            try {

                String json = objectMapper.writeValueAsString(obj);

                sb.append(json);
                if (iterator.hasNext()) {
                    sb.append(",");
                    sb.append("\n");
                }
            } catch (JsonProcessingException e) {
                _log.error(e);
            }

        }

        sb.append("]");

        if (_log.isDebugEnabled()) {
            _log.debug("toJSON took " + stopWatch.getTime() + " ms");
        }

        return sb.toString();

    }
}
