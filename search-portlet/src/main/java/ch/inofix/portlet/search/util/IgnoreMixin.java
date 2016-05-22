
package ch.inofix.portlet.search.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;

/**
 * Configure the fields that should be ignored by the Jackson ObjectMapper
 * class.
 * 
 * @author Christian Berndt
 * @created 2016-05-20 22:08
 * @modified 2016-05-20 22:08
 * @version 1.0.0
 */
abstract class IgnoreMixin {

    // Disable Jackson Object Mapper on DLFileEntry.getFileVersion() since this
    // method has a circular dependency.

    @JsonIgnore
    abstract DLFileVersion getFileVersion();

}
