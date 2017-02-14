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

import java.io.File;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.constants.ReferenceActionKeys;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.base.ReferenceServiceBaseImpl;
import ch.inofix.referencemanager.service.permission.ReferenceEditorPortletPermission;
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
 * @modified 2017-02-14 22:03
 * @version 1.1.2
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

        ReferenceEditorPortletPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
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
    public Reference addReference(long userId, String bibTeX, long[] bibliographyIds, ServiceContext serviceContext)
            throws PortalException {

        ReferenceEditorPortletPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
                ReferenceActionKeys.ADD_REFERENCE);

        return referenceLocalService.addReference(userId, bibTeX, bibliographyIds, serviceContext);
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
    public Reference deleteReference(long referenceId) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), referenceId, ReferenceActionKeys.DELETE);

        return referenceLocalService.deleteReference(referenceId);

    }

    /**
     * 
     * @return
     * @since 1.0.5
     * @throws PortalException
     */
    public List<Reference> deleteReferences() throws PortalException {

        // TODO: Check DELETE_ALL_REFERENCES permission!
        // ReferenceManagerPortletPermission.check(getPermissionChecker(),
        // groupId,
        // ReferenceActionKeys.DELETE_GROUP_REFERENCES);

        List<Reference> references = referenceLocalService.getReferences(0, Integer.MAX_VALUE);

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

    /**
     * @param userId
     * @param groupId
     * @param keywords
     * @param bibliographyId
     * @param start
     * @param end
     * @param sort
     */
    public Hits search(long userId, long groupId, String keywords, long bibliographyId, int start, int end, Sort sort)
            throws PortalException {

        return referenceLocalService.search(userId, groupId, keywords, bibliographyId, start, end, sort);

    }

    /**
     * 
     * @param userId
     * @param taskName
     * @param groupId
     * @param privateLayout
     * @param parameterMap
     * @param file
     * @param serviceContext
     * @since 1.0.8
     * @return
     * @throws PortalException
     */
    public long importReferencesInBackground(long userId, String taskName, long groupId, boolean privateLayout,
            Map<String, String[]> parameterMap, File file, ServiceContext serviceContext) throws PortalException {

        ReferenceManagerPortletPermission.check(getPermissionChecker(), groupId, ReferenceActionKeys.IMPORT_REFERENCES);

        return referenceLocalService.importReferencesInBackground(userId, taskName, groupId, privateLayout,
                parameterMap, file, serviceContext);

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

    /**
     * 
     * @param referenceId
     * @param userId
     * @param bibTeX
     * @param bibliographyIds
     * @param serviceContext
     * @return
     * @since 1.0.8
     * @throws PortalException
     */
    public Reference updateReference(long referenceId, long userId, String bibTeX, long[] bibliographyIds,
            ServiceContext serviceContext) throws PortalException {

        ReferencePermission.check(getPermissionChecker(), referenceId, ActionKeys.UPDATE);

        return referenceLocalService.updateReference(referenceId, userId, bibTeX, bibliographyIds, serviceContext);
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceServiceImpl.class);

}