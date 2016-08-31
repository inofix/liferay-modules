package ch.inofix.hook.downloadfilter.filter;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Christian Berndt
 * @created 2016-08-31 17:05
 * @modified 2016-08-31 17:05
 * @version 1.0.0
 */
public class DownloadFilter implements Filter {

    private boolean printMessages = false;

    @Override
    public void destroy() {
        if (printMessages) {
            System.out.println("Called DownloadFilter.destroy()");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String uri = (String) servletRequest
                .getAttribute(WebKeys.INVOKER_FILTER_URI);

        String[] tokens = uri.split(StringPool.SLASH);
        String fileName = null;
        if (tokens.length > 0) {
            fileName = tokens[tokens.length - 1];
        }

        boolean isDownload = ParamUtil.get((HttpServletRequest) servletRequest,
                "download", false);

        if (isDownload && fileName != null) {

            servletResponse.setContentType("application/force-download");
            ((HttpServletResponse) servletResponse).setHeader(
                    "Content-Transfer-Encoding", "binary");
            ((HttpServletResponse) servletResponse).setHeader(
                    "Content-Disposition", "attachment; filename=\"" + fileName
                            + "\"");
        }

        if (printMessages) {
            System.out.println("isDownload = " + isDownload);
            System.out.println("fileName = " + fileName);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {

        System.out
                .println("Called DownloadFilter.init() where printMessages = "
                        + filterConfig.getInitParameter("printMessages"));

        printMessages = GetterUtil.getBoolean(filterConfig
                .getInitParameter("printMessages"));
    }
}
