package ch.inofix.referencemanager.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
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
 * @modified 2016-12-179 23:53
 * @version 1.0.2
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
    
    // BibTeX entry types as defined in
    // http://bibtexml.sourceforge.net/btxdoc.pdf
    
    public static final String TYPE_ARTICLE = get("entry.type.article"); 
    public static final String TYPE_BOOK = get("entry.type.book"); 
    public static final String TYPE_BOOKLET = get("entry.type.booklet"); 
    public static final String TYPE_CONFERENCE = get("entry.type.conference");
    public static final String TYPE_INBOOK = get("entry.type.inbook");
    public static final String TYPE_INCOLLECTION = get("entry.type.incollection");
    public static final String TYPE_INPROCEEDINGS = TYPE_CONFERENCE;
    public static final String TYPE_MANUAL = get("entry.type.manual"); 
    public static final String TYPE_MASTERTHESIS = get("entry.type.masterthesis"); 
    public static final String TYPE_MISC = get("entry.type.misc"); 
    public static final String TYPE_PHDTHESIS = get("entry.type.phdthesis"); 
    
    private static String get(String key) {
        
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
