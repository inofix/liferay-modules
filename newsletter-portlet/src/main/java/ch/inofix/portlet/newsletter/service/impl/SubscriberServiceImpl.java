package ch.inofix.portlet.newsletter.service.impl;

import ch.inofix.portlet.newsletter.service.base.SubscriberServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;

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
 * @modified 2016-10-09 21:10
 * @version 1.0.0
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

    public Hits search(SearchContext searchContext, int start, int end)
            throws PortalException, SystemException {

        _log.info("search()");

        // TODO: Check permissions
        return subscriberLocalService.search(searchContext, start, end);
    }

    private static Log _log = LogFactoryUtil.getLog(SubscriberServiceImpl.class
            .getName());
}
