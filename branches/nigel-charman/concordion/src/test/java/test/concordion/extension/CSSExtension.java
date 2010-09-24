package test.concordion.extension;

import org.concordion.api.Resource;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class CSSExtension implements ConcordionExtension {

    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withCSS("/test/concordion/my.css", as("/css/my.css"));
    }

    private Resource as(String path) {
        return new Resource(path);
    }
}
