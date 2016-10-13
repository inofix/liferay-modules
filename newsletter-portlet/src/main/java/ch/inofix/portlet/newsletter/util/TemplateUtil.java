package ch.inofix.portlet.newsletter.util;

import java.util.Map;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateConstants;
import com.liferay.taglib.util.VelocityTaglib;

/**
 * Based on the example of com.liferay.portal.templateparser.Transformer
 *
 * @author Christian Berndt
 * @created 2016-10-06 11:28
 * @modified 2016-10-13 19:23
 * @version 1.0.2
 */
public class TemplateUtil {

    /**
     * based on the example of com.liferay.portal.templateparser.Transformer
     *
     * @param contextObjects
     * @param script
     * @param langType
     * @return
     * @throws Exception
     */
    public static String transform(Map<String, Object> contextObjects,
            String script, String langType) throws Exception {

        if (Validator.isNull(langType)) {
            return null;
        }

        // TODO
        String templateId = StringUtil.randomId();

        Template template = getTemplate(templateId, script, langType);

        UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

        try {

            if (contextObjects != null) {
                for (String key : contextObjects.keySet()) {
                    template.put(key, contextObjects.get(key));
                }
            }

            mergeTemplate(template, unsyncStringWriter);

        } catch (Exception e) {
            throw new TransformException("Unhandled exception", e);
        }

        return unsyncStringWriter.toString();
    }

    protected static Template getTemplate(String templateId, String script,
            String langType) throws Exception {

        TemplateResource templateResource = new StringTemplateResource(
                templateId, script);

        TemplateResource errorTemplateResource = null;

        return TemplateManagerUtil.getTemplate(langType, templateResource,
                errorTemplateResource, _restricted);
    }

    protected static void mergeTemplate(Template template,
            UnsyncStringWriter unsyncStringWriter) throws Exception {

        VelocityTaglib velocityTaglib = (VelocityTaglib) template
                .get(PortletDisplayTemplateConstants.TAGLIB_LIFERAY);

        if (velocityTaglib != null) {
            velocityTaglib.setTemplate(template);
        }

        template.processTemplate(unsyncStringWriter);
    }

    private static Log _log = LogFactoryUtil.getLog(TemplateUtil.class);
    private static boolean _restricted = false;

}
