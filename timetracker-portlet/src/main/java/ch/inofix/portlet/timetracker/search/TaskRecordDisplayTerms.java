package ch.inofix.portlet.timetracker.search;

import javax.portlet.PortletRequest;

import ch.inofix.portlet.timetracker.util.CommonFields;
import ch.inofix.portlet.timetracker.util.TaskRecordFields;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2013-10-06 17:34
 * @modified 2013-10-06 17:34
 * @version 1.0
 * 
 */
public class TaskRecordDisplayTerms extends DisplayTerms {

	// Enable logging for this class
	private static Log _log = LogFactoryUtil
			.getLog(TaskRecordDisplayTerms.class.getName());

	// Search-form fields.
	protected String workPackage = null;
	protected String endDate = null;
	protected long groupId = 0;
	protected String startDate = null;
	protected int status = 0;
	protected String description = null;
	protected long userId = 0;

	public TaskRecordDisplayTerms(PortletRequest portletRequest) {

		super(portletRequest);

		_log.info("Construction TaskRecordDisplayTerms.");

		// Retrieve the parameter values
		// from the request.
		workPackage = ParamUtil.getString(portletRequest, TaskRecordFields.WORK_PACKAGE);
		endDate = ParamUtil.getString(portletRequest, TaskRecordFields.END_DATE); 
		groupId = ParamUtil.getLong(portletRequest, CommonFields.GROUP_ID); 
		startDate = ParamUtil.getString(portletRequest, TaskRecordFields.START_DATE); 
		status = ParamUtil.getInteger(portletRequest, CommonFields.STATUS); 
		description = ParamUtil.getString(portletRequest, TaskRecordFields.DESCRIPTION); 
	}

	// Getters and setters for them form fiels.
	
	public String getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(String workPackage) {
		this.workPackage = workPackage;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String task) {
		this.description = task;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
