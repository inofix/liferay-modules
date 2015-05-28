package ch.inofix.portlet.contact.social;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.security.permission.ActionKeys;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.permission.ContactPermission;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;

/**
 * ... Based on the model of CalendarActivityInterpreter.
 * 
 * @author Christian Berndt
 * @created 2015-05-23 21:58
 * @modified 2015-05-28 16:41
 * @version 1.0.1
 *
 */
public class ContactActivityInterpreter extends BaseSocialActivityInterpreter {

	// Enable logging for this class
	private static final Log log = LogFactoryUtil
			.getLog(ContactActivityInterpreter.class.getName());

	@Override
	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected String getPath(SocialActivity activity,
			ServiceContext serviceContext) throws Exception {

		long plid = PortalUtil.getPlidFromPortletId(
				serviceContext.getScopeGroupId(),
				"contactmanager_WAR_contactmanager");

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		PermissionChecker permissionChecker = themeDisplay
				.getPermissionChecker();

		PortletURL portletURL = PortletURLFactoryUtil.create(
				serviceContext.getRequest(),
				"contactmanager_WAR_contactmanager", plid,
				PortletRequest.ACTION_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		portletURL.setParameter("redirect", serviceContext.getCurrentURL());
		portletURL.setParameter("contactId",
				String.valueOf(activity.getClassPK()));
		portletURL.setParameter("javax.portlet.action", "editContact");
		String title = "";

		if (ContactPermission.contains(permissionChecker,
				activity.getClassPK(), ActionKeys.UPDATE)) {

			portletURL.setParameter("mvcPath", "/html/edit_contact.jsp");
			// TODO: Format the popup title
			title = "edit";

		} else if (ContactPermission.contains(permissionChecker,
				activity.getClassPK(), ActionKeys.VIEW)) {

			portletURL.setParameter("mvcPath", "/html/view_contact.jsp");
			// TODO: Format the popup title
			title = "view";

		} else {
			return StringPool.BLANK;
		}

		String taglibURL = "javascript:Liferay.Util.openWindow({id: '"
				+ "_contactmanager_WAR_contactmanager_"
				+ "editAsset', title: '" + title + "', uri:'"
				+ HtmlUtil.escapeJS(portletURL.toString()) + "'});";

		return taglibURL;
	}

	/**
	 * We need to override BaseSocialActivityInterpreter's getTitle method
	 * because it does find the portlet's Language.properties.
	 */
	@Override
	protected String getTitle(SocialActivity activity,
			ServiceContext serviceContext) throws Exception {

		String groupName = StringPool.BLANK;

		// Only include the group's name if the activity was performed in a
		// group other than the current group
		if (activity.getGroupId() != serviceContext.getScopeGroupId()) {
			groupName = getGroupName(activity.getGroupId(), serviceContext);
		}

		String link = getLink(activity, serviceContext);

		String titlePattern = getTitlePattern(groupName, activity);

		ResourceBundle res = ResourceBundle.getBundle("Language",
				serviceContext.getLocale());

		String titleMessage = res.getString(titlePattern);

		String entryTitle = getEntryTitle(activity, serviceContext);

		String wrappedLink = wrapLink(link, entryTitle);

		Object[] titleArguments = getTitleArguments(groupName, activity,
				wrappedLink, entryTitle, serviceContext);

		String title = MessageFormat.format(titleMessage, titleArguments);

		return title;

	}

	@Override
	protected Object[] getTitleArguments(String groupName,
			SocialActivity activity, String wrappedLink, String title,
			ServiceContext serviceContext) {

		String userName = getUserName(activity.getUserId(), serviceContext);

		Object[] titleArguments = new Object[] { groupName, userName,
				wrappedLink };

		return titleArguments;
	}

	@Override
	protected String getTitlePattern(String groupName, SocialActivity activity) {

		int activityType = activity.getType();

		if (activityType == ContactActivityKeys.ADD_CONTACT) {
			if (Validator.isNull(groupName)) {
				return "activity-contact-add-contact";
			} else {
				return "activity-contact-add-contact-in";
			}
		} else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (Validator.isNull(groupName)) {
				return "activity-contact-move-to-trash";
			} else {
				return "activity-contact-move-to-trash-in";
			}
		} else if (activityType == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (Validator.isNull(groupName)) {
				return "activity-contact-restore-from-trash";
			} else {
				return "activity-contact-restore-from-trash-in";
			}
		} else if (activityType == ContactActivityKeys.UPDATE_CONTACT) {
			if (Validator.isNull(groupName)) {
				return "activity-contact-update-contact";
			} else {
				return "activity-contact-update-contact-in";
			}
		}

		return StringPool.BLANK;
	}

	@Override
	protected boolean hasPermissions(PermissionChecker permissionChecker,
			SocialActivity activity, String actionId,
			ServiceContext serviceContext) throws Exception {

		Contact contact = ContactLocalServiceUtil.getContact(activity
				.getClassPK());

		return ContactPermission.contains(permissionChecker,
				contact.getContactId(), actionId);
	}

	private static final String[] _CLASS_NAMES = { Contact.class.getName() };

}
