
package ch.inofix.portlet.timetracker.service.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.Duration;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLinkConstants;

import ch.inofix.portlet.timetracker.TaskRecordEndDateException;
import ch.inofix.portlet.timetracker.TaskRecordStartDateException;
import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.service.base.TaskRecordLocalServiceBaseImpl;

/**
 * The implementation of the task record local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.timetracker.service.TaskRecordLocalService}
 * interface. <p> This is a local service. Methods of this service will not have
 * security checks based on the propagated JAAS credentials because this service
 * can only be accessed from within the same VM. </p>
 *
 * @author Christian Berndt
 * @created 2013-10-06 21:24
 * @modified 2016-03-22 13:57
 * @version 1.0.5
 * @see ch.inofix.portlet.timetracker.service.base.TaskRecordLocalServiceBaseImpl
 * @see ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil
 */
public class TaskRecordLocalServiceImpl extends TaskRecordLocalServiceBaseImpl {

    /*
     * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
     * {@link ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil}
     * to access the task record local service.
     */
    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(TaskRecordLocalServiceImpl.class.getName());

    /**
     * Add a task record to the database.
     *
     * @param userId
     * @param workPackage
     * @param description
     * @param ticketURL
     * @param endDateDay
     * @param endDateMonth
     * @param endDateYear
     * @param endDateHour
     * @param endDateMinute
     * @param startDateDay
     * @param startDateMonth
     * @param startDateYear
     * @param startDateHour
     * @param startDateMinute
     * @param duration
     * @param serviceContext
     * @return the added TaskRecord.
     * @since 1.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord addTaskRecord(
        long userId, long groupId, String workPackage, String description,
        String ticketURL, int endDateDay, int endDateMonth, int endDateYear,
        int endDateHour, int endDateMinute, int startDateDay,
        int startDateMonth, int startDateYear, int startDateHour,
        int startDateMinute, long duration, ServiceContext serviceContext)
        throws PortalException, SystemException {

        Date endDate =
            PortalUtil.getDate(
                endDateMonth, endDateDay, endDateYear, endDateHour,
                endDateMinute, TaskRecordEndDateException.class);

        Date startDate =
            PortalUtil.getDate(
                startDateMonth, startDateDay, startDateYear, startDateHour,
                startDateMinute, TaskRecordStartDateException.class);

        // TODO
        int status = WorkflowConstants.STATUS_APPROVED;

        TaskRecord taskRecord =
            saveTaskRecord(
                userId, groupId, 0, description, duration, endDate, startDate,
                status, workPackage, serviceContext);

        // Asset

        resourceLocalService.addResources(
            taskRecord.getCompanyId(), groupId, userId,
            TaskRecord.class.getName(), taskRecord.getTaskRecordId(), false,
            true, true);

        updateAsset(
            userId, taskRecord, serviceContext.getAssetCategoryIds(),
            serviceContext.getAssetTagNames(),
            serviceContext.getAssetLinkEntryIds());

        return taskRecord;

    }

    /**
     * @since 1.0
     */
    public TaskRecord deleteTaskRecord(TaskRecord taskRecord)
        throws PortalException, SystemException {

        taskRecordPersistence.remove(taskRecord);

        return taskRecord;
    }

    /**
     * @since 1.0.0
     */
    public TaskRecord deleteTaskRecord(long taskRecordId)
        throws PortalException, SystemException {

        TaskRecord taskRecord =
            taskRecordPersistence.findByPrimaryKey(taskRecordId);

        return deleteTaskRecord(taskRecord);
    }

    /**
     * 
     * @param groupId
     * @return
     * @since 1.0.5
     * @throws PortalException
     * @throws SystemException
     */
    public List<TaskRecord> getTaskRecords(long groupId)
        throws PortalException, SystemException {

        return taskRecordPersistence.findByGroupId(groupId);
    }

    /**
     * @param userId
     * @param groupId
     * @param taskRecordId
     * @param uid
     * @param serviceContext
     * @return
     * @throws PortalException
     * @throws SystemException
     */
    private TaskRecord saveTaskRecord(
        long userId, long groupId, long taskRecordId, String description,
        long duration, Date endDate, Date startDate, int status,
        String workPackage, ServiceContext serviceContext)
        throws PortalException, SystemException {

        User user = userPersistence.findByPrimaryKey(userId);
        Date now = new Date();
        TaskRecord taskRecord = null;

        if (taskRecordId > 0) {
            taskRecord = taskRecordLocalService.getTaskRecord(taskRecordId);
        }
        else {
            taskRecordId = counterLocalService.increment();
            taskRecord = taskRecordPersistence.create(taskRecordId);
            taskRecord.setCompanyId(user.getCompanyId());
            taskRecord.setGroupId(groupId);
            taskRecord.setUserId(user.getUserId());
            taskRecord.setUserName(user.getFullName());
            taskRecord.setCreateDate(now);
        }

        taskRecord.setModifiedDate(now);

        taskRecord.setDescription(description);
        taskRecord.setDuration(duration);
        taskRecord.setEndDate(endDate);
        taskRecord.setStartDate(startDate);
        taskRecord.setStatus(status);
        taskRecord.setWorkPackage(workPackage);
        taskRecord.setExpandoBridgeAttributes(serviceContext);

        taskRecordPersistence.update(taskRecord);

        return taskRecord;

    }

