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
import java.util.Iterator;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;

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
 * @modified 2016-11-20 16:34
 * @version 0.3.1
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
        return getValue("author");
    }

    public String getCitation() {

        StringBuilder sb = new StringBuilder();

        sb.append(getAuthor());
        sb.append(StringPool.COMMA_AND_SPACE);
        sb.append(getTitle());
        sb.append(StringPool.COMMA_AND_SPACE);
        sb.append(getYear());

        return sb.toString();

    }

    public String getTitle() {
        return getValue("title");
    }

    public String getYear() {
        return getValue("year");
    }

    public boolean isApproved() {
        if (getStatus() == WorkflowConstants.STATUS_APPROVED) {
            return true;
        } else {
            return false;
        }
    }

    private String getValue(String field) {

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