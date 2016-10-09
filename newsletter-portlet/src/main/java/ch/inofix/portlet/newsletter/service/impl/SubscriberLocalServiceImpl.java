package ch.inofix.portlet.newsletter.service.impl;

import ch.inofix.portlet.newsletter.service.base.SubscriberLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;

/**
 * The implementation of the subscriber local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.newsletter.service.SubscriberLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-09 20:49
 * @modified 2016-10-09 23:02
 * @version 1.0.1
 * @see ch.inofix.portlet.newsletter.service.base.SubscriberLocalServiceBaseImpl
 * @see ch.inofix.portlet.newsletter.service.SubscriberLocalServiceUtil
 */
public class SubscriberLocalServiceImpl extends SubscriberLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.newsletter.service.SubscriberLocalServiceUtil} to
     * access the subscriber local service.
     */

    @Override
    public Hits search(long groupId, SearchContext searchContext, int start,
            int end) throws PortalException, SystemException {

        _log.info("search()");

        searchContext.setStart(start);
        searchContext.setEnd(end);

        Facet scopeFacet = new ScopeFacet(searchContext);
        scopeFacet.setStatic(true);
        searchContext.addFacet(scopeFacet);

        Indexer indexer = FacetedSearcher.getInstance();

        Hits hits = indexer.search(searchContext);

        return hits;

    }

    private static Log _log = LogFactoryUtil
            .getLog(SubscriberLocalServiceImpl.class.getName());
}
