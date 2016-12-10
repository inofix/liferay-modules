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

import java.util.Date;
import java.util.List;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringUtil;

import ch.inofix.referencemanager.model.BibRefRelation;
import ch.inofix.referencemanager.model.Bibliography;
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
 * @modified 2016-12-03 00:07
 * @version 1.0.1
 * @see ReferenceLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.ReferenceLocalServiceUtil
 */
public class ReferenceLocalServiceImpl extends ReferenceLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.ReferenceLocalServiceUtil} to access
     * the reference local service.
     */
    @Override
    public Reference addReference(long userId, String bibTeX, ServiceContext serviceContext) throws PortalException {
        
        return addReference(userId, bibTeX, new String[0], serviceContext);

    }

    @Indexable(type = IndexableType.REINDEX)
    @Override
    public Reference addReference(long userId, String bibTeX, String[] bibliographyUuids, ServiceContext serviceContext)
            throws PortalException {
        
        // Reference

        User user = userPersistence.findByPrimaryKey(userId);
        long groupId = serviceContext.getScopeGroupId();

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

        // BibRefRelation

        for (String bibliographyUuid : bibliographyUuids) {

            Bibliography bibliography = bibliographyLocalService.getBibliographyByUuidAndGroupId(bibliographyUuid,
                    groupId);

            long bibRefRelationId = counterLocalService.increment();
            BibRefRelation bibRefRelation = bibRefRelationPersistence.create(bibRefRelationId);
            
            bibRefRelation.setGroupId(bibliography.getGroupId());
            bibRefRelation.setCompanyId(bibliography.getCompanyId());
            bibRefRelation.setUserId(bibliography.getUserId());
            bibRefRelation.setUserName(bibliography.getUserName());
            
            bibRefRelation.setBibliographyGroupId(bibliography.getGroupId());
            bibRefRelation.setBibliographyUuid(bibliographyUuid);
            bibRefRelation.setReferenceGroupId(reference.getGroupId());
            bibRefRelation.setReferenceUuid(reference.getUuid());
            
            bibRefRelationPersistence.update(bibRefRelation); 

        }

        // Resources

        if (serviceContext.isAddGroupPermissions() || serviceContext.isAddGuestPermissions()) {

            addReferenceResources(reference, serviceContext.isAddGroupPermissions(),
                    serviceContext.isAddGuestPermissions());
        } else {
            addReferenceResources(reference, serviceContext.getModelPermissions());
        }

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

    @Override
    public void addReferenceResources(Reference reference, boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException {

        resourceLocalService.addResources(reference.getCompanyId(), reference.getGroupId(), reference.getUserId(),
                Reference.class.getName(), reference.getReferenceId(), false, addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addReferenceResources(Reference reference, ModelPermissions modelPermissions) throws PortalException {

        resourceLocalService.addModelResources(reference.getCompanyId(), reference.getGroupId(), reference.getUserId(),
                Reference.class.getName(), reference.getReferenceId(), modelPermissions);
    }

    @Override
    public void addReferenceResources(long referenceId, boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException {

        Reference reference = referencePersistence.findByPrimaryKey(referenceId);

        addReferenceResources(reference, addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addReferenceResources(long referenceId, ModelPermissions modelPermissions) throws PortalException {

        Reference reference = referencePersistence.findByPrimaryKey(referenceId);

        addReferenceResources(reference, modelPermissions);
    }

    @Override
    public void deleteReferences(long groupId) throws PortalException {
        for (Reference reference : referencePersistence.findByGroupId(groupId)) {
            referenceLocalService.deleteReference(reference);
        }
    }

    @Indexable(type = IndexableType.DELETE)
    @Override
    @SystemEvent(type = SystemEventConstants.TYPE_DELETE)
    public Reference deleteReference(Reference reference) throws PortalException {

        // Reference

        referencePersistence.remove(reference);

        // Resources

        resourceLocalService.deleteResource(reference.getCompanyId(), Reference.class.getName(),
                ResourceConstants.SCOPE_INDIVIDUAL, reference.getReferenceId());

        // Subscriptions

        subscriptionLocalService.deleteSubscriptions(reference.getCompanyId(), Reference.class.getName(),
                reference.getReferenceId());

        // Asset

        assetEntryLocalService.deleteEntry(Reference.class.getName(), reference.getReferenceId());

        // Comment

        // TODO
        // deleteDiscussion(reference);

        // Expando

        expandoRowLocalService.deleteRows(reference.getReferenceId());

        // Ratings

        ratingsStatsLocalService.deleteStats(Reference.class.getName(), reference.getReferenceId());

        // Trash

        trashEntryLocalService.deleteEntry(Reference.class.getName(), reference.getReferenceId());

        // Workflow

        // TODO: do we need workflow support?
        // workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
        // reference.getCompanyId(), reference.getGroupId(),
        // Reference.class.getName(), reference.getReferenceId());

        return reference;
    }

    @Override
    public Reference deleteReference(long referenceId) throws PortalException {
        Reference reference = referencePersistence.findByPrimaryKey(referenceId);

        return referenceLocalService.deleteReference(reference);
    }

    public List<Reference> getGroupReferences(long groupId) throws PortalException, SystemException {

        return referencePersistence.findByGroupId(groupId);
    }

    @Override
    public Reference getReference(long referenceId) throws PortalException {
        return referencePersistence.findByPrimaryKey(referenceId);
    }

    @Override
    public void subscribe(long userId, long groupId) throws PortalException {
        subscriptionLocalService.addSubscription(userId, groupId, Reference.class.getName(), groupId);
    }

    @Override
    public void unsubscribe(long userId, long groupId) throws PortalException {
        subscriptionLocalService.deleteSubscription(userId, Reference.class.getName(), groupId);
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
    public Reference updateReference(long referenceId, long userId, String bibTeX, ServiceContext serviceContext)
            throws PortalException {

        // Reference

        User user = userPersistence.findByPrimaryKey(userId);

        // TODO: validate bibTeX
        // validate(bibTeX);

        Reference reference = referencePersistence.findByPrimaryKey(referenceId);
        long groupId = serviceContext.getScopeGroupId();

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

    @Override
    public void updateReferenceResources(Reference reference, ModelPermissions modelPermissions)
            throws PortalException {

        resourceLocalService.updateResources(reference.getCompanyId(), reference.getGroupId(),
                Reference.class.getName(), reference.getReferenceId(), modelPermissions);
    }

    @Override
    public void updateReferenceResources(Reference reference, String[] groupPermissions, String[] guestPermissions)
            throws PortalException {

        resourceLocalService.updateResources(reference.getCompanyId(), reference.getGroupId(),
                Reference.class.getName(), reference.getReferenceId(), groupPermissions, guestPermissions);
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceLocalServiceImpl.class);

}