/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.model.RefRefRelation;
import ch.inofix.referencemanager.service.base.RefRefRelationLocalServiceBaseImpl;

/**
 * The implementation of the ref ref relation local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.RefRefRelationLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2017-02-15 22:47
 * @modified 2017-02-15 22:47
 * @version 1.0.0
 * @see RefRefRelationLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.RefRefRelationLocalServiceUtil
 */
@ProviderType
public class RefRefRelationLocalServiceImpl extends RefRefRelationLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.RefRefRelationLocalServiceUtil} to
     * access the ref ref relation local service.
     */

    public RefRefRelation addRefRefRelation(long userId, long referenceId1, long referenceId2,
            ServiceContext serviceContext) throws PortalException {

        return addRefRefRelation(userId, referenceId1, referenceId2, null, serviceContext);

    }

    public RefRefRelation addRefRefRelation(long userId, long referenceId1, long referenceId2, String type,
            ServiceContext serviceContext) throws PortalException {

        // RefRefRelation

        User user = userPersistence.findByPrimaryKey(userId);
        long groupId = serviceContext.getScopeGroupId();

        RefRefRelation refRefRelation = null;

        refRefRelation = refRefRelationPersistence.fetchByR_R(referenceId1, referenceId2);

        if (refRefRelation == null) {

            refRefRelation = refRefRelationPersistence.fetchByR_R(referenceId2, referenceId1);

        }

        if (refRefRelation == null) {

            long refRefRelationId = counterLocalService.increment();

            refRefRelation = refRefRelationPersistence.create(refRefRelationId);

            refRefRelation.setUuid(serviceContext.getUuid());
            refRefRelation.setGroupId(groupId);
            refRefRelation.setCompanyId(user.getCompanyId());
            refRefRelation.setUserId(user.getUserId());
            refRefRelation.setUserName(user.getFullName());
            refRefRelation.setExpandoBridgeAttributes(serviceContext);

            refRefRelation.setReferenceId2(referenceId1);
            refRefRelation.setReferenceId2(referenceId2);
            refRefRelation.setType(type);

            refRefRelationPersistence.update(refRefRelation);

            // Update (and re-index) both parts of the relation

            referenceLocalService.reIndexReference(referenceId1);
            referenceLocalService.reIndexReference(referenceId2);

        } else {
            _log.info("The refRefRelation already exists.");
        }

        return refRefRelation;
    }

    public void deleteByReferenceId(long referenceId) throws PortalException {

        List<RefRefRelation> refRefRelations = refRefRelationPersistence.findByReferenceId1(referenceId);
        refRefRelations.addAll(refRefRelationPersistence.findByReferenceId2(referenceId));

        for (RefRefRelation refRefRelation : refRefRelations) {
            refRefRelationLocalService.deleteRefRefRelation(refRefRelation);
        }

    }

    public void deleteRefRefRelation(long referenceId1, long referenceId2) throws PortalException {

        RefRefRelation refRefRelation = refRefRelationPersistence.fetchByR_R(referenceId1, referenceId2);

        deleteRefRefRelation(refRefRelation);

        // Update (and re-index) both parts of the relation

        referenceLocalService.reIndexReference(referenceId1);
        referenceLocalService.reIndexReference(referenceId2);

    }

    private static final Log _log = LogFactoryUtil.getLog(BibRefRelationLocalServiceImpl.class);

}