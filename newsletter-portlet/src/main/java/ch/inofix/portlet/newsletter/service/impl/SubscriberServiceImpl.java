package ch.inofix.portlet.newsletter.service.impl;

import java.util.ArrayList;
import java.util.List;

import ch.inofix.portlet.newsletter.service.base.SubscriberServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.SimpleFacet;
import com.liferay.portal.kernel.util.Validator;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Member;

/**
 * The implementation of the subscriber remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.SubscriberService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-09 21:10
 * @modified 2016-10-10 14:09
 * @version 1.0.2
 * @see ch.inofix.portlet.newsletter.service.base.SubscriberServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.SubscriberServiceUtil
 */
public class SubscriberServiceImpl extends SubscriberServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.SubscriberServiceUtil} to access the
     * subscriber remote service.
     */

    @Override
    public Hits search(long groupId, SearchContext searchContext, int start,
            int end) throws PortalException, SystemException {

        return subscriberLocalService
                .search(groupId, searchContext, start, end);
    }

    private Document getDocument(long groupId, SearchContext searchContext,
            String vCardUID) throws PortalException, SystemException {

        searchContext.setAttribute("vCardUID", vCardUID);
        Facet simpleFacet = new SimpleFacet(searchContext);
        simpleFacet.setFieldName("vCardUID");
        searchContext.addFacet(simpleFacet);

        Hits hits = search(groupId, searchContext, 0, 1);

        if (hits.getLength() > 0) {
            return hits.doc(0);
        } else {
            return null;
        }

    }

    @Override
    public List<Document> getSubscribersFromVCardGroup(long groupId,
            SearchContext searchContext, String vCardGroupId, int start, int end)
            throws PortalException, SystemException {

        Document group = getDocument(groupId, searchContext, vCardGroupId);
        List<Document> subscribers = new ArrayList<Document>();

        if (group != null) {

            String content = group.get(Field.CONTENT);

            VCard vCard = Ezvcard.parse(content).first();

            if (vCard != null) {
                List<Member> members = vCard.getMembers();

                for (Member member : members) {

                    String uri = member.getUri();
                    String[] tokens = uri.split(":");
                    String uuid = null;
                    if (tokens.length > 2) {
                        uuid = tokens[2];
                    }

                    if (uuid != null) {

                        Document memberDocument = getDocument(groupId,
                                searchContext, uuid);
                        if (memberDocument != null) {

                            // Only add members with email-address

                            String email = memberDocument.get("email");
                            if (Validator.isNotNull(email)) {
                                subscribers.add(memberDocument);
                            }

                        }
                    }
                }
            }
        }

        if (end > subscribers.size()) {
            end = subscribers.size();
        }

        return subscribers.subList(start, end);
    }

    @Override
    public int getSubscribersFromVCardGroupCount(long groupId,
            SearchContext searchContext, String vCardGroupId)
            throws PortalException, SystemException {

        List<Document> documents = getSubscribersFromVCardGroup(groupId,
                searchContext, vCardGroupId, 0, Integer.MAX_VALUE);

        return documents.size();

    }

    private static Log _log = LogFactoryUtil.getLog(SubscriberServiceImpl.class
            .getName());
}
