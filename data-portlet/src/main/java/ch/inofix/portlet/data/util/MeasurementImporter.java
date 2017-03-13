package ch.inofix.portlet.data.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

import ch.inofix.portlet.data.model.Measurement;
import ch.inofix.portlet.data.service.MeasurementLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 17:53
 * @modified 2017-03-13 15:39
 * @version 1.0.1
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

        try {

            int numProcessed = 0;
            int numImported = 0;
            int numIgnored = 0;
            int numUpdated = 0;

            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            Document document = SAXReaderUtil.read(file);

            // TODO: read xPath selector from configuration
            List<Node> nodes = document
                    .selectNodes("//ch.inofix.portlet.timetracker.model.impl.TaskRecordImpl");

            _log.info("nodes.size() = " + nodes.size());

            for (Node node : nodes) {

                // TODO: Store node data in database and index.
                Measurement measurement = MeasurementLocalServiceUtil
                        .addMeasurement(userId, node.asXML(), serviceContext);

                if (numProcessed % 100 == 0 && numProcessed > 0) {

                    float completed = ((Integer) numProcessed).floatValue()
                            / ((Integer) nodes.size()).floatValue() * 100;

                    _log.info("Processed " + numProcessed + " of "
                            + nodes.size() + " measurements in "
                            + stopWatch.getTime() + " ms (" + completed + "%).");
                }

                numProcessed++;
            }

            _log.info("Import took " + stopWatch.getTime() + " ms");
            _log.info("Processed " + numProcessed + " measurements.");
            _log.info("Imported " + numImported + " measurements.");
            _log.info("Ignored " + numIgnored + " measurements.");
            _log.info("Updated " + numUpdated + " measurements.");

        } catch (DocumentException de) {
            _log.error(de);
        }

    }

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
