
package ch.inofix.hook.fmtools.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Based on https://github.com/limburgie/velocity-bean-locator.
 * 
 * @author Christian Berndt
 */
@Component
public class SpringBeanLocator implements ApplicationContextAware {

    private static ApplicationContext ctx;

    /**
     * Returns the bean of the specified full qualified class name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String className) {

        try {
            return (T) ctx.getBean(Class.forName(className));
        }
        catch (BeansException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {

        ctx = applicationContext;
    }

}
