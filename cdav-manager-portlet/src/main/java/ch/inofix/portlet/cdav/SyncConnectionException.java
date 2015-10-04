package ch.inofix.portlet.cdav;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * 
 * @author Christian Berndt
 * @created 2015-07-30 19:02
 * @modified 2015-07-30 19:02
 * @version 1.0.0
 *
 */
public class SyncConnectionException extends PortalException {

	public SyncConnectionException() {
		super();
	}

	public SyncConnectionException(String msg) {
		super(msg);
	}

	public SyncConnectionException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SyncConnectionException(Throwable cause) {
		super(cause);
	}
}
