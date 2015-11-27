package ch.inofix.portlet.contact.service.messaging;

import ch.inofix.portlet.contact.service.ClpSerializer;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;
import ch.inofix.portlet.contact.service.ContactServiceUtil;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;


public class ClpMessageListener extends BaseMessageListener {
    public static String getServletContextName() {
        return ClpSerializer.getServletContextName();
    }

    @Override
    protected void doReceive(Message message) throws Exception {
        String command = message.getString("command");
        String servletContextName = message.getString("servletContextName");

        if (command.equals("undeploy") &&
                servletContextName.equals(getServletContextName())) {
            ContactLocalServiceUtil.clearService();

            ContactServiceUtil.clearService();
        }
    }
}
