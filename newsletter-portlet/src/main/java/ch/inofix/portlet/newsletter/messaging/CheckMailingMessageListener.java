package ch.inofix.portlet.newsletter.messaging;

import ch.inofix.portlet.newsletter.service.MailingLocalServiceUtil;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-14 18:08
 * @modified 2016-10-14 18:08
 * @version 1.0.0
 */
public class CheckMailingMessageListener extends BaseMessageListener {

    @Override
    protected void doReceive(Message message) throws Exception {
        MailingLocalServiceUtil.checkMailings();
    }

}
