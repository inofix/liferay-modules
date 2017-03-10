package ch.inofix.portlet.data.service.impl;

import java.io.File;
import java.util.Map;

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
 * @modified 2017-03-09 18:42
 * @version 1.0.1
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
    public long importMeasurementsInBackground(long userId, String taskName,
            long groupId, boolean privateLayout,
            Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        // TODO: enable permission check
//        DataPortletPermission.check(getPermissionChecker(), groupId,
//                ActionKeys.IMPORT_MEASUREMENTS);

        return MeasurementLocalServiceUtil.importMeasurementsInBackground(
                userId, taskName, groupId, privateLayout, parameterMap, file);

    }
}
