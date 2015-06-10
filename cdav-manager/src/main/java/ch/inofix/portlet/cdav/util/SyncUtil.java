package ch.inofix.portlet.cdav.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;

import zswi.protocols.caldav.ServerVEvent;
import zswi.protocols.communication.core.HTTPSConnection;
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
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

import com.liferay.calendar.NoSuchBookingException;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.calendar.service.CalendarBookingServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;

/**
 * Utility methods used to synchronize the Liferay Calendar with a
 * CalDAV-Server.
 * 
 * @author Christian Berndt
 * @created 2015-06-10 10:54
 * @modified 2015-06-10 10:54
 * @version 1.0.0
 *
 */
public class SyncUtil {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(SyncUtil.class.getName());

	public static CalendarBooking getCalendarBooking(VEvent vEvent,
			ServiceContext serviceContext) {

		log.info("Executing getCalendarBooking().");

		log.info(vEvent);

		Locale defaultLocale = LocaleUtil.getDefault();

		Description vDescription = vEvent.getDescription();
		Summary vSummary = vEvent.getSummary();
		Location vLocation = vEvent.getLocation();
		RecurrenceId vRecurrenceId = vEvent.getRecurrenceId();
		Uid uid = vEvent.getUid();

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
		String uuid = null;

		firstReminder = getReminder(vEvent, 0);
		log.info("firstReminder = " + firstReminder);

		secondReminder = getReminder(vEvent, 1);
		log.info("secondReminder = " + secondReminder);

		if (vSummary != null) {

			String summary = vSummary.getValue();

			log.info("summary = " + summary);
			log.info("defaultLocale = " + defaultLocale);

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

		if (vRecurrenceId != null) {

			recurrence = vRecurrenceId.getValue();

			// TODO: process recurrence
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
		calendarBooking.setDescriptionMap(descriptionMap);
		calendarBooking.setEndTime(endTime);
		calendarBooking.setFirstReminder(firstReminder);
		calendarBooking.setFirstReminderType(firstReminderType);
		calendarBooking.setGroupId(serviceContext.getScopeGroupId());
		calendarBooking.setLocation(location);
		calendarBooking.setParentCalendarBookingId(parentCalendarBookingId);
		calendarBooking.setRecurrence(recurrence);
		calendarBooking.setSecondReminder(secondReminder);
		calendarBooking.setSecondReminderType(secondReminderType);
		calendarBooking.setStartTime(startTime);
		calendarBooking.setTitleMap(titleMap);
		calendarBooking.setUserId(serviceContext.getUserId());

		return calendarBooking;

	}

	public static VEvent getVEvent(CalendarBooking calendarBooking,
			Locale locale) {

		log.info("Executing getVEvent().");

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
		String descriptionStr = calendarBooking.getDescription(locale);
		Description description = new Description(descriptionStr);
		// Duration duration = new Duration(
		// new java.util.Date(calendarBooking.getStartTime()),
		// new java.util.Date(calendarBooking.getEndTime()));
		// TODO: set GeographicPos
		LastModified lastModified = new LastModified(new DateTime(
				calendarBooking.getModifiedDate()));
		String locationStr = calendarBooking.getLocation();
		log.info("locationStr = " + locationStr);
		Location location = new Location(locationStr);
		log.info("location.getValue() = " + location.getValue()); 
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

	private static long getTime(Date date, TimeZone timeZone) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(timeZone);
		cal.setTime(date);

		return cal.getTimeInMillis();
	}

	public static void syncFromCalDAVServer(
			com.liferay.calendar.model.Calendar calendar,
			List<ServerVEvent> serverVEvents, boolean restoreFromTrash,
			ServiceContext serviceContext) throws PortalException,
			SystemException {

		for (ServerVEvent serverVEvent : serverVEvents) {

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

			CalendarBooking newBooking = SyncUtil.getCalendarBooking(vEvent,
					serviceContext);

			// At the moment we do not support synchronization of
			// childCalendarBookings.
			long[] childCalendarBookings = new long[0];

			if (booking == null) {

				try {
					CalendarBookingServiceUtil.addCalendarBooking(
							calendar.getCalendarId(), childCalendarBookings,
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

				java.util.Date lastLocalModification = booking
						.getModifiedDate();
				java.util.Date lastRemoteModification = vEvent.getDateStamp()
						.getDateTime();

				log.info("lastLocalModification = "
						+ lastLocalModification.getTime());
				log.info("lastRemoteModification = "
						+ lastRemoteModification.getTime());

				// if (lastRemoteModification.after(lastLocalModification)) {

				// if (booking.getStatus() ==
				// WorkflowConstants.STATUS_IN_TRASH
				// && restoreFromTrash) {

				// log.info("restore booking from trash");

				CalendarBookingServiceUtil.updateCalendarBooking(
						booking.getCalendarBookingId(),
						calendar.getCalendarId(), childCalendarBookings,
						newBooking.getTitleMap(),
						newBooking.getDescriptionMap(),
						newBooking.getLocation(), newBooking.getStartTime(),
						newBooking.getEndTime(), newBooking.getAllDay(),
						newBooking.getRecurrence(),
						newBooking.getFirstReminder(),
						newBooking.getFirstReminderType(),
						newBooking.getSecondReminder(),
						newBooking.getSecondReminderType(),
						WorkflowConstants.STATUS_APPROVED, serviceContext);
			}

		}

	}

	public static void syncToCalDAVServer(HTTPSConnection conn,
			List<ServerVEvent> serverVEvents,
			com.liferay.calendar.model.Calendar source,
			boolean syncOnlyUpcoming, Locale locale) throws PortalException,
			SystemException, ClientProtocolException, IOException,
			URISyntaxException {

		log.info("Executing syncToCalDAVServer().");

		long startTime = 0;

		if (syncOnlyUpcoming) {
			java.util.Date now = new java.util.Date();
			startTime = now.getTime();
		}

		List<CalendarBooking> calendarBookings = CalendarBookingServiceUtil
				.getCalendarBookings(source.getCalendarId(), startTime,
						Long.MAX_VALUE);

		log.info("calendarBookings.size = " + calendarBookings.size());

		for (CalendarBooking booking : calendarBookings) {

			String uuid = booking.getUuid();

			// Do not sync events from trash
			if (booking.getStatus() != WorkflowConstants.STATUS_IN_TRASH) {

				ServerVEvent serverVEvent = getServerVEvent(uuid, serverVEvents);

				VEvent vEvent = SyncUtil.getVEvent(booking, locale);

				if (serverVEvent == null) {

					conn.addVEvent(vEvent);
					// conn.addVEvent(vEvent, serverCalendar);

				} else {

					serverVEvent.setVevent(vEvent);
					conn.updateVEvent(serverVEvent);

				}
			}
		}
	}

}
