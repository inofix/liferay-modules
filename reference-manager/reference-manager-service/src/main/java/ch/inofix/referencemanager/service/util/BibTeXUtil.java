package ch.inofix.referencemanager.service.util;

import java.util.Map;
import java.util.Set;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-29 12:28
 * @modified 2016-11-29 12:28
 * @version 1.0.0
 *
 */
public class BibTeXUtil {

    public static final String format(BibTeXEntry bibTeXEntry) {

        Map<Key, Value> fields = bibTeXEntry.getFields();

        Set<Key> keys = fields.keySet();

        StringBuilder sb = new StringBuilder();

        sb.append("@");
        sb.append(bibTeXEntry.getType());
        sb.append("{");
        sb.append(bibTeXEntry.getKey());
        sb.append(",");
        sb.append(System.lineSeparator());
        for (Key key : keys) {
            sb.append(key);
            sb.append(" = ");
            sb.append("\"");
            sb.append(fields.get(key).toUserString());
            sb.append("\"");
            sb.append(",");
            sb.append(System.lineSeparator());
        }
        sb.append("}");
        sb.append(System.lineSeparator());

        return sb.toString();

    }

    private static Log _log = LogFactoryUtil.getLog(BibTeXUtil.class.getName());

}
