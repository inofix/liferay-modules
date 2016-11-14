package ch.inofix.referencemanager.test;

import org.osgi.service.component.annotations.Component;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalService;

/**
 * 
 * @author Christian Berndt
 *
 */
@Component(
        property = {"osgi.command.function=addreference", "osgi.command.scope=reference-manager"},
        service = Object.class
    )
public class AddReferenceCommand {
    
    public int addreference() {
        int retval = 0;
        
        Reference reference = _referenceLocalService.createReference(0);
        
        reference.setBibtex("bibtex");
        reference.isNew();

        _referenceLocalService.addReferenceWithoutId(reference);
        
        return retval;
    }

    @org.osgi.service.component.annotations.Reference
    private  ReferenceLocalService _referenceLocalService;

}
