package ch.inofix.portlet.form;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-20 16:38
 * @modified 2017-02-20 16:38
 * @version 1.0.0
 *
 */
public class MemberNumberException extends PortalException {

    public MemberNumberException() {
        super();
    }

    public MemberNumberException(String msg) {
        super(msg);
    }

    public MemberNumberException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MemberNumberException(Throwable cause) {
        super(cause);
    }

}
