package ch.inofix.portlet.data.asset;

import ch.inofix.portlet.data.model.Measurement;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-28 17:17
 * @modified 2017-03-28 17:17
 * @version 1.0.0
 *
 */
public class MeasurementAssetRendererFactory extends BaseAssetRendererFactory {

    public static final String TYPE = "measurement";

    @Override
    public AssetRenderer getAssetRenderer(long classPK, int type)
            throws PortalException, SystemException {

        return null;

    }

    @Override
    public String getClassName() {
        return Measurement.class.getName();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    private static Log _log = LogFactoryUtil
            .getLog(MeasurementAssetRendererFactory.class.getName());

}
