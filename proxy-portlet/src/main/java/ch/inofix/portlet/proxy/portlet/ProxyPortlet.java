
package ch.inofix.portlet.proxy.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
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
 * @modified 2016-04-06 15:39
 * @version 1.0.0
 */
public class ProxyPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log =
        LogFactoryUtil.getLog(ProxyPortlet.class.getName());

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
        throws PortletException, IOException {

        _log.info("serveResource() v. 2");

        String url = ParamUtil.getString(request, "url");

        url = HttpUtil.decodeURL(url);

        _log.info("url = " + url);

        String str = "";

        if (Validator.isNotNull(url)) {

            // TODO: check for allowed hosts
            str = HttpUtil.URLtoString(url);
        }

        _log.info("str = " + str);

        PortletResponseUtil.write(response, str);

    }

}
