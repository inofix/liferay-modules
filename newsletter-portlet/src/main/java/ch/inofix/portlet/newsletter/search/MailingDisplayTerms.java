package ch.inofix.portlet.newsletter.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 *
 * @author Christian Berndt
 * @created 2016-10-16 23:25
 * @modified 2016-10-16 23:25
 * @version 1.0.0
 *
 */
public class MailingDisplayTerms extends DisplayTerms {

    public static final String CREATE_DATE = "createDate";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String TITLE = "title";
    public static final String USER_NAME = "userName";

    public MailingDisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);

        createDate = ParamUtil.getString(portletRequest, CREATE_DATE);
        title = ParamUtil.getString(portletRequest, TITLE);
        modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
        userName = ParamUtil.getString(portletRequest, USER_NAME);
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected String createDate;
    protected String modifiedDate;
    protected String title;
    protected String userName;

}
