package ch.inofix.referencemanager.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-18 00:25
 * @modified 2016-11-18 00:25
 * @version 1.0.0
 *
 */
public class ReferenceDisplayTerms extends DisplayTerms {

    public static final String AUTHOR = "author";
    public static final String STATUS = "status";
    public static final String TITLE = "title";
    public static final String YEAR = "year";

    public ReferenceDisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);

        author = ParamUtil.getString(portletRequest, AUTHOR);

        String statusString = ParamUtil.getString(portletRequest, STATUS);

        if (Validator.isNotNull(statusString)) {
            status = GetterUtil.getInteger(statusString);
        }

        title = ParamUtil.getString(portletRequest, TITLE);
        year = ParamUtil.getString(portletRequest, YEAR);

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    protected String author;
    protected int status;
    protected String title;
    protected String year;

}
