package ch.inofix.referencemanager.web.social;

import org.osgi.service.component.annotations.Component;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.social.kernel.model.BaseSocialActivityInterpreter;
import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityConstants;
import com.liferay.social.kernel.model.SocialActivityInterpreter;

import ch.inofix.referencemanager.constants.PortletKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalService;
import ch.inofix.referencemanager.service.permission.ReferencePermission;
import ch.inofix.referencemanager.social.ReferenceActivityKeys;

/**
 * @author Christian Berndt
 * @created 2016-12-04 13:20
 * @modified 2017-01-07 14:28
 * @version 1.0.2
 */
@Component(
    property = {"javax.portlet.name=" + PortletKeys.REFERENCE_MANAGER }, 
    service = SocialActivityInterpreter.class
)
public class ReferenceActivityInterpreter extends BaseSocialActivityInterpreter {

    @Override
    public String[] getClassNames() {
        return _CLASS_NAMES;
    }

    @Override
    protected String getPath(SocialActivity activity, ServiceContext serviceContext) throws Exception {

        AssetRendererFactory<?> assetRendererFactory = AssetRendererFactoryRegistryUtil
                .getAssetRendererFactoryByClassName(Reference.class.getName());

        AssetRenderer<?> assetRenderer = assetRendererFactory.getAssetRenderer(activity.getClassPK());

        String path = assetRenderer.getURLViewInContext(serviceContext.getLiferayPortletRequest(),
                serviceContext.getLiferayPortletResponse(), null);

        path = HttpUtil.addParameter(path, "redirect", serviceContext.getCurrentURL());

        return path;
    }

    @Override
    protected ResourceBundleLoader getResourceBundleLoader() {
        return _resourceBundleLoader;
    }

    @Override
    protected String getTitlePattern(String groupName, SocialActivity activity) {

        int activityType = activity.getType();

        if (activityType == ReferenceActivityKeys.ADD_REFERENCE) {
            if (Validator.isNull(groupName)) {
                return "activity-reference-add-reference";
            } else {
                return "activity-reference-add-reference-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
            if (Validator.isNull(groupName)) {
                return "activity-reference-move-to-trash";
            } else {
                return "activity-reference-move-to-trash-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

            if (Validator.isNull(groupName)) {
                return "activity-reference-restore-from-trash";
            } else {
                return "activity-reference-restore-from-trash-in";
            }
        } else if (activityType == ReferenceActivityKeys.UPDATE_REFERENCE) {
            if (Validator.isNull(groupName)) {
                return "activity-reference-update-reference";
            } else {
                return "activity-reference-update-reference-in";
            }
        }

        return StringPool.BLANK;
    }

    @Override
    protected boolean hasPermissions(PermissionChecker permissionChecker, SocialActivity activity, String actionId,
            ServiceContext serviceContext) throws Exception {

        Reference reference = _referenceLocalService.getReference(activity.getClassPK());

        return ReferencePermission.contains(permissionChecker, reference.getReferenceId(), actionId);
    }

    @org.osgi.service.component.annotations.Reference(unbind = "-")
    protected void setReferenceLocalService(ReferenceLocalService referenceLocalService) {
        _referenceLocalService = referenceLocalService;
    }

    private static final String[] _CLASS_NAMES = { Reference.class.getName() };

    private ReferenceLocalService _referenceLocalService;
    private ResourceBundleLoader _resourceBundleLoader;

}