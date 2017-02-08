package ch.inofix.referencemanager.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-08 22:48
 * @modified 2017-02-08 22:48
 * @version 1.0.0
 *
 */
@Meta.OCD(
    id = "ch.inofix.referencemanager.web.configuration.BibliographyManagerPortletInstanceConfiguration",
    localization = "content/Language",
    name = "bibliographymanager.portlet.instance.configuration.name"
)
public interface BibliographyManagerPortletInstanceConfiguration {

    @Meta.AD(deflt = "blue", required = false)
    public String favoriteColor();

    @Meta.AD(deflt = "red|green|blue", required = false)
    public String[] validLanguages();

    @Meta.AD(required = false)
    public int itemsPerPage();

}
