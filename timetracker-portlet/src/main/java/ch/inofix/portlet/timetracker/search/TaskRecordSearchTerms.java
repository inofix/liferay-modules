package ch.inofix.portlet.timetracker.search;

import javax.portlet.PortletRequest;

import ch.inofix.portlet.timetracker.util.CommonFields;
import ch.inofix.portlet.timetracker.util.TaskRecordFields;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class TaskRecordSearchTerms extends TaskRecordDisplayTerms {

	// Enable logging for this class
	private static Log _log = LogFactoryUtil
			.getLog(TaskRecordDisplayTerms.class.getName());

	public TaskRecordSearchTerms(PortletRequest portletRequest) {

		super(portletRequest);

		// Translate the request parameters into
		// SQL compatible values (or vice versa?)
		workPackage = DAOParamUtil
				.getString(portletRequest, TaskRecordFields.WORK_PACKAGE);
		endDate = DAOParamUtil.getString(portletRequest,
				TaskRecordFields.END_DATE);
		groupId = DAOParamUtil.getLong(portletRequest, 
				CommonFields.GROUP_ID);
		startDate = DAOParamUtil.getString(portletRequest,
				TaskRecordFields.START_DATE);
		status = DAOParamUtil.getInteger(portletRequest, 
				CommonFields.STATUS); 
		description = DAOParamUtil.getString(portletRequest, 
				TaskRecordFields.DESCRIPTION); 
		userId = DAOParamUtil.getLong(portletRequest, 
				CommonFields.USER_ID); 

	}

}
