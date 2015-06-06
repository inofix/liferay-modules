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
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Transp;
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
 * @modified 2015-06-05 23:20
 * @version 1.0.2
 *
 */
public class CDAVManagerPortlet extends MVCPortlet {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(CDAVManagerPortlet.class
			.getName());

	private static CalendarBooking calendarBookingFromVEvent(Locale locale,
			ServiceContext serviceContext, VEvent vEvent) {

		Summary vSummary = vEvent.getSummary();
		Description vDescription = vEvent.getDescription();
		Location vLocation = vEvent.getLocation();
		DtStart vStartDate = vEvent.getStartDate();
		// TODO: Handle NPE
		// DtEnd vEndDate = vEvent.getEndDate();
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
			titleMap.put(locale, summary);

			// TODO: the site's default locale?

		}

		if (vDescription != null) {

			String description = vDescription.getValue();

			// TODO: retrieve the language from vDescription

			// if none was found, use the portal's default locale
			descriptionMap.put(locale, description);

		}

		if (vLocation != null) {

			location = vLocation.getValue();

		}

		if (vStartDate != null) {

			Date vDate = vStartDate.getDate();
			vTimeZone = vStartDate.getTimeZone();

			Calendar startDate = Calendar.getInstance();
			startDate.setTimeZone(vTimeZone);

			if (vDate != null) {
				long time = vDate.getTime();

				log.info("time = " + time);
				// startDate.setTimeInMillis(time);
			}

			startTime = startDate.getTimeInMillis();

		}

		// if (vEndDate != null) {
		//
		// Date vDate = vEndDate.getDate();
		// vTimeZone = vEndDate.getTimeZone();
		//
		// Calendar endDate = Calendar.getInstance();
		// endDate.setTimeZone(vTimeZone);
		// endDate.setTime(vDate);
		//
		// endTime = endDate.getTimeInMillis();
		//
		// }

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

