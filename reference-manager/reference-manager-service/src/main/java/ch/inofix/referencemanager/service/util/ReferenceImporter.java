package ch.inofix.referencemanager.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;
import org.jbibtex.BibTeXComment;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.service.BibliographyServiceUtil;
import ch.inofix.referencemanager.service.ReferenceServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-17 17:07
 * @modified 2016-12-17 17:07
 * @version 1.0.0
 */
public class ReferenceImporter {

    public void importReferences(long userId, long groupId, boolean privateLayout, Map<String, String[]> parameterMap,
            File file) throws PortalException {

        _log.info("importReferences()");

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        User user = UserLocalServiceUtil.getUser(userId);
        serviceContext.setCompanyId(user.getCompanyId());

        long bibliographyId = GetterUtil.getLong(ArrayUtil.getValue(parameterMap.get("bibliographyId"), 0));
        // TODO: read default value from resource bundle
        String title = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("title"), 0), "New Bibliography");
        // TODO: handle update of existing references / bibliographies
        boolean updateExisting = GetterUtil.getBoolean(ArrayUtil.getValue(parameterMap.get("updateExisting"), 0));
        // TODO: read default value from resource bundle
        String urlTitle = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("urlTitle"), 0), "new-bibliography");

        _log.info("bibliographyId = " + bibliographyId);
        _log.info("title = " + title);
        _log.info("updateExisting = " + updateExisting);
        _log.info("urlTitle = " + urlTitle);

        try {

            int numProcessed = 0;
            int numImported = 0;
            int numIgnored = 0;
            int numUpdated = 0;

            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            InputStream inputStream = new FileInputStream(file);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            BibTeXParser bibTeXParser = new BibTeXParser();

            BibTeXDatabase database = bibTeXParser.parse(bufferedReader);

            List<BibTeXObject> objects = database.getObjects();

            String uuid = null;

            for (BibTeXObject object : objects) {

                if (object.getClass() == BibTeXComment.class) {
                    BibTeXComment comment = (BibTeXComment) object;
                    _log.info(comment.getValue().toUserString());

                    // TODO: read uuid from comment
                }
            }

            Bibliography bibliography = null;

            // TODO
            if (uuid != null) {
                bibliography = BibliographyServiceUtil.getBibliography(uuid, groupId);
            } else if (bibliographyId > 0) {
                bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
            } else {
                bibliography = BibliographyServiceUtil.addBibliography(userId, title, null, urlTitle, serviceContext);
            }

            bibliographyId = bibliography.getBibliographyId();
            uuid = bibliography.getUuid();

            _log.info("Start import");

            Collection<BibTeXEntry> bibTeXEntries = database.getEntries().values();

            _log.info("bibTeXEntries.size() = " + bibTeXEntries.size());

            for (BibTeXEntry bibTeXEntry : bibTeXEntries) {

                String bibTeX = "";

                bibTeX = BibTeXUtil.format(bibTeXEntry);

                // TODO: check whether a reference with the same uid has
                // already been uploaded

                ReferenceServiceUtil.addReference(userId, bibTeX, new String[] { uuid }, serviceContext);

                if (numProcessed % 100 == 0 && numProcessed > 0) {

                    float completed = ((Integer) numProcessed).floatValue()
                            / ((Integer) bibTeXEntries.size()).floatValue() * 100;

                    _log.info("Processed " + numProcessed + " of " + bibTeXEntries.size() + " references in "
                            + stopWatch.getTime() + " ms (" + completed + "%).");
                }

                numProcessed++;

            }

            _log.info("Import took " + stopWatch.getTime() + " ms");
            _log.info("Processed " + numProcessed + " references.");
            _log.info("Imported " + numImported + " references.");
            _log.info("Ignored " + numIgnored + " references.");
            _log.info("Updated " + numUpdated + " references.");

        } catch (IOException ioe) {
            _log.error(ioe);
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static Log _log = LogFactoryUtil.getLog(ReferenceImporter.class.getName());

}
