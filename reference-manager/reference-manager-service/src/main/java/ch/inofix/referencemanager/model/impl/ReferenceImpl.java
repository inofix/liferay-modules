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
 * @modified 2016-12-24 16:16
 * @version 1.0.6
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
        
        String[] authors = getField("author").split("and"); 
        
        String author = ""; 
        
        if (authors.length == 1) {
            author = authors[0];
        } 
        if (authors.length == 2) {
            author = getField("author"); 
        }        
        if (authors.length > 2) {
            author = authors[0] + " et al.";
        }
        
        return author; 

    }

    public String[] getBibliographyUuids() throws PortalException {

        List<BibRefRelation> bibRefRelations = BibRefRelationLocalServiceUtil
                .getBibRefRelationsByReferenceUuid(getUuid());

        String[] bibliographyUuids = new String[bibRefRelations.size()];

        for (int i = 0; i < bibRefRelations.size(); i++) {

            BibRefRelation bibRefRelation = bibRefRelations.get(i);

            bibliographyUuids[i] = bibRefRelation.getBibliographyUuid();

        }

        return bibliographyUuids;

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
        
        String[] editors = getField("editor").split("and"); 
        
        String editor = ""; 
        
        if (editors.length == 1) {
            editor = editors[0];
        }
        if (editors.length == 2) {
            editor = getField("editor"); 
        }       
        if (editors.length > 2) {
            editor = editors[0] + " et al.";
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

    public String getTitle() {
        return getField("title");
    }

    public String getType() {

        String type = "misc";

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