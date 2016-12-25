package ch.inofix.referencemanager.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorCategory;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-25 17:00
 * @modified 2016-12-25 17:00
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.category.order:Integer=20" }, service = FormNavigatorCategory.class)
public class ReferenceFormNavigatorCategory implements FormNavigatorCategory {

    @Override
    public String getFormNavigatorId() {
        return "reference.form";
    }

    @Override
    public String getKey() {
        return "default";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "default");
    }

}
