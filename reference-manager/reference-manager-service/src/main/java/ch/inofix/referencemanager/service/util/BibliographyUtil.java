package ch.inofix.referencemanager.service.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.util.GetterUtil;

import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-30 23:33
 * @modified 2017-01-30 23:33
 * @version 1.0.0
 *
 */
public class BibliographyUtil {

    public static List<Bibliography> documentsToBibliographies(List<Document> documents) {

        List<Bibliography> bibliographies = new ArrayList<Bibliography>();

        for (Document document : documents) {
            try {
                long bibliographyId = GetterUtil.getLong(document.get("entryClassPK"));
                Bibliography bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
                bibliographies.add(bibliography);
            } catch (Exception e) {
                _log.error(e);
            }
        }

        return bibliographies;
    }

    private static final Log _log = LogFactoryUtil.getLog(BibliographyUtil.class);

}
