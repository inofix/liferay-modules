package ch.inofix.referencemanager.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-25 16:44
 * @modified 2016-12-26 16:44
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.entry.order:Integer=30" }, service = FormNavigatorEntry.class)
public class ReferenceRequiredFieldsFormNavigatorEntry extends BaseReferenceFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/reference/required_fields.jsp";
    }

    @Override
    public String getKey() {
        return "required-fields";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "required-fields");
    }

}
