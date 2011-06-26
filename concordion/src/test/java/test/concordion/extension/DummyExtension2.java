package test.concordion.extension;


import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.DocumentParsingListener;

public class DummyExtension2 extends DummyExtensionBase implements ConcordionExtension, DocumentParsingListener {

    public DummyExtension2() {
        super();
    }
    
    public DummyExtension2(String text) {
        super(text);
    }
    
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withDocumentParsingListener(this);
    }
}
