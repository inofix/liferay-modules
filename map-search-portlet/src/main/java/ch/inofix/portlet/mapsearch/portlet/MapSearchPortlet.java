package ch.inofix.portlet.mapsearch.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;

import ch.inofix.portlet.mapsearch.search.Result;
import ch.inofix.portlet.mapsearch.util.PortletUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author Christian Berndt
 * @created 2016-07-23 16:05
 * @modified 2016-08-31 10:46
 * @version 1.0.5
 */
public class MapSearchPortlet extends MVCPortlet {

    // Enable logging for this class.
    private static final Log _log = LogFactoryUtil
            .getLog(MapSearchPortlet.class.getName());

    @Override
    public void serveResource(ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws IOException,
            PortletException {

        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest
                .getAttribute(WebKeys.THEME_DISPLAY);

        LiferayPortletRequest liferayPortletRequest = (LiferayPortletRequest) resourceRequest;

        LiferayPortletResponse liferayPortletResponse = (LiferayPortletResponse) resourceResponse;

        // TODO:
        // boolean inheritRedirect = true;

        PortletPreferences portletPreferences = resourceRequest
                .getPreferences();

        boolean viewByDefault = GetterUtil.getBoolean(
                portletPreferences.getValue("viewByDefault", null), true);

        boolean viewInContext = GetterUtil.getBoolean(
                portletPreferences.getValue("viewInContext", null), true);

        String currentURL = _getClosePopupURL(resourceRequest,
                resourceResponse, viewInContext);

        Locale locale = themeDisplay.getLocale();

        String[] classNames = StringUtil.split(
                portletPreferences.getValue("classNames", ""),
                StringPool.NEW_LINE);

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(resourceRequest);

        SearchContext searchContext = SearchContextFactory.getInstance(request);
        searchContext.setEntryClassNames(classNames);

        Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);
        assetEntriesFacet.setStatic(true);
        searchContext.addFacet(assetEntriesFacet);

        Facet scopeFacet = new ScopeFacet(searchContext);
        scopeFacet.setStatic(true);
        searchContext.addFacet(scopeFacet);

        Indexer indexer = FacetedSearcher.getInstance();

        Result result = new Result();

        String geoJSONField = "expando/custom_fields/geoJSON";

        long[] assetCategoryIds = null;
        String[] assetTagNames = null;

        try {

            Hits hits = indexer.search(searchContext);

            List<Document> documents = hits.toList();

            List<Document> filteredDocuments = new ArrayList<Document>();

            // We are only interested in the records with geoJSON data
            for (Document document : documents) {

                String geoJSON = document.get(geoJSONField);

                if (Validator.isNotNull(geoJSON)) {
                    filteredDocuments.add(document);
                }
            }

            result.setRecordsFiltered(filteredDocuments.size());
            result.setRecordsFiltered(filteredDocuments.size());
            result.setDraw(true);

            List<List<String>> data = new ArrayList<List<String>>();

            for (Document document : filteredDocuments) {

                String entryTitle = null;
                
                // TODO: unused
                String entrySummary = null;
                String downloadURL = null;
                
                PortletURL viewFullContentURL = null;
                String viewURL = null;

                String geoJSONString = document.get(geoJSONField);

                JSONObject geoJSON = JSONFactoryUtil
                        .createJSONObject(geoJSONString);

                JSONObject geometry = geoJSON.getJSONObject("geometry");

                // _log.info(geometry);

                String type = geometry.getString("type");

                String lat = "0";
                String lon = "0";

                if ("Point".equals(type)) {

                    JSONArray coordinates = geometry
                            .getJSONArray("coordinates");

                    lat = coordinates.getString(0);
                    lon = coordinates.getString(1);

                } else {

                    // TODO: Use the polygon's or path's center
                    // instead of the first point.

                    JSONArray points = geometry.getJSONArray("coordinates");
                    JSONArray coords = points.getJSONArray(0);
                    JSONArray coordinates = coords.getJSONArray(0);

                    lat = coordinates.getString(0);
                    lon = coordinates.getString(1);

                }

                String className = GetterUtil.getString(document
                        .get(Field.ENTRY_CLASS_NAME));

                long entryClassPK = GetterUtil.getLong(document
                        .get(Field.ENTRY_CLASS_PK));

                long resourcePrimKey = GetterUtil.getLong(document
                        .get(Field.ROOT_ENTRY_CLASS_PK));

                if (resourcePrimKey > 0) {
                    entryClassPK = resourcePrimKey;
                }

                AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
                        className, entryClassPK);

                AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil
                        .getAssetRendererFactoryByClassName(className);

                AssetRenderer assetRenderer = assetRendererFactory
                        .getAssetRenderer(entryClassPK);

                assetCategoryIds = assetEntry.getCategoryIds();
                assetTagNames = assetEntry.getTagNames();

                String portletId = document.get(Field.PORTLET_ID);

                downloadURL = assetRenderer.getURLDownload(themeDisplay);

                viewURL = _getViewURL(assetEntry, assetRendererFactory,
                        className, currentURL, document, entryClassPK,
                        liferayPortletRequest, liferayPortletResponse, request,
                        themeDisplay, viewInContext);

                viewFullContentURL = _getViewFullContentURL(request,
                        themeDisplay, portletId, document);

                Indexer assetIndexer = IndexerRegistryUtil
                        .getIndexer(className);

                Summary summary = null;

                if (assetIndexer != null) {
                    String snippet = document.get(Field.SNIPPET);

                    summary = assetIndexer.getSummary(document, locale,
                            snippet, viewFullContentURL);

                    entryTitle = summary.getTitle();
                    entrySummary = summary.getContent();
                } else if (assetRenderer != null) {
                    entryTitle = assetRenderer.getTitle(locale);
                    entrySummary = assetRenderer.getSearchSummary(locale);
                }

                if (!viewInContext) {
                    viewURL = HttpUtil.setParameter(viewURL, "p_p_state",
                            "pop_up");
                }

                String message = "view-x";

                PortletURL editURL = assetRenderer.getURLEdit(
                        liferayPortletRequest, liferayPortletResponse);

                if (!viewByDefault && editURL != null) {

                    editURL.setWindowState(LiferayWindowState.POP_UP);
                    editURL.setParameter("redirect", currentURL);

                    message = "edit-x";

                    viewURL = editURL.toString();
                }

                String taglibViewURL = "javascript:Liferay.Util.openWindow({id: '"
                        + liferayPortletResponse.getNamespace()
                        + "editAsset', title: '"
                        + HtmlUtil.escapeJS(LanguageUtil.format(locale,
                                message, HtmlUtil.escape(entryTitle)))
                        + "', uri:'" + HtmlUtil.escapeJS(viewURL) + "'});";

                StringBuilder sb = new StringBuilder();
                sb.append("<a href=\"");
                if (viewInContext) {
                    sb.append(viewURL);
                } else {
                    sb.append(taglibViewURL);
                }
                sb.append("\"");
                if (viewInContext) {
                    sb.append(" target=\"_blank\"");
                }
                sb.append(">");
                sb.append(_getLabel(assetRendererFactory, className,
                        entryClassPK, entryTitle, liferayPortletRequest,
                        viewByDefault));
                sb.append("</a>");
                
                // if (Validator.isNotNull(entrySummary)) {
                // sb.append(entrySummary);
                // }

                List<String> row = new ArrayList<String>();
                row.add(sb.toString());
                row.add(lat);
                row.add(lon);
                row.add(StringUtil.merge(assetCategoryIds));
                row.add(StringUtil.merge(assetTagNames));

                data.add(row);

            }

            result.setData(data);

        } catch (SearchException e) {
            _log.error(e);
        } catch (Exception e) {
            _log.error(e);
        }

