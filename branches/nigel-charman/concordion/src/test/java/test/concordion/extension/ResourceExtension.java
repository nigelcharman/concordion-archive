package test.concordion.extension;

import org.concordion.api.Resource;
import org.concordion.internal.ConcordionExtender;
import org.concordion.internal.ConcordionExtension;

public class ResourceExtension implements ConcordionExtension {

    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withResource("/test/concordion/o.png", at("/images/o.png"));
    }

    private Resource at(String path) {
        return new Resource(path);
    }
}
