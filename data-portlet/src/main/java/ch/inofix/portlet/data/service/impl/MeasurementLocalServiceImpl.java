package ch.inofix.portlet.data.service.impl;

import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.base.MeasurementLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
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
 * @modified 2017-03-09 17:40
 * @version 1.0.1
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

        return measurement;

    }

    @Override
    public Measurement deleteMeasurement(Measurement measurement)
            throws SystemException {

        // Measurement

        measurementPersistence.remove(measurement);

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

        return measurement;

    }

    private static final Log _log = LogFactoryUtil
            .getLog(MeasurementLocalServiceImpl.class);
}
