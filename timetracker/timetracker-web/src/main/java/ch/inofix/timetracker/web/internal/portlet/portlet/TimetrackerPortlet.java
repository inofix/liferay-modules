package ch.inofix.timetracker.web.internal.portlet.portlet;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.timetracker.exception.TaskRecordEndDateException;
import ch.inofix.timetracker.exception.TaskRecordStartDateException;
import ch.inofix.timetracker.model.TaskRecord;
import ch.inofix.timetracker.service.TaskRecordLocalService;
import ch.inofix.timetracker.service.TaskRecordService;

import java.io.IOException;
import java.util.Date;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * View Controller of Inofix' timetracker.
 *
 * @author Christian Berndt
 * @created 2013-10-07 10:47
 * @modified 2016-11-12 19:57
 * @version 1.5.0
 */
@Component(
    immediate = true, 
    property = { 
        "com.liferay.portlet.display-category=category.inofix",
        "com.liferay.portlet.instanceable=true", 
        "javax.portlet.display-name=Timetracker",
        "javax.portlet.init-param.template-path=/", 
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" 
    }, 
    service = Portlet.class
)
public class TimetrackerPortlet extends MVCPortlet {

    public void deleteTaskRecord(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        long taskRecordId = ParamUtil.getLong(actionRequest, "taskRecordId");

        _taskRecordService.deleteTaskRecord(taskRecordId);
    }

    public TaskRecord editTaskRecord(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        
        _log.info("editTaskRecord()");

        long taskRecordId = ParamUtil.getLong(actionRequest, "taskRecordId");
        
        _log.info("taskRecordId = " + taskRecordId);

        ServiceContext serviceContext = ServiceContextFactory.getInstance(TaskRecord.class.getName(), actionRequest);

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        String workPackage = ParamUtil.getString(actionRequest, "workPackage");
        String description = ParamUtil.getString(actionRequest, "description");
        String ticketURL = ParamUtil.getString(actionRequest, "ticketURL");
        int durationInMinutes = ParamUtil.getInteger(actionRequest, "duration");
        long duration = durationInMinutes * 60 * 1000;
        int status = ParamUtil.getInteger(actionRequest, "status");

        int startDateDay = ParamUtil.getInteger(actionRequest, "startDateDay");
        int startDateMonth = ParamUtil.getInteger(actionRequest, "startDateMonth");
        int startDateYear = ParamUtil.getInteger(actionRequest, "startDateYear");
        int startDateHour = ParamUtil.getInteger(actionRequest, "startDateHour");
        int startDateMinute = ParamUtil.getInteger(actionRequest, "startDateMinute");

        // TODO: clean this up!
        // Create the endDate with the date values of
        // the startDate, because we want the user to
        // have to select only one date.
        int endDateDay = ParamUtil.getInteger(actionRequest, "startDateDay");
        int endDateMonth = ParamUtil.getInteger(actionRequest, "startDateMonth");
        int endDateYear = ParamUtil.getInteger(actionRequest, "startDateYear");
        int endDateHour = ParamUtil.getInteger(actionRequest, "endDateHour");
        int endDateMinute = ParamUtil.getInteger(actionRequest, "endDateMinute");

        Date endDate = null;
        
        try {
            PortalUtil.getDate(endDateMonth, endDateDay, endDateYear, endDateHour, endDateMinute,
                    TaskRecordEndDateException.class);
        } catch (Exception e) {
            _log.error(e);
        }

        Date startDate = null; 
        
        try {
            PortalUtil.getDate(startDateMonth, startDateDay, startDateYear, startDateHour, startDateMinute,
                    TaskRecordStartDateException.class);
        } catch (Exception e) {
            _log.error(e);
        }

        if (taskRecordId <= 0) {

            // Add taskRecord

            _log.info("add taskRecord");
            
            // TODO: Use remote service
            return _taskRecordLocalService.addTaskRecord(userId, groupId, workPackage, description, ticketURL, endDate, startDate,
                    status, duration, serviceContext);

        } else {

            // Update taskRecord
            
            _log.info("update taskRecord");

            // TODO: Use remote service
            return _taskRecordLocalService.updateTaskRecord(userId, groupId, taskRecordId, workPackage, description, ticketURL,
                    endDate, startDate, status, duration, serviceContext);
        }
    }

    public TaskRecordLocalService getTaskRecordLocalService() {
        return _taskRecordLocalService;
    }

    public TaskRecordService getTaskRecordService() {
        return _taskRecordService;
    }

    @Override
    public void render(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        // set service bean

        request.setAttribute("taskRecordLocaleService", getTaskRecordLocalService());
        request.setAttribute("taskRecordService", getTaskRecordService());

        super.render(request, response);
    }

    @Reference
    public void setTaskRecordLocalService(TaskRecordLocalService taskRecordLocalService) {
        this._taskRecordLocalService = taskRecordLocalService;
    }

    @Reference
    public void setTaskRecordService(TaskRecordService taskRecordService) {
        this._taskRecordService = taskRecordService;
    }

    protected Hits search(ThemeDisplay themeDisplay, String keywords) throws Exception {

        SearchContext searchContext = new SearchContext();

        keywords = StringUtil.toLowerCase(keywords);

        searchContext.setAttribute(Field.NAME, keywords);
        searchContext.setAttribute("resourceName", keywords);

        searchContext.setCompanyId(themeDisplay.getCompanyId());
        searchContext.setEnd(SearchContainer.DEFAULT_DELTA);
        searchContext.setGroupIds(new long[0]);

        Group group = themeDisplay.getScopeGroup();

        searchContext.setIncludeStagingGroups(group.isStagingGroup());

        searchContext.setStart(0);
        searchContext.setUserId(themeDisplay.getUserId());

        // TODO
        // Indexer<?> indexer = TaskRecordSearcher.getInstance();
        //
        // return indexer.search(searchContext);
        return null;
    }

    private static final Log _log = LogFactoryUtil.getLog(TimetrackerPortlet.class.getName());

    private TaskRecordLocalService _taskRecordLocalService;
    private TaskRecordService _taskRecordService;

}
