
package ch.inofix.hook.fmtools.spring;

import com.liferay.portal.kernel.bean.BeanLocatorException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Based on https://github.com/limburgie/velocity-bean-locator.
 * 
 * Implementation of Liferay's bean locator that uses the BeanLocator of Literay
 * Spring to retrieve beans. This bean locator should only be used in the
 * context of Velocity templates.
 */
@Component
public class VelocityBeanLocator
    implements com.liferay.portal.kernel.bean.BeanLocator {

    private static final String VELOCITY_SUFFIX = ".velocity";

    public Object locate(String name) {

        String realName = stripVelocitySuffix(name);
        return SpringBeanLocator.getBean(realName);
    }

    private String stripVelocitySuffix(String name) {

        String realName = name;
        if (realName.endsWith(VELOCITY_SUFFIX)) {
            realName =
                realName.substring(
                    0, realName.length() - VELOCITY_SUFFIX.length());
        }
        return realName;
    }

    public String[] getNames() {

        return new String[0];
    }

    public ClassLoader getClassLoader() {

        throw new UnsupportedOperationException();
    }

    public Class<?> getType(String name)
        throws BeanLocatorException {

        throw new UnsupportedOperationException();
    }

    public <T> Map<String, T> locate(Class<T> clazz)
        throws BeanLocatorException {

        throw new UnsupportedOperationException();
    }

}
