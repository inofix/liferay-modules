
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
 * @modified 2016-05-08 13:14
 * @version 1.0.5
 */
public class TaskRecordDisplayTerms extends DisplayTerms {

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(TaskRecordDisplayTerms.class.getName());

    public static final String CREATE_DATE = "createDate";
    public static final String DESCRIPTION = "description";
    public static final String DURATION = "duration";
    public static final String IGNORE_END_DATE = "ignoreEndDate";
    public static final String IGNORE_START_DATE = "ignoreStartDate";
    public static final String END_DATE = "endDate";
    public static final String END_DATE_DAY = "endDateDay";
    public static final String END_DATE_MONTH = "endDateMonth";
    public static final String END_DATE_YEAR = "endDateYear";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String START_DATE = "startDate";
    public static final String START_DATE_DAY = "startDateDay";
    public static final String START_DATE_MONTH = "startDateMonth";
    public static final String START_DATE_YEAR = "startDateYear";
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
        endDateDay = ParamUtil.getString(portletRequest, END_DATE_DAY);
        endDateMonth = ParamUtil.getString(portletRequest, END_DATE_MONTH);
        endDateYear = ParamUtil.getString(portletRequest, END_DATE_YEAR);
        groupId = ParamUtil.getLong(portletRequest, CommonFields.GROUP_ID);
        ignoreEndDate = ParamUtil.getBoolean(portletRequest, IGNORE_END_DATE);
        ignoreStartDate = ParamUtil.getBoolean(portletRequest, IGNORE_START_DATE);
        modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
        startDate = ParamUtil.getString(portletRequest, START_DATE);
        startDateDay = ParamUtil.getString(portletRequest, START_DATE_DAY);
        startDateMonth = ParamUtil.getString(portletRequest, START_DATE_MONTH);
        startDateYear = ParamUtil.getString(portletRequest, START_DATE_YEAR);
        status = ParamUtil.getInteger(portletRequest, STATUS);
        userId = ParamUtil.getLong(portletRequest, USER_ID);
        userName = ParamUtil.getString(portletRequest, USER_NAME);
        workPackage = ParamUtil.getString(portletRequest, WORK_PACKAGE);
    }

    public String getCreateDate() {

        return createDate;
    }

    public void setCreateDate(String createDate) {

        this.createDate = createDate;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDuration() {

        return duration;
    }

    public void setDuration(String duration) {

        this.duration = duration;
    }

    public String getEndDate() {

        return endDate;
    }

    public void setEndDate(String endDate) {

        this.endDate = endDate;
    }

    public String getEndDateDay() {

        return endDateDay;
    }

    public void setEndDateDay(String endDateDay) {

        this.endDateDay = endDateDay;
    }

    public String getEndDateMonth() {

        return endDateMonth;
    }

    public void setEndDateMonth(String endDateMonth) {

        this.endDateMonth = endDateMonth;
    }

    public String getEndDateYear() {

        return endDateYear;
    }

    public void setEndDateYear(String endDateYear) {

        this.endDateYear = endDateYear;
    }

    public long getGroupId() {

        return groupId;
    }

    public void setGroupId(long groupId) {

        this.groupId = groupId;
    }

    public String getModifiedDate() {

        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {

        this.modifiedDate = modifiedDate;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }

    public String getStartDateDay() {

        return startDateDay;
    }

    public void setStartDateDay(String startDateDay) {

        this.startDateDay = startDateDay;
    }

    public String getStartDateMonth() {

        return startDateMonth;
    }

    public void setStartDateMonth(String startDateMonth) {

        this.startDateMonth = startDateMonth;
    }

    public String getStartDateYear() {

        return startDateYear;
    }

    public void setStartDateYear(String startDateYear) {

        this.startDateYear = startDateYear;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public long getTaskRecordId() {

        return taskRecordId;
    }

    public void setTaskRecordId(long taskRecordId) {

        this.taskRecordId = taskRecordId;
    }

    public long getUserId() {

        return userId;
    }

    public void setUserId(long userId) {

        this.userId = userId;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getWorkPackage() {

        return workPackage;
    }

    public void setWorkPackage(String workPackage) {

        this.workPackage = workPackage;
    }

    protected String createDate = null;
    protected String description = null;
    protected String duration = null;
    protected String endDate = "";
    protected String endDateDay = "";
    protected String endDateMonth = "";
    protected String endDateYear = "";
    protected long groupId = 0;
    protected boolean ignoreEndDate = true;
    protected boolean ignoreStartDate = true;
    protected String modifiedDate = null;
    protected String startDate = "";
    protected String startDateDay = "";
    protected String startDateMonth = "";
    protected String startDateYear = "";
    protected int status = WorkflowConstants.STATUS_ANY;
    protected long taskRecordId = 0;
    protected long userId = 0;
    protected String userName = null;
    protected String workPackage = null;

}
