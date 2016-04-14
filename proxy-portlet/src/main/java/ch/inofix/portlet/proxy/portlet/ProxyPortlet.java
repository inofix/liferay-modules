
package ch.inofix.portlet.proxy.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2016-04-06 15:39
 * @modified 2016-04-14 13:34
 * @version 1.0.2
 */
public class ProxyPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(ProxyPortlet.class.getName());

    @Override
    public void serveResource(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws PortletException, IOException {

        PortletPreferences portletPreferences =
            resourceRequest.getPreferences();

        String[] hosts = portletPreferences.getValues("hosts", new String[] {});

        String embedURL = ParamUtil.getString(resourceRequest, "embedURL");

        embedURL = HttpUtil.decodeURL(embedURL);

        String str = "";

        if (Validator.isNotNull(embedURL)) {

            boolean allowed = false;

            for (String host : hosts) {

                if (embedURL.contains(host)) {
                    allowed = true;
                }
            }

            if (allowed) {
                str = HttpUtil.URLtoString(embedURL);
            }
            else {
                _log.error("Access to " + embedURL +
                    " is not allowed. Check your Proxy Portlet configuration.");
            }

        }

        PortletResponseUtil.write(resourceResponse, str);

    }

}
