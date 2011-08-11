package test.concordion.extension.fake;

import nu.xom.Document;
import nu.xom.Element;

public abstract class FakeExtensionBase {

    private final String text;

    public FakeExtensionBase() {
        text = this.getClass().getSimpleName();
    }
    
    public FakeExtensionBase(String text) {
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
