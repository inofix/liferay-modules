package ch.inofix.referencemanager.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-13 21:43
 * @modified 2017-02-13 21:43
 * @version 1.0.0
 *
 */
@Meta.OCD(id = "ch.inofix.referencemanager.web.configuration.ReferenceManagerConfiguration", localization = "content/Language", name = "referencemanager.configuration.name")
public interface ReferenceManagerConfiguration {

    @Meta.AD(deflt = "referenceId|type|label|author|title|year|modified", required = false)
    public String[] columns();
}
