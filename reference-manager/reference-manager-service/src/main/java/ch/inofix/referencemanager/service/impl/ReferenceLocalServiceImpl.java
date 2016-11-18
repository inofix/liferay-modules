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

import java.util.Date;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringUtil;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.base.ReferenceLocalServiceBaseImpl;
import ch.inofix.referencemanager.social.ReferenceActivityKeys;

/**
 * The implementation of the reference local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.ReferenceLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @created 2016-03-28 17:08
 * @modified 2016-11-18 19:30
 * @version 0.2.0
 * @see ReferenceLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.ReferenceLocalServiceUtil
 */
@ProviderType
public class ReferenceLocalServiceImpl extends ReferenceLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.ReferenceLocalServiceUtil} to access
     * the reference local service.
     */
    @Indexable(type = IndexableType.REINDEX)
    @Override
    public Reference addReference(long userId, long groupId, String bibTeX, ServiceContext serviceContext)
            throws PortalException {

        // Reference

        User user = userPersistence.findByPrimaryKey(userId);

        // TODO: validate bibTeX
        // validate(bibTeX);

        long referenceId = counterLocalService.increment();

        Reference reference = referencePersistence.create(referenceId);

        reference.setUuid(serviceContext.getUuid());
        reference.setGroupId(groupId);
        reference.setCompanyId(user.getCompanyId());
        reference.setUserId(user.getUserId());
        reference.setUserName(user.getFullName());
        reference.setExpandoBridgeAttributes(serviceContext);

        reference.setBibTeX(bibTeX);

        referencePersistence.update(reference);

        // Resources

        // TODO: configure default permissions
        // resourceLocalService.addModelResources(reference, serviceContext);

        // Asset

        updateAsset(userId, reference, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

        extraDataJSONObject.put("title", reference.getCitation());

        socialActivityLocalService.addActivity(userId, groupId, Reference.class.getName(), referenceId,
                ReferenceActivityKeys.ADD_REFERENCE, extraDataJSONObject.toString(), 0);

        return reference;

    }

    public void updateAsset(long userId, Reference reference, long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds, Double priority) throws PortalException {

        boolean visible = false;

        Date publishDate = null;

        if (reference.isApproved()) {
            visible = true;

            publishDate = reference.getCreateDate();
        }

        String summary = HtmlUtil.extractText(StringUtil.shorten(reference.getCitation(), 500));

        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId, reference.getGroupId(),
                reference.getCreateDate(), reference.getModifiedDate(), Reference.class.getName(),
                reference.getReferenceId(), reference.getUuid(), 0, assetCategoryIds, assetTagNames, true, visible,
                null, null, publishDate, null, ContentTypes.TEXT_HTML, reference.getTitle(), reference.getCitation(),
                summary, null, null, 0, 0, priority);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(), assetLinkEntryIds,
                AssetLinkConstants.TYPE_RELATED);
    }

    @Indexable(type = IndexableType.REINDEX)
    public Reference updateReference(long referenceId, long userId, long groupId, String bibTeX,
            ServiceContext serviceContext) throws PortalException {

        // Reference

        User user = userPersistence.findByPrimaryKey(userId);

        // TODO: validate bibTeX
        // validate(bibTeX);

        Reference reference = referencePersistence.findByPrimaryKey(referenceId);

        reference.setUuid(serviceContext.getUuid());
        reference.setGroupId(groupId);
        reference.setCompanyId(user.getCompanyId());
        reference.setUserId(user.getUserId());
        reference.setUserName(user.getFullName());
        reference.setExpandoBridgeAttributes(serviceContext);

        reference.setBibTeX(bibTeX);

        referencePersistence.update(reference);

        // Resources

        resourceLocalService.addModelResources(reference, serviceContext);

        // Asset

        updateAsset(userId, reference, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

        extraDataJSONObject.put("title", reference.getCitation());

        socialActivityLocalService.addActivity(userId, groupId, Reference.class.getName(), referenceId,
                ReferenceActivityKeys.UPDATE_REFERENCE, extraDataJSONObject.toString(), 0);

        return reference;

    }

    public String referenceLocal() {
        return "referenceLocal";
    }
}