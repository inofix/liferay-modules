
package ch.inofix.hook.geotag.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.util.GetterUtil;
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
 * @modified 2014-06-20 22:23
 * @version 1.0.0
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
            // Create a custom coordinates column required by the geotag-hook
            ExpandoColumnLocalServiceUtil.addColumn(
                expandoTable.getTableId(), "coordinates",
                ExpandoColumnConstants.STRING);

        }
        catch (Exception e) {
        }
    }
}
