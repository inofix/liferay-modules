package ch.inofix.portlet.data.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.data.backgroundtask.MeasurementImportBackgroundTaskExecutor;
import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.base.MeasurementLocalServiceBaseImpl;
import ch.inofix.portlet.data.util.MeasurementImporter;

import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.util.FileUtil;
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
 * @modified 2017-06-26 10:56
 * @version 1.0.9
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
    public Measurement addMeasurement(long userId, String data,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        // Measurement

        User user = userPersistence.findByPrimaryKey(userId);
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

        measurementPersistence.update(measurement);

        // Indexing

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Measurement.class);
        indexer.reindex(measurement);

        return measurement;

    }

    @Override
    public List<Measurement> deleteGroupMeasurements(long groupId)
            throws PortalException, SystemException {

        _log.info("deleteGroupMeasurements");

        List<Measurement> measurements = measurementPersistence
                .findByGroupId(groupId);

        _log.info("measurements.size() = " + measurements.size());

        for (Measurement measurement : measurements) {
            deleteMeasurement(measurement);

            _log.info("delete  " + measurement);

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
            boolean privateLayout, Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        try {
            MeasurementImporter measurementImporter = new MeasurementImporter();

            measurementImporter.importMeasurements(userId, groupId,
                    privateLayout, parameterMap, file);

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
     * @param privateLayout
     *            whether the layout is private to the group
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
            boolean privateLayout, Map<String, String[]> parameterMap,
            InputStream is, String extension) throws PortalException,
            SystemException {

        File file = null;

        try {
            file = FileUtil.createTempFile(extension);

            FileUtil.write(file, is);

            importMeasurements(userId, groupId, privateLayout, parameterMap,
                    file);

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
            long groupId, boolean privateLayout,
            Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        String extension = FileUtil.getExtension(file.getName());

        Map<String, Serializable> taskContextMap = new HashMap<String, Serializable>();
        taskContextMap.put("userId", userId);
        taskContextMap.put("groupId", groupId);
        taskContextMap.put("parameterMap", (Serializable) parameterMap);
        taskContextMap.put("privateLayout", privateLayout);
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
     * Returns an ordered range of all the measurements whose channelId, or
     * timestamp fields match the keywords specified for them, using the
     * indexer.
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
     * @param channelId
     * @param timestamp
     * @param andSearch
     * @param start
     * @param end
     * @param sort
     * @return
     * @throws SystemException
     */
    @Override
    public Hits search(long companyId, long groupId, String channelId,
            String channelName, String timestamp, boolean andSearch, int start,
            int end, Sort sort) throws SystemException {

        return search(companyId, groupId, channelId, channelName, timestamp, 0,
                0, andSearch, start, end, sort);

    }

    /**
     *
     * @param companyId
     * @param groupId
     * @param channelId
     * @param channelName
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
    public Hits search(long companyId, long groupId, String channelId,
            String channelName, long from, long until, boolean andSearch,
            int start, int end, Sort sort) throws SystemException {

        return search(companyId, groupId, channelId, channelName, null, from,
                until, andSearch, start, end, sort);
    }

    /**
     * Returns an ordered range of all the measurements whose channelId, or
     * timestamp fields match the keywords specified for them, using the
     * indexer.
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
     * @param channelId
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
    public Hits search(long companyId, long groupId, String channelId,
            String channelName, String timestamp, long from, long until,
            boolean andSearch, int start, int end, Sort sort)
            throws SystemException {

        if (Validator.isNull(sort)) {
            sort = new Sort("timestamp", true);
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

            attributes.put("channelId", channelId);
            attributes.put("channelName", channelName);
            attributes.put("timestamp", timestamp);
            attributes.put("from", from);
            attributes.put("until", until);

            searchContext.setAttributes(attributes);

            // Always add facets as late as possible so that the search context
            // fields can be considered by the facets

            List<Facet> facets = new ArrayList<Facet>();

            if (Validator.isNotNull(channelId)) {
                Facet facet = new MultiValueFacet(searchContext);
                facet.setFieldName("channelId");
                facets.add(facet);
            }

            if (Validator.isNotNull(channelName)) {
                Facet facet = new MultiValueFacet(searchContext);
                facet.setFieldName("channelName");
                facets.add(facet);
            }

            if (Validator.isNotNull(timestamp)) {
                Facet facet = new MultiValueFacet(searchContext);
                facet.setFieldName("timestamp");
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
    public Measurement updateMeasurement(long measurementId, long userId,
            String data, ServiceContext serviceContext) throws PortalException,
            SystemException {

        // Measurement

        User user = userPersistence.findByPrimaryKey(userId);

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

        measurementPersistence.update(measurement);

        // Indexing

        Indexer indexer = IndexerRegistryUtil
                .nullSafeGetIndexer(Measurement.class);
        indexer.reindex(measurement);

        return measurement;

    }

    private static final Log _log = LogFactoryUtil
            .getLog(MeasurementLocalServiceImpl.class);
}
