package ch.inofix.referencemanager.test;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalService;

/**
 * 
 * @author Christian Berndt
 *
 */
@Component(property = { "osgi.command.function=addreference",
        "osgi.command.scope=reference-manager" }, service = Object.class)
public class AddReferenceCommand {

    public int addreference() {
        
        int retval = 0;

        Reference reference = _referenceLocalService.createReference(0);

        long userId = 0;
        long groupId = 0;
        String bibTeX = "BibTeX";
        ServiceContext serviceContext = new ServiceContext();

        try {
            _referenceLocalService.addReference(userId, groupId, bibTeX, serviceContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retval;
    }

    @org.osgi.service.component.annotations.Reference
    private ReferenceLocalService _referenceLocalService;

}
