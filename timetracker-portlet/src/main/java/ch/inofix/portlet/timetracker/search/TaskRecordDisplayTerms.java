package ch.inofix.portlet.timetracker.search;

import javax.portlet.PortletRequest;

import ch.inofix.portlet.timetracker.util.CommonFields;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author Christian Berndt
 * @created 2013-10-06 17:34
 * @modified 2016-04-27 19:57
 * @version 1.0.3
 */
public class TaskRecordDisplayTerms extends DisplayTerms {

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(TaskRecordDisplayTerms.class.getName());

    public static final String CREATE_DATE = "createDate";
    public static final String DESCRIPTION = "description";
    public static final String DURATION = "duration";
    public static final String END_DATE = "endDate";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String START_DATE = "startDate";
    public static final String STATUS = "status";
    public static final String TASK_RECORD_ID = "taskRecordId";
    public static final String WORK_PACKAGE = "workPackage";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";

    public TaskRecordDisplayTerms(PortletRequest portletRequest) {

        super(portletRequest);

        String statusString = ParamUtil.getString(portletRequest, STATUS);

        if (Validator.isNotNull(statusString)) {
            status = GetterUtil.getInteger(statusString);
        }

        createDate = ParamUtil.getString(portletRequest, CREATE_DATE);
        description = ParamUtil.getString(portletRequest, DESCRIPTION);
        duration = ParamUtil.getString(portletRequest, DURATION);
        endDate = ParamUtil.getString(portletRequest, END_DATE);
        groupId = ParamUtil.getLong(portletRequest, CommonFields.GROUP_ID);
        modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
        startDate = ParamUtil.getString(portletRequest, START_DATE);
        status = ParamUtil.getInteger(portletRequest, STATUS);
        userId = ParamUtil.getLong(portletRequest, USER_ID);
        userName = ParamUtil.getString(portletRequest, USER_NAME);
        workPackage = ParamUtil.getString(portletRequest, WORK_PACKAGE);
    }

    // Getters and setters for them form fields.

    public String getCreateDate() {

        return createDate;
    }

    public String getDescription() {

        return description;
    }

    public String getDuration() {

        return duration;
    }

    public String getEndDate() {

        return endDate;
    }

    public long getGroupId() {

        return groupId;
    }

    public String getModifiedDate() {

        return modifiedDate;
    }

    public String getStartDate() {

        return startDate;
    }

    public int getStatus() {

        return status;
    }

    public long getTaskRecordId() {

        return taskRecordId;
    }

    public long getUserId() {

        return userId;
    }

    public String getUserName() {

        return userName;
    }

    public String getWorkPackage() {

        return workPackage;
    }

    public void setCreateDate(String createDate) {

        this.createDate = createDate;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public void setDuration(String duration) {

        this.duration = duration;
    }

    public void setEndDate(String endDate) {

        this.endDate = endDate;
    }

    public void setGroupId(long groupId) {

        this.groupId = groupId;
    }

    public void setModifiedDate(String modifiedDate) {

        this.modifiedDate = modifiedDate;
    }

    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public void setTaskRecordId(long taskRecordId) {

        this.taskRecordId = taskRecordId;
    }

    public void setUserId(long userId) {

        this.userId = userId;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public void setWorkPackage(String workPackage) {

        this.workPackage = workPackage;
    }

    public boolean isActive() {

        if (status == WorkflowConstants.STATUS_APPROVED) {
            return true;
        }
        else {
            return false;
        }
    }

    protected String createDate = null;
    protected String description = null;
    protected String duration = null;
    protected String endDate = null;
    protected long groupId = 0;
    protected String modifiedDate = null;
    protected String startDate = null;
    protected int status = 0;
    protected long taskRecordId = 0;
    protected long userId = 0;
    protected String userName = null;
    protected String workPackage = null;

}
