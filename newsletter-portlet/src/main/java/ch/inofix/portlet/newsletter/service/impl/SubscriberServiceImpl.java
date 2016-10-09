package ch.inofix.portlet.newsletter.service.impl;

import java.util.ArrayList;
import java.util.List;

import ch.inofix.portlet.newsletter.service.base.SubscriberServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.SimpleFacet;

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
 * @modified 2016-10-09 23:03
 * @version 1.0.1
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
    public Hits search(long groupId, SearchContext searchContext, int start, int end)
            throws PortalException, SystemException {

        _log.info("search()");

        // TODO: Check permissions
        return subscriberLocalService.search(groupId, searchContext, start, end);
    }

    @Override
    public List<Document> getSubscribersFromVCardGroup(long groupId,
            SearchContext searchContext, String vCardGroupId)
            throws PortalException, SystemException {

        _log.info("getSubscribersFromVCardGroup()");
        _log.info("vCardGroupId = " + vCardGroupId);

        searchContext.setAttribute("vCardUID", vCardGroupId);
        Facet simpleFacet = new SimpleFacet(searchContext);
        simpleFacet.setFieldName("vCardUID");
        searchContext.addFacet(simpleFacet);

        Hits hits = search(groupId, searchContext, 0, Integer.MAX_VALUE);

        _log.info("hits.getLength() = " + hits.getLength());

        List<Document> documents = new ArrayList<Document>();

        // Search

        return documents;

    }

    private static Log _log = LogFactoryUtil.getLog(SubscriberServiceImpl.class
            .getName());
}
