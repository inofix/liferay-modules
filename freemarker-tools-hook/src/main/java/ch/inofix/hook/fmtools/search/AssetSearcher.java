
package ch.inofix.hook.fmtools.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import ch.inofix.hook.fmtools.permission.AssetCategoryPermission;
import ch.inofix.hook.fmtools.permission.AssetTagPermission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.DefaultSearchResultPermissionFilter;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.SearchResultPermissionFilter;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;

/**
 * A customized version of com.liferay.portlet.assetpublisher.util.AssetSearcher
 * with explicit setting of the permissionChecker.
 * 
 * @author Eudaldo Alonso
 * @author Christian Berndt
 */
public class AssetSearcher extends BaseIndexer {

    // Enable logging for this class
    private static final Log _log =
        LogFactoryUtil.getLog(AssetSearcher.class.getName());

    public static Indexer getInstance() {

        return new AssetSearcher();
    }

    public AssetSearcher() {

        setFilterSearch(true);
        setPermissionAware(true);
    }

    @Override
    public String[] getClassNames() {

        long[] classNameIds = _assetEntryQuery.getClassNameIds();

        String[] classNames = new String[classNameIds.length];

        for (int i = 0; i < classNames.length; i++) {
            long classNameId = classNameIds[i];

            classNames[i] = PortalUtil.getClassName(classNameId);
        }

        return classNames;
    }

    @Override
    public IndexerPostProcessor[] getIndexerPostProcessors() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getPortletId() {

        return null;
    }

    @Override
    public void registerIndexerPostProcessor(
        IndexerPostProcessor indexerPostProcessor) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Hits search(SearchContext searchContext)
        throws SearchException {
        
        try {
            Hits hits = null;

            if ((_permissionChecker != null) &&
                isUseSearchResultPermissionFilter(searchContext)) {
                
                SearchResultPermissionFilter searchResultPermissionFilter =
                    new DefaultSearchResultPermissionFilter(
                        this, _permissionChecker);

                hits = searchResultPermissionFilter.search(searchContext);
            }
            else {
                hits = doSearch(searchContext);
            }

            processHits(searchContext, hits);

            return hits;
        }
        catch (SearchException se) {
            throw se;
        }
        catch (Exception e) {
            throw new SearchException(e);
        }
    }

    public void setAssetEntryQuery(AssetEntryQuery assetEntryQuery) {

        _assetEntryQuery = assetEntryQuery;
    }

    public void setPermissionChecker(PermissionChecker permissionChecker) {

        _permissionChecker = permissionChecker;
    }

    protected void addImpossibleTerm(BooleanQuery contextQuery, String field)
        throws Exception {

        contextQuery.addTerm(field, "-1", false, BooleanClauseOccur.MUST);
    }

    protected void addSearchAllCategories(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] allCategoryIds = _assetEntryQuery.getAllCategoryIds();

        if (allCategoryIds.length == 0) {
            return;
        }

        long[] filteredAllCategoryIds =
            filterCategoryIds(_permissionChecker, allCategoryIds);

        if (allCategoryIds.length != filteredAllCategoryIds.length) {
            addImpossibleTerm(contextQuery, Field.ASSET_CATEGORY_IDS);

            return;
        }