			// log.info("uuid = " + uuid);

		}

		// Store the vEvent parameters in a new calendarBooking

		CalendarBooking calendarBooking = CalendarBookingLocalServiceUtil
				.createCalendarBooking(0);

		calendarBooking.setAllDay(allDay);
		// TODO:
		// calendarBooking.setCalendarResourceId(0);
		calendarBooking.setCompanyId(serviceContext.getCompanyId());
		calendarBooking.setDescriptionMap(descriptionMap);
		calendarBooking.setEndTime(endTime);
		calendarBooking.setFirstReminder(firstReminder);
		calendarBooking.setFirstReminderType(firstReminderType);
		calendarBooking.setGroupId(serviceContext.getScopeGroupId());
		// TODO: ?
		// calendarBooking.setInstanceIndex(0);
		calendarBooking.setLocation(location);
		// TODO: ?
		// calendarBooking.setModifiedDate();
		calendarBooking.setParentCalendarBookingId(parentCalendarBookingId);
		calendarBooking.setRecurrence(recurrence);
		calendarBooking.setSecondReminder(secondReminder);
		calendarBooking.setSecondReminderType(secondReminderType);
		calendarBooking.setStartTime(startTime);
		// TODO: ?
		// calendarBooking.setStatus(0);
		// TODO: ?
		// calendarBooking.setStatusDate();
		calendarBooking.setTitleMap(titleMap);
		calendarBooking.setUserId(serviceContext.getUserId());

		return calendarBooking;
	}

	private static ServerVEvent getServerVEvent(String uuid,
			List<ServerVEvent> serverVEvents) {

		ServerVEvent matchingEvent = null;

		for (ServerVEvent serverVEvent : serverVEvents) {

			VEvent vEvent = serverVEvent.getVevent();

			if (vEvent != null) {

				Uid uid = vEvent.getUid();

				if (uid != null) {

					if (uuid.equals(uid.getValue())) {

						return serverVEvent;

					}
				}

			}

		}

		return matchingEvent;

	}

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

		// log.info("domain = " + domain);
		// log.info("password = " + password);
		// log.info("servername = " + servername);
		// log.info("username = " + username);

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

		// log.info("Executing syncResources().");

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

		for (ServerCalendar serverCalendar : calendars) {

			if (calendar.equals(serverCalendar.getDisplayName())) {

				// log.info("Synching " + serverCalendar.getDisplayName());

				List<ServerVEvent> vEvents = conn.getVEvents(serverCalendar);

				// Sync from cDAV-Server
				// syncFromCDAVServer(calendarId, defaultLocale,
				// restoreFromTrash,
				// serviceContext, vEvents);

				// Sync to cDAV-Server
				syncToCDAVServer(calendarId, conn, defaultLocale,
						serverCalendar, syncOnlyUpcoming, vEvents);

			}
		}

		conn.shutdown();

	}

	private static void syncFromCDAVServer(long calendarId, Locale locale,
			boolean restoreFromTrash, ServiceContext serviceContext,
			List<ServerVEvent> vEvents) throws PortalException, SystemException {

		for (ServerVEvent serverEvent : vEvents) {

			VEvent vEvent = serverEvent.getVevent();

			String uuid = "";

			if (vEvent != null) {
				Uid uid = vEvent.getUid();
				if (uid != null) {
					uuid = uid.getValue();
				}
			}

			// Check whether a booking with this uuid already
			// exists in this scope
			CalendarBooking booking = null;

			try {
				booking = CalendarBookingLocalServiceUtil
						.getCalendarBookingByUuidAndGroupId(uuid,
								serviceContext.getScopeGroupId());
			} catch (NoSuchBookingException nsbe) {
				// ignore
			}

			CalendarBooking newBooking = calendarBookingFromVEvent(locale,
					serviceContext, vEvent);

			// TODO
			long[] childCalendarBookings = new long[0];

			if (booking == null) {

				try {
					CalendarBookingServiceUtil.addCalendarBooking(calendarId,
							childCalendarBookings,
							newBooking.getParentCalendarBookingId(),
							newBooking.getTitleMap(),
							newBooking.getDescriptionMap(),
							newBooking.getLocation(),
							newBooking.getStartTime(), newBooking.getEndTime(),
							newBooking.getAllDay(), newBooking.getRecurrence(),
							newBooking.getFirstReminder(),
							newBooking.getFirstReminderType(),
							newBooking.getSecondReminder(),
							newBooking.getSecondReminderType(), serviceContext);
				} catch (Exception e) {
					log.error(e);
				}
			} else {

				// TODO: Check the timestamp whether we can update the
				// log.info("booking already exists.");

				if (booking.getStatus() == WorkflowConstants.STATUS_IN_TRASH
						&& restoreFromTrash) {

					// log.info("restore booking from trash");

					CalendarBookingServiceUtil.updateCalendarBooking(
							booking.getCalendarBookingId(), calendarId,
							childCalendarBookings,
							newBooking.getDescriptionMap(),
							newBooking.getDescriptionMap(),
							newBooking.getLocation(),
							newBooking.getStartTime(), newBooking.getEndTime(),
							newBooking.getAllDay(), newBooking.getRecurrence(),
							newBooking.getFirstReminder(),
							newBooking.getFirstReminderType(),
							newBooking.getSecondReminder(),
							newBooking.getSecondReminderType(),
							WorkflowConstants.STATUS_APPROVED, serviceContext);
				}
			}

		}

	}

	private static void syncToCDAVServer(long calendarId, HTTPSConnection conn,
			Locale locale, ServerCalendar serverCalendar,
			boolean syncOnlyUpcoming, List<ServerVEvent> serverVEvents)
			throws PortalException, SystemException, ClientProtocolException,
			IOException, URISyntaxException {

		log.info("Executing syncToCDAVServer()");

		long startTime = 0;

		if (syncOnlyUpcoming) {
			java.util.Date now = new java.util.Date();
			startTime = now.getTime();
		}

		List<CalendarBooking> calendarBookings = CalendarBookingServiceUtil
				.getCalendarBookings(calendarId, startTime, Long.MAX_VALUE);

		for (CalendarBooking booking : calendarBookings) {

			String uuid = booking.getUuid();

			ServerVEvent serverVEvent = getServerVEvent(uuid, serverVEvents);

			VEvent vEvent = vEventFromCalendarBooking(booking, locale);

			if (serverVEvent == null) {

				conn.addVEvent(vEvent, serverCalendar);

			} else {

				serverVEvent.setVevent(vEvent);
				conn.updateVEvent(serverVEvent);

			}
		}
	}

	private static VEvent vEventFromCalendarBooking(
			CalendarBooking calendarBooking, Locale locale) {
		
		DateTime firstReminderTrigger = new DateTime(
				calendarBooking.getFirstReminder());
		VAlarm firstReminder = new VAlarm(firstReminderTrigger);
		
		DateTime secondReminderTrigger = new DateTime(
				calendarBooking.getSecondReminder()); 
		VAlarm secondReminder = new VAlarm(secondReminderTrigger); 

		// TODO: set Alarms
		// TODO: set Classification ?
		// TODO: set ConsumedTime ?
		Created created = new Created(new DateTime(
				calendarBooking.getCreateDate()));
		// TODO: set DateStamp?
		Description description = new Description(
				calendarBooking.getDescription(locale));
		// Duration duration = new Duration(
		// new java.util.Date(calendarBooking.getStartTime()),
		// new java.util.Date(calendarBooking.getEndTime()));
		// TODO: set GeographicPos
		LastModified lastModified = new LastModified(new DateTime(
				calendarBooking.getModifiedDate()));
		Location location = new Location(calendarBooking.getLocation());
		// TODO: set Name
		// TODO: set Occurence?
		// No organizer, since Liferay's calendar does not
		// know organizers
		// TODO: set Priority
		Priority priority = new Priority(0);
		// TODO: set RecurrenceId
		// TODO: set Sequence
		// TODO: set Status?
		Summary summary = new Summary(calendarBooking.getTitle(locale));
		// TODO: set Transparency?
		Transp transp = new Transp("5");
		Uid uid = new Uid(calendarBooking.getUuid());
		// TODO: set Url

		long startTime = calendarBooking.getStartTime();
		long endTime = calendarBooking.getEndTime();

		DateTime startDT = new DateTime(startTime);
		DateTime endDT = new DateTime(endTime);

		
		VEvent vEvent = new VEvent(startDT, endDT, summary.getValue());
		
		vEvent.getAlarms().add(firstReminder); 
		vEvent.getAlarms().add(secondReminder); 

		vEvent.getProperties().add(created);
		vEvent.getProperties().add(description);
		vEvent.getProperties().add(lastModified);
		vEvent.getProperties().add(location);
		vEvent.getProperties().add(uid);
		

		return vEvent;
	}
}
