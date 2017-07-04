package ch.inofix.portlet.data.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.util.FileUtil;
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
 * @modified 2017-07-04 14:48
 * @version 1.0.6
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

            try {
                Document document = SAXReaderUtil.read(file);

                // TODO: read xPath selector from configuration
                String selector = "//ChannelData";
                List<Node> channels = document.selectNodes(selector);
                int numValues = document.selectNodes("//VT").size();

                for (Node channel : channels) {

                    Element channelElement = (Element) channel;

                    String channelId = channelElement
                            .attributeValue("channelId");
                    String channelName = channelElement.attributeValue("name");
                    String channelUnit = channelElement.attributeValue("unit");

                    List<Node> values = channel.selectNodes("descendant::VT");

                    for (Node value : values) {

                        Element valueElement = (Element) value;
                        String timestamp = valueElement.attributeValue("t");
                        String val = valueElement.getText();

                        JSONObject jsonObject = createJSONObject(channelId,
                                channelName, channelUnit, timestamp, val);

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

                        String channelId = getCellValue(row.getCell(3), true);
                        String channelName = getCellValue(row.getCell(1));
                        String channelUnit = getCellValue(row.getCell(2));
                        Date timestampDate = row.getCell(0).getDateCellValue();
                        String timestamp = dateFormat.format(timestampDate)
                                + "T" + timeFormat.format(timestampDate);
                        String value = getCellValue(row.getCell(4));


                        JSONObject jsonObject = createJSONObject(channelId,
                                channelName, channelUnit, timestamp, value);

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

        String channelId = jsonObject.getString("channelId");
        String channelName = jsonObject.getString("channelName");
        String timestamp = jsonObject.getString("timestamp");

        Hits hits = MeasurementLocalServiceUtil.search(
                serviceContext.getCompanyId(),
                serviceContext.getScopeGroupId(), channelId, channelName,
                timestamp, true, 0, 1, null);

        if (hits.getLength() == 0) {

            MeasurementLocalServiceUtil.addMeasurement(userId,
                    jsonObject.toString(), serviceContext);

            return IMPORTED;

        } else {

            return IGNORED;
        }
    }

    private static JSONObject createJSONObject(String channelId,
            String channelName, String channelUnit, String timestamp,
            String value) {

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
        jsonObject.put("channelId", channelId);
        jsonObject.put("channelName", channelName);
        jsonObject.put("channelUnit", channelUnit);
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

        return value;
    }

    private static int IGNORED = 0;
    private static int IMPORTED = 1;

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
