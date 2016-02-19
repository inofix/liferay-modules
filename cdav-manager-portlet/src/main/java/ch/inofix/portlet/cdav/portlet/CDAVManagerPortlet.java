package ch.inofix.portlet.cdav.portlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import zswi.protocols.caldav.ServerCalendar;
import zswi.protocols.communication.core.HTTPSConnection;
import zswi.protocols.communication.core.InitKeystoreException;
import zswi.protocols.communication.core.InstallCertException;
import ch.inofix.portlet.cdav.NoCalendarSelectedException;
import ch.inofix.portlet.cdav.SyncConnectionException;
import ch.inofix.portlet.cdav.util.SyncUtil;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-29 16:37
 * @modified 2015-07-30 19:57
 * @version 1.0.8
 *
 */
public class CDAVManagerPortlet extends MVCPortlet {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(CDAVManagerPortlet.class
			.getName());

	/**
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @since 1.0.0
	 * @throws Exception
	 */
	public void syncResources(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		log.debug("Executing syncResources().");

		PortletPreferences portletPreferences = actionRequest.getPreferences();

		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");

		// name of the remote configuredCalendar
		String configuredCalendar = portletPreferences.getValue("calendar", "");
		// id of the liferay calendar
		long calendarId = GetterUtil.getLong(portletPreferences.getValue(
				"calendarId", "0"));
		String domain = portletPreferences.getValue("domain", "");
		String password = portletPreferences.getValue("password", "");
		boolean restoreFromTrash = GetterUtil.getBoolean(portletPreferences
				.getValue("restoreFromTrash", "true"));
		String servername = portletPreferences.getValue("servername", "");
		boolean syncOnlyUpcoming = GetterUtil.getBoolean(portletPreferences
				.getValue("syncOnlyUpcoming", "true"));
		String username = portletPreferences.getValue("username", "");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				CalendarBooking.class.getName(), actionRequest);

		if (Validator.isNotNull(configuredCalendar)) {

			syncResources(configuredCalendar, calendarId, domain, password,
					restoreFromTrash, servername, syncOnlyUpcoming, username,
					serviceContext);

		} else {

			ResourceBundle messages = ResourceBundle.getBundle("Language",
					actionRequest.getLocale());

			String message = messages
					.getString("you-havent-selected-a-calendar-yet");

			log.error(message);

			throw new NoCalendarSelectedException();

		}

		actionResponse.setRenderParameter("mvcPath", mvcPath);

	}

	/**
	 * 
	 * @param domain
	 * @param password
	 * @param servername
	 * @param username
	 * @param serviceContext
	 * @since 1.0.0
	 * @throws InstallCertException
	 * @throws InitKeystoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws SAXException
	 * @throws SystemException
	 * @throws PortalException
	 */
	private static void syncResources(String configuredCalendar,
			long calendarId, String domain, String password,
			boolean restoreFromTrash, String servername,
			boolean syncOnlyUpcoming, String username,
			ServiceContext serviceContext) throws PortalException,
			SystemException {

		// Do not create a keystore in the user's home directory
		// but use the truststore of the JRE installation. cdav-connect
		// expects it secured with the default keystore password "changeit".
		boolean installCert = false;

		HTTPSConnection conn = null;

		try {

			conn = new HTTPSConnection(servername, domain, username, password,
					443, installCert);

		} catch (InstallCertException e) {

			log.error(e);
			throw new SyncConnectionException();

		} catch (InitKeystoreException e) {

			log.error(e);
			throw new SyncConnectionException();
		}

		// Retrieve the serverCalendar from the calDAV server.
		ServerCalendar serverCalendar = SyncUtil.getServerCalendar(conn,
				configuredCalendar);

		Calendar currentCalendar = CalendarLocalServiceUtil
				.getCalendar(calendarId);

		// Sync with the selected calendar
		if (serverCalendar != null) {

			SyncUtil.syncFromCalDAVServer(serverCalendar, currentCalendar, conn,
					restoreFromTrash, syncOnlyUpcoming, serviceContext);

			SyncUtil.syncToCalDAVServer(serverCalendar, currentCalendar, conn,
					restoreFromTrash, syncOnlyUpcoming, serviceContext);

		}

		conn.shutdown();

	}

}
