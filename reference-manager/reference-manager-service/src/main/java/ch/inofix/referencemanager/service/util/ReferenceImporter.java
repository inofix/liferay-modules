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
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.Value;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

import ch.inofix.referencemanager.exception.NoReferencesException;
import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.BibliographyServiceUtil;
import ch.inofix.referencemanager.service.ReferenceServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-17 17:07
 * @modified 2017-01-28 17:44
 * @version 1.1.1
 */
public class ReferenceImporter {

    public void importReferences(long userId, long groupId, boolean privateLayout, Map<String, String[]> parameterMap,
            File file, ServiceContext serviceContext) throws PortalException {

        User user = UserLocalServiceUtil.getUser(userId);

        // Import into the user's group
        Group group = user.getGroup();
        if (group != null) {
            groupId = group.getGroupId();
        }

        long bibliographyId = GetterUtil.getLong(ArrayUtil.getValue(parameterMap.get("bibliographyId"), 0));
        String description = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("description"), 0), null);
        String fileName = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("fileName"), 0), null);
        String title = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("title"), 0),
                LanguageUtil.get(serviceContext.getLocale(), "new-bibliography"));
        boolean updateExisting = GetterUtil.getBoolean(ArrayUtil.getValue(parameterMap.get("updateExisting"), 0));
        String urlTitle = GetterUtil.getString(ArrayUtil.getValue(parameterMap.get("urlTitle"), 0),
                LanguageUtil.get(serviceContext.getLocale(), "new-bibliography-url-title"));

        Bibliography bibliography = null;

        long[] bibliographyIds = new long[0];

        if (bibliographyId > 0) {
            bibliographyIds = new long[] { bibliographyId };
            bibliography = BibliographyServiceUtil.getBibliography(bibliographyId);
        }

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

            BibTeXDatabase database = bibTeXParser.parseFully(bufferedReader);

            List<Exception> exceptions = bibTeXParser.getExceptions();

            for (Exception exception : exceptions) {
                _log.error(exception.getMessage());
            }

            List<BibTeXObject> bibTeXObjects = database.getObjects();

            StringBuilder comments = new StringBuilder();
            List<BibTeXComment> bibTeXComments = BibTeXUtil.getBibTeXComments(database);

            for (BibTeXComment bibTeXComment : bibTeXComments) {

                Value value = bibTeXComment.getValue();
                String bibshareFilename = BibTeXUtil.getCommentValue("bibshare-filename", bibTeXComment);
                String bibshareId = BibTeXUtil.getCommentValue("bibshare-id", bibTeXComment);

                if (bibshareFilename == null && bibshareId == null) {

                    // Irrelevant for bibshare

                    if (value != null) {
                        comments.append(formatComment(value.toUserString()));
                    }
                }
            }

            // Update bibshare-metadata

            comments.append(formatComment("bibshare-filename: " + fileName));
            comments.append(formatComment("bibshare-id: " + String.valueOf(bibliographyId)));

            long bibshareId = GetterUtil.getLong(BibTeXUtil.getCommentValue("bibshare-id", bibTeXComments));

            boolean reImport = false;

            if (bibshareId > 0) {

                _log.info("bibshareId = " + bibshareId);

                if (bibliographyId == bibshareId) {

                    _log.info("Re-importing database.");
                    reImport = true;

                } else {

                    _log.info("Importing into another database.");

                }
            }

            // TODO: retrieve preamble from file
            String preamble = null;
            String strings = getStrings(bibTeXObjects);

            if (bibliography != null) {

                // import into an already existing bibliography
                title = bibliography.getTitle();
                description = bibliography.getDescription();
                urlTitle = bibliography.getUrlTitle();

                bibliography = BibliographyServiceUtil.updateBibliography(bibliographyId, userId, title, description,
                        urlTitle, comments.toString(), preamble, strings, serviceContext);
            }

            _log.info("Start import");

            Collection<BibTeXEntry> bibTeXEntries = database.getEntries().values();

            _log.info("bibTeXEntries.size() = " + bibTeXEntries.size());
            
            if (bibTeXEntries.size() == 0) {
                
                throw new NoReferencesException();
                
            }

            for (BibTeXEntry bibTeXEntry : bibTeXEntries) {

                String bibTeX = "";

                bibTeX = BibTeXUtil.format(bibTeXEntry);

                Reference reference = null;

                _log.info("updateExisting = " + updateExisting);

                Value value = bibTeXEntry.getField(new Key("bibshare-id"));

                if (value != null) {

                    // Imported bibTeXEntry contains a bibshare-id.

                    long referenceId = GetterUtil.getLong(value.toUserString());

                    if (referenceId > 0) {

                        try {

                            reference = ReferenceServiceUtil.getReference(referenceId);

                            // Consider the scope, since the user may upload
                            // a bibliography he has downloaded from another
                            // user.

                            if (groupId == reference.getGroupId()) {

                                // reference exists and belongs to the user

                                if (updateExisting) {

                                    reference = ReferenceServiceUtil.updateReference(referenceId, userId, bibTeX,
                                            serviceContext);

                                    numUpdated++;

                                } else {

                                    numIgnored++;

                                }
                            } else {

                                // reference exists, but belongs to another user

                                _log.info("adding reference");

                                numImported++;

                                reference = ReferenceServiceUtil.addReference(userId, bibTeX, bibliographyIds,
                                        serviceContext);

                            }

                        } catch (PortalException pe) {

                            _log.warn(pe.getMessage());

                            // invalid referenceId

                            _log.info("adding reference");

                            numImported++;

                            reference = ReferenceServiceUtil.addReference(userId, bibTeX, bibliographyIds,
                                    serviceContext);
                        }
                    } 
                    
                } else {

                    // no bibshare-id

                    _log.info("adding reference");

                    numImported++;

                    reference = ReferenceServiceUtil.addReference(userId, bibTeX, bibliographyIds, serviceContext);
                }

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

    private static String formatComment(String str) {

        StringBuilder sb = new StringBuilder(5);
        sb.append("@Comment{");
        sb.append(str);
        sb.append("}");
        sb.append(StringPool.NEW_LINE);
        sb.append(StringPool.NEW_LINE);

        return sb.toString();
    }

    private static String getStrings(List<BibTeXObject> bibTeXObjects) {

        StringBuilder sb = new StringBuilder();

        for (BibTeXObject bibTeXObject : bibTeXObjects) {

            if (bibTeXObject.getClass() == BibTeXString.class) {
                BibTeXString string = (BibTeXString) bibTeXObject;
                Key key = string.getKey();
                Value value = string.getValue();
                if (key != null && value != null) {
                    sb.append("@String{");
                    sb.append(key.getValue());
                    sb.append(StringPool.BLANK);
                    sb.append(StringPool.EQUAL);
                    sb.append(StringPool.BLANK);
                    sb.append(StringPool.OPEN_CURLY_BRACE);
                    sb.append(value.toUserString());
                    sb.append(StringPool.CLOSE_CURLY_BRACE);
                    sb.append("}");
                    sb.append(StringPool.NEW_LINE);
                    sb.append(StringPool.NEW_LINE);
                }
            }
        }

        return sb.toString();

    }

    private static Log _log = LogFactoryUtil.getLog(ReferenceImporter.class.getName());

}
