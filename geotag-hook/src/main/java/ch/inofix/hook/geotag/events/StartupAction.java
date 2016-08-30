package ch.inofix.hook.geotag.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;

/**
 * After the model of com.liferay.sampleexpando.hook.events.StartupAction.
 *
 * @author Christian Berndt
 * @created 2016-06-20 22:23
 * @modified 2016-08-30 12:49
 * @version 1.0.4
 */
public class StartupAction extends SimpleAction {

    @Override
    public void run(String[] ids) throws ActionException {

        try {
            doRun(GetterUtil.getLong(ids[0]));
        } catch (Exception e) {
            throw new ActionException(e);
        }
    }

    protected void doRun(long companyId) throws Exception {

        // Create a custom columns required by the geotag-hook.
        createColumn(companyId, DLFileEntry.class, "geoJSON");
        createColumn(companyId, DLFileEntry.class, "mapState");
        createColumn(companyId, JournalArticle.class, "geoJSON");
        createColumn(companyId, JournalArticle.class, "mapState");
    }

    private void createColumn(long companyId, Class<?> clazz, String fieldName)
            throws PortalException, SystemException {

        ExpandoTable expandoTable = null;

        try {
            expandoTable = ExpandoTableLocalServiceUtil.addTable(companyId,
                    clazz.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
        } catch (Exception e) {
            expandoTable = ExpandoTableLocalServiceUtil.getTable(companyId,
                    clazz.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
        }

        try {
            ExpandoColumnLocalServiceUtil.addColumn(expandoTable.getTableId(),
                    fieldName, ExpandoColumnConstants.STRING);

        } catch (Exception e) {
            System.out.println("StartupAction.e: " + e);
        }
    }
}
