package ch.inofix.referencemanager.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-08 22:48
 * @modified 2017-02-11 18:46
 * @version 1.0.1
 *
 */
@Meta.OCD(id = "ch.inofix.referencemanager.web.configuration.BibliographyManagerConfiguration", localization = "content/Language", name = "bibliographymanager.configuration.name")
public interface BibliographyManagerConfiguration {

    @Meta.AD(deflt = "blue", required = false)
    public String favoriteColor();
//
//    @Meta.AD(deflt = "red|green|blue", required = false)
//    public String[] validLanguages();
//
//    @Meta.AD(required = false)
//    public int itemsPerPage();
    
    @Meta.AD(deflt = "referenceId|type|label|author|title|year|modified", required = false)
    public String[] columns();

}
