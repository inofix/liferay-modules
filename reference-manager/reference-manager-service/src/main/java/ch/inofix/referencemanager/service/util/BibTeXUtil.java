package ch.inofix.referencemanager.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-29 12:28
 * @modified 2016-12-29 14:37
 * @version 1.0.4
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

        Iterator<Key> iterator = keys.iterator();

        while (iterator.hasNext()) {

            Key key = iterator.next();

            sb.append(StringPool.BLANK);
            sb.append(StringPool.BLANK);
            sb.append(key);
            sb.append(" = ");
            sb.append("\"");
            sb.append(fields.get(key).toUserString());
            sb.append("\"");
            if (iterator.hasNext()) {
                sb.append(",");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("}");
        sb.append(System.lineSeparator());

        return sb.toString();

    }

    // BibTeX entry types as defined in
    // http://bibtexml.sourceforge.net/btxdoc.pdf
    public static final String[] ENTRY_TYPES = new String[] { "article", "book", "booklet", "conference", "inbook",
            "incollection", "inproceedings", "manual", "masterthesis", "misc", "phdthesis", "proceedings", "techreport",
            "unpublished" };

    public static String getProperty(String key) {

        ClassLoader classLoader = BibTeXUtil.class.getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream("portlet.properties");

        Properties _properties = new Properties();

        try {
            _properties.load(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return _properties.getProperty(key);
    }

    private static Log _log = LogFactoryUtil.getLog(BibTeXUtil.class.getName());

}
