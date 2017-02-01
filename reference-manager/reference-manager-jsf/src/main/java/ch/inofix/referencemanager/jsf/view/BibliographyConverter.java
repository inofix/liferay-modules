package ch.inofix.referencemanager.jsf.view;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-31 21:59
 * @modified 2017-01-31 21:59
 * @version 1.0.0
 *
 */
@ManagedBean(name = "bibliographyConverter")
public class BibliographyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        _log.info("value = " + value);

        long bibliographyId = GetterUtil.getLong(value);

        if (bibliographyId > 0) {

            Bibliography bibliography = null;

            try {
                bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
            } catch (PortalException e) {
                _log.error(e.getMessage());
            }

            return bibliography;

        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        _log.info("value = " + value);

        long bibliographyId = GetterUtil.getLong(value);

        if (bibliographyId > 0) {

            Bibliography bibliography = null;

            try {
                bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
            } catch (PortalException e) {
                _log.error(e.getMessage());
            }

            return String.valueOf(bibliography.getBibliographyId());

        } else {
            return null;
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyConverter.class);

}