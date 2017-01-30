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

package ch.inofix.timetracker.service.impl;

import java.util.Date;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.ServiceContext;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.timetracker.model.TaskRecord;
import ch.inofix.timetracker.security.ActionKeys;
import ch.inofix.timetracker.service.TaskRecordLocalServiceUtil;
import ch.inofix.timetracker.service.base.TaskRecordServiceBaseImpl;
import ch.inofix.timetracker.service.permission.TaskRecordPermission;
import ch.inofix.timetracker.service.permission.TimetrackerPortletPermission;

/**
 * The implementation of the task record remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.timetracker.service.TaskRecordService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @created 2015-05-07 23:50
 * @modified 2016-11-13 17:43
 * @version 1.0.3
 * @see TaskRecordServiceBaseImpl
 * @see ch.inofix.timetracker.service.TaskRecordServiceUtil
 */
@ProviderType
public class TaskRecordServiceImpl extends TaskRecordServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.timetracker.service.TaskRecordServiceUtil} to access the task
     * record remote service.
     */
    public TaskRecord addTaskRecord(long groupId, String workPackage, String description, String ticketURL,
            Date endDate, Date startDate, int status, long duration, ServiceContext serviceContext)
            throws PortalException, SystemException {

        TimetrackerPortletPermission.check(getPermissionChecker(), groupId, ActionKeys.ADD_TASK_RECORD);

        return taskRecordLocalService.addTaskRecord(getUserId(), groupId, workPackage, description, ticketURL, endDate,
                startDate, status, duration, serviceContext);

    }

    public TaskRecord createTaskRecord() throws PortalException, SystemException {

        // Create an empty taskRecord - no permission check required
        return TaskRecordLocalServiceUtil.createTaskRecord(0);
    }

    public TaskRecord deleteTaskRecord(long taskRecordId) throws PortalException, SystemException {

        TaskRecordPermission.check(getPermissionChecker(), taskRecordId, ActionKeys.DELETE);

        TaskRecord taskRecord = TaskRecordLocalServiceUtil.deleteTaskRecord(taskRecordId);

        return taskRecord;

    }

    public TaskRecord getTaskRecord(long taskRecordId) throws PortalException, SystemException {

        TaskRecordPermission.check(getPermissionChecker(), taskRecordId, ActionKeys.VIEW);

        return TaskRecordLocalServiceUtil.getTaskRecord(taskRecordId);

    }

    public TaskRecord updateTaskRecord(long groupId, long taskRecordId, String workPackage, String description,
            String ticketURL, Date endDate, Date startDate, int status, long duration, ServiceContext serviceContext)
            throws PortalException, SystemException {

        TaskRecordPermission.check(getPermissionChecker(), taskRecordId, ActionKeys.UPDATE);

        return taskRecordLocalService.updateTaskRecord(getUserId(), groupId, taskRecordId, workPackage, description,
                ticketURL, endDate, startDate, status, duration, serviceContext);

    }
}