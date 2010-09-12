package org.concordion.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.concordion.Concordion;
import org.concordion.api.Command;
import org.concordion.api.EvaluatorFactory;
import org.concordion.api.Resource;
import org.concordion.api.Source;
import org.concordion.api.SpecificationLocator;
import org.concordion.api.SpecificationReader;
import org.concordion.api.Target;
import org.concordion.internal.command.AssertEqualsCommand;
import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertFalseCommand;
import org.concordion.internal.command.AssertFalseListener;
import org.concordion.internal.command.AssertTrueCommand;
import org.concordion.internal.command.AssertTrueListener;
import org.concordion.internal.command.EchoCommand;
import org.concordion.internal.command.ExecuteCommand;
import org.concordion.internal.command.ExecuteListener;
import org.concordion.internal.command.LocalTextDecorator;
import org.concordion.internal.command.RunCommand;
import org.concordion.internal.command.SetCommand;
import org.concordion.internal.command.SpecificationCommand;
import org.concordion.internal.command.SpecificationProcessingListener;
import org.concordion.internal.command.ThrowableCatchingDecorator;
import org.concordion.internal.command.ThrowableCaughtListener;
import org.concordion.internal.command.ThrowableCaughtPublisher;
import org.concordion.internal.command.VerifyRowsCommand;
import org.concordion.internal.command.VerifyRowsListener;
import org.concordion.internal.listener.AssertResultRenderer;
import org.concordion.internal.listener.BreadcrumbRenderer;
import org.concordion.internal.listener.DocumentStructureImprover;
import org.concordion.internal.listener.PageFooterRenderer;
import org.concordion.internal.listener.RunResultRenderer;
import org.concordion.internal.listener.SpecificationExporter;
import org.concordion.internal.listener.StylesheetEmbedder;
import org.concordion.internal.listener.StylesheetLinker;
import org.concordion.internal.listener.ThrowableRenderer;
import org.concordion.internal.listener.VerifyRowsResultRenderer;
import org.concordion.internal.util.Check;
import org.concordion.internal.util.IOUtil;

public class ConcordionBuilder implements ConcordionExtender {

    public static final String NAMESPACE_CONCORDION_2007 = "http://www.concordion.org/2007/concordion";
    private static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";
    private static final String PROPERTY_EXTENSIONS = "concordion.extensions";
    private static final String EMBEDDED_STYLESHEET_RESOURCE = "/org/concordion/internal/resource/embedded.css";
    
    private SpecificationLocator specificationLocator = new ClassNameBasedSpecificationLocator();
    private Source source = new ClassPathSource();
    private Target target = null;
    private CommandRegistry commandRegistry = new CommandRegistry();
    private DocumentParser documentParser = new DocumentParser(commandRegistry);
    private SpecificationReader specificationReader;
    private EvaluatorFactory evaluatorFactory = new SimpleEvaluatorFactory();
    private SpecificationCommand specificationCommand = new SpecificationCommand();
    private AssertEqualsCommand assertEqualsCommand = new AssertEqualsCommand();
    private AssertTrueCommand assertTrueCommand = new AssertTrueCommand();
    private AssertFalseCommand assertFalseCommand = new AssertFalseCommand();
    private ExecuteCommand executeCommand = new ExecuteCommand();
    private RunCommand runCommand = new RunCommand();
    private VerifyRowsCommand verifyRowsCommand = new VerifyRowsCommand();
    private EchoCommand echoCommand = new EchoCommand();
    private File baseOutputDir;
    private ThrowableCaughtPublisher throwableListenerPublisher = new ThrowableCaughtPublisher();
    private LinkedHashMap<String, Resource> resourceToCopyMap = new LinkedHashMap<String, Resource>();
    
    {
        withThrowableListener(new ThrowableRenderer());
        
        commandRegistry.register("", "specification", specificationCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "run", runCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "execute", executeCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "set", new SetCommand());
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertEquals", assertEqualsCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertTrue", assertTrueCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertFalse", assertFalseCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "verifyRows", verifyRowsCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "echo", echoCommand);
        
        AssertResultRenderer assertRenderer = new AssertResultRenderer();
        withAssertEqualsListener(assertRenderer);
        withAssertTrueListener(assertRenderer);
        withAssertFalseListener(assertRenderer);
        withVerifyRowsListener(new VerifyRowsResultRenderer());
        withRunListener(new RunResultRenderer());
        withDocumentParsingListener(new DocumentStructureImprover());
        String stylesheetContent = IOUtil.readResourceAsString(EMBEDDED_STYLESHEET_RESOURCE);
        withDocumentParsingListener(new StylesheetEmbedder(stylesheetContent));
    }

    public ConcordionBuilder withSource(Source source) {
        this.source = source;
        return this;
    }

    public ConcordionBuilder withTarget(Target target) {
        this.target = target;
        return this;
    }

    public ConcordionBuilder withEvaluatorFactory(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
        return this;
    }
    
    public ConcordionBuilder withThrowableListener(ThrowableCaughtListener throwableListener) {
        throwableListenerPublisher.addThrowableListener(throwableListener);
        return this;
    }

