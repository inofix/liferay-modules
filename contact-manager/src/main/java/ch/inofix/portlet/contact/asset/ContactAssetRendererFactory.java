package ch.inofix.portlet.contact.asset;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-19 17:12
 * @modified 2015-05-19 17:12
 * @version 1.0.0
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
		
		log.info("getAssetRenderer()"); 

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
	public boolean hasPermission(PermissionChecker permissionChecker,
			long classPK, String actionId) throws Exception {

		return true;
		// TODO: Check permission
		// return ContactPermission.contains(permissionChecker, classPK,
		// actionId);
	}

	@Override
	public boolean isLinkable() {
		return _LINKABLE;
	}

	private static final boolean _LINKABLE = true;

}
