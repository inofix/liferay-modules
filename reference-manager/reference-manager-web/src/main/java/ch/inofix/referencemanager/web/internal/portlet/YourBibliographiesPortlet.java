package ch.inofix.referencemanager.web.internal.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import ch.inofix.referencemanager.constants.PortletKeys;

/**
 * View Controller of Inofix' your-bibliographies-portlet
 * 
 * @author Christian Berndt
 * @created 2016-12-01 02:02
 * @modified 2016-12-01 02:02
 * @version 1.0.0
 */
@Component(
    immediate = true, 
    property = { 
        "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.css-class-wrapper=your-bibliographies-portlet",
        "com.liferay.portlet.display-category=category.inofix", "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.instanceable=false", "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/your_bibliographies.jsp",
        "javax.portlet.name=" + PortletKeys.YOUR_BIBLIOGRAPHIES, "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" 
    }, 
service = Portlet.class)

public class YourBibliographiesPortlet extends MVCPortlet {

}
