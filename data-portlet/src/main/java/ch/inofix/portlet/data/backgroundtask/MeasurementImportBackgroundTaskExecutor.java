package ch.inofix.portlet.data.backgroundtask;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.model.BackgroundTask;

/**
 * @author Christian Berndt
 * @created 2017-03-09 17:43
 * @modified 2017-11-20 18:34
 * @version 1.0.2
 */
public class MeasurementImportBackgroundTaskExecutor extends
        BaseBackgroundTaskExecutor {

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask)
            throws Exception {

        Map<String, Serializable> taskContextMap = backgroundTask
                .getTaskContextMap();

        long userId = MapUtil.getLong(taskContextMap, "userId");
        long groupId = MapUtil.getLong(taskContextMap, "groupId");

        String extension = MapUtil.getString(taskContextMap, "extension");
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = (Map<String, String[]>) taskContextMap
                .get("parameterMap");

        List<FileEntry> attachmentsFileEntries = backgroundTask
                .getAttachmentsFileEntries();

        for (FileEntry attachmentsFileEntry : attachmentsFileEntries) {

            MeasurementLocalServiceUtil.importMeasurements(userId, groupId,
                    parameterMap, attachmentsFileEntry.getContentStream(),
                    extension);
        }

        return BackgroundTaskResult.SUCCESS;
    }

}
