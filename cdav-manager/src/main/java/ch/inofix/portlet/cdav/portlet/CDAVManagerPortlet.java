package ch.inofix.portlet.cdav.portlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.xml.parsers.ParserConfigurationException;

import net.fortuna.ical4j.data.ParserException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import zswi.protocols.caldav.ServerCalendar;
import zswi.protocols.caldav.ServerVEvent;
import zswi.protocols.carddav.ServerVCard;
import zswi.protocols.communication.core.HTTPSConnection;
import zswi.protocols.communication.core.InitKeystoreException;
import zswi.protocols.communication.core.InstallCertException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-29 16:37
 * @modified 2015-05-29 16:37
 * @version 1.0.0
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

		String domain = portletPreferences.getValue("domain", "");
		String password = portletPreferences.getValue("password", "");
		String servername = portletPreferences.getValue("servername", "");
		String username = portletPreferences.getValue("username", "");

		// log.info("domain = " + domain);
		// log.info("password = " + password);
		// log.info("servername = " + servername);
		// log.info("username = " + username);

		syncResources(domain, password, servername, username);

		actionResponse.setRenderParameter("mvcPath", mvcPath);

	}

	/**
	 * 
	 * @param domain
	 * @param password
	 * @param servername
	 * @param username
	 * @param servletContext
	 * @since 1.0.0
	 * @throws InstallCertException
	 * @throws InitKeystoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParserException
	 */
	private static void syncResources(String domain, String password,
			String servername, String username) throws InstallCertException,
			InitKeystoreException, ClientProtocolException, IOException,
			URISyntaxException, ParserConfigurationException, SAXException,
			ParserException {

		log.info("Executing syncResources().");

		// Do not create a keystore in the user's home directory
		// but use the truststore of the JRE installation. cdav-connect expects
		// it secured with the default keystore password "changeit".
		boolean installCert = false;

		HTTPSConnection conn = null;

		conn = new HTTPSConnection(servername, domain, username, password, 443,
				installCert);

		// Retrieve the user's personal contacts
		List<ServerVCard> vCards = conn.getVCards();

		log.info("vCards.size = " + vCards.size());

		// Retrieve the user's calendars
		List<ServerCalendar> calendars = conn.getCalendars();

		log.info("calendars.size = " + calendars.size());

		for (ServerCalendar calendar : calendars) {
			log.info(calendar.getDisplayName());

			List<ServerVEvent> events = conn.getVEvents(calendar);

			log.info("events.size = " + events.size());
		}

		conn.shutdown();

	}
}
