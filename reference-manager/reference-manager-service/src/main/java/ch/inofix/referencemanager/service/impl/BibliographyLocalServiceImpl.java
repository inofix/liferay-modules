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
import com.liferay.portal.kernel.util.Validator;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.exception.BibliographyUrlTitleException;
import ch.inofix.referencemanager.exception.DuplicateUrlTitleException;
import ch.inofix.referencemanager.exception.NoSuchBibliographyException;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.base.BibliographyLocalServiceBaseImpl;
import ch.inofix.referencemanager.social.BibliographyActivityKeys;

/**
 * The implementation of the bibliography local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.BibliographyLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-11-29 21:27
 * @modified 2017-02-06 22:33
 * @version 1.0.6
 * @see BibliographyLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.BibliographyLocalServiceUtil
 */
@ProviderType
public class BibliographyLocalServiceImpl extends BibliographyLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.referencemanager.service.BibliographyLocalServiceUtil} to
     * access the bibliography local service.
     */
    @Indexable(type = IndexableType.REINDEX)
    @Override
    public Bibliography addBibliography(long userId, String title, String description, String urlTitle, String comments,
            String preamble, String strings, ServiceContext serviceContext) throws PortalException {

        // Bibliography

        User user = userPersistence.findByPrimaryKey(userId);
        long groupId = serviceContext.getScopeGroupId();

        validate(groupId, urlTitle);

        long bibliographyId = counterLocalService.increment();

        Bibliography bibliography = bibliographyPersistence.create(bibliographyId);

        bibliography.setUuid(serviceContext.getUuid());
        bibliography.setGroupId(groupId);
        bibliography.setCompanyId(user.getCompanyId());
        bibliography.setUserId(user.getUserId());
        bibliography.setUserName(user.getFullName());
        bibliography.setExpandoBridgeAttributes(serviceContext);

        bibliography.setTitle(title);
        bibliography.setDescription(description);
        bibliography.setUrlTitle(urlTitle);
        bibliography.setComments(comments);
        bibliography.setPreamble(preamble);
        bibliography.setStrings(strings);

        bibliographyPersistence.update(bibliography);

        // Resources

        if (serviceContext.isAddGroupPermissions() || serviceContext.isAddGuestPermissions()) {

            addBibliographyResources(bibliography, serviceContext.isAddGroupPermissions(),
                    serviceContext.isAddGuestPermissions());
        } else {
            addBibliographyResources(bibliography, serviceContext.getModelPermissions());
        }

        // Asset

        updateAsset(userId, bibliography, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

        extraDataJSONObject.put("title", bibliography.getTitle());

        socialActivityLocalService.addActivity(userId, groupId, Bibliography.class.getName(), bibliographyId,
                BibliographyActivityKeys.ADD_BIBLIOGRAPHY, extraDataJSONObject.toString(), 0);

        return bibliography;

    }

    @Override
    public void addBibliographyResources(Bibliography bibliography, boolean addGroupPermissions,
            boolean addGuestPermissions) throws PortalException {

        resourceLocalService.addResources(bibliography.getCompanyId(), bibliography.getGroupId(),
                bibliography.getUserId(), Bibliography.class.getName(), bibliography.getBibliographyId(), false,
                addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addBibliographyResources(Bibliography bibliography, ModelPermissions modelPermissions)
            throws PortalException {

        resourceLocalService.addModelResources(bibliography.getCompanyId(), bibliography.getGroupId(),
                bibliography.getUserId(), Bibliography.class.getName(), bibliography.getBibliographyId(),
                modelPermissions);
    }

    @Override
    public void addBibliographyResources(long bibliographyId, boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException {

        Bibliography bibliography = bibliographyPersistence.findByPrimaryKey(bibliographyId);

        addBibliographyResources(bibliography, addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addBibliographyResources(long bibliographyId, ModelPermissions modelPermissions)
            throws PortalException {

        Bibliography bibliography = bibliographyPersistence.findByPrimaryKey(bibliographyId);

        addBibliographyResources(bibliography, modelPermissions);
    }

    @Indexable(type = IndexableType.DELETE)
    @Override
    @SystemEvent(type = SystemEventConstants.TYPE_DELETE)
    public Bibliography deleteBibliography(Bibliography bibliography) throws PortalException {

        // Bibliography

        bibliographyPersistence.remove(bibliography);

        // Resources

        resourceLocalService.deleteResource(bibliography.getCompanyId(), Bibliography.class.getName(),
                ResourceConstants.SCOPE_INDIVIDUAL, bibliography.getBibliographyId());

        // Subscriptions

        subscriptionLocalService.deleteSubscriptions(bibliography.getCompanyId(), Bibliography.class.getName(),
                bibliography.getBibliographyId());

        // Asset

        assetEntryLocalService.deleteEntry(Bibliography.class.getName(), bibliography.getBibliographyId());

        // BibRefRelation

        bibRefRelationLocalService.deleteByBibliographyId(bibliography.getBibliographyId());

        // Comment

        // TODO
        // deleteDiscussion(bibliography);

        // Expando

        expandoRowLocalService.deleteRows(bibliography.getBibliographyId());

        // Ratings

        ratingsStatsLocalService.deleteStats(Bibliography.class.getName(), bibliography.getBibliographyId());

        // Trash

        trashEntryLocalService.deleteEntry(Bibliography.class.getName(), bibliography.getBibliographyId());

        // Workflow

        // TODO: do we need workflow support?
        // workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
        // bibliography.getCompanyId(), bibliography.getGroupId(),
        // Bibliography.class.getName(), bibliography.getBibliographyId());

        return bibliography;
    }

    @Override
    public Bibliography deleteBibliography(long bibliographyId) throws PortalException {
        Bibliography bibliography = bibliographyPersistence.findByPrimaryKey(bibliographyId);

        return bibliographyLocalService.deleteBibliography(bibliography);
    }

    public List<Bibliography> getGroupBibliographies(long groupId) throws PortalException, SystemException {
        return bibliographyPersistence.findByGroupId(groupId);
    }

    @Override
    public Bibliography getBibliography(long bibliographyId) throws PortalException {
        return bibliographyPersistence.findByPrimaryKey(bibliographyId);
    }

    @Override
    public Bibliography getBibliography(long groupId, String urlTitle) throws PortalException {
        return bibliographyPersistence.fetchByG_UT(groupId, urlTitle);
    }

    @Indexable(type = IndexableType.REINDEX)
    public Bibliography reIndexBibligraphy(long bibliographyId) throws PortalException {

        return getBibliography(bibliographyId);

    }

    @Override
    public void subscribe(long userId, long groupId) throws PortalException {
        subscriptionLocalService.addSubscription(userId, groupId, Bibliography.class.getName(), groupId);
    }

    @Override
    public void unsubscribe(long userId, long groupId) throws PortalException {
        subscriptionLocalService.deleteSubscription(userId, Bibliography.class.getName(), groupId);
    }

    public void updateAsset(long userId, Bibliography bibliography, long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds, Double priority) throws PortalException {

        boolean visible = false;

        Date publishDate = null;

        if (bibliography.isApproved()) {
            visible = true;
            publishDate = bibliography.getCreateDate();
        }

        String summary = HtmlUtil.extractText(StringUtil.shorten(bibliography.getTitle(), 500));

        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId, bibliography.getGroupId(),
                bibliography.getCreateDate(), bibliography.getModifiedDate(), Bibliography.class.getName(),
                bibliography.getBibliographyId(), bibliography.getUuid(), 0, assetCategoryIds, assetTagNames, true,
                visible, null, null, publishDate, null, ContentTypes.TEXT_HTML, bibliography.getTitle(),
                bibliography.getTitle(), summary, null, null, 0, 0, priority);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(), assetLinkEntryIds,
                AssetLinkConstants.TYPE_RELATED);
    }

    @Indexable(type = IndexableType.REINDEX)
    public Bibliography updateBibliography(long bibliographyId, long userId, String title, String description,
            String urlTitle, String comments, String preamble, String strings, ServiceContext serviceContext)
            throws PortalException {

        // Bibliography

        User user = userPersistence.findByPrimaryKey(userId);

        Bibliography bibliography = bibliographyPersistence.findByPrimaryKey(bibliographyId);
        
        long groupId = serviceContext.getScopeGroupId();

        if (!bibliography.getUrlTitle().equals(urlTitle)) {
            
            // modified urlTitle
            
            validate(groupId, urlTitle);            
        }
        

        bibliography.setUuid(serviceContext.getUuid());
        bibliography.setGroupId(groupId);
        bibliography.setCompanyId(user.getCompanyId());
        bibliography.setUserId(user.getUserId());
        bibliography.setUserName(user.getFullName());
        bibliography.setExpandoBridgeAttributes(serviceContext);

        bibliography.setTitle(title);
        bibliography.setDescription(description);
        bibliography.setUrlTitle(urlTitle);
        bibliography.setComments(comments);
        bibliography.setPreamble(preamble);
        bibliography.setStrings(strings);

        bibliographyPersistence.update(bibliography);

        // Resources

        resourceLocalService.addModelResources(bibliography, serviceContext);

        // Asset

        updateAsset(userId, bibliography, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

        extraDataJSONObject.put("title", bibliography.getTitle());

        socialActivityLocalService.addActivity(userId, groupId, Bibliography.class.getName(), bibliographyId,
                BibliographyActivityKeys.UPDATE_BIBLIOGRAPHY, extraDataJSONObject.toString(), 0);

        return bibliography;

    }

    @Override
    public void updateBibliographyResources(Bibliography bibliography, ModelPermissions modelPermissions)
            throws PortalException {

        resourceLocalService.updateResources(bibliography.getCompanyId(), bibliography.getGroupId(),
                Bibliography.class.getName(), bibliography.getBibliographyId(), modelPermissions);
    }

    @Override
    public void updateBibliographyResources(Bibliography bibliography, String[] groupPermissions,
            String[] guestPermissions) throws PortalException {

        resourceLocalService.updateResources(bibliography.getCompanyId(), bibliography.getGroupId(),
                Bibliography.class.getName(), bibliography.getBibliographyId(), groupPermissions, guestPermissions);
    }

    protected void validate(long groupId, String urlTitle) throws PortalException {
        if (Validator.isNull(urlTitle)) {
            throw new BibliographyUrlTitleException();
        } else {
            try {
                Bibliography bibliography = getBibliography(groupId, urlTitle);
                if (bibliography != null) {
                    throw new DuplicateUrlTitleException();
                }
            } catch (NoSuchBibliographyException e) {
                // ignore
            }
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyLocalServiceImpl.class);

}