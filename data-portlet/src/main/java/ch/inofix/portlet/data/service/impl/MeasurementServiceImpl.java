package ch.inofix.portlet.data.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;
import ch.inofix.portlet.data.service.base.MeasurementServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the measurement remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.data.service.MeasurementService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2017-03-08 19:46
 * @modified 2017-10-25 00:29
 * @version 1.0.3
 * @see ch.inofix.portlet.data.service.base.MeasurementServiceBaseImpl
 * @see ch.inofix.portlet.data.service.MeasurementServiceUtil
 */
public class MeasurementServiceImpl extends MeasurementServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.data.service.MeasurementServiceUtil} to access the
     * measurement remote service.
     */

    @Override
    public List<Measurement> deleteGroupMeasurements(long groupId)
            throws PortalException, SystemException {

        // TODO: enable permission checks
        // DataPortletPermission.check(getPermissionChecker(), groupId,
        // ActionKeys.DELETE_GROUP_MEASUREMENTS);

        return MeasurementLocalServiceUtil.deleteGroupMeasurements(groupId);
    }
    
    @Override
    public List<Measurement> deleteMeasurementsByChannelName(long companyId,
            long groupId, String channelName) throws PortalException,
            SystemException {

        // TODO: enable permission checks
        // DataPortletPermission.check(getPermissionChecker(), groupId,
        // ActionKeys.DELETE_MEASUREMENTS_BY_CHANNEL_NAME);

        return MeasurementLocalServiceUtil.deleteMeasurementsByChannelName(
                companyId, groupId, channelName);
    }

    @Override
    public long importMeasurementsInBackground(long userId, String taskName,
            long groupId, boolean privateLayout,
            Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        // TODO: enable permission check
        // DataPortletPermission.check(getPermissionChecker(), groupId,
        // ActionKeys.IMPORT_MEASUREMENTS);

        return MeasurementLocalServiceUtil.importMeasurementsInBackground(
                userId, taskName, groupId, privateLayout, parameterMap, file);

    }
}
