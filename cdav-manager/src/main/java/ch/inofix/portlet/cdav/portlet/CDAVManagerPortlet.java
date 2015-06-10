package ch.inofix.portlet.cdav.portlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.xml.parsers.ParserConfigurationException;

import net.fortuna.ical4j.data.ParserException;
import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import zswi.protocols.caldav.ServerCalendar;
import zswi.protocols.caldav.ServerVEvent;
import zswi.protocols.communication.core.HTTPSConnection;
import zswi.protocols.communication.core.InitKeystoreException;
import zswi.protocols.communication.core.InstallCertException;
import ch.inofix.portlet.cdav.util.SyncUtil;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-29 16:37
 * @modified 2015-06-05 23:20
 * @version 1.0.2
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

		log.info("Executing syncResources().");

		PortletPreferences portletPreferences = actionRequest.getPreferences();

		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");

		// name of the remote calendar
		String calendar = portletPreferences.getValue("calendar", "");
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

		syncResources(calendar, calendarId, domain, password, restoreFromTrash,
				servername, syncOnlyUpcoming, username, serviceContext);

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
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParserException
	 * @throws SystemException
	 * @throws PortalException
	 */
	private static void syncResources(String calendar, long calendarId,
			String domain, String password, boolean restoreFromTrash,
			String servername, boolean syncOnlyUpcoming, String username,
			ServiceContext serviceContext) throws InstallCertException,
			InitKeystoreException, ClientProtocolException, IOException,
			URISyntaxException, ParserConfigurationException, SAXException,
			ParserException, PortalException, SystemException {

		log.info("Executing syncResources().");

		Locale defaultLocale = LocaleUtil.getDefault();

		// Do not create a keystore in the user's home directory
		// but use the truststore of the JRE installation. cdav-connect expects
		// it secured with the default keystore password "changeit".
		boolean installCert = false;

		HTTPSConnection conn = null;

		conn = new HTTPSConnection(servername, domain, username, password, 443,
				installCert);

		// Retrieve the user's calendars
		List<ServerCalendar> calendars = conn.getCalendars();

		// log.info("calendars.size() = " + calendars.size());

		for (ServerCalendar serverCalendar : calendars) {

			// log.info("serverCalendar.getDisplayName() = "
			// + serverCalendar.getDisplayName());

			if (calendar.equals(serverCalendar.getDisplayName())) {

				log.info("Synchronizing " + serverCalendar.getDisplayName());

				List<ServerVEvent> serverVEvents = conn.getVEvents();

				// List<ServerVEvent> serverVEvents = conn
				// .getVEvents(serverCalendar);

				log.info("serverVEvents.size() = " + serverVEvents.size());

				Calendar currentCalendar = CalendarLocalServiceUtil
						.getCalendar(calendarId);

				// Sync from cDAV-Server
				// SyncUtil.syncFromCalDAVServer(currentCalendar, serverVEvents,
				// restoreFromTrash, serviceContext);

				// Sync to cDAV-Server
				SyncUtil.syncToCalDAVServer(conn, serverVEvents,
						currentCalendar, syncOnlyUpcoming, defaultLocale);

			}
		}

		conn.shutdown();

	}

}
