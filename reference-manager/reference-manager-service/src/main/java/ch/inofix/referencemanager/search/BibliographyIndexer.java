package ch.inofix.referencemanager.search;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyLocalService;
import ch.inofix.referencemanager.service.permission.BibliographyPermission;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-30 18:09
 * @modified 2016-12-15 11:49
 * @version 1.0.1
 *
 */
@Component(immediate = true, service = Indexer.class)
public class BibliographyIndexer extends BaseIndexer<Bibliography> {

    public static final String CLASS_NAME = Bibliography.class.getName();

    public BibliographyIndexer() {
        setDefaultSelectedFieldNames(Field.ASSET_TAG_NAMES, Field.COMPANY_ID, Field.ENTRY_CLASS_NAME,
                Field.ENTRY_CLASS_PK, Field.GROUP_ID, Field.MODIFIED_DATE, Field.SCOPE_GROUP_ID, Field.TITLE, Field.UID,
                Field.URL);
        setFilterSearch(true);
        setPermissionAware(true);
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker, String entryClassName, long entryClassPK,
            String actionId) throws Exception {
        return BibliographyPermission.contains(permissionChecker, entryClassPK, ActionKeys.VIEW);
    }

    @Override
    public void postProcessFullQuery(BooleanQuery fullQuery, SearchContext searchContext) throws Exception {

        if (searchContext.getOwnerUserId() > 0) {
            BooleanQuery booleanQuery = new BooleanQueryImpl();
            booleanQuery.addExactTerm(Field.USER_ID, searchContext.getOwnerUserId());
            fullQuery.add(booleanQuery, BooleanClauseOccur.MUST);

        }
    }

    @Override
    protected void doDelete(Bibliography bibliography) throws Exception {

        deleteDocument(bibliography.getCompanyId(), bibliography.getBibliographyId());
    }

    @Override
    protected Document doGetDocument(Bibliography bibliography) throws Exception {

        Document document = getBaseModelDocument(CLASS_NAME, bibliography);
        document.addText(Field.CONTENT, bibliography.getDescription());
        document.addTextSortable(Field.TITLE, bibliography.getTitle());

        return document;

    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale, String snippet, PortletRequest portletRequest,
            PortletResponse portletResponse) throws Exception {

        Summary summary = createSummary(document, Field.TITLE, Field.URL);

        return summary;
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {

        Bibliography bibliography = _bibliographyLocalService.getBibliography(classPK);

        doReindex(bibliography);
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {

        long companyId = GetterUtil.getLong(ids[0]);

        // TODO: what about the group?
        reindexBibliographys(companyId);
        // reindexBibliographys(companyId, groupId);

    }

    @Override
    protected void doReindex(Bibliography bibliography) throws Exception {

        Document document = getDocument(bibliography);

        IndexWriterHelperUtil.updateDocument(getSearchEngineId(), bibliography.getCompanyId(), document,
                isCommitImmediately());
    }

    protected void reindexBibliographys(long companyId) throws PortalException {

        final IndexableActionableDynamicQuery indexableActionableDynamicQuery = _bibliographyLocalService
                .getIndexableActionableDynamicQuery();

        indexableActionableDynamicQuery.setAddCriteriaMethod(new ActionableDynamicQuery.AddCriteriaMethod() {

            @Override
            public void addCriteria(DynamicQuery dynamicQuery) {

                Property statusProperty = PropertyFactoryUtil.forName("status");

                Integer[] statuses = { WorkflowConstants.STATUS_APPROVED, WorkflowConstants.STATUS_IN_TRASH };

                dynamicQuery.add(statusProperty.in(statuses));
            }

        });
        indexableActionableDynamicQuery.setCompanyId(companyId);
        // TODO: what about the group?
        // indexableActionableDynamicQuery.setGroupId(groupId);
        indexableActionableDynamicQuery
                .setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod<Bibliography>() {

                    @Override
                    public void performAction(Bibliography bibliography) {
                        try {
                            Document document = getDocument(bibliography);

                            indexableActionableDynamicQuery.addDocuments(document);
                        } catch (PortalException pe) {
                            if (_log.isWarnEnabled()) {
                                _log.warn("Unable to index bibliography " + bibliography.getBibliographyId(), pe);
                            }
                        }
                    }

                });
        indexableActionableDynamicQuery.setSearchEngineId(getSearchEngineId());

        indexableActionableDynamicQuery.performActions();
    }

    @Reference(unbind = "-")
    protected void setBibliographyLocalService(BibliographyLocalService bibliographyLocalService) {

        _bibliographyLocalService = bibliographyLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyIndexer.class);

    private BibliographyLocalService _bibliographyLocalService;

}
