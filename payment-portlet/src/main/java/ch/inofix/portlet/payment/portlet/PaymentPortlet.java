package ch.inofix.portlet.payment.portlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * PaymentPortlet: MVC-Controller of the payment-portlet.
 *
 * @author Christian Berndt
 * @created 2017-02-03 19:09
 * @modified 2017-02-03 19:09
 * @version 1.0.0
 */
public class PaymentPortlet extends MVCPortlet {

    public void submitPayment(ActionRequest actionRequest,
            ActionResponse actionResponse) throws PortalException,
            SystemException, IOException {

        _log.info("submitPayment()");

        // TODO: read apiKey from (site-wide) configuration

        String apiKey = "aab1fbbca555e0e70c27";

        // TODO: read apiURL from (site-wide) configuration

        String apiURL = "https://testapi.betterpayment.de/rest/capture";

        // TODO: read outgoingKey from (site-wide) configuration

        String outgoingKey = "4d422da6fb8e3bb2749a";

        StringBuilder sb = new StringBuilder();
        sb.append("api_key");
        sb.append(StringPool.EQUAL);
        sb.append(apiKey);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("api_key", apiKey));

        Enumeration<String> names = actionRequest.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = actionRequest.getParameter(name);

            if (Validator.isNotNull(value)) {
                // _log.info("name = " + name);
                // _log.info("value = " + value);
                sb.append(StringPool.AMPERSAND);
                sb.append(name);
                sb.append(StringPool.EQUAL);
                sb.append(HttpUtil.encodeURL(value));

                nvps.add(new BasicNameValuePair(name, value));

            }
        }

        String checksum = null;

        try {
            checksum = sha1(sb.toString() + outgoingKey);
        } catch (NoSuchAlgorithmException e) {
            _log.error(e.getMessage());
        }

        // _log.info("checksum = " + checksum);

        sb.append(StringPool.AMPERSAND);
        sb.append("check_sum");
        sb.append(StringPool.EQUAL);
        sb.append(checksum);

        nvps.add(new BasicNameValuePair("check_sum", checksum));

        // _log.info(sb.toString());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(apiURL);

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
        
        String str = ""; 

        try {
            _log.info(httpResponse.getStatusLine());
            HttpEntity httpEntity = httpResponse.getEntity();

            str = new Scanner(httpEntity.getContent(), "UTF-8")
                    .useDelimiter("\\A").next();

            _log.info(str);
            
            actionRequest.setAttribute("message", str);

            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(httpEntity);
        } finally {
            httpResponse.close();
        }
        
        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        
        actionResponse.setRenderParameter("tabs1", tabs1);
        
        // TODO: use JSON-fields
        
        boolean error = str.contains("error_code"); 
        
        // TODO: use JSON-fields
        
        actionRequest.setAttribute("message", str);
        
        if (error) {
            actionResponse.setRenderParameter("mvcPath", "/html/error.jsp");            
        } else {
            actionResponse.setRenderParameter("mvcPath", "/html/success.jsp");            
        }

    }

    public static void main(String[] args) throws ClientProtocolException,
            IOException {

        String apiURL = "https://testapi.betterpayment.de/rest/capture";
        String checksum = "9b6b075854fc3473c09700e20e19af3fbc3ff543";
        String outgoingKey = "4d422da6fb8e3bb2749a";
        String queryString = "api_key=aab1fbbca555e0e70c27&currency=EUR&merchant_reference=123&order_id=123&payment_type=cc&shipping_costs=3.50&amount=17.50";

        _log.info("checksum = " + checksum);
        try {
            _log.info("checksum = " + sha1(queryString + outgoingKey));
        } catch (NoSuchAlgorithmException e) {
            _log.error(e.getMessage());
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(apiURL);
        // HttpPost httpPost = new HttpPost("http://targethost/login");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse httpResponse = httpclient.execute(httpPost);

        try {
            System.out.println(httpResponse.getStatusLine());
            HttpEntity httpEntity = httpResponse.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(httpEntity);
        } finally {
            httpResponse.close();
        }

    }

    private static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

    private static Log _log = LogFactoryUtil.getLog(PaymentPortlet.class
            .getName());

}
