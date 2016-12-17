package ch.inofix.referencemanager.backgroundtask;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;

/**
 * @author Christian Berndt
 * @created 2016-12-17 13:11
 * @modified 2016-12-17 13:11
 * @version 1.0.0
 */
@Component(immediate = true, service = BackgroundTaskExecutorConfigurator.class)
public class BackgroundTaskExecutorConfigurator {

    @Activate
    protected void activate(BundleContext bundleContext) {

        BackgroundTaskExecutor referenceImportBackgroundTaskExecutor = new ReferenceImportBackgroundTaskExecutor();

        registerBackgroundTaskExecutor(bundleContext, referenceImportBackgroundTaskExecutor);

    }

    @Deactivate
    protected void deactivate() {

        for (ServiceRegistration<BackgroundTaskExecutor> serviceRegistration : _serviceRegistrations) {

            serviceRegistration.unregister();
        }
    }

    protected void registerBackgroundTaskExecutor(BundleContext bundleContext,
            BackgroundTaskExecutor backgroundTaskExecutor) {

        Dictionary<String, Object> properties = new HashMapDictionary<>();

        Class<?> clazz = backgroundTaskExecutor.getClass();

        properties.put("background.task.executor.class.name", clazz.getName());

        ServiceRegistration<BackgroundTaskExecutor> serviceRegistration = bundleContext
                .registerService(BackgroundTaskExecutor.class, backgroundTaskExecutor, properties);

        _serviceRegistrations.add(serviceRegistration);
    }

    private final Set<ServiceRegistration<BackgroundTaskExecutor>> _serviceRegistrations = new HashSet<>();

    private static final Log _log = LogFactoryUtil.getLog(BackgroundTaskExecutorConfigurator.class);

}
