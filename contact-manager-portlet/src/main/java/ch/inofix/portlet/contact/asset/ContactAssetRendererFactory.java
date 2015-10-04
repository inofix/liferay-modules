package ch.inofix.portlet.contact.asset;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.security.permission.ActionKeys;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.permission.ContactPermission;
import ch.inofix.portlet.contact.service.permission.ContactPortletPermission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-19 17:12
 * @modified 2015-05-23 14:13
 * @version 1.0.1
 *
 */
public class ContactAssetRendererFactory extends BaseAssetRendererFactory {

	private static Log log = LogFactoryUtil
			.getLog(ContactAssetRendererFactory.class.getName());

	// TODO: What's the meaning of "TYPE" in this context?
	public static final String TYPE = "contact";

	@Override
	public AssetRenderer getAssetRenderer(long classPK, int type)
			throws PortalException, SystemException {

		Contact contact = ContactLocalServiceUtil.getContact(classPK);

		ContactAssetRenderer contactAssetRenderer = new ContactAssetRenderer(
				contact);

		contactAssetRenderer.setAssetRendererType(type);

		return contactAssetRenderer;
	}

	@Override
	public String getClassName() {
		return Contact.class.getName();
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
			throws PortalException, SystemException {

		ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		if (!ContactPortletPermission.contains(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(), ActionKeys.ADD_CONTACT)) {

			return null;
		}

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				getControlPanelPlid(themeDisplay),
				"contactmanager_WAR_contactmanager",
				PortletRequest.ACTION_PHASE);

		String redirect = PortalUtil.getCurrentURL(liferayPortletRequest);

		portletURL.setParameter("javax.portlet.action", "editContact");
		portletURL.setParameter("mvcPath", "/html/edit_contact.jsp");
		portletURL.setParameter("redirect", redirect);

		return portletURL;
	}

	@Override
	public boolean hasPermission(PermissionChecker permissionChecker,
			long classPK, String actionId) throws Exception {

		return ContactPermission.contains(permissionChecker, classPK, actionId);
	}

	@Override
	public boolean isLinkable() {
		return _LINKABLE;
	}

	private static final boolean _LINKABLE = true;

}
