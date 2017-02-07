/**
 * Copyright (c) 2016-present Inofix GmbH, Luzern. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.inofix.referencemanager.model.impl;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.model.BibRefRelation;
import ch.inofix.referencemanager.service.BibRefRelationLocalServiceUtil;

/**
 * The extended model implementation for the Reference service. Represents a row
 * in the &quot;Reference&quot; database table, with each column mapped to a
 * property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.referencemanager.model.Reference} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @created 2016-03-29 14:43
 * @modified 2017-02-07 18:38
 * @version 1.1.0
 */
@SuppressWarnings("serial")
@ProviderType
public class ReferenceImpl extends ReferenceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. All methods that expect a reference
     * model instance should use the {@link
     * ch.inofix.referencemanager.model.Reference} interface instead.
     */
    public ReferenceImpl() {
    }

    public String getAuthor() {

        String author = null;

        String str = getField("author");

        if (str != null) {
            author = getNameNormalized(str);
        } else {
            return getEditor();
        }

        return author;
    }

    public long[] getBibliographyIds() throws PortalException {

        List<BibRefRelation> bibRefRelations = BibRefRelationLocalServiceUtil
                .getBibRefRelationsByReferenceId(getReferenceId());

        long[] bibliographyIds = new long[bibRefRelations.size()];

        for (int i = 0; i < bibRefRelations.size(); i++) {
            BibRefRelation bibRefRelation = bibRefRelations.get(i);
            bibliographyIds[i] = bibRefRelation.getBibliographyId();
        }

        return bibliographyIds;

    }

    public String getCitation() {

        StringBuilder sb = new StringBuilder();

        sb.append(getAuthor());
        if (Validator.isNotNull(sb.toString())) {
            sb.append(StringPool.COMMA_AND_SPACE);
        }
        sb.append(getTitle());
        if (Validator.isNotNull(sb.toString())) {
            sb.append(StringPool.COMMA_AND_SPACE);
        }
        sb.append(getYear());

        return sb.toString();

    }

    public String getEditor() {

        String editor = null;

        String str = getField("editor");

        if (str != null) {
            editor = getNameNormalized(str);
            // TODO: consider language
            // TODO: consider multiple editors (either "and" or "et.al.").
            editor = editor + " (ed.)";
        }

        return editor;
    }

    public Map<String, String> getFields() {

        Map<String, String> fields = new HashMap<String, String>();

        if (_bibTeXEntry == null) {
            _bibTeXEntry = getBibTeXEntry();
        }

        Map<Key, Value> entryFields = new HashMap<Key, Value>();

        if (_bibTeXEntry != null) {
            entryFields = _bibTeXEntry.getFields();
        }

        Set<Key> keys = entryFields.keySet();

        for (Key key : keys) {
            fields.put(key.getValue(), _bibTeXEntry.getField(key).toUserString());
        }

        return fields;

    }

    public String getLabel() {

        String label = "";

        if (_bibTeXEntry == null) {
            _bibTeXEntry = getBibTeXEntry();
        }

        if (_bibTeXEntry != null) {
            if (_bibTeXEntry.getKey() != null) {
                label = _bibTeXEntry.getKey().getValue();
            }
        }

        return label;

    }

    public String getTitle() {
        return getField("title");
    }

    public String getType() {

        String type = "article";

        if (_bibTeXEntry == null) {
            _bibTeXEntry = getBibTeXEntry();
        }

        if (_bibTeXEntry != null) {
            if (_bibTeXEntry.getType() != null) {
                type = _bibTeXEntry.getType().getValue();
            }
        }

        return type;

    }

    public String getYear() {
        return getField("year");
    }

    public boolean isApproved() {
        if (getStatus() == WorkflowConstants.STATUS_APPROVED) {
            return true;
        } else {
            return false;
        }
    }

    public String getField(String field) {

        String str = "";

        Key key = new Key(field);

        if (_bibTeXEntry == null) {
            _bibTeXEntry = getBibTeXEntry();
        }

        if (_bibTeXEntry != null) {
            Value value = _bibTeXEntry.getField(key);
            if (value != null) {
                str = value.toUserString();
            }
        }

        return str;

    }

    private String getNameFormatted(String[] nameParts) {

        // names[0] = first;
        // names[1] = von;
        // names[2] = last;
        // names[3] = jr;

        StringBuilder sb = new StringBuilder();
        
        if (Validator.isNotNull(nameParts[2])) {           
            sb.append(nameParts[2]);
        }
        
        if (Validator.isNotNull(nameParts[0])) {
            sb.append(StringPool.COMMA);
            sb.append(StringPool.SPACE);
            sb.append(nameParts[0].substring(0, 1));
            sb.append(StringPool.PERIOD);
            
            if (Validator.isNotNull(nameParts[1])) {
                sb.append(StringPool.SPACE);
                sb.append(nameParts[1]);
            }
        }


        if (Validator.isNotNull(nameParts[3])) {
            // TODO: how do we handle the jr part?
            // sb.append(nameParts[3]);
        }

        return sb.toString();

    }

    private String getNameNormalized(String str) {

        String name = null;
        String[] names = str.split(" (?i)and ");

        if (names.length == 1) {
            String[] nameParts = getNameParts(names[0]);
            name = getNameFormatted(nameParts);
        } else if (names.length == 2) {
            String[] nameParts1 = getNameParts(names[0]);
            String[] nameParts2 = getNameParts(names[1]);
            // TODO: consider configured language
            name = getNameFormatted(nameParts1) + " and " + getNameFormatted(nameParts2);
        } else {
            String[] nameParts = getNameParts(names[0]);
            // TODO: consider configured et al.
            name = getNameFormatted(nameParts) + " et al.";
        }

        return name;
    }

    private String[] getNameParts(String name) {

        // see:
        // http://maverick.inria.fr/~Xavier.Decoret/resources/xdkbibtex/bibtex_summary.html

        String last = null;
        String first = null;
        String von = null;
        String jr = null;

        String[] tokens = name.split(StringPool.COMMA);

        if (tokens.length == 1) {

            // no comma
            String[] parts = tokens[0].split(" ");

            if (parts.length == 1) {
                last = parts[parts.length - 1];
            } else if (parts.length == 2) {
                last = parts[parts.length - 1];
                first = parts[0];
            } else {
                // TODO: implement cases mentioned above
                last = parts[parts.length - 1];
                first = parts[0];
            }

        } else if (tokens.length == 2) {

            // one comma

            last = tokens[0];
            first = tokens[1];

            // TODO: implement cases mentioned above

        } else if (tokens.length == 3) {
            // TODO: two commas
        } else {
            // TODO: invalid
        }

        String[] parts = new String[4];

        parts[0] = first;
        parts[1] = von;
        parts[2] = last;
        parts[3] = jr;

        return parts;

    }

    private BibTeXEntry getBibTeXEntry() {

        try {

            StringReader bibTeXReader = new StringReader(getBibTeX());
            BibTeXParser bibtexParser = new BibTeXParser();
            BibTeXDatabase bibTeXDatabase = bibtexParser.parse(bibTeXReader);
            Collection<BibTeXEntry> bibTeXEntries = bibTeXDatabase.getEntries().values();

            Iterator<BibTeXEntry> iterator = bibTeXEntries.iterator();
            while (iterator.hasNext()) {

                _bibTeXEntry = iterator.next();
            }

        } catch (Exception e) {
            _log.error(e.getMessage());
        }

        return _bibTeXEntry;
    }

    private BibTeXEntry _bibTeXEntry = null;

    private static Log _log = LogFactoryUtil.getLog(ReferenceImpl.class.getName());

}