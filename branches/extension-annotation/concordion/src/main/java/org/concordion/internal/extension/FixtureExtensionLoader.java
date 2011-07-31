package org.concordion.internal.extension;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.extension.Extension;
import org.concordion.internal.ConcordionBuilder;

public class FixtureExtensionLoader implements ExtensionLoader {
    
    public void addExtensions(final Object fixture, ConcordionBuilder concordionBuilder) {
        for (ConcordionExtension concordionExtension : getExtensionsForFixture(fixture)) {
            concordionExtension.addTo(concordionBuilder);
        }
    }

    public List<ConcordionExtension> getExtensionsForFixture(Object fixture) {
        final List<ConcordionExtension> extensions = new ArrayList<ConcordionExtension>();

        List<Class<?>> classes = getClassHeirarchyParentFirst(fixture.getClass());
        
        for (Class<?> class1 : classes) {
            extensions.addAll(getExtensionsForFixtureFromClass(fixture, class1));
        }
        
        return extensions;
    }

    private List<ConcordionExtension> getExtensionsForFixtureFromClass(Object fixture, Class<?> class1) {
        List<ConcordionExtension> extensions = new ArrayList<ConcordionExtension>();
        Field[] declaredFields = class1.getDeclaredFields();      
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Extension.class)) {
                validatePublic(field);
                ConcordionExtension extension = getExtension(fixture, field);
                validateNonNull(field, extension);
                extensions.add(extension);
            }
        }
        return extensions;
    }

    /**
     * Returns the specified class and all of its superclasses, excluding java.lang.Object,
     * ordered from the most super class to the specified class.
     */
    private List<Class<?>> getClassHeirarchyParentFirst(Class<?> class1) {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> current = class1;
        while (current != null && current != Object.class) {
            classes.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(classes);
        return classes;
    }
    
    private ConcordionExtension getExtension(Object fixture, Field field) {
        try {
            return (ConcordionExtension) field.get(fixture);
        } catch (ClassCastException e) {
            throw new ExtensionInitialisationException("Extension field '" + field.getName() + "' must implement org.concordion.api.extension.ConcordionExtension");
        } catch (IllegalArgumentException e) {
            throw new ExtensionInitialisationException("Defect - this exception should not occur. Please report to Concordion issues list.", e);
        } catch (IllegalAccessException e) {
            throw new ExtensionInitialisationException("Defect - this exception should not occur. Please report to Concordion issues list.", e);
        }
    }

    private void validatePublic(Field field) {
        if (!(Modifier.isPublic(field.getModifiers()))) {
            throw new ExtensionInitialisationException("Extension field '" + field.getName() + "' must be public");
        }
    }
    
    private void validateNonNull(Field field, ConcordionExtension extension) {
        if (extension == null) {
            throw new ExtensionInitialisationException("Extension field '" + field.getName() + "' must be non-null");
        }
    }
}