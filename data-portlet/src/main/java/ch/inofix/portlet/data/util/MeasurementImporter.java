package ch.inofix.portlet.data.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-03-09 17:53
 * @modified 2017-03-09 17:53
 * @version 1.0.0
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

            _log.info(document.asXML());


//            _log.info("Import took " + stopWatch.getTime() + " ms");
//            _log.info("Processed " + numProcessed + " cards.");
//            _log.info("Imported " + numImported + " cards.");
//            _log.info("Ignored " + numIgnored + " cards.");
//            _log.info("Updated " + numUpdated + " cards.");

        } catch (DocumentException de) {
            _log.error(de);
        }

    }

    private static Log _log = LogFactoryUtil.getLog(MeasurementImporter.class
            .getName());

}
