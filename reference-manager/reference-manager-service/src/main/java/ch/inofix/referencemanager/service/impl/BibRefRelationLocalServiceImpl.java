/**
 * Copyright (c) 2000-present Inofix GmbH, Luzern. All rights reserved.
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
import ch.inofix.referencemanager.model.BibRefRelation;
import ch.inofix.referencemanager.service.base.BibRefRelationLocalServiceBaseImpl;

/**
 * The implementation of the bib ref relation local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.BibRefRelationLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-12-03 15:33
 * @modified 2017-02-03 22:18
 * @version 1.0.5
 * @see BibRefRelationLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.BibRefRelationLocalServiceUtil
 */
@ProviderType
public class BibRefRelationLocalServiceImpl extends BibRefRelationLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.BibRefRelationLocalServiceUtil} to
     * access the bib ref relation local service.
     */

    public BibRefRelation addBibRefRelation(long userId, long bibliographyId, long referenceId,
            ServiceContext serviceContext) throws PortalException {

        // BibRefRelation

        User user = userPersistence.findByPrimaryKey(userId);
        long groupId = serviceContext.getScopeGroupId();

        BibRefRelation bibRefRelation = null;

        bibRefRelation = bibRefRelationPersistence.fetchByB_R(bibliographyId, referenceId);

        if (bibRefRelation == null) {

            long bibRefRelationId = counterLocalService.increment();

            bibRefRelation = bibRefRelationPersistence.create(bibRefRelationId);

            bibRefRelation.setUuid(serviceContext.getUuid());
            bibRefRelation.setGroupId(groupId);
            bibRefRelation.setCompanyId(user.getCompanyId());
            bibRefRelation.setUserId(user.getUserId());
            bibRefRelation.setUserName(user.getFullName());
            bibRefRelation.setExpandoBridgeAttributes(serviceContext);

            bibRefRelation.setBibliographyId(bibliographyId);
            bibRefRelation.setReferenceId(referenceId);

            bibRefRelationPersistence.update(bibRefRelation);

            // Update (and re-index) both parts of the relation

            bibliographyLocalService.reIndexBibligraphy(bibliographyId);
            referenceLocalService.reIndexReference(referenceId);

        } else {
            _log.info("The bibRefRelation already exists.");
        }

        return bibRefRelation;
    }

    public void deleteBibRefRelation(long bibliographyId, long referenceId) throws PortalException {

        BibRefRelation bibRefRelation = bibRefRelationPersistence.fetchByB_R(bibliographyId, referenceId);

        deleteBibRefRelation(bibRefRelation);

        // Update (and re-index) both parts of the relation

        bibliographyLocalService.reIndexBibligraphy(bibliographyId);
        referenceLocalService.reIndexReference(referenceId);

    }

    public void deleteByBibliographyId(long bibliographyId) throws PortalException {

        List<BibRefRelation> bibRefRelations = bibRefRelationPersistence.findByBibliographyId(bibliographyId);

        for (BibRefRelation bibRefRelation : bibRefRelations) {

            long referenceId = bibRefRelation.getReferenceId();

            deleteBibRefRelation(bibRefRelation);

            // Re-index the remaining part of the relation

            referenceLocalService.reIndexReference(referenceId);
        }
    }

    public void deleteByReferenceId(long referenceId) throws PortalException {

        List<BibRefRelation> bibRefRelations = bibRefRelationPersistence.findByReferenceId(referenceId);

        for (BibRefRelation bibRefRelation : bibRefRelations) {

            long bibliographyId = bibRefRelation.getBibliographyId();

            deleteBibRefRelation(bibRefRelation);

            // Re-index the remaining part of the relation

            bibliographyLocalService.reIndexBibligraphy(bibliographyId);

        }
    }

    public List<BibRefRelation> getBibRefRelationsByBibliographyId(long bibliographyId) {
        return bibRefRelationPersistence.findByBibliographyId(bibliographyId);
    }

    public List<BibRefRelation> getBibRefRelationsByReferenceId(long referenceId) {
        return bibRefRelationPersistence.findByReferenceId(referenceId);
    }

    private static final Log _log = LogFactoryUtil.getLog(BibRefRelationLocalServiceImpl.class);

}