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

import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.constants.ReferenceActionKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.base.ReferenceServiceBaseImpl;
import ch.inofix.referencemanager.service.permission.ReferenceManagerPortletPermission;
import ch.inofix.referencemanager.service.permission.ReferencePermission;

/**
 * The implementation of the reference remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.ReferenceService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-03-28 17:08
 * @modified 2016-12-03 00:16
 * @version 1.0.4
 * @see ReferenceServiceBaseImpl
 * @see ch.inofix.referencemanager.service.ReferenceServiceUtil
 */
@ProviderType
public class ReferenceServiceImpl extends ReferenceServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.ReferenceServiceUtil} to access the
     * reference remote service.
     */

    /**
     * 
     * @param userId
     * @param bibTeX
     * @param serviceContext
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Reference addReference(long userId, String bibTeX, ServiceContext serviceContext) throws PortalException {
        
        ReferenceManagerPortletPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
                ReferenceActionKeys.ADD_REFERENCE);

        return referenceLocalService.addReference(userId, bibTeX, serviceContext);
    }

    /**
     * 
     * @param userId
     * @param bibTeX
     * @param bibliographyUuids
     * @param serviceContext
     * @return
     * @since 1.0.4
     * @throws PortalException
     */
    public Reference addReference(long userId, String bibTeX, String[] bibliographyUuids, ServiceContext serviceContext)
            throws PortalException {
        
        ReferenceManagerPortletPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
                ReferenceActionKeys.ADD_REFERENCE);

        return referenceLocalService.addReference(userId, bibTeX, bibliographyUuids, serviceContext);
    }

    /**
     * 
     * @param referenceId
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Reference deleteReference(long referenceId) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), referenceId, ReferenceActionKeys.DELETE);

        return referenceLocalService.deleteReference(referenceId);

    }

    /**
     * 
     * @param groupId
     * @return
     * @since 1.0.3
     * @throws PortalException
     */
    public List<Reference> deleteGroupReferences(long groupId) throws PortalException {

        ReferenceManagerPortletPermission.check(getPermissionChecker(), groupId,
                ReferenceActionKeys.DELETE_GROUP_REFERENCES);

        List<Reference> references = referenceLocalService.getGroupReferences(groupId);

        for (Reference reference : references) {

            reference = referenceLocalService.deleteReference(reference.getReferenceId());

        }

        return references;

    }

    /**
     * 
     * @param referenceId
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Reference getReference(long referenceId) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), referenceId, ActionKeys.VIEW);

        return referenceLocalService.getReference(referenceId);
    }

    public Hits search(long userId, long groupId, String keywords, int start, int end, Sort sort)
            throws PortalException {

        if (sort == null) {
            sort = new Sort(Field.MODIFIED_DATE, true);
        }

        Indexer<Reference> indexer = IndexerRegistryUtil.getIndexer(Reference.class.getName());

        SearchContext searchContext = new SearchContext();

        searchContext.setAttribute(Field.STATUS, WorkflowConstants.STATUS_ANY);

        searchContext.setAttribute("paginationType", "more");

        Group group = GroupLocalServiceUtil.getGroup(groupId);

        searchContext.setCompanyId(group.getCompanyId());

        searchContext.setEnd(end);
        searchContext.setGroupIds(new long[] { groupId });
        searchContext.setSorts(sort);
        searchContext.setStart(start);
        searchContext.setEnd(end);
        searchContext.setGroupIds(new long[] { groupId });
        searchContext.setStart(start);
        searchContext.setUserId(userId);

        return indexer.search(searchContext);

    }

    /**
     * @param groupId
     * @since 1.0.0
     * @throws PortalException
     */
    public void subscribe(long groupId) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), groupId, ActionKeys.SUBSCRIBE);

        referenceLocalService.subscribe(getUserId(), groupId);
    }

    /**
     * @param groupId
     * @since 1.0.0
     * @throws PortalException
     */
    public void unsubscribe(long groupId) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), groupId, ActionKeys.SUBSCRIBE);

        referenceLocalService.unsubscribe(getUserId(), groupId);
    }

    /**
     * 
     * @param referenceId
     * @param userId
     * @param bibTeX
     * @param serviceContext
     * @return
     * @since 1.0.0
     * @throws PortalException
     */
    public Reference updateReference(long referenceId, long userId, String bibTeX, ServiceContext serviceContext)
            throws PortalException {

        ReferencePermission.check(getPermissionChecker(), referenceId, ActionKeys.UPDATE);

        return referenceLocalService.updateReference(referenceId, userId, bibTeX, serviceContext);
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceServiceImpl.class);

}