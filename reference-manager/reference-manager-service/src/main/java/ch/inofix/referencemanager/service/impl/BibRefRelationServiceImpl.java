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

import com.liferay.portal.kernel.exception.PortalException;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.constants.ReferenceActionKeys;
import ch.inofix.referencemanager.model.BibRefRelation;
import ch.inofix.referencemanager.service.base.BibRefRelationServiceBaseImpl;
import ch.inofix.referencemanager.service.permission.ReferencePermission;

/**
 * The implementation of the bib ref relation remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.BibRefRelationService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2017-02-03 21:36
 * @modified 2017-02-03 21:36
 * @version 1.0.0
 * @see BibRefRelationServiceBaseImpl
 * @see ch.inofix.referencemanager.service.BibRefRelationServiceUtil
 */
@ProviderType
public class BibRefRelationServiceImpl extends BibRefRelationServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.BibRefRelationServiceUtil} to access
     * the bib ref relation remote service.
     */

    public void deleteBibRefRelation(long bibliographyId, long referenceId) throws PortalException {

        // If the user has the permission to delete the reference, he is also
        // allowed to remove bibRefRelation.

        ReferencePermission.check(getPermissionChecker(), referenceId, ReferenceActionKeys.DELETE);

        bibRefRelationLocalService.deleteBibRefRelation(bibliographyId, referenceId);

    }
}