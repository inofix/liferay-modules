package ch.inofix.referencemanager.backgroundtask;

import java.io.Serializable;
import java.util.Map;

import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.display.BaseBackgroundTaskDisplay;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateResource;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-17 15:42
 * @modified 2016-12-17 15:42
 * @version 1.0.0
 *
 */
public class ExportImportBackgroundTaskDisplay extends BaseBackgroundTaskDisplay {

    public ExportImportBackgroundTaskDisplay(BackgroundTask backgroundTask) {
        super(backgroundTask);

        _log.info("setup ReferenceImportBackgroundTaskDisplay");

        Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();

        // TODO
    }

    @Override
    public int getPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected TemplateResource getTemplateResource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Map<String, Object> getTemplateVars() {
        // TODO Auto-generated method stub
        return null;
    }

    private static final Log _log = LogFactoryUtil.getLog(ExportImportBackgroundTaskDisplay.class);

}
