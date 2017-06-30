package ch.inofix.portlet.data.util;

import java.io.File;
import java.io.FileInputStream;
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
 * @modified 2017-06-30 14:29
 * @version 1.0.5
 *
 */
public class MeasurementImporter {

    public void importMeasurements(long userId, long groupId,
            boolean privateLayout, Map<String, String[]> parameterMap, File file)
            throws PortalException, SystemException {

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        User user = UserLocalServiceUtil.getUser(userId);
        serviceContext.setCompanyId(user.getCompanyId());

        int numProcessed = 0;
        int numImported = 0;
        int numIgnored = 0;
        int numUpdated = 0;

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        String extension = FileUtil.getExtension(file.getName());

        _log.info(file.getName());
        _log.info("extension = " + extension);

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

                        Hits hits = MeasurementLocalServiceUtil.search(
                                serviceContext.getCompanyId(),
                                serviceContext.getScopeGroupId(), channelId,
                                channelName, timestamp, true, 0,
                                Integer.MAX_VALUE, null);

                        if (hits.getLength() == 0) {

                            MeasurementLocalServiceUtil.addMeasurement(userId,
                                    jsonObject.toString(), serviceContext);

                            numImported++;

                        } else {
                            numIgnored++;
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

            try {

                FileInputStream fileInputStream = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
                HSSFSheet worksheet = workbook.getSheetAt(0);

                int i = 0;

                for (Row row : worksheet) {

                    if (i > 0) {

                        String channelId = getCellValue(row.getCell(3));
                        String channelName = getCellValue(row.getCell(1));
                        String channelUnit = getCellValue(row.getCell(2));
                        String timestamp = getCellValue(row.getCell(0));
                        String value = getCellValue(row.getCell(4));

                        _log.info("channelId = " + channelId);
                        _log.info("channelName = " + channelName);
                        _log.info("channelUnit = " + channelUnit);
                        _log.info("timestampDate = " + timestamp);
                        _log.info("value = " + value);

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
        _log.info("Updated " + numUpdated + " measurements.");

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

        String value = null;

        CellType cellType = cell.getCellTypeEnum();

        if (cellType.equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else if (cellType.equals(CellType.NUMERIC)) {
            value = String.valueOf(cell.getNumericCellValue());
        } else if (cellType.equals(CellType.BOOLEAN)) {
            value = String.valueOf(cell.getBooleanCellValue());
        } else {
            value = "Unsupported Celltype";
        }

        return value;

    }

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
