
package ch.inofix.hook.geotag.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
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
 * @modified 2016-06-21 14:20
 * @version 1.0.1
 */
public class StartupAction extends SimpleAction {

    @Override
    public void run(String[] ids)
        throws ActionException {

        try {
            doRun(GetterUtil.getLong(ids[0]));
        }
        catch (Exception e) {
            throw new ActionException(e);
        }
    }

    protected void doRun(long companyId)
        throws Exception {

        ExpandoTable expandoTable = null;

        // Create a custom coordinates column required by the geotag-hook
        // for the JournalArticle class

        try {
            expandoTable =
                ExpandoTableLocalServiceUtil.addTable(
                    companyId, JournalArticle.class.getName(),
                    ExpandoTableConstants.DEFAULT_TABLE_NAME);
        }
        catch (Exception e) {
            expandoTable =
                ExpandoTableLocalServiceUtil.getTable(
                    companyId, JournalArticle.class.getName(),
                    ExpandoTableConstants.DEFAULT_TABLE_NAME);
        }

        try {
            ExpandoColumnLocalServiceUtil.addColumn(
                expandoTable.getTableId(), "coordinates",
                ExpandoColumnConstants.STRING);

        }
        catch (Exception ignore) {
        }

        // Create a custom coordinates column required by the geotag-hook
        // for the DLFileEntry class
        try {
            expandoTable =
                ExpandoTableLocalServiceUtil.addTable(
                    companyId, DLFileEntry.class.getName(),
                    ExpandoTableConstants.DEFAULT_TABLE_NAME);
        }
        catch (Exception e) {
            expandoTable =
                ExpandoTableLocalServiceUtil.getTable(
                    companyId, DLFileEntry.class.getName(),
                    ExpandoTableConstants.DEFAULT_TABLE_NAME);
        }

        try {
            ExpandoColumnLocalServiceUtil.addColumn(
                expandoTable.getTableId(), "coordinates",
                ExpandoColumnConstants.STRING);

        }
        catch (Exception ignore) {
        }
    }
}