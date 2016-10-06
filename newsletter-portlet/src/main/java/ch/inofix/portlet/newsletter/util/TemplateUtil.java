
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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateConstants;
import com.liferay.taglib.util.VelocityTaglib;

/**
 * Based on the example of com.liferay.portal.templateparser.Transformer
 *
 * @author Christian Berndt
 * @created 2016-10-06 11:28
 * @modified 2016-10-06 11:28
 * @version 1.0.0
 */
public class TemplateUtil {

    /**
     * based on the example of com.liferay.portal.templateparser.Transformer
     *
     * @param themeDisplay
     * @param contextObjects
     * @param script
     * @param langType
     * @return
     * @throws Exception
     */
    public static String transform(
        ThemeDisplay themeDisplay, Map<String, Object> contextObjects,
        String script, String langType)
        throws Exception {

        if (Validator.isNull(langType)) {
            return null;
        }

        long companyId = 0;
        long companyGroupId = 0;
        long scopeGroupId = 0;
        long siteGroupId = 0;

        if (themeDisplay != null) {
            companyId = themeDisplay.getCompanyId();
            companyGroupId = themeDisplay.getCompanyGroupId();
            scopeGroupId = themeDisplay.getScopeGroupId();
            siteGroupId = themeDisplay.getSiteGroupId();
        }

        String templateId = String.valueOf(contextObjects.get("template_id"));

        templateId =
            getTemplateId(templateId, companyId, companyGroupId, scopeGroupId);

        Template template = getTemplate(templateId, script, langType);

        UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

        try {
            prepareTemplate(themeDisplay, template);

            if (contextObjects != null) {
                for (String key : contextObjects.keySet()) {
                    template.put(key, contextObjects.get(key));
                }
            }

            template.put(
                "permissionChecker",
                PermissionThreadLocal.getPermissionChecker());
            template.put("randomNamespace", StringUtil.randomId() +
                StringPool.UNDERLINE);
            template.put("scopeGroupId", scopeGroupId);
            template.put("siteGroupId", siteGroupId);

            mergeTemplate(template, unsyncStringWriter);
        }
        catch (Exception e) {
            throw new TransformException("Unhandled exception", e);
        }

        return unsyncStringWriter.toString();
    }

    protected static Template getTemplate(
        String templateId, String script, String langType)
        throws Exception {

        TemplateResource templateResource =
            new StringTemplateResource(templateId, script);

        TemplateResource errorTemplateResource = null;

        return TemplateManagerUtil.getTemplate(
            langType, templateResource, errorTemplateResource, _restricted);
    }

    protected static String getTemplateId(
        String templateId, long companyId, long companyGroupId, long groupId) {

        StringBundler sb = new StringBundler(5);

        sb.append(companyId);
        sb.append(StringPool.POUND);

        if (companyGroupId > 0) {
            sb.append(companyGroupId);
        }
        else {
            sb.append(groupId);
        }

        sb.append(StringPool.POUND);
        sb.append(templateId);

        return sb.toString();
    }

    protected static void mergeTemplate(
        Template template, UnsyncStringWriter unsyncStringWriter)
        throws Exception {

        VelocityTaglib velocityTaglib =
            (VelocityTaglib) template.get(PortletDisplayTemplateConstants.TAGLIB_LIFERAY);

        if (velocityTaglib != null) {
            velocityTaglib.setTemplate(template);
        }

        template.processTemplate(unsyncStringWriter);
    }

    protected static void prepareTemplate(
        ThemeDisplay themeDisplay, Template template)
        throws Exception {

        if (themeDisplay == null) {
            return;
        }

        template.prepare(themeDisplay.getRequest());
    }

    private static Log _log = LogFactoryUtil.getLog(TemplateUtil.class);
    private static boolean _restricted = false;

}
