package ch.inofix.portlet.newsletter.backgroundtask;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.model.BackgroundTask;

/**
 * @author Christian Berndt
 * @created 2016-10-13 15:15
 * @modified 2016-11-01 12:35
 * @version 1.0.1
 */
public class MailingBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask)
            throws Exception {

        Map<String, Serializable> taskContextMap = backgroundTask
                .getTaskContextMap();

        long groupId = MapUtil.getLong(taskContextMap, "groupId");
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = (Map<String, String[]>) taskContextMap
                .get("parameterMap");
        
        Date now = new Date();

        MailingLocalServiceUtil.sendMailings(groupId, parameterMap, now);

        return BackgroundTaskResult.SUCCESS;
    }

    private static Log _log = LogFactoryUtil
            .getLog(MailingBackgroundTaskExecutor.class.getName());

}
