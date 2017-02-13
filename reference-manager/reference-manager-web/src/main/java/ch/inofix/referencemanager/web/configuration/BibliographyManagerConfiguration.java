package ch.inofix.referencemanager.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-08 22:48
 * @modified 2017-02-13 22:49
 * @version 1.0.2
 *
 */
@Meta.OCD(id = "ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration", localization = "content/Language", name = "bibliographymanager.configuration.name")
public interface BibliographyManagerConfiguration extends ReferenceManagerConfiguration {

    @Meta.AD(deflt = "blue", required = false)
    public String favoriteColor();

}
