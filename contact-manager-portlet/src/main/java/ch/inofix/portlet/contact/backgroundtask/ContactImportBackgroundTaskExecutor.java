package ch.inofix.portlet.contact.backgroundtask;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.model.BackgroundTask;

/**
 * @author Christian Berndt
 * @created 2016-10-06 19:48
 * @modified 2016-10-06 19:48
 * @version 1.0.0
 */
public class ContactImportBackgroundTaskExecutor extends
        BaseBackgroundTaskExecutor {

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask)
            throws Exception {

        Map<String, Serializable> taskContextMap = backgroundTask
                .getTaskContextMap();

        long userId = MapUtil.getLong(taskContextMap, "userId");
        long groupId = MapUtil.getLong(taskContextMap, "groupId");
        boolean privateLayout = MapUtil.getBoolean(taskContextMap,
                "privateLayout");
        Map<String, String[]> parameterMap = (Map<String, String[]>) taskContextMap
                .get("parameterMap");

        List<FileEntry> attachmentsFileEntries = backgroundTask
                .getAttachmentsFileEntries();

        for (FileEntry attachmentsFileEntry : attachmentsFileEntries) {

            ContactLocalServiceUtil.importContacts(userId, groupId,
                    privateLayout, parameterMap,
                    attachmentsFileEntry.getContentStream());
        }

        return BackgroundTaskResult.SUCCESS;
    }

    private static Log _log = LogFactoryUtil
            .getLog(ContactImportBackgroundTaskExecutor.class.getName());

}
