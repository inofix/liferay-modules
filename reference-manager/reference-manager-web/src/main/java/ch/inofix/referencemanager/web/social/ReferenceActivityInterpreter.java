package ch.inofix.referencemanager.web.social;

import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
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

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 * @created 2016-12-04 13:20
 * @modified 2016-12-04 13:20
 * @version 1.0.0
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

        long plid = PortalUtil.getPlidFromPortletId(serviceContext.getScopeGroupId(), PortletKeys.REFERENCE_MANAGER);

        PortletURL portletURL = PortletURLFactoryUtil.create(serviceContext.getRequest(), PortletKeys.REFERENCE_MANAGER,
                plid, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcPath", "/edit_reference.jsp");
        portletURL.setParameter("backURL", serviceContext.getCurrentURL());
        portletURL.setParameter("referenceId", String.valueOf(activity.getClassPK()));

        return portletURL.toString();
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