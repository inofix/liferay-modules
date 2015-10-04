package ch.inofix.portlet.cdav;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * 
 * @author Christian Berndt
 * @created 2015-07-30 17:42
 * @modified 2015-07-30 17:42
 * @version 1.0.0
 *
 */
public class NoCalendarSelectedException extends PortalException {

	public NoCalendarSelectedException() {
		super();
	}

	public NoCalendarSelectedException(String msg) {
		super(msg);
	}

	public NoCalendarSelectedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoCalendarSelectedException(Throwable cause) {
		super(cause);
	}

}
