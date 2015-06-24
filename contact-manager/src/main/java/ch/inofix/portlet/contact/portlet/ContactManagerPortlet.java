package ch.inofix.portlet.contact.portlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.ContactServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
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
 * @modified 2015-06-24 19:36
 * @version 1.1.0
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
	 * @since 1.0.9
	 * @throws Exception
	 */
	public void deleteAllContacts(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				Contact.class.getName(), actionRequest);

		List<Contact> contacts = ContactLocalServiceUtil
				.getContacts(serviceContext.getScopeGroupId());

		for (Contact contact : contacts) {

			// TODO: Add try-catch and count failed deletions
			contact = ContactServiceUtil.deleteContact(contact.getContactId());

		}

		SessionMessages.add(actionRequest, "request_processed",
				PortletUtil.translate("successfully-deleted-x-contacts"));

	}

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 */
	public void deleteContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		long contactId = ParamUtil.getLong(actionRequest, "contactId");

		Contact contact = ContactServiceUtil.deleteContact(contactId);

		actionRequest.setAttribute("CONTACT", contact);

		SessionMessages.add(actionRequest, "request_processed",
				PortletUtil.translate("successfully-deleted-the-contact"));

	}

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 */
	public void deleteContacts(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		long[] contactIds = ParamUtil.getLongValues(actionRequest, "rowIds");

		// log.info("contactIds.length = " + contactIds.length);

		if (contactIds.length > 0) {

			for (long contactId : contactIds) {

				// TODO: Add try-catch and count failed deletions
				Contact contact = ContactServiceUtil.deleteContact(contactId);

			}

			SessionMessages.add(actionRequest, "request_processed",
					PortletUtil.translate("successfully-deleted-x-contacts"));

		} else {
			SessionMessages.add(actionRequest, "request_processed",
					PortletUtil.translate("no-contact-selected"));
		}

	}

	/**
	 * Load a single contact for editing.
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.1
	 * @throws Exception
	 * 
	 */
	public void editContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		String backURL = ParamUtil.getString(actionRequest, "backURL");
		long contactId = ParamUtil.getLong(actionRequest, "contactId");
		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
		String redirect = ParamUtil.getString(actionRequest, "redirect");
		String windowId = ParamUtil.getString(actionRequest, "windowId");

		Contact contact = null;

		if (contactId > 0) {
			contact = ContactServiceUtil.getContact(contactId);
		} else {
			contact = ContactServiceUtil.createContact();
		}

		actionRequest.setAttribute("CONTACT", contact);

		actionResponse.setRenderParameter("backURL", backURL);
		actionResponse.setRenderParameter("contactId",
				String.valueOf(contactId));
		actionResponse.setRenderParameter("mvcPath", mvcPath);
		actionResponse.setRenderParameter("redirect", redirect);
		actionResponse.setRenderParameter("windowId", windowId);

	}

	/**
	 * Disable the get- / sendRedirect feature of LiferayPortlet.
	 * 
	 */
	@Override
	protected String getRedirect(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		return null;
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

		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);

		File file = uploadPortletRequest.getFile("file");

		if (Validator.isNotNull(file)) {

			List<VCard> vCards = Ezvcard.parse(file).all();

			String message = PortletUtil.translate("no-vcards-found");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					Contact.class.getName(), uploadPortletRequest);

			if (vCards.size() > 0) {

				message = PortletUtil.importVcards(vCards, serviceContext);
			}

			SessionMessages.add(actionRequest, "request_processed", message);

		} else {

			SessionErrors.add(actionRequest, "file-not-found");

		}

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

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

		long userId = themeDisplay.getUserId();
		long groupId = themeDisplay.getScopeGroupId();

		String backURL = ParamUtil.getString(actionRequest, "backURL");
		long contactId = ParamUtil.getLong(actionRequest, "contactId");
		String historyKey = ParamUtil.getString(actionRequest, "historyKey");
		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
		String redirect = ParamUtil.getString(actionRequest, "redirect");
		String windowId = ParamUtil.getString(actionRequest, "windowId");

		VCard vCard = null;
		String uid = null;

		if (contactId > 0) {

			Contact contact = ContactServiceUtil.getContact(contactId);
			uid = contact.getUid();
			vCard = contact.getVCard();

		} else {

			vCard = new VCard();
			vCard.setUid(Uid.random());
			uid = vCard.getUid().getValue();

		}

		// Update the vCard with the request parameters

		vCard = PortletUtil.getVCard(actionRequest, vCard);

		// Store contact information in vCard format

		String card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

		// Save the contact

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				Contact.class.getName(), actionRequest);

		Contact contact = null;

		if (contactId > 0) {
			contact = ContactServiceUtil.updateContact(userId, groupId,
					contactId, card, uid, serviceContext);
			SessionMessages.add(actionRequest, "request_processed",
					PortletUtil.translate("successfully-updated-the-contact"));
		} else {
			contact = ContactServiceUtil.addContact(userId, groupId, card, uid,
					serviceContext);
			SessionMessages.add(actionRequest, "request_processed",
					PortletUtil.translate("successfully-added-the-contact"));
		}

		actionRequest.setAttribute("CONTACT", contact);

		actionResponse.setRenderParameter("contactId",
				String.valueOf(contactId));
		actionResponse.setRenderParameter("backURL", backURL);
		actionResponse.setRenderParameter("historyKey", historyKey);
		actionResponse.setRenderParameter("mvcPath", mvcPath);
		actionResponse.setRenderParameter("redirect", redirect);
		actionResponse.setRenderParameter("windowId", windowId);

	}

	/**
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @since 1.0.6
	 * @throws PortletException
	 */
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws PortletException {

		try {
			String resourceID = resourceRequest.getResourceID();

			if (resourceID.equals("serveVCard")) {
				serveVCard(resourceRequest, resourceResponse);
			} else if (resourceID.equals("serveVCards")) {
				serveVCards(resourceRequest, resourceResponse);
			} else {
				super.serveResource(resourceRequest, resourceResponse);
			}
		} catch (Exception e) {
			log.error(e);
			throw new PortletException(e);
		}

	}

	/**
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @since 1.0.6
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	protected void serveVCard(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws PortalException,
			SystemException, IOException {

		long contactId = ParamUtil.getLong(resourceRequest, "contactId");

		Contact contact = ContactServiceUtil.getContact(contactId);

		String card = contact.getCard();
		String name = contact.getFullName(true);

		PortletResponseUtil.sendFile(resourceRequest, resourceResponse, name
				+ ".vcf", card.getBytes(), ContentTypes.TEXT_PLAIN_UTF8);

	}

	/**
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @since 1.0.6
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	protected void serveVCards(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws PortalException,
			SystemException, IOException {

		// TODO: Move this to an exportContacts() method in
		// ContactServiceImpl and check for the export permission
		List<Contact> contacts = ContactLocalServiceUtil.getContacts(0,
				Integer.MAX_VALUE);

		StringBuilder sb = new StringBuilder();

		for (Contact contact : contacts) {
			sb.append(contact.getCard());
			sb.append("\n");
		}

		String cards = sb.toString();

		PortletResponseUtil.sendFile(resourceRequest, resourceResponse,
				"list.vcf", cards.getBytes(), ContentTypes.TEXT_PLAIN_UTF8);

	}

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.2
	 * @throws Exception
	 */
	public void viewContact(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		editContact(actionRequest, actionResponse);

	}

}
