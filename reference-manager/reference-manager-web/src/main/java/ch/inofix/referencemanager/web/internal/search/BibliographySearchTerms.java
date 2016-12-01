package ch.inofix.referencemanager.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-30 18:22
 * @modified 2016-11-30 18:22
 * @version 1.0.0
 *
 */
public class BibliographySearchTerms extends BibliographyDisplayTerms {

    public BibliographySearchTerms(PortletRequest portletRequest) {
        super(portletRequest);

        description = DAOParamUtil.getString(portletRequest, DESCRIPTION);
        status = ParamUtil.getInteger(portletRequest, STATUS, WorkflowConstants.STATUS_APPROVED);
        title = DAOParamUtil.getString(portletRequest, TITLE);

    }
}
