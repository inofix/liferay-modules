package ch.inofix.portlet.assetsearch.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.journal.model.JournalArticle;

@SuppressWarnings("serial")
public class LazySearchDataModel extends LazyDataModel<Document> {

    private static final Log _log = LogFactoryUtil
            .getLog(LazySearchDataModel.class);

    private List<Document> datasource;

    public LazySearchDataModel() {
    }

    public LazySearchDataModel(List<Document> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Document getRowData(String rowKey) {
        for (Document document : datasource) {

            if (document.getUID().equals(rowKey))
                return document;
        }

        return null;
    }

    @Override
    public Object getRowKey(Document document) {
        return document.getUID();
    }

    @Override
    public List<Document> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, String> filters) {

        _log.info("first = " + first);
        _log.info("pageSize = " + pageSize);

        List<Document> documents = new ArrayList<Document>();

        FacesContext facesContext = FacesContext.getCurrentInstance();

        PortletRequest portletRequest = (PortletRequest) facesContext
                .getExternalContext().getRequest();

        HttpServletRequest request = PortalUtil
                .getHttpServletRequest(portletRequest);

        // String[] entryClassNames = { JournalArticle.class.getName() };

        SearchContext searchContext = SearchContextFactory.getInstance(request);
        searchContext.setStart(first);
        searchContext.setEnd(first + pageSize);
        // searchContext.setEntryClassNames(entryClassNames);
        // searchContext.setScopeStrict(true);

        _log.info(searchContext);
        _log.info(searchContext.getCompanyId());

        for (long groupId : searchContext.getGroupIds()) {
            _log.info("groupId = " + groupId);
        }

        Indexer indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class
                .getName());

        // Indexer indexer = FacetedSearcher.getInstance();

        try {
            Hits hits = indexer.search(searchContext);
            documents = hits.toList();

            // rowCount
            this.setRowCount(hits.getLength());

        } catch (SearchException se) {
            _log.error(se);
        }

        return documents;

        // sort
        // if (sortField != null) {
        // Collections.sort(documents, new LazySorter(sortField, sortOrder));
        // }

        // paginate
        // if (getRowCount() > pageSize) {
        // try {
        // return documents.subList(first, first + pageSize);
        // } catch (IndexOutOfBoundsException e) {
        // return documents.subList(first, first
        // + (getRowCount() % pageSize));
        // }
        // } else {
        // return documents;
        // }
    }
}
