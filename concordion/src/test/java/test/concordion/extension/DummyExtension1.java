package test.concordion.extension;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.DocumentParsingListener;

public class DummyExtension1 extends DummyExtensionBase implements ConcordionExtension, DocumentParsingListener {

    public DummyExtension1() {
        super();
    }
    
    public DummyExtension1(String text) {
        super(text);
    }
    
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withDocumentParsingListener(this);
    }
}
