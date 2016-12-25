package ch.inofix.referencemanager.web.servlet.taglib.ui;

import com.liferay.portal.kernel.servlet.taglib.ui.BaseJSPFormNavigatorEntry;

import ch.inofix.referencemanager.model.Reference;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-25 17:15
 * @modified 2016-12-25 17:15
 * @version 1.0.0
 *
 */
public abstract class BaseReferenceFormNavigatorEntry extends BaseJSPFormNavigatorEntry<Reference> {
    

    @Override
    public String getCategoryKey() {
        return "default"; 
    }

    @Override
    public String getFormNavigatorId() {

        return "reference.form";
    }

}
