package ch.inofix.portlet.cdav.portlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.xml.parsers.ParserConfigurationException;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import zswi.protocols.caldav.ServerCalendar;
import zswi.protocols.caldav.ServerVEvent;
import zswi.protocols.communication.core.HTTPSConnection;
import zswi.protocols.communication.core.InitKeystoreException;
import zswi.protocols.communication.core.InstallCertException;

import com.liferay.calendar.NoSuchBookingException;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.calendar.service.CalendarBookingServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-29 16:37
 * @modified 2015-06-01 10:17
 * @version 1.0.1
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
		String username = portletPreferences.getValue("username", "");

		// log.info("domain = " + domain);
		// log.info("password = " + password);
		// log.info("servername = " + servername);
		// log.info("username = " + username);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				CalendarBooking.class.getName(), actionRequest);

		syncResources(calendar, calendarId, domain, password, restoreFromTrash,
				servername, username, serviceContext);

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
			String servername, String username, ServiceContext serviceContext)
			throws InstallCertException, InitKeystoreException,
			ClientProtocolException, IOException, URISyntaxException,
			ParserConfigurationException, SAXException, ParserException,
			PortalException, SystemException {

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

		log.info("calendars.size = " + calendars.size());

		for (ServerCalendar serverCalendar : calendars) {

			if (calendar.equals(serverCalendar.getDisplayName())) {

				// Sync events from caldav-server to liferay calendar
				List<ServerVEvent> vEvents = conn.getVEvents(serverCalendar);

				log.info("vEvents.size() = " + vEvents.size());

				for (ServerVEvent serverEvent : vEvents) {

					VEvent vEvent = serverEvent.getVevent();

					Summary vSummary = vEvent.getSummary();
					Description vDescription = vEvent.getDescription();
					Location vLocation = vEvent.getLocation();
					DtStart vStartDate = vEvent.getStartDate();
					DtEnd vEndDate = vEvent.getEndDate();
					TimeZone vTimeZone = null;
					RecurrenceId vRecurrenceId = vEvent.getRecurrenceId();
					Uid uid = vEvent.getUid();

					long[] childCalendarIds = new long[0];
					long parentCalendarBookingId = 0;
					Map<Locale, String> titleMap = new HashMap<Locale, String>();
					Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
					String location = "";
					long startTime = 0;
					long endTime = 0;
					// TODO: Check whether it's all day
					boolean allDay = false;
					String recurrence = "";
					// TODO: Retrieve first reminder and type
					long firstReminder = 0;
					String firstReminderType = "";
					// TODO: Retrieve second reminder and type
					long secondReminder = 0;
					String secondReminderType = "";
					String uuid = null;

					if (vSummary != null) {

						String summary = vSummary.getValue();

						// TODO: retrieve language from vSummary

						// if none was found, use the portal's default locale
						titleMap.put(defaultLocale, summary);

						// TODO: the site's default locale?

					}

					if (vDescription != null) {

						String description = vDescription.getValue();

						// TODO: retrieve the language from vDescription

						// if none was found, use the portal's default locale
						descriptionMap.put(defaultLocale, description);

					}

					if (vLocation != null) {

						location = vLocation.getValue();

					}

					if (vStartDate != null) {

						Date vDate = vStartDate.getDate();
						vTimeZone = vStartDate.getTimeZone();

						Calendar startDate = Calendar.getInstance();
						startDate.setTimeZone(vTimeZone);
						startDate.setTime(vDate);

						startTime = startDate.getTimeInMillis();

					}

					if (vEndDate != null) {

						Date vDate = vEndDate.getDate();
						vTimeZone = vEndDate.getTimeZone();

						Calendar endDate = Calendar.getInstance();
						endDate.setTimeZone(vTimeZone);
						endDate.setTime(vDate);

						endTime = endDate.getTimeInMillis();

					}

					if (vRecurrenceId != null) {

						recurrence = vRecurrenceId.getValue();

						// log.info("recurrence = " + recurrence);

					}

					if (uid != null) {

						uuid = uid.getValue();

						if (Validator.isNotNull(uuid)) {
							serviceContext.setUuid(uuid);
						} else {
							uuid = UUID.randomUUID().toString();
						}

						log.info("uuid = " + uuid);

					}

					// Check whether a calendarBooking with this uuid already
					// exists in this scope
					CalendarBooking calendarBooking = null;
					try {
						calendarBooking = CalendarBookingLocalServiceUtil
								.getCalendarBookingByUuidAndGroupId(uuid,
										serviceContext.getScopeGroupId());
					} catch (NoSuchBookingException nsbe) {
						// ignore
					}

					log.info("calendarBooking = " + calendarBooking);

					if (calendarBooking == null) {

						try {
							CalendarBookingServiceUtil.addCalendarBooking(
									calendarId, childCalendarIds,
									parentCalendarBookingId, titleMap,
									descriptionMap, location, startTime,
									endTime, allDay, recurrence, firstReminder,
									firstReminderType, secondReminder,
									secondReminderType, serviceContext);
						} catch (Exception e) {
							log.error(e);
						}
					} else {

						// TODO: Check the timestamp whether we can update the
						log.info("calendarBooking already exists.");

						if (calendarBooking.getStatus() == WorkflowConstants.STATUS_IN_TRASH
								&& restoreFromTrash) {
							
							log.info("restore booking from trash");

							CalendarBookingServiceUtil.updateCalendarBooking(
									calendarBooking.getCalendarBookingId(),
									calendarId, childCalendarIds, titleMap,
									descriptionMap, location, startTime,
									endTime, allDay, recurrence, firstReminder,
									firstReminderType, secondReminder,
									secondReminderType,
									WorkflowConstants.STATUS_APPROVED,
									serviceContext);
						}
					}

				}

				// TODO: Sync calendarbookings from liferay calendar to caldav
				// server
			}
		}

		conn.shutdown();

	}
}
