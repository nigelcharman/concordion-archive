package test.concordion.internal;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import org.concordion.internal.extension.ExtensionInitialisationException;
import org.concordion.internal.extension.FixtureExtensionLoader;
import org.junit.Test;

import test.concordion.JavaSourceCompiler;
import test.concordion.extension.DummyExtension1;
import test.concordion.extension.DummyExtension2;

@SuppressWarnings({"rawtypes","unchecked"})
public class FixtureExtensionLoaderTest {
    private JavaSourceCompiler compiler = new JavaSourceCompiler();
    private FixtureExtensionLoader loader = new FixtureExtensionLoader();

    @Test
    public void loadsPublicFieldWithExtensionAnnotation() throws Exception {
        String fields = 
            "@Extension " +
            "public DummyExtension1 extension = new DummyExtension1();";
        
        List extensions = loader.getExtensionsForFixture(compiledWithDeclaration(fields));
        
        assertThat((List<Object>)extensions, hasItem(instanceOf(DummyExtension1.class)));
    }
    
    @Test
    public void loadsPublicFieldFromSuperClassWithExtensionAnnotation() throws Exception {
        String superClassFields = 
            "@Extension " +
            "public ConcordionExtension extension = new DummyExtension2();";
        
        createAndCompile(superClassFields, "BaseFixture", null);
        List extensions = loader.getExtensionsForFixture(compiledWithDeclaration("", "BaseFixture"));
        
        assertThat((List<Object>)extensions, hasItem(instanceOf(DummyExtension2.class)));
    }
    
    @Test
    public void ignoresFieldsWithoutExtensionAnnotation() throws Exception {
        String fields = 
            "public ConcordionExtension extension = new DummyExtension1();";
        
        List extensions = loader.getExtensionsForFixture(compiledWithDeclaration(fields));
        
        assertThat(extensions.size(), equalTo(0));
    }

    @Test
    public void errorsIfPrivateFieldHasExtensionAnnotation() throws Exception {
        String fields = 
            "@Extension " +
            "private ConcordionExtension extension = new DummyExtension1();";
        
        try {
            loader.getExtensionsForFixture(compiledWithDeclaration(fields));
            fail("Expected ExtensionInitialisationException");
        } catch (ExtensionInitialisationException e) {
            assertThat(e.getMessage(), containsString("should be public"));
        }
    }
    
    @Test
    public void errorsIfProtectedFieldHasExtensionAnnotation() throws Exception {
        String fields = 
            "@Extension " +
            "protected ConcordionExtension extension = new DummyExtension1();";
        
        try {
            loader.getExtensionsForFixture(compiledWithDeclaration(fields));
            fail("Expected ExtensionInitialisationException");
        } catch (ExtensionInitialisationException e) {
            assertThat(e.getMessage(), containsString("should be public"));
        }
    }
    
    @Test
    public void errorsIfPackageAccessibleFieldHasExtensionAnnotation() throws Exception {
        String fields = 
            "@Extension " +
            "ConcordionExtension extension = new DummyExtension1();";
        
        try {
            loader.getExtensionsForFixture(compiledWithDeclaration(fields));
            fail("Expected ExtensionInitialisationException");
        } catch (ExtensionInitialisationException e) {
            assertThat(e.getMessage(), containsString("should be public"));
        }
    }
    
    @Test
    public void errorsIfFieldWithExtensionAnnotationIsNull() throws Exception {
        String fields = 
            "@Extension " +
            "public ConcordionExtension badExtension = null;";
        
        try {
            loader.getExtensionsForFixture(compiledWithDeclaration(fields));
            fail("Expected ExtensionInitialisationException");
        } catch (ExtensionInitialisationException e) {
            assertThat(e.getMessage(), containsString("should be non-null"));
        }
    }
    
    @Test
    public void errorsIfFieldWithExtensionAnnotationIsNotAConcordionExtension() throws Exception {
        String fields = 
            "@Extension " +
            "public String notAnExtension = \"foo\";";
        
        try {
            loader.getExtensionsForFixture(compiledWithDeclaration(fields));
            fail("Expected ExtensionInitialisationException");
        } catch (ExtensionInitialisationException e) {
            assertThat(e.getMessage(), containsString("must implement org.concordion.api.extension.ConcordionExtension"));
        }
    }
    
    private Object compiledWithDeclaration(String declaration) throws Exception, InstantiationException,
            IllegalAccessException {
        return compiledWithDeclaration(declaration, null);
    }

    private Object compiledWithDeclaration(String declaration, String superClassName) throws Exception, InstantiationException,
            IllegalAccessException {
        String className = "ExampleFixture";
        Class<?> clazz = createAndCompile(declaration, className, superClassName);
        Object fixture = clazz.newInstance();
        return fixture;
    }
    
    private Class<?> createAndCompile(String declaration, String className, String superClassName) throws Exception {
        String code = 
            "import org.concordion.api.extension.Extension;" +
            "import org.concordion.api.extension.ConcordionExtension;" +
            "import org.concordion.api.extension.ConcordionExtender;" +
            "import test.concordion.extension.*;" +
            "public class " + className + (superClassName != null ? " extends " + superClassName : "") + " {" +
            declaration +
            "}";    
        Class<?> clazz = compiler.compile(className, code);
        return clazz;
    }
}
