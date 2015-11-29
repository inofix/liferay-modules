package ch.inofix.hook.cachefilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.ByteBufferServletResponse;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.servlet.filters.CacheResponseData;
import com.liferay.util.servlet.filters.CacheResponseUtil;

/**
 * 
 * CacheFilter can be used as a replacement of Liferay's default CacheFilter.
 * Liferay's default CacheFilter has a very detailed distinction of client
 * parameters (e.g. OS and client version) which leads on public sites to a very
 * poor hit ratio. The Inofix CacheFilter borrows its core functionality from
 * Liferay's CacheFilter but lets you configure the desired granularity
 * yourself.
 * 
 * @author Christian Berndt
 * @created 2015-11-27 15:17
 * @modified 2015-11-29 18:17
 * @version 1.0.2
 * 
 */
public class CacheFilter extends BaseFilter {

    public static final String SKIP_FILTER = CacheFilter.class + "SKIP_FILTER";

    @Override
    protected void processFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws Exception {

        long userId = PortalUtil.getUserId(request);
        String remoteUser = request.getRemoteUser();

        if (_log.isDebugEnabled()) {
            _log.debug("Executing processFilter().");
            _log.debug("userId = " + userId);
            _log.debug("remoteUser = " + remoteUser);
        }

        request.setAttribute(SKIP_FILTER, Boolean.TRUE);

        long companyId = PortalUtil.getCompanyId(request);

        String key = companyId + StringPool.POUND + getCacheKey(request);

        CacheResponseData cacheResponseData = null;

        // Use the cached response data only for unauthenticated users
        // (otherwise authenticated users will receive cached responses for the
        // public layouts, too).
        if (userId == 0) {
            cacheResponseData = (CacheResponseData) MultiVMPoolUtil.get(
                    CacheFilter.class.getName(), key);
        }

        if (cacheResponseData == null) {

            if (!isCacheableData(companyId, request)) {

                if (_log.isDebugEnabled()) {
                    _log.debug("Request is not cacheable " + key);
                }

                processFilter(CacheFilter.class, request, response, filterChain);

                return;
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Caching request " + key);
            }

            ByteBufferServletResponse byteBufferResponse = new ByteBufferServletResponse(
                    response);

            processFilter(CacheFilter.class, request, byteBufferResponse,
                    filterChain);

            cacheResponseData = new CacheResponseData(byteBufferResponse);

            LastPath lastPath = (LastPath) request
                    .getAttribute(WebKeys.LAST_PATH);

            if (lastPath != null) {
                cacheResponseData.setAttribute(WebKeys.LAST_PATH, lastPath);
            }

            // Cache the result if and only if there is a result and the
            // request is cacheable. We have to test the cacheability of a
            // request twice because the user could have been authenticated
            // after the initial test.

            String cacheControl = GetterUtil.getString(byteBufferResponse
                    .getHeader(HttpHeaders.CACHE_CONTROL));

            if ((byteBufferResponse.getStatus() == HttpServletResponse.SC_OK)
                    && !cacheControl
                            .contains(HttpHeaders.PRAGMA_NO_CACHE_VALUE)
                    && isCacheableRequest(request)
                    && isCacheableResponse(byteBufferResponse)) {

                MultiVMPoolUtil.put(CacheFilter.class.getName(), key,
                        cacheResponseData);

            }
        }

        CacheResponseUtil.write(response, cacheResponseData);

    }

    public void init(FilterConfig filterConfig) {

        _pattern = GetterUtil.getInteger(filterConfig
                .getInitParameter("pattern"));

        if ((_pattern != _PATTERN_FRIENDLY) && (_pattern != _PATTERN_LAYOUT)
                && (_pattern != _PATTERN_RESOURCE)) {

            _log.error("Cache pattern is invalid");
        }
    }

