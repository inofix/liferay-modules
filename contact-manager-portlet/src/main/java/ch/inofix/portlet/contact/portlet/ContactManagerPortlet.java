package ch.inofix.portlet.contact.portlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.contact.ImageFileFormatException;
import ch.inofix.portlet.contact.KeyFileFormatException;
import ch.inofix.portlet.contact.SoundFileFormatException;
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
 * @modified 2015-10-07 22:59
 * @version 1.2.1
 */
public class ContactManagerPortlet extends MVCPortlet {

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.9
     * @throws Exception
     */
    public void deleteAllContacts(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

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

        actionResponse.setRenderParameter("tabs1", tabs1);
    }

    /**
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
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    protected void doDeleteContacts(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        long[] contactIds = ParamUtil.getLongValues(actionRequest, "rowIds");

        if (contactIds.length > 0) {

            for (long contactId : contactIds) {

                // TODO: Add try-catch and count failed deletions
                ContactServiceUtil.deleteContact(contactId);

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
     * @param actionRequest
     * @param actionResponse
     * @throws Exception
     * @since 1.1.9
     */
    public void editSet(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        String cmd = ParamUtil.getString(actionRequest, "cmd");

        if ("delete".equals(cmd)) {
            doDeleteContacts(actionRequest, actionResponse);
        }

    }

    /**
     * @param resourceRequest
     * @param resourceResponse
     * @since 1.0.6
     * @throws PortalException
     * @throws SystemException
     * @throws IOException
     */
    protected void exportVCards(ResourceRequest resourceRequest,
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
     * Disable the get- / sendRedirect feature of LiferayPortlet.
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
     */
    public void importVCardFile(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) request
                .getAttribute(WebKeys.THEME_DISPLAY);

        UploadPortletRequest uploadPortletRequest = PortalUtil
                .getUploadPortletRequest(actionRequest);

        File file = uploadPortletRequest.getFile("file");
        String fileName = file.getName();

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        boolean privateLayout = themeDisplay.getLayout().isPrivateLayout();

        String servletContextName = request.getSession().getServletContext()
                .getServletContextName();

        String[] servletContextNames = new String[] { servletContextName };

        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("servletContextNames", servletContextNames);

        if (Validator.isNotNull(file)) {

            List<VCard> vCards = Ezvcard.parse(file).all();

            String message = PortletUtil.translate("no-v-cards-were-found");

            if (vCards.size() > 0) {

                message = PortletUtil.translate(
                        "found-x-v-cards-the-import-will-finish-in-separate-thread",
                                vCards.size());

                // TODO: use remote service and check permissions
                ContactLocalServiceUtil.importContactsInBackground(userId,
                        fileName, groupId, privateLayout, parameterMap, file);
            }

            SessionMessages.add(actionRequest, "request_processed", message);

        } else {

            SessionErrors.add(actionRequest, "file-not-found");

        }

    }

    /**
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

        UploadPortletRequest uploadPortletRequest = PortalUtil
                .getUploadPortletRequest(actionRequest);

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        String backURL = ParamUtil.getString(uploadPortletRequest, "backURL");
        long contactId = ParamUtil.getLong(uploadPortletRequest, "contactId");
        String historyKey = ParamUtil.getString(uploadPortletRequest,
                "historyKey");
        String mvcPath = ParamUtil.getString(uploadPortletRequest, "mvcPath");
        String redirect = ParamUtil.getString(uploadPortletRequest, "redirect");
        String windowId = ParamUtil.getString(uploadPortletRequest, "windowId");

        Contact contact = null;

        VCard vCard = null;
        String uid = null;

        if (contactId > 0) {

            contact = ContactServiceUtil.getContact(contactId);
            uid = contact.getUid();
            vCard = contact.getVCard();

        } else {

            vCard = new VCard();
            vCard.setUid(Uid.random());
            uid = vCard.getUid().getValue();

        }

        // Pass the required parameters to the render phase

        actionResponse.setRenderParameter("contactId",
                String.valueOf(contactId));
        actionResponse.setRenderParameter("backURL", backURL);
        actionResponse.setRenderParameter("historyKey", historyKey);
        actionResponse.setRenderParameter("mvcPath", mvcPath);
        actionResponse.setRenderParameter("redirect", redirect);
        actionResponse.setRenderParameter("windowId", windowId);

        // Retrieve associated file data
        File[] keyFiles = uploadPortletRequest.getFiles("key.file");
        File[] logoFiles = uploadPortletRequest.getFiles("logo.file");
        File[] photoFiles = uploadPortletRequest.getFiles("photo.file");
        File[] soundFiles = uploadPortletRequest.getFiles("sound.file");

        Map<String, File[]> map = new HashMap<String, File[]>();

        if (keyFiles != null) {
            map.put("key.file", keyFiles);
        }
        if (logoFiles != null) {
            map.put("logo.file", logoFiles);
        }
        if (photoFiles != null) {
            map.put("photo.file", photoFiles);
        }
        if (soundFiles != null) {
            map.put("sound.file", soundFiles);
        }

        // Update the vCard with the request parameters

        try {

            vCard = PortletUtil.getVCard(uploadPortletRequest, vCard, map);

        } catch (ImageFileFormatException iffe) {

            SessionErrors.add(actionRequest,
                    "the-image-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute("CONTACT", contact);

            return;

        } catch (KeyFileFormatException kffe) {

            SessionErrors.add(actionRequest,
                    "the-key-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute("CONTACT", contact);

            return;

        } catch (SoundFileFormatException sffe) {

            SessionErrors.add(actionRequest,
                    "the-sound-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute("CONTACT", contact);

            return;
        }

        // Store contact information in vCard format

        String card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

        // Save the contact

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Contact.class.getName(), uploadPortletRequest);

        String[] assetTagNames = PortletUtil.getAssetTagNames(vCard);

        serviceContext.setAssetTagNames(assetTagNames);

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

        // Store the updated or added contact as a request attribute
        uploadPortletRequest.setAttribute("CONTACT", contact);

    }

    /**
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

            if (resourceID.equals("exportVCards")) {
                exportVCards(resourceRequest, resourceResponse);
            } else if (resourceID.equals("serveVCard")) {
                serveVCard(resourceRequest, resourceResponse);
            } else if (resourceID.equals("serveVCards")) {
                serveVCards(resourceRequest, resourceResponse);
            } else {
                super.serveResource(resourceRequest, resourceResponse);
            }
        } catch (Exception e) {
            _log.error(e);
            throw new PortletException(e);
        }

    }

    /**
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
     * @param resourceRequest
     * @param resourceResponse
     * @since 1.1.9
     * @throws PortalException
     * @throws SystemException
     * @throws IOException
     */
    protected void serveVCards(ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws PortalException,
            SystemException, IOException {

        long[] contactIds = ParamUtil.getLongValues(resourceRequest, "rowIds");

        StringBuilder sb = new StringBuilder();

        for (long contactId : contactIds) {
            // TODO: add exception handling
            Contact contact = ContactServiceUtil.getContact(contactId);
            sb.append(contact.getCard());
            sb.append("\n");
        }

        String cards = sb.toString();

        PortletResponseUtil.sendFile(resourceRequest, resourceResponse,
                "list.vcf", cards.getBytes(), ContentTypes.TEXT_PLAIN_UTF8);

    }

    /**
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.2
     * @throws Exception
     */
    public void viewContact(ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        editContact(actionRequest, actionResponse);

    }

    private static Log _log = LogFactoryUtil.getLog(ContactManagerPortlet.class
            .getName());

}
