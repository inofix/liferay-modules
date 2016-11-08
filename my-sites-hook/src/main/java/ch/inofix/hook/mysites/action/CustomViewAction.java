package ch.inofix.hook.mysites.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.NoSuchLayoutSetException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-07 22:48
 * @modified 2016-11-07 22:48
 * @version 1.0.0
 *
 */
public class CustomViewAction extends BaseStrutsPortletAction {

    /**
     * @since 1.0.0
     */
    public void processAction(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
                .getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = ParamUtil.getLong(actionRequest, "groupId");
        String privateLayoutParam = actionRequest.getParameter("privateLayout");

        List<Layout> layouts = getLayouts(groupId, privateLayoutParam);

        if (layouts.isEmpty()) {
            SessionErrors.add(actionRequest,
                    NoSuchLayoutSetException.class.getName(),
                    new NoSuchLayoutSetException("{groupId=" + groupId
                            + ",privateLayout=" + privateLayoutParam + "}"));
        }

        String redirect = getRedirect(themeDisplay, layouts, groupId,
                privateLayoutParam);

        if (Validator.isNull(redirect)) {
            redirect = PortalUtil.escapeRedirect(ParamUtil.getString(
                    actionRequest, "redirect"));
        }

        if (Validator.isNotNull(redirect)) {
            actionResponse.sendRedirect(redirect);
        }

    }

    /**
     * @since 1.0.0
     */
    public String render(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        return originalStrutsPortletAction.render(null, portletConfig,
                renderRequest, renderResponse);

    }

    /**
     * @since 1.0.0
     */
    public void serveResource(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws Exception {

        originalStrutsPortletAction.serveResource(originalStrutsPortletAction,
                portletConfig, resourceRequest, resourceResponse);

    }

    /**
     * 
     * @param groupId
     * @param privateLayout
     * @return
     * @since 1.0.0
     * @throws Exception
     */
    protected List<Layout> getLayouts(long groupId, boolean privateLayout)
            throws Exception {

        return LayoutLocalServiceUtil.getLayouts(groupId, privateLayout,
                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
    }

    /**
     * 
     * @param groupId
     * @param privateLayoutParam
     * @return
     * @since 1.0.0
     * @throws Exception
     */
    protected List<Layout> getLayouts(long groupId, String privateLayoutParam)
            throws Exception {

        List<Layout> layouts = null;

        boolean privateLayout = false;

        if (Validator.isNull(privateLayoutParam)) {
            layouts = getLayouts(groupId, false);

            if (layouts.isEmpty()) {
                layouts = getLayouts(groupId, true);
            }
        } else {
            privateLayout = GetterUtil.getBoolean(privateLayoutParam);

            layouts = getLayouts(groupId, privateLayout);
        }

        return layouts;
    }

    /**
     * 
     * @param themeDisplay
     * @param layouts
     * @param groupId
     * @param privateLayoutParam
     * @return
     * @since 1.0.0
     * @throws Exception
     */
    protected String getRedirect(ThemeDisplay themeDisplay,
            List<Layout> layouts, long groupId, String privateLayoutParam)
            throws Exception {

        PermissionChecker permissionChecker = themeDisplay
                .getPermissionChecker();

        for (Layout layout : layouts) {
            if (!layout.isHidden()
                    && LayoutPermissionUtil.contains(permissionChecker, layout,
                            ActionKeys.VIEW)) {

                // Customized: use the layout's friendlyURL instead of the
                // canonical since the canonical url redirects to the site's
                // virtualhost (if configured) which usually requires a fresh
                // login.
                String layoutFriendlyURL = PortalUtil.getLayoutFriendlyURL(
                        layout, themeDisplay);

                // String canonicalURL = PortalUtil.getCanonicalURL(null,
                // themeDisplay, layout, true);

                return PortalUtil.addPreservedParameters(themeDisplay, layout,
                        layoutFriendlyURL, true);

            }
        }

        Group group = GroupLocalServiceUtil.getGroup(groupId);

        String groupFriendlyURL = PortalUtil.getGroupFriendlyURL(group,
                GetterUtil.getBoolean(privateLayoutParam), themeDisplay);

        return PortalUtil
                .addPreservedParameters(themeDisplay, groupFriendlyURL);
    }

    private static Log _log = LogFactoryUtil.getLog(CustomViewAction.class
            .getName());
}
