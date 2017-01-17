package ch.inofix.referencemanager.backgroundtask;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.MapUtil;

import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-17 00:30
 * @modified 2017-01-17 15:15
 * @version 1.0.1
 *
 */
public class ReferenceImportBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {

        Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();

        long userId = MapUtil.getLong(taskContextMap, "userId");
        long groupId = MapUtil.getLong(taskContextMap, "groupId");
        boolean privateLayout = MapUtil.getBoolean(taskContextMap, "privateLayout");
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = (Map<String, String[]>) taskContextMap.get("parameterMap");
        ServiceContext serviceContext = (ServiceContext) taskContextMap.get("serviceContext"); 

        List<FileEntry> attachmentsFileEntries = backgroundTask.getAttachmentsFileEntries();

        for (FileEntry attachmentsFileEntry : attachmentsFileEntries) {

            ReferenceLocalServiceUtil.importReferences(userId, groupId, privateLayout, parameterMap,
                    attachmentsFileEntry.getContentStream(), serviceContext);
        }

        return BackgroundTaskResult.SUCCESS;
    }

    @Override
    public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
        return new ExportImportBackgroundTaskDisplay(backgroundTask);
    }

    @Override
    public BackgroundTaskExecutor clone() {
        return this;
    }

    @Override
    public boolean isSerial() {
        return super.isSerial();
    }

    private static final Log _log = LogFactoryUtil.getLog(ReferenceImportBackgroundTaskExecutor.class);

}
