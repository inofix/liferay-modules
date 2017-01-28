package ch.inofix.referencemanager.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jbibtex.BibTeXComment;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * 
 * @author Christian Berndt
 * @created 2016-11-29 12:28
 * @modified 2017-01-28 16:21
 * @version 1.1.1
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
            sb.append(StringPool.OPEN_CURLY_BRACE);
            sb.append(fields.get(key).toUserString());
            sb.append(StringPool.CLOSE_CURLY_BRACE);
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

    public static List<BibTeXComment> getBibTeXComments(BibTeXDatabase database) {

        List<BibTeXComment> bibTeXComments = new ArrayList<BibTeXComment>();

        if (database != null) {

            List<BibTeXObject> bibTeXObjects = database.getObjects();

            for (BibTeXObject bibTeXObject : bibTeXObjects) {

                if (bibTeXObject.getClass() == BibTeXComment.class) {
                    bibTeXComments.add((BibTeXComment) bibTeXObject);
                }

            }

        }

        return bibTeXComments;

    }

    public static List<BibTeXComment> getBibTeXComments(String str) {

        BibTeXDatabase database = getBibTeXDatabase(str);

        return getBibTeXComments(database);

    }

    public static BibTeXDatabase getBibTeXDatabase(String str) {

        // Read bibTeXDatabase from str

        StringReader stringReader = new StringReader(str);

        BibTeXParser bibTeXParser;

        BibTeXDatabase database = null;

        try {
            bibTeXParser = new BibTeXParser();

            database = bibTeXParser.parseFully(stringReader);

        } catch (TokenMgrException e) {
            _log.error(e);
        } catch (ParseException e) {
            _log.error(e);
        } catch (Exception e) {
            _log.error(e);
        }

        return database;

    }

    public static BibTeXEntry getBibTeXEntry(String str) {

        // Read bibTeXEntry from str

        BibTeXEntry bibTexEntry = null;

        BibTeXDatabase database = getBibTeXDatabase(str);

        if (database != null) {

            Map<Key, BibTeXEntry> entriesMap = database.getEntries();

            if (entriesMap != null) {

                Collection<BibTeXEntry> bibTexEntries = entriesMap.values();

                if (bibTexEntries.size() > 0) {

                    Iterator<BibTeXEntry> iterator = bibTexEntries.iterator();

                    bibTexEntry = iterator.next();

                }
            }
        }

        return bibTexEntry;

    }

    public static String getCommentValue(String key, BibTeXComment bibTeXComment) {

        String str = null;

        Value value = bibTeXComment.getValue();

        if (value != null) {

            String val = value.toUserString();

            if (val.startsWith(key + StringPool.COLON)) {
                str = val.substring(val.indexOf(StringPool.COLON) + 1, val.length()).trim();
            }
        }

        return str;

    }

    public static String getCommentValue(String key, List<BibTeXComment> bibTeXComments) {

        String str = null;

        for (BibTeXComment bibTeXComment : bibTeXComments) {

            str = getCommentValue(key, bibTeXComment);

        }

        return str;
    }

    private static Log _log = LogFactoryUtil.getLog(BibTeXUtil.class.getName());

}
