package ch.inofix.portlet.data.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.portlet.PortletURL;

import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;
import ch.inofix.portlet.data.service.persistence.MeasurementActionableDynamicQuery;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author Christian Berndt
 * @created 2017-03-13 15:52
 * @modified 2017-03-31 23:17
 * @version 1.0.2
 */
public class MeasurementIndexer extends BaseIndexer {

    public static final String[] CLASS_NAMES = { Measurement.class.getName() };
    public static final String PORTLET_ID = "datamanager";

    @Override
    public String[] getClassNames() {
        return CLASS_NAMES;
    }

    @Override
    public String getPortletId() {
        return PORTLET_ID;
    }

    @Override
    protected void doDelete(Object obj) throws Exception {
        Measurement measurement = (Measurement) obj;

        deleteDocument(measurement.getCompanyId(),
                measurement.getMeasurementId());
    }

    @Override
    protected Document doGetDocument(Object obj) throws Exception {
        Measurement measurement = (Measurement) obj;

        Document document = getBaseModelDocument(PORTLET_ID, measurement);

        // Set document field values (in alphabetical order)

        document.addText(Field.CONTENT, measurement.getData());
        document.addKeyword(Field.GROUP_ID,
                getSiteGroupId(measurement.getGroupId()));
        document.addDate(Field.MODIFIED_DATE, measurement.getModifiedDate());
        document.addKeyword(Field.SCOPE_GROUP_ID, measurement.getGroupId());

        document.addKeyword("measurementId", measurement.getMeasurementId());
        document.addKeyword(Field.USER_ID, measurement.getUserId());

        // Store the jsonObject's properties
        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(measurement
                .getData());

        JSONArray names = jsonObject.names();

        for (int i = 0; i < names.length(); i++) {

            String name = names.getString(i);

            document.addKeyword(name, jsonObject.getString(name));

        }

        return document;
    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale,
            String snippet, PortletURL portletURL) throws Exception {

        Summary summary = createSummary(document);

        summary.setMaxContentLength(200);

        return summary;
    }

    @Override
    protected void doReindex(Object obj) throws Exception {
        Measurement measurement = (Measurement) obj;

        Document document = getDocument(measurement);

        SearchEngineUtil.updateDocument(getSearchEngineId(),
                measurement.getCompanyId(), document);

    }

    @Override
    protected void doReindex(String[] ids) throws Exception {
        long companyId = GetterUtil.getLong(ids[0]);

        reindexMeasurements(companyId);
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {
        Measurement measurement = MeasurementLocalServiceUtil
                .getMeasurement(classPK);

        doReindex(measurement);

    }

    @Override
    protected String getPortletId(SearchContext searchContext) {
        return PORTLET_ID;

    }

    protected void reindexMeasurements(long companyId) throws PortalException,
            SystemException {

        final Collection<Document> documents = new ArrayList<Document>();

        ActionableDynamicQuery actionableDynamicQuery = new MeasurementActionableDynamicQuery() {

            @Override
            protected void addCriteria(DynamicQuery dynamicQuery) {

            }

            @Override
            protected void performAction(Object object) throws PortalException {

                Measurement measurement = (Measurement) object;

                Document document = getDocument(measurement);

                documents.add(document);

            }

        };

        actionableDynamicQuery.setCompanyId(companyId);

        actionableDynamicQuery.performActions();

        SearchEngineUtil.updateDocuments(getSearchEngineId(), companyId,
                documents);

    }

    private static final Log _log = LogFactoryUtil
            .getLog(MeasurementIndexer.class.getName());

}
