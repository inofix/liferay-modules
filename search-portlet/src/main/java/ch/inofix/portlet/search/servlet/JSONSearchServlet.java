package ch.inofix.portlet.search.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.StopWatch;

import ch.inofix.portlet.search.util.SearchUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;

/**
 * @author Christian Berndt
 * @created 2016-05-20 16:41
 * @modified 2016-10-01 11:29
 * @version 1.0.2
 */
@SuppressWarnings("serial")
public class JSONSearchServlet extends HttpServlet {

    // Enable logging for this class
    private static final Log _log = LogFactoryUtil
            .getLog(JSONSearchServlet.class.getName());

    /**
     * @since 1.0.0
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ThemeDisplay themeDisplay = null;

        try {
            themeDisplay = initThemeDisplay(request, response);
        } catch (Exception e) {
            _log.error(e);
        }

        if (themeDisplay == null) {
            return;
        }

        String classNames[] = ParamUtil
                .getParameterValues(request, "className");
        boolean useModel = ParamUtil.getBoolean(request, "useModel", false);

        SearchContext searchContext = SearchContextFactory.getInstance(request);
        searchContext.setEntryClassNames(classNames);

        Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);
        assetEntriesFacet.setStatic(true);
        searchContext.addFacet(assetEntriesFacet);

        Facet scopeFacet = new ScopeFacet(searchContext);
        scopeFacet.setStatic(true);
        searchContext.addFacet(scopeFacet);

        Indexer indexer = FacetedSearcher.getInstance();

        String json = "[]";

        try {

            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            if (_log.isDebugEnabled()) {
                _log.debug("Start search");
            }

            Hits hits = indexer.search(searchContext);

            if (_log.isDebugEnabled()) {
                _log.debug("Search took " + stopWatch.getTime() + " ms");
            }

            if (_log.isDebugEnabled()) {
                _log.debug("hits.getLength = " + hits.getLength());
            }

            json = SearchUtil.toJSON(hits, useModel);

        } catch (SearchException e) {
            _log.error(e);
        }

        response.setCharacterEncoding(StringPool.UTF8);
        response.setContentType(ContentTypes.TEXT_PLAIN_UTF8);

        ServletResponseUtil.write(response, json);

    }

    /**
     * Set up a minimal themeDisplay with the attributes required by the
     * SearchContextFactory.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ThemeDisplay initThemeDisplay(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ThemeDisplay themeDisplay = new ThemeDisplay();

        User user = null;

        user = _getUser(request);

        PrincipalThreadLocal.setName(user.getUserId());
        PrincipalThreadLocal.setPassword(PortalUtil.getUserPassword(request));

        PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
                .create(user);

        PermissionThreadLocal.setPermissionChecker(permissionChecker);

        Company company = PortalUtil.getCompany(request);

        themeDisplay = new ThemeDisplay();
        themeDisplay.setCompany(company);
        themeDisplay.setPermissionChecker(permissionChecker);
        themeDisplay.setUser(user);

        request.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

        return themeDisplay;
    }

    // From WebServerServlet
    private static User _getUser(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();

        if (PortalSessionThreadLocal.getHttpSession() == null) {
            PortalSessionThreadLocal.setHttpSession(session);
        }

        User user = PortalUtil.getUser(request);

        if (user != null) {
            return user;
        }

        String userIdString = (String) session.getAttribute("j_username");
        String password = (String) session.getAttribute("j_password");

        if ((userIdString != null) && (password != null)) {

            long userId = GetterUtil.getLong(userIdString);

            user = UserLocalServiceUtil.getUser(userId);
        } else {

            long companyId = PortalUtil.getCompanyId(request);

            Company company = CompanyLocalServiceUtil.getCompany(companyId);

            user = company.getDefaultUser();
        }

        return user;
    }
}
