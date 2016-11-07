package ch.inofix.hook.login.action;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Account;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-04 19:18
 * @modified 2016-11-05 22:56
 * @version 1.0.1
 *
 */
public class CustomLoginAction extends BaseStrutsPortletAction {

    /**
     * @since 1.0.0
     */
    public void processAction(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        originalStrutsPortletAction.processAction(originalStrutsPortletAction,
                portletConfig, actionRequest, actionResponse);

    }

    /**
     * @since 1.0.0
     */
    public String render(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        PortletPreferences preferences = renderRequest.getPreferences();

        String groups = preferences.getValue("groups", StringPool.BLANK);

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
                .getAttribute(WebKeys.THEME_DISPLAY);

        if (themeDisplay != null) {

            User user = themeDisplay.getUser();

            if (user != null && !user.isDefaultUser()) {

                String[] defaultGroupNames = StringUtil.split(groups,
                        StringPool.COMMA);

                Set<Long> groupIdsSet = new HashSet<Long>();

                for (String defaultGroupName : defaultGroupNames) {
                    Company company = CompanyLocalServiceUtil.getCompany(user
                            .getCompanyId());

                    Account account = company.getAccount();

                    if (StringUtil.equalsIgnoreCase(defaultGroupName,
                            account.getName())) {

                        defaultGroupName = GroupConstants.GUEST;
                    }

                    Group group = null;

                    try {

                        group = GroupLocalServiceUtil.getGroup(
                                user.getCompanyId(), defaultGroupName);

                    } catch (NoSuchGroupException ignore) {
                        // ignore
                    }

                    if ((group != null)
                            && !UserLocalServiceUtil.hasGroupUser(
                                    group.getGroupId(), user.getUserId())) {

                        groupIdsSet.add(group.getGroupId());
                    }
                }

                long[] groupIds = ArrayUtil.toArray(groupIdsSet
                        .toArray(new Long[groupIdsSet.size()]));

                GroupLocalServiceUtil.addUserGroups(user.getUserId(), groupIds);

            }
        }

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

    private static Log _log = LogFactoryUtil.getLog(CustomLoginAction.class
            .getName());

}
