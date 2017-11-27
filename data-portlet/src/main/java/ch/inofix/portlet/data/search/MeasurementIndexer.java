package ch.inofix.portlet.data.search;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.portlet.PortletURL;

import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;
import ch.inofix.portlet.data.service.persistence.MeasurementActionableDynamicQuery;
import ch.inofix.portlet.data.util.DataManagerFields;

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
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * @author Christian Berndt
 * @created 2017-03-13 15:52
 * @modified 2017-11-20 15:17
 * @version 1.0.5
 */
public class MeasurementIndexer extends BaseIndexer {

    public static final String[] CLASS_NAMES = { Measurement.class.getName() };
    public static final String PORTLET_ID = "datamanager_WAR_datamanager";

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
        
        _log.info("doDelete()");

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

        document.addKeywordSortable(DataManagerFields.ID, measurement.getId());

        document.addKeyword(DataManagerFields.NAME, measurement.getName());
        if (measurement.getTimestamp() != null) {
            document.addNumber(DataManagerFields.TIMESTAMP, measurement
                    .getTimestamp().getTime());
        }
        document.addKeyword(DataManagerFields.UNIT, measurement.getUnit());
        document.addKeyword(DataManagerFields.VALUE, measurement.getValue());

        // Store the jsonObject's properties
        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(measurement
                .getData());

        JSONArray names = jsonObject.names();

        for (int i = 0; i < names.length(); i++) {

            String name = names.getString(i);
            String value = jsonObject.getString(name);

            document.addKeyword("json_" + name, value);

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

    @Override
    protected void postProcessFullQuery(BooleanQuery fullQuery,
            SearchContext searchContext) throws Exception {

        long from = GetterUtil.getLong(searchContext.getAttribute("from"));
        long until = GetterUtil.getLong(searchContext.getAttribute("until"));

        if (until == 0) {
            until = Long.MAX_VALUE;
        }

        BooleanQuery booleanQuery = BooleanQueryFactoryUtil
                .create(searchContext);

        booleanQuery.addNumericRangeTerm("timestamp_sortable", from, until);
        fullQuery.add(booleanQuery, BooleanClauseOccur.MUST);

    };

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

//                _log.info("measurementId = " + measurement.getMeasurementId());
//                _log.info("data = " + measurement.getData());
//
//                ServiceContext serviceContext = new ServiceContext();
//                serviceContext.setCompanyId(measurement.getCompanyId());
//                serviceContext.setScopeGroupId(measurement.getGroupId());
//                serviceContext.setUserId(measurement.getUserId());
//
//                String json = measurement.getData();
//                JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);
//
//                String id = jsonObject.getString(DataManagerFields.ID);
//                String name = jsonObject.getString(DataManagerFields.NAME);
//                String timestamp = jsonObject.getString(DataManagerFields.TIMESTAMP);
//                String unit = jsonObject.getString(DataManagerFields.UNIT);
//                String value = jsonObject.getString(DataManagerFields.VALUE);
//
////                JSONObject dataObject = JSONFactoryUtil.createJSONObject();
////                dataObject.put(DataManagerFields.ID, id);
////                dataObject.put(DataManagerFields.NAME, name);
////                dataObject.put(DataManagerFields.TIMESTAMP, timestamp);
////                dataObject.put(DataManagerFields.UNIT, unit);
////                dataObject.put(DataManagerFields.VALUE, value);
//
//                Date date = getDate(timestamp);
//                
//                _log.info("timestamp = " + timestamp);
//                _log.info("date = " + date);
//
//                try {
//                    MeasurementLocalServiceUtil.updateMeasurement(
//                            measurement.getMeasurementId(),
//                            measurement.getUserId(), jsonObject.toString(), id,
//                            name, date, unit, value, serviceContext);
//                } catch (PortalException e) {
//                    _log.error(e.getMessage());
//                    
//                } catch (SystemException e) {
//                    _log.error(e.getMessage());
//                }

                Document document = getDocument(measurement);

                documents.add(document);
                
//                try {
//                    doDelete(measurement);
//                } catch (Exception e) {
//                    _log.error(e.getMessage());
//                }

            }

        };

        actionableDynamicQuery.setCompanyId(companyId);

        actionableDynamicQuery.performActions();

        SearchEngineUtil.updateDocuments(getSearchEngineId(), companyId,
                documents);

    }
    
    
    private Date getDate(String str) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date date = null;

        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            _log.error(e.getMessage());
        }

        return date;
    }
    
    private static final Log _log = LogFactoryUtil
            .getLog(MeasurementIndexer.class);

}
