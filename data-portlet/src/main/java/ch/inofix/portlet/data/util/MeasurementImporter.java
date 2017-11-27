package ch.inofix.portlet.data.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;

import ch.inofix.portlet.data.FileFormatException;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 17:53
 * @modified 2017-11-27 18:03
 * @version 1.0.9
 *
 */
public class MeasurementImporter {

    public void importMeasurements(long userId, long groupId,
            Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        String idField = MapUtil.getString(parameterMap, "idField");
        String nameField = MapUtil.getString(parameterMap, "nameField");
        String timestampField = MapUtil.getString(parameterMap,
                "timestampField");

        _log.info("idField = " + idField);
        _log.info("nameField = " + nameField);
        _log.info("timestampField = " + timestampField);

        // Setup serviceContext

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        int numProcessed = 0;
        int numImported = 0;
        int numIgnored = 0;

        User user = UserLocalServiceUtil.getUser(userId);
        serviceContext.setCompanyId(user.getCompanyId());

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        String extension = FileUtil.getExtension(file.getName());

        // import from xml

        if ("xml".equalsIgnoreCase(extension)) {

            _log.info("process xml");

            try {
                Document document = SAXReaderUtil.read(file);

                // TODO: read xPath selector from configuration
                String selector = "//ChannelData";
                List<Node> channels = document.selectNodes(selector);
                int numValues = document.selectNodes("//VT").size();

                for (Node channel : channels) {

                    Element channelElement = (Element) channel;

                    String id = channelElement.attributeValue(idField);
                    String name = channelElement.attributeValue(nameField);
                    String unit = channelElement.attributeValue("unit");
                    
                    List<Node> values = channel.selectNodes("descendant::VT");

                    for (Node node : values) {

                        Element valueElement = (Element) node;
                        String timestampStr = valueElement.attributeValue(timestampField);
                        Date timestamp = getDate(timestampStr);
                        String value = valueElement.getText();
                        
                        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(); 
                        jsonObject.put(DataManagerFields.ID, id);
                        jsonObject.put(DataManagerFields.NAME, name);
                        jsonObject.put(DataManagerFields.TIMESTAMP, timestampStr);
                        jsonObject.put(DataManagerFields.UNIT, unit);
                        jsonObject.put(DataManagerFields.VALUE, value);

                        if (Validator.isNotNull(value)) {

                            int status = addMeasurement(serviceContext, userId,
                                    jsonObject, id, name, timestamp, unit, value);

                            if (status == IGNORED) {
                                numIgnored++;
                            }

                            if (status == IMPORTED) {
                                numImported++;
                            }

                            if (numProcessed % 100 == 0 && numProcessed > 0) {

                                float completed = ((Integer) numProcessed)
                                        .floatValue() / numValues * 100;

                                _log.info("Processed " + numProcessed + " of "
                                        + numValues + " measurements in "
                                        + stopWatch.getTime() + " ms ("
                                        + completed + "%).");
                            }
                        }

                        numProcessed++;

                    }
                }

            } catch (DocumentException de) {
                _log.error(de);
            }

        } else if ("json".equalsIgnoreCase(extension)) {

            _log.info("process json");

            try {
                String json = FileUtils.readFileToString(file,
                        Charset.defaultCharset());

                if (Validator.isNotNull(json)) {

                    // Remove start and end quotes added by python's dump method

                    if (json.startsWith("'") && json.endsWith("'")) {

                        json = json.substring(1, json.length() - 1);

                    }

                    JSONArray jsonArray = JSONFactoryUtil.createJSONArray(json);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject inObject = jsonArray.getJSONObject(i);

                        String id = inObject.getString(idField);
                        String name = inObject.getString(nameField);
                        Date timestamp = getDate(inObject.getString(timestampField));
                        String unit = inObject.getString(DataManagerFields.UNIT);
                        String value = inObject.getString(DataManagerFields.VALUE);

                        if (Validator.isNotNull(value)) {

                            int status = addMeasurement(serviceContext, userId,
                                    inObject, id, name, timestamp, unit, value);

                            if (status == IGNORED) {
                                numIgnored++;
                            }

                            if (status == IMPORTED) {
                                numImported++;
                            }
                        } else {
                            numIgnored++;
                        }

                        numProcessed++;

                    }
                }

            } catch (IOException e) {
                _log.error(e.getMessage());
            }

        } else {

            throw new FileFormatException();

        }

        _log.info("Import took " + stopWatch.getTime() + " ms");
        _log.info("Processed " + numProcessed + " measurements.");
        _log.info("Imported " + numImported + " measurements.");
        _log.info("Ignored " + numIgnored + " measurements.");

    }

    private int addMeasurement(ServiceContext serviceContext, long userId,
            JSONObject data, String id, String name, Date timestamp,
            String unit, String value) throws SystemException, PortalException {

        Hits hits = MeasurementLocalServiceUtil.search(
                serviceContext.getCompanyId(),
                serviceContext.getScopeGroupId(), id, timestamp, true, 0, 1,
                null);

        _log.info("hits.getLength() = " + hits.getLength());

        if (hits.getLength() == 0) {

            MeasurementLocalServiceUtil.addMeasurement(userId, data.toString(),
                    id, name, timestamp, unit, value, serviceContext);

            return IMPORTED;

        } else {

            return IGNORED;
        }
    }
    
    private Date getDate(String str) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date date = null;

        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            _log.error(e.getMessage());
        }

        return date;
    }

    private static int IGNORED = 0;
    private static int IMPORTED = 1;

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
