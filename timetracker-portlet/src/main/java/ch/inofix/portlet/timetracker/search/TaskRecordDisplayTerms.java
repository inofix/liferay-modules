
package ch.inofix.portlet.timetracker.search;

import javax.portlet.PortletRequest;

import ch.inofix.portlet.timetracker.util.CommonFields;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Christian Berndt
 * @created 2013-10-06 17:34
 * @modified 2016-03-19 22:15
 * @version 1.0.1
 */
public class TaskRecordDisplayTerms extends DisplayTerms {

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(TaskRecordDisplayTerms.class.getName());

    public static final String CREATE_DATE = "createDate";
    public static final String DESCRIPTION = "description";
    public static final String END_DATE = "endDate";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String START_DATE = "startDate";
    public static final String TASK_RECORD_ID = "taskRecordId";
    public static final String WORK_PACKAGE = "workPackage";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";

    public TaskRecordDisplayTerms(PortletRequest portletRequest) {

        super(portletRequest);

        // Retrieve the parameter values
        // from the request.
        workPackage = ParamUtil.getString(portletRequest, WORK_PACKAGE);
        endDate = ParamUtil.getString(portletRequest, END_DATE);
        groupId = ParamUtil.getLong(portletRequest, CommonFields.GROUP_ID);
        startDate = ParamUtil.getString(portletRequest, START_DATE);
        status = ParamUtil.getInteger(portletRequest, CommonFields.STATUS);
        description = ParamUtil.getString(portletRequest, DESCRIPTION);
    }

    // Getters and setters for them form fiels.

    // Search-form fields.
    protected String description = null;

    public String getDescription() {

        return description;
    }

    public String getEndDate() {

        return endDate;
    }

    public long getGroupId() {

        return groupId;
    }

    public String getStartDate() {

        return startDate;
    }

    public int getStatus() {

        return status;
    }

    public long getUserId() {

        return userId;
    }

    public String getWorkPackage() {

        return workPackage;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public void setEndDate(String endDate) {

        this.endDate = endDate;
    }

    public void setGroupId(long groupId) {

        this.groupId = groupId;
    }

    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public void setUserId(long userId) {

        this.userId = userId;
    }

    public void setWorkPackage(String workPackage) {

        this.workPackage = workPackage;
    }

    protected String endDate = null;
    protected long groupId = 0;
    protected String startDate = null;
    protected int status = 0;
    protected long userId = 0;
    protected String workPackage = null;

}
