package ch.inofix.hook.cachefilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;

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
 * @modified 2015-11-27 15:17
 * @version 1.0.0
 * 
 */
public class CacheFilter implements Filter {

    private static final Log _log = LogFactoryUtil.getLog(CacheFilter.class
            .getName());

    private FilterConfig filterConfig = null;

    public void destroy() {
        filterConfig = null;
    }

    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        _log.info("Executing doFilter().");

        String uri = (String) servletRequest
                .getAttribute(WebKeys.INVOKER_FILTER_URI);

        _log.info("uri = " + uri);

        // Pass the request to the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);

    }

    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
    }

}