    protected long getPlid(long companyId, String pathInfo, String servletPath,
            long defaultPlid) {

        if (_pattern == _PATTERN_LAYOUT) {
            return defaultPlid;
        }

        if (Validator.isNull(pathInfo)
                || !pathInfo.startsWith(StringPool.SLASH)) {

            return 0;
        }

        // Group friendly URL

        String friendlyURL = null;

        int pos = pathInfo.indexOf(CharPool.SLASH, 1);

        if (pos != -1) {
            friendlyURL = pathInfo.substring(0, pos);
        } else if (pathInfo.length() > 1) {
            friendlyURL = pathInfo;
        }

        if (Validator.isNull(friendlyURL)) {
            return 0;
        }

        long groupId = 0;
        boolean privateLayout = false;

        try {
            Group group = GroupLocalServiceUtil.getFriendlyURLGroup(companyId,
                    friendlyURL);

            groupId = group.getGroupId();

            if (servletPath.startsWith("/group")
                    || servletPath.startsWith("/user")) {

                privateLayout = true;
            } else if (servletPath.startsWith("/web")) {

                privateLayout = false;
            }
        } catch (NoSuchLayoutException nsle) {
            if (_log.isWarnEnabled()) {
                _log.warn(nsle);
            }
        } catch (Exception e) {
            if (_log.isWarnEnabled()) {
                _log.error(e);
            }

            return 0;
        }

        // Layout friendly URL

        friendlyURL = null;

        if ((pos != -1) && ((pos + 1) != pathInfo.length())) {

            // with friendly-url-routes
            if (pathInfo.indexOf("/-/", 1) > 0) {
                friendlyURL = pathInfo.substring(pos,
                        pathInfo.indexOf("/-/", 1));
            } else {
                friendlyURL = pathInfo.substring(pos);
            }

            if (_log.isDebugEnabled()) {
                _log.debug("friendlyURL = " + friendlyURL);
            }
        }

        if (Validator.isNull(friendlyURL)) {
            try {
                long plid = LayoutLocalServiceUtil.getDefaultPlid(groupId,
                        privateLayout);

                return plid;
            } catch (Exception e) {
                _log.warn(e);

                return 0;
            }
        } else if (friendlyURL.endsWith(StringPool.FORWARD_SLASH)) {
            friendlyURL = friendlyURL.substring(0, friendlyURL.length() - 1);
        }

        // If there is no layout path take the first from the group or user

        try {
            Layout layout = LayoutLocalServiceUtil.getFriendlyURLLayout(
                    groupId, privateLayout, friendlyURL);

            return layout.getPlid();
        } catch (NoSuchLayoutException nsle) {
            _log.warn(nsle);

            return 0;
        } catch (Exception e) {
            _log.error(e);

            return 0;
        }
    }

    protected String getCacheKey(HttpServletRequest request) {
        StringBundler sb = new StringBundler(13);

        // Url

        sb.append(HttpUtil.getProtocol(request));
        sb.append(Http.PROTOCOL_DELIMITER);
        sb.append(request.getContextPath());
        sb.append(request.getServletPath());
        sb.append(request.getPathInfo());
        sb.append(StringPool.QUESTION);

        String queryString = request.getQueryString();

        if (queryString == null) {
            queryString = (String) request
                    .getAttribute(JavaConstants.JAVAX_SERVLET_FORWARD_QUERY_STRING);

            if (queryString == null) {
                String url = PortalUtil.getCurrentCompleteURL(request);

                int pos = url.indexOf(StringPool.QUESTION);

                if (pos > -1) {
                    queryString = url.substring(pos + 1);
                }
            }
        }

        if (queryString != null) {
            sb.append(queryString);
        }

        // Language

        sb.append(StringPool.POUND);

        String languageId = (String) request.getAttribute("I18N_LANGUAGE_ID");
        // String languageId = (String)request.getAttribute(
        // WebKeys.I18N_LANGUAGE_ID);

        if (Validator.isNull(languageId)) {
            languageId = LanguageUtil.getLanguageId(request);
        }

        sb.append(languageId);

        // User agent

        // TODO: make user-agent processing configurable
        // String userAgent = GetterUtil.getString(request
        // .getHeader(HttpHeaders.USER_AGENT));
        //
        // _log.info("userAgent = " + userAgent);
        //
        // sb.append(StringPool.POUND);
        // sb.append(userAgent.toLowerCase().hashCode());

        // Gzip compression

        sb.append(StringPool.POUND);
        sb.append(BrowserSnifferUtil.acceptsGzip(request));

        return sb.toString().trim().toUpperCase();
    }

