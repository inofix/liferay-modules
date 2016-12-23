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
 * @modified 2016-12-23 14:54
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
    public static final String[] ENTRY_TYPES = new String[] { "article", "book", "booklet", "conference", "inbook",
            "incollection", "inproceedings", "manual", "masterthesis", "misc", "phdthesis", "proceedings", "techreport",
            "unpublished" };
    
//    public static final String TYPE_ARTICLE = getProperty("entry.type.article");
//    public static final String TYPE_BOOK = getProperty("entry.type.book");
//    public static final String TYPE_BOOKLET =getProperty("entry.type.booklet");
//    public static final String TYPE_CONFERENCE = getProperty("entry.type.conference");
//    public static final String TYPE_INBOOK = getProperty("entry.type.inbook");
//    public static final String TYPE_INCOLLECTION = getProperty("entry.type.incollection");
//    public static final String TYPE_INPROCEEDINGS = TYPE_CONFERENCE;
//    public static final String TYPE_MANUAL = getProperty("entry.type.manual");
//    public static final String TYPE_MASTERTHESIS = getProperty("entry.type.masterthesis");
//    public static final String TYPE_MISC = getProperty("entry.type.misc");
//    public static final String TYPE_PHDTHESIS = getProperty("entry.type.phdthesis");
//    public static final String TYPE_PROCEEDINGS = getProperty("entry.type.proceedings");
//    public static final String TYPE_TECHREPORT = getProperty("entry.type.techreport");
//    public static final String TYPE_UNPUBLISHED = getProperty("entry.type.unpublished");
    
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
