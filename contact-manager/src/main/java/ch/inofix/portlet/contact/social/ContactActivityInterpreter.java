package ch.inofix.portlet.contact.social;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.permission.ContactPermission;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ServiceContext;
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
 * @modified 2015-05-23 21:58
 * @version 1.0.0
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

		log.info("Executing getPath().");

		long plid = PortalUtil.getPlidFromPortletId(
				serviceContext.getScopeGroupId(),
				"contactmanager_WAR_contactmanager");
		
		PortletURL portletURL = PortletURLFactoryUtil.create(
				serviceContext.getRequest(),
				"contactmanager_WAR_contactmanager", plid,
				PortletRequest.ACTION_PHASE);

		portletURL.setWindowState(LiferayWindowState.NORMAL);

		portletURL.setParameter("backURL", serviceContext.getCurrentURL());
		portletURL.setParameter("contactId",
				String.valueOf(activity.getClassPK()));
		portletURL.setParameter("javax.portlet.action", "editContact");
		// TODO: Check the user's permissions and set the path accordingly: 
		// no permissions: return StringPool.BLANK, 
		// view permission: mvcPath = /html/view_contact.jsp
		// update permission: mvcPath = /html/edit_contact.jsp
		portletURL.setParameter("mvcPath", "/html/edit_contact.jsp");

		return portletURL.toString();
	}

	/**
	 * We need to override BaseSocialActivityInterpreter's getTitle method
	 * because it does find the portlet's Language.properties.
	 */
	@Override
	protected String getTitle(SocialActivity activity,
			ServiceContext serviceContext) throws Exception {

		log.info("Executing getTitle().");

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
