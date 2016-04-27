package ch.inofix.portlet.timetracker.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author Christian Berndt
 * @created 2013-10-06 17:34
 * @modified 2016-04-27 20:11
 * @version 1.0.1
 */
public class TaskRecordSearchTerms extends TaskRecordDisplayTerms {

    // Enable logging for this class
    private static Log _log =
        LogFactoryUtil.getLog(TaskRecordDisplayTerms.class.getName());

    public TaskRecordSearchTerms(PortletRequest portletRequest) {

        super(portletRequest);

        description = DAOParamUtil.getString(portletRequest, DESCRIPTION);
        endDate = DAOParamUtil.getString(portletRequest, END_DATE);
        startDate = DAOParamUtil.getString(portletRequest, START_DATE);
        status = ParamUtil.getInteger(
            portletRequest, STATUS, WorkflowConstants.STATUS_APPROVED);
        userId = DAOParamUtil.getLong(portletRequest, USER_ID);
        workPackage = DAOParamUtil.getString(portletRequest, WORK_PACKAGE);

    }
}
