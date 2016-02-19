package ch.inofix.portlet.cdav.expando;

import com.liferay.calendar.model.CalendarBooking;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

/**
 * 
 * Create entries in the Expando table required by the cdav manager. (Based on
 * the model of com.liferay.sampleexpando.hook.events.StartupAction.)
 * 
 * @author Christian Berndt
 * @created 2015-06-11 10:36
 * @modified 2015-06-11 10:36
 * @version 1.0.0
 * 
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

		ExpandoTable expandoTable = null;

		try {
			expandoTable = ExpandoTableLocalServiceUtil.addTable(companyId,
					CalendarBooking.class.getName(),
					ExpandoTableConstants.DEFAULT_TABLE_NAME);
		} catch (Exception e) {
			expandoTable = ExpandoTableLocalServiceUtil.getTable(companyId,
					CalendarBooking.class.getName(),
					ExpandoTableConstants.DEFAULT_TABLE_NAME);
		}

		try {
			// Create custom columns for the CalendarBooking table
			// required by the cdav-manager portlet
			ExpandoColumnLocalServiceUtil.addColumn(expandoTable.getTableId(),
					"eTag", ExpandoColumnConstants.STRING);
		} catch (Exception e) {
		}
	}

}
