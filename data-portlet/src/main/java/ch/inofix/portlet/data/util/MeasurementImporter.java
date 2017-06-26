package ch.inofix.portlet.data.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
 * @modified 2017-06-26 10:39
 * @version 1.0.4
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

                        JSONObject jsonObject = JSONFactoryUtil
                                .createJSONObject();
                        jsonObject.put("channelId", channelId);
                        jsonObject.put("channelName", channelName);
                        jsonObject.put("channelUnit", channelUnit);
                        jsonObject.put("timestamp", timestamp);
                        jsonObject.put("value", val);

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
                HSSFRow row1 = worksheet.getRow(0);
                HSSFCell cellA1 = row1.getCell((short) 0);
                String a1Val = cellA1.getStringCellValue();
                HSSFCell cellB1 = row1.getCell((short) 1);
                String b1Val = cellB1.getStringCellValue();
                HSSFCell cellC1 = row1.getCell((short) 2);
//                boolean c1Val = cellC1.getBooleanCellValue();
                HSSFCell cellD1 = row1.getCell((short) 3);
//                Date d1Val = cellD1.getDateCellValue();

                System.out.println("A1: " + a1Val);
                System.out.println("B1: " + b1Val);

                
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

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
