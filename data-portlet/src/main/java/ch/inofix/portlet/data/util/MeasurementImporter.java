package ch.inofix.portlet.data.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import ch.inofix.portlet.data.FileFormatException;
import ch.inofix.portlet.data.MeasurementXLSException;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 17:53
 * @modified 2017-11-15 15:09
 * @version 1.0.8
 *
 */
public class MeasurementImporter {

    public void importMeasurements(long userId, long groupId,
            boolean privateLayout, Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

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

                    String id = channelElement.attributeValue("channelId");
                    String name = channelElement.attributeValue("name");
                    String unit = channelElement.attributeValue("unit");

                    List<Node> values = channel.selectNodes("descendant::VT");

                    for (Node value : values) {

                        Element valueElement = (Element) value;
                        String timestamp = valueElement.attributeValue("t");
                        String val = valueElement.getText();

                        JSONObject jsonObject = createJSONObject(id, name,
                                unit, timestamp, val);

                        int status = addMeasurement(serviceContext, userId,
                                jsonObject);

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
                                    + stopWatch.getTime() + " ms (" + completed
                                    + "%).");
                        }

                        numProcessed++;

                    }
                }

            } catch (DocumentException de) {
                _log.error(de);
            }

        } else if ("xls".equalsIgnoreCase(extension)) {

            // import from xls

            _log.info("process xls");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            try {

                FileInputStream fileInputStream = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
                HSSFSheet worksheet = workbook.getSheetAt(0);

                int i = 0;

                for (Row row : worksheet) {

                    if (i > 0) {

                        String id = getCellValue(row.getCell(3), true);
                        String name = getCellValue(row.getCell(1));
                        String unit = getCellValue(row.getCell(2));
                        Date timestampDate = row.getCell(0).getDateCellValue();
                        String timestamp = dateFormat.format(timestampDate)
                                + "T" + timeFormat.format(timestampDate);
                        String value = getCellValue(row.getCell(4));

                        JSONObject jsonObject = createJSONObject(id, name,
                                unit, timestamp, value);

                        int status = addMeasurement(serviceContext, userId,
                                jsonObject);

                        if (status == IGNORED) {
                            numIgnored++;
                        }

                        if (status == IMPORTED) {
                            numImported++;
                        }

                        numProcessed++;

                    }

                    i++;

                }

                workbook.close();
                fileInputStream.close();

            } catch (Exception e) {
                _log.error(e.getMessage());
                throw new MeasurementXLSException();
            }

        } else if ("json".equalsIgnoreCase(extension)) {

            _log.info("process json");

            try {
                String json = FileUtils.readFileToString(file,
                        Charset.defaultCharset());

                if (Validator.isNotNull(json)) {
                    
                    // Read preferences
                    
                    long ownerId = groupId;
                    int ownerType = PortletKeys.PREFS_OWNER_TYPE_GROUP;
                    long plid = 0;
                    
                    PortletPreferences portletPreferences = PortletPreferencesLocalServiceUtil.getPortletPreferences(ownerId,
                            ownerType, plid, "dataportlet_WAR_dataportlet");

                    javax.portlet.PortletPreferences preferences = PortletPreferencesFactoryUtil
                            .fromDefaultXML(portletPreferences.getPreferences());

                    String idFieldName = preferences.getValue("idField", "id");
                    String timestampFieldName = preferences.getValue("timestampField", "timestamp");

                    // Remove start and end quotes added by python's dump method
                    
                    if (json.startsWith("'") && json.endsWith("'")) {
                        
                        json = json.substring(1,json.length() - 1); 
                                                
                    }
                    
                    JSONArray jsonArray = JSONFactoryUtil.createJSONArray(json); 
                    
                    for (int i=0; i<jsonArray.length(); i++) {
                        
                        JSONObject inObject = jsonArray.getJSONObject(i);
                                                
                        String id = inObject.getString(idFieldName); 
                        String name = inObject.getString("id"); 
                        String unit = inObject.getString("unit"); 
                        String timestamp = inObject.getString(timestampFieldName); 
                        String value = inObject.getString("value"); 
                        
                        JSONObject jsonObject = createJSONObject(id, name,
                                unit, timestamp, value);

                        int status = addMeasurement(serviceContext, userId,
                                jsonObject);

                        if (status == IGNORED) {
                            numIgnored++;
                        }

                        if (status == IMPORTED) {
                            numImported++;
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
            JSONObject jsonObject) throws SystemException, PortalException {

        String id = jsonObject.getString("id");
        String name = jsonObject.getString("channelName");
        String timestamp = jsonObject.getString("timestamp");

        Hits hits = MeasurementLocalServiceUtil.search(
                serviceContext.getCompanyId(),
                serviceContext.getScopeGroupId(), id, name, timestamp, true, 0,
                1, null);

        if (hits.getLength() == 0) {

            MeasurementLocalServiceUtil.addMeasurement(userId,
                    jsonObject.toString(), serviceContext);

            return IMPORTED;

        } else {

            return IGNORED;
        }
    }

    private static JSONObject createJSONObject(String id, String name,
            String unit, String timestamp, String value) {

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
        jsonObject.put("channelId", id);
        jsonObject.put("channelName", name);
        jsonObject.put("channelUnit", unit);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("value", value);

        return jsonObject;

    }

    private String getCellValue(Cell cell) {

        return getCellValue(cell, false);

    }

    private String getCellValue(Cell cell, boolean useInt) {

        NumberFormat numberFormat = new DecimalFormat("0"); // omit

        String value = null;

        if (cell != null) {

            CellType cellType = cell.getCellTypeEnum();

            if (cellType.equals(CellType.STRING)) {
                value = cell.getStringCellValue();
            } else if (cellType.equals(CellType.NUMERIC)) {
                double val = cell.getNumericCellValue();

                if (useInt) {
                    value = String.valueOf(numberFormat.format(val));
                } else {
                    value = String.valueOf(val);
                }
            } else if (cellType.equals(CellType.BOOLEAN)) {
                value = String.valueOf(cell.getBooleanCellValue());
            } else {
                value = "Unsupported Celltype";
            }

        }

        return value;
    }

    private static int IGNORED = 0;
    private static int IMPORTED = 1;

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
