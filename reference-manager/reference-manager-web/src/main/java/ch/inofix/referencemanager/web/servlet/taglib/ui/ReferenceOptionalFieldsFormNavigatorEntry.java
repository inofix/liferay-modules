package ch.inofix.referencemanager.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

@Component(property = { "form.navigator.entry.order:Integer=30" }, service = FormNavigatorEntry.class)
public class ReferenceOptionalFieldsFormNavigatorEntry extends BaseReferenceFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/reference/optional_fields.jsp";
    }

    @Override
    public String getKey() {
        return "optional-fields";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "optional-fields");
    }

}