        ObjectMapper mapper = new ObjectMapper();

        PortletResponseUtil.write(resourceResponse,
                mapper.writeValueAsString(result));

    }

    private String _getClosePopupURL(PortletRequest portletRequest,
            PortletResponse portletResponse, boolean viewInContext)
            throws WindowStateException {

        LiferayPortletResponse liferayPortletResponse = (LiferayPortletResponse) portletResponse;

        String currentURL = (String) portletRequest
                .getAttribute(WebKeys.CURRENT_URL);

        PortletURL portletURL = liferayPortletResponse.createRenderURL();
        if (!viewInContext) {
            portletURL.setWindowState(LiferayWindowState.POP_UP);
            portletURL.setParameter("mvcPath", "/html/close_popup.jsp");
        }
        portletURL.setParameter("redirect", HttpUtil.getPath(currentURL));
        portletURL.setParameter("windowId", "editAsset");

        return portletURL.toString();
    }

    private String _getLabel(AssetRendererFactory assetRendererFactory,
            String className, long entryClassPK, String entryTitle,
            LiferayPortletRequest liferayPortletRequest, boolean viewByDefault)
            throws Exception {

        StringBuilder label = new StringBuilder();

        AssetRenderer assetRenderer = assetRendererFactory
                .getAssetRenderer(entryClassPK);

        String thumbnail = assetRenderer
                .getThumbnailPath(liferayPortletRequest);

        if (Validator.isNotNull(thumbnail)) {
            label.append("<img src=\"");
            label.append(thumbnail);
            label.append("\" ");
            label.append("title=\"");
            if (viewByDefault) {
                label.append(PortletUtil.translate("click-to-view-x",
                        liferayPortletRequest.getLocale(),
                        HtmlUtil.escape(entryTitle)));
            } else {
                label.append(PortletUtil.translate("click-to-edit-x",
                        liferayPortletRequest.getLocale(),
                        HtmlUtil.escape(entryTitle)));
            }
            label.append("\"> ");
        }

        label.append(" <span class=\"title\">");
        label.append(entryTitle);
        label.append("</span>");

        return label.toString();

    }

    private PortletURL _getViewFullContentURL(HttpServletRequest request,
            ThemeDisplay themeDisplay, String portletId, Document document)
            throws Exception {

        long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

        if (groupId == 0) {
            Layout layout = themeDisplay.getLayout();

            groupId = layout.getGroupId();
        }

        long scopeGroupId = GetterUtil.getLong(document
                .get(Field.SCOPE_GROUP_ID));

        if (scopeGroupId == 0) {
            scopeGroupId = themeDisplay.getScopeGroupId();
        }

        long plid = LayoutConstants.DEFAULT_PLID;

        Layout layout = (Layout) request.getAttribute(WebKeys.LAYOUT);

        if (layout != null) {
            plid = layout.getPlid();
        }

        if (plid == 0) {
            plid = LayoutServiceUtil.getDefaultPlid(groupId, scopeGroupId,
                    portletId);
        }

        PortletURL portletURL = PortletURLFactoryUtil.create(request,
                portletId, plid, PortletRequest.RENDER_PHASE);

        portletURL.setPortletMode(PortletMode.VIEW);
        portletURL.setWindowState(WindowState.MAXIMIZED);

        return portletURL;
    }

    private String _getViewURL(AssetEntry assetEntry,
            AssetRendererFactory assetRendererFactory, String className,
            String currentURL, Document document, long entryClassPK,
            LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse,
            HttpServletRequest request, ThemeDisplay themeDisplay,
            boolean viewInContext) throws Exception {

        String viewURL = null;
        PortletURL viewFullContentURL = null;

        if (assetRendererFactory != null) {

            // AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
            // className, entryClassPK);

            AssetRenderer assetRenderer = assetRendererFactory
                    .getAssetRenderer(entryClassPK);

            viewFullContentURL = _getViewFullContentURL(request, themeDisplay,
                    PortletKeys.ASSET_PUBLISHER, document);

            viewFullContentURL.setParameter("struts_action",
                    "/asset_publisher/view_content");

            viewFullContentURL.setParameter("assetEntryId",
                    String.valueOf(assetEntry.getEntryId()));
            viewFullContentURL.setParameter("type",
                    assetRendererFactory.getType());

            if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
                if ((assetRenderer.getGroupId() > 0)
                        && (assetRenderer.getGroupId() != themeDisplay
                                .getScopeGroupId())) {
                    viewFullContentURL.setParameter("groupId",
                            String.valueOf(assetRenderer.getGroupId()));
                }

                viewFullContentURL.setParameter("urlTitle",
                        assetRenderer.getUrlTitle());
            }

            if (viewInContext || !assetEntry.isVisible()) {
                // inheritRedirect = true;

                String viewFullContentURLString = viewFullContentURL.toString();

                viewFullContentURLString = HttpUtil.setParameter(
                        viewFullContentURLString, "redirect", currentURL);

                viewURL = assetRenderer.getURLViewInContext(
                        liferayPortletRequest, liferayPortletResponse,
                        viewFullContentURLString);
            } else {
                viewURL = viewFullContentURL.toString();
                viewURL = HttpUtil
                        .setParameter(viewURL, "redirect", currentURL);
            }
        } else {
            String portletId = document.get(Field.PORTLET_ID);

            viewFullContentURL = _getViewFullContentURL(request, themeDisplay,
                    portletId, document);

            viewURL = viewFullContentURL.toString();

        }
        return viewURL;
    }
}
