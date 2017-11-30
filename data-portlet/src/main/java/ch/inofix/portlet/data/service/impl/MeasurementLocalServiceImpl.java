package ch.inofix.portlet.data.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.data.MeasurementIdException;
import ch.inofix.portlet.data.MeasurementNameException;
import ch.inofix.portlet.data.MeasurementTimestampException;
import ch.inofix.portlet.data.MeasurementValueException;
import ch.inofix.portlet.data.backgroundtask.MeasurementImportBackgroundTaskExecutor;
import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.base.MeasurementLocalServiceBaseImpl;
import ch.inofix.portlet.data.util.DataManagerFields;
import ch.inofix.portlet.data.util.MeasurementImporter;

import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.SimpleFacet;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.User;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the measurement local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.data.service.MeasurementLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2017-03-08 19:46
 * @modified 2017-11-30 15:33
 * @version 1.1.3
 * @see ch.inofix.portlet.data.service.base.MeasurementLocalServiceBaseImpl
 * @see ch.inofix.portlet.data.service.MeasurementLocalServiceUtil
 */
public class MeasurementLocalServiceImpl extends
        MeasurementLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.data.service.MeasurementLocalServiceUtil} to access the
     * measurement local service.
     */

    @Override
    public Measurement addMeasurement(long userId, String data, String id,
            String name, Date timestamp, String unit, String value,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        // Measurement

        User user = userPersistence.findByPrimaryKey(userId);

        validate(id, name, timestamp, value);

        long groupId = serviceContext.getScopeGroupId();

        long measurementId = counterLocalService.increment();

        Measurement measurement = measurementPersistence.create(measurementId);

        measurement.setUuid(serviceContext.getUuid());
        measurement.setGroupId(groupId);
        measurement.setCompanyId(user.getCompanyId());
        measurement.setUserId(user.getUserId());
        measurement.setUserName(user.getFullName());
        measurement.setExpandoBridgeAttributes(serviceContext);

        measurement.setData(data);
        measurement.setId(id);
        measurement.setName(name);
        measurement.setTimestamp(timestamp);
        measurement.setUnit(unit);
        measurement.setValue(value);

        measurementPersistence.update(measurement);

        // Asset

        updateAsset(userId, measurement, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        // Indexing

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Measurement.class);
        indexer.reindex(measurement);

        return measurement;

    }

    @Override
    public List<Measurement> deleteGroupMeasurements(long groupId)
            throws PortalException, SystemException {

        List<Measurement> measurements = measurementPersistence
                .findByGroupId(groupId);

        for (Measurement measurement : measurements) {
            deleteMeasurement(measurement);
        }

        return measurements;

    }

    @Override
    public Measurement deleteMeasurement(Measurement measurement)
            throws PortalException, SystemException {

        // Measurement

        measurementPersistence.remove(measurement);

        // Indexer

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Measurement.class);
        indexer.delete(measurement);

        return measurement;
    }

    @Override
    public Measurement deleteMeasurement(long measurementId)
            throws PortalException, SystemException {

        Measurement measurement = measurementPersistence
                .findByPrimaryKey(measurementId);

        return measurementLocalService.deleteMeasurement(measurement);
    }

    public List<Measurement> deleteMeasurementsById(long companyId,
            long groupId, String id) throws PortalException, SystemException {

        Hits hits = search(companyId, groupId, id, Long.MIN_VALUE,
                Long.MAX_VALUE, true, 0, Integer.MAX_VALUE, null);

        List<Document> documents = hits.toList();

        List<Measurement> measurements = new ArrayList<Measurement>();

        for (Document document : documents) {

            long classPK = GetterUtil.getLong(document.get("entryClassPK"));

            deleteMeasurement(classPK);

        }

        return measurements;

    }

    @Override
    public Measurement getMeasurement(long measurementId)
            throws PortalException, SystemException {
        return measurementPersistence.findByPrimaryKey(measurementId);
    }

    /**
     * Imports the measurements from the file.
     *
     * @param userId
     *            the primary key of the user
     * @param groupId
     *            the primary key of the group
     * @param privateLayout
     *            whether the layout is private to the group
     * @param parameterMap
     *            the mapping of parameters indicating which information will be
     *            imported.
     * @param file
     *            the file with the data
     * @since 1.0.1
     * @throws PortalException
     *             if a group or user with the primary key could not be found,
     *             or if some other portal exception occurred
     * @throws SystemException
     *             if a system exception occurred
     * @see com.liferay.portal.lar.LayoutImporter
     */
    @Override
    public void importMeasurements(long userId, long groupId,
            Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        try {
            MeasurementImporter measurementImporter = new MeasurementImporter();

            measurementImporter.importMeasurements(userId, groupId,
                    parameterMap, file);

        } catch (PortalException pe) {
            Throwable cause = pe.getCause();

            if (cause instanceof LocaleException) {
                throw (PortalException) cause;
            }

            _log.error(pe);

            throw pe;
        } catch (SystemException se) {

            _log.error(se);

            throw se;
        } catch (Exception e) {

            _log.error(e);

            throw new SystemException(e);
        }
    }

    /**
     * Imports the measurements from the input stream.
     *
     * @param userId
     *            the primary key of the user
     * @param groupId
     *            the primary key of the group
     * @param parameterMap
     *            the mapping of parameters indicating which information will be
     *            imported.
     * @param is
     *            the input stream
     * @since 1.0.1
     * @throws PortalException
     *             if a group or user with the primary key could not be found,
     *             or if some other portal exception occurred
     * @throws SystemException
     *             if a system exception occurred
     */
    @Override
    public void importMeasurements(long userId, long groupId,
            Map<String, String[]> parameterMap, InputStream is, String extension)
            throws PortalException, SystemException {

        File file = null;

        try {
            file = FileUtil.createTempFile(extension);

            FileUtil.write(file, is);

            importMeasurements(userId, groupId, parameterMap, file);

        } catch (IOException ioe) {

            _log.error(ioe);

            throw new SystemException(ioe);
        } finally {
            FileUtil.delete(file);
        }
    }

    /**
     * @param userId
     * @param taskName
     * @param groupId
     * @param privateLayout
     * @param parameterMap
     * @param file
     * @return
     * @since 1.0.1
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public long importMeasurementsInBackground(long userId, String taskName,
            long groupId, Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        String extension = FileUtil.getExtension(file.getName());

        Map<String, Serializable> taskContextMap = new HashMap<String, Serializable>();
        taskContextMap.put("userId", userId);
        taskContextMap.put("groupId", groupId);
        taskContextMap.put("parameterMap", (Serializable) parameterMap);
        taskContextMap.put("extension", extension);

        String[] servletContextNames = parameterMap.get("servletContextNames");

        BackgroundTask backgroundTask = BackgroundTaskLocalServiceUtil
                .addBackgroundTask(userId, groupId, taskName,
                        servletContextNames,
                        MeasurementImportBackgroundTaskExecutor.class,
                        taskContextMap, new ServiceContext());

        BackgroundTaskLocalServiceUtil.addBackgroundTaskAttachment(userId,
                backgroundTask.getBackgroundTaskId(), taskName, file);

        return backgroundTask.getBackgroundTaskId();

    }

    /**
     * Returns an ordered range of all the measurements whose id, or timestamp
     * fields match the keywords specified for them, using the indexer.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end -
     * start</code> instances. <code>start</code> and <code>end</code> are not
     * primary keys, they are indexes in the result set. Thus, <code>0</code>
     * refers to the first result in the set. Setting both <code>start</code>
     * and <code>end</code> to
     * {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return
     * the full result set.
     * </p>
     *
     *
     * @param companyId
     * @param id
     * @param timestamp
     * @param andSearch
     * @param start
     * @param end
     * @param sort
     * @return
     * @throws SystemException
     */
    @Override
    public Hits search(long companyId, long groupId, String id, Date timestamp,
            boolean andSearch, int start, int end, Sort sort)
            throws SystemException {

        return search(companyId, groupId, id, timestamp, Long.MIN_VALUE,
                Long.MAX_VALUE, andSearch, start, end, sort);

    }

    /**
     *
     * @param companyId
     * @param groupId
     * @param id
     * @param from
     * @param until
     * @param andSearch
     * @param start
     * @param end
     * @param sort
     * @return
     * @throws SystemException
     */
    @Override
    public Hits search(long companyId, long groupId, String id, long from,
            long until, boolean andSearch, int start, int end, Sort sort)
            throws SystemException {

        return search(companyId, groupId, id, null, from, until, andSearch,
                start, end, sort);
    }

    /**
     * Returns an ordered range of all the measurements whose id, or timestamp
     * fields match the keywords specified for them, using the indexer.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end -
     * start</code> instances. <code>start</code> and <code>end</code> are not
     * primary keys, they are indexes in the result set. Thus, <code>0</code>
     * refers to the first result in the set. Setting both <code>start</code>
     * and <code>end</code> to
     * {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return
     * the full result set.
     * </p>
     *
     *
     * @param companyId
     * @param id
     * @param timestamp
     * @param from
     * @param until
     * @param andSearch
     * @param start
     * @param end
     * @param sort
     * @return
     * @throws SystemException
     */
    @Override
    public Hits search(long companyId, long groupId, String id, Date timestamp,
            long from, long until, boolean andSearch, int start, int end,
            Sort sort) throws SystemException {
        
        if (_log.isDebugEnabled()) {
            _log.debug("search()");
            _log.debug("sort = " + sort); 
        }
        
        if (Validator.isNull(sort)) {
            sort = new Sort(DataManagerFields.TIMESTAMP, true);
        }
              
        try {

            SearchContext searchContext = new SearchContext();

            searchContext.setCompanyId(companyId);
            searchContext.setGroupIds(new long[] { groupId });

            searchContext.setAttribute("paginationType", "more");
            searchContext.setStart(start);
            searchContext.setEnd(end);

            searchContext.setAndSearch(andSearch);

            Map<String, Serializable> attributes = new HashMap<String, Serializable>();

            if (Validator.isNotNull(id)) {
                attributes.put(DataManagerFields.ID, id);

            }

            if (timestamp != null) {
                attributes.put(DataManagerFields.TIMESTAMP,
                        String.valueOf(timestamp.getTime()));
            }
            attributes.put("from", from);
            attributes.put("until", until);

            searchContext.setAttributes(attributes);

            // Always add facets as late as possible so that the search context
            // fields can be considered by the facets

            List<Facet> facets = new ArrayList<Facet>();

            if (Validator.isNotNull(id)) {
                Facet facet = new SimpleFacet(searchContext);
                facet.setFieldName(DataManagerFields.ID);
                facets.add(facet);
            }

            if (Validator.isNotNull(timestamp)) {
                Facet facet = new MultiValueFacet(searchContext);
                facet.setFieldName(DataManagerFields.TIMESTAMP);
                facets.add(facet);
            }

            searchContext.setFacets(facets);
            searchContext.setSorts(sort);

            Indexer indexer = IndexerRegistryUtil
                    .nullSafeGetIndexer(Measurement.class);

            return indexer.search(searchContext);

        } catch (Exception e) {
            throw new SystemException(e);
        }

    }

    @Override
    public void updateAsset(long userId, Measurement measurement,
            long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds) throws PortalException, SystemException {

        boolean visible = true;
        // boolean visible = false;

        // if (measurement.isApproved()) {
        // visible = true;
        // }

        // TODO: What's the classTypeId?
        long classTypeId = 0;
        Date startDate = null;
        Date endDate = null;
        Date expirationDate = null;
        String mimeType = "text/x-vcard";
        String title = String.valueOf(measurement.getMeasurementId());
        String description = null;
        String summary = null;
        // TODO: What does url mean in this context?
        String url = null;
        // TODO: What does layoutUuid mean in this context?
        String layoutUuid = null;
        int height = 0;
        int width = 0;
        Integer priority = null;
        boolean sync = false;

        assetEntryLocalService.updateEntry(userId, measurement.getGroupId(),
                measurement.getCreateDate(), measurement.getModifiedDate(),
                Measurement.class.getName(), measurement.getMeasurementId(),
                measurement.getUuid(), classTypeId, assetCategoryIds,
                assetTagNames, visible, startDate, endDate, expirationDate,
                mimeType, title, description, summary, url, layoutUuid, height,
                width, priority, sync);

    }

    @Override
    public Measurement updateMeasurement(long measurementId, long userId,
            String data, String id, String name, Date timestamp, String unit,
            String value, ServiceContext serviceContext)
            throws PortalException, SystemException {

        // Measurement

        User user = userPersistence.findByPrimaryKey(userId);

        validate(id, name, timestamp, value);

        Measurement measurement = measurementPersistence
                .findByPrimaryKey(measurementId);

        long groupId = serviceContext.getScopeGroupId();

        measurement.setUuid(serviceContext.getUuid());
        measurement.setGroupId(groupId);
        measurement.setCompanyId(user.getCompanyId());
        measurement.setUserId(user.getUserId());
        measurement.setUserName(user.getFullName());
        measurement.setExpandoBridgeAttributes(serviceContext);

        measurement.setData(data);
        measurement.setId(id);
        measurement.setName(name);
        measurement.setTimestamp(timestamp);
        measurement.setUnit(unit);
        measurement.setValue(value);

        measurementPersistence.update(measurement);

        // Asset

        updateAsset(userId, measurement, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds());

        // Indexing

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Measurement.class);
        indexer.reindex(measurement);

        return measurement;

    }

    protected void validate(String id, String name, Date timestamp, String value)
            throws PortalException {

        if (Validator.isNull(id)) {
            throw new MeasurementIdException();
        }

        if (Validator.isNull(name)) {
            throw new MeasurementNameException();
        }

        if (Validator.isNull(timestamp)) {
            throw new MeasurementTimestampException();
        }

        if (Validator.isNull(value)) {
            throw new MeasurementValueException();
        }
    }

    private static final Log _log = LogFactoryUtil
            .getLog(MeasurementLocalServiceImpl.class);
}
