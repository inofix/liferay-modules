
package ch.inofix.hook.fmtools.spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.XmlWebApplicationContext;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;

/**
 * Based on https://github.com/limburgie/velocity-bean-locator.
 * 
 * This context listener initializes a new ApplicationContext and replaces the
 * default BeanLocator by a VelocityBeanLocator, allowing $utilLocator to
 * retrieve any bean registered on the classpath of the web application this
 * context listener is invoked for. To enable the context listener, configure
 * the following in the web.xml of an empty hook project: <pre> <listener>
 * <listener
 * -class>ch.inofix.hook.fmtools.spring.VelocityBeanLocatorContextListener
 * </listener -class> </listener> <context-param>
 * <param-name>contextConfigLocation</param-name>
 * <param-value>classpath*:applicationContext.xml</param-value> </context-param>
 * </pre> Note: the contextConfigLocation value can point to any context
 * location.
 */
public class VelocityBeanLocatorContextListener
    implements ServletContextListener {

    private static final String CONTEXT_CONFIG_LOCATION =
        "contextConfigLocation";

    private XmlWebApplicationContext ctx;

    public VelocityBeanLocatorContextListener() {

        ctx = new XmlWebApplicationContext();
    }

    public void contextInitialized(ServletContextEvent sce) {

        initializeContext(sce);
        String servletContextName =
            sce.getServletContext().getServletContextName();

        PortletBeanLocatorUtil.setBeanLocator(
            servletContextName, new VelocityBeanLocator());
    }

    private void initializeContext(ServletContextEvent sce) {

        ctx.setServletContext(sce.getServletContext());
        ctx.setConfigLocation(sce.getServletContext().getInitParameter(
            CONTEXT_CONFIG_LOCATION));
        ctx.refresh();
    }

    public void contextDestroyed(ServletContextEvent sce) {

        ctx.close();
    }

}
