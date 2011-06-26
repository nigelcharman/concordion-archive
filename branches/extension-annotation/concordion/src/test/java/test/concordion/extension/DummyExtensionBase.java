package test.concordion.extension;

import nu.xom.Document;
import nu.xom.Element;

public abstract class DummyExtensionBase {

    private final String text;

    public DummyExtensionBase() {
        text = this.getClass().getSimpleName();
    }
    
    public DummyExtensionBase(String text) {
        this.text = text;
    }

    public void beforeParsing(Document document) {
        Element fragment = document.getRootElement().getFirstChildElement("body").getFirstChildElement("fragment");
        String newText = getText();
        if ("" != fragment.getValue()) {
            newText = ", " + newText;
        }
        fragment.appendChild(newText);
    }

    private String getText() {
        return text;
    }

}
