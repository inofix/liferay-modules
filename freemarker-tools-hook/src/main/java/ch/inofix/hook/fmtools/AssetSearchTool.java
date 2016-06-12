
package ch.inofix.hook.fmtools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.inofix.hook.fmtools.search.AssetSearcher;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;

/**
 * @author Christian Berndt
 */
@Service
public class AssetSearchTool {

    // Enable logging for this class
    private static final Log _log =
        LogFactoryUtil.getLog(AssetSearchTool.class.getName());

    public List<AssetEntry> search(
        long companyId, long[] groupIds, long userId,
        PermissionChecker permissionChecker, String className, String userName,
        String title, String description, String assetCategoryIds,
        String assetTagNames, boolean anyTag, int status, boolean andSearch,
        int start, int end)
        throws PortalException, SystemException {

        Hits hits =
            doSearch(
                companyId, groupIds, userId, permissionChecker, className,
                userName, title, description, assetCategoryIds, assetTagNames,
                anyTag, status, andSearch, start, end);

        return getAssetEntries(hits);

    }

    private Hits doSearch(
        long companyId, long[] groupIds, long userId,
        PermissionChecker permissionChecker, String className, String userName,
        String title, String description, String assetCategoryIds,
        String assetTagNames, boolean anyTag, int status, boolean andSearch,
        int start, int end)
        throws PortalException, SystemException {

        try {

            if (userId == 0) {
                userId = UserLocalServiceUtil.getDefaultUserId(companyId);
            }

            Indexer searcher = AssetSearcher.getInstance();

            AssetSearcher assetSearcher = (AssetSearcher) searcher;

            AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

            assetEntryQuery.setClassNameIds(getClassNameIds(
                companyId, className));

            String[] assetTagNamesArray = StringUtil.split(assetTagNames);

            long[] tagIds =
                AssetTagLocalServiceUtil.getTagIds(groupIds, assetTagNamesArray);

            if (anyTag) {
                assetEntryQuery.setAnyTagIds(tagIds);
            }
            else {
                assetEntryQuery.setAllTagIds(tagIds);
            }

            // TODO: process categoryIds

            SearchContext searchContext = new SearchContext();

            searchContext.setAndSearch(andSearch);
            searchContext.setAttribute(Field.DESCRIPTION, description);
            searchContext.setAttribute(Field.TITLE, title);
            searchContext.setAttribute(Field.USER_NAME, userName);
            searchContext.setAttribute("paginationType", "regular");
            searchContext.setAttribute("status", status);
            searchContext.setCompanyId(companyId);
            searchContext.setUserId(userId);
            searchContext.setStart(start);
            searchContext.setEnd(end);
            searchContext.setGroupIds(groupIds);

            QueryConfig queryConfig = new QueryConfig();

            queryConfig.setHighlightEnabled(false);
            queryConfig.setScoreEnabled(false);

            searchContext.setQueryConfig(queryConfig);

            searchContext.setStart(start);

            assetSearcher.setPermissionChecker(permissionChecker);
            assetSearcher.setAssetEntryQuery(assetEntryQuery);

            return assetSearcher.search(searchContext);
        }

        catch (Exception e) {
            throw new SystemException(e);
        }

    }

    // From AssetUtil
    public static List<AssetEntry> getAssetEntries(Hits hits) {

        List<AssetEntry> assetEntries = new ArrayList<AssetEntry>();

        for (Document document : hits.getDocs()) {
            String className =
                GetterUtil.getString(document.get(Field.ENTRY_CLASS_NAME));
            long classPK =
                GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

            try {
                AssetEntry assetEntry =
                    AssetEntryLocalServiceUtil.getEntry(className, classPK);

                assetEntries.add(assetEntry);
            }
            catch (Exception e) {
            }
        }

        return assetEntries;
    }

    // From AssetEntryLocalServiceImpl
    private long[] getClassNameIds(long companyId, String className) {

        if (Validator.isNotNull(className)) {
            return new long[] {
                PortalUtil.getClassNameId(className)
            };
        }

        List<AssetRendererFactory> rendererFactories =
            AssetRendererFactoryRegistryUtil.getAssetRendererFactories(companyId);

        long[] classNameIds = new long[rendererFactories.size()];

        for (int i = 0; i < rendererFactories.size(); i++) {
            AssetRendererFactory rendererFactory = rendererFactories.get(i);

            classNameIds[i] =
                PortalUtil.getClassNameId(rendererFactory.getClassName());
        }

        return classNameIds;
    }

}
