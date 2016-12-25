package ch.inofix.referencemanager.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-25 20:09
 * @modified 2016-12-25 20:09
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.entry.order:Integer=30" }, service = FormNavigatorEntry.class)
public class ReferenceBibTeXFormNavigatorEntry extends BaseReferenceFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/reference/bibtex.jsp";
    }

    @Override
    public String getKey() {
        return "bibtex";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "bibtex");
    }

}
