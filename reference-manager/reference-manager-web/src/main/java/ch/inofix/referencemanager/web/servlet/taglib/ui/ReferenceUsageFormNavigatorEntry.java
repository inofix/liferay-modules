package ch.inofix.referencemanager.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-25 19:46
 * @modified 2016-12-25 19:46
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.entry.order:Integer=20" }, service = FormNavigatorEntry.class)
public class ReferenceUsageFormNavigatorEntry extends BaseReferenceFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/reference/usage.jsp";
    }

    @Override
    public String getKey() {
        return "usage";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "usage");
    }

}
