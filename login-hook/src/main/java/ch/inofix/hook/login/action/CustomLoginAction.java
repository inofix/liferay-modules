package ch.inofix.hook.login.action;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

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
import com.liferay.portal.util.PrefsPropsUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-04 19:18
 * @modified 2016-11-04 19:18
 * @version 1.0.0
 *
 */
public class CustomLoginAction extends BaseStrutsPortletAction {

    public void processAction(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        _log.info("processAction()");

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
                .getAttribute(WebKeys.THEME_DISPLAY);

        String groups = actionRequest.getParameter("groups");

        _log.info("groups = " + groups);

        User user = themeDisplay.getUser();

        _log.info("user = " + user);

        if (user != null && !user.isDefaultUser()) {

            String[] defaultGroupNames = PrefsPropsUtil.getStringArray(
                    user.getCompanyId(), groups, StringPool.COMMA);

            _log.info(defaultGroupNames.length);

            Set<Long> groupIdsSet = new HashSet<Long>();

            for (String defaultGroupName : defaultGroupNames) {
                Company company = CompanyLocalServiceUtil.getCompany(user
                        .getCompanyId());

                Account account = company.getAccount();

                if (StringUtil.equalsIgnoreCase(defaultGroupName,
                        account.getName())) {

                    defaultGroupName = GroupConstants.GUEST;
                }

                Group group = GroupLocalServiceUtil.getGroup(
                        user.getCompanyId(), defaultGroupName);

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

        originalStrutsPortletAction.processAction(originalStrutsPortletAction,
                portletConfig, actionRequest, actionResponse);
    }

    public String render(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        _log.info("render()");

        return originalStrutsPortletAction.render(null, portletConfig,
                renderRequest, renderResponse);

    }

    public void serveResource(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws Exception {

        originalStrutsPortletAction.serveResource(originalStrutsPortletAction,
                portletConfig, resourceRequest, resourceResponse);

    }

    private static Log _log = LogFactoryUtil.getLog(CustomLoginAction.class
            .getName());

}
