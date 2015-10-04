package ch.inofix.portlet.cdav.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import zswi.protocols.caldav.ServerCalendar;
import zswi.protocols.caldav.ServerVEvent;
import zswi.protocols.communication.core.HTTPSConnection;
import zswi.protocols.communication.core.InitKeystoreException;
import zswi.protocols.communication.core.InstallCertException;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import ch.inofix.portlet.cdav.SyncConnectionException;
import ch.inofix.portlet.cdav.SyncWithCalDAVServerException;

import com.liferay.calendar.NoSuchBookingException;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.calendar.service.CalendarLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

/**
 * Utility methods used to synchronize the Liferay Calendar with a
 * CalDAV-Server.
 * 
 * @author Christian Berndt
 * @created 2015-06-10 10:54
 * @modified 2015-07-30 18:55
 * @version 1.0.4
 *
 */
public class SyncUtil {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(SyncUtil.class.getName());

	/**
	 * 
	 * @param vEvent
	 * @param serviceContext
	 * @since 1.0.0
	 * @return
	 */
	public static CalendarBooking getCalendarBooking(VEvent vEvent) {

		log.trace("Executing getCalendarBooking().");

		log.info(vEvent);

		Locale defaultLocale = LocaleUtil.getDefault();

		Description vDescription = vEvent.getDescription();
		Summary vSummary = vEvent.getSummary();
		Location vLocation = vEvent.getLocation();
		RecurrenceId vRecurrenceId = vEvent.getRecurrenceId();

		long parentCalendarBookingId = 0;
		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
		String location = "";
		long startTime = 0;
		long endTime = 0;
		// TODO: Check whether it's all day
		boolean allDay = false;
		String recurrence = "";
		long firstReminder = 0;
		// Only valid notification type according to
		// com.liferay.calendar.notification.NotificationUtil is "email"
		String firstReminderType = "email";
		long secondReminder = 0;
		// Only valid notification type according to
		// com.liferay.calendar.notification.NotificationUtil is "email"
		String secondReminderType = "email";

		firstReminder = getReminder(vEvent, 0);

		secondReminder = getReminder(vEvent, 1);

		if (vSummary != null) {

			String summary = vSummary.getValue();

			// TODO: retrieve language from vSummary

			// if none was found, use the portal's default locale
			titleMap.put(defaultLocale, summary);

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

		startTime = getStartTime(vEvent);
		endTime = getEndTime(vEvent);
		
		RRule rRule=(RRule)vEvent.getProperty(Property.RRULE);
		
		if (rRule != null) {

			// TODO: process recurrence
			log.info("rRule.getName() = " + rRule.getName());
			log.info("rRule.getValue() = " + rRule.getValue());

		}

		// Store the vEvent parameters in a new calendarBooking

		CalendarBooking calendarBooking = CalendarBookingLocalServiceUtil
				.createCalendarBooking(0);

		calendarBooking.setAllDay(allDay);
		calendarBooking.setDescriptionMap(descriptionMap);
		calendarBooking.setEndTime(endTime);
		calendarBooking.setFirstReminder(firstReminder);
		calendarBooking.setFirstReminderType(firstReminderType);
		calendarBooking.setLocation(location);
		calendarBooking.setParentCalendarBookingId(parentCalendarBookingId);
		calendarBooking.setRecurrence(recurrence);
		calendarBooking.setSecondReminder(secondReminder);
		calendarBooking.setSecondReminderType(secondReminderType);
		calendarBooking.setStartTime(startTime);
		calendarBooking.setTitleMap(titleMap);

		return calendarBooking;

	}

	/**
	 * 
	 * @param calendarBooking
	 * @since 1.0.0
	 * @return
	 */
	public static VEvent getVEvent(CalendarBooking calendarBooking) {

		log.info("Executing getVEvent().");

		Locale locale = LocaleUtil.getDefault();

		long firstReminderTime = calendarBooking.getStartTime()
				- calendarBooking.getFirstReminder();
		DateTime firstReminderTrigger = new DateTime(firstReminderTime);
		VAlarm firstReminder = new VAlarm(firstReminderTrigger);

		long secondReminderTime = calendarBooking.getStartTime()
				- calendarBooking.getSecondReminder();
		DateTime secondReminderTrigger = new DateTime(secondReminderTime);
		VAlarm secondReminder = new VAlarm(secondReminderTrigger);

		Created created = new Created(new DateTime(
				calendarBooking.getCreateDate()));
		Description description = new Description(
				calendarBooking.getDescription(locale));
		// No GeographicPos since Liferay's calendar does not support
		// GeographicPos
		LastModified lastModified = new LastModified(new DateTime(
				calendarBooking.getModifiedDate()));
		Location location = new Location(calendarBooking.getLocation());
		// TODO Organizer - is this the one who created the event /
		// calendarBooking?
		// No Priority since Liferay's calendar does not support priorities
		// TODO: set RecurrenceId
		// TODO: set Sequence
		// TODO: set Status?
		Summary summary = new Summary(calendarBooking.getTitle(locale));
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

	/**
	 * 
	 * @param conn
	 * @param configuredCalendar
	 * @return
	 * @since 1.0.4
	 */
	public static ServerCalendar getServerCalendar(HTTPSConnection conn,
			String configuredCalendar) throws SyncConnectionException {

		log.debug("Executing getServerCalendar().");

		ServerCalendar syncCalendar = null;

		List<ServerCalendar> serverCalendars = new ArrayList<ServerCalendar>();

		try {

			serverCalendars = conn.getCalendars();

		} catch (ParserConfigurationException e) {

			log.error(e);
			throw new SyncConnectionException();

		} catch (SAXException e) {

			log.error(e);
			throw new SyncConnectionException();

		} catch (IOException e) {

			log.error(e);
			throw new SyncConnectionException();

		} catch (URISyntaxException e) {

			log.error(e);
			throw new SyncConnectionException();

		}

		log.trace("calendars.size() = " + serverCalendars.size());

		for (ServerCalendar serverCalendar : serverCalendars) {

			log.trace("serverCalendar.getDisplayName() = "
					+ serverCalendar.getDisplayName());

			log.trace("configuredCalendar = " + configuredCalendar);

			if (configuredCalendar.equals(serverCalendar.getDisplayName())) {

				log.debug("Synchronizing " + serverCalendar.getDisplayName());

				syncCalendar = serverCalendar;

			}
		}

		return syncCalendar;
	}

	/**
	 * 
	 * @param uuid
	 * @param serverVEvents
	 * @since 1.0.0
	 * @return
	 */
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
	 * @param vEvent
	 * @return
	 * @since 1.0.0
	 */
	private static long getEndTime(VEvent vEvent) {

		long endTime = 0;

		DtEnd dtEnd = vEvent.getEndDate();

		if (dtEnd != null) {

			Date vDate = dtEnd.getDate();
			TimeZone timeZone = dtEnd.getTimeZone();

			endTime = getTime(vDate, timeZone);

		}

		return endTime;

	}

	/**
	 * 
	 * @param calendarBooking
	 * @return
	 * @since 1.0.1
	 * @throws PortalException
	 * @throws SystemException
	 */
	private static String getETag(CalendarBooking calendarBooking)
			throws PortalException, SystemException {

		String className = CalendarBooking.class.getName();
		String tableName = ExpandoTableConstants.DEFAULT_TABLE_NAME;

		return (String) ExpandoValueLocalServiceUtil.getData(
				calendarBooking.getCompanyId(), className, tableName, "eTag",
				calendarBooking.getCalendarBookingId());
	}

	/**
	 * 
	 * @param vEvent
	 * @return
	 * @since 1.0.2
	 */
	private static long getLastModifiedTime(VEvent vEvent) {

		long lastModifiedTime = 0;

		LastModified lastModified = vEvent.getLastModified();
		if (lastModified != null) {
			DateTime dateTime = lastModified.getDateTime();
			if (dateTime != null) {
				lastModifiedTime = dateTime.getTime();
			}
		}

		return lastModifiedTime;
	}

	/**
	 * 
	 * @param vEvent
	 * @param idx
	 * @return
	 * @since 1.0.0
	 */
	private static long getReminder(VEvent vEvent, int idx) {

		long reminder = 0;

		ComponentList alarms = vEvent.getAlarms();

		if (alarms != null) {

			if (alarms.size() > idx) {

				Component alarm = (Component) alarms.get(idx);

				PropertyList properties = alarm.getProperties();

				Property trigger = properties.getProperty(Property.TRIGGER);

				if (trigger != null) {

					Parameter vRelated = trigger
							.getParameter(Parameter.RELATED);

					if (vRelated != null) {

						// Trigger is either defined in relation to the event's
						// start or end date

						String related = vRelated.getValue();

						String value = trigger.getValue();

						if ("PTOS".equals(value)) {

							reminder = 0;

						} else if (value.startsWith("-PT")) {

							int length = value.length();

							String unit = value.substring(length - 1, length);

							String val = value.replace("-PT", "");
							val = val.replace(unit, "");

							int factor = Integer.parseInt(val);

							if ("START".equals(related)) {

								long diff = 0;

								if ("H".equals(unit)) {
									diff = 1000 * 60 * 60 * factor;
								} else if ("M".equals(unit)) {
									diff = 1000 * 60 * factor;
								}
								reminder = diff;
							}
						}

					} else {

						// or absolute in a DateTime object
						try {

							String value = trigger.getValue();
							String defaultValue = "19760401T005545Z";

							if (!defaultValue.equals(value)) {

								DateTime dateTime = new DateTime(value);

								long startTime = getStartTime(vEvent);

								reminder = startTime - dateTime.getTime();

							}

						} catch (ParseException pe) {
							log.error(pe);
						}
					}
				}
			}
		}

		return reminder;

	}

	/**
	 * 
	 * @param vEvent
	 * @return
	 * @since 1.0.0
	 */
	private static long getStartTime(VEvent vEvent) {

		long startTime = 0;

		DtStart dtStart = vEvent.getStartDate();

		if (dtStart != null) {

			Date vDate = dtStart.getDate();
			TimeZone timeZone = dtStart.getTimeZone();

			startTime = getTime(vDate, timeZone);

		}

		return startTime;

	}

	/**
	 * 
	 * @param date
	 * @param timeZone
	 * @return
	 * @since 1.0.0
	 */
	private static long getTime(Date date, TimeZone timeZone) {

		long time = 0;

		Calendar cal = Calendar.getInstance();
		if (timeZone != null) {
			cal.setTimeZone(timeZone);
		}
		cal.setTime(date);
		time = cal.getTimeInMillis();

		return time;
	}

	/**
	 * 
	 * @param calendar
	 * @param serverVEvents
	 * @param restoreFromTrash
	 * @param serviceContext
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 * @throws ParserException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void syncFromCalDAVServer(ServerCalendar serverCalendar,
			com.liferay.calendar.model.Calendar targetCalendar,
			HTTPSConnection conn, boolean restoreFromTrash,
			boolean syncOnlyUpcoming, ServiceContext serviceContext)
			throws PortalException, SystemException {

		// TODO: Perform the sync in a background job

		List<ServerVEvent> serverVEvents = new ArrayList<ServerVEvent>();

		try {

			if (serverCalendar != null) {

				// Retrieve the events from the provided calendar
				serverVEvents = conn.getVEvents(serverCalendar);

			} else {
				// Retrieve the events from the default calendar
				serverVEvents = conn.getVEvents();
			}

		} catch (ClientProtocolException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (IOException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (URISyntaxException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (ParserException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		}

		for (ServerVEvent serverVEvent : serverVEvents) {

			String eTag = serverVEvent.geteTag();

			VEvent vEvent = serverVEvent.getVevent();

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

			CalendarBooking newBooking = SyncUtil.getCalendarBooking(vEvent);

			if (booking == null) {

				log.info("Adding new calendarBooking, since the event does "
						+ "not exist in this calendar, yet.");

				try {
					// Use the serverEvent's uuid for the new calendarBooking
					// too,
					// so that we can check, whether they are in sync or not
					serviceContext.setUuid(uuid);

					long[] childCalendarIds = new long[0];
					long parentCalendarBookingId = 0;

					CalendarBookingLocalServiceUtil.addCalendarBooking(
							targetCalendar.getUserId(),
							targetCalendar.getCalendarId(), childCalendarIds,
							parentCalendarBookingId, newBooking.getTitleMap(),
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

				// Check the whether the record is current (has the same eTag as
				// the record of the calDAV server.

				String currentETag = getETag(booking);

				log.info("title = " + vEvent.getSummary().getValue());
				log.info(eTag);
				log.info(currentETag);

				if (!eTag.equals(currentETag)) {

					if (booking.getStatus() != WorkflowConstants.STATUS_IN_TRASH
							| restoreFromTrash) {

						log.info("Updating calendarBooking, since the event has "
								+ "been modified on the remote server.");

						// TODO: But might have been modified in the liferay
						// calendar, too.

						// We have to bypass the standard way of setting
						// expandoBridgeAttributes with
						// portletserviceContext.setExpandoBridgeAttributes(eTagMap)
						// because of permission issues when we use the SyncUtil
						// with a scheduler and not from a portletRequest.

						ExpandoValueLocalServiceUtil.addValue(
								targetCalendar.getCompanyId(),
								CalendarBooking.class.getName(),
								ExpandoTableConstants.DEFAULT_TABLE_NAME,
								"eTag", booking.getCalendarBookingId(), eTag);

						long lastModifiedTime = getLastModifiedTime(vEvent);

						if (lastModifiedTime > 0) {
							serviceContext.setModifiedDate(new Date(
									lastModifiedTime));
						}

						try {

							CalendarBookingLocalServiceUtil
									.updateCalendarBooking(
											targetCalendar.getUserId(),
											booking.getCalendarBookingId(),
											targetCalendar.getCalendarId(),
											newBooking.getTitleMap(),
											newBooking.getDescriptionMap(),
											newBooking.getLocation(),
											newBooking.getStartTime(),
											newBooking.getEndTime(),
											newBooking.getAllDay(),
											newBooking.getRecurrence(),
											newBooking.getFirstReminder(),
											newBooking.getFirstReminderType(),
											newBooking.getSecondReminder(),
											newBooking.getSecondReminderType(),
											WorkflowConstants.STATUS_APPROVED,
											serviceContext);
						} catch (Exception e) {
							log.error(e);
						}

					} else {
						log.info("Not updating, since the booking has been "
								+ "moved to trash and the restoreFromTrash "
								+ "flag has not been set.");
					}

				} else {
					log.info("Skipping update, since the event has not "
							+ "been modified on the remote server.");
				}

			}

		}

	}

	/**
	 * 
	 * @param calendar
	 * @param conn
	 * @param restoreFromTrash
	 * @param syncOnlyUpcoming
	 * @param serviceContext
	 * @since 1.0.0
	 * @throws PortalException
	 * @throws SystemException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserException
	 */
	public static void syncToCalDAVServer(ServerCalendar serverCalendar,
			com.liferay.calendar.model.Calendar targetCalendar,
			HTTPSConnection conn, boolean restoreFromTrash,
			boolean syncOnlyUpcoming, ServiceContext serviceContext)
			throws SystemException, PortalException {

		log.info("Executing syncToCalDAVServer().");

		// TODO: Perform the sync in a background job

		long startTime = 0;

		if (syncOnlyUpcoming) {
			java.util.Date now = new java.util.Date();
			startTime = now.getTime();
		}

		List<CalendarBooking> calendarBookings = CalendarBookingLocalServiceUtil
				.getCalendarBookings(targetCalendar.getCalendarId(), startTime,
						Long.MAX_VALUE);

		log.info("calendarBookings.size = " + calendarBookings.size());

		List<ServerVEvent> serverVEvents = new ArrayList<ServerVEvent>();

		try {
			if (serverCalendar != null) {
				// Retrieve the events from the provided calendar

				serverVEvents = conn.getVEvents(serverCalendar);

			} else {
				// Retrieve the events from the default calendar
				serverVEvents = conn.getVEvents();
			}

			for (CalendarBooking calendarBooking : calendarBookings) {

				String uuid = calendarBooking.getUuid();

				// Do not sync events from trash

				if (calendarBooking.getStatus() != WorkflowConstants.STATUS_IN_TRASH) {

					ServerVEvent serverVEvent = getServerVEvent(uuid,
							serverVEvents);

					VEvent vEvent = SyncUtil.getVEvent(calendarBooking);

					if (serverVEvent == null) {

						log.info("Adding a new event, since this event does "
								+ "not exist in the remote calendar yet.");

						if (serverCalendar != null) {
							// Add the event to configured calendar
							conn.addVEvent(vEvent, serverCalendar);
						} else {
							// Add the event to the default calendar
							conn.addVEvent(vEvent);
						}

					} else {

						long lastModifiedTime = 0;

						lastModifiedTime = getLastModifiedTime(serverVEvent
								.getVevent());

						String eTag = serverVEvent.geteTag();

						String currentETag = getETag(calendarBooking);

						log.info(calendarBooking.getTitle(serviceContext
								.getLocale()));
						log.info(eTag);
						log.info(currentETag);

						if (eTag.equals(currentETag)) {

							// DateTime of VEvent does not support millisecond
							// resolution so we have to compare the modification
							// dates on second level

							// TODO: Store the ModifiedDate timestamp as a
							// non-standard property
							long lastVEventModification = lastModifiedTime / 1000;
							long lastCalendarBookingModification = calendarBooking
									.getModifiedDate().getTime() / 1000;

							log.info(lastVEventModification);
							log.info(lastCalendarBookingModification);

							if (lastCalendarBookingModification > lastVEventModification) {

								log.info("Updating the event, since it has not been "
										+ "modified on the remote server.");

								serverVEvent.setVevent(vEvent);
								conn.updateVEvent(serverVEvent);

							} else {

								log.info("Not updating the event, since it has not "
										+ "been modified since the last synchronization");
							}

						} else {

							log.info("Not updating the event, since it has been "
									+ "modified on the remote server.");

							// TODO: But might have been modified in the liferay
							// calendar, too

						}

					}

				}
			}

		} catch (ClientProtocolException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (IOException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (URISyntaxException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		} catch (ParserException e) {

			log.error(e);
			throw new SyncWithCalDAVServerException();

		}
	}

	/**
	 * 
	 * @param calendarId
	 * @param servername
	 * @param domain
	 * @param username
	 * @param password
	 * @param restoreFromTrash
	 * @param syncOnlyUpcoming
	 * @since 1.0.2
	 * @throws InstallCertException
	 * @throws InitKeystoreException
	 * @throws PortalException
	 * @throws SystemException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void syncWithCalDAVServer(long calendarId,
			String configuredCalendar, String servername, String domain,
			String username, String password, boolean restoreFromTrash,
			boolean syncOnlyUpcoming) throws InstallCertException,
			InitKeystoreException, PortalException, SystemException,
			ClientProtocolException, IOException, URISyntaxException,
			ParserException, ParserConfigurationException, SAXException {

		log.info("Executing syncWithCalDAVServer().");

		com.liferay.calendar.model.Calendar calendar = CalendarLocalServiceUtil
				.getCalendar(calendarId);

		// Do not create a keystore in the user's home directory
		// but use the truststore of the JRE installation. cdav-connect expects
		// it secured with the default keystore password "changeit".
		boolean installCert = false;

		HTTPSConnection conn = new HTTPSConnection(servername, domain,
				username, password, 443, installCert);

		ServerCalendar syncCalendar = null;

		List<ServerCalendar> serverCalendars = conn.getCalendars();

		log.info("calendars.size() = " + serverCalendars.size());

		for (ServerCalendar serverCalendar : serverCalendars) {

			log.info("serverCalendar.getDisplayName() = "
					+ serverCalendar.getDisplayName());

			log.info("configuredCalendar = " + configuredCalendar);

			if (configuredCalendar.equals(serverCalendar.getDisplayName())) {

				log.info("Synchronizing " + serverCalendar.getDisplayName());

				syncCalendar = serverCalendar;

			}
		}

		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setScopeGroupId(calendar.getGroupId());

		syncFromCalDAVServer(syncCalendar, calendar, conn, restoreFromTrash,
				syncOnlyUpcoming, serviceContext);

		syncToCalDAVServer(syncCalendar, calendar, conn, restoreFromTrash,
				syncOnlyUpcoming, serviceContext);

	}

}