    protected boolean isCacheableColumn(long companyId, String columnSettings)
            throws SystemException {

        String[] portletIds = StringUtil.split(columnSettings);

        for (String portletId : portletIds) {
            portletId = PortletConstants.getRootPortletId(portletId);

            Portlet portlet = PortletLocalServiceUtil.getPortletById(companyId,
                    portletId);

            if (!portlet.isLayoutCacheable()) {
                return false;
            }
        }

        return true;
    }

    protected boolean isCacheableData(long companyId, HttpServletRequest request) {

        try {
            if (_pattern == _PATTERN_RESOURCE) {
                return true;
            }

            long plid = getPlid(companyId, request.getPathInfo(),
                    request.getServletPath(),
                    ParamUtil.getLong(request, "p_l_id"));

            if (plid <= 0) {
                return false;
            }

            Layout layout = LayoutLocalServiceUtil.getLayout(plid);

            if (!layout.isTypePortlet()) {
                return false;
            }

            UnicodeProperties properties = layout.getTypeSettingsProperties();

            for (int i = 0; i < 10; i++) {
                String columnId = "column-" + i;

                String settings = properties.getProperty(columnId,
                        StringPool.BLANK);

                if (!isCacheableColumn(companyId, settings)) {
                    return false;
                }
            }

            String columnIdsString = properties
                    .get(LayoutTypePortletConstants.NESTED_COLUMN_IDS);

            if (columnIdsString != null) {
                String[] columnIds = StringUtil.split(columnIdsString);

                for (String columnId : columnIds) {
                    String settings = properties.getProperty(columnId,
                            StringPool.BLANK);

                    if (!isCacheableColumn(companyId, settings)) {
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isCacheableRequest(HttpServletRequest request) {
        String portletId = ParamUtil.getString(request, "p_p_id");

        if (Validator.isNotNull(portletId)) {
            return false;
        }

        if ((_pattern == _PATTERN_FRIENDLY) || (_pattern == _PATTERN_LAYOUT)) {
            long userId = PortalUtil.getUserId(request);
            String remoteUser = request.getRemoteUser();

            if ((userId > 0) || Validator.isNotNull(remoteUser)) {
                return false;
            }
        }

        if (_pattern == _PATTERN_LAYOUT) {
            String plid = ParamUtil.getString(request, "p_l_id");

            if (Validator.isNull(plid)) {
                return false;
            }
        }

        return true;
    }

    protected boolean isCacheableResponse(
            ByteBufferServletResponse byteBufferResponse) {

        //
        // The cache filter caches processed web content. Set the threshold size
        // to prevent caching resources that are too large. The default value is
        // 500 kb.
        //
        // cache.content.threshold.size=512000

        int CACHE_CONTENT_THRESHOLD_SIZE = 1024000;

        if ((byteBufferResponse.getStatus() == HttpServletResponse.SC_OK)
                && (byteBufferResponse.getBufferSize() < CACHE_CONTENT_THRESHOLD_SIZE)) {

            return true;
        } else {
            return false;
        }
    }

    private static final int _PATTERN_FRIENDLY = 0;

    private static final int _PATTERN_LAYOUT = 1;

    private static final int _PATTERN_RESOURCE = 2;

    private static Log _log = LogFactoryUtil.getLog(CacheFilter.class);

    private int _pattern;

    @Override
    protected Log getLog() {
        return _log;
    }
}
