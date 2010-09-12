package spec.concordion.extension;

public class CSSExtensionTest extends AbstractExtensionTestCase {

    public void addCSSExtension() {
        setExtensions("test.concordion.extension.CSSExtension");
    }

    public boolean hasCSSDeclaration(String cssFilename) {
        return getProcessingResult().hasCSSDeclaration(cssFilename);
    }
}