        BooleanQuery categoryIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long allCategoryId : filteredAllCategoryIds) {
            AssetCategory assetCategory =
                AssetCategoryLocalServiceUtil.fetchAssetCategory(allCategoryId);

            if (assetCategory == null) {
                continue;
            }

            List<Long> categoryIds = new ArrayList<Long>();

            if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
                categoryIds.addAll(AssetCategoryLocalServiceUtil.getSubcategoryIds(allCategoryId));
            }

            if (categoryIds.isEmpty()) {
                categoryIds.add(allCategoryId);
            }

            BooleanQuery categoryIdQuery =
                BooleanQueryFactoryUtil.create(searchContext);

            for (long categoryId : categoryIds) {
                categoryIdQuery.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
            }

            categoryIdsQuery.add(categoryIdQuery, BooleanClauseOccur.MUST);
        }

        contextQuery.add(categoryIdsQuery, BooleanClauseOccur.MUST);
    }

    protected void addSearchAllTags(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[][] allTagIdsArray = _assetEntryQuery.getAllTagIdsArray();

        if (allTagIdsArray.length == 0) {
            return;
        }

        BooleanQuery tagIdsArrayQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long[] allTagIds : allTagIdsArray) {
            if (allTagIds.length == 0) {
                continue;
            }

            long[] filteredAllTagIds =
                filterTagIds(_permissionChecker, allTagIds);

            if (allTagIds.length != filteredAllTagIds.length) {
                addImpossibleTerm(contextQuery, Field.ASSET_TAG_IDS);

                return;
            }

            BooleanQuery tagIdsQuery =
                BooleanQueryFactoryUtil.create(searchContext);

            for (long tagId : allTagIds) {
                tagIdsQuery.addTerm(Field.ASSET_TAG_IDS, tagId);
            }

            tagIdsArrayQuery.add(tagIdsQuery, BooleanClauseOccur.MUST);
        }

        contextQuery.add(tagIdsArrayQuery, BooleanClauseOccur.MUST);
    }

    protected void addSearchAnyCategories(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] anyCategoryIds = _assetEntryQuery.getAnyCategoryIds();

        if (anyCategoryIds.length == 0) {
            return;
        }

        long[] filteredAnyCategoryIds =
            filterCategoryIds(_permissionChecker, anyCategoryIds);

        if (filteredAnyCategoryIds.length == 0) {
            addImpossibleTerm(contextQuery, Field.ASSET_CATEGORY_IDS);

            return;
        }

        BooleanQuery categoryIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long anyCategoryId : filteredAnyCategoryIds) {
            AssetCategory assetCategory =
                AssetCategoryLocalServiceUtil.fetchAssetCategory(anyCategoryId);

            if (assetCategory == null) {
                continue;
            }

            List<Long> categoryIds = new ArrayList<Long>();

            if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
                categoryIds.addAll(AssetCategoryLocalServiceUtil.getSubcategoryIds(anyCategoryId));
            }

            if (categoryIds.isEmpty()) {
                categoryIds.add(anyCategoryId);
            }

            for (long categoryId : categoryIds) {
                categoryIdsQuery.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
            }
        }

        contextQuery.add(categoryIdsQuery, BooleanClauseOccur.MUST);
    }

    protected void addSearchAnyTags(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] anyTagIds = _assetEntryQuery.getAnyTagIds();

        if (anyTagIds.length == 0) {
            return;
        }

        long[] filteredAnyTagIds = filterTagIds(_permissionChecker, anyTagIds);

        if (filteredAnyTagIds.length == 0) {
            addImpossibleTerm(contextQuery, Field.ASSET_TAG_IDS);

            return;
        }

        BooleanQuery tagIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long tagId : anyTagIds) {
            tagIdsQuery.addTerm(Field.ASSET_TAG_IDS, tagId);
        }

        contextQuery.add(tagIdsQuery, BooleanClauseOccur.MUST);
    }

    @Override
    protected void addSearchAssetCategoryIds(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        addSearchAllCategories(contextQuery, searchContext);
        addSearchAnyCategories(contextQuery, searchContext);
        addSearchNotAnyCategories(contextQuery, searchContext);
        addSearchNotAllCategories(contextQuery, searchContext);
    }

    @Override
    protected void addSearchAssetTagNames(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        addSearchAllTags(contextQuery, searchContext);
        addSearchAnyTags(contextQuery, searchContext);
        addSearchNotAllTags(contextQuery, searchContext);
        addSearchNotAnyTags(contextQuery, searchContext);
    }

    @Override
    protected void addSearchKeywords(
        BooleanQuery searchQuery, SearchContext searchContext)
        throws Exception {

        String keywords = searchContext.getKeywords();

        if (Validator.isNull(keywords)) {
            return;
        }

        super.addSearchKeywords(searchQuery, searchContext);

        String field =
            DocumentImpl.getLocalizedName(
                searchContext.getLocale(), "localized_title");

        searchQuery.addTerm(field, keywords, true);
    }

    @Override
    protected void addSearchLayout(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        String layoutUuid =
            (String) searchContext.getAttribute(Field.LAYOUT_UUID);

        if (Validator.isNotNull(layoutUuid)) {
            contextQuery.addRequiredTerm(Field.LAYOUT_UUID, layoutUuid);
        }
    }

    protected void addSearchNotAllCategories(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] notAllCategoryIds = _assetEntryQuery.getNotAllCategoryIds();

        if (notAllCategoryIds.length == 0) {
            return;
        }

        BooleanQuery categoryIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long notAllCategoryId : notAllCategoryIds) {
            AssetCategory assetCategory =
                AssetCategoryLocalServiceUtil.fetchAssetCategory(notAllCategoryId);

            if (assetCategory == null) {
                continue;
            }

            List<Long> categoryIds = new ArrayList<Long>();

            if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
                categoryIds.addAll(AssetCategoryLocalServiceUtil.getSubcategoryIds(notAllCategoryId));
            }

            if (categoryIds.isEmpty()) {
                categoryIds.add(notAllCategoryId);
            }

            BooleanQuery categoryIdQuery =
                BooleanQueryFactoryUtil.create(searchContext);

            for (long categoryId : categoryIds) {
                categoryIdQuery.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
            }

            categoryIdsQuery.add(categoryIdQuery, BooleanClauseOccur.MUST);
        }

        contextQuery.add(categoryIdsQuery, BooleanClauseOccur.MUST_NOT);
    }

    protected void addSearchNotAllTags(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[][] notAllTagIdsArray = _assetEntryQuery.getNotAllTagIdsArray();

        if (notAllTagIdsArray.length == 0) {
            return;
        }

        BooleanQuery tagIdsArrayQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long[] notAllTagIds : notAllTagIdsArray) {
            if (notAllTagIds.length == 0) {
                continue;
            }

            BooleanQuery tagIdsQuery =
                BooleanQueryFactoryUtil.create(searchContext);

            for (long tagId : notAllTagIds) {
                tagIdsQuery.addTerm(Field.ASSET_TAG_IDS, tagId);
            }

            tagIdsArrayQuery.add(tagIdsQuery, BooleanClauseOccur.MUST);
        }

        contextQuery.add(tagIdsArrayQuery, BooleanClauseOccur.MUST_NOT);
    }

    protected void addSearchNotAnyCategories(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] notAnyCategoryIds = _assetEntryQuery.getNotAnyCategoryIds();

        if (notAnyCategoryIds.length == 0) {
            return;
        }

        BooleanQuery categoryIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long notAnyCategoryId : notAnyCategoryIds) {
            AssetCategory assetCategory =
                AssetCategoryLocalServiceUtil.fetchAssetCategory(notAnyCategoryId);

            if (assetCategory == null) {
                continue;
            }

            List<Long> categoryIds = new ArrayList<Long>();

            if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
                categoryIds.addAll(AssetCategoryLocalServiceUtil.getSubcategoryIds(notAnyCategoryId));
            }

            if (categoryIds.isEmpty()) {
                categoryIds.add(notAnyCategoryId);
            }

            for (long categoryId : categoryIds) {
                categoryIdsQuery.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
            }
        }

        contextQuery.add(categoryIdsQuery, BooleanClauseOccur.MUST_NOT);
    }

    protected void addSearchNotAnyTags(
        BooleanQuery contextQuery, SearchContext searchContext)
        throws Exception {

        long[] notAnyTagIds = _assetEntryQuery.getNotAnyTagIds();

        if (notAnyTagIds.length == 0) {
            return;
        }

        BooleanQuery tagIdsQuery =
            BooleanQueryFactoryUtil.create(searchContext);

        for (long tagId : _assetEntryQuery.getNotAnyTagIds()) {
            tagIdsQuery.addTerm(Field.ASSET_TAG_IDS, tagId);
        }

        contextQuery.add(tagIdsQuery, BooleanClauseOccur.MUST_NOT);
    }

    @Override
    protected void postProcessFullQuery(
        BooleanQuery fullQuery, SearchContext searchContext)
        throws Exception {

        fullQuery.addRequiredTerm("visible", true);
    }

    @Override
    protected void doDelete(Object obj)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected Document doGetDocument(Object obj)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected Summary doGetSummary(
        Document document, Locale locale, String snippet, PortletURL portletURL)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void doReindex(Object obj)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void doReindex(String className, long classPK)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void doReindex(String[] ids)
        throws Exception {

        throw new UnsupportedOperationException();
    }

    @Override
    protected String getPortletId(SearchContext searchContext) {

        return null;
    }

    // from com.liferay.portlet.asset.util.AssetUtil
    private static long[] filterCategoryIds(
        PermissionChecker permissionChecker, long[] categoryIds)
        throws PortalException, SystemException {

        List<Long> viewableCategoryIds = new ArrayList<Long>();

        for (long categoryId : categoryIds) {
            AssetCategory category =
                AssetCategoryLocalServiceUtil.fetchCategory(categoryId);

            if ((category != null) &&
                AssetCategoryPermission.contains(
                    permissionChecker, categoryId, ActionKeys.VIEW)) {

                viewableCategoryIds.add(categoryId);
            }
        }

        return ArrayUtil.toArray(viewableCategoryIds.toArray(new Long[viewableCategoryIds.size()]));
    }

    // from com.liferay.portlet.asset.util.AssetUtil
    private static long[] filterTagIds(
        PermissionChecker permissionChecker, long[] tagIds)
        throws PortalException, SystemException {

        List<Long> viewableTagIds = new ArrayList<Long>();

        for (long tagId : tagIds) {
            AssetTag tag = AssetTagLocalServiceUtil.fetchAssetTag(tagId);

            if ((tag != null) &&
                AssetTagPermission.contains(
                    permissionChecker, tagId, ActionKeys.VIEW)) {

                viewableTagIds.add(tagId);
            }
        }

        return ArrayUtil.toArray(viewableTagIds.toArray(new Long[viewableTagIds.size()]));
    }

    private AssetEntryQuery _assetEntryQuery;
    private PermissionChecker _permissionChecker;

}
