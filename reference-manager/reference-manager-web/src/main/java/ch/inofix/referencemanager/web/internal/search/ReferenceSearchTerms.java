package ch.inofix.referencemanager.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-18 00:55
 * @modified 2016-11-18 00:55
 * @version 1.0.0
 *
 */
public class ReferenceSearchTerms extends ReferenceDisplayTerms {

    public ReferenceSearchTerms(PortletRequest portletRequest) {
        super(portletRequest);

        author = DAOParamUtil.getString(portletRequest, AUTHOR);
        status = ParamUtil.getInteger(portletRequest, STATUS, WorkflowConstants.STATUS_APPROVED);
        title = DAOParamUtil.getString(portletRequest, TITLE);
        year = DAOParamUtil.getString(portletRequest, YEAR);

    }

}
