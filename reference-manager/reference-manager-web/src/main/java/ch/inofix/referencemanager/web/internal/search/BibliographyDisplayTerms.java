package ch.inofix.referencemanager.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-30 18:20
 * @modified 2016-11-30 18:20
 * @version 1.0.0
 *
 */
public class BibliographyDisplayTerms extends DisplayTerms {

    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String TITLE = "title";

    public BibliographyDisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);

        description = ParamUtil.getString(portletRequest, DESCRIPTION);

        String statusString = ParamUtil.getString(portletRequest, STATUS);

        if (Validator.isNotNull(statusString)) {
            status = GetterUtil.getInteger(statusString);
        }

        title = ParamUtil.getString(portletRequest, TITLE);

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    protected String description;
    protected int status;
    protected String title;

}
