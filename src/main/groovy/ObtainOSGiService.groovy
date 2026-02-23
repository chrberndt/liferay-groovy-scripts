import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.FrameworkUtil
import org.osgi.util.tracker.ServiceTracker

static <T> T getService(Class<T> clazz) {
    Bundle bundle = FrameworkUtil.getBundle(clazz)
    BundleContext bundleContext = bundle.getBundleContext()
    ServiceTracker serviceTracker = new ServiceTracker(bundleContext, clazz, null)
    serviceTracker.open()
    T service = (T) serviceTracker.waitForService(500)
    serviceTracker.close()
    return service
}

configurationHelper = getService(SamlProviderConfigurationHelper)

out.println(configurationHelper.enabled)
out.println(configurationHelper.roleSp)

