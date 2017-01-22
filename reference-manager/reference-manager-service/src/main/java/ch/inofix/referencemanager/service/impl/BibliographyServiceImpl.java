/**
 * Copyright (c) 2016-present Inofix GmbH, Luzern. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.inofix.referencemanager.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.constants.BibliographyActionKeys;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.base.BibliographyServiceBaseImpl;
import ch.inofix.referencemanager.service.permission.BibliographyManagerPortletPermission;
import ch.inofix.referencemanager.service.permission.BibliographyPermission;

/**
 * The implementation of the bibliography remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.BibliographyService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-11-29 21:27
 * @modified 2017-01-22 17:55
 * @version 1.0.6
 * @see BibliographyServiceBaseImpl
 * @see ch.inofix.referencemanager.service.BibliographyServiceUtil
 */
@ProviderType
public class BibliographyServiceImpl extends BibliographyServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.BibliographyServiceUtil} to access the
     * bibliography remote service.
     */

    /**
     * 
     * @param userId
     * @param title
     * @param description
     * @param serviceContext
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Bibliography addBibliography(long userId, String title, String description, String urlTitle, String comments,
            String preamble, String strings, ServiceContext serviceContext) throws PortalException {

        BibliographyManagerPortletPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
                BibliographyActionKeys.ADD_BIBLIOGRAPHY);

        return bibliographyLocalService.addBibliography(userId, title, description, urlTitle, comments, preamble,
                strings, serviceContext);
    }

    /**
     * 
     * @param bibliographyId
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Bibliography deleteBibliography(long bibliographyId) throws PortalException {

        BibliographyPermission.check(getPermissionChecker(), bibliographyId, BibliographyActionKeys.DELETE);

        return bibliographyLocalService.deleteBibliography(bibliographyId);

    }

    /**
     * 
     * @param bibliographyId
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Bibliography getBibliography(long bibliographyId) throws PortalException {

        BibliographyPermission.check(getPermissionChecker(), bibliographyId, ActionKeys.VIEW);

        return bibliographyLocalService.getBibliography(bibliographyId);
    }

    /**
     * 
     * @param uuid
     * @param groupId
     * @return
     * @since 1.0.4
     * @throws PortalException
     */
    public Bibliography getBibliography(String uuid, long groupId) throws PortalException {

        Bibliography bibliography = bibliographyLocalService.getBibliographyByUuidAndGroupId(uuid, groupId);

        BibliographyPermission.check(getPermissionChecker(), bibliography, ActionKeys.VIEW);

        return bibliography;

    }

    /**
     * 
     * @param groupId
     * @param urlTitle
     * @return
     * @since 1.0.4
     * @throws PortalException
     */
    public Bibliography getBibliography(long groupId, String urlTitle) throws PortalException {

        Bibliography bibliography = bibliographyLocalService.getBibliography(groupId, urlTitle);

        BibliographyPermission.check(getPermissionChecker(), bibliography, ActionKeys.VIEW);

        return bibliography;

    }

    /**
     * @param userId
     *            the userId of the current user
     * @param groupId
     *            the scopeGroupId of the bibliography. 0 means: any scope.
     * @param ownerUserId
     *            the userId of the bibliography owner. -1 means: ignore
     *            ownerUserId parameter.
     * @param keywords
     * @param start
     * @param end
     * @param sort
     * @return the hits for the given parameters
     * @since 1.0.0
     * @throws PortalException
     */
    public Hits search(long userId, long groupId, long ownerUserId, String keywords, int start, int end, Sort sort)
            throws PortalException {

        if (sort == null) {
            sort = new Sort(Field.MODIFIED_DATE, true);
        }

        Indexer<Bibliography> indexer = IndexerRegistryUtil.getIndexer(Bibliography.class.getName());

        SearchContext searchContext = new SearchContext();

        searchContext.setAttribute(Field.STATUS, WorkflowConstants.STATUS_ANY);

        searchContext.setAttribute("paginationType", "more");

        User user = UserLocalServiceUtil.getUser(userId);

        searchContext.setCompanyId(user.getCompanyId());

        searchContext.setEnd(end);

        if (groupId > 0) {
            searchContext.setGroupIds(new long[] { groupId });
        }
        searchContext.setSorts(sort);
        searchContext.setStart(start);
        searchContext.setUserId(userId);
        searchContext.setOwnerUserId(ownerUserId);

        return indexer.search(searchContext);

    }

    /**
     * @param groupId
     * @since 1.0.0
     * @throws PortalException
     */
    public void subscribe(long groupId) throws PortalException {

        BibliographyPermission.check(getPermissionChecker(), groupId, ActionKeys.SUBSCRIBE);

        bibliographyLocalService.subscribe(getUserId(), groupId);
    }

    /**
     * @param groupId
     * @since 1.0.0
     * @throws PortalException
     */
    public void unsubscribe(long groupId) throws PortalException {

        BibliographyPermission.check(getPermissionChecker(), groupId, ActionKeys.SUBSCRIBE);

        bibliographyLocalService.unsubscribe(getUserId(), groupId);
    }

    /**
     * 
     * @param bibliographyId
     * @param userId
     * @param title
     * @paramet description
     * @param serviceContext
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Bibliography updateBibliography(long bibliographyId, long userId, String title, String description,
            String urlTitle, String comments, String preamble, String strings, ServiceContext serviceContext)
            throws PortalException {

        BibliographyPermission.check(getPermissionChecker(), bibliographyId, ActionKeys.UPDATE);

        return bibliographyLocalService.updateBibliography(bibliographyId, userId, title, description, urlTitle,
                comments, preamble, strings, serviceContext);
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyServiceImpl.class);

}