package ch.inofix.referencemanager.web.internal.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;

import ch.inofix.referencemanager.service.BibliographyService;

/**
 * Base class for the different bibliographies related portlets
 * (PopularBibliographiesPortlet, YourBibliographiesPortlet).
 * 
 * @author Christian Berndt
 * @created 2016-12-01 02:02
 * @modified 2016-12-15 13:10
 * @version 1.0.1
 */
public class BibliographiesPortlet extends MVCPortlet {

    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @since 1.0.0
     * @throws Exception
     */
    public void deleteBibliography(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        _log.info("deleteBibliography()");

        long bibliographyId = ParamUtil.getLong(actionRequest, "bibliographyId");

        _bibliographyService.deleteBibliography(bibliographyId);
    }

    @org.osgi.service.component.annotations.Reference
    protected void setBibliographyService(BibliographyService bibliographyService) {
        this._bibliographyService = bibliographyService;
    }

    private BibliographyService _bibliographyService;

    private static final String REQUEST_PROCESSED = "request_processed";

    private static Log _log = LogFactoryUtil.getLog(YourBibliographiesPortlet.class.getName());

}
