package ch.inofix.portlet.contact.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.StopWatch;

import ch.inofix.portlet.contact.NoSuchContactException;
import ch.inofix.portlet.contact.model.Contact;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.ContactServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Categories;
import ezvcard.property.Uid;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-07 13:04
 * @modified 2016-10-07 13:04
 * @version 1.0.0
 *
 */
public class ContactImporter {

    public void importContacts(long userId, long groupId,
            boolean privateLayout, Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        User user = UserLocalServiceUtil.getUser(userId);
        serviceContext.setCompanyId(user.getCompanyId());

        // TODO: read from parameterMap
        boolean updateExisting = true;

        try {

            int numProcessed = 1;
            // int numImported = 0;
            // int numIgnored = 0;
            // int numUpdated = 0;

            // TODO:
            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            List<VCard> vCards = Ezvcard.parse(file).all();

            _log.info("Start import");

            for (VCard vCard : vCards) {

                Uid uidObj = vCard.getUid();
                String uid = null;

                if (Validator.isNotNull(uidObj)) {
                    uid = uidObj.getValue();
                } else {
                    uid = UUID.randomUUID().toString();
                    uidObj = new Uid(uid);
                    vCard.setUid(uidObj);
                }

                String[] assetTagNames = getAssetTagNames(vCard);

                serviceContext.setAssetTagNames(assetTagNames);

                String card = Ezvcard.write(vCard).version(VCardVersion.V4_0)
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
                    ContactServiceUtil.addContact(userId, groupId, card, uid,
                            serviceContext);
                    // numImported++;
                } else {

                    if (updateExisting) {

                        ContactServiceUtil.updateContact(userId, groupId,
                                contact.getContactId(), card, uid,
                                serviceContext);
                        // numUpdated++;

                    } else {
                        // numIgnored++;
                    }
                }

                if (numProcessed % 100 == 0) {

                    float completed = ((Integer) numProcessed).floatValue()
                            / ((Integer) vCards.size()).floatValue() * 100;

                    _log.info("Processed " + numProcessed + " of "
                            + vCards.size() + " cards in "
                            + stopWatch.getTime() + " ms (" + completed + "%).");
                }

                numProcessed++;

            }

            _log.info("Import took " + stopWatch.getTime() + " ms");

        } catch (IOException ioe) {
            _log.error(ioe);
        }

    }

    private static String[] getAssetTagNames(VCard vCard) {

        List<Categories> categories = vCard.getCategoriesList();

        List<String> assetTags = new ArrayList<String>();

        for (Categories category : categories) {

            List<String> values = category.getValues();
            assetTags.addAll(values);

        }

        String[] assetTagNames = new String[0];
        return assetTags.toArray(assetTagNames);
    }

    private static Log _log = LogFactoryUtil.getLog(ContactImporter.class
            .getName());

}
