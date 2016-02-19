package ch.inofix.portlet.timetracker.service.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.Duration;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.User;

import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import ch.inofix.portlet.timetracker.TaskRecordEndDateException;
import ch.inofix.portlet.timetracker.TaskRecordStartDateException;
import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.service.base.TaskRecordLocalServiceBaseImpl;
import ch.inofix.portlet.timetracker.service.persistence.TaskRecordFinderUtil;

/**
 * The implementation of the task record local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.portlet.timetracker.service.TaskRecordLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @created 2013-10-06 21:24
 * @modified 2013-11-09 23:01
 * @version 1.2
 *
 * @see ch.inofix.portlet.timetracker.service.base.TaskRecordLocalServiceBaseImpl
 * @see ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil
 */
public class TaskRecordLocalServiceImpl extends TaskRecordLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this interface directly. Always use {@link
     * ch.inofix.portlet.timetracker.service.TaskRecordLocalServiceUtil} to
     * access the task record local service.
     */
    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil
            .getLog(TaskRecordLocalServiceImpl.class.getName());

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
    public TaskRecord addTaskRecord(long userId, String workPackage,
            String description, String ticketURL, int endDateDay, int endDateMonth,
            int endDateYear, int endDateHour, int endDateMinute,
            int startDateDay, int startDateMonth, int startDateYear,
            int startDateHour, int startDateMinute, long duration,
            ServiceContext serviceContext) throws PortalException,
            SystemException {

        _log.info("Executing addTaskRecord().");

        try {
            User user = userPersistence.findByPrimaryKey(userId);
            long groupId = serviceContext.getScopeGroupId();

            Date endDate = PortalUtil.getDate(endDateMonth, endDateDay,
                    endDateYear, endDateHour, endDateMinute,
                    TaskRecordEndDateException.class);

            Date startDate = PortalUtil.getDate(startDateMonth, startDateDay,
                    startDateYear, startDateHour, startDateMinute,
                    TaskRecordStartDateException.class);

            Date now = new Date();

            long taskRecordId = counterLocalService.increment();

            TaskRecord taskRecord = taskRecordPersistence.create(taskRecordId);

            // Pass the values to the new record.
            taskRecord.setUuid(serviceContext.getUuid());
            taskRecord.setGroupId(groupId);
            taskRecord.setCompanyId(user.getCompanyId());
            taskRecord.setUserId(userId);
            taskRecord.setUserName(user.getFullName());
            taskRecord.setCreateDate(serviceContext.getCreateDate(now));
            taskRecord.setModifiedDate(serviceContext.getCreateDate(now));

            taskRecord.setWorkPackage(workPackage);
            taskRecord.setDescription(description);
            taskRecord.setTicketURL(ticketURL);
            taskRecord.setStartDate(startDate);
            taskRecord.setEndDate(endDate);
            taskRecord.setDuration(duration);

            return doUpdate(taskRecord);

        } catch (PortalException pe) {

            _log.error(pe);
            throw new PortalException(pe);

        } catch (SystemException se) {

            _log.error(se);
            throw new SystemException(se);
        }
    }

    /**
     * @since 1.0
     */
    public void addTaskRecordResources(TaskRecord taskRecord,
            boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException, SystemException {

        resourceLocalService.addResources(taskRecord.getCompanyId(),
                taskRecord.getGroupId(), taskRecord.getUserId(),
                TaskRecord.class.getName(), taskRecord.getTaskRecordId(),
                false, addGroupPermissions, addGuestPermissions);
    }

    /**
     * @since 1.0
     */
    public void addTaskRecordResources(TaskRecord taskRecord,
            String[] groupPermissions, String[] guestPermissions)
            throws PortalException, SystemException {

        resourceLocalService.addModelResources(taskRecord.getCompanyId(),
                taskRecord.getGroupId(), taskRecord.getUserId(),
                TaskRecord.class.getName(), taskRecord.getTaskRecordId(),
                groupPermissions, guestPermissions);
    }

    /**
     * @since 1.0
     */
    public void addTaskRecordResources(long taskRecordId,
            boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException, SystemException {

        TaskRecord taskRecord = taskRecordPersistence
                .findByPrimaryKey(taskRecordId);

        addTaskRecordResources(taskRecord, addGroupPermissions,
                addGuestPermissions);
    }

    /**
     * @since 1.0
     */
    public void addTaskRecordResources(long taskRecordId,
            String[] groupPermissions, String[] guestPermissions)
            throws PortalException, SystemException {

        TaskRecord taskRecord = taskRecordPersistence
                .findByPrimaryKey(taskRecordId);

        addTaskRecordResources(taskRecord, groupPermissions, guestPermissions);
    }

    /**
     *
     * @since 1.0
     */
    public TaskRecord deleteTaskRecord(TaskRecord taskRecord)
            throws PortalException, SystemException {

        _log.info("Executing deleteTaskRecord(taskRecord).");

        taskRecordPersistence.remove(taskRecord);

        return taskRecord;
    }

    /**
     * @since 1.0
     */
    public TaskRecord deleteTaskRecord(long taskRecordId)
            throws PortalException, SystemException {

        _log.info("Executing deleteTaskRecord(taskRecordId).");

        TaskRecord taskRecord = taskRecordPersistence
                .findByPrimaryKey(taskRecordId);

        return deleteTaskRecord(taskRecord);
    }

    /**
     * Return the list of matching task records.
     *
     * @param companyId
     * @param groupId
     * @param userId
     * @param keywords
     * @param status
     * @param start
     * @param end
     * @param obc
     * @return the list of matching task records.
     * @since 1.0
     * @throws SystemException
     */
    public List<TaskRecord> search(long companyId, long groupId, long userId,
            String keywords, int status, int start, int end,
            OrderByComparator obc) throws SystemException {

        _log.info("Executing search().");

        return TaskRecordFinderUtil.findByKeywords(companyId, groupId, userId,
                keywords, status, start, end, obc);

    }

    /**
     * Return the list of matching task records.
     *
     * @param companyId
     * @param groupId
     * @param userId
     * @param workPackage
     * @param description
     * @param startDate
     * @param endDate
     * @param status
     * @param start
     * @param end
     * @param andOperator
     * @param obc
     * @return the list of matching task records.
     * @since 1.0
     * @throws SystemException
     */
    public List<TaskRecord> search(long companyId, long groupId, long userId,
            String workPackage, String description, Date startDate,
            Date endDate, int status, int start, int end, boolean andOperator,
            OrderByComparator obc) throws SystemException {

        _log.info("Executing search(advanced).");

        return TaskRecordFinderUtil.findBy_C_G_U_W_S_E_D_S(companyId, groupId,
                userId, workPackage, startDate, endDate, description, status,
                andOperator, start, end, obc);

    }

    /**
     * Return the number of task records matching the given keywords.
     *
     * @param companyId
     * @param groupId
     * @param userId
     * @param keywords
     * @param status
     * @return the number of task records matching the given keywords.
     * @since 1.0
     * @throws SystemException
     */
    public int searchCount(long companyId, long groupId, long userId,
            String keywords, int status) throws SystemException {

        _log.info("Executing searchCount().");

        return TaskRecordFinderUtil.countByKeywords(companyId, groupId, userId,
                keywords, null, null, status);

    }

    /**
     * Return the number of matching task records.
     *
     * @param companyId
     * @param groupId
     * @param userId
     * @param workPackage
     * @param description
     * @param startDate
     * @param endDate
     * @param status
     * @param andOperator
     * @return the number of matching task records.
     * @since 1.0
     * @throws SystemException
     */
    public int searchCount(long companyId, long groupId, long userId,
            String workPackage, String description, Date startDate,
            Date endDate, int status, boolean andOperator)
            throws SystemException {

        return TaskRecordFinderUtil.countBy_C_G_U_W_S_E_D_S(companyId, groupId,
                userId, workPackage, startDate, endDate, description, status,
                andOperator);

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
    public TaskRecord updateTaskRecord(long userId, long taskRecordId,
            String workPackage, String description, String ticketURL,
            int endDateDay, int endDateMonth, int endDateYear, int endDateHour,
            int endDateMinute, int startDateDay, int startDateMonth,
            int startDateYear, int startDateHour, int startDateMinute,
            long duration, ServiceContext serviceContext)
            throws PortalException, SystemException {

        _log.info("Executing updateTaskRecord().");

        try {

            Date endDate = PortalUtil.getDate(endDateMonth, endDateDay,
                    endDateYear, endDateHour, endDateMinute,
                    TaskRecordEndDateException.class);

            Date startDate = PortalUtil.getDate(startDateMonth, startDateDay,
                    startDateYear, startDateHour, startDateMinute,
                    TaskRecordStartDateException.class);

            Duration diff = new Duration(startDate.getTime(), endDate.getTime());

            if (duration == 0) {
                duration = diff.getMillis();
            }

            // TODO: Implement input validation.
            // either startDate / endDate or duration in milliseconds
            // workPackage: required
            // task: required




            TaskRecord taskRecord = taskRecordPersistence
                    .fetchByPrimaryKey(taskRecordId);

            // Update the record with the new values.
            taskRecord.setModifiedDate(serviceContext.getModifiedDate(null));

            taskRecord.setWorkPackage(workPackage);
            taskRecord.setDescription(description);
            taskRecord.setTicketURL(ticketURL);
            taskRecord.setStartDate(startDate);
            taskRecord.setEndDate(endDate);
            taskRecord.setDuration(duration);

            return doUpdate(taskRecord);

        } catch (SystemException se) {

            _log.error(se);
            throw new SystemException(se);

        }
    }

    /**
     * Perform the actual update, perform input validation
     * and return the updated taskRecord.
     *
     * @param taskRecord
     * @return the updated taskRecord.
     * @since 1.2
     * @throws PortalException
     * @throws SystemException
     */
    protected TaskRecord doUpdate(TaskRecord taskRecord)
            throws PortalException, SystemException {

        _log.debug("Executing doUpdate().");

        // TODO: Implement input validation.
        // either startDate / endDate or duration in milliseconds
        // is required
        // workPackage: required
        // task: required

        Date startDate = taskRecord.getStartDate();
        Date endDate = taskRecord.getEndDate();

        if (endDate.before(startDate)) {

            // TODO: Leave a message or better: move this
            // code to the MVC-layer and inform the user
            // of the input modification.
            _log.debug("endDate is before startDate.");
            endDate = startDate;
            taskRecord.setEndDate(endDate);
        }

        long duration = taskRecord.getDuration();

        Duration diff = new Duration(startDate.getTime(), endDate.getTime());

        if (duration == 0) {
            duration = diff.getMillis();
        }

        taskRecord.setDuration(duration);

        return taskRecordPersistence.update(taskRecord);

    }

}
