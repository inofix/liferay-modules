package ch.inofix.portlet.newsletter.search;

import java.util.Locale;

import javax.portlet.PortletURL;

import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;

public class NewsletterIndexer extends BaseIndexer {

    @Override
    public String[] getClassNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPortletId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doDelete(Object arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected Document doGetDocument(Object arg0) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Summary doGetSummary(Document arg0, Locale arg1, String arg2,
            PortletURL arg3) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doReindex(Object arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doReindex(String[] arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doReindex(String arg0, long arg1) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getPortletId(SearchContext arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