    public ConcordionBuilder withAssertEqualsListener(AssertEqualsListener listener) {
        assertEqualsCommand.addAssertEqualsListener(listener);
        return this;
    }
    
    public ConcordionBuilder withAssertTrueListener(AssertTrueListener listener) {
        assertTrueCommand.addAssertListener(listener);
        return this;
    }
    
    public ConcordionBuilder withAssertFalseListener(AssertFalseListener listener) {
        assertFalseCommand.addAssertListener(listener);
        return this;
    }
    
    public ConcordionBuilder withVerifyRowsListener(VerifyRowsListener listener) {
        verifyRowsCommand.addVerifyRowsListener(listener);
        return this;
    }
    
    public ConcordionBuilder withRunListener(RunListener listener) {
        runCommand.addRunListener(listener);
        return this;
    }

    public ConcordionBuilder withExecuteListener(ExecuteListener listener) {
        executeCommand.addExecuteListener(listener);
        return this;
    }

    public ConcordionBuilder withDocumentParsingListener(DocumentParsingListener listener) {
        documentParser.addDocumentParsingListener(listener);
        return this;
    }

    public ConcordionBuilder withSpecificationProcessingListener(SpecificationProcessingListener listener) {
        specificationCommand.addSpecificationListener(listener);
        return this;
    }

    private ConcordionBuilder withApprovedCommand(String namespaceURI, String commandName, Command command) {
        ThrowableCatchingDecorator throwableCatchingDecorator = new ThrowableCatchingDecorator(new LocalTextDecorator(command));
        throwableCatchingDecorator.addThrowableListener(throwableListenerPublisher);
        Command decoratedCommand = throwableCatchingDecorator;
        commandRegistry.register(namespaceURI, commandName, decoratedCommand);
        return this;
    }

    public ConcordionBuilder withCommand(String namespaceURI, String commandName, Command command) {
        Check.notEmpty(namespaceURI, "Namespace URI is mandatory");
        Check.notEmpty(commandName, "Command name is mandatory");
        Check.notNull(command, "Command is null");
        Check.isFalse(namespaceURI.contains("concordion.org"),
                "The namespace URI for user-contributed command '" + commandName + "' "
              + "must not contain 'concordion.org'. Use your own domain name instead.");
        return withApprovedCommand(namespaceURI, commandName, command);
    }
    
    public ConcordionBuilder withResource(String sourcePath, Resource targetResource) {
        resourceToCopyMap.put(sourcePath, targetResource);
        return this;
    }

    public ConcordionBuilder withCSS(String sourcePath, Resource targetResource) {
        withResource(sourcePath, targetResource);
        StylesheetLinker cssLinker = new StylesheetLinker(targetResource);
        withDocumentParsingListener(cssLinker);
        withSpecificationProcessingListener(cssLinker);
        return this;
    }
    
    public Concordion build() {
        if (target == null) {
            target = new FileTarget(getBaseOutputDir());
        }
        XMLParser xmlParser = new XMLParser();
        
        specificationCommand.addSpecificationListener(new BreadcrumbRenderer(source, xmlParser));
        specificationCommand.addSpecificationListener(new PageFooterRenderer(target));
        specificationCommand.addSpecificationListener(new SpecificationExporter(target));

        specificationReader = new XMLSpecificationReader(source, xmlParser, documentParser);        

        addExtensions();
        copyResources();
        
        return new Concordion(specificationLocator, specificationReader, evaluatorFactory);
    }

    private void copyResources() {
        for (Entry<String, Resource> resourceToCopy : resourceToCopyMap.entrySet()) {
            String sourcePath = resourceToCopy.getKey();
            Resource targetResource = resourceToCopy.getValue();
            InputStream inputStream = getClass().getResourceAsStream(sourcePath);
            if (inputStream == null) {
                throw new RuntimeException("Unable to find resource "+ sourcePath + " to copy");
            }
            try {
                target.copyTo(targetResource, inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy " + sourcePath + " to target " + targetResource, e);
            }
        }
    }

    private void addExtensions() {
        String extensionProp = System.getProperty(PROPERTY_EXTENSIONS);
        if (extensionProp != null) {
            String[] extensions = extensionProp.split("\\s*,\\s*");
            for (String className : extensions) {
                addExtension(className);
            }
        }
    }

    private void addExtension(String className) {
        Class<?> extensionClass;
        try {
            extensionClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find extension '" + className + "' on classpath", e);
        }
        Object extensionObject;
        try {
            extensionObject = extensionClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate extension '" + className + "'", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Extension '" + className + "' or constructor are inaccessible", e);
        }

        ConcordionExtension extension;
        try {
            extension = (ConcordionExtension) extensionObject;
        } catch (ClassCastException e) {
            throw new RuntimeException("Extension '" + className + "' must implement ConcordionExtension", e);
        }
        extension.addTo(this);
    }

    private File getBaseOutputDir() {
        if (baseOutputDir != null) {
            return baseOutputDir;
        }
        String outputPath = System.getProperty(PROPERTY_OUTPUT_DIR);
        if (outputPath == null) {
            return new File(System.getProperty("java.io.tmpdir"), "concordion");
        }
        return new File(outputPath);
    }
}
