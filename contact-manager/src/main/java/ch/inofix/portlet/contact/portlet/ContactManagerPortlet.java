package ch.inofix.portlet.contact.portlet;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.contact.NoSuchContactException;
import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.ContactServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Uid;

/**
 * ContactManagerPortlet: MVC-Controller of the contact-manager portlet.
 * 
 * @author Christian Berndt
 * @created 2015-05-07 15:38
 * @modified 2015-05-07 15:38
 * @version 1.0.0
 *
 */
public class ContactManagerPortlet extends MVCPortlet {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(ContactManagerPortlet.class
			.getName());

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 */
	public void deleteContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		log.info("Executing deleteContact().");

		// TODO: use contactId instead of id
		String contactId = ParamUtil.getString(actionRequest, "contactId");
		long id = ParamUtil.getLong(actionRequest, "id");

		Contact contact = ContactLocalServiceUtil.deleteContact(id);

		actionRequest.setAttribute("CONTACT", contact);

		SessionMessages.add(actionRequest, "request_processed",
				translate("successfully-deleted-the-contact"));

	}

	/**
	 * Load a single contact for editing.
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since
	 * @throws Exception
	 * 
	 */
	public void editContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		log.info("Executing editContact().");

		String backURL = ParamUtil.getString(actionRequest, "backURL");
		String contactId = ParamUtil.getString(actionRequest, "contactId");
		long id = ParamUtil.getLong(actionRequest, "id");
		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");

		// TODO: use remote service
		// TODO: use getContact(contactId)
		Contact contact = null;

		if (id > 0) {
			contact = ContactLocalServiceUtil.getContact(id);
		} else {
			contact = ContactLocalServiceUtil.createContact(0);
		}

		actionRequest.setAttribute("CONTACT", contact);
		
		actionResponse.setRenderParameter("backURL", backURL);
		actionResponse.setRenderParameter("id", String.valueOf(id));
		actionResponse.setRenderParameter("mvcPath", mvcPath);
	}

	/**
	 * Import vCards from an uploaded file to the contact manager.
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 * 
	 */
	public void importVCardFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

		long userId = themeDisplay.getUserId();
		long groupId = themeDisplay.getScopeGroupId();

		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);

		File file = uploadPortletRequest.getFile("file");

		Integer numVCards = 0;
		Integer numImported = 0;
		Integer numIgnored = 0;

		if (Validator.isNotNull(file)) {

			List<VCard> vcards = Ezvcard.parse(file).all();

			// TODO: move this code to a remote service method and add
			// permission checks

			numVCards = vcards.size();

			for (VCard vcard : vcards) {

				Uid uidObj = vcard.getUid();
				String uid = null;

				if (Validator.isNotNull(uidObj)) {
					uid = uidObj.getValue();
				}

				String str = Ezvcard.write(vcard).version(VCardVersion.V4_0)
						.go();

				// Only add the contact, if the vCard's uid does not yet exist
				// in this scope
				Contact contact = null;

				try {
					contact = ContactLocalServiceUtil.getContact(groupId, uid);
				} catch (NoSuchContactException ignore) {
					// ignore
				}

				if (contact == null) {
					ContactServiceUtil.addContact(userId, groupId, str, uid);
					numImported++;
				} else {
					numIgnored++;
				}

			}

		}

		StringBuffer sb = new StringBuffer();

		sb.append(translate("found-x-vcards", numVCards));
		sb.append(" ");
		sb.append(translate("imported-x-vcards", numImported));
		sb.append(" ");
		sb.append(translate("ignored-x-vcards", numIgnored));

		SessionMessages.add(actionRequest, "request_processed", sb.toString());

	}

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 */
	public void saveContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		log.info("Executing saveContact().");

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

		long userId = themeDisplay.getUserId();
		long groupId = themeDisplay.getScopeGroupId();

		String backURL = ParamUtil.getString(actionRequest, "backURL");
		String contactId = ParamUtil.getString(actionRequest, "contactId");
		long id = ParamUtil.getLong(actionRequest, "id");
		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");

		VCard vCard = null;
		String uid = null;

		// TODO: use contactId
		if (id > 0) {

			// TODO: use remote service
			Contact contact = ContactLocalServiceUtil.getContact(id);
			uid = contact.getUid();
			vCard = contact.getVCard();

		} else {

			vCard = new VCard();
			vCard.setUid(Uid.random());
			uid = vCard.getUid().getValue();

			log.info("uid = " + uid);

		}

		// Update the vCard with the request parameters

		vCard = PortletUtil.getVCard(actionRequest, vCard);

		// Store contact information in vCard format

		String card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();
		
		log.info("card = " + card);

		// Save the contact
		// TODO: use contactId
		Contact contact = ContactServiceUtil.saveContact(userId, groupId, id,
				card, uid);

		actionRequest.setAttribute("CONTACT", contact);

		SessionMessages.add(actionRequest, "request_processed",
				translate("successfully-updated-the-contact"));

		actionResponse.setRenderParameter("id", String.valueOf(id));
		actionResponse.setRenderParameter("backURL", backURL);
		actionResponse.setRenderParameter("mvcPath", mvcPath);

	}

	/**
	 * 
	 * @param key
	 * @return
	 * @since 1.0.0
	 */
	private String translate(String key) {
		return translate(key, null);
	}

	/**
	 * 
	 * @param key
	 * @param object
	 * @return
	 * @since 1.0.0
	 */
	private String translate(String key, Object object) {
		return translate(key, new Object[] { object });
	}

	/**
	 * 
	 * @param key
	 * @param objects
	 * @return
	 * @since 1.0.0
	 */
	private String translate(String key, Object[] objects) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("Language");
			return MessageFormat.format(bundle.getString(key), objects);
		} catch (MissingResourceException mre) {
			log.warn(mre.getMessage());
			return key;
		}
	}
}
