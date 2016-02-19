package ch.inofix.portlet.cdav;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * 
 * @author Christian Berndt
 * @created 2015-07-30 18:14
 * @modified 2015-07-30 18:14
 * @version 1.0.0
 *
 */
public class SyncWithCalDAVServerException extends PortalException {

	public SyncWithCalDAVServerException() {
		super();
	}

	public SyncWithCalDAVServerException(String msg) {
		super(msg);
	}

	public SyncWithCalDAVServerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SyncWithCalDAVServerException(Throwable cause) {
		super(cause);
	}
}