    public void updateAsset(
        long userId, TaskRecord taskRecord, long[] assetCategoryIds,
        String[] assetTagNames, long[] assetLinkEntryIds)
        throws PortalException, SystemException {

        boolean visible = true;
        // boolean visible = false;

        // if (taskRecord.isApproved()) {
        // visible = true;
        // }

        // TODO: What's the classTypeId?
        long classTypeId = 0;
        Date startDate = null;
        Date endDate = null;
        Date expirationDate = null;
        String mimeType = "text/text";
        String title = taskRecord.getWorkPackage();
        String description = taskRecord.getDescription();
        String summary = taskRecord.getDescription();
        // TODO: What does url mean in this context?
        String url = null;
        // TODO: What does layoutUuid mean in this context?
        String layoutUuid = null;
        int height = 0;
        int width = 0;
        Integer priority = null;
        boolean sync = false;

        AssetEntry assetEntry =
            assetEntryLocalService.updateEntry(
                userId, taskRecord.getGroupId(), taskRecord.getCreateDate(),
                taskRecord.getModifiedDate(), TaskRecord.class.getName(),
                taskRecord.getTaskRecordId(), taskRecord.getUuid(),
                classTypeId, assetCategoryIds, assetTagNames, visible,
                startDate, endDate, expirationDate, mimeType, title,
                description, summary, url, layoutUuid, height, width, priority,
                sync);

        assetLinkLocalService.updateLinks(
            userId, assetEntry.getEntryId(), assetLinkEntryIds,
            AssetLinkConstants.TYPE_RELATED);

        Indexer indexer =
            IndexerRegistryUtil.nullSafeGetIndexer(TaskRecord.class);
        indexer.reindex(taskRecord);
    }

    /**
     * Return the updated task record.
     *
     * @param userId
     * @param taskRecordId
     * @param workPackage
     * @param description
     * @param ticketURL
     * @param endDateDay
     * @param endDateMonth
     * @param endDateYear
     * @param endDateHour
     * @param endDateMinute
     * @param startDateDay
     * @param startDateMonth
     * @param startDateYear
     * @param startDateHour
     * @param startDateMinute
     * @param duration
     * @param serviceContext
     * @return the updated task record.
     * @since 1.0
     * @throws PortalException
     * @throws SystemException
     */
    public TaskRecord updateTaskRecord(
        long userId, long groupId, long taskRecordId, String workPackage,
        String description, String ticketURL, int endDateDay, int endDateMonth,
        int endDateYear, int endDateHour, int endDateMinute, int startDateDay,
        int startDateMonth, int startDateYear, int startDateHour,
        int startDateMinute, long duration, ServiceContext serviceContext)
        throws PortalException, SystemException {

        Date endDate =
            PortalUtil.getDate(
                endDateMonth, endDateDay, endDateYear, endDateHour,
                endDateMinute, TaskRecordEndDateException.class);

        Date startDate =
            PortalUtil.getDate(
                startDateMonth, startDateDay, startDateYear, startDateHour,
                startDateMinute, TaskRecordStartDateException.class);

        Duration diff = new Duration(startDate.getTime(), endDate.getTime());

        if (duration == 0) {
            duration = diff.getMillis();
        }

        // TODO: Implement input validation.
        // either startDate / endDate or duration in milliseconds
        // workPackage: required
        // task: required

        // TODO
        int status = WorkflowConstants.STATUS_APPROVED;

        TaskRecord taskRecord =
            saveTaskRecord(
                userId, groupId, taskRecordId, description, duration, endDate,
                startDate, status, workPackage, serviceContext);

        // Asset

        resourceLocalService.updateResources(
            serviceContext.getCompanyId(), serviceContext.getScopeGroupId(),
            taskRecord.getWorkPackage(), taskRecordId,
            serviceContext.getGroupPermissions(),
            serviceContext.getGuestPermissions());

        updateAsset(
            userId, taskRecord, serviceContext.getAssetCategoryIds(),
            serviceContext.getAssetTagNames(),
            serviceContext.getAssetLinkEntryIds());

        return taskRecord;

    }
}
