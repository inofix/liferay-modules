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
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyLocalService;
import ch.inofix.referencemanager.service.permission.BibliographyPermission;
import ch.inofix.referencemanager.social.BibliographyActivityKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 * @created 2016-12-04 13:21
 * @modified 2016-12-04 13:21
 * @version 1.0.0
 */
@Component(
    property = {"javax.portlet.name=" + PortletKeys.BIBLIOGRAPHY_MANAGER }, 
    service = SocialActivityInterpreter.class
)
public class BibliographyActivityInterpreter extends BaseSocialActivityInterpreter {

    @Override
    public String[] getClassNames() {
        return _CLASS_NAMES;
    }
    
    @Override
    protected String getPath(SocialActivity activity, ServiceContext serviceContext) throws Exception {

        long plid = PortalUtil.getPlidFromPortletId(serviceContext.getScopeGroupId(), PortletKeys.BIBLIOGRAPHY_MANAGER);

        PortletURL portletURL = PortletURLFactoryUtil.create(serviceContext.getRequest(), PortletKeys.BIBLIOGRAPHY_MANAGER,
                plid, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcPath", "/edit_bibliography.jsp");
        portletURL.setParameter("backURL", serviceContext.getCurrentURL());
        portletURL.setParameter("bibliographyId", String.valueOf(activity.getClassPK()));

        return portletURL.toString();
    }


    @Override
    protected ResourceBundleLoader getResourceBundleLoader() {
        return _resourceBundleLoader;
    }
    
    @Override
    protected String getTitlePattern(String groupName, SocialActivity activity) {

        int activityType = activity.getType();

        if (activityType == BibliographyActivityKeys.ADD_BIBLIOGRAPHY) {
            if (Validator.isNull(groupName)) {
                return "activity-bibliography-add-bibliography";
            } else {
                return "activity-bibliography-add-bibliography-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
            if (Validator.isNull(groupName)) {
                return "activity-bibliography-move-to-trash";
            } else {
                return "activity-bibliography-move-to-trash-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

            if (Validator.isNull(groupName)) {
                return "activity-bibliography-restore-from-trash";
            } else {
                return "activity-bibliography-restore-from-trash-in";
            }
        } else if (activityType == BibliographyActivityKeys.UPDATE_BIBLIOGRAPHY) {
            if (Validator.isNull(groupName)) {
                return "activity-bibliography-update-bibliography";
            } else {
                return "activity-bibliography-update-bibliography-in";
            }
        }

        return StringPool.BLANK;
    }
    
    @Override
    protected boolean hasPermissions(PermissionChecker permissionChecker, SocialActivity activity, String actionId,
            ServiceContext serviceContext) throws Exception {

        Bibliography bibliography = _bibliographyLocalService.getBibliography(activity.getClassPK());

        return BibliographyPermission.contains(permissionChecker, bibliography.getBibliographyId(), actionId);
    }

    @Reference(unbind = "-")
    protected void setBibliographyLocalService(BibliographyLocalService bibliographyLocalService) {
        _bibliographyLocalService = bibliographyLocalService;
    }
    
    private static final String[] _CLASS_NAMES = { Bibliography.class.getName() };
    
    private BibliographyLocalService _bibliographyLocalService;
    private ResourceBundleLoader _resourceBundleLoader;

}
